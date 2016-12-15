package ru.terra.jbrss.core.rss;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.core.db.entity.Feeds;

import java.util.List;

@Service
@Scope("singleton")
public class RssCore {

    public void runUpdate() {

    }

    public List<Feeds> getFeeds(Integer uid) {
        return null;
    }

    public void updateFeed(Feeds f) {

    }
}
