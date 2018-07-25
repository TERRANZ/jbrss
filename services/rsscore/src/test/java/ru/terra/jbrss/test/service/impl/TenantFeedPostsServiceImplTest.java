package ru.terra.jbrss.test.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.service.FeedPostsService;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

/**
 * Created by terranz on 18.04.17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TenantFeedPostsServiceImplTest {
    @Autowired
    @Qualifier("tenantFeedPostsService")
    private FeedPostsService feedPostsService;
    private Feedposts fp1;
    private Feedposts fp12;
    private Feedposts fp21;

    @Before
    public void setupData() {
        Calendar calendar = Calendar.getInstance();
        fp1 = feedPostsService.save(Feedposts.builder().feedId(1).postdate(calendar.getTime()).postlink("l1").posttext("t1").posttitle("t1").build());
        calendar.add(Calendar.HOUR, 1);
        fp12 = feedPostsService.save(Feedposts.builder().feedId(1).postdate(calendar.getTime()).postlink("l2").posttext("t2").posttitle("t2").build());
        calendar.add(Calendar.HOUR, 1);
        fp21 = feedPostsService.save(Feedposts.builder().feedId(2).postdate(calendar.getTime()).postlink("l3").posttext("t3").posttitle("t3").build());
    }

    @After
    public void cleanUp() {
        feedPostsService.deleteAll();
    }

    @Test
    public void findFeedpostsByFeedLimited() throws Exception {
        List<Feedposts> list = feedPostsService.findFeedpostsByFeedLimited(1, 0, 1);
        assertEquals(1, list.size());
    }

    @Test
    public void countByFeedId() throws Exception {
        assertEquals(2, (int) feedPostsService.countByFeedId(1));
        assertEquals(1, (int) feedPostsService.countByFeedId(2));
    }

    @Test
    public void getLastPostInFeed() throws Exception {
        assertEquals(feedPostsService.getLastPostInFeed(1).get(0).getId(), fp12.getId());
    }

    @Test
    public void findByFeedId() throws Exception {
        Assert.assertTrue(feedPostsService.findByFeedId(1).size() == 2);
    }


    @Test
    public void findOne() throws Exception {
        assertEquals(feedPostsService.findOne(fp1.getId()).getId(), fp1.getId());
    }

    @Test
    public void testGetPath() throws Exception {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info(TenantFeedPostsServiceImplTest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

        logger.info(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        URL path = ClassLoader.getSystemClassLoader().getResource(".");
        if (path != null)
            logger.info(path.getPath());
    }
}
