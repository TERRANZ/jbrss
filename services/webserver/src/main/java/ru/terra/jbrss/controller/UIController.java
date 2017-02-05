package ru.terra.jbrss.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.service.RssService;

import java.io.IOException;

@Controller
@RequestMapping(URLConstants.UI.UI)
public class UIController {

    @Autowired
    private RssService rssService;

    @RequestMapping(URLConstants.UI.MAIN)
    public String main(Model model) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        authentication.toString();
//        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("data", rssCore.getFeeds(user.getId()).parallelStream().map(FeedDto::new).collect(Collectors.toList()));
        return URLConstants.View.MAIN;
    }

    @RequestMapping(value = URLConstants.UI.LOGIN, method = RequestMethod.GET)
    public String login() {
        return URLConstants.View.LOGIN;
    }

    @RequestMapping(value = URLConstants.UI.FEED, method = RequestMethod.GET)
    public String feed(@RequestParam(value = "id") Integer id, Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
//        model.addAttribute("data", rssCore.getFeedPosts(id, page, 10).stream().map(FeedPostDto::new).collect(Collectors.toList()));
//        model.addAttribute("page", page + 1);
//        model.addAttribute("id", id);
        return URLConstants.View.FEED;
    }

    @RequestMapping("/test")
    public
    @ResponseBody
    String test() throws IOException {
        return new ObjectMapper().writeValueAsString(rssService.getCurrentUserId());
    }
}
