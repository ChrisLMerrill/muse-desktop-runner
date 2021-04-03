package org.museautomation.runner.projects

/*
 * The settings for a RegisteredProject that can be downloaded (and updated)
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class DownloadableProjectSettings(var url: String, var spec: DownloadableProject?, var installedVersion: ProjectVersion?)