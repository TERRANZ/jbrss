package ru.terra.jbrss.db.controllers;

import ru.terra.jbrss.db.controllers.exceptions.NonexistentEntityException;
import ru.terra.jbrss.db.entity.Feeds;
import ru.terra.server.db.controllers.AbstractJpaController;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * @author terranz
 */
public class FeedsJpaController extends AbstractJpaController<Feeds> implements Serializable {

    public FeedsJpaController() {
        super(Feeds.class);
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

    @Override
    public void delete(Integer id) throws Exception {
        EntityManager em;
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

    @Override
    public void update(Feeds feeds) throws Exception {
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
                if (get(id) == null) {
                    throw new NonexistentEntityException("The feeds with id " + id + " no longer exists.");
                }
            }
            throw ex;
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

    public Feeds findFeedByUrl(String url) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Feeds.findByFeedurl").setParameter("feedurl", url);
        try {
            return (Feeds) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {

        }
    }
}
