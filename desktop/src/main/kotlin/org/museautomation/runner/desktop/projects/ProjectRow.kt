package org.museautomation.runner.desktop.projects
import org.museautomation.runner.projects.RegisteredProject

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectRow(val table: ProjectListTable, val project: RegisteredProject)
{
    var name = ""
    var version = ""
    var date = ""

    init
    {
        update()
    }

    fun update()
    {
        name = project.name
        val latest = project.download_settings?.spec?.latest
        if (latest == null)
        {
            version = ""
            date = ""
        }
        else
        {
            version = latest.number.toString()
            date = table.date_format.format(latest.date)
        }
    }
}