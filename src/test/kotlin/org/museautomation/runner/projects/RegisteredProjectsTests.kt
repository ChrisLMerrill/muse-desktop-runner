package org.museautomation.runner.projects

import org.junit.*
import org.museautomation.desktop.runner.settings.*

import java.io.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class RegisteredProjectsTests {
    @Test
    fun readProjectsFromFile() {
        // point the settings folder to unit test data
        SettingsFolder.BASE_FOLDER = File(javaClass.classLoader.getResource("home")!!.file)

        Assert.assertEquals(2, RegisteredProjects.asList().size.toLong())

        val p1 = RegisteredProjects.asList().get(0)
        Assert.assertEquals("p1", p1.id)
        Assert.assertEquals("path1/path2/folder", p1.path)
        Assert.assertEquals("Project #1", p1.name)

        val p2 = RegisteredProjects.asList().get(1)
        Assert.assertEquals("p2", p2.id)
        Assert.assertEquals("path/to/project", p2.path)
        Assert.assertEquals("My second project", p2.name)

        Assert.assertSame(p1, RegisteredProjects.get("p1"))
        Assert.assertSame(p2, RegisteredProjects.get("p2"))
    }
}