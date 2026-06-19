package com.jbrss.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Folder {
    private String name;
    private List<Folder> subFolders = new ArrayList<>();
    private List<RSSFeed> feeds = new ArrayList<>();
}
