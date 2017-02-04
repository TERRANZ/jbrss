package ru.terra.jbrss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.service.pojo.Feeds;

import java.net.URI;
import java.util.List;

@Service
public class RssService {

    @Autowired
    OAuth2RestOperations restTemplate;

    public List<Feeds> getFeeds() {
        Object ret = restTemplate.getForObject(URI.create("http://localhost:2224/rss/feedses"), Object.class);
        return (List<Feeds>) ret;
    }
}
