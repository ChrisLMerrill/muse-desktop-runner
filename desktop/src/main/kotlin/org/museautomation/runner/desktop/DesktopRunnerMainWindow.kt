package org.museautomation.runner.desktop

import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.image.Image
import javafx.stage.Stage
import org.museautomation.runner.desktop.projects.ProjectsTab
import org.museautomation.runner.desktop.tasks.TaskHistoryTab
import org.museautomation.runner.projects.RegisteredProjects
import org.museautomation.runner.settings.SettingsFiles
import org.museautomation.runner.desktop.settings.StageSettings
import org.museautomation.runner.tasks.ExecutedTasks

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

        val use_default = !SettingsFiles.FACTORY.exists(settings_name)
        if (use_default)
        {
            val size = getDefaultSize()
            stage.width = size.width
            stage.height = size.height
        }
        else
        {
            stage.x = settings.x
            stage.y = settings.y
            stage.width = settings.width
            stage.height = settings.height
        }

        stage.title = createStageTitle()
        addWindowIcons(stage.icons)
        stage.show()
        stage.setOnCloseRequest {event ->
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

        val tasks_tab = createTaskHistoryTab()
        tabs.add(tasks_tab.getTab())

        return tabs
    }

    protected fun createProjectsTab(): ProjectsTab
    {
        val projects_tab = ProjectsTab()
        projects_tab.setProjects(RegisteredProjects.asList())
        return projects_tab
    }
    
    protected fun createTaskHistoryTab(): TaskHistoryTab
    {
        val tasks_tab = TaskHistoryTab()
        tasks_tab.setTasks(ExecutedTasks.asList())
        ExecutedTasks.addListener(tasks_tab.getChangeListener())
        return tasks_tab
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

    private fun createStageTitle(): String
    {
        var title = getTitle()
        val is_default = System.getProperty("release.channel.isDefault")
        if (is_default != null && !is_default.toBoolean())
        {
            val channel = System.getProperty("release.channel")
            if (channel != null)
                title = "$title ($channel)"
        }
        return title
    }

    open fun close()
    {
        settings.x = _stage.x
        settings.y = _stage.y
        settings.width = _stage.width
        settings.height = _stage.height
        SettingsFiles.FACTORY.storeSettings(settings_name, settings)

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
    private val settings: StageSettings
    private val settings_name = "MainWindow-stage.json"

    init
    {
        settings = SettingsFiles.FACTORY.getSettings(settings_name, StageSettings::class.java)

    }
}