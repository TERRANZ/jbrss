package ru.terra.jbrss.service.pojo;

import java.io.Serializable;
import java.util.Date;

public class Feedposts implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private int feedId;
    private Date postdate = new Date();
    private String posttitle;
    private String postlink;
    private String posttext;
    private boolean isRead;
    private Date updated = new Date();

    public Feedposts() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public Date getPostdate() {
        return postdate;
    }

    public void setPostdate(Date postdate) {
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

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}