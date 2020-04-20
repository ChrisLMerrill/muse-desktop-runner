package org.museautomation.runner.jobs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.museautomation.runner.settings.SettingsFolder
import java.io.File

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class JobRunsTests {
    @Test
    fun readJobRunsFromFiles() {
        // point the settings folder to unit test data
        SettingsFolder.BASE_FOLDER = File(javaClass.classLoader.getResource("home")!!.file)

       assertEquals(2, JobRuns.asList().size.toLong())

        val r1 = JobRuns.asList()[0]
        assertEquals("r1", r1.id)
        assertEquals("job123", r1.jobId)
        assertEquals(1234L, r1.startTime)
        assertEquals(5678L, r1.endTime)

        val r2 = JobRuns.asList()[1]
        assertEquals("r2", r2.id)
        assertEquals("job456", r2.jobId)
        assertEquals(111L, r2.startTime)
        assertEquals(222L, r2.endTime)

        assertSame(r1, JobRuns["r1"])
        assertSame(r2, JobRuns["r2"])
    }
}