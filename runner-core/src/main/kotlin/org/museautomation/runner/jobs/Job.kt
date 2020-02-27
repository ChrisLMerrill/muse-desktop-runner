package org.museautomation.runner.jobs

import org.museautomation.core.values.descriptor.SimpleSubsourceDescriptor

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class Job (var id: String,
                var taskId: String,
                var projectId: String,
                var inputs: List<SimpleSubsourceDescriptor>,
                var outputFile: String?,
                var outputs: List<JobOutputSpec>,
                var successMessageFormat: String?)