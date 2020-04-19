package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.museautomation.runner.settings.SettingsFiles
import java.io.File

@Suppress("LeakingThis")
open class DesktopRunnerApp: Application()
{
    init
    {
        Platform.setImplicitExit(false)  // without this, the Platform shuts down when the first window closes...preventing creation of new windows
        INSTANCE = this
        SettingsFiles.FACTORY.setBaseLocation(getSettingsFolder())
    }

    override fun start(stage: Stage)
    {
        openMainWindow(stage)
    }

    open fun openMainWindow(stage: Stage)
    {
        val window = createMainWindow(this)
        window.show(stage)
    }

    open fun createMainWindow(app: DesktopRunnerApp): DesktopRunnerMainWindow
    {
        return DesktopRunnerMainWindow(app)
    }

    /*
     * Note that this is called during the constructor, before subclasses can be initialized. So
     * overriding functions should return a constant, or something else that does not require the class to
     * be initialized.
     */
    open fun getSettingsFolder(): File
    {
        return BASE_SETTINGS_FOLDER
    }

    companion object
    {
        lateinit var INSTANCE: DesktopRunnerApp
        private val BASE_SETTINGS_FOLDER: File = File(File(System.getProperty("user.home")), ".muse/runner")
    }
}