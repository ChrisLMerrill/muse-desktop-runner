package org.museautomation.runner.desktop.projects

import javafx.scene.Node
import javafx.scene.control.TabPane
import net.christophermerrill.testfx.ComponentTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectsTabTests: ComponentTest()
{
    @Test
    fun addProject()
    {
        internalTestAddProject()
    }

    private fun internalTestAddProject()
    {
        assertFalse(exists(id(RegisteredProjectEditor.EDITOR_NODE_ID)))
        clickOn(id(ProjectsTab.ADD_BUTTON_ID))
        assertTrue(exists(id(RegisteredProjectEditor.EDITOR_NODE_ID)))

        RegisteredProjectEditorTests.fillAndSaveLocalProject(this, "Added Project", "project-added", "/added/path")

        assertFalse(exists(id(RegisteredProjectEditor.EDITOR_NODE_ID)))
        assertTrue(exists("Added Project"))
    }

    @Test
    fun cancelAddThenAdd()
    {
        clickOn(id(ProjectsTab.ADD_BUTTON_ID))
        fillField(id(RegisteredProjectEditor.NAME_FIELD_ID), "cancelled name")
        clickOn(id(RegisteredProjectEditor.CANCEL_BUTTON_ID))
        internalTestAddProject()
    }

    private lateinit var _tab: ProjectsTab

    override fun createComponentNode(): Node
    {
        _tab = ProjectsTab()

        val pane = TabPane()
        pane.tabs.add(_tab.getTab())
        return pane
    }

    override fun getDefaultHeight(): Double
    {
        return 500.0
    }

    override fun getDefaultWidth(): Double
    {
        return 400.0
    }
}