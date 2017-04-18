package ru.terra.jbrss.service;

import ru.terra.jbrss.db.entity.BaseFeeds;

import java.util.List;

public interface FeedService<T extends BaseFeeds> {
    List<T> getFeeds();
}
