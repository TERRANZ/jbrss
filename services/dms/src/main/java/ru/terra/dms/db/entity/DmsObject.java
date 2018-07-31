package ru.terra.dms.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class DmsObject {
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

    private String userId;

    @JsonIgnore
    @OneToMany(
            mappedBy = "dmsObject",
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @OrderBy("createdDate ASC")
    private List<DmsObjectField> fieldList;

    public DmsObject(final String userId, final List<DmsObjectField> fieldList) {
        this.userId = userId;
        this.fieldList = fieldList;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public List<DmsObjectField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(final List<DmsObjectField> fieldList) {
        this.fieldList = fieldList;
    }
}
