package ru.terra.jbrss.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "feeds")
@NamedQueries({@NamedQuery(name = "Feeds.findAll", query = "SELECT f FROM Feeds f"),
        @NamedQuery(name = "Feeds.findByUseridAndByFeedId", query = "SELECT f FROM Feeds f WHERE f.userid = ?1 AND f.id = ?2"),
        @NamedQuery(name = "Feeds.findByUseridAndByFeedURL", query = "SELECT f FROM Feeds f WHERE f.userid = ?1 AND f.feedurl = ?2")})
public class Feeds implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "userid", nullable = false)
    private String userid;
    @Basic(optional = false)
    @Column(name = "feedname", nullable = false, length = 512)
    private String feedname;
    @Basic(optional = false)
    @Column(name = "feedurl", nullable = false, length = 512)
    private String feedurl;
    @Basic(optional = false)
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public Feeds() {
    }

    public Feeds(Integer id) {
        this.id = id;
    }

    public Feeds(Integer id, String userId, String feedname, String feedurl, Date updateTime) {
        this.id = id;
        this.userid = userId;
        this.feedname = feedname;
        this.feedurl = feedurl;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
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