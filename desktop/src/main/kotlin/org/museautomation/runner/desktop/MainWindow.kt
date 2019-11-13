package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.stage.WindowEvent

class MainWindow : Application()
{
    override fun start(stage: Stage)
    {
        val hello_button = Button()
        hello_button.setText("Print Greeting")
        hello_button.setOnAction { println("Hello, World!") }

        val exit_button = Button()
        exit_button.setText("Exit")
        exit_button.setOnAction { close() }

        val root_pane = GridPane()
        root_pane.children.add(hello_button)
        root_pane.children.add(exit_button)

        val scene = Scene(root_pane, 300.0, 250.0)

        stage.setOnCloseRequest(fun(event: WindowEvent)
        {
            stage.setIconified(true)
            event.consume()
        })
        stage.title = "Hello World app"
        stage.scene = scene
        stage.show()

        _stage = stage

        _tray_ui = SystemTrayUI(this)
    }

    fun close()
    {
        _tray_ui.teardown()
        System.exit(0)
    }

    fun show()
    {
        Platform.runLater { _stage.setIconified(false) }
    }

    lateinit var _stage : Stage
    lateinit var _tray_ui : SystemTrayUI
}