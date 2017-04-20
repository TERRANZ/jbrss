package ru.terra.jbrss.test.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
import ru.terra.jbrss.shared.constants.URLConstants;
import ru.terra.jbrss.controller.UsersController;
import ru.terra.jbrss.db.entity.JbrssUser;
import ru.terra.jbrss.db.repos.UsersRepository;
import ru.terra.jbrss.test.OAuthHelper;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@Ignore
public class UsersControllerTest {
    @Autowired
    private OAuthHelper oAuthHelper;
    @Autowired
    private WebApplicationContext webapp;
    @Autowired
    private UsersRepository repository;
    @Autowired
    private UsersController controller;

    public static final String userId = UUID.randomUUID().toString();
    private MockMvc restMvc;
    private JbrssUser user1;

    @Before
    public void setupData() {
        restMvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
        user1 = new JbrssUser();
        user1.setLevel(0);
        user1.setLogin("testlogin");
        user1.setPassword("testpass");
        repository.save(user1);
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void getIdTest() throws Exception {
        RequestPostProcessor bearerToken = oAuthHelper.addBearerToken(userId, "ROLE_USER");
        ResultActions resultActions = restMvc.perform(get(URLConstants.Account.GET_ID.replace("{client}", user1.getLogin())).with(bearerToken)).andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void allIdsTest() throws Exception {

    }

    @Test
    public void createTest() throws Exception {

    }

    @Test
    public void loginTest() throws Exception {

    }
}
