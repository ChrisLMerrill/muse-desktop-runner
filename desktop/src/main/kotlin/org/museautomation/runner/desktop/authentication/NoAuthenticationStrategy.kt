package org.museautomation.runner.desktop.authentication

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class NoAuthenticationStrategy: AuthenticationStrategy
{
    override fun requiresAuthentication(): Boolean
    {
        return false
    }

    override fun isAuthenticated(): Boolean
    {
        return false
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun startAuthentication(auth_listener: AuthenticationListener)
    {
        // no-op
    }

    override fun getCredentialsProvider(): CredentialsProvider
    {
        return object: CredentialsProvider
        {
            override fun startGetCredentials(validator: CredentialsValidator, listener: CredentialsProviderListener)
            {
                // no-op
            }
        }
    }

    override fun getCredentialsValidator(): CredentialsValidator
    {
        return object: CredentialsValidator
        {
            override fun startValidation(credentials: AuthenticationCredentials, listener: CredentialsValidatorListener)
            {
                // no-op
            }
        }
    }
}
