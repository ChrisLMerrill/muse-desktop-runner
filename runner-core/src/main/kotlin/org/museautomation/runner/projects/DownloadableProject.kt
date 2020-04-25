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
data class DownloadableProject(val name: String, val version_url: String, val latest: DownloadableProjectVersion, val other_versions: List<DownloadableProjectVersion>)

data class DownloadableProjectVersion(val date: Date, val number: Int, val notes: String)