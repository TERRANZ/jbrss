package ru.terra.jbrss.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.rss.RssCore;
import ru.terra.jbrss.service.FeedPostsService;
import ru.terra.jbrss.service.FeedService;
import ru.terra.jbrss.service.SettingsService;
import ru.terra.jbrss.shared.dto.FeedDto;
import ru.terra.jbrss.shared.dto.FeedListDto;
import ru.terra.jbrss.shared.dto.FeedPostDto;
import ru.terra.jbrss.shared.dto.FeedPostsPageableDto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.terra.jbrss.shared.constants.URLConstants.Rss.*;

public abstract class AbstractRssController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract FeedService getFeedService();

    protected abstract FeedPostsService getFeedPostsService();

    protected abstract SettingsService getSettingsService();

    protected abstract RssCore getRssCore();

    @RequestMapping(value = FEED, method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<FeedListDto> getFeeds() {
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


    @RequestMapping(value = FEED + FEED_POSTS, method = RequestMethod.GET)
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


    @RequestMapping(value = FEED + ADD, method = RequestMethod.PUT)
    public ResponseEntity addFeedEndPoint(@RequestParam(value = "url") String url) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            if (addFeed(url)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }

    @RequestMapping(value = FEED + DEL, method = RequestMethod.DELETE)
    public ResponseEntity delFeed(@PathVariable Integer fid) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            Feeds feed = getFeedService().findOne(fid);
            if (feed == null) {
                return ResponseEntity.notFound().build();
            }
            if (removeFeed(fid)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }


    private Boolean addFeed(final String url) {
        if (getFeedService().findByFeedURL(url).isEmpty()) {
            getFeedService().save(new Feeds(0, getRssCore().getDownloader().getFeedTitle(url), url, new Date()));
            return true;
        }
        return false;
    }


    private boolean removeFeed(Integer id) {
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

    @RequestMapping(value = SETTINGS, method = RequestMethod.PUT)
    public ResponseEntity addSettings(@RequestParam(value = "key") String key, @RequestParam(value = "val") String value) {
        Settings settings = getSettingsService().get(key);
        if (settings != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            getSettingsService().save(Settings.builder().key(key).value(value).build());
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = SETTINGS, method = RequestMethod.GET)
    public ResponseEntity<String> getSettings(@RequestParam(value = "key") String key) {
        Settings settings = getSettingsService().get(key);
        if (settings == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(settings.getValue());
        }
    }

    @RequestMapping(value = SETTINGS, method = RequestMethod.DELETE)
    public ResponseEntity delSettings(@RequestParam(value = "key") String key) {
        Settings settings = getSettingsService().get(key);
        if (settings == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            getSettingsService().delete(key);
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = SETTINGS, method = RequestMethod.POST)
    public ResponseEntity updateSettings(@RequestParam(value = "key") String key, @RequestParam(value = "val") String value) {
        Settings settings = getSettingsService().get(key);
        if (settings == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            settings.setValue(value);
            getSettingsService().save(settings);
            return ResponseEntity.ok().build();
        }
    }
}
