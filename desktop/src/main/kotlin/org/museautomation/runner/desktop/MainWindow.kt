package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import org.museautomation.runner.desktop.RunnerDesktopApp.Companion.APP
import org.museautomation.runner.projects.RegisteredProjects

class MainWindow : Application()
{
    override fun start(stage: Stage)
    {
        APP.main_stage = stage

        val hello_button = Button()
        hello_button.text = "Print Greeting"
        hello_button.setOnAction { println("Hello, World!") }

        val exit_button = Button()
        exit_button.text = "Exit"
        exit_button.setOnAction { APP.shutdown() }

        val root_pane = GridPane()
        root_pane.children.add(hello_button)
        root_pane.children.add(exit_button)

        val scene = Scene(root_pane, 300.0, 250.0)

        stage.setOnCloseRequest(
        { event ->
            APP.main_stage?.close()
            APP.main_stage = null
            event.consume()
        })
        stage.title = "Hello World app"
        stage.scene = scene
        stage.show()

        _stage = stage

        val projects = RegisteredProjects.asList()
        println("There are ${projects.size} projects.")
    }

    private lateinit var _stage : Stage
}