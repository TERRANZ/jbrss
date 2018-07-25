package ru.terra.dms.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.terra.dms.constants.DmsConstants;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class DmsObjectField {
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false, length = 35)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    @Column(nullable = true, updatable = true)
    private LocalDateTime modifiedDate;

    @JsonIgnore
    @JoinColumn(name = "object_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private DmsObject dmsObject;

    @Basic(optional = false)
    @Column(nullable = false, length = 256)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private DmsConstants.DmsTypes type;

    private Integer intval;

    private Long longval;

    @Column(length = 2048)
    private String strval;

    @Column(precision = 22)
    private Double floatval;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateval;

    @Column(length = 4096)
    private String listval;

    public DmsObjectField(final DmsObject dmsObject, final String name, final DmsConstants.DmsTypes type) {
        this.dmsObject = dmsObject;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public DmsObject getDmsObject() {
        return dmsObject;
    }

    public void setDmsObject(final DmsObject dmsObject) {
        this.dmsObject = dmsObject;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getIntval() {
        return intval;
    }

    public void setIntval(final Integer intval) {
        this.intval = intval;
    }

    public Long getLongval() {
        return longval;
    }

    public void setLongval(final Long longval) {
        this.longval = longval;
    }

    public String getStrval() {
        return strval;
    }

    public void setStrval(final String strval) {
        this.strval = strval;
    }

    public Double getFloatval() {
        return floatval;
    }

    public void setFloatval(final Double floatval) {
        this.floatval = floatval;
    }

    public Date getDateval() {
        return dateval;
    }

    public void setDateval(final Date dateval) {
        this.dateval = dateval;
    }

    public String getListval() {
        return listval;
    }

    public void setListval(final String listval) {
        this.listval = listval;
    }

    public DmsConstants.DmsTypes getType() {
        return type;
    }

    public void setType(final DmsConstants.DmsTypes type) {
        this.type = type;
    }
}
