package ru.terra.jbrss.db.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author terranz
 */
@Entity
@Table(name = "settings", catalog = "jbrss", schema = "")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Settings.findAll", query = "SELECT s FROM Settings s"),
        @NamedQuery(name = "Settings.findByKey", query = "SELECT s FROM Settings s WHERE s.key = :key AND s.userId = :uid"),
        @NamedQuery(name = "Settings.findByValue", query = "SELECT s FROM Settings s WHERE s.value = :value AND s.userId = :uid")})
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (key != null ? key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Settings)) {
            return false;
        }
        Settings other = (Settings) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.terra.jbrss.db.entity.Settings[ key=" + key + " ]";
    }

}
