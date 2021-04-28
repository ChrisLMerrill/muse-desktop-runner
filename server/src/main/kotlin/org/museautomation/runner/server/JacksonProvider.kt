package org.museautomation.runner.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
class JacksonProvider : ContextResolver<ObjectMapper>
{
    val mapper = ObjectMapper().registerKotlinModule()

    override fun getContext(type: Class<*>?): ObjectMapper
    {
        return mapper
    }
}