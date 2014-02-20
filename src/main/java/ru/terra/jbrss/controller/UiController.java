package ru.terra.jbrss.controller;

import com.sun.jersey.api.core.HttpContext;
import org.apache.log4j.Logger;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.server.controller.AbstractResource;
import sun.misc.IOUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Date: 12.02.14
 * Time: 18:30
 */
@Path(URLConstants.UI.UI)
public class UiController extends AbstractResource {
    private Logger logger = Logger.getLogger(this.getClass());

    @Path(URLConstants.UI.MAIN)
    @GET
    @Produces({"text/html"})
    public Response getMain(@Context HttpContext hc) {
        if (isAuthorized(hc))
            return returnHtmlFile("html/main.html");
        else
            return getLogin(hc);
    }

    @Path(URLConstants.UI.LOGIN)
    @GET
    @Produces({"text/html"})
    public Response getLogin(@Context HttpContext hc) {
        return returnHtmlFile("html/login.html");
    }

    protected Response returnHtmlFile(String fileName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(IOUtils.readFully(new FileInputStream(new File(fileName)), -1, true));
        } catch (IOException e) {
            logger.error("Unable to read file");
            Response response = Response
                    .noContent()
                    .type("text/html")
                    .build();
            return response;
        }
        Response response = Response
                .ok()
                .type("text/html")
                .entity(stream.toByteArray())
                .build();
        return response;
    }
}
