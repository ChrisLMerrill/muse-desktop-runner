package org.museautomation.runner.desktop.projects

import javafx.scene.Node
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import org.museautomation.runner.projects.RegisteredProject
import java.text.SimpleDateFormat

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectListTable
{

    fun getNode(): Node
    {
        return _table
    }

    fun setProjects(projects: MutableList<RegisteredProject>)
    {
        _table.items.clear()
        for (project in projects)
            _table.items.add(ProjectRow(this, project))
    }

    fun add(project: RegisteredProject)
    {
        _table.items.add(ProjectRow(this, project))
    }

    fun update(project: RegisteredProject)
    {
        for (row in _table.items)
            if (row.project === project)
                row.update()
        _table.refresh()
    }

    fun addSelectionListener(listener: SelectionListener)
    {
        _listener = listener
    }

    val date_format = SimpleDateFormat("MMM dd")
    private var _table: TableView<ProjectRow>
    private var _listener: SelectionListener? = null

    init
    {
        _table = TableView<ProjectRow>()
        _table.id = TABLE_ID

        val name_column = TableColumn<ProjectRow, String>("Project Name")
        name_column.setCellValueFactory(PropertyValueFactory("name"))
        _table.columns.add(name_column)

        val version_column = TableColumn<ProjectRow, String>("Version")
        version_column.setCellValueFactory(PropertyValueFactory("version"))
        _table.columns.add(version_column)

        val date_column = TableColumn<ProjectRow, String>("Date")
        date_column.setCellValueFactory(PropertyValueFactory("date"))
        _table.columns.add(date_column)

        _table.selectionModel.selectedItemProperty().addListener(
        { _, _, new_row ->
            val listener = _listener
            if (listener != null)
                listener.selectionChanged(new_row?.project)
        })
    }

    companion object
    {
        const val TABLE_ID = "omrdp.plt.id"
    }

    interface SelectionListener
    {
        fun selectionChanged(project: RegisteredProject?)
    }

}