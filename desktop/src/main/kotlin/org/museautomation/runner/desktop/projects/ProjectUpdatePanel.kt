package org.museautomation.runner.desktop.projects

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import org.museautomation.ui.extend.glyphs.Glyphs

/**
 * Provides a compact UI for checking/updating a project. It is designed
 * to fit in a table cell.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectUpdatePanel(private val row_state: RowUpdateState)
{
    private fun setState(new_state: ProjectUpdateState)
    {
        Platform.runLater({
            when (new_state)
            {
                ProjectUpdateState.Unavailable -> _pane.center = _unavailable_node
                ProjectUpdateState.Start -> _pane.center = _start_node
                ProjectUpdateState.Checking -> _pane.center = _checking_node
                ProjectUpdateState.ReadyToUpdate -> _pane.center = _ready_node
                ProjectUpdateState.Updating -> _pane.center = _updating_node
                ProjectUpdateState.Message -> _pane.center = _message_node
            }
        })
    }

    private fun returnToStartState()
    {
        val settings = row_state.row.project.download_settings
        if (settings == null || settings.url == "")
        {
            setState(ProjectUpdateState.Unavailable)
        }
        else
        {
            setState(ProjectUpdateState.Start)
        }
    }

    private fun showMessage(message: String)
    {
        _message_label.text = message
        setState(ProjectUpdateState.Message)
    }

    fun getNode(): Node
    {
        return _pane
    }

    private val _pane = BorderPane()
    private var _unavailable_node = Label("n/a")
    private var _start_node = HBox()
    private val _checking_node = Label("Checking...")
    private val _ready_node = HBox()
    private val _updating_node = Label("Updating...")
    private val _message_label = Label()
    private val _message_node = BorderPane()

    init
    {
        val check_button = Button("Check")
        _start_node.alignment = Pos.CENTER
        _start_node.children.add(check_button)
        _start_node.children.add(Label(" for updates"))
        check_button.setOnAction {
            val thread = Thread(Runnable {
                println("checking...")
                Thread.sleep(3000)
                setState(ProjectUpdateState.ReadyToUpdate)
            })
            thread.start()
            setState(ProjectUpdateState.Checking)
        }

        val update_button = Button("Update")
        _ready_node.alignment = Pos.CENTER
        _ready_node.children.add(update_button)
        _ready_node.children.add(Label(" to version N"))
        update_button.setOnAction {
            val thread = Thread(Runnable {
                println("updating...")
                Thread.sleep(3000)
                _message_label.text = "updated to version N"
                setState(ProjectUpdateState.Message)
            })
            thread.start()
            setState(ProjectUpdateState.Updating)
        }

        val reset_button = Button("", Glyphs.create("FA:REFRESH"))
        _message_node.right = reset_button
        _message_node.center = _message_label
        reset_button.setOnAction { returnToStartState() }

        if (row_state.message != null)
            showMessage(row_state.message)
        else
            setState(row_state.update_state)
    }
}
