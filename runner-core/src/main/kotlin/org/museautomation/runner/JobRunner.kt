package org.museautomation.runner

import org.museautomation.runner.jobs.JobRun
import org.museautomation.runner.jobs.JobRuns
import org.museautomation.runner.jobs.Jobs
import org.museautomation.runner.projects.RegisteredProjects
import org.museautomation.runner.settings.SettingsFolder
import org.musetest.core.MuseTest
import org.musetest.core.TestResult
import org.musetest.core.context.ProjectExecutionContext
import org.musetest.core.events.matching.EventTypeMatcher
import org.musetest.core.execution.BlockingThreadedTestRunner
import org.musetest.core.plugins.MusePlugin
import org.musetest.core.project.SimpleProject
import org.musetest.core.resource.storage.FolderIntoMemoryResourceStorage
import org.musetest.core.resultstorage.LocalStorageLocationEventType
import org.musetest.core.test.BasicTestConfiguration
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class JobRunner {
    fun run(run: JobRun, plugins: List<MusePlugin>) {

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

        if (!(resource is MuseTest)) {
            LOG.error("Task ${job.taskId} is not a task. It is a " + resource::class.simpleName)
            return
        }

        val config = BasicTestConfiguration(resource)
        plugins.forEach {
            config.addPlugin(it)
        }
        val runner = BlockingThreadedTestRunner(ProjectExecutionContext(project), config)
        runner.runTest()

        val test_context = config.context()

        // find result storage location
        val event = test_context.eventLog.findFirstEvent(EventTypeMatcher(LocalStorageLocationEventType.TYPE_ID))
        if (event != null) {
            LOG.info("task result storage location: " + LocalStorageLocationEventType().getTestPath(event))
            if (LocalStorageLocationEventType().getTestPath(event) != null)
                run.results = LocalStorageLocationEventType().getTestPath(event)
        }

        // find the result
        val result = TestResult.find(test_context)
        if (result == null)
            LOG.info("unable to evaluate the task result")
        else {
            run.success = result.isPass
            if (result.isPass)
                LOG.info("success")
            else
                LOG.info("${result.failures.size} failures recorded")
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

            run.startTime = System.currentTimeMillis()
            JobRuns.save(run)
            JobRunner().run(run, Collections.emptyList())
            run.endTime = System.currentTimeMillis()
            JobRuns.save(run)
        }

        private val LOG = LoggerFactory.getLogger(SettingsFolder::class.java)
    }
}