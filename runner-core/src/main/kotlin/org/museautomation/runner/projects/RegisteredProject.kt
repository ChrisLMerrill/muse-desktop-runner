package org.museautomation.runner.projects

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * A RegisteredProject allows the desktop runner to locate projects and run tasks from that project.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class RegisteredProject(var id: String, var name: String, var path: String, var download_settings: DownloadableProjectSettings?, var version: String?)
{
    @Suppress("unused")
    constructor(id: String, name: String, path: String): this(id, name, path, null, null)

    @Suppress("unused")
    constructor(id: String, name: String, path: String, download_settings: DownloadableProjectSettings?): this(id, name, path, download_settings, null)

    @Suppress("unused")
    constructor(id: String, name: String, path: String, version: String?): this(id, name, path, null, version)
}