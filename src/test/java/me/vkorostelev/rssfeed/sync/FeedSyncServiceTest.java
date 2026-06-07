package me.vkorostelev.rssfeed.sync;

import me.vkorostelev.rssfeed.model.Feed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedSyncServiceTest {

    @Test
    void testSyncFeedWithInvalidUrlThrowsException() throws Exception {
        FeedSyncService service = new FeedSyncService();
        Feed feed = new Feed("folder-id", "Test", "http://invalid-url-that-does-not-exist.invalid");

        assertThrows(Exception.class, () -> service.syncFeed(feed));
    }
}
