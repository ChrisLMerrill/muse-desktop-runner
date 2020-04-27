package org.museautomation.runner.projects

import java.util.*

/*
 * Provides information about a downloadable (and updatable) projet.
 *
 * This object would be downloaded from a server and provides information about the project and its versions,
 * so that a version can be downloaded and registerd...and updated in the future.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class DownloadableProject()
{
    var name: String = ""
    var versionUrl: String = ""
    var latest: ProjectVersion = ProjectVersion()
    var otherVersions: List<ProjectVersion> = mutableListOf()

    constructor(name: String, versionUrl: String, latest: ProjectVersion, otherVersions: List<ProjectVersion>): this()
    {
        this.name = name
        this.versionUrl = versionUrl
        this.latest = latest
        this.otherVersions = otherVersions
    }
}

class ProjectVersion()
{
    var date: Long = Date().time
    var number: Int = 0
    var notes: String = ""

    constructor(date: Long, number: Int, notes: String): this()
    {
        this.date = date
        this.number = number
        this.notes = notes
    }
}