package org.museautomation.runner.tasks

import com.fasterxml.jackson.annotation.JsonIgnore
import org.museautomation.core.events.matching.EventTypeMatcher
import org.museautomation.core.task.state.EndStateTransitionEventType
import org.museautomation.core.task.state.StartStateTransitionEventType
import org.museautomation.core.task.state.StateTransition

/**
 * Represents the execution of a Job. It may not be finished (or even started).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class ExecutedTask (var taskId: String,
                         var startTime: Long?,
                         var endTime: Long?,
                         var resultFolder: String?,
                         var success: Boolean?,
                         var message: String?,
                         var transitionId: String?)

{
    @JsonIgnore
    fun getDuration(): Long
    {
        val start = startTime
        val end = endTime
        if (start== null || end == null)
            return 0
        return end - start
    }

    @SuppressWarnings("unused")
    @Deprecated("Only here for backwards-compability")
    fun setDuration(duration: Long) {}

    companion object
    {
        @SuppressWarnings("unused")  // public API
        fun fromTransition(transition: StateTransition): ExecutedTask
        {
            val context = transition.context
            val result = transition.result

            var output_folder: String? = null
            if (result.taskResult() != null)
                output_folder = result.taskResult().storageLocation
            val start_time = context.eventLog.findFirstEvent(EventTypeMatcher(StartStateTransitionEventType.TYPE_ID)).timestamp
            val end_event = context.eventLog.findFirstEvent(EventTypeMatcher(EndStateTransitionEventType.TYPE_ID))
            var end_time = System.currentTimeMillis()
            if (end_event != null)
                end_time = end_event.timestamp

            val message: String
            if (result.transitionSuccess())
                message = "Completed successfully"
            else
                message = result.failureMessage

            return ExecutedTask(context.config.taskId, start_time, end_time, output_folder, result.transitionSuccess(), message, context.config.id)
        }
    }
}