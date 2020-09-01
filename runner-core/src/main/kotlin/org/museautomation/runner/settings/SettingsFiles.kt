package org.museautomation.runner.settings

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException

class SettingsFiles
{
    fun <T : Any> getSettings(filename: String, type: Class<T>): T
    {
        val found = _settings[filename]
        @Suppress("UNCHECKED_CAST")
        if (type.isInstance(found))
            return found as T

        val file = File(_base_folder, filename)
        if (file.exists())
            try
            {
                val settings = MAPPER.readValue(file, type)
                _settings[filename] = settings
                return settings
            }
            catch (e: IOException)
            {
                LOG.error("Unable to read settings file ($filename) due to ${e.message}", e)
            }

        return type.getDeclaredConstructor().newInstance()
    }

    fun storeSettings(filename: String, settings: Any)
    {
        try
        {
             MAPPER.writeValue(File(_base_folder, filename), settings)
            _settings[filename] = settings
        }
        catch (e: IOException)
        {
            LOG.error("Unable to store settings file ($filename) due to ${e.message}", e)
        }
    }

    fun exists(filename: String): Boolean
    {
        return File(_base_folder, filename).exists()
    }

    fun setBaseLocation(base: File)
    {
        _base_folder = base
    }

    private val _settings = mutableMapOf<String,Any>()
    private var _base_folder = File(System.getProperty("user.home"))

    companion object
    {
        val FACTORY = SettingsFiles()
        private val LOG = LoggerFactory.getLogger(SettingsFiles::class.java)
        private val MAPPER = ObjectMapper().registerModule(KotlinModule()).enable(SerializationFeature.INDENT_OUTPUT)
    }
}