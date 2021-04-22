package org.museautomation.runnner.server

import javax.ws.rs.core.Application

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class RunnerServerApplication: Application() {

    override fun getClasses(): MutableSet<Class<*>> {
        val classes = HashSet<Class<*>>()
        classes.add(PingService::class.java)
        classes.add(FileService::class.java)
        classes.add(ProjectsService::class.java)
        return classes
    }
}