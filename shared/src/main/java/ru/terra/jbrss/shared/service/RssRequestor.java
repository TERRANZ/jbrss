package ru.terra.jbrss.shared.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.terra.jbrss.shared.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.terra.jbrss.shared.constants.URLConstants.Rss.*;

@Service
public class RssRequestor {
    @Autowired
    OAuth2RestOperations oAuth2RestOperations;

    @Value("${rssservice:http://localhost:1111/rss/}")
    String rssServiceUrl;

    private HttpEntity<String> prepEntity(String authToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", String.format(authToken));
        headers.add("Content-Type", "application/json");
        return new HttpEntity<>(headers);
    }

    protected String url(String uid) {
        return rssServiceUrl + uid;
    }

    public List<FeedDto> getFeeds(String authToken, String uid) {
        ResponseEntity<FeedListDto> result = new RestTemplate().exchange(url(uid) + FEED, HttpMethod.GET, prepEntity(authToken), FeedListDto.class);
        return result.getBody().data;
    }

    public List<FeedPostDto> getFeedPosts(String authToken, Integer targetFeed, Integer page, Integer perPage, String uid) {
        ResponseEntity<FeedPostsPageableDto> result = new RestTemplate().exchange(url(uid) + FEED + "/" + targetFeed + "/list?page=" + page + "&limit=" + perPage, HttpMethod.GET, prepEntity(authToken), FeedPostsPageableDto.class);
        return result.getBody().getPosts();
    }

    public boolean addFeed(String authToken, String url, String uid) {
        ResponseEntity result = new RestTemplate().exchange(url(uid) + FEED + ADD + "?url=" + url, HttpMethod.PUT, prepEntity(authToken), BooleanDto.class);
        return result.getStatusCode().equals(HttpStatus.OK);
    }

    public boolean removeFeed(String authToken, Integer feedId, String uid) {
        ResponseEntity result = new RestTemplate().exchange(url(uid) + FEED + "/" + feedId + "/del", HttpMethod.DELETE, prepEntity(authToken), BooleanDto.class);
        return result.getStatusCode().equals(HttpStatus.OK);
    }

    public boolean updateSchedulingForUser(String authToken, String uid) {
        ResponseEntity<BooleanDto> result = new RestTemplate().exchange(url(uid) + UPDATE, HttpMethod.GET, prepEntity(authToken), BooleanDto.class);
        return result.getBody().getStatus();
    }

    public void updateSetting(String key, String val, String authToken, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("key", key);
        params.put("val", val);
        ResponseEntity result = new RestTemplate().exchange(url(uid) + SETTINGS, HttpMethod.POST, prepEntity(authToken), Void.class, params);
    }

    public void createUser(String uid) {
        oAuth2RestOperations.getForEntity(rssServiceUrl + "/createuser?uid=" + uid, BooleanDto.class);
    }
}
