package ru.terra.jbrss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.terra.jbrss.constants.URLConstants;

@Controller
@RequestMapping(URLConstants.UI.UI)
public class UIController {
//    @Autowired
//    private RssCore rssCore;
//
//    @RequestMapping(URLConstants.UI.MAIN)
//    public String main(Model model) {
//        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("data", rssCore.getFeeds(user.getId()).parallelStream().map(FeedDto::new).collect(Collectors.toList()));
//        return URLConstants.View.MAIN;
//    }
//
//    @RequestMapping(value = URLConstants.UI.LOGIN, method = RequestMethod.GET)
//    public String login() {
//        return URLConstants.View.LOGIN;
//    }
//
//    @RequestMapping(value = URLConstants.UI.FEED, method = RequestMethod.GET)
//    public String feed(@RequestParam(value = "id") Integer id, Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
//        model.addAttribute("data", rssCore.getFeedPosts(id, page, 10).stream().map(FeedPostDto::new).collect(Collectors.toList()));
//        model.addAttribute("page", page + 1);
//        model.addAttribute("id", id);
//        return URLConstants.View.FEED;
//    }
}
