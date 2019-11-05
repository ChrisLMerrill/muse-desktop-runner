package org.museautomation.desktop.runner;

import org.junit.*;
import org.museautomation.desktop.runner.projects.*;
import org.museautomation.desktop.runner.settings.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class RegisteredProjectsTests
    {
    @Test
    public void readProjectsFromFile()
        {
        // point the settings folder to unit test data
        //noinspection ConstantConditions
        SettingsFolder.BASE_FOLDER = new File(getClass().getClassLoader().getResource("home").getFile());

        RegisteredProjects projects = RegisteredProjects.get();
        Assert.assertEquals(2, projects.asList().size());

        RegisteredProject p1 = projects.asList().get(0);
        Assert.assertEquals("p1", p1.getId());
        Assert.assertEquals("path1/path2/folder", p1.getPath());
        Assert.assertEquals("Project #1", p1.getName());

        RegisteredProject p2 = projects.asList().get(1);
        Assert.assertEquals("p2", p2.getId());
        Assert.assertEquals("path/to/project", p2.getPath());
        Assert.assertEquals("My second project", p2.getName());

        Assert.assertSame(p1, projects.get("p1"));
        Assert.assertSame(p2, projects.get("p2"));
        }
    }