package org.museautomation.runner.jobs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.museautomation.runner.settings.SettingsFolder

import java.util.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
object Jobs : SettingsFolder() {
    private val FOLDER = "runner/jobs"
    private val list = ArrayList<Job>()
    private val mapper : ObjectMapper

    init {
        mapper = ObjectMapper().registerModule(KotlinModule())
        loadFiles(FOLDER, Job::class.java, mapper)
    }

    fun asList(): List<Job> {
        return list
    }

    operator fun get(id: String): Job? {
        for (job in list)
            if (id == job.id)
                return job
        return null
    }

    override fun accept(filename: String, settings: Any) {
        var name = filename
        if (settings is Job) {
            name = name.substring(0, name.indexOf("."))
            settings.id = name
            list.add(settings)
        }
    }
}