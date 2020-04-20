package org.museautomation.runner.jobs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.museautomation.runner.settings.SettingsFolder

import java.io.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class JobsTests {
    @Test
    fun readJobsFromFiles() {
        // point the settings folder to unit test data
        SettingsFolder.BASE_FOLDER = File(javaClass.classLoader.getResource("home")!!.file)

        assertEquals(2, Jobs.asList().size.toLong())

        val j1 = Jobs.asList().get(0)
        assertEquals("j1", j1.id)
        assertEquals("project1", j1.projectId)
        assertEquals("login-and-buy", j1.taskId)

        val j2 = Jobs.asList().get(1)
        assertEquals("j2", j2.id)
        assertEquals("project1", j2.projectId)
        assertEquals("login-generate-report", j2.taskId)

        assertSame(j1, Jobs.get("j1"))
        assertSame(j2, Jobs.get("j2"))
    }
}