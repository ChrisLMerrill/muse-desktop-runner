package org.museautomation.runner.desktop.tasks

import javafx.scene.Node
import net.christophermerrill.testfx.ComponentTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.museautomation.runner.tasks.ExecutedTask

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectListTableTests: ComponentTest()
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

    private fun setupTasks(): List<ExecutedTask>
    {
        val tasks = mutableListOf<ExecutedTask>()
        tasks.add(ExecutedTask("taskid1", System.currentTimeMillis() - 1000, System.currentTimeMillis(), "/path/to/results", true, "Complete", null))
        tasks.add(ExecutedTask("taskid1", System.currentTimeMillis() - (100000 + 3 * 60000), System.currentTimeMillis() - 100000, "/other/path", false, "FAIL", "transid1"))
        return tasks
    }


    private lateinit var _table: TaskHistoryTable

    override fun createComponentNode(): Node
    {
        _table = TaskHistoryTable()
        return _table.getNode()
    }

}