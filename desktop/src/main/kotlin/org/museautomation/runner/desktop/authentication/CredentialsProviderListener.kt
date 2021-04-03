package org.museautomation.runner.desktop.authentication

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
interface CredentialsProviderListener
{
    fun credentialsProvided(credentials: AuthenticationCredentials)
    fun cancelled()
}