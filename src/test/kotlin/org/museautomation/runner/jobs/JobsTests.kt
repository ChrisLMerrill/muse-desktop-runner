package org.museautomation.runner.jobs

import org.junit.*
import org.museautomation.desktop.runner.settings.*

import java.io.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class JobsTests {
    @Test
    fun readJobsFromFiles() {
        // point the settings folder to unit test data
        SettingsFolder.BASE_FOLDER = File(javaClass.classLoader.getResource("home")!!.file)

        Assert.assertEquals(2, Jobs.asList().size.toLong())

        val j1 = Jobs.asList().get(0)
        Assert.assertEquals("j1", j1.id)
        Assert.assertEquals("project1", j1.projectId)
        Assert.assertEquals("login-and-buy", j1.taskId)

        val j2 = Jobs.asList().get(1)
        Assert.assertEquals("j2", j2.id)
        Assert.assertEquals("project1", j2.projectId)
        Assert.assertEquals("login-generate-report", j2.taskId)

        Assert.assertSame(j1, Jobs.get("j1"))
        Assert.assertSame(j2, Jobs.get("j2"))
    }
}