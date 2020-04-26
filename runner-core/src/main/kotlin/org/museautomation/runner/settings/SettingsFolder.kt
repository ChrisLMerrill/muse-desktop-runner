package org.museautomation.runner.settings

import com.fasterxml.jackson.databind.*
import org.slf4j.*

import java.io.*

/**
 * Represents a folder of homogenous settings files, where each file represents a instance of they setting type.
 * All settings files represent the same type of settings object. The files are serialized to/from JSON.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
abstract class SettingsFolder {
    protected abstract fun accept(filename: String, settings: Any)

    @Suppress("SameParameterValue")
    protected fun loadFiles(path: String, type: Class<*>, custom_mapper: ObjectMapper?) {
        val folder = getFolder(path)
        if (!folder.exists() && !folder.mkdir())
            return

        val files = folder.listFiles()
        if (files != null) {
            var mapper = custom_mapper
            if (mapper == null)
                mapper = ObjectMapper()
            for (source_file in files) {
                try {
                    FileInputStream(source_file).use { instream ->
                        val settings = mapper.readValue(instream, type)
                        accept(source_file.name, settings)
                    }
                } catch (e: Exception) {
                    LOG.error("Unable to load settings from " + source_file.path, e)
                }
            }
        }
    }

    protected fun getFolder(path: String): File
    {
        return File(BASE_FOLDER, path)
    }

    protected fun saveFile(settings: Any, path: String, filename: String, custom_mapper: ObjectMapper?) {
        val folder = getFolder(path)
        if (!folder.exists())
            if (!folder.mkdirs())
                throw Exception("Unable to create settings folder: " + folder.absolutePath)

        var mapper = custom_mapper
        if (mapper == null)
            mapper = ObjectMapper()
        try {
            FileOutputStream(File(folder, filename)).use { outstream ->
                mapper.writeValue(outstream, settings)
            }
        } catch (e: Exception) {
            LOG.error("Unable to save settings to " + path, e)
        }
    }

    companion object {
        var BASE_FOLDER = File(File(System.getProperty("user.home")), ".muse")
        private val LOG = LoggerFactory.getLogger(SettingsFolder::class.java)
    }
}