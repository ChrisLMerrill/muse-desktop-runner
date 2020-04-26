package org.museautomation.runner.desktop.projects

import javafx.scene.Node
import javafx.scene.control.TableCell
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

    fun removeProject(project: RegisteredProject)
    {
        for (row in _table.items)
            if (row.project === project)
            {
                _table.items.remove(row)
                _table.selectionModel.clearSelection()
                break
            }
    }

    val date_format = SimpleDateFormat("MMM dd")
    private var _table: TableView<ProjectRow> = TableView<ProjectRow>()
    private var _listener: SelectionListener? = null

    init
    {
        _table.id = TABLE_ID

        val name_column = TableColumn<ProjectRow, String>("Project Name")
        name_column.cellValueFactory = PropertyValueFactory("name")
        name_column.prefWidthProperty().set(100.0)
        _table.columns.add(name_column)

        val version_column = TableColumn<ProjectRow, String>("Version")
        version_column.cellValueFactory = PropertyValueFactory("version")
        version_column.prefWidthProperty().set(50.0)
        _table.columns.add(version_column)

        val date_column = TableColumn<ProjectRow, String>("Date")
        date_column.cellValueFactory = PropertyValueFactory("date")
        date_column.prefWidthProperty().set(50.0)
        _table.columns.add(date_column)

        val update_column = TableColumn<ProjectRow, RowUpdateState>("Updates")
        update_column.prefWidthProperty().set(300.0)
        update_column.cellValueFactory = PropertyValueFactory("updateState")
        update_column.setCellFactory({
            object : TableCell<ProjectRow, RowUpdateState>()
            {
                override fun updateItem(item: RowUpdateState?, empty: Boolean)
                {
                    graphic = if (item == null)
                        null
                    else
                        ProjectUpdatePanel(item).getNode()
                    }
                }
            })
        _table.columns.add(update_column)

        _table.selectionModel.selectedItemProperty().addListener(
        { _, _, new_row ->
            _listener?.selectionChanged(new_row?.project)
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