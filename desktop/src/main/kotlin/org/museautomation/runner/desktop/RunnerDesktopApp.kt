package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import kotlin.system.exitProcess

class RunnerDesktopApp
{
    var window : MainWindow? = null

    fun shutdown()
    {
        window?.close()
        TRAY.teardown()
        exitProcess(0)
    }

    fun showWindow()
    {
        if (window == null)
            createWindow()
        else
            window?.show()
    }

    fun createWindow()
    {
        _window_count++
        if (_window_count == 1)
            Application.launch(MainWindow::class.java, *ARGS)
        else
        {
            val new_window = MainWindow()
            Platform.runLater { new_window.start(Stage()) }

            window = new_window
        }
    }

    companion object
    {
        @JvmStatic
        fun main(args: Array<String>)
        {
            Platform.setImplicitExit(false)
            ARGS = args
            APP.createWindow()
        }

        lateinit var ARGS : Array<String>
        var APP = RunnerDesktopApp()
        val TRAY = SystemTrayUI()

    }

    var _window_count = 0
}