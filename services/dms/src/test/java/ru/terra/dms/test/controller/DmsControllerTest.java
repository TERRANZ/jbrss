package ru.terra.dms.test.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.terra.dms.test.OAuthHelper;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DmsControllerTest {
    @Autowired
    private OAuthHelper oAuthHelper;
    @Autowired
    private WebApplicationContext webapp;
    public static final String userId = "testuser";

    private MockMvc restMvc;

    @Before
    public void setupData() {
        restMvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
    }

    @After
    public void cleanUp() {
    }


    @Test
    public void getAllTest() {

    }

    @Test
    public void postTest() throws Exception {
    }
}
