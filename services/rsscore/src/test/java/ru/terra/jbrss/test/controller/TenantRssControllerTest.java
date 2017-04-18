package ru.terra.jbrss.test.controller;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.terra.jbrss.controller.TenantRssController;

@SpringBootTest
@RunWith(SpringRunner.class)
@Ignore
public class TenantRssControllerTest {
    @Autowired
    private TenantRssController rssController;
}
