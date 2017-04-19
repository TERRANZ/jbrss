package ru.terra.jbrss.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.rss.RssCore;
import ru.terra.jbrss.service.FeedPostsService;
import ru.terra.jbrss.service.FeedService;
import ru.terra.jbrss.service.SettingsService;
import ru.terra.jbrss.shared.dto.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRssController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract FeedService getFeedService();

    protected abstract FeedPostsService getFeedPostsService();

    protected abstract SettingsService getSettingsService();

    protected abstract RssCore getRssCore();

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<FeedListDto> allFeeds() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            FeedListDto feedListDto = new FeedListDto();
            feedListDto.data = getFeedService().getFeeds().stream().map(feed -> {
                FeedDto feedDto = new FeedDto();
                feedDto.setId(feed.getId());
                feedDto.setFeedname(feed.getFeedname());
                feedDto.setFeedurl(feed.getFeedurl());
                feedDto.setUpdateTime(feed.getUpdateTime().getTime());
                return feedDto;
            }).collect(Collectors.toList());
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
            Feeds baseFeeds = getFeedService().findOne(fid);
            if (baseFeeds == null) {
                return ResponseEntity.notFound().build();
            }
            List<FeedPostDto> dtos = getFeedPostsService().findFeedpostsByFeedLimited(fid, offset, limit).stream().map(fp -> {
                FeedPostDto feedPostDto = new FeedPostDto();
                feedPostDto.setId(fp.getId());
                feedPostDto.setFeedId(fp.getFeedId());
                feedPostDto.setPostdate(fp.getPostdate().getTime());
                feedPostDto.setPostlink(fp.getPostlink());
                feedPostDto.setPosttext(fp.getPosttext());
                feedPostDto.setPosttitle(fp.getPosttitle());
                feedPostDto.setRead(fp.isRead());
                return feedPostDto;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(new FeedPostsPageableDto(dtos, getFeedPostsService().countByFeedId(fid)));
        }
    }


    @RequestMapping(value = "/feed/add", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<BooleanDto> addFeedEndPoint(@RequestParam(value = "url") String url) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            try {
                return ResponseEntity.ok(new BooleanDto(addFeed(url)));
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
            Feeds feed = getFeedService().findOne(fid);
            if (feed == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new BooleanDto(removeFeed(fid)));
        }
    }


    public Boolean addFeed(final String url) throws IllegalAccessException {
        if (getFeedService().findByFeedURL(url).isEmpty()) {
            getFeedService().save(new Feeds(0, getRssCore().getDownloader().getFeedTitle(url), url, new Date()));
            return true;
        }
        return false;
    }


    public boolean removeFeed(Integer id) {
        logger.info("Removed posts in feed " + id);
        getFeedPostsService().findByFeedId(id).parallelStream().forEach(fp -> getFeedPostsService().delete(fp));
        try {
            getFeedService().delete(id);
        } catch (Exception e) {
            logger.error("Unable to delete feed " + id, e);
            return false;
        }
        return true;
    }

}
