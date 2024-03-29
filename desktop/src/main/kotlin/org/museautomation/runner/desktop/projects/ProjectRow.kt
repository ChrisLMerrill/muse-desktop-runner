package org.museautomation.runner.desktop.projects
import org.museautomation.runner.projects.RegisteredProject

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectRow(private val table: ProjectListTable, val project: RegisteredProject)
{
    @Suppress("MemberVisibilityCanBePrivate") // used via reflection by table columns
    var name = ""

    @Suppress("MemberVisibilityCanBePrivate") // used via reflection by table columns
    var version = ""

    @Suppress("MemberVisibilityCanBePrivate") // used via reflection by table columns
    var date = ""

    @Suppress("MemberVisibilityCanBePrivate") // used via reflection by table columns
    val updateState: RowUpdateState

    init
    {
        update()
        updateState = if (project.download_settings == null)
            RowUpdateState(ProjectUpdateState.Unavailable, null, null, this)
        else
            RowUpdateState(ProjectUpdateState.Start, null, null, this)
    }

    fun update()
    {
        name = project.name
        val installed = project.download_settings?.installedVersion
        if (installed == null)
        {
            version = ""
            date = ""
        }
        else
        {
            version = installed.version ?: ""
            date = table.date_format.format(installed.date)
        }
    }
}

class RowUpdateState(val update_state: ProjectUpdateState, val message: String?, val available_version: Int?, val row: ProjectRow)

enum class ProjectUpdateState
{
    Unavailable,
    Start,
    Checking,
    ReadyToUpdate,
    Updating,
    Message
}