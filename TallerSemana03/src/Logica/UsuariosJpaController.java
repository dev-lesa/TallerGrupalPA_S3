/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Celulares;
import Clases.Usuarios;
import Logica.exceptions.IllegalOrphanException;
import Logica.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Luis
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController() {
        this.emf = Persistence.createEntityManagerFactory("TallerSemana03PU");;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getCelularesCollection() == null) {
            usuarios.setCelularesCollection(new ArrayList<Celulares>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Celulares> attachedCelularesCollection = new ArrayList<Celulares>();
            for (Celulares celularesCollectionCelularesToAttach : usuarios.getCelularesCollection()) {
                celularesCollectionCelularesToAttach = em.getReference(celularesCollectionCelularesToAttach.getClass(), celularesCollectionCelularesToAttach.getIdCelulares());
                attachedCelularesCollection.add(celularesCollectionCelularesToAttach);
            }
            usuarios.setCelularesCollection(attachedCelularesCollection);
            em.persist(usuarios);
            for (Celulares celularesCollectionCelulares : usuarios.getCelularesCollection()) {
                Usuarios oldIdUsuariosOfCelularesCollectionCelulares = celularesCollectionCelulares.getIdUsuarios();
                celularesCollectionCelulares.setIdUsuarios(usuarios);
                celularesCollectionCelulares = em.merge(celularesCollectionCelulares);
                if (oldIdUsuariosOfCelularesCollectionCelulares != null) {
                    oldIdUsuariosOfCelularesCollectionCelulares.getCelularesCollection().remove(celularesCollectionCelulares);
                    oldIdUsuariosOfCelularesCollectionCelulares = em.merge(oldIdUsuariosOfCelularesCollectionCelulares);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuarios());
            Collection<Celulares> celularesCollectionOld = persistentUsuarios.getCelularesCollection();
            Collection<Celulares> celularesCollectionNew = usuarios.getCelularesCollection();
            List<String> illegalOrphanMessages = null;
            for (Celulares celularesCollectionOldCelulares : celularesCollectionOld) {
                if (!celularesCollectionNew.contains(celularesCollectionOldCelulares)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Celulares " + celularesCollectionOldCelulares + " since its idUsuarios field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Celulares> attachedCelularesCollectionNew = new ArrayList<Celulares>();
            for (Celulares celularesCollectionNewCelularesToAttach : celularesCollectionNew) {
                celularesCollectionNewCelularesToAttach = em.getReference(celularesCollectionNewCelularesToAttach.getClass(), celularesCollectionNewCelularesToAttach.getIdCelulares());
                attachedCelularesCollectionNew.add(celularesCollectionNewCelularesToAttach);
            }
            celularesCollectionNew = attachedCelularesCollectionNew;
            usuarios.setCelularesCollection(celularesCollectionNew);
            usuarios = em.merge(usuarios);
            for (Celulares celularesCollectionNewCelulares : celularesCollectionNew) {
                if (!celularesCollectionOld.contains(celularesCollectionNewCelulares)) {
                    Usuarios oldIdUsuariosOfCelularesCollectionNewCelulares = celularesCollectionNewCelulares.getIdUsuarios();
                    celularesCollectionNewCelulares.setIdUsuarios(usuarios);
                    celularesCollectionNewCelulares = em.merge(celularesCollectionNewCelulares);
                    if (oldIdUsuariosOfCelularesCollectionNewCelulares != null && !oldIdUsuariosOfCelularesCollectionNewCelulares.equals(usuarios)) {
                        oldIdUsuariosOfCelularesCollectionNewCelulares.getCelularesCollection().remove(celularesCollectionNewCelulares);
                        oldIdUsuariosOfCelularesCollectionNewCelulares = em.merge(oldIdUsuariosOfCelularesCollectionNewCelulares);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getIdUsuarios();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuarios();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Celulares> celularesCollectionOrphanCheck = usuarios.getCelularesCollection();
            for (Celulares celularesCollectionOrphanCheckCelulares : celularesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Celulares " + celularesCollectionOrphanCheckCelulares + " in its celularesCollection field has a non-nullable idUsuarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
