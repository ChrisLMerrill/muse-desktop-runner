package org.museautomation.runner.desktop

import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.image.Image
import javafx.stage.Stage
import org.museautomation.runner.desktop.projects.ProjectsTab
import org.museautomation.runner.jobs.JobRuns
import org.museautomation.runner.jobs.Jobs
import org.museautomation.runner.projects.RegisteredProjects
import org.museautomation.runner.settings.SettingsFiles
import org.museautomation.runner.settings.StageSettings

/*
 * This is the main window that is shown:
 * 1. at application start-up
 * 2. when the tray icon is pressed
 * 3. 'open' menu item is selcted
 */

open class DesktopRunnerMainWindow(private val app: DesktopRunnerApp)
{
    fun show(stage: Stage)
    {
        _stage = stage
        stage.scene = createScene()

        val settings_name = "MainWindow-stage.json"
        val settings = SettingsFiles.FACTORY.getSettings(settings_name, StageSettings::class.java)
        if (settings.width > 0)
        {
            stage.x = settings.x
            stage.y = settings.y
            stage.width = settings.width
            stage.height = settings.height
        }
        else
        {
            val size = getDefaultSize()
            stage.width = size.width
            stage.width = size.height
        }

        stage.title = getTitle()
        addWindowIcons(stage.icons)
        stage.show()
        stage.setOnCloseRequest {event ->
            settings.x = stage.x
            settings.y = stage.y
            settings.width = stage.width
            settings.height = stage.height
            SettingsFiles.FACTORY.storeSettings(settings_name, settings)

            close()
            stage.close()
            event.consume()
            app.mainWindowClosed()
        }
    }

    open fun getDefaultSize(): Dimension2D
    {
        return Dimension2D(300.0, 500.0)
    }

    open fun createScene(): Scene
    {
        val tabs = TabPane()
        createTabs().forEach { tab ->
            tabs.tabs.add(tab)
        }
        tabs.selectionModel.select(getInitialTab())

        val scene = Scene(tabs)
        scene.stylesheets.add(javaClass.getResource("/runner.css").toExternalForm())
        return scene
    }

    open fun createTabs() : MutableList<Tab>
    {
        val tabs = mutableListOf<Tab>()

        val projects_tab = createProjectsTab()
        tabs.add(projects_tab.getTab())
        _first_tab = projects_tab.getTab()

        val jobs_tab = Tab("Jobs")
        jobs_tab.content = Label("There are ${Jobs.asList().size} jobs")
        tabs.add(jobs_tab)

        val runs_tab = Tab("Runs")
        runs_tab.content = Label("There are ${JobRuns.asList().size} runs")
        tabs.add(runs_tab)

        return tabs
    }

    protected fun createProjectsTab(): ProjectsTab
    {
        val projects_tab = ProjectsTab()
        projects_tab.setProjects(RegisteredProjects.asList())
        return projects_tab
    }
    
    open fun getInitialTab() : Tab
    {
        return _first_tab
    }

    open fun addWindowIcons(icons: ObservableList<Image>)
    {
        icons.add(Image("/Mu-icon16.png"))
    }

    open fun getTitle() = "Muse Runner"

    open fun close()
    {
        _stage.close()
    }

    open fun show()
    {
        Platform.runLater({
            _stage.show()
            _stage.requestFocus()
            _stage.toFront()
            _stage.isIconified = false
        })
    }

    private lateinit var _first_tab : Tab
    private lateinit var _stage : Stage
}