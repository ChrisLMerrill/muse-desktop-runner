package org.museautomation.runner.desktop.tasks

import javafx.scene.Node
import net.christophermerrill.testfx.ComponentTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.museautomation.runner.tasks.ExecutedTask

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TaskHistoryTableTests: ComponentTest()
{
    @Test
    fun showTasks()
    {
        val tasks = setupTasks()
        _table.setTasks(tasks)
        waitForUiEvents()

        val task1 = tasks[0]
        val task2 = tasks[1]
        Assertions.assertTrue(exists(task1.taskId))
        Assertions.assertTrue(exists(task1.message))
        Assertions.assertTrue(exists(TaskHistoryTable.DATE_FORMATTER.format(task1.startTime)))
        Assertions.assertTrue(exists(TaskHistoryTable.TaskDurationFormat().format(task1.getDuration())))

        Assertions.assertTrue(exists(task2.taskId))
        Assertions.assertTrue(exists(task2.message))
        Assertions.assertTrue(exists(TaskHistoryTable.DATE_FORMATTER.format(task2.startTime)))
        Assertions.assertTrue(exists(TaskHistoryTable.TaskDurationFormat().format(task2.getDuration())))

    }

    @Test
    fun addTask()
    {
        val tasks = setupTasks()
        _table.setTasks(tasks)
        waitForUiEvents()

        val new_task = createTask(7)
        _table.addTask(new_task)
        waitForUiEvents()
        Assertions.assertTrue(exists(new_task.taskId))
        Assertions.assertTrue(exists(new_task.message))
        Assertions.assertTrue(exists(TaskHistoryTable.DATE_FORMATTER.format(new_task.startTime)))
        Assertions.assertTrue(exists(TaskHistoryTable.TaskDurationFormat().format(new_task.getDuration())))
    }

    @Test
    fun removeTask()
    {
        val tasks = setupTasks()
        _table.setTasks(tasks)
        waitForUiEvents()

        _table.removeTask(tasks[0])
        waitForUiEvents()

        Assertions.assertFalse(exists(tasks[0].taskId))
        Assertions.assertTrue(exists(tasks[1].taskId))
    }

    private fun setupTasks(): List<ExecutedTask>
    {
        val tasks = mutableListOf<ExecutedTask>()
        tasks.add(ExecutedTask("taskid1", System.currentTimeMillis() - 1000, System.currentTimeMillis(), "/path/to/results", true, "Complete", null))
        tasks.add(ExecutedTask("taskid2", System.currentTimeMillis() - (100000 + 3 * 60000), System.currentTimeMillis() - 100000, "/other/path", false, "FAIL", "transid1"))
        return tasks
    }

    private fun createTask(index: Int): ExecutedTask
    {
        val start_time = System.currentTimeMillis() - 100000 * index
        return ExecutedTask("taskid$index", start_time, start_time + 1000 * index, "/path/to/task/$index", true, "Complete $index", null)
    }

    private lateinit var _table: TaskHistoryTable

    override fun createComponentNode(): Node
    {
        _table = TaskHistoryTable()
        return _table.getNode()
    }

}