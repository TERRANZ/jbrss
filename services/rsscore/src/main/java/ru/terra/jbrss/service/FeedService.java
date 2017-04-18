package ru.terra.jbrss.service;

import ru.terra.jbrss.db.entity.Feeds;

import java.util.List;

public interface FeedService {
    List<Feeds> getFeeds();

    Feeds findOne(Integer fid);

    List<Feeds> findByFeedURL(String url);

    void save(Feeds feeds);

    void delete(Integer id);
}
