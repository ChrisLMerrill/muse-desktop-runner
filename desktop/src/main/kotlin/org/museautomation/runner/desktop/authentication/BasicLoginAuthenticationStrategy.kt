package org.museautomation.runner.desktop.authentication

import javafx.application.Platform
import javafx.stage.Stage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Suppress("unused")  // expected to be used by extensions of the desktop runner
open class BasicLoginAuthenticationStrategy: AuthenticationStrategy
{
    override fun requiresAuthentication(): Boolean
    {
        return true
    }

    override fun isAuthenticated(): Boolean
    {
        return false
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun startAuthentication(auth_listener: AuthenticationListener)
    {
        GlobalScope.launch {
            val provider_listener = object: CredentialsProviderListener
            {
                override fun credentialsProvided(credentials: AuthenticationCredentials)
                {
                    auth_listener.authenticationComplete(object: AuthenticationResult
                    {
                        override fun isSuccess(): Boolean
                        {
                            return true
                        }

                        override fun isCancelled(): Boolean
                        {
                            return false
                        }

                        override fun getIdentity(): AuthenticatedUserIdentity
                        {
                            if (credentials is BasicLoginCredentials)
                                return object: AuthenticatedUserIdentity
                                {
                                    override fun getName(): String
                                    {
                                        return credentials.username
                                    }
                                }
                            else return object: AuthenticatedUserIdentity
                            {
                                override fun getName(): String
                                {
                                    return "unknown"
                                }
                            }
                        }
                    })
                }

                override fun cancelled()
                {
                    auth_listener.authenticationComplete(object: AuthenticationResult
                    {
                        override fun isSuccess(): Boolean
                        {
                            return false
                        }

                        override fun isCancelled(): Boolean
                        {
                            return true
                        }

                        override fun getIdentity(): AuthenticatedUserIdentity?
                        {
                            return null
                        }
                    })
                }
            }
            getCredentialsProvider().startGetCredentials(getCredentialsValidator(), provider_listener)
        }
    }

    override fun getCredentialsProvider(): CredentialsProvider
    {
        return object: CredentialsProvider
        {
            override fun startGetCredentials(validator: CredentialsValidator, listener: CredentialsProviderListener)
            {
                Platform.runLater {
                    val window = BasicCredentialsInputWindow(Stage(), validator, listener)
                    window.show()
                }
            }
        }
    }

    override fun getCredentialsValidator(): CredentialsValidator
    {
        return object: CredentialsValidator
        {
            override fun startValidation(credentials: AuthenticationCredentials, listener: CredentialsValidatorListener)
            {
                if (credentials is BasicLoginCredentials && credentials.username.length > 0 && credentials.password.length > 0)
                    listener.validationSuccess()
                else
                    listener.validationFailure()
            }
        }
    }
}
