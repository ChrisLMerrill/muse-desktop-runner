package org.museautomation.runnner.server;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Path("file")
public class FileService
    {
    @GET()
    @Path("exists")
    @Produces(MediaType.TEXT_PLAIN)
    public String fileExists(@QueryParam("path") String path)
        {
        boolean exists = new File(path).exists();
        return Boolean.toString(exists);
        }

    @GET()
    @Path("default-path")
    @Produces(MediaType.TEXT_PLAIN)
    public String fileExists()
        {
        return new File("").getAbsolutePath();
        }
    }