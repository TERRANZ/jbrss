package me.vkorostelev.jbrss.rss.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedEntry {
    private String id, title, author, link;
    private Date published, updated;
    private List<FeedMedia> media;
}
