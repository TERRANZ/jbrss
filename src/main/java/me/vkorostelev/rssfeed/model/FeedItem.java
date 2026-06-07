package me.vkorostelev.rssfeed.model;

import java.util.UUID;

public class FeedItem {
    private String id = UUID.randomUUID().toString();
    private String feedId;
    private String title;
    private String link;
    private String pubDate;
    private String content;

    public FeedItem(String feedId, String title, String link, String pubDate, String content) {
        this.feedId = feedId;
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.content = content;
    }

    public String getId() { return id; }
    public String getFeedId() { return feedId; }
    public String getTitle() { return title; }
    public String getLink() { return link; }
    public String getPubDate() { return pubDate; }
    public String getContent() { return content; }
}
