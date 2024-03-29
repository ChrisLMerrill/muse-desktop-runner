package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.museautomation.runner.desktop.authentication.*
import org.museautomation.runner.settings.SettingsFiles
import org.museautomation.runner.settings.SettingsFolder
import java.io.File
import kotlin.system.exitProcess

//@Suppress("LeakingThis")  // silence warnings from IDE about calling instance methods in the constructor.
open class DesktopRunnerApp: Application()
{
    init
    {
        Platform.setImplicitExit(false)  // without this, the Platform shuts down when the first window closes...preventing creation of new windows
        INSTANCE = this
    }

    override fun start(stage: Stage)
    {
        _stage = stage
        val auth_strategy = getAuthenticationStrategy()
        if (auth_strategy.requiresAuthentication())
        {
            if (!auth_strategy.isAuthenticated())
            {
                auth_strategy.startAuthentication(object: AuthenticationListener
                {
                    override fun authenticationComplete(result: AuthenticationResult)
                    {
                        if (!result.isSuccess())
                            exitProcess(1)
                        else
                        {
                            println("User is ${result.getIdentity()?.getName()}")
                            Platform.runLater { openMainWindow(stage) }
                        }
                    }
                })
            }
            else
                openMainWindow(stage)
        }
        else
            openMainWindow(stage)
    }

    open fun openMainWindow(stage: Stage)
    {
        val window = createMainWindow()
        window.show(stage)
        _main_window = window
        _tray_ui = createTrayUI()
    }

    open fun createMainWindow(): DesktopRunnerMainWindow
    {
        return DesktopRunnerMainWindow(this)
    }

    open fun createTrayUI(): DesktopRunnerTrayUI
    {
        return DesktopRunnerTrayUI(this, createMenuProvider())
    }

    open fun createMenuProvider(): DesktopRunnerTrayMenuProvider
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
    @Suppress("unused") // expect to be used by extensions
    open fun getSettingsFolder(): File
    {
        return BASE_SETTINGS_FOLDER
    }

    fun mainWindowClosed()
    {
         _main_window = null
    }

    open fun exitRequested()
    {
        Platform.runLater(
        {
            _main_window?.close()
            _tray_ui.shutdown()
            exitProcess(0)
        })
    }

    fun getTrayUI(): DesktopRunnerTrayUI
    {
        return _tray_ui
    }

    /* Maybe this will be needed for launching from the updater?
    open fun launch()
    {
        launch(this::class.java, *RunnerDesktopApp.ARGS)
    }
    */

    open fun getAuthenticationStrategy() : AuthenticationStrategy
    {
        return NoAuthenticationStrategy()
//        return BasicLoginAuthenticationStrategy()
    }

    private lateinit var _stage : Stage
    private var _main_window : DesktopRunnerMainWindow? = null
    private lateinit var _tray_ui : DesktopRunnerTrayUI

    companion object
    {
        lateinit var INSTANCE: DesktopRunnerApp
        private var BASE_SETTINGS_FOLDER: File = File(File(System.getProperty("user.home")), "default")

        @Suppress("SameParameterValue")
        fun setHomeFolder(path: String)
        {
            BASE_SETTINGS_FOLDER = File(File(System.getProperty("user.home")), path)
            SettingsFolder.BASE_FOLDER = BASE_SETTINGS_FOLDER
            SettingsFiles.FACTORY.setBaseLocation(BASE_SETTINGS_FOLDER)
        }

        fun getHomeFolder(): File
        {
            return BASE_SETTINGS_FOLDER
        }

        init
        {
            setHomeFolder(".muse/runner")
        }
    }
}