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
import ru.terra.jbrss.shared.dto.UserIdDto;
import ru.terra.jbrss.shared.dto.UserIdListDto;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/user")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(value = "/{client}/id", method = RequestMethod.GET)
    public
    @ResponseBody
    UserIdDto getId(@PathVariable(value = "client") String client) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            JbrssUser user = usersRepository.findByLogin(client);
            if (user != null)
                return new UserIdDto(user.getId());
            else
                return new UserIdDto(-1);
        } else
            return new UserIdDto(-1);
    }

    @RequestMapping(value = "/ids", method = RequestMethod.GET)
    public
    @ResponseBody
    UserIdListDto allIds() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            UserIdListDto ret = new UserIdListDto();
            ret.data = usersRepository.findAll().stream().map(u -> new UserIdDto(u.getId())).collect(Collectors.toList());
            return ret;
        }
        return new UserIdListDto();
    }

    @RequestMapping("/")
    public Principal user(Principal user) {
        return user;
    }
}
