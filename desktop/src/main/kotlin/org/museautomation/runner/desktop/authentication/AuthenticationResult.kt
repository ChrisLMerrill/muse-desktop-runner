package org.museautomation.runner.desktop.authentication

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
interface AuthenticationResult
{
    fun isSuccess(): Boolean
    fun isCancelled(): Boolean
    fun getIdentity(): AuthenticatedUserIdentity?
}