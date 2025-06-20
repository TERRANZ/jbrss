package me.vkorostelev.jbrss.rss;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vkorostelev.jbrss.rss.entity.Feed;

@NoArgsConstructor
@Slf4j
public class RssMain {
    private final RssLoader loader = new RssLoader();
    private final RssStorage storage = new RssStorage();

    public Feed loadFeed(final String url) {
        return loader.load(url);
    }
}
