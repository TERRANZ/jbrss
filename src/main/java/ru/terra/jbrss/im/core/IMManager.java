package ru.terra.jbrss.im.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.core.db.entity.Feedposts;
import ru.terra.jbrss.core.db.entity.Feeds;
import ru.terra.jbrss.im.jabber.JabberIM;
import ru.terra.jbrss.im.telegram.TelegramIM;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class IMManager {
    private ExecutorService threadPool = Executors.newFixedThreadPool(20);
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JabberIM jabberIM;
    @Autowired
    private TelegramIM telegramIM;

    public void start() {
        jabberIM.start();
        telegramIM.start();
    }

    public void onFeedUpdated(Integer usedId, Feeds feed, List<Feedposts> newPosts) {
        threadPool.submit(() -> {
            logger.info("Feed " + feed.getFeedname() + " of user " + usedId + " have " + newPosts.size() + " new posts");
        });
    }
}
