package ru.terra.jbrss.test.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.terra.jbrss.service.FeedPostsService;


public class NonTenantFeedPostsServiceImplTest {
    @Autowired
    @Qualifier("nonTenantFeedPostsService")
    private FeedPostsService feedPostsService;

    @Test
    public void findFeedpostsByFeedLimited() throws Exception {
    }

    @Test
    public void countByFeedId() throws Exception {
    }

    @Test
    public void getLastPostInFeed() throws Exception {
    }

    @Test
    public void save() throws Exception {
    }

    @Test
    public void findByFeedId() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void findByPosttextLike() throws Exception {
    }

    @Test
    public void findOne() throws Exception {
    }

}