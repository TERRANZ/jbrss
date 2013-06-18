/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.terra.jbrss.db.controllers;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ru.terra.jbrss.db.controllers.exceptions.NonexistentEntityException;
import ru.terra.jbrss.db.entity.Feeds;

/**
 * 
 * @author terranz
 */
public class FeedsJpaController implements Serializable {

	public FeedsJpaController(EntityManagerFactory emf) {
		this.emf = emf;
	}

	private EntityManagerFactory emf = null;

	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public void create(Feeds feeds) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(feeds);
			em.getTransaction().commit();
		} finally {

		}
	}

	public void edit(Feeds feeds) throws NonexistentEntityException, Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			feeds = em.merge(feeds);
			em.getTransaction().commit();
		} catch (Exception ex) {
			String msg = ex.getLocalizedMessage();
			if (msg == null || msg.length() == 0) {
				Integer id = feeds.getId();
				if (findFeeds(id) == null) {
					throw new NonexistentEntityException("The feeds with id " + id + " no longer exists.");
				}
			}
			throw ex;
		} finally {

		}
	}

	public void destroy(Integer id) throws NonexistentEntityException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			Feeds feeds;
			try {
				feeds = em.getReference(Feeds.class, id);
				feeds.getId();
			} catch (EntityNotFoundException enfe) {
				throw new NonexistentEntityException("The feeds with id " + id + " no longer exists.", enfe);
			}
			em.remove(feeds);
			em.getTransaction().commit();
		} finally {

		}
	}

	public List<Feeds> findFeedsEntities() {
		return findFeedsEntities(true, -1, -1);
	}

	public List<Feeds> findFeedsEntities(int maxResults, int firstResult) {
		return findFeedsEntities(false, maxResults, firstResult);
	}

	private List<Feeds> findFeedsEntities(boolean all, int maxResults, int firstResult) {
		EntityManager em = getEntityManager();
		try {
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			cq.select(cq.from(Feeds.class));
			Query q = em.createQuery(cq);
			if (!all) {
				q.setMaxResults(maxResults);
				q.setFirstResult(firstResult);
			}
			return q.getResultList();
		} finally {

		}
	}

	public Feeds findFeeds(Integer id) {
		EntityManager em = getEntityManager();
		try {
			return em.find(Feeds.class, id);
		} finally {

		}
	}

	public int getFeedsCount() {
		EntityManager em = getEntityManager();
		try {
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<Feeds> rt = cq.from(Feeds.class);
			cq.select(em.getCriteriaBuilder().count(rt));
			Query q = em.createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {

		}
	}

	public Boolean isFeedExists(String feedUrl) {
		EntityManager em = getEntityManager();
		Query q = em.createNamedQuery("Feeds.findByFeedurl").setParameter("feedurl", feedUrl);
		try {
			if (q.getSingleResult() != null)
				return true;
			return false;
		} catch (NoResultException e) {
			return false;
		} finally {

		}
	}

	public List<Feeds> findFeedsByUser(Integer uid) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createNamedQuery("Feeds.findByUserid").setParameter("userid", uid);
			return q.getResultList();
		} catch (NoResultException e) {
			return null;
		} finally {

		}
	}

	public Feeds findFeedByUserAndById(Integer uid, Integer fid) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createNamedQuery("Feeds.findByUseridAndByFeedId").setParameter("userid", uid).setParameter("fid", fid).setMaxResults(1);
			return (Feeds) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {

		}
	}

	public Feeds findFeedByUserAndByURL(Integer uid, String url) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createNamedQuery("Feeds.findByUseridAndByFeedURL").setParameter("userid", uid).setParameter("url", url).setMaxResults(1);
			return (Feeds) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {

		}
	}
}
