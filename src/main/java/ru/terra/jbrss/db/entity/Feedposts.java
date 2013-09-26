/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.terra.jbrss.db.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author terranz
 */
@Entity
@Table(name = "feedposts", catalog = "jbrss", schema = "")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Feedposts.findAll", query = "SELECT f FROM Feedposts f"),
        @NamedQuery(name = "Feedposts.findById", query = "SELECT f FROM Feedposts f WHERE f.id = :id"),
        @NamedQuery(name = "Feedposts.findByFeedId", query = "SELECT f FROM Feedposts f WHERE f.feedId = :feedId"),
        @NamedQuery(name = "Feedposts.findByPostdate", query = "SELECT f FROM Feedposts f WHERE f.postdate = :postdate"),
        @NamedQuery(name = "Feedposts.findByPosttitle", query = "SELECT f FROM Feedposts f WHERE f.posttitle = :posttitle"),
        @NamedQuery(name = "Feedposts.getPostsByFeedAndByDateSorted", query = "SELECT f FROM Feedposts f WHERE f.feedId = :feedId ORDER BY f.postdate DESC"),
        @NamedQuery(name = "Feedposts.getPostsByFeedAndByDateSortedUnread", query = "SELECT f FROM Feedposts f WHERE f.feedId = :feedId AND f.isRead = :isread ORDER BY f.postdate DESC"),
        @NamedQuery(name = "Feedposts.getPostsByFeedAndByDate", query = "SELECT f FROM Feedposts f WHERE f.feedId = :feedId AND f.postdate >= :pdate ORDER BY f.postdate DESC"),
        @NamedQuery(name = "Feedposts.findByPostlink", query = "SELECT f FROM Feedposts f WHERE f.postlink = :postlink")})
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
    private Date postdate;
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
    private Date updated;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Feedposts)) {
            return false;
        }
        Feedposts other = (Feedposts) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Feedposts{" +
                "id=" + id +
                ", feedId=" + feedId +
                ", postdate=" + postdate +
                ", posttitle='" + posttitle + '\'' +
                ", postlink='" + postlink + '\'' +
                ", posttext='" + posttext + '\'' +
                ", isRead=" + isRead +
                ", updated=" + updated +
                '}';
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
