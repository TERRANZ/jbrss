/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terra.jbrss.jabber.db.controller;

import ru.terra.jbrss.jabber.db.controller.exceptions.NonexistentEntityException;
import ru.terra.jbrss.jabber.db.entity.Contact;
import ru.terra.server.db.controllers.AbstractJpaController;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;

/**
 * @author terranz
 */
public class ContactJpaController extends AbstractJpaController<Contact> implements Serializable {
    public ContactJpaController(Class entityClass) {
        super(entityClass);
    }

    public void create(Contact contact) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(contact);
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
            Contact contact;
            try {
                contact = em.getReference(Contact.class, id);
                contact.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contact with id " + id + " no longer exists.", enfe);
            }
            em.remove(contact);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void update(Contact contact) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            contact = em.merge(contact);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contact.getId();
                if (get(id) == null) {
                    throw new NonexistentEntityException("The contact with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public boolean isContactExists(String contact) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Contact.findByContact").setParameter("contact", contact);
            return q.getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        } finally {
            em.close();
        }
    }

    public Contact findByContact(String contact) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Contact.findByContact").setParameter("contact", contact);
            return (Contact) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
