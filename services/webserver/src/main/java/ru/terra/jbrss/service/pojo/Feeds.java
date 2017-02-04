package ru.terra.jbrss.service.pojo;

import java.io.Serializable;
import java.util.Date;

public class Feeds implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private int userid;

    private String feedname;

    private String feedurl;

    private Date updateTime;

    public Feeds() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}