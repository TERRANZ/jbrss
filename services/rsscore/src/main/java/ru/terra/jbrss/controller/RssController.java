package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.terra.jbrss.db.repos.FeedsRepository;
import ru.terra.jbrss.dto.FeedDto;

@Controller
public class RssController {
    @Autowired
    private FeedsRepository feedsRepository;


    @RequestMapping(value = "/{uid}/feeds", method = RequestMethod.GET)
    public @ResponseBody FeedDto[] allFeeds(@PathVariable Integer uid) {
        return feedsRepository.findByUserid(uid).stream().map(FeedDto::new).toArray(FeedDto[]::new);
    }

}
