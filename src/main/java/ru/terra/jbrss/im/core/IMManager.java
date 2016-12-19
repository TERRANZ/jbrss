package ru.terra.jbrss.im.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.core.db.entity.Feedposts;
import ru.terra.jbrss.core.db.entity.Feeds;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class IMManager {
    private ExecutorService threadPool = Executors.newFixedThreadPool(20);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    public IMManager() {
        logger.info("IMManager starting...");
    }

    public void onFeedUpdated(Integer usedId, Feeds feed, List<Feedposts> newPosts) {
        threadPool.submit(() -> {
            logger.info("Feed " + feed.getFeedname() + " of user " + usedId + " have " + newPosts.size() + " new posts");
        });
    }
}
