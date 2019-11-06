package org.museautomation.runner.jobs

import org.museautomation.runner.settings.SettingsFolder

import java.util.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
object JobRuns : SettingsFolder() {
    private val FOLDER = "runner/runs"
    private val _runs = ArrayList<JobRun>()

    init {
        loadFiles(FOLDER, JobRun::class.java, null)
    }

    fun asList(): List<JobRun> {
        return _runs
    }

    operator fun get(id: String): JobRun? {
        for (run in _runs)
            if (id == run.id)
                return run
        return null
    }

    override fun accept(filename: String, settings: Any) {
        var name = filename
        if (settings is JobRun) {
            name = name.substring(0, name.indexOf("."))
            settings.id = name
            _runs.add(settings)
        }
    }
}