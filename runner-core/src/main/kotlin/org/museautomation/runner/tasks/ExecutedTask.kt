package org.museautomation.runner.tasks

import org.museautomation.builtins.plugins.resultstorage.LocalStorageLocationEventType
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
    companion object
    {
        fun fromTransition(transition: StateTransition): ExecutedTask
        {
            val context = transition.context
            val result = transition.result
            
            val start_time = context.eventLog.findFirstEvent(EventTypeMatcher(StartStateTransitionEventType.TYPE_ID)).timestamp
            val end_time = context.eventLog.findFirstEvent(EventTypeMatcher(EndStateTransitionEventType.TYPE_ID)).timestamp

            val message: String
            if (result.transitionSuccess())
                message = "Completed successfully"
            else
                message = result.failureMessage

            var output_folder: String? = null
            val event = transition.taskContext.eventLog.findFirstEvent(EventTypeMatcher(LocalStorageLocationEventType.TYPE_ID))
            if (event != null) {
                if (LocalStorageLocationEventType().getTestPath(event) != null)
                    output_folder = LocalStorageLocationEventType().getTestPath(event)
            }

            return ExecutedTask(context.config.taskId, start_time, end_time, output_folder, result.transitionSuccess(), message, context.config.id)
        }
    }
}