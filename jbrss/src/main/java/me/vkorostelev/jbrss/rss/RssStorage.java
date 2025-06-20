package me.vkorostelev.jbrss.rss;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.vkorostelev.jbrss.rss.entity.Feed;
import me.vkorostelev.jbrss.rss.entity.FeedEntry;
import me.vkorostelev.jbrss.rss.entity.FeedFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ToString
@Slf4j
public class RssStorage {
    private static final String STORAGE_FILE_NAME = "folders.json";

    @Getter
    @Setter
    private FeedFolder rootFolder;
    private Map<UUID, FeedFolder> folderMap;

    public void load() throws IOException {
        rootFolder = new ObjectMapper().readValue(new File(STORAGE_FILE_NAME), FeedFolder.class);
        fillFolderMap(rootFolder);
    }

    public void save() {
        try {
            new ObjectMapper().writeValue(new File(STORAGE_FILE_NAME), rootFolder);
        } catch (IOException e) {
            log.error("Can't save", e);
        }
    }

    public void merge(final Feed feed, final FeedFolder feedFolder) {
        if (feedFolder.getFeed() != null) {
            if (feedFolder.getFeed().getId().equalsIgnoreCase(feed.getId())) {
                val feList = feedFolder.getFeed().getEntries().stream().map(FeedEntry::getId).toList();
                feed.getEntries().stream().filter(nfe -> !feList.contains(nfe.getId())).forEach(nfe -> feedFolder.getFeed().getEntries().add(nfe));
            }
        } else {
            feedFolder.setFeed(feed);
        }
    }

    public void remove(final FeedFolder toRemove) {
        toRemove.getChildFolders().clear();
        toRemove.setFeed(null);
        val parent = folderMap.get(toRemove.getParentFolderId());
        var childFolders = parent.getChildFolders();
        childFolders.removeIf(folder -> folder.getId().equals(toRemove.getId()));
        folderMap.remove(toRemove.getId());
    }


    private void fillFolderMap(final FeedFolder root) {
        if (folderMap == null) {
            folderMap = new HashMap<>();
        }
        folderMap.put(root.getId(), root);
        root.getChildFolders().forEach(this::fillFolderMap);
    }

}
