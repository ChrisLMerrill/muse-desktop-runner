package org.museautomation.runner.projects

/**
 * A RegisteredProject allows the desktop runner to locate projects and run tasks from that project.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class RegisteredProject(var id: String, var name: String, var path: String, var download_settings: DownloadableProjectSettings?)