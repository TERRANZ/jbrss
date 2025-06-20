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
public class Feed {
    private String url, title, link, id;
    private List<FeedEntry> entries;
    private Date updated;
}
