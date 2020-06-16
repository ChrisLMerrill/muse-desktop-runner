package org.museautomation.runner.desktop.tasks

import javafx.geometry.Insets
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Tab
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import org.museautomation.runner.tasks.ExecutedTask
import org.museautomation.runner.tasks.ExecutedTasks
import org.museautomation.ui.extend.glyphs.Glyphs

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TaskHistoryTab
{
    fun getTab(): Tab
    {
        return _tab
    }

    fun setTasks(projects: List<ExecutedTask>)
    {
        val list = mutableListOf<ExecutedTask>()
        list.addAll(projects)
        _table.setTasks(list)
    }

    private fun updateButtonStates(enabled: Boolean)
    {
        val edit_enabled = enabled && _selected_tasks.isNotEmpty()
        _delete_button.disableProperty().set(!edit_enabled)
    }

    fun getChangeListener(): ExecutedTasks.ChangeListener
    {
        return _table.getChangeListener()
    }

    private val _tab = Tab("Task History")
    private val _table = TaskHistoryTable()
    private val _outer = BorderPane()
    private val _delete_button = Button("Delete")
    private var _selected_tasks: List<ExecutedTask> = listOf()

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
        _delete_button.id = DELETE_BUTTON_ID
        _delete_button.graphic = Glyphs.create("FA:MINUS_CIRCLE", Color.DARKRED)
        _delete_button.disableProperty().set(true) // disabled until selection made
        button_area.children.add(_delete_button)
        _delete_button.setOnAction {
            val dialog = Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the history of ${_selected_tasks.size} tasks?")
            dialog.title = "Confirm delete"
            dialog.headerText = null
            dialog.initStyle(StageStyle.UTILITY)
            dialog.initOwner(_table.getNode().scene.window)
            val result = dialog.showAndWait()
            if (result.isPresent && result.get() == ButtonType.OK)
            {
                for (task in _selected_tasks)
                {
                    ExecutedTasks.delete(task)
                    _table.removeTask(task)
                    _selected_tasks = listOf()
                    updateButtonStates(true)
                }
            }
        }

        _table.setSelectionListener(object : TaskHistoryTable.SelectionListener {
            override fun selectionChanged(tasks: List<ExecutedTask>)
            {
                _selected_tasks = tasks
                updateButtonStates(true)
            }
        })
    }

    companion object
    {
        const val DELETE_BUTTON_ID = "omrdt-tht-delete"
    }
}