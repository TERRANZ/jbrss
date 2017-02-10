package ru.terra.jbrss.shared.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.shared.dto.*;

import java.net.URI;
import java.util.List;

@Service
public class RssService {
    @Autowired
    OAuth2RestOperations restTemplate;

    @Value("${rssservice:http://localhost:2224/rss/}")
    String rssServiceUrl;

    public List<FeedDto> getFeeds(Integer userId) {
        return restTemplate.getForObject(URI.create(rssServiceUrl + "{uid}/feed"), FeedListDto.class).data;
    }

    public List<FeedPostDto> getFeedPosts(Integer userId, Integer targetFeed, Integer page, Integer perPage) {
        return restTemplate.getForObject(rssServiceUrl + "{uid}/feed/{fid}/list?page={page}&limit={limit}", FeedPostsPageableDto.class, userId, targetFeed, page, perPage).getPosts();
    }

    public boolean addFeed(Integer userId, String url) {
        return restTemplate.getForObject(rssServiceUrl + "{uid}/feed/add?url={url}", BooleanDto.class, userId, url).getStatus();
    }

    public boolean removeFeed(Integer userId, Integer feedId) {
        return restTemplate.getForObject(rssServiceUrl + "{uid}/feed/{feedId}/del", BooleanDto.class, userId, feedId).getStatus();
    }

    public void updateSchedulingForUser(Integer userId) {
        restTemplate.getForObject(rssServiceUrl + "{uid}/update", BooleanDto.class, userId);
    }

    public void updateSetting(String key, String val, Integer userId) {

    }
}
