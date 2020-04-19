package org.museautomation.runner.desktop

import org.museautomation.core.plugins.MusePlugin
import org.museautomation.core.project.SimpleProject
import org.museautomation.core.values.ValueSourceConfiguration
import org.museautomation.runner.JobRunner
import org.museautomation.runner.desktop.input.CollectInputWindow
import org.museautomation.runner.jobs.Job
import org.museautomation.runner.jobs.JobRun
import org.museautomation.runner.jobs.JobRuns
import org.museautomation.runner.jobs.Jobs
import org.museautomation.runner.plugins.InputInjectionPlugin
import java.awt.MenuItem
import java.awt.PopupMenu
import kotlin.concurrent.thread

open class DesktopRunnerTrayMenuProvider(val app: DesktopRunnerApp)
{
    open fun createMenu(): PopupMenu
    {
        val popup = PopupMenu()

        popup.add(createOpenMenuItem())
        popup.addSeparator()

        val jobs = createJobMenuItems()
        if (jobs.isNotEmpty())
        {
            for (item in jobs)
                popup.add(item)
            popup.addSeparator()
        }

        val admin = createAdminMenuItems()
        if (admin.isNotEmpty())
        {
            for (item in admin)
                popup.add(item)
            popup.addSeparator()
        }

        popup.add(createExitMenuItem())
        return popup
    }

    open fun createOpenMenuItem(): MenuItem
    {
        val open_item = MenuItem("Open")
        open_item.addActionListener{ app.showMainWindow() }
        return open_item
    }

    open fun createExitMenuItem(): MenuItem
    {
        val exit_item = MenuItem()
        exit_item.label = "Exit"
        exit_item.addActionListener({ app.exitRequested() })
        return exit_item
    }

    open fun createJobMenuItems(): List<MenuItem>
    {
        val jobs = mutableListOf<MenuItem>()
        for (job in Jobs.asList())
        {
            val item = MenuItem("Run " + job.id)
            item.addActionListener {
                val input_initial_values = HashMap<String, ValueSourceConfiguration>()
                for (descriptor in job.inputs)
                    input_initial_values[descriptor.name] = ValueSourceConfiguration.forValue(descriptor.defaultValueString)
                val window = CollectInputWindow(SimpleProject(), job.inputs, input_initial_values, { input -> runJob(job, input) })
                window.open()
            }
            jobs.add(item)
        }

        return jobs
    }

    open fun createAdminMenuItems(): List<MenuItem>
    {
        return emptyList()
    }

    open fun runJob(job: Job, input_list: Map<String, ValueSourceConfiguration>)
    {
        thread(start = true) {
            val plugins = ArrayList<MusePlugin>()
            val element = InputInjectionPlugin()
            for (name in input_list.keys)
                input_list[name]?.let { element.addInput(name, it) }
            plugins.add(element)

            val run = JobRun("r" + System.currentTimeMillis(), job.id, System.currentTimeMillis(), null, null, null, null)
            val runner = JobRunner()
            runner.run(run, plugins)
            run.endTime = System.currentTimeMillis()
            JobRuns.save(run)
            notifyUserJobComplete(run, runner)
        }
    }

    open fun notifyUserJobComplete(run: JobRun, runner: JobRunner)
    {
        val success = run.success ?: return

        var message = "Job ${run.jobId} completed successfully"
        if (!success)
            message = "Job ${run.jobId} failed"
        if (run.message != null)
            message += ": " + run.message

        app.getTrayUI().showNotification("Job completed", message, success)
    }
}