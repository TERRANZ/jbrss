package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.terra.jbrss.db.entity.JbrssUser;
import ru.terra.jbrss.db.repos.UsersRepository;

@Controller
@RequestMapping(value = "/user")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(value = "/{client}/id", method = RequestMethod.GET)
    public
    @ResponseBody
    Integer getId(@PathVariable(value = "client") String client) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            JbrssUser user = usersRepository.findByLogin(authentication.getName());
            if (user != null)
                return user.getId();
            else
                return -1;
        } else
            return -1;
    }
}
