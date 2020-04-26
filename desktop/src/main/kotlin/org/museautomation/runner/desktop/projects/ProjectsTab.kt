package org.museautomation.runner.desktop.projects

import javafx.geometry.Insets
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Tab
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.stage.StageStyle
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
        updateButtonStates(true)
        _add_button.disableProperty().set(false)
    }

    private fun editFinished(project: RegisteredProject?)
    {
        if (project != null)
        {
            _table.update(project)
            RegisteredProjects.save(project)
        }
        _outer.bottom = null
        updateButtonStates(true)
        _add_button.disableProperty().set(false)
    }

    private fun updateButtonStates(enabled: Boolean)
    {
        _add_button.disableProperty().set(!enabled)

        val edit_enabled = enabled && _selected_project != null
        _edit_button.disableProperty().set(!edit_enabled)
        _delete_button.disableProperty().set(!edit_enabled)
    }

    private val _tab = Tab("Projects")
    private val _table = ProjectListTable()
    private val _editor = RegisteredProjectEditor()
    private val _outer = BorderPane()
    private val _add_button = Button("Add")
    private val _edit_button = Button("Edit")
    private val _delete_button = Button("Delete")
    private var _selected_project: RegisteredProject? = null
    private var _editing_project: RegisteredProject? = null

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
            _editing_project = null
            _editor.setProject(null)
            updateButtonStates(false)
        }

        _edit_button.id = EDIT_BUTTON_ID
        _edit_button.graphic = Glyphs.create("FA:EDIT", Color.GREEN)
        _edit_button.disableProperty().set(true) // disabled until selection made
        button_area.children.add(_edit_button)
        _edit_button.setOnAction {
            _editing_project = _selected_project
            _editor.setProject(_selected_project)
            _outer.bottom = _editor.getNode()
            updateButtonStates(false)
        }

        _delete_button.id = EDIT_BUTTON_ID
        _delete_button.graphic = Glyphs.create("FA:MINUS_CIRCLE", Color.DARKRED)
        _delete_button.disableProperty().set(true) // disabled until selection made
        button_area.children.add(_delete_button)
        _delete_button.setOnAction {
            val dialog = Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete ${_selected_project?.name} ?")
            dialog.title = "Confirm delete"
            dialog.headerText = null
            dialog.initStyle(StageStyle.UTILITY)
            dialog.initOwner(_table.getNode().scene.window)
            val result = dialog.showAndWait()
            if (result.isPresent && result.get() == ButtonType.OK)
            {
                RegisteredProjects.delete(_selected_project!!)
                _table.removeProject(_selected_project!!)
                _selected_project = null
                updateButtonStates(true)
            }
        }

        _editor.setOnAction(object : RegisteredProjectEditor.ActionListener {
            override fun savePressed(project: RegisteredProject)
            {
                if (_editing_project == null)
                    addFinished(project)
                else
                    editFinished(project)
            }

            override fun cancelPressed()
            {
                if (_editing_project == null)
                    addFinished(null)
                else
                    editFinished(null)
            }
        })

        _table.addSelectionListener(object : ProjectListTable.SelectionListener {
            override fun selectionChanged(project: RegisteredProject?)
            {
                _selected_project = project
                _edit_button.disableProperty().set(project == null)
                _delete_button.disableProperty().set(project == null)
            }
        })
    }

    companion object
    {
        const val ADD_BUTTON_ID = "omrdp-pt-add"
        const val EDIT_BUTTON_ID = "omrdp-pt-edit"
    }
}