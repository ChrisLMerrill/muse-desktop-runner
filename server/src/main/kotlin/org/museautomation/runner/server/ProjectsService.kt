package org.museautomation.runner.server

import org.museautomation.runner.projects.RegisteredProjectStore
import org.museautomation.runner.projects.RegisteredProjects
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Path("/project")
class ProjectsService
{
    var project_store: RegisteredProjectStore = RegisteredProjects

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    fun listProjects(): Response
    {
        println("listing projects...")
        return Response.status(Response.Status.OK).entity(project_store.getAll()).build()
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") id: String): Response
    {
        val project = project_store.get(id)
        if (project == null)
            return Response.status(Response.Status.NOT_FOUND).build()
        else
            return Response.status(Response.Status.OK).entity(project).build()
    }
}