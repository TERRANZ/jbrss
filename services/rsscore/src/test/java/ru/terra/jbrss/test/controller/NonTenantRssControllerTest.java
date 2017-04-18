package ru.terra.jbrss.test.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.terra.jbrss.controller.NonTenantRssController;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.FeedPostsRepository;
import ru.terra.jbrss.db.repos.FeedsRepository;
import ru.terra.jbrss.test.OAuthHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by terranz on 01.04.17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NonTenantRssControllerTest {
    @Autowired
    private NonTenantRssController nonTenantRssController;
    @Autowired
    private FeedsRepository feedsRepository;
    @Autowired
    private FeedPostsRepository feedPostsRepository;
    @Autowired
    private OAuthHelper oAuthHelper;
    @Autowired
    private WebApplicationContext webapp;

    private Feeds f1;
    private Feeds f2;
    private Feedposts fp1;
    private Feedposts fp12;
    private Feedposts fp21;
    public static final String userId = UUID.randomUUID().toString();
    private MockMvc restMvc;

    @Before
    public void setupData() {
        restMvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
        f1 = feedsRepository.save(new Feeds(0, "feed1", "url1", new Date()));
        f2 = feedsRepository.save(new Feeds(0, "feed2", "url2", new Date()));
        Calendar calendar = Calendar.getInstance();
        fp1 = feedPostsRepository.save(new Feedposts(1, f1.getId(), calendar.getTime(), "title1", "link1", "text1"));
        calendar.add(Calendar.HOUR, 1);
        fp12 = feedPostsRepository.save(new Feedposts(2, f1.getId(), calendar.getTime(), "title2", "link2", "text2"));
        calendar.add(Calendar.HOUR, 1);
        fp21 = feedPostsRepository.save(new Feedposts(3, f2.getId(), calendar.getTime(), "title3", "link3", "awdawd"));
    }

    @After
    public void cleanUp() {
        feedPostsRepository.deleteAll();
        feedsRepository.deleteAll();
    }


    @Test
    public void allFeedsTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(get("/feed").with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void feedPosts() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc
                .perform(get("/feed/" + f1.getId() + "/list")
                        .param("page", "0")
                        .param("limit", "1")
                        .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void addFeedTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc
                .perform(get("/feed/add")
                        .param("url", "http://url.ru/awd.rss")
                        .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions = restMvc.perform(get("/feed").with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void delFeedTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc
                .perform(get("/feed/" + f2.getId() + "/del")
                        .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions = restMvc.perform(get("/feed").with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }
}
