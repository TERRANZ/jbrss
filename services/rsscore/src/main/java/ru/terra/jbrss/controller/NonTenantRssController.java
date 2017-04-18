package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.terra.jbrss.rss.RssCore;
import ru.terra.jbrss.service.FeedPostsService;
import ru.terra.jbrss.service.FeedService;
import ru.terra.jbrss.service.SettingsService;
import ru.terra.jbrss.shared.dto.BooleanDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class NonTenantRssController extends AbstractRssController {
    @Autowired
    private RssCore rssCore;

    @Autowired
    @Qualifier("nonTenantFeedsService")
    private FeedService feedService;

    @Autowired
    @Qualifier("nonTenantFeedPostsService")
    private FeedPostsService feedPostsService;

    @Override
    protected FeedService getFeedService() {
        return feedService;
    }

    @Override
    protected FeedPostsService getFeedPostsService() {
        return feedPostsService;
    }

    @Override
    protected SettingsService getSettingsService() {
        return null;
    }

    @Override
    protected RssCore getRssCore() {
        return rssCore;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @RequestMapping(value = "/createuser", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<BooleanDto> createUser(@RequestParam("uid") String uid) throws IOException {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            jdbcTemplate.execute("CREATE SCHEMA `jbrss3_" + uid + "` ;");
            jdbcTemplate.execute("USE `jbrss3_" + uid + "`;");
            jdbcTemplate.execute(new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/feeds.sql").getFile()))));
            jdbcTemplate.execute(new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/feedposts.sql").getFile()))));
            jdbcTemplate.execute(new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/settings.sql").getFile()))));
            return ResponseEntity.ok(new BooleanDto(true));
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
            getRssCore().start();
            return ResponseEntity.ok(new BooleanDto(true));
        }
    }

}
