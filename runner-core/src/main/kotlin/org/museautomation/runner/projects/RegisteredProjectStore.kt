package org.museautomation.runner.projects

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
interface RegisteredProjectStore {
    fun get(id: String): RegisteredProject?
    fun add(project: RegisteredProject)
    fun getAll(): List<RegisteredProject>
}