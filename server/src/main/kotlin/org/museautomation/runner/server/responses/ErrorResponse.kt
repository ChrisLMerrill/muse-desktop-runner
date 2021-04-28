package org.museautomation.runner.server.responses

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
data class ErrorResponse(val message: String, val error: Boolean = true)