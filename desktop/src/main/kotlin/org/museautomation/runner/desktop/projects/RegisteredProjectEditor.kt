package org.museautomation.runner.desktop.projects

import javafx.application.Platform
import javafx.geometry.HPos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import org.museautomation.runner.projects.DownloadableProjectSettings
import org.museautomation.runner.projects.RegisteredProject

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class RegisteredProjectEditor
{
    fun getNode(): Node
    {
        return _grid
    }

    fun setProject(project: RegisteredProject?)
    {
        _project = project
        fillFields()
    }

    interface ActionListener
    {
        fun savePressed(project: RegisteredProject)
        fun cancelPressed(project: RegisteredProject)
    }

    fun setOnAction(listener: ActionListener)
    {
        _listener = listener
    }

    private fun fillFields()
    {
        Platform.runLater(
        {
            val project = _project
            if (project == null)
            {
                _name_field.text = ""
                _id_field.text = ""
                _path_field.text = ""
                _download_field.selectedProperty().set(false)
                _url_field.disableProperty().set(true)
                _url_field.text = ""
            }
            else
            {
                _name_field.text = project.name
                _id_field.text = project.id
                _path_field.text = project.path

                val downloaded = project.download_settings
                if (downloaded == null)
                {
                    _download_field.selectedProperty().set(false)
                    _url_field.disableProperty().set(true)
                    _url_field.text = ""
                }
                else
                {
                    _download_field.selectedProperty().set(true)
                    _url_field.disableProperty().set(false)
                    _url_field.text = downloaded.url
                }
            }
        })
    }

    private fun getProject(): RegisteredProject
    {
        val project = _project
        var download: DownloadableProjectSettings? = null
        if (_download_field.isSelected)
            download = DownloadableProjectSettings(_url_field.text, null)
        return if (project == null)
        {
            RegisteredProject(_id_field.text, _name_field.text, _path_field.text, download)
        }
        else
        {
            project.id = _id_field.text
            project.name = _name_field.text
            project.path = _path_field.text
            project.download_settings = download
            project
        }
    }

    private var _listener: ActionListener? = null
    private val _grid = GridPane()
    private var _project: RegisteredProject? = null
    private val _name_field = TextField()
    private val _id_field = TextField()
    private val _path_field = TextField()
    private val _download_field = CheckBox()
    private val _url_field = TextField()
    private val _save_button = Button("Save")

    init
    {
        _grid.hgap = 5.0
        _grid.vgap = 5.0
        _grid.id = EDITOR_NODE_ID

        var row = 0
        val name_label = Label("Name")
        _grid.add(name_label, 0, row)
        _name_field.id = NAME_FIELD_ID
        _grid.add(_name_field, 1, row)

        row++
        val id_label = Label("Id")
        _grid.add(id_label, 0, row)
        _id_field.id = ID_FIELD_ID
        _grid.add(_id_field, 1, row)

        row++
        val path_label = Label("Path")
        _grid.add(path_label, 0, row)
        _path_field.id = PATH_FIELD_ID
        _grid.add(_path_field, 1, row)

        row++
        val download_label = Label("Download this")
        _grid.add(download_label, 0, row)
        _download_field.id = DOWNLOAD_CHECKBOX_ID
        _grid.add(_download_field, 1, row)
        _download_field.setOnAction { _url_field.disableProperty().set(!_download_field.isSelected) }

        row++
        val url_label = Label("from URL")
        _grid.add(url_label, 0, row)
        _url_field.id = URL_FIELD_ID
        _grid.add(_url_field, 1, row)

        row++
        GridPane.setHgrow(_save_button, Priority.ALWAYS)
        GridPane.setHalignment(_save_button, HPos.RIGHT)
        _grid.add(_save_button, 1, row)
        _save_button.id = SAVE_BUTTON_ID
        _save_button.setOnAction { _listener?.savePressed(getProject()) }
    }

    companion object
    {
        const val EDITOR_NODE_ID = "omrdp-rep-node"

        const val NAME_FIELD_ID = "omrdp-rep-name"
        const val ID_FIELD_ID = "omrdp-rep-id"
        const val PATH_FIELD_ID = "omrdp-rep-path"

        const val DOWNLOAD_CHECKBOX_ID = "omrdp-rep-download"
        const val URL_FIELD_ID = "omrdp-rep-url"

        const val SAVE_BUTTON_ID = "omrdp-rep-save"
    }
}