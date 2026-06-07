package me.vkorostelev.rssfeed.storage;

import me.vkorostelev.rssfeed.model.Folder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoad() throws IOException {
        Path filePath = tempDir.resolve("test.json");
        StorageManager manager = new StorageManager(filePath);

        Folder folder = new Folder("Test Folder");
        List<Folder> folders = List.of(folder);

        manager.save(folders);

        List<Folder> loaded = manager.load();
        assertEquals(1, loaded.size());
        assertEquals("Test Folder", loaded.get(0).getName());
    }

    @Test
    void testLoadNonExistentFile() throws IOException {
        Path filePath = tempDir.resolve("nonexistent.json");
        StorageManager manager = new StorageManager(filePath);

        List<Folder> loaded = manager.load();
        assertTrue(loaded.isEmpty());
    }
}
