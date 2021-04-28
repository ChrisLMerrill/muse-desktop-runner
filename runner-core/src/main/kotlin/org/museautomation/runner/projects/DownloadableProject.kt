package org.museautomation.runner.projects

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vdurmont.semver4j.Semver

/*
 * Provides information about a downloadable (and updatable) projet.
 *
 * This object would be downloaded from a server and provides information about the project and its versions,
 * so that a version can be downloaded and registerd...and updated in the future.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class DownloadableProject(val name: String, val url: String, val versions: List<ProjectVersion>)
{
//    var name: String = ""
//    var versionUrl: String = ""
//    var otherVersions: List<ProjectVersion> = mutableListOf()

    @JsonIgnore
    fun getLatest(): ProjectVersion?
    {
        return getNewest(null)
    }

    fun getNewest(current: ProjectVersion?): ProjectVersion?
    {
        var latest_project: ProjectVersion? = current
        for (project in versions)
        {
            if (latest_project == null)
                latest_project = project
            else if (Semver(latest_project.version).isGreaterThan(Semver(project.version)))
                latest_project = project
        }
        return latest_project
    }
}

data class ProjectVersion(val date: Long, val version: String, val notes: String)
{
    constructor(date: Long, number: Int, notes: String): this(date, number.toString(), notes)
}