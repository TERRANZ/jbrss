/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.terra.jbrss.db.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.db.controllers.exceptions.NonexistentEntityException;
import ru.terra.jbrss.db.entity.Feedposts;
import ru.terra.server.db.controllers.AbstractJpaController;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author terranz
 */
public class FeedpostsJpaController extends AbstractJpaController<Feedposts> implements Serializable {
    Logger log = LoggerFactory.getLogger(FeedpostsJpaController.class);
    private EntityManager em = getEntityManager();

    public FeedpostsJpaController() {
        super(Feedposts.class);
    }

    public void create(List<Feedposts> feedpostses) {
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            for (Feedposts feedposts : feedpostses) {
                feedposts.setUpdated(new Date());
                em.persist(feedposts);
            }
            em.getTransaction().commit();
        } finally {

        }
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

    @Override
    public void delete(Integer id) throws Exception {
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

    @Override
    public void update(Feedposts feedposts) throws Exception {
        try {
            em.getTransaction().begin();
            feedposts.setUpdated(new Date());
            feedposts = em.merge(feedposts);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = feedposts.getId();
                if (get(id) == null) {
                    throw new NonexistentEntityException("The feedposts with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {

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
            Query q = em.createNativeQuery("UPDATE feedposts SET isread = " + read + ",update_time = '" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'  WHERE feed_id = " + feedId.toString() + " AND isread = false");
            em.getTransaction().begin();
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (NoResultException e) {
        } finally {
        }
    }

    public Integer removePosts(Integer feedId) {
        try {
            if (!em.isOpen())
                em = getEntityManager();
            Query q = em.createNativeQuery("DELETE FROM feedposts WHERE feed_id = " + feedId.toString());
            em.getTransaction().begin();
            int deleted = q.executeUpdate();
            em.getTransaction().commit();
            return deleted;
        } catch (NoResultException e) {
            return 0;
        } finally {
        }
    }
}
