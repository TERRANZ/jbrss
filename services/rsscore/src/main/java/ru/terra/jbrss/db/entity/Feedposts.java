package ru.terra.jbrss.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "feedposts", indexes = {
        @Index(columnList = "id", name = "fp_id_idx"),
        @Index(columnList = "postdate", name = "fp_postdate_idx")
})
@NamedQuery(name = "Feedposts.getPostsByFeedAndByDateSortedUnread", query = "SELECT f FROM Feedposts f WHERE f.feedId = ?0 AND f.isRead = ?1 ORDER BY f.postdate DESC")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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
    @Builder.Default
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
    @Builder.Default
    private Date updated = new Date();
}