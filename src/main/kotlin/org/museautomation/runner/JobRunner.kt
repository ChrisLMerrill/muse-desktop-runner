package org.museautomation.runner

import org.museautomation.runner.jobs.*
import org.museautomation.runner.projects.RegisteredProjects
import org.museautomation.runner.settings.SettingsFolder
import org.musetest.core.MuseProject
import org.musetest.core.project.SimpleProject
import org.musetest.core.resource.MuseResourceRunner
import org.musetest.core.resource.storage.FolderIntoMemoryResourceStorage
import org.slf4j.LoggerFactory
import java.io.File

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class JobRunner {
    fun run(run: JobRun) {

        val job = Jobs.get(run.jobId)
        if (job == null) {
            LOG.error("Job ${run.jobId} was not found")
            return
        }

        val proj_reg = RegisteredProjects.get(job.projectId)
        if (proj_reg == null) {
            LOG.error("Project ${job.projectId} was not found")
            return
        }

        println("Will run ${job.taskId} in project ${proj_reg.name}")

        val project = SimpleProject(FolderIntoMemoryResourceStorage(File(proj_reg.path)))
        val resource = project.resourceStorage.getResource(job.taskId)
        if (resource == null) {
            LOG.error("Task ${job.taskId} was not found in project")
            return
        }

        val runners = project.classLocator.getInstances<MuseResourceRunner>(MuseResourceRunner::class.java)
        for (runner in runners) {
            if (runner.canRun(resource)) {
                if (runner.run(project, resource, false, null, null))
                    LOG.info("success")
                else
                    LOG.info("failed :(")
            }
        }

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val run = JobRuns.get(args[0])
            if (run == null) {
                LOG.error("JobRun ${args[0]} was not found")
                return
            }

            JobRunner().run(run)
        }

        private val LOG = LoggerFactory.getLogger(SettingsFolder::class.java)
    }
}