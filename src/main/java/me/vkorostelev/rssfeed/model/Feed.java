package me.vkorostelev.rssfeed.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Feed {
    private String id = UUID.randomUUID().toString();
    private String folderId;
    private String title;
    private String url;
    private List<FeedItem> items = new ArrayList<>();

    public Feed(String folderId, String title, String url) {
        this.folderId = folderId;
        this.title = title;
        this.url = url;
    }

    public String getId() { return id; }
    public String getFolderId() { return folderId; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public void setTitle(String title) { this.title = title; }
    public void setUrl(String url) { this.url = url; }
    public List<FeedItem> getItems() { return items; }
    public void addItem(FeedItem item) { items.add(item); }
    public void clearItems() { items.clear(); }
}
