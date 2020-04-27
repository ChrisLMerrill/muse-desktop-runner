package org.museautomation.runner.projects

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectUpdater(private val download_settings: DownloadableProjectSettings, private val path: String)
{
    private val _client = OkHttpClient.Builder().build()
    var _download_unversioned = false
    var _downloaded_project_file: File? = null

    fun checkForUpdates(): UpdateCheckResult
    {
        try
        {
            var request = Request.Builder().head().url(download_settings.url).build()
            var response = _client.newCall(request).execute()
            if (!response.isSuccessful)
                return UpdateCheckResult(false, response.message())

            val content_type = response.header("Content-Type")
                    ?: return UpdateCheckResult(false, "Response is missing Content-Type header")

            if (content_type.contains("json"))
            {
                request = Request.Builder().get().url(download_settings.url).build()
                response = _client.newCall(request).execute()
                if (!response.isSuccessful)
                    return UpdateCheckResult(false, response.message())
                val body = response.body()
                        ?: return UpdateCheckResult(false, "Response body is missing")
                val download_spec = ObjectMapper().readValue(body.string(), DownloadableProject::class.java)
                download_settings.spec = download_spec

                // TODO determine if newer version is available
            }
            if (content_type.contains("application/zip"))
                _download_unversioned = true
            else
            {
                return UpdateCheckResult(false, "unexpected content type - zip file?")
            }

            return UpdateCheckResult(true, null)
        } catch (e: Exception)
        {
            LOG.error("Unable to check for updates", e)
            return UpdateCheckResult(false, "Unexpected failure")
        }
    }

    fun updateUnversioned(): UpdateResult
    {
        var result = prepareFolder()
        if (!result.success)
            return result

        result = downloadProject(download_settings.url)
        if (!result.success)
            return result

        val file = _downloaded_project_file
        if (file == null)
        {
            LOG.error("Download reported success, but file location not set")
            return UpdateResult(false, "Download failed")
        }

        result = unpackIntoTarget(file, path)
        _downloaded_project_file = null
        return result
    }

    private fun prepareFolder(): UpdateResult
    {
        val folder = File(path)
        if (folder.exists())
            try
            {
                FileUtils.cleanDirectory(folder)
            } catch (e: IOException)
            {
                return UpdateResult(false, "Unable to cleanup folder " + path)
            }
        else if (!folder.mkdirs())
            return UpdateResult(false, "Unable to create folder " + path)

        return UpdateResult(true, null)
    }

    private fun downloadProject(url: String): UpdateResult
    {
        val request = Request.Builder().get().url(url).build()
        val response = _client.newCall(request).execute()
        if (!response.isSuccessful)
            return UpdateResult(false, response.message())
        val body = response.body()
        if (body == null)
            return UpdateResult(false, response.message())

        val file = File.createTempFile("mrd", null)
        val sink = Okio.buffer(Okio.sink(file))
        sink.writeAll(body.source())
        sink.close()

        _downloaded_project_file = file

        return UpdateResult(true, null)
    }

    private fun unpackIntoTarget(file: File, path: String): UpdateResult
    {
        try
        {
            ZipFile(file).use({ zipFile ->
                val entries: Enumeration<out ZipEntry> = zipFile.entries()
                while (entries.hasMoreElements())
                {
                    val entry = entries.nextElement()
                    val entryDestination = File(path, entry.name)
                    if (entry.isDirectory)
                        entryDestination.mkdirs()
                    else
                    {
                        entryDestination.parentFile.mkdirs()
                        zipFile.getInputStream(entry).use({ `in` -> FileOutputStream(entryDestination).use { out -> IOUtils.copy(`in`, out) } })
                    }
                }
            })
        }
        catch (e: IOException)
        {
            LOG.error("Unable to unpack zip file", e)
            return UpdateResult(false, "Unable to unpack project")
        }
        file.delete()
        return UpdateResult(true, null)
    }

    companion object
    {
        private val LOG = LoggerFactory.getLogger(ProjectUpdater::class.java)
    }
}

class UpdateCheckResult(val success: Boolean, val message: String?)
class UpdateResult(val success: Boolean, val message: String?)