package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import ru.terra.jbrss.rss.RssCore;
import ru.terra.jbrss.service.FeedPostsService;
import ru.terra.jbrss.service.FeedService;
import ru.terra.jbrss.service.SettingsService;

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

}
