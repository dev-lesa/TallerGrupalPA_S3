/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Clases.Celulares;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Usuarios;
import Clases.Chips;
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
public class CelularesJpaController implements Serializable {

    public CelularesJpaController() {
        this.emf = Persistence.createEntityManagerFactory("TallerSemana03PU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Celulares celulares) {
        if (celulares.getChipsCollection() == null) {
            celulares.setChipsCollection(new ArrayList<Chips>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios idUsuarios = celulares.getIdUsuarios();
            if (idUsuarios != null) {
                idUsuarios = em.getReference(idUsuarios.getClass(), idUsuarios.getIdUsuarios());
                celulares.setIdUsuarios(idUsuarios);
            }
            Collection<Chips> attachedChipsCollection = new ArrayList<Chips>();
            for (Chips chipsCollectionChipsToAttach : celulares.getChipsCollection()) {
                chipsCollectionChipsToAttach = em.getReference(chipsCollectionChipsToAttach.getClass(), chipsCollectionChipsToAttach.getIdChips());
                attachedChipsCollection.add(chipsCollectionChipsToAttach);
            }
            celulares.setChipsCollection(attachedChipsCollection);
            em.persist(celulares);
            if (idUsuarios != null) {
                idUsuarios.getCelularesCollection().add(celulares);
                idUsuarios = em.merge(idUsuarios);
            }
            for (Chips chipsCollectionChips : celulares.getChipsCollection()) {
                Celulares oldIdCelularesOfChipsCollectionChips = chipsCollectionChips.getIdCelulares();
                chipsCollectionChips.setIdCelulares(celulares);
                chipsCollectionChips = em.merge(chipsCollectionChips);
                if (oldIdCelularesOfChipsCollectionChips != null) {
                    oldIdCelularesOfChipsCollectionChips.getChipsCollection().remove(chipsCollectionChips);
                    oldIdCelularesOfChipsCollectionChips = em.merge(oldIdCelularesOfChipsCollectionChips);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Celulares celulares) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Celulares persistentCelulares = em.find(Celulares.class, celulares.getIdCelulares());
            Usuarios idUsuariosOld = persistentCelulares.getIdUsuarios();
            Usuarios idUsuariosNew = celulares.getIdUsuarios();
            Collection<Chips> chipsCollectionOld = persistentCelulares.getChipsCollection();
            Collection<Chips> chipsCollectionNew = celulares.getChipsCollection();
            List<String> illegalOrphanMessages = null;
            for (Chips chipsCollectionOldChips : chipsCollectionOld) {
                if (!chipsCollectionNew.contains(chipsCollectionOldChips)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Chips " + chipsCollectionOldChips + " since its idCelulares field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuariosNew != null) {
                idUsuariosNew = em.getReference(idUsuariosNew.getClass(), idUsuariosNew.getIdUsuarios());
                celulares.setIdUsuarios(idUsuariosNew);
            }
            Collection<Chips> attachedChipsCollectionNew = new ArrayList<Chips>();
            for (Chips chipsCollectionNewChipsToAttach : chipsCollectionNew) {
                chipsCollectionNewChipsToAttach = em.getReference(chipsCollectionNewChipsToAttach.getClass(), chipsCollectionNewChipsToAttach.getIdChips());
                attachedChipsCollectionNew.add(chipsCollectionNewChipsToAttach);
            }
            chipsCollectionNew = attachedChipsCollectionNew;
            celulares.setChipsCollection(chipsCollectionNew);
            celulares = em.merge(celulares);
            if (idUsuariosOld != null && !idUsuariosOld.equals(idUsuariosNew)) {
                idUsuariosOld.getCelularesCollection().remove(celulares);
                idUsuariosOld = em.merge(idUsuariosOld);
            }
            if (idUsuariosNew != null && !idUsuariosNew.equals(idUsuariosOld)) {
                idUsuariosNew.getCelularesCollection().add(celulares);
                idUsuariosNew = em.merge(idUsuariosNew);
            }
            for (Chips chipsCollectionNewChips : chipsCollectionNew) {
                if (!chipsCollectionOld.contains(chipsCollectionNewChips)) {
                    Celulares oldIdCelularesOfChipsCollectionNewChips = chipsCollectionNewChips.getIdCelulares();
                    chipsCollectionNewChips.setIdCelulares(celulares);
                    chipsCollectionNewChips = em.merge(chipsCollectionNewChips);
                    if (oldIdCelularesOfChipsCollectionNewChips != null && !oldIdCelularesOfChipsCollectionNewChips.equals(celulares)) {
                        oldIdCelularesOfChipsCollectionNewChips.getChipsCollection().remove(chipsCollectionNewChips);
                        oldIdCelularesOfChipsCollectionNewChips = em.merge(oldIdCelularesOfChipsCollectionNewChips);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = celulares.getIdCelulares();
                if (findCelulares(id) == null) {
                    throw new NonexistentEntityException("The celulares with id " + id + " no longer exists.");
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
            Celulares celulares;
            try {
                celulares = em.getReference(Celulares.class, id);
                celulares.getIdCelulares();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The celulares with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Chips> chipsCollectionOrphanCheck = celulares.getChipsCollection();
            for (Chips chipsCollectionOrphanCheckChips : chipsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Celulares (" + celulares + ") cannot be destroyed since the Chips " + chipsCollectionOrphanCheckChips + " in its chipsCollection field has a non-nullable idCelulares field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios idUsuarios = celulares.getIdUsuarios();
            if (idUsuarios != null) {
                idUsuarios.getCelularesCollection().remove(celulares);
                idUsuarios = em.merge(idUsuarios);
            }
            em.remove(celulares);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Celulares> findCelularesEntities() {
        return findCelularesEntities(true, -1, -1);
    }

    public List<Celulares> findCelularesEntities(int maxResults, int firstResult) {
        return findCelularesEntities(false, maxResults, firstResult);
    }

    private List<Celulares> findCelularesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Celulares.class));
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

    public Celulares findCelulares(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Celulares.class, id);
        } finally {
            em.close();
        }
    }

    public int getCelularesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Celulares> rt = cq.from(Celulares.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
