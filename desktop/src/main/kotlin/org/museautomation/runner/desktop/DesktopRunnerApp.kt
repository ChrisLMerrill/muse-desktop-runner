package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.museautomation.runner.settings.SettingsFiles
import java.io.File
import kotlin.system.exitProcess

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
        _tray_ui = createTrayUI()
    }

    open fun openMainWindow(stage: Stage)
    {
        val window = createMainWindow()
        window.show(stage)
        _main_window = window
    }

    open fun createMainWindow(): DesktopRunnerMainWindow
    {
        return DesktopRunnerMainWindow(this)
    }

    open fun createTrayUI(): DesktopRunnerTrayUI
    {
        return DesktopRunnerTrayUI(this, createMenuProvider())
    }

    private fun createMenuProvider(): DesktopRunnerTrayMenuProvider
    {
        return DesktopRunnerTrayMenuProvider(this)
    }

    fun showMainWindow()
    {
        val window = _main_window
        if (window == null)
            Platform.runLater(
            {
                openMainWindow(Stage())
            })
        else
        {
            window.show()
        }
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

    fun mainWindowClosed()
    {
         _main_window = null
    }

    fun exitRequested()
    {
        Platform.runLater(
        {
            _main_window?.close()
            _tray_ui.shutdown()
            exitProcess(0)
        })
    }

    var _main_window : DesktopRunnerMainWindow? = null
    lateinit var _tray_ui : DesktopRunnerTrayUI

    companion object
    {
        lateinit var INSTANCE: DesktopRunnerApp
        private val BASE_SETTINGS_FOLDER: File = File(File(System.getProperty("user.home")), ".muse/runner")
    }
}