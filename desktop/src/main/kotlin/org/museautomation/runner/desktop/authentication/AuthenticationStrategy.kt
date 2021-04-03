package org.museautomation.runner.desktop.authentication

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
interface AuthenticationStrategy
{
    fun requiresAuthentication(): Boolean
    fun isAuthenticated(): Boolean
    fun startAuthentication(listener: AuthenticationListener)
    fun getCredentialsProvider(): CredentialsProvider
    fun getCredentialsValidator(): CredentialsValidator
}