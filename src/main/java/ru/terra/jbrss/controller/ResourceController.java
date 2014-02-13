package ru.terra.jbrss.controller;

import com.sun.jersey.api.core.HttpContext;
import org.apache.log4j.Logger;
import ru.terra.jbrss.constants.URLConstants;
import sun.misc.IOUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Date: 13.02.14
 * Time: 11:47
 */
@Path(URLConstants.Resources.RESOURCES)
public class ResourceController {
    private Logger logger = Logger.getLogger(this.getClass());

    @Path("/js/{path}")
    @GET
    public Response getJS(@Context HttpContext hc, @PathParam("path") String path) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        logger.info("Loading file '" + path + "'");

        try {
            stream.write(IOUtils.readFully(new FileInputStream(new File("resources/js/" + path)), -1, true));
        } catch (IOException e) {
            logger.error("Unable to read file", e);
            Response response = Response
                    .noContent()
                    .build();
            return response;
        }
        CacheControl cc = new CacheControl();
        cc.setNoTransform(true);
        cc.setMustRevalidate(false);
        cc.setNoCache(false);
        cc.setMaxAge(3600);
        EntityTag entityTag = new EntityTag(String.valueOf(path.hashCode()));

        Response response = Response
                .ok()
                .cacheControl(cc)
                .tag(entityTag)
                .entity(stream.toByteArray())
                .build();
        return response;
    }

    @Path("/js/images/{path}")
    @GET
    public Response getImages(@Context HttpContext hc, @PathParam("path") String path) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        logger.info("Loading file '" + path + "'");

        try {
            stream.write(IOUtils.readFully(new FileInputStream(new File("resources/js/images/" + path)), -1, true));
        } catch (IOException e) {
            logger.error("Unable to read file", e);
            Response response = Response
                    .noContent()
                    .build();
            return response;
        }
        CacheControl cc = new CacheControl();
        cc.setNoTransform(true);
        cc.setMustRevalidate(false);
        cc.setNoCache(false);
        cc.setMaxAge(3600);
        EntityTag entityTag = new EntityTag(String.valueOf(path.hashCode()));

        Response response = Response
                .ok()
                .cacheControl(cc)
                .tag(entityTag)
                .entity(stream.toByteArray())
                .build();
        return response;
    }
}
