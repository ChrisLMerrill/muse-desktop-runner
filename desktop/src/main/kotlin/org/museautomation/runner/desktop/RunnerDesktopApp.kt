package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.museautomation.runner.JobRunner
import org.museautomation.runner.desktop.input.CollectInputWindow
import org.museautomation.runner.jobs.Job
import org.museautomation.runner.jobs.JobRun
import org.museautomation.runner.jobs.JobRuns
import org.museautomation.runner.plugins.InputInjectionPlugin
import org.museautomation.core.plugins.MusePlugin
import org.museautomation.core.project.SimpleProject
import org.museautomation.core.values.ValueSourceConfiguration
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/*
 * The core of the application. Opens new windows as needed (e.g. at startup) and puts an icon in the System Tray.
 * Wne closing the main UI window, the app continues to run, accessible to the user via the system tray.
 */
open class RunnerDesktopApp
{
    var main_stage: Stage? = null

    init
    {
        Platform.setImplicitExit(false)  // without this, the Platform shuts down when the first window closes...preventing creation of new windows
        TRAY.listener = (object: SystemTrayListener{
            override fun exitRequested() = shutdown()
            override fun openRequested() = showWindow()
            override fun runJobRequsted(job: Job)
            {
                val input_initial_values = HashMap<String, ValueSourceConfiguration>()
                for (descriptor in job.inputs)
                    input_initial_values[descriptor.name] = ValueSourceConfiguration.forValue(descriptor.defaultValueString)
                val window = CollectInputWindow(SimpleProject(), job.inputs, input_initial_values, { input -> runJob(job, input) })
                window.open()
            }
        })
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

    open fun shutdown()
    {
        Platform.runLater(
        {
            main_stage?.close()
            TRAY.teardown()
            exitProcess(0)
        })
    }

    fun showWindow()
    {
        val stage = main_stage
        if (stage == null)
            Platform.runLater(
            {
                val new_window = createMainWindow()
                val new_stage = Stage()
                main_stage = new_stage
                new_window.start(new_stage)
            })
        else
        {
            Platform.runLater({
                stage.show()
                stage.requestFocus()
                stage.toFront()
                stage.isIconified = false
            })
        }
    }

    open fun createMainWindow() : Application
    {
        return MainWindow()
    }

    protected open fun notifyUserJobComplete(run: JobRun, runner: JobRunner)
    {
        val success = run.success ?: return

        var message = "Job ${run.jobId} completed successfully"
        if (!success)
            message = "Job ${run.jobId} failed"
        if (run.message != null)
            message += ": " + run.message

        TRAY.showNotification("Job completed", message, success)
    }

    open fun getMainWindowClass(): Class<Application>
    {
        @Suppress("UNCHECKED_CAST")
        return MainWindow::class.java as Class<Application>
    }

    fun launch()
    {
        Application.launch(getMainWindowClass(), *ARGS)
    }

    companion object
    {
        @JvmStatic
        fun main(args: Array<String>)
        {
            APP = RunnerDesktopApp()
            ARGS = args
            APP.launch()
        }

        lateinit var ARGS: Array<String>
        val TRAY = SystemTrayUI()
        lateinit var APP : RunnerDesktopApp
    }
}