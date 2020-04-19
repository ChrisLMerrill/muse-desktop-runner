package org.museautomation.runner.desktop

import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage
import org.museautomation.runner.settings.SettingsFiles
import org.museautomation.runner.settings.StageSettings

/*
 * This is the main window that is shown:
 * 1. at application start-up
 * 2. when the tray icon is pressed
 * 3. 'open' menu item is selcted
 */

open class DesktopRunnerMainWindow(app: DesktopRunnerApp)
{
    fun show(stage: Stage)
    {
        stage.scene = getScene()

        val settings_name = "MainWindow-stage.json"
        val settings = SettingsFiles.FACTORY.getSettings(settings_name, StageSettings::class.java)
        if (settings.width > 0)
        {
            stage.x = settings.x
            stage.y = settings.y
            stage.width = settings.width
            stage.height = settings.height
        }
        stage.show()

        stage.setOnCloseRequest {
            settings.x = stage.x
            settings.y = stage.y
            settings.width = stage.width
            settings.height = stage.height
            SettingsFiles.FACTORY.storeSettings(settings_name, settings)
        }
    }

    open fun getScene(): Scene
    {
        return Scene(Label("Hello!"))
    }
}