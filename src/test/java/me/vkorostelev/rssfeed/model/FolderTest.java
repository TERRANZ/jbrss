package me.vkorostelev.rssfeed.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FolderTest {

    @Test
    void testConstructorAndGetters() {
        Folder folder = new Folder("My Folder");
        assertEquals("My Folder", folder.getName());
        assertNotNull(folder.getId());
        assertTrue(folder.getFeeds().isEmpty());
    }

    @Test
    void testSetName() {
        Folder folder = new Folder("Old Name");
        folder.setName("New Name");
        assertEquals("New Name", folder.getName());
    }

    @Test
    void testAddFeed() {
        Folder folder = new Folder("Folder");
        Feed feed = new Feed("folder-id", "Feed", "http://url.com");
        folder.addFeed(feed);

        assertEquals(1, folder.getFeeds().size());
        assertTrue(folder.getFeeds().contains(feed));
    }

    @Test
    void testRemoveFeed() {
        Folder folder = new Folder("Folder");
        Feed feed = new Feed("folder-id", "Feed", "http://url.com");
        folder.addFeed(feed);
        folder.removeFeed(feed);

        assertTrue(folder.getFeeds().isEmpty());
    }
}
