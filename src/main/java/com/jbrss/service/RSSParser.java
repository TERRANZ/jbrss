package com.jbrss.service;

import com.jbrss.model.FeedItem;
import com.jbrss.model.RSSFeed;
import com.rometools.rome.feed.syndication.SyndEntry;
import com.rometools.rome.feed.syndication.SyndFeed;
import java.util.ArrayList;
import java.util.List;

public class RSSParser {
    public static List<FeedItem> parseFeed(String url) throws Exception {
        List<FeedItem> items = new ArrayList<>();
        SyndFeed feed = new SyndFeed(url);
        for (SyndEntry entry : feed.getEntries()) {
            items.add(new FeedItem(
                entry.getTitle(),
                entry.getLink(),
                entry.getDescription() != null ? entry.getDescription().toString() : ""
            ));
        }
        return items;
    }

    public static RSSFeed fetchFeed(String title, String url) throws Exception {
        List<FeedItem> items = parseFeed(url);
        RSSFeed rssFeed = new RSSFeed();
        rssFeed.setTitle(title);
        rssFeed.setUrl(url);
        rssFeed.setItems(items);
        return rssFeed;
    }
}
