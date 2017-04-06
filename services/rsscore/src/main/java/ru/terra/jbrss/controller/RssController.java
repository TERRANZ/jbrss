package ru.terra.jbrss.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.FeedPostsRepository;
import ru.terra.jbrss.db.repos.FeedsRepository;
import ru.terra.jbrss.rss.RssCore;
import ru.terra.jbrss.shared.dto.*;

import java.util.stream.Collectors;

@Controller
public class RssController {
    @Autowired
    private FeedsRepository feedsRepository;
    @Autowired
    private FeedPostsRepository feedPostsRepository;
    @Autowired
    private RssCore rssCore;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/{uid}/feed", method = RequestMethod.GET)
    public
    @ResponseBody
    FeedListDto allFeeds(@PathVariable Integer uid) {
        FeedListDto feedListDto = new FeedListDto();
        feedListDto.data = feedsRepository.findByUserid(uid).stream().map(u -> {
            FeedDto feedDto = new FeedDto();
            feedDto.setId(u.getId());
            feedDto.setFeedname(u.getFeedname());
            feedDto.setFeedurl(u.getFeedurl());
            feedDto.setUpdateTime(u.getUpdateTime().getTime());
            return feedDto;
        }).collect(Collectors.toList());
        return feedListDto;
    }

    @RequestMapping(value = "/{uid}/feed/{fid}/list", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<FeedPostsPageableDto> feedPosts(@PathVariable Integer uid,
                                                   @PathVariable Integer fid,
                                                   @RequestParam(value = "page", defaultValue = "0", required = false) Integer offset,
                                                   @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {
        Feeds feeds = feedsRepository.findOne(fid);
        if (feeds == null) {
            return ResponseEntity.notFound().build();
        }
        if (feeds.getUserid() != uid) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new FeedPostsPageableDto(
                feedPostsRepository.findFeedpostsByFeedLimited(fid, offset, limit).stream().map(fp -> {
                    FeedPostDto feedPostDto = new FeedPostDto();
                    feedPostDto.setId(fp.getId());
                    feedPostDto.setFeedId(fp.getFeedId());
                    feedPostDto.setPostdate(fp.getPostdate().getTime());
                    feedPostDto.setPostlink(fp.getPostlink());
                    feedPostDto.setPosttext(fp.getPosttext());
                    feedPostDto.setPosttitle(fp.getPosttitle());
                    feedPostDto.setRead(fp.isRead());
                    return feedPostDto;
                }).collect(Collectors.toList()),
                feedPostsRepository.countByFeedId(fid)
        ));
    }

    @RequestMapping(value = "/{uid}/feed/add", method = RequestMethod.GET)
    public
    @ResponseBody
    BooleanDto addFeed(@PathVariable Integer uid, @RequestParam(value = "url") String url) {
        try {
            return new BooleanDto(rssCore.addFeed(uid, url));
        } catch (IllegalAccessException e) {
            logger.error("Unable to add feed to user " + uid + " url: " + url, e);
            return new BooleanDto(false);
        }
    }

    @RequestMapping(value = "/{uid}/feed/{fid}/del", method = RequestMethod.GET)
    public
    @ResponseBody
    BooleanDto delFeed(@PathVariable Integer uid, @PathVariable Integer fid) {
        return new BooleanDto(rssCore.removeFeed(fid));
    }

    @RequestMapping(value = "/{uid}/update", method = RequestMethod.GET)
    public
    @ResponseBody
    BooleanDto update(@PathVariable Integer uid) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        return new BooleanDto(rssCore.updateSchedulingForUser(uid));
    }
}
