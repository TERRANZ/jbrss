package ru.terra.jbrss.test.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.tenant.TenantFeedPostsRepository;
import ru.terra.jbrss.db.repos.tenant.TenantFeedsRepository;
import ru.terra.jbrss.tenancy.TenantDataStoreAccessor;
import ru.terra.jbrss.test.OAuthHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TenantRssControllerTest {

    @Autowired
    private TenantFeedsRepository feedsRepository;
    @Autowired
    private TenantFeedPostsRepository feedPostsRepository;
    @Autowired
    private OAuthHelper oAuthHelper;
    @Autowired
    private WebApplicationContext webapp;

    private Feeds f1;
    private Feeds f2;
    private Feedposts fp1;
    private Feedposts fp12;
    private Feedposts fp21;
    public static final String userId = "testuser";
    private MockMvc restMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setupData() throws IOException, URISyntaxException {
        restMvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
        TenantDataStoreAccessor.setConfiguration(userId);
        jdbcTemplate.execute(
                new String(
                        Files.readAllBytes(
                                Paths.get(this.getClass().getResource("/mocked.sql").toURI())
                        ),
                        Charset.forName("UTF-8")
                )
        );
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
        ResultActions resultActions = restMvc.perform(get("/" + userId + "/feed").with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void feedPosts() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc
                .perform(get("/" + userId + "/feed/" + f1.getId() + "/list")
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
                .perform(put("/" + userId + "/feed/add")
                        .param("url", "http://url.ru/awd.rss")
                        .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions = restMvc.perform(get("/" + userId + "/feed").with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void delFeedTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc
                .perform(delete("/" + userId + "/feed/" + f2.getId() + "/del")
                        .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions = restMvc.perform(get("/" + userId + "/feed").with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }
}
