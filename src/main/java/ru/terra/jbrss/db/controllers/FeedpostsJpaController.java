/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.terra.jbrss.db.controllers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.terra.jbrss.db.controllers.exceptions.NonexistentEntityException;
import ru.terra.jbrss.db.entity.Feedposts;

/**
 * 
 * @author terranz
 */
public class FeedpostsJpaController implements Serializable {
	Logger log = LoggerFactory.getLogger(FeedpostsJpaController.class);

	public FeedpostsJpaController(EntityManagerFactory emf) {
		this.emf = emf;
		em = getEntityManager();
	}

	private EntityManagerFactory emf = null;
	private EntityManager em = null;

	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public void create(Feedposts feedposts) {
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			feedposts.setUpdated(new Date());
			em.persist(feedposts);
			em.getTransaction().commit();
		} finally {

		}
	}

	public void edit(Feedposts feedposts) throws NonexistentEntityException, Exception {
		try {
			em.getTransaction().begin();
			feedposts.setUpdated(new Date());
			feedposts = em.merge(feedposts);
			em.getTransaction().commit();
		} catch (Exception ex) {
			String msg = ex.getLocalizedMessage();
			if (msg == null || msg.length() == 0) {
				Integer id = feedposts.getId();
				if (findFeedposts(id) == null) {
					throw new NonexistentEntityException("The feedposts with id " + id + " no longer exists.");
				}
			}
			throw ex;
		} finally {

		}
	}

	public void destroy(Integer id) throws NonexistentEntityException {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			em.getTransaction().begin();
			Feedposts feedposts;
			try {
				feedposts = em.getReference(Feedposts.class, id);
				feedposts.getId();
			} catch (EntityNotFoundException enfe) {
				throw new NonexistentEntityException("The feedposts with id " + id + " no longer exists.", enfe);
			}
			em.remove(feedposts);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<Feedposts> findFeedpostsEntities() {
		return findFeedpostsEntities(true, -1, -1);
	}

	public List<Feedposts> findFeedpostsEntities(int maxResults, int firstResult) {
		return findFeedpostsEntities(false, maxResults, firstResult);
	}

	private List<Feedposts> findFeedpostsEntities(boolean all, int maxResults, int firstResult) {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			cq.select(cq.from(Feedposts.class));
			Query q = em.createQuery(cq);
			if (!all) {
				q.setMaxResults(maxResults);
				q.setFirstResult(firstResult);
			}
			return q.getResultList();
		} finally {

		}
	}

	public Feedposts findFeedposts(Integer id) {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			return em.find(Feedposts.class, id);
		} finally {

		}
	}

	public int getFeedpostsCount() {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<Feedposts> rt = cq.from(Feedposts.class);
			cq.select(em.getCriteriaBuilder().count(rt));
			Query q = em.createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			em.close();
		}
	}

	public Date getLastPostDate(Integer feedId) {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			Query q = em.createNamedQuery("Feedposts.getPostsByFeedAndByDateSorted").setParameter("feedId", feedId);
			q.setMaxResults(1);
			return ((Feedposts) q.getSingleResult()).getPostdate();
		} catch (NoResultException e) {
			return null;
		} finally {
			if (em != null)
				em.close();
		}
	}

	public List<Feedposts> findFeedpostsByFeedSortedUnread(Integer id) {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			Query q = em.createNamedQuery("Feedposts.getPostsByFeedAndByDateSortedUnread").setParameter("feedId", id).setParameter("isread", false);
			return q.getResultList();
		} catch (NoResultException e) {
			return null;
		} finally {

		}
	}

	public List<Feedposts> findFeedpostsByFeed(Integer id, Integer page, Integer perpage) {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			Query q = em.createNamedQuery("Feedposts.getPostsByFeedAndByDateSorted").setParameter("feedId", id);
			q.setMaxResults(perpage);
			q.setFirstResult(page * perpage);
			return q.getResultList();
		} catch (NoResultException e) {
			return null;
		} finally {

		}
	}

	public List<Feedposts> findFeedpostsByFeedFromDate(Integer id, Date date) {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			Query q = em.createNamedQuery("Feedposts.getPostsByFeedAndByDate").setParameter("feedId", id).setParameter("pdate", date);
			return q.getResultList();
		} catch (NoResultException e) {
			return null;
		} finally {

		}
	}

	public void setPostsForFeedRead(Integer feedId, Boolean read) {
		try {
			if (!em.isOpen())
				em = getEntityManager();
			Query q = em.createNativeQuery("UPDATE feedposts SET isread = " + read + " WHERE feed_id = " + feedId.toString());
			em.getTransaction().begin();
			int updated = q.executeUpdate();
			log.info(updated + "posts set read");
			em.getTransaction().commit();
		} catch (NoResultException e) {
		} finally {

		}
	}
}
