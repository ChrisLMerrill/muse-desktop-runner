package org.museautomation.runnner.server;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Path("ping")
public class PingService
    {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping()
        {
        return "pong";
        }
    }