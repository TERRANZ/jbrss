package ru.terra.jbrss.web.dto;

import ru.terra.jbrss.core.db.entity.Feedposts;

public class FeedPostDto {
    public Integer id;
    public Integer feedId;
    public String postdate;
    public String posttitle;
    public String postlink;
    public String posttext;
    public boolean isRead;

    public FeedPostDto(Feedposts feedposts) {
        this.id = feedposts.getId();
        this.feedId = feedposts.getFeedId();
        this.postdate = feedposts.getPostdate().toString();
        this.posttitle = feedposts.getPosttitle();
        this.postlink = feedposts.getPostlink();
        this.posttext = feedposts.getPosttext();
        this.isRead = feedposts.isRead();
    }

    public FeedPostDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFeedId() {
        return feedId;
    }

    public void setFeedId(Integer feedId) {
        this.feedId = feedId;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getPostlink() {
        return postlink;
    }

    public void setPostlink(String postlink) {
        this.postlink = postlink;
    }

    public String getPosttext() {
        return posttext;
    }

    public void setPosttext(String posttext) {
        this.posttext = posttext;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
