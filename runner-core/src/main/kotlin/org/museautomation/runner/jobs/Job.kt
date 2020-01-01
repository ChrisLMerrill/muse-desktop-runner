package org.museautomation.runner.jobs

import org.musetest.core.values.descriptor.SimpleSubsourceDescriptor

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class Job (var id: String,
                var taskId: String,
                var projectId: String,
                var inputs: List<SimpleSubsourceDescriptor>)