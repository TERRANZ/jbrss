package com.jbrss.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSSFeed {
    private String title;
    private String url;
    private List<FeedItem> items = new ArrayList<>();
}
