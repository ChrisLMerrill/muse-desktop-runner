package org.museautomation.runner.projects

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectAlreadyExistsException(project_id: String): Exception("Project already exists: " + project_id)