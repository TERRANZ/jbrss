package me.vkorostelev.jbrss.rss.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedFolder {
    private UUID id;
    private String name;
    private Feed feed;
    private UUID parentFolderId;
    private List<FeedFolder> childFolders = new ArrayList<>();

    @Override
    public String toString() {
        return name;
    }
}
