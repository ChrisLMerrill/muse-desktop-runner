package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import org.museautomation.runner.desktop.RunnerDesktopApp.Companion.APP
import org.museautomation.runner.jobs.JobRuns
import org.museautomation.runner.jobs.Jobs
import org.museautomation.runner.projects.RegisteredProjects

class MainWindow : Application()
{
    override fun start(stage: Stage)
    {
        APP.main_stage = stage

        val tabs = TabPane()

        val projects_tab = Tab("Projects")
        projects_tab.content = Label("There are ${RegisteredProjects.asList().size} projects")
        tabs.tabs.add(projects_tab)

        val jobs_tab = Tab("Jobs")
        jobs_tab.content = Label("There are ${Jobs.asList().size} jobs")
        tabs.tabs.add(jobs_tab)

        val runs_tab = Tab("Runs")
        runs_tab.content = Label("There are ${JobRuns.asList().size} runs")
        tabs.tabs.add(runs_tab)

        val scene = Scene(tabs, 300.0, 250.0)

        stage.setOnCloseRequest(
        { event ->
            APP.main_stage?.close()
            APP.main_stage = null
            event.consume()
        })
        stage.title = "Muse Runner"
        stage.scene = scene
        stage.show()

        _stage = stage

        val projects = RegisteredProjects.asList()
        println("There are ${projects.size} projects.")
    }

    private lateinit var _stage : Stage
}