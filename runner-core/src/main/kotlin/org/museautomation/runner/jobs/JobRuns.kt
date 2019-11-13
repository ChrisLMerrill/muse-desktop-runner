package org.museautomation.runner.jobs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.museautomation.runner.settings.SettingsFolder

import java.util.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
object JobRuns : SettingsFolder() {
    private val FOLDER = "runner/runs"
    private val _runs = ArrayList<JobRun>()
    private val mapper : ObjectMapper

    init {
        mapper = ObjectMapper().registerModule(KotlinModule()).enable(SerializationFeature.INDENT_OUTPUT)
        loadFiles(FOLDER, JobRun::class.java, mapper)
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

    fun save(run: JobRun) {
        saveFile(run, FOLDER, run.id + ".json", mapper)
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