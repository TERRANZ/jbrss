package ru.terra.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.terra.dms.db.entity.DmsObject;
import ru.terra.dms.db.service.DmsObjectFieldService;
import ru.terra.dms.db.service.DmsObjectSerivce;
import ru.terra.dms.dto.DmsObjectDto;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dms")
public class DmsController {

    @Autowired
    private DmsObjectSerivce dmsObjectSerivce;
    @Autowired
    private DmsObjectFieldService dmsObjectFieldService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response getObjects() {
        return Response.ok(dmsObjectSerivce.findAll()).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response getObject(@PathVariable("id") final String id) {
        final Optional<DmsObject> ret = dmsObjectSerivce.findOne(id);
        if (ret.isPresent()) {
            return Response.ok(dmsObjectSerivce.mapDto(ret.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response postObject(@RequestBody DmsObjectDto dto) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        dmsObjectSerivce.save(dto, authentication.getName());
        return Response.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Response deleteObject(@PathVariable("id") final String id) {
        final Optional<DmsObject> ret = dmsObjectSerivce.findOne(id);
        if (ret.isPresent()) {
            dmsObjectSerivce.delete(ret.get());
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Response updateObject(@PathVariable("id") final String id, @RequestBody DmsObjectDto dto) {
        final Optional<DmsObject> ret = dmsObjectSerivce.findOne(id);
        if (ret.isPresent()) {
            dmsObjectSerivce.update(ret.get(), dto);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RequestMapping("/find/byParam")
    public Response findByParam(@RequestParam("param") String param, @RequestParam("val") String val) {
        final List<DmsObject> ret = dmsObjectSerivce.findByParam(param, val);
        return Response.ok(
                ret.parallelStream().map(dmsObjectSerivce::mapDto).collect(Collectors.toList())
        ).build();
    }
}
