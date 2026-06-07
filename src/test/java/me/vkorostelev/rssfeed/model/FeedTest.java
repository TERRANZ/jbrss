package me.vkorostelev.rssfeed.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedTest {

    @Test
    void testConstructorAndGetters() {
        String folderId = "folder-1";
        String title = "Test Feed";
        String url = "http://example.com/feed.xml";
        Feed feed = new Feed(folderId, title, url);

        assertEquals(folderId, feed.getFolderId());
        assertEquals(title, feed.getTitle());
        assertEquals(url, feed.getUrl());
        assertNotNull(feed.getId());
        assertTrue(feed.getItems().isEmpty());
    }

    @Test
    void testSetters() {
        Feed feed = new Feed("folder-1", "Old Title", "http://old.com");
        feed.setTitle("New Title");
        feed.setUrl("http://new.com");

        assertEquals("New Title", feed.getTitle());
        assertEquals("http://new.com", feed.getUrl());
    }

    @Test
    void testAddItem() {
        Feed feed = new Feed("folder-1", "Title", "http://url.com");
        FeedItem item = new FeedItem("feed-1", "Item Title", "http://link.com", "2023-01-01", "Content");
        feed.addItem(item);

        assertEquals(1, feed.getItems().size());
        assertEquals(item, feed.getItems().get(0));
    }

    @Test
    void testClearItems() {
        Feed feed = new Feed("folder-1", "Title", "http://url.com");
        feed.addItem(new FeedItem("feed-1", "Title", "Link", "Date", "Content"));
        feed.clearItems();

        assertTrue(feed.getItems().isEmpty());
    }
}
