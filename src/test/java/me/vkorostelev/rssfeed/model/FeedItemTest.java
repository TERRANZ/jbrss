package me.vkorostelev.rssfeed.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedItemTest {

    @Test
    void testConstructorAndGetters() {
        String feedId = "feed-1";
        String title = "Test Item";
        String link = "http://example.com/item";
        String pubDate = "2023-10-25";
        String content = "Some content";

        FeedItem item = new FeedItem(feedId, title, link, pubDate, content);

        assertEquals(feedId, item.getFeedId());
        assertEquals(title, item.getTitle());
        assertEquals(link, item.getLink());
        assertEquals(pubDate, item.getPubDate());
        assertEquals(content, item.getContent());
        assertNotNull(item.getId());
    }
}
