package org.museautomation.runner.desktop.authentication

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
interface CredentialsValidator
{
    fun startValidation(credentials: AuthenticationCredentials, listener: CredentialsValidatorListener)
}