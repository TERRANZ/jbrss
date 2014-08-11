/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terra.jbrss.db.controllers;

import ru.terra.jbrss.db.controllers.exceptions.NonexistentEntityException;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.server.db.controllers.AbstractJpaController;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author terranz
 */
public class SettingsJpaController extends AbstractJpaController<Settings> {
    public SettingsJpaController() {
        super(Settings.class);
    }

    @Override
    public void create(Settings settings) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(settings);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Settings settings;
            try {
                settings = em.getReference(Settings.class, id);
                settings.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The settings with id " + id + " no longer exists.", enfe);
            }
            em.remove(settings);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void update(Settings settings) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            settings = em.merge(settings);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = settings.getId();
                if (get(id) == null) {
                    throw new NonexistentEntityException("The settings with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Settings findByKey(String key, Integer uid) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.createNamedQuery("Settings.findByKey", Settings.class).setParameter("key", key).setParameter("uid", uid).getSingleResult();
        } catch (Exception ex) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Settings> findByUser(Integer uid) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.createNamedQuery("Settings.findByUser", Settings.class).setParameter("uid", uid).getResultList();
        } catch (Exception ex) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
