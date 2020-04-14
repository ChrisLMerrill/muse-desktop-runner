package org.museautomation.runner.desktop

interface SystemTrayListener
{
    fun openRequested()

    class NoopListener : SystemTrayListener
    {
        override fun openRequested() {}
    }
}

