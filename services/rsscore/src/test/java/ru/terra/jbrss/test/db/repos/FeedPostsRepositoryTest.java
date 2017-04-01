package ru.terra.jbrss.test.db.repos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.jbrss.db.repos.FeedPostsRepository;

import java.util.Calendar;
import java.util.List;

/**
 * Created by terranz on 01.04.17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedPostsRepositoryTest {
    @Autowired
    private FeedPostsRepository feedPostsRepository;
    Integer firstItemId;
    Integer lastItemId;
    private Feedposts fp1;
    private Feedposts fp2;
    private Feedposts fp3;

    @Before

    public void setupData() {
        Calendar calendar = Calendar.getInstance();
        fp1 = new Feedposts(1, 0, calendar.getTime(), "title1", "link1", "text1");
        calendar.add(Calendar.HOUR, 1);
        fp2 = new Feedposts(2, 0, calendar.getTime(), "title2", "link2", "text2");
        calendar.add(Calendar.HOUR, 1);
        fp3 = new Feedposts(3, 0, calendar.getTime(), "title3", "link3", "text3");
        firstItemId = feedPostsRepository.save(fp1).getId();
        feedPostsRepository.save(fp2);
        lastItemId = feedPostsRepository.save(fp3).getId();
    }

    @Test
    public void getPostsByFeedAndByDateSortedTest() {
        List<Feedposts> res = feedPostsRepository.getLastPostInFeed(0);
        Assert.assertEquals(1, res.size());
        Assert.assertEquals(lastItemId, res.get(0).getId());
    }
}