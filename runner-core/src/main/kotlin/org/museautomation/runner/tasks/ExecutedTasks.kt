package org.museautomation.runner.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.museautomation.runner.settings.SettingsFolder

import java.util.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
object ExecutedTasks : SettingsFolder() {
    private val _tasks = ArrayList<ExecutedTask>()
    private val _mapper : ObjectMapper
    private val FOLDER = "tasks-executed"

    init {
        _mapper = ObjectMapper().registerModule(KotlinModule()).enable(SerializationFeature.INDENT_OUTPUT)
        loadFiles(FOLDER, ExecutedTask::class.java, _mapper)
    }

    fun asList(): List<ExecutedTask> {
        return _tasks
    }

    fun save(task: ExecutedTask) {
        saveFile(task, FOLDER, "${task.taskId}-${task.startTime}.json", _mapper)
        _tasks.add(task)
    }

    override fun accept(filename: String, settings: Any) {
        var name = filename
        if (settings is ExecutedTask) {
            _tasks.add(settings)
        }
    }
}