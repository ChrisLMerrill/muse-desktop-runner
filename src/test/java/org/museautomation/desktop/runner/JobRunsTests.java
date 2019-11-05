package org.museautomation.desktop.runner;

import org.junit.*;
import org.museautomation.desktop.runner.jobs.*;
import org.museautomation.desktop.runner.settings.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JobRunsTests
    {
    @Test
    public void readJobRunsFromFiles()
        {
        // point the settings folder to unit test data
        //noinspection ConstantConditions
        SettingsFolder.BASE_FOLDER = new File(getClass().getClassLoader().getResource("home").getFile());

        JobRuns runs = JobRuns.get();
        Assert.assertEquals(2, runs.asList().size());

        JobRun r1 = runs.asList().get(0);
        Assert.assertEquals("r1", r1.getId());
        Assert.assertEquals("job123", r1.getJobId());
        Assert.assertEquals(1234, r1.getStartTime());
        Assert.assertEquals(5678, r1.getEndTime());

        JobRun r2 = runs.asList().get(1);
        Assert.assertEquals("r2", r2.getId());
        Assert.assertEquals("job456", r2.getJobId());
        Assert.assertEquals(111, r2.getStartTime());
        Assert.assertEquals(222, r2.getEndTime());

        Assert.assertSame(r1, runs.get("r1"));
        Assert.assertSame(r2, runs.get("r2"));
        }
    }