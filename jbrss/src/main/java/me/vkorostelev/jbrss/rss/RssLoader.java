package me.vkorostelev.jbrss.rss;

import com.rometools.rome.io.SyndFeedInput;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.vkorostelev.jbrss.RssConfiguration;
import me.vkorostelev.jbrss.rss.entity.Feed;
import me.vkorostelev.jbrss.rss.entity.FeedEntry;

import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class RssLoader {
    @SneakyThrows
    public Feed load(final String url) {
        val proxyUrl = new RssConfiguration().getConfig().getString("proxy.url");
        val proxyPort = new RssConfiguration().getConfig().getInt("proxy.port");
        val connection = new URL(url).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort)));
        val input = new SyndFeedInput();
        val feed = input.build(new InputStreamReader(connection.getInputStream()));

        log.info("Loaded: {}", feed.getTitle());

        val entries = new ArrayList<FeedEntry>();

        for (val e : feed.getEntries()) {
            val feedEntry = FeedEntry.builder()
                    .title(e.getTitle())
                    .author(e.getAuthor())
                    .id(e.getUri())
                    .link(e.getLink())
                    .published(e.getPublishedDate())
                    .updated(e.getUpdatedDate())
                    .build();

            entries.add(feedEntry);
        }

        val result = Feed.builder()
                .title(feed.getTitle())
                .id(feed.getUri())
                .link(feed.getLink())
                .entries(entries)
                .url(url)
                .updated(new Date())
                .build();

        return result;
    }
}
