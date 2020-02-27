package org.museautomation.runner

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.TextNode
import org.museautomation.runner.format.JsonNodeMapBuilder
import org.museautomation.runner.format.MapFormat
import org.museautomation.runner.jobs.JobRun
import org.museautomation.runner.jobs.JobRuns
import org.museautomation.runner.jobs.Jobs
import org.museautomation.runner.projects.RegisteredProjects
import org.museautomation.runner.settings.SettingsFolder
import org.museautomation.core.MuseTask
import org.museautomation.core.TaskResult
import org.museautomation.core.context.ProjectExecutionContext
import org.museautomation.core.context.TaskExecutionContext
import org.museautomation.core.events.matching.EventTypeMatcher
import org.museautomation.core.execution.BlockingThreadedTaskRunner
import org.museautomation.core.plugins.MusePlugin
import org.museautomation.core.project.SimpleProject
import org.museautomation.core.resource.storage.FolderIntoMemoryResourceStorage
import org.museautomation.builtins.plugins.resultstorage.LocalStorageLocationEventType
import org.museautomation.core.task.BasicTaskConfiguration
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

        if (!(resource is MuseTask)) {
            LOG.error("Task ${job.taskId} is not a task. It is a " + resource::class.simpleName)
            return
        }

        val config = BasicTaskConfiguration(resource)
        plugins.forEach {
            config.addPlugin(it)
        }
        val runner = BlockingThreadedTaskRunner(ProjectExecutionContext(project), config)
        runner.runTask()

        _test_context = config.context()

        // find result storage location
        val event = _test_context.eventLog.findFirstEvent(EventTypeMatcher(LocalStorageLocationEventType.TYPE_ID))
        if (event != null) {
            LOG.info("task result storage location: " + LocalStorageLocationEventType().getTestPath(event))
            if (LocalStorageLocationEventType().getTestPath(event) != null)
                run.results = LocalStorageLocationEventType().getTestPath(event)
        }

        // find the result
        val result = TaskResult.find(_test_context)
        if (result == null)
            LOG.info("unable to evaluate the task result")
        else {
            run.success = result.isPass
            if (result.isPass)
                LOG.info("success")
            else
                LOG.info("${result.failures.size} failures recorded")
        }

        // print output to console
        val outputs = job.outputs
        var root : JsonNode = TextNode("")
        if (outputs.size > 0)
        {
            val outfile = job.outputFile
            if (outfile != null)
            {
                root = ObjectMapper().readTree(File(run.results, outfile))
                for (output in outputs)
                    println("${output.label}=${root.get(output.name)}")
            }
        }

        if (result != null && result.isPass)
        {
            if (job.successMessageFormat == null)
                run.message = "Completed successfully"
            else
                run.message = MapFormat.format(job.successMessageFormat, JsonNodeMapBuilder(root).buildMap())
        }
    }

    private lateinit var _test_context: TaskExecutionContext

    @Suppress("unused")
    fun getTextContext() : TaskExecutionContext
    {
        return _test_context
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