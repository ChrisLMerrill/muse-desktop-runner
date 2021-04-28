package org.museautomation.runner.server

import org.museautomation.runner.projects.*
import org.museautomation.runner.server.responses.ErrorResponse
import javax.ws.rs.*
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

    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun downloadProject(project: DownloadableProjectSettings, @PathParam("id") id: String): Response
    {
        try
        {
            project_store.install(project)
            return Response.status(Response.Status.OK).entity(project).build()
        }
        catch (exists: ProjectAlreadyExistsException)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse(exists.message ?: "Project exists")).build()
        }
        catch (illegal_identifier: IllegalProjectIdentifierException)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse(illegal_identifier.message ?: "Illegal project id")).build()
        }
    }

}