package ru.terra.jbrss.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.FeedPostsRepository;
import ru.terra.jbrss.db.repos.FeedsRepository;
import ru.terra.jbrss.rss.RssCore;
import ru.terra.jbrss.service.NonTenantFeedServiceImpl;
import ru.terra.jbrss.service.TenantFeedServiceImpl;
import ru.terra.jbrss.shared.dto.*;

import java.util.stream.Collectors;

@Controller
public class NonTenantRssController {
    @Autowired
    private FeedsRepository feedsRepository;
    @Autowired
    private FeedPostsRepository feedPostsRepository;
    @Autowired
    private RssCore rssCore;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TenantFeedServiceImpl tenantFeedService;

    @Autowired
    private NonTenantFeedServiceImpl nonTenantFeedService;

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<FeedListDto> allFeeds() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            FeedListDto feedListDto = new FeedListDto();
            feedListDto.data = tenantFeedService.getFeeds().stream().map(feed -> {
                FeedDto feedDto = new FeedDto();
                feedDto.setId(feed.getId());
                feedDto.setFeedname(feed.getFeedname());
                feedDto.setFeedurl(feed.getFeedurl());
                feedDto.setUpdateTime(feed.getUpdateTime().getTime());
                return feedDto;
            }).collect(Collectors.toList());
            feedListDto.data.addAll(nonTenantFeedService.getFeeds().stream().map(feed -> {
                FeedDto feedDto = new FeedDto();
                feedDto.setId(feed.getId());
                feedDto.setFeedname(feed.getFeedname());
                feedDto.setFeedurl(feed.getFeedurl());
                feedDto.setUpdateTime(feed.getUpdateTime().getTime());
                return feedDto;
            }).collect(Collectors.toList()));
            return ResponseEntity.ok(feedListDto);
        }
    }

    @RequestMapping(value = "/feed/{fid}/list", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<FeedPostsPageableDto> feedPosts(@PathVariable Integer fid,
                                                   @RequestParam(value = "page", defaultValue = "0", required = false) Integer offset,
                                                   @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            Feeds feeds = feedsRepository.findOne(fid);
            if (feeds == null) {
                return ResponseEntity.notFound().build();
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
    }

    @RequestMapping(value = "/feed/add", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<BooleanDto> addFeed(@RequestParam(value = "url") String url) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            try {
                return ResponseEntity.ok(new BooleanDto(rssCore.addFeed(authentication.getOAuth2Request().getClientId(), url)));
            } catch (IllegalAccessException e) {
                logger.error("Unable to add feed to user " + authentication.getOAuth2Request().getClientId() + " url: " + url, e);
                return ResponseEntity.ok(new BooleanDto(false));
            }
        }
    }

    @RequestMapping(value = "/feed/{fid}/del", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<BooleanDto> delFeed(@PathVariable Integer fid) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            Feeds feed = feedsRepository.findOne(fid);
            if (feed == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new BooleanDto(rssCore.removeFeed(fid)));
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<BooleanDto> update() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.ok(new BooleanDto(rssCore.updateSchedulingForUser(authentication.getOAuth2Request().getClientId())));
        }
    }
}
