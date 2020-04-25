package org.museautomation.runner.desktop.projects

import javafx.scene.Node
import net.christophermerrill.testfx.ComponentTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.museautomation.runner.projects.*
import java.util.concurrent.atomic.AtomicReference

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class RegisteredProjectEditorTests: ComponentTest()
{
    @Test
    fun displayLocalProject()
    {
        val project = setupLocalProject()
        waitForUiEvents()

        assertEquals(project.name, textOf(id(RegisteredProjectEditor.NAME_FIELD_ID)))
        assertEquals(project.id, textOf(id(RegisteredProjectEditor.ID_FIELD_ID)))
        assertEquals(project.path, textOf(id(RegisteredProjectEditor.PATH_FIELD_ID)))
        assertFalse(isChecked(id(RegisteredProjectEditor.DOWNLOAD_CHECKBOX_ID)))
        assertTrue(isDisabled(id(RegisteredProjectEditor.URL_FIELD_ID)))
    }

    @Test
    fun displayDownloadedProject()
    {
        val project = setupDownloadedProject()
        waitForUiEvents()

        assertEquals(project.name, textOf(id(RegisteredProjectEditor.NAME_FIELD_ID)))
        assertEquals(project.id, textOf(id(RegisteredProjectEditor.ID_FIELD_ID)))
        assertEquals(project.path, textOf(id(RegisteredProjectEditor.PATH_FIELD_ID)))

        assertTrue(isChecked(id(RegisteredProjectEditor.DOWNLOAD_CHECKBOX_ID)))
        assertFalse(isDisabled(id(RegisteredProjectEditor.URL_FIELD_ID)))
        assertEquals(project.download_settings?.url, textOf(id(RegisteredProjectEditor.URL_FIELD_ID)))
    }

    @Test
    fun createLocalProject()
    {
        _editor.setProject(null)
        setupSaveListener()

        fillField(id(RegisteredProjectEditor.NAME_FIELD_ID), "Project 2")
        fillField(id(RegisteredProjectEditor.ID_FIELD_ID), "project-2")
        fillField(id(RegisteredProjectEditor.PATH_FIELD_ID), "/path/to/project2")
        clickOn(id(RegisteredProjectEditor.SAVE_BUTTON_ID))

        assertNotNull(_saved_project.get())
        val new_project = _saved_project.get()
        assertEquals("project-2", new_project.id)
        assertEquals("Project 2", new_project.name)
        assertEquals("/path/to/project2", new_project.path)
    }

    @Test
    fun createDownloadedProject()
    {
        _editor.setProject(null)
        setupSaveListener()

        fillField(id(RegisteredProjectEditor.NAME_FIELD_ID), "Project 2")
        fillField(id(RegisteredProjectEditor.ID_FIELD_ID), "project-2")
        fillField(id(RegisteredProjectEditor.PATH_FIELD_ID), "/path/to/project2")
        clickOn(id(RegisteredProjectEditor.DOWNLOAD_CHECKBOX_ID))
        fillField(id(RegisteredProjectEditor.URL_FIELD_ID), "my download url")


        clickOn(id(RegisteredProjectEditor.SAVE_BUTTON_ID))

        assertNotNull(_saved_project.get())
        val new_project = _saved_project.get()
        assertEquals("project-2", new_project.id)
        assertEquals("Project 2", new_project.name)
        assertEquals("/path/to/project2", new_project.path)
        assertEquals("my download url", new_project.download_settings?.url)
    }

    @Test
    fun editLocalProject()
    {
        val project = setupLocalProject()
        setupSaveListener()
        waitForUiEvents()

        _editor.setProject(project)
        fillField(id(RegisteredProjectEditor.NAME_FIELD_ID), "Project 2")
        fillField(id(RegisteredProjectEditor.ID_FIELD_ID), "project-2")
        fillField(id(RegisteredProjectEditor.PATH_FIELD_ID), "/path/to/project2")
        clickOn(id(RegisteredProjectEditor.SAVE_BUTTON_ID))

        assertSame(project, _saved_project.get())
        val new_project = _saved_project.get()
        assertEquals("project-2", new_project.id)
        assertEquals("Project 2", new_project.name)
        assertEquals("/path/to/project2", new_project.path)
    }

    @Test
    fun editDownloadedProject()
    {
        val project = setupDownloadedProject()
        setupSaveListener()
        waitForUiEvents()

        fillField(id(RegisteredProjectEditor.URL_FIELD_ID), "my download url")
        clickOn(id(RegisteredProjectEditor.SAVE_BUTTON_ID))

        assertSame(project, _saved_project.get())
        val new_project = _saved_project.get()
        assertEquals("my download url", new_project.download_settings?.url)
    }

    private lateinit var _editor: RegisteredProjectEditor
    private lateinit var _saved_project: AtomicReference<RegisteredProject>

    private fun setupLocalProject(): RegisteredProject
    {
        val project = RegisteredProject("project1", "Project #1", "/path/to/the/project", null)
        _editor.setProject(project)
        return project
    }

    private fun setupDownloadedProject(): RegisteredProject
    {
        val settings = DownloadableProjectSettings("http://download.me/project", null)
        val project = RegisteredProject("projectD", "Project Downloaded", "/path/to/the/downloaded/project", settings)
        _editor.setProject(project)
        return project
    }

    private fun setupSaveListener()
    {
        _editor.setOnAction(object : RegisteredProjectEditor.ActionListener {
            override fun savePressed(project: RegisteredProject)
            {
                _saved_project.set(project)
            }

            override fun cancelPressed(project: RegisteredProject)
            {
            }
        })
    }

    override fun createComponentNode(): Node
    {
        _editor = RegisteredProjectEditor()
        _saved_project = AtomicReference<RegisteredProject>()
        RegisteredProjects.clear()
        return _editor.getNode()
    }
}