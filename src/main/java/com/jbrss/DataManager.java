package com.jbrss;

import com.jbrss.model.*;
import java.util.*;

public class DataManager {
    private Folder rootFolder = new Folder("Root");

    public Folder getRoot() {
        return rootFolder;
    }

    public void addFolder(Folder parent, String name) {
        Folder newFolder = new Folder(name);
        parent.getSubFolders().add(newFolder);
    }

    public void removeFolder(Folder parent, Folder folderToRemove) {
        parent.getSubFolders().remove(folderToRemove);
    }

    public void addFeed(Folder folder, RSSFeed feed) {
        folder.getFeeds().add(feed);
    }

    public void removeFeed(Folder folder, RSSFeed feed) {
        folder.getFeeds().remove(feed);
    }
}
