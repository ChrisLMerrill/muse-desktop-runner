package org.museautomation.runner.projects

import org.museautomation.desktop.runner.settings.*

import java.util.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
object  RegisteredProjects : SettingsFolder() {
    private val FOLDER = "runner/projects"
    private val _projects = ArrayList<RegisteredProject>()

    init {
        loadFiles(FOLDER, RegisteredProject::class.java, null)
    }

    fun asList(): List<RegisteredProject> {
        return _projects
    }

    operator fun get(id: String): RegisteredProject? {
        for (p in _projects)
            if (id == p.id)
                return p
        return null
    }

    override fun accept(filename: String, settings: Any) {
        var name = filename
        if (settings is RegisteredProject) {
            name = name.substring(0, name.indexOf("."))
            settings.id = name
            _projects.add(settings)
        }
    }
}