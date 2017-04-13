package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.terra.jbrss.db.OAuthDataService;
import ru.terra.jbrss.db.entity.JbrssUser;
import ru.terra.jbrss.db.repos.UsersRepository;
import ru.terra.jbrss.shared.dto.UserIdDto;
import ru.terra.jbrss.shared.dto.UserIdListDto;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/user")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private OAuthDataService oAuthDataService;

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
                return new UserIdDto("");
        } else
            return new UserIdDto("");
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
    public @ResponseBody
    Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/create")
    @Transactional
    public @ResponseBody
    UserIdDto create(@RequestParam(value = "login") String login, @RequestParam(value = "pass") String pass) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            if (usersRepository.findByLogin(login) != null) {
                return new UserIdDto("");
            } else {
                JbrssUser newUser = new JbrssUser();
                newUser.setLogin(login);
                newUser.setPassword(pass);
                newUser = usersRepository.save(newUser);
                oAuthDataService.addOAuthUser(login, pass);
                return new UserIdDto(newUser.getId());
            }
        } else
            return new UserIdDto("");
    }

    @RequestMapping("/login")
    public @ResponseBody
    UserIdDto login(@RequestParam(value = "login") String login, @RequestParam(value = "pass") String pass) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            JbrssUser user = usersRepository.findByLoginAndPassword(login, pass);
            if (user != null) {
                return new UserIdDto(user.getId());
            } else {
                return new UserIdDto("");
            }
        } else
            return new UserIdDto("");
    }
}
