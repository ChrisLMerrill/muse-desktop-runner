package org.museautomation.runner.projects

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectUpdater(private val download_settings: DownloadableProjectSettings)
{
    private val _client = OkHttpClient.Builder().build()
    private var _raw_download = false

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
            }
            else
            {
                _raw_download = true
                return UpdateCheckResult(false, "unexpected content type - zip file?")
            }

            return UpdateCheckResult(true, null)
        }
        catch (e: Exception)
        {
            LOG.error("Unable to check for updates", e)
            return UpdateCheckResult(false, "Unexpected failure")
        }
    }

    companion object
    {
        private val LOG = LoggerFactory.getLogger(ProjectUpdater::class.java)
    }
}

class UpdateCheckResult(val success: Boolean, val message: String?)