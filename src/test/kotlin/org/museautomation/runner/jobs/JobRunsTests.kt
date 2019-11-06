package org.museautomation.runner.jobs

import org.junit.*
import org.museautomation.desktop.runner.settings.*

import java.io.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class JobRunsTests {
    @Test
    fun readJobRunsFromFiles() {
        // point the settings folder to unit test data
        SettingsFolder.BASE_FOLDER = File(javaClass.classLoader.getResource("home")!!.file)

        Assert.assertEquals(2, JobRuns.asList().size.toLong())

        val r1 = JobRuns.asList()[0]
        Assert.assertEquals("r1", r1.id)
        Assert.assertEquals("job123", r1.jobId)
        Assert.assertEquals(1234L, r1.startTime)
        Assert.assertEquals(5678L, r1.endTime)

        val r2 = JobRuns.asList()[1]
        Assert.assertEquals("r2", r2.id)
        Assert.assertEquals("job456", r2.jobId)
        Assert.assertEquals(111L, r2.startTime)
        Assert.assertEquals(222L, r2.endTime)

        Assert.assertSame(r1, JobRuns["r1"])
        Assert.assertSame(r2, JobRuns["r2"])
    }
}