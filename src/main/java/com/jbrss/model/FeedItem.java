package com.jbrss.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedItem {
    private String title;
    private String link;
    private String description;
}
