package org.museautomation.runnner.server;


import org.museautomation.runner.projects.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Path("projects")
public class ProjectsService
    {
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProjects()
        {
        System.out.println("listing projects...");
        return Response.status(Response.Status.OK).entity(RegisteredProjects.INSTANCE.asList()).build();
        }
    }