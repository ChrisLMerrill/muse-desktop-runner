package org.museautomation.runner.desktop.authentication

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
interface CredentialsProvider
{
    fun startGetCredentials(validator: CredentialsValidator, listener: CredentialsProviderListener)
}