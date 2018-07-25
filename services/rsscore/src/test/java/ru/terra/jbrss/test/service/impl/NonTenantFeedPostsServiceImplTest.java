package ru.terra.jbrss.test.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.service.FeedPostsService;

import java.util.Calendar;
import java.util.List;

/**
 * Created by terranz on 18.04.17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NonTenantFeedPostsServiceImplTest {
    @Autowired
    @Qualifier("nonTenantFeedPostsService")
    private FeedPostsService feedPostsService;
    private Feedposts fp1;
    private Feedposts fp12;
    private Feedposts fp21;

    @Before
    public void setupData() {
        Calendar calendar = Calendar.getInstance();
        fp1 = feedPostsService.save(Feedposts.builder().feedId(1).postdate(calendar.getTime()).postlink("l1").posttext("t1").posttitle("t1").build());
        calendar.add(Calendar.HOUR, 1);
        fp12 = feedPostsService.save(Feedposts.builder().feedId(1).postdate(calendar.getTime()).postlink("l1").posttext("t1").posttitle("t1").build());
        calendar.add(Calendar.HOUR, 1);
        fp21 = feedPostsService.save(Feedposts.builder().feedId(2).postdate(calendar.getTime()).postlink("l1").posttext("t1").posttitle("t1").build());
    }

    @After
    public void cleanUp() {
        feedPostsService.deleteAll();
    }

    @Test
    public void findFeedpostsByFeedLimited() throws Exception {
        List<Feedposts> list = feedPostsService.findFeedpostsByFeedLimited(1, 0, 1);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void countByFeedId() throws Exception {
        Assert.assertTrue(feedPostsService.countByFeedId(1) == 2);
        Assert.assertTrue(feedPostsService.countByFeedId(2) == 1);
    }

    @Test
    public void getLastPostInFeed() throws Exception {
        Assert.assertEquals(feedPostsService.getLastPostInFeed(1).get(0).getId(), fp12.getId());
    }

    @Test
    public void findByFeedId() throws Exception {
        Assert.assertTrue(feedPostsService.findByFeedId(1).size() == 2);
    }


    @Test
    public void findOne() throws Exception {
        Assert.assertEquals(feedPostsService.findOne(fp1.getId()).getId(), fp1.getId());
    }

}