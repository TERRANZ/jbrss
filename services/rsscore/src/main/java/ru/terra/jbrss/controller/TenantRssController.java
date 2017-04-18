package ru.terra.jbrss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.terra.jbrss.rss.RssCore;
import ru.terra.jbrss.service.FeedPostsService;
import ru.terra.jbrss.service.FeedService;
import ru.terra.jbrss.service.SettingsService;

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
