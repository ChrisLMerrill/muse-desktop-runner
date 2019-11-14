package org.museautomation.runner.desktop

import org.museautomation.runner.jobs.Job

interface SystemTrayListener
{
    fun exitRequested()
    fun openRequested()
    fun runJobRequsted(job: Job)

    class NoopListener : SystemTrayListener
    {
        override fun exitRequested() {}
        override fun openRequested() {}
        override fun runJobRequsted(job: Job) {}
    }
}

