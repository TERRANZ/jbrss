package ru.terra.jbrss.core.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "feedposts")
@NamedQuery(name = "Feedposts.getPostsByFeedAndByDateSortedUnread", query = "SELECT f FROM Feedposts f WHERE f.feedId = ?0 AND f.isRead = ?1 ORDER BY f.postdate DESC")
public class Feedposts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "feed_id", nullable = false)
    private int feedId;
    @Basic(optional = false)
    @Column(name = "postdate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postdate = new Date();
    @Basic(optional = false)
    @Column(name = "posttitle", nullable = false, length = 512)
    private String posttitle;
    @Basic(optional = false)
    @Column(name = "postlink", nullable = false, length = 512)
    private String postlink;
    @Basic(optional = false)
    @Lob
    @Column(name = "posttext", nullable = false, length = 2147483647)
    private String posttext;

    @Basic(optional = false)
    @Column(name = "isread", nullable = false)
    private boolean isRead;

    @Basic(optional = false)
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated = new Date();

    public Feedposts() {
    }

    public Feedposts(Integer id) {
        this.id = id;
    }

    public Feedposts(Integer id, int feedId, Date postdate, String posttitle,
                     String postlink, String posttext) {
        this.id = id;
        this.feedId = feedId;
        this.postdate = postdate;
        this.posttitle = posttitle;
        this.postlink = postlink;
        this.posttext = posttext;
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