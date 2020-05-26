package org.museautomation.runner.desktop.tasks

import javafx.collections.FXCollections
import javafx.collections.transformation.SortedList
import javafx.scene.Node
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import org.museautomation.runner.desktop.widgets.DateCell
import org.museautomation.runner.desktop.widgets.FormattedCell
import org.museautomation.runner.tasks.ExecutedTask
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TaskHistoryTable
{
    fun getNode(): Node
    {
        return _table
    }

    fun setTasks(tasks: List<ExecutedTask>)
    {
        _list.clear()
        _list.addAll(tasks)
        val sorted = SortedList(_list)
        // create comparator to sort in reverse chronological order of task start time
        sorted.comparatorProperty().set(Comparator { (_, startTime1), (_, startTime2) -> Objects.compare(startTime2, startTime1, Comparator.comparingLong { aLong: Long? -> aLong!! }) })
        _table.items = sorted
    }

    fun addTask(task: ExecutedTask)
    {
        _list.add(task)
    }

    fun removeTask(task: ExecutedTask)
    {
        _list.remove(task)
    }

    val _table: TableView<ExecutedTask> = TableView<ExecutedTask>()
    val _list = FXCollections.observableArrayList<ExecutedTask>()

    init
    {
        _table.id = TABLE_ID

        val name_column = TableColumn<ExecutedTask, String>("Task")
        name_column.cellValueFactory = PropertyValueFactory("taskId")
        name_column.prefWidthProperty().set(100.0)
        _table.columns.add(name_column)

        val started_column = TableColumn<ExecutedTask, String>("Started")
        started_column.cellValueFactory = PropertyValueFactory("startTime")
        started_column.cellFactory = DateCell.create(DATE_FORMATTER)
        started_column.prefWidthProperty().set(100.0)
        _table.columns.add(started_column)

        val duration_column = TableColumn<ExecutedTask, String>("Duration")
        duration_column.cellValueFactory = PropertyValueFactory("duration")
        duration_column.cellFactory = FormattedCell.create(TaskDurationFormat())
        duration_column.prefWidthProperty().set(70.0)
        _table.columns.add(duration_column)

        val message_column = TableColumn<ExecutedTask, String>("Message")
        message_column.cellValueFactory = PropertyValueFactory("message")
        message_column.prefWidthProperty().set(300.0)
        _table.columns.add(message_column)
    }

    companion object
    {
        const val TABLE_ID = "omrdt.tht.id"
        val DATE_FORMATTER = SimpleDateFormat("dd MMM HH:mm:ss")
    }

    class TaskDurationFormat: Format()
    {
        override fun format(obj: Any?, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer
        {
            if (obj is Long)
            {
                val duration = Duration.ofMillis(obj)
                if (duration.toSeconds() > 59)
                    return toAppendTo.append("${duration.toMinutes()} min")
                else
                    return toAppendTo.append("${duration.toSeconds()} sec")
            }
            return toAppendTo
        }

        override fun parseObject(source: String?, pos: ParsePosition): Any
        {
            return source ?: "huh?"
        }
    }
}