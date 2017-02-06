package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.terra.jbrss.db.repos.FeedPostsRepository;
import ru.terra.jbrss.db.repos.FeedsRepository;
import ru.terra.jbrss.dto.FeedDto;
import ru.terra.jbrss.dto.FeedPostDto;
import ru.terra.jbrss.dto.FeedPostsPageableDto;

import java.util.stream.Collectors;

@Controller
public class RssController {
    @Autowired
    private FeedsRepository feedsRepository;
    @Autowired
    private FeedPostsRepository feedPostsRepository;

    @RequestMapping(value = "/{uid}/feed", method = RequestMethod.GET)
    public
    @ResponseBody
    FeedDto[] allFeeds(@PathVariable Integer uid) {
        return feedsRepository.findByUserid(uid).stream().map(FeedDto::new).toArray(FeedDto[]::new);
    }

    @RequestMapping(value = "/{uid}/feed/{fid}/list", method = RequestMethod.GET)
    public
    @ResponseBody
    FeedPostsPageableDto feedPosts(@PathVariable Integer fid,
                                   @RequestParam(value = "page", defaultValue = "0", required = false) Integer offset,
                                   @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {
        return new FeedPostsPageableDto(
                feedPostsRepository.findFeedpostsByFeedLimited(fid, offset, limit).stream().map(FeedPostDto::new).collect(Collectors.toList()),
                feedPostsRepository.countByFeedId(fid)
        );
    }


}
