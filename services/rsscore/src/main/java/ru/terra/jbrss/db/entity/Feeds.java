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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "feeds")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Feeds implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "feedname", nullable = false, length = 512)
    private String feedname;
    @Basic(optional = false)
    @Column(name = "feedurl", nullable = false, length = 512)
    private String feedurl;
    @Basic(optional = false)
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date updateTime;
}