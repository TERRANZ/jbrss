/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.terra.jbrss.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author terranz
 */
@Entity
@Table(name = "feeds", catalog = "jbrss", schema = "")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Feeds.findAll", query = "SELECT f FROM Feeds f"),
		@NamedQuery(name = "Feeds.findById", query = "SELECT f FROM Feeds f WHERE f.id = :id"),
		@NamedQuery(name = "Feeds.findByUserid", query = "SELECT f FROM Feeds f WHERE f.userid = :userid"),
		@NamedQuery(name = "Feeds.findByUseridAndByFeedId", query = "SELECT f FROM Feeds f WHERE f.userid = :userid AND f.id = :fid"),
		@NamedQuery(name = "Feeds.findByUseridAndByFeedURL", query = "SELECT f FROM Feeds f WHERE f.userid = :userid AND f.feedurl = :url"),
		@NamedQuery(name = "Feeds.findByFeedname", query = "SELECT f FROM Feeds f WHERE f.feedname = :feedname"),
		@NamedQuery(name = "Feeds.findByFeedurl", query = "SELECT f FROM Feeds f WHERE f.feedurl = :feedurl"),
		@NamedQuery(name = "Feeds.findByUpdateTime", query = "SELECT f FROM Feeds f WHERE f.updateTime = :updateTime") })
public class Feeds implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(name = "userid", nullable = false)
	private int userid;
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

	public Feeds()
	{
	}

	public Feeds(Integer id)
	{
		this.id = id;
	}

	public Feeds(Integer id, int userid, String feedname, String feedurl, Date updateTime)
	{
		this.id = id;
		this.userid = userid;
		this.feedname = feedname;
		this.feedurl = feedurl;
		this.updateTime = updateTime;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public int getUserid()
	{
		return userid;
	}

	public void setUserid(int userid)
	{
		this.userid = userid;
	}

	public String getFeedname()
	{
		return feedname;
	}

	public void setFeedname(String feedname)
	{
		this.feedname = feedname;
	}

	public String getFeedurl()
	{
		return feedurl;
	}

	public void setFeedurl(String feedurl)
	{
		this.feedurl = feedurl;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Feeds))
		{
			return false;
		}
		Feeds other = (Feeds) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "ru.terra.jbrss.db.entity.Feeds[ id=" + id + " ]";
	}
}
