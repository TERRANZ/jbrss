package ru.terra.jbrss.web.dto;

import ru.terra.jbrss.core.db.entity.Feeds;

public class FeedDto {
    public Integer id = 0;
    public String feedname = "";
    public String feedurl = "";
    public Long updateTime = 0L;

    public FeedDto(Feeds feeds) {
        this.id = feeds.getId();
        this.feedname = feeds.getFeedname();
        this.feedurl = feeds.getFeedurl();
        this.updateTime = feeds.getUpdateTime().getTime();
    }

    public FeedDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeedname() {
        return feedname;
    }

    public void setFeedname(String feedname) {
        this.feedname = feedname;
    }

    public String getFeedurl() {
        return feedurl;
    }

    public void setFeedurl(String feedurl) {
        this.feedurl = feedurl;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
