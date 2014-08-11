/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terra.jbrss.jabber.db.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author terranz
 */
@Entity
@Table(name = "CONTACTS", catalog = "", schema = "JBRSS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c"),
    @NamedQuery(name = "Contact.findById", query = "SELECT c FROM Contact c WHERE c.id = :id"),
    @NamedQuery(name = "Contact.findByContact", query = "SELECT c FROM Contact c WHERE c.contact = :contact"),
    @NamedQuery(name = "Contact.findByUserId", query = "SELECT c FROM Contact c WHERE c.userId = :userId"),
    @NamedQuery(name = "Contact.findByCaptchaKey", query = "SELECT c FROM Contact c WHERE c.captchaKey = :captchaKey"),
    @NamedQuery(name = "Contact.findByCaptchaVal", query = "SELECT c FROM Contact c WHERE c.captchaVal = :captchaVal"),
    @NamedQuery(name = "Contact.findByLastlogin", query = "SELECT c FROM Contact c WHERE c.lastlogin = :lastlogin"),
    @NamedQuery(name = "Contact.findByCheckinterval", query = "SELECT c FROM Contact c WHERE c.checkinterval = :checkinterval"),
    @NamedQuery(name = "Contact.findByStatus", query = "SELECT c FROM Contact c WHERE c.status = :status")})
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 256)
    private String contact;
    @Column(name = "USER_ID")
    private Integer userId;
    @Column(name = "CAPTCHA_KEY", length = 128)
    private String captchaKey;
    @Column(name = "CAPTCHA_VAL", length = 128)
    private String captchaVal;
    private Long lastlogin;
    private Long checkinterval;
    private Integer status;

    public Contact() {
    }

    public Contact(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public String getCaptchaVal() {
        return captchaVal;
    }

    public void setCaptchaVal(String captchaVal) {
        this.captchaVal = captchaVal;
    }

    public Long getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Long lastlogin) {
        this.lastlogin = lastlogin;
    }

    public Long getCheckinterval() {
        return checkinterval;
    }

    public void setCheckinterval(Long checkinterval) {
        this.checkinterval = checkinterval;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.terra.jbrss.jabber.db.entity.Contact[ id=" + id + " ]";
    }
    
}
