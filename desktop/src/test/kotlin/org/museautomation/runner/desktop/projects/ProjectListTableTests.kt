package org.museautomation.runner.desktop.projects

import javafx.scene.Node
import javafx.scene.input.KeyCode
import net.christophermerrill.testfx.ComponentTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.museautomation.runner.projects.DownloadableProject
import org.museautomation.runner.projects.DownloadableProjectSettings
import org.museautomation.runner.projects.ProjectVersion
import org.museautomation.runner.projects.RegisteredProject
import java.util.*
import java.util.concurrent.atomic.AtomicReference

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectListTableTests: ComponentTest()
{
    @Test
    fun showProjects()
    {
        val local = setupLocalProject()
        val download = setupDownloadedProject()
        val projects = mutableListOf<RegisteredProject>()
        projects.add(local)
        projects.add(download)
        _table.setProjects(projects)
        waitForUiEvents()

        assertTrue(exists(local.name))
        assertTrue(exists(download.name))
        assertTrue(exists(download.download_settings?.installedVersion?.version))
        val formatted_date = _table.date_format.format(download.download_settings?.installedVersion?.date)
        assertTrue(exists(formatted_date))
    }

    @Test
    fun showAddedProject()
    {
        val local = setupLocalProject()
        val projects = mutableListOf<RegisteredProject>()
        projects.add(local)
        _table.setProjects(projects)
        waitForUiEvents()

        val download = setupDownloadedProject()
        assertFalse(exists(download.name))

        _table.add(download)
        waitForUiEvents()

        assertTrue(exists(download.name))
    }

    @Test
    fun showEditedProject()
    {
        val local = setupLocalProject()
        val projects = mutableListOf<RegisteredProject>()
        projects.add(local)
        _table.setProjects(projects)
        waitForUiEvents()

        assertTrue(exists(local.name))

        val old_name = local.name
        val new_name = "new_project_name"
        local.name = new_name
        _table.update(local)
        waitForUiEvents()

        assertFalse(exists(old_name))
        assertTrue(exists(new_name))
    }

    @Test
    fun selectionChanged()
    {
        val local = setupLocalProject()
        val download = setupDownloadedProject()
        _table.setProjects(mutableListOf(local, download))
        waitForUiEvents()

        val selected = AtomicReference<RegisteredProject>()
        _table.addSelectionListener(object : ProjectListTable.SelectionListener {
            override fun selectionChanged(project: RegisteredProject?)
            {
                selected.set(project)
            }
        })

        clickOn(local.name)
        assertEquals(local, selected.get())
        clickOn(download.name)
        assertEquals(download, selected.get())
        press(KeyCode.CONTROL).clickOn(download.name).release(KeyCode.CONTROL)
        assertNull(selected.get())
    }

    private fun setupDownloadedProject(): RegisteredProject
    {
        val version = ProjectVersion(Date().time, 12, "version 12 notes")
        val spec = DownloadableProject("Project #1", "version.url", emptyList())
        val settings = DownloadableProjectSettings("http://download.me/project", spec, version)
        return RegisteredProject("projectD", "Project Downloaded", "/path/to/the/downloaded/project", settings)
    }

    private lateinit var _table: ProjectListTable

    override fun createComponentNode(): Node
    {
        _table = ProjectListTable()
        return _table.getNode()
    }

    companion object
    {
        fun setupLocalProject(): RegisteredProject
        {
            return RegisteredProject("project1", "Project #1", "/path/to/the/project")
        }
    }
}