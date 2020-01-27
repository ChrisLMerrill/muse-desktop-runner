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

open class MainWindow : Application()
{
    override fun start(stage: Stage)
    {
        APP.main_stage = stage

        val tabs = TabPane()
        createTabs().forEach { tab ->
            tabs.tabs.add(tab)
        }
        tabs.selectionModel.select(getInitialTab())

        val scene = createScene(tabs)
        scene.stylesheets.add(javaClass.getResource("/runner.css").toExternalForm())

        stage.setOnCloseRequest(
        { event ->
            APP.main_stage?.close()
            APP.main_stage = null
            event.consume()
        })
        stage.title = getTitle()
        stage.scene = scene
        stage.show()

        _stage = stage

        val projects = RegisteredProjects.asList()
        println("There are ${projects.size} projects.")
    }

    open fun createScene(tabs: TabPane) = Scene(tabs, 300.0, 250.0)

    open fun getTitle() = "Muse Runner"

    protected fun createTabs() : MutableList<Tab>
    {
        val tabs = mutableListOf<Tab>()

        val projects_tab = Tab("Projects")
        projects_tab.content = Label("There are ${RegisteredProjects.asList().size} projects")
        tabs.add(projects_tab)
        _first_tab = projects_tab

        val jobs_tab = Tab("Jobs")
        jobs_tab.content = Label("There are ${Jobs.asList().size} jobs")
        tabs.add(jobs_tab)

        val runs_tab = Tab("Runs")
        runs_tab.content = Label("There are ${JobRuns.asList().size} runs")
        tabs.add(runs_tab)

        return tabs
    }

    protected fun getInitialTab() : Tab
    {
        return _first_tab
    }

    private lateinit var _stage : Stage
    private lateinit var _first_tab : Tab
}