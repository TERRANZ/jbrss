package me.vkorostelev.rssfeed.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Folder {
    private String id = UUID.randomUUID().toString();
    private String name;
    private List<Feed> feeds = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Feed> getFeeds() { return feeds; }
    public void addFeed(Feed feed) { feeds.add(feed); }
    public void removeFeed(Feed feed) { feeds.remove(feed); }
}
