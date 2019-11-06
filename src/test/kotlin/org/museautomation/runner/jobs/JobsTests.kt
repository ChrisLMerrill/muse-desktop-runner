package org.museautomation.desktop.runner;

import org.junit.*;
import org.museautomation.desktop.runner.jobs.*;
import org.museautomation.desktop.runner.projects.*;
import org.museautomation.desktop.runner.settings.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JobsTests
    {
    @Test
    public void readJobsFromFiles()
        {
        // point the settings folder to unit test data
        //noinspection ConstantConditions
        SettingsFolder.BASE_FOLDER = new File(getClass().getClassLoader().getResource("home").getFile());

        Jobs jobs = Jobs.get();
        Assert.assertEquals(2, jobs.asList().size());

        Job j1 = jobs.asList().get(0);
        Assert.assertEquals("j1", j1.getId());
        Assert.assertEquals("project1", j1.getProjectId());
        Assert.assertEquals("login-and-buy", j1.getTaskId());

        Job j2 = jobs.asList().get(1);
        Assert.assertEquals("j2", j2.getId());
        Assert.assertEquals("project1", j2.getProjectId());
        Assert.assertEquals("login-generate-report", j2.getTaskId());

        Assert.assertSame(j1, jobs.get("j1"));
        Assert.assertSame(j2, jobs.get("j2"));
        }
    }