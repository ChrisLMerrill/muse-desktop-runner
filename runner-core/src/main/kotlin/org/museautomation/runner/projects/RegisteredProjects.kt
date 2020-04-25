package org.museautomation.runner.projects

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.museautomation.runner.settings.SettingsFolder

import java.util.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
object RegisteredProjects : SettingsFolder()
{
    private const val FOLDER = "runner/projects"
    private val _projects = ArrayList<RegisteredProject>()
    private val mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

    init
    {
        load()
    }

    private fun load()
    {
        loadFiles(FOLDER, RegisteredProject::class.java, mapper)
    }

    fun clear()
    {
        _projects.clear()
    }

    fun asList(): List<RegisteredProject>
    {
        return _projects
    }

    operator fun get(id: String): RegisteredProject?
    {
        for (p in _projects)
            if (id == p.id)
                return p
        return null
    }

    override fun accept(filename: String, settings: Any)
    {
        var name = filename
        if (settings is RegisteredProject)
        {
            name = name.substring(0, name.indexOf("."))
            settings.id = name
            _projects.add(settings)
        }
    }
}