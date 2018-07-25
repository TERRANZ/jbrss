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
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.nontenant.NonTenantFeedPostsRepository;
import ru.terra.jbrss.db.repos.nontenant.NonTenantFeedsRepository;
import ru.terra.jbrss.test.OAuthHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.terra.jbrss.shared.constants.URLConstants.Rss.FEED;

/**
 * Created by terranz on 01.04.17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NonTenantRssControllerTest {

    @Autowired
    private NonTenantFeedsRepository feedsRepository;
    @Autowired
    private NonTenantFeedPostsRepository feedPostsRepository;
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
        fp1 = feedPostsRepository.save(Feedposts.builder().feedId(f1.getId()).postdate(calendar.getTime()).postlink("l1").posttext("t1").posttitle("t1").build());
        calendar.add(Calendar.HOUR, 1);
        fp12 = feedPostsRepository.save(Feedposts.builder().feedId(f1.getId()).postdate(calendar.getTime()).postlink("l1").posttext("t1").posttitle("t1").build());
        calendar.add(Calendar.HOUR, 1);
        fp21 = feedPostsRepository.save(Feedposts.builder().feedId(f2.getId()).postdate(calendar.getTime()).postlink("l1").posttext("t1").posttitle("t1").build());
    }

    @After
    public void cleanUp() {
        feedPostsRepository.deleteAll();
        feedsRepository.deleteAll();
    }


    @Test
    public void allFeedsTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(get(FEED).with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void feedPosts() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc
                .perform(get(FEED + "/" + f1.getId() + "/list")
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
                .perform(put(FEED + "/add")
                        .param("url", "https://auto.onliner.by/feed")
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
                .perform(delete(FEED + "/" + f2.getId() + "/del")
                        .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions = restMvc.perform(get("/feed").with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }
}
