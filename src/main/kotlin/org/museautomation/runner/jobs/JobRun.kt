package org.museautomation.runner.jobs

/**
 * Represents the execution of a Job. It may not be finished (or even started).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class JobRun (var id: String? = null,
                   var jobId: String? = null,
                   var startTime: Long? = null,
                   var endTime: Long? = null)