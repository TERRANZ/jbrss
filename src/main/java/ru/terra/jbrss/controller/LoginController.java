package ru.terra.jbrss.controller;


import com.sun.jersey.api.core.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.engine.UsersEngine;
import ru.terra.server.controller.AbstractController;
import ru.terra.server.controller.AbstractResource;
import ru.terra.server.dto.LoginDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path(URLConstants.DoJson.Login.LOGIN)
public class LoginController extends AbstractResource {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private UsersEngine usersEngine = new UsersEngine();

    @GET
    @Path(URLConstants.DoJson.Login.LOGIN_DO_LOGIN_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoginDTO login(@Context HttpContext hc) {
        String user = getParameter(hc, "user");
        String pass = getParameter(hc, "pass");
        logger.info("User requests login with user = " + user + " and pass = " + pass);

        LoginDTO ret = new LoginDTO();
        ret.logged = false;
        if (user != null && pass != null) {
            User u = usersEngine.login(user, pass);
            if (u != null) {
                ret.id = u.getId();
                ret.session = sessionsHolder.registerUserSession(u);
                ret.logged = true;
            } else {
                ret.message = "user not found";
            }
        } else {
            ret.message = "invalid parameters";
        }

        return ret;
    }
}
