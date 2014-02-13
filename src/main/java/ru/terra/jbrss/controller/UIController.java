package ru.terra.jbrss.controller;

import com.sun.jersey.api.core.HttpContext;
import org.apache.log4j.Logger;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.server.controller.AbstractResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Date: 12.02.14
 * Time: 18:30
 */
@Path(URLConstants.UI.UI)
public class UIController extends AbstractResource {
    private Logger logger = Logger.getLogger(this.getClass());

    @Path(URLConstants.UI.MAIN)
    @GET
    @Produces({"text/html"})
    public Response getMain(@Context HttpContext hc) {
        return returnFile("html/main.html");
    }

    @Path(URLConstants.UI.LOGIN)
    @GET
    @Produces({"text/html"})
    public Response getLogin(@Context HttpContext hc) {
        return returnFile("html/login.html");
    }

}
