package me.vkorostelev.rssfeed.sync;

import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndEntry;
import me.vkorostelev.rssfeed.model.Feed;
import me.vkorostelev.rssfeed.model.FeedItem;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FeedSyncService {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void syncFeed(Feed feed) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(feed.getUrl()))
                .header("User-Agent", "RSSFeedApp/1.0")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) return;

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed syndFeed = input.build(new StringReader(response.body()));

        feed.setTitle(syndFeed.getTitle());
        feed.clearItems();
        List<FeedItem> items = new ArrayList<>();
        for (SyndEntry entry : syndFeed.getEntries()) {
            items.add(new FeedItem(
                    feed.getId(),
                    entry.getTitle(),
                    entry.getLink(),
                    entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : "",
                    entry.getDescription() != null ? entry.getDescription().getValue() : ""
            ));
        }
        feed.getItems().addAll(items);
    }
}
