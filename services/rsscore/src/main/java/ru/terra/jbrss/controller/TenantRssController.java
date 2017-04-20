package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.terra.jbrss.rss.RssCore;
import ru.terra.jbrss.service.FeedPostsService;
import ru.terra.jbrss.service.FeedService;
import ru.terra.jbrss.service.SettingsService;
import ru.terra.jbrss.shared.dto.BooleanDto;

import static ru.terra.jbrss.shared.constants.URLConstants.Rss.UPDATE;

@Controller
@RequestMapping("/{uid}")
public class TenantRssController extends AbstractRssController {

    @Autowired
    private RssCore rssCore;

    @Autowired
    @Qualifier("tenantFeedsService")
    private FeedService feedService;

    @Autowired
    @Qualifier("tenantFeedPostsService")
    private FeedPostsService feedPostsService;

    @Autowired
    @Qualifier("tenantSettingService")
    private SettingsService settingsService;

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
        return settingsService;
    }

    @Override
    protected RssCore getRssCore() {
        return rssCore;
    }

    @RequestMapping(value = UPDATE, method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<BooleanDto> update(@PathVariable String uid) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.ok(new BooleanDto(getRssCore().updateSchedulingForUser(uid)));
        }
    }

}
