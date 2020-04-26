package org.museautomation.runner.desktop.projects

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import org.museautomation.runner.projects.RegisteredProject
import org.museautomation.runner.projects.RegisteredProjects
import org.museautomation.ui.extend.glyphs.Glyphs

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectsTab
{
    fun getTab(): Tab
    {
        return _tab
    }

    fun setProjects(projects: List<RegisteredProject>)
    {
        val list = mutableListOf<RegisteredProject>()
        list.addAll(projects)
        _table.setProjects(list)
    }

    private fun addFinished(project: RegisteredProject?)
    {
        if (project != null)
        {
            _table.add(project)
            RegisteredProjects.add(project)
        }
        _outer.bottom = null
        _add_button.disableProperty().set(false)
    }

    private val _tab = Tab("Projects")
    private val _table = ProjectListTable()
    private val _editor = RegisteredProjectEditor()
    private val _outer = BorderPane()
    private val _add_button = Button("Add")

    init
    {
        _tab.content = _outer
        _outer.padding = Insets(5.0)

        val inner = BorderPane()
        _outer.center = inner
        inner.center = _table.getNode()
        val button_area = HBox()
        button_area.padding = Insets(5.0)
        inner.bottom = button_area
        _add_button.id = ADD_BUTTON_ID
        _add_button.graphic = Glyphs.create("FA:PLUS_CIRCLE", Color.GREEN)
        button_area.children.add(_add_button)
        _add_button.setOnAction {
            _outer.bottom = _editor.getNode()
            _add_button.disableProperty().set(true)
        }

        _editor.setOnAction(object : RegisteredProjectEditor.ActionListener {
            override fun savePressed(project: RegisteredProject)
            {
                addFinished(project)
            }

            override fun cancelPressed()
            {
                addFinished(null)
            }
        })
    }

    companion object
    {
        const val ADD_BUTTON_ID = "omrdp-pt-add"
    }
}