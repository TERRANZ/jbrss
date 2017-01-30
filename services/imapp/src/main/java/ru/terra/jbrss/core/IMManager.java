package ru.terra.jbrss.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terra.jbrss.core.db.entity.Feedposts;
import ru.terra.jbrss.core.db.entity.Feeds;
import ru.terra.jbrss.core.db.repos.ContactsRepository;
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
    @Autowired
    private ContactsRepository contactsRepository;

    public void start() {
        jabberIM.start();
        telegramIM.start();
    }

    public void notifyFeedUpdated(Integer usedId, Feeds feed, List<Feedposts> newPosts) {
        threadPool.submit(() -> {
            logger.info("Feed " + feed.getFeedname() + " of user " + usedId + " have " + newPosts.size() + " new posts");
            contactsRepository.findByUserId(usedId).forEach(c -> {
                ServerInterface im;
                if (c.getType().equals(IMType.TELEGRAM.name()))
                    im = telegramIM;
                else
                    im = jabberIM;

                StringBuilder sb = new StringBuilder();
                sb.append("New message on feed ");
                sb.append(feed.getFeedname());
                sb.append("\n");
                newPosts.forEach(p -> sb.append(p.getPosttext()).append("\n").append("<===================================================>").append("\n"));
                im.sendMessage(c.getContact(), sb.toString());
            });
        });
    }
}
