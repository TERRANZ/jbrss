package ru.terra.jbrss.shared.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.terra.jbrss.shared.dto.*;

import java.util.List;

@Service
public class RssService {
    @Autowired
    OAuth2RestOperations oAuth2RestOperations;

    @Value("${rssservice:http://localhost:1111/rss/}")
    String rssServiceUrl;

    public List<FeedDto> getFeeds(String authToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        //oAuthUser.getTokenType(), oAuthUser.getAccessToken()
        headers.add("Authorization", String.format(authToken));
        headers.add("Content-Type", "application/json");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(headers);
//        return oAuth2RestOperations.getForObject(rssServiceUrl + "feed", FeedListDto.class).data;
        ResponseEntity<FeedListDto> result = restTemplate.exchange(rssServiceUrl + "feed", HttpMethod.GET, request, FeedListDto.class);
        return result.getBody().data;

    }

    public List<FeedPostDto> getFeedPosts(String authToken, Integer targetFeed, Integer page, Integer perPage) {
        return oAuth2RestOperations.getForObject(rssServiceUrl + "feed/{fid}/list?page={page}&limit={limit}", FeedPostsPageableDto.class, targetFeed, page, perPage).getPosts();
    }

    public boolean addFeed(String authToken, String url) {
        return oAuth2RestOperations.getForObject(rssServiceUrl + "feed/add?url={url}", BooleanDto.class, url).getStatus();
    }

    public boolean removeFeed(String authToken, Integer feedId) {
        return oAuth2RestOperations.getForObject(rssServiceUrl + "feed/{feedId}/del", BooleanDto.class, feedId).getStatus();
    }

    public void updateSchedulingForUser(String authToken) {
        oAuth2RestOperations.getForObject(rssServiceUrl + "update", BooleanDto.class);
    }

    public void updateSetting(String key, String val, String authToken) {

    }
}
