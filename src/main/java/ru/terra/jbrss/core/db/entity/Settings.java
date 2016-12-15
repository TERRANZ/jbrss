package ru.terra.jbrss.core.db.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "settings")
public class Settings implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "skey", nullable = false, length = 128)
    private String key;
    @Basic(optional = false)
    @Column(name = "svalue", nullable = false, length = 512)
    private String value;
    @Basic(optional = false)
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Settings() {
    }

    public Settings(String key) {
        this.key = key;
    }

    public Settings(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}