package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.terra.jbrss.db.repos.FeedsRepository;
import ru.terra.jbrss.dto.FeedDto;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RssController {
    @Autowired
    private FeedsRepository feedsRepository;


    @RequestMapping(value = "/feeds/all", method = RequestMethod.GET)
    public List<FeedDto> allFeeds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ArrayList<>();
    }

}
