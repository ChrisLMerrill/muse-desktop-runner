package org.museautomation.runner.desktop

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.museautomation.runner.JobRunner
import org.museautomation.runner.jobs.Job
import org.museautomation.runner.jobs.JobRun
import org.museautomation.runner.jobs.JobRuns
import kotlin.system.exitProcess

/*
 * The core of the application. Opens new windows as needed (e.g. at startup) and puts an icon in the System Tray.
 * Wne closing the main UI window, the app continues to run, accessible to the user via the system tray.
 */
class RunnerDesktopApp
{
    var main_stage: Stage? = null

    init
    {
        TRAY.listener = (object: SystemTrayListener{
            override fun exitRequested() = shutdown()
            override fun openRequested() = showWindow()
            override fun runJobRequsted(job: Job)
            {
                val run = JobRun("r" + System.currentTimeMillis(), job.id, System.currentTimeMillis(), null, null, null)
                JobRunner().run(run)
                run.endTime = System.currentTimeMillis()
                JobRuns.save(run)
                notifyUserJobComplete(run)
            }
        })
    }

    fun shutdown()
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
            createWindow()
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

    fun createWindow()
    {
        Platform.runLater(
        {
            val new_window = MainWindow()
            val new_stage = Stage()
            main_stage = new_stage
            new_window.start(new_stage)
        })
    }

    fun notifyUserJobComplete(run: JobRun)
    {
        val success = run.success
        if (success == null)
            return

        var message = "Job ${run.jobId} completed successfully"
        if (!success)
            message = "Job ${run.jobId} failed"
        
        TRAY.showNotification("Job completed", message, success)
    }

    companion object
    {
        @JvmStatic
        fun main(args: Array<String>)
        {
            Platform.setImplicitExit(false)  // without this, the Platform shuts down when the first window closes...preventing creation of new windows
            ARGS = args
            Application.launch(MainWindow::class.java, *ARGS)
        }

        lateinit var ARGS: Array<String>
        val TRAY = SystemTrayUI()
        var APP = RunnerDesktopApp()
    }
}