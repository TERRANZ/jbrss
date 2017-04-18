package ru.terra.jbrss.service;

import ru.terra.jbrss.db.entity.Feeds;

import java.util.List;

public interface FeedService {
    List<Feeds> getFeeds();
}
