package ru.terra.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.terra.dms.db.entity.DmsObject;
import ru.terra.dms.db.service.DmsObjectFieldService;
import ru.terra.dms.db.service.DmsObjectSerivce;
import ru.terra.dms.dto.DmsObjectDto;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Component
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DmsController {

    @Autowired
    private DmsObjectSerivce dmsObjectSerivce;
    @Autowired
    private DmsObjectFieldService dmsObjectFieldService;

    @GET
    public Response getObjects() {
        return Response.ok(dmsObjectSerivce.findAll()).build();
    }

    @GET
    @Path("{id}")
    public Response getObject(@PathParam("id") final String id) {
        final Optional<DmsObject> ret = dmsObjectSerivce.findOne(id);
        if (ret.isPresent()) {
            return Response.ok(dmsObjectSerivce.mapDto(ret.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response postObject(@RequestBody DmsObjectDto dto) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        dmsObjectSerivce.save(dto, authentication.getName());
        return Response.ok().build();
    }
}
