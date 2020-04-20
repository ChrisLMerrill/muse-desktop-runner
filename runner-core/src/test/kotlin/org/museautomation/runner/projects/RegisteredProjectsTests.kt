package org.museautomation.runner.projects

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.museautomation.runner.settings.SettingsFolder

import java.io.*

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class RegisteredProjectsTests {
    @Test
    fun readProjectsFromFile() {
        // point the settings folder to unit test data
        SettingsFolder.BASE_FOLDER = File(javaClass.classLoader.getResource("home")!!.file)

        assertEquals(2, RegisteredProjects.asList().size.toLong())

        val p1 = RegisteredProjects.asList().get(0)
        assertEquals("p1", p1.id)
        assertEquals("path1/path2/folder", p1.path)
        assertEquals("Project #1", p1.name)

        val p2 = RegisteredProjects.asList().get(1)
        assertEquals("p2", p2.id)
        assertEquals("path/to/project", p2.path)
        assertEquals("My second project", p2.name)

        assertSame(p1, RegisteredProjects.get("p1"))
        assertSame(p2, RegisteredProjects.get("p2"))
    }
}