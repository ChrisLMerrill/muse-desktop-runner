package org.museautomation.runner.projects

/*
 * The settings for a RegisteredProject that is downloaded by the desktop runner
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class DownloadableProjectSettings(var url: String, var spec: DownloadableProject?)