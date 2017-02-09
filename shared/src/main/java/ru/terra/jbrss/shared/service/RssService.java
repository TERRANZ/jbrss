package ru.terra.jbrss.shared.service;

import org.springframework.stereotype.Service;
import ru.terra.jbrss.shared.dto.FeedDto;
import ru.terra.jbrss.shared.dto.FeedPostDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class RssService {
    public List<FeedDto> getFeeds(Integer userId) {
        return new ArrayList<>();
    }

    public List<FeedPostDto> getFeedPosts(Integer targetFeed, Integer page, Integer perPage) {
        return new ArrayList<>();
    }

    public boolean addFeed(Integer userId, String url) {
        return false;
    }

    public boolean removeFeed(Integer feedId) {
        return false;
    }

    public void updateSchedulingForUser(Integer userId) {

    }

    public void updateSetting(String key, String val, Integer userId) {

    }
}
