package me.vkorostelev.jbrss.rss.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedMedia {
    private String title, content_url, thumbnail_url, description;
}
