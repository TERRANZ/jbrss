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
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.db.repos.nontenant.NonTenantSettingRepository;
import ru.terra.jbrss.test.OAuthHelper;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.terra.jbrss.shared.constants.URLConstants.Rss.SETTINGS;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NonTenantRssSettingsTest {
    @Autowired
    private OAuthHelper oAuthHelper;
    @Autowired
    private WebApplicationContext webapp;
    @Autowired
    private NonTenantSettingRepository repository;

    private MockMvc restMvc;
    private Settings s1;
    public static final String userId = "testuser";

    @Before
    public void setupData() {
        restMvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
        s1 = repository.save(Settings.builder().key("key1").value("value1").build());
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void addSettingTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(put(SETTINGS)
                .param("key", "key2")
                .param("val", "val2")
                .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void delSettingTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(delete(SETTINGS)
                .param("key", s1.getKey())
                .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getSettingTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(get(SETTINGS)
                .param("key", s1.getKey())
                .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void updateSettingTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(post(SETTINGS)
                .param("key", s1.getKey())
                .param("val", "val2")
                .with(bearerToken))
                .andDo(print());
        resultActions.andExpect(status().isOk());
    }
}
