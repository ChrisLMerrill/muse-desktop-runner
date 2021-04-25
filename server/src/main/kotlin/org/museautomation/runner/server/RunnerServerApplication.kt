package org.museautomation.runner.server

import org.museautomation.runner.settings.SettingsFiles
import org.museautomation.runner.settings.SettingsFolder
import org.museautomation.runnner.server.FileService
import org.museautomation.runnner.server.PingService
import java.io.File
import javax.ws.rs.core.Application

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class RunnerServerApplication: Application()
{

    override fun getClasses(): MutableSet<Class<*>>
    {
        val classes = HashSet<Class<*>>()
        classes.add(PingService::class.java)
        classes.add(FileService::class.java)
        classes.add(ProjectsService::class.java)
        return classes
    }

    companion object
    {
        init
        {
            val BASE_SETTINGS_FOLDER = File(File(System.getProperty("user.home")), ".muse/runner")
            SettingsFolder.BASE_FOLDER = BASE_SETTINGS_FOLDER
            SettingsFiles.FACTORY.setBaseLocation(BASE_SETTINGS_FOLDER)
        }
    }
}