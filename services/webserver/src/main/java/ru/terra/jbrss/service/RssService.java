package ru.terra.jbrss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.dto.FeedDto;
import ru.terra.jbrss.dto.UserIdDto;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class RssService {

    @Autowired
    OAuth2RestOperations restTemplate;

    public List<FeedDto> getFeeds(Integer userId) {
        FeedDto[] ret = restTemplate.getForObject(URI.create("http://localhost:2224/rss/" + userId + "/feeds"), FeedDto[].class);
        return Arrays.asList(ret);
    }

    public Integer getUserId(OAuth2Authentication authentication) {
        return restTemplate.getForObject(URI.create("http://localhost:2222/acc/user/" + authentication.getOAuth2Request().getClientId() + "/id"), UserIdDto.class).id;
    }
}
