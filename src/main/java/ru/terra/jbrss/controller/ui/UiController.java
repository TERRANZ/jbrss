package ru.terra.jbrss.controller.ui;

import com.sun.jersey.api.core.HttpContext;
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
public class UiController extends AbstractResource {
    @Path(URLConstants.UI.MAIN)
    @GET
    @Produces({"text/html"})
    public Response getMain(@Context HttpContext hc) {
        return returnHtmlFile("html/main.html");
    }
}
