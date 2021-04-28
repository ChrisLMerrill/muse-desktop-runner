package org.museautomation.runner.projects

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
interface RegisteredProjectStore {
    fun get(id: String): RegisteredProject?
    fun add(project: RegisteredProject)
    fun getAll(): List<RegisteredProject>

    @Throws(IllegalProjectIdentifierException::class, ProjectAlreadyExistsException::class)
    fun install(project: DownloadableProjectSettings): InstallResult
}

data class InstallResult(val success: Boolean, val message: String?, val exception: Throwable?)