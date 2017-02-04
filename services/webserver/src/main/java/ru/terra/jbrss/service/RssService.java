package ru.terra.jbrss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.service.pojo.EmbeddedView;

import java.net.URI;

@Service
public class RssService {

    @Autowired
    OAuth2RestOperations restTemplate;

    public EmbeddedView getFeeds() {
        EmbeddedView ret = restTemplate.getForObject(URI.create("http://localhost:2224/rss/feedses"), EmbeddedView.class);
        return ret;
    }
}
