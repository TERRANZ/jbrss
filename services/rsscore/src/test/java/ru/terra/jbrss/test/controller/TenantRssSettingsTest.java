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
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.db.repos.tenant.TenantSettingRepository;
import ru.terra.jbrss.tenancy.TenantDataStoreAccessor;
import ru.terra.jbrss.test.OAuthHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.terra.jbrss.shared.constants.URLConstants.Rss.SETTINGS;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TenantRssSettingsTest {
    @Autowired
    private OAuthHelper oAuthHelper;
    @Autowired
    private WebApplicationContext webapp;
    @Autowired
    private TenantSettingRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc restMvc;
    private Settings s1;
    public static final String userId = "testuser";

    @Before
    public void setupData() throws URISyntaxException, IOException {
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
        s1 = repository.save(new Settings("key1", "value1"));
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void addSettingTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(put("/" + userId + SETTINGS)
                .param("key", "key2")
                .param("val", "val2")
                .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void delSettingTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(delete("/" + userId + SETTINGS)
                .param("key", s1.getKey())
                .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getSettingTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(get("/" + userId + SETTINGS)
                .param("key", s1.getKey())
                .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void updateSettingTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(post("/" + userId + SETTINGS)
                .param("key", s1.getKey())
                .param("val", "val2")
                .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }
}
