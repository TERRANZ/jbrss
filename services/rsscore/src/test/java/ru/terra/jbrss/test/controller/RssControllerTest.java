package ru.terra.jbrss.test.controller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.terra.jbrss.controller.RssController;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.jbrss.db.repos.FeedPostsRepository;
import ru.terra.jbrss.db.repos.FeedsRepository;
import ru.terra.jbrss.shared.dto.BooleanDto;
import ru.terra.jbrss.shared.dto.FeedListDto;
import ru.terra.jbrss.shared.dto.FeedPostsPageableDto;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by terranz on 01.04.17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RssControllerTest {
    @Autowired
    private RssController rssController;
    @Autowired
    private FeedsRepository feedsRepository;
    @Autowired
    private FeedPostsRepository feedPostsRepository;
    private Feeds f1;
    private Feeds f2;
    private Feedposts fp1;
    private Feedposts fp12;
    private Feedposts fp21;

    @Before
    public void setupData() {
        f1 = feedsRepository.save(new Feeds(0, 0, "feed1", "url1", new Date()));
        f2 = feedsRepository.save(new Feeds(0, 0, "feed2", "url2", new Date()));
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
    public void allFeedsTest() {
        FeedListDto res = rssController.allFeeds(0);
        Assert.assertEquals(2, res.data.size());
    }

    @Test
    public void feedPosts() {
        FeedPostsPageableDto res = rssController.feedPosts(0, f1.getId(), 0, 1).getBody();
        Assert.assertEquals(1, res.getPosts().size());
        Assert.assertEquals(fp12.getId(), res.getPosts().get(0).getId());
    }

    @Test
    public void addFeedTest() {
        BooleanDto booleanDto = rssController.addFeed(0, "http://url.ru/awd.rss");
        Assert.assertTrue(booleanDto.getStatus());
        FeedListDto feedListDto = rssController.allFeeds(0);
        Assert.assertEquals(3, feedListDto.data.size());
    }

    @Test
    public void delFeedTest() {
        BooleanDto booleanDto = rssController.delFeed(0, f2.getId()).getBody();
        Assert.assertTrue(booleanDto.getStatus());
        FeedListDto feedListDto = rssController.allFeeds(0);
        Assert.assertEquals(1, feedListDto.data.size());
    }
}
