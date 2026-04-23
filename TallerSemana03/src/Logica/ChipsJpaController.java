package Logica;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Celulares;
import Clases.Chips;
import Clases.Recargas;
import Logica.exceptions.IllegalOrphanException;
import Logica.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ChipsJpaController implements Serializable {

    public ChipsJpaController() {
        this.emf = Persistence.createEntityManagerFactory("TallerSemana03PU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public boolean chipSlotOcupado(int idCelulares, int chipSlot) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery(
                "SELECT COUNT(c) FROM Chips c WHERE c.idCelulares.idCelulares = :idCelulares AND c.chipSlot = :chipSlot"
            );
            q.setParameter("idCelulares", idCelulares);
            q.setParameter("chipSlot", chipSlot);
            long count = (long) q.getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void create(Chips chips) throws Exception {
        if (chipSlotOcupado(chips.getIdCelulares().getIdCelulares(), chips.getChipSlot())) {
            throw new Exception("El slot " + chips.getChipSlot() +
                " ya esta ocupado en el celular con id " + chips.getIdCelulares().getIdCelulares());
        }

        if (chips.getRecargasCollection() == null) {
            chips.setRecargasCollection(new ArrayList<Recargas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Celulares idCelulares = chips.getIdCelulares();
            if (idCelulares != null) {
                idCelulares = em.getReference(idCelulares.getClass(), idCelulares.getIdCelulares());
                chips.setIdCelulares(idCelulares);
            }
            Collection<Recargas> attachedRecargasCollection = new ArrayList<Recargas>();
            for (Recargas recargasCollectionRecargasToAttach : chips.getRecargasCollection()) {
                recargasCollectionRecargasToAttach = em.getReference(recargasCollectionRecargasToAttach.getClass(), recargasCollectionRecargasToAttach.getIdRecargas());
                attachedRecargasCollection.add(recargasCollectionRecargasToAttach);
            }
            chips.setRecargasCollection(attachedRecargasCollection);
            em.persist(chips);
            if (idCelulares != null) {
                idCelulares.getChipsCollection().add(chips);
                idCelulares = em.merge(idCelulares);
            }
            for (Recargas recargasCollectionRecargas : chips.getRecargasCollection()) {
                Chips oldIdChipsOfRecargasCollectionRecargas = recargasCollectionRecargas.getIdChips();
                recargasCollectionRecargas.setIdChips(chips);
                recargasCollectionRecargas = em.merge(recargasCollectionRecargas);
                if (oldIdChipsOfRecargasCollectionRecargas != null) {
                    oldIdChipsOfRecargasCollectionRecargas.getRecargasCollection().remove(recargasCollectionRecargas);
                    oldIdChipsOfRecargasCollectionRecargas = em.merge(oldIdChipsOfRecargasCollectionRecargas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Chips chips) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Chips persistentChips = em.find(Chips.class, chips.getIdChips());
            Celulares idCelularesOld = persistentChips.getIdCelulares();
            Celulares idCelularesNew = chips.getIdCelulares();
            Collection<Recargas> recargasCollectionOld = persistentChips.getRecargasCollection();
            Collection<Recargas> recargasCollectionNew = chips.getRecargasCollection();
            List<String> illegalOrphanMessages = null;
            for (Recargas recargasCollectionOldRecargas : recargasCollectionOld) {
                if (!recargasCollectionNew.contains(recargasCollectionOldRecargas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recargas " + recargasCollectionOldRecargas + " since its idChips field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCelularesNew != null) {
                idCelularesNew = em.getReference(idCelularesNew.getClass(), idCelularesNew.getIdCelulares());
                chips.setIdCelulares(idCelularesNew);
            }
            Collection<Recargas> attachedRecargasCollectionNew = new ArrayList<Recargas>();
            for (Recargas recargasCollectionNewRecargasToAttach : recargasCollectionNew) {
                recargasCollectionNewRecargasToAttach = em.getReference(recargasCollectionNewRecargasToAttach.getClass(), recargasCollectionNewRecargasToAttach.getIdRecargas());
                attachedRecargasCollectionNew.add(recargasCollectionNewRecargasToAttach);
            }
            recargasCollectionNew = attachedRecargasCollectionNew;
            chips.setRecargasCollection(recargasCollectionNew);
            chips = em.merge(chips);
            if (idCelularesOld != null && !idCelularesOld.equals(idCelularesNew)) {
                idCelularesOld.getChipsCollection().remove(chips);
                idCelularesOld = em.merge(idCelularesOld);
            }
            if (idCelularesNew != null && !idCelularesNew.equals(idCelularesOld)) {
                idCelularesNew.getChipsCollection().add(chips);
                idCelularesNew = em.merge(idCelularesNew);
            }
            for (Recargas recargasCollectionNewRecargas : recargasCollectionNew) {
                if (!recargasCollectionOld.contains(recargasCollectionNewRecargas)) {
                    Chips oldIdChipsOfRecargasCollectionNewRecargas = recargasCollectionNewRecargas.getIdChips();
                    recargasCollectionNewRecargas.setIdChips(chips);
                    recargasCollectionNewRecargas = em.merge(recargasCollectionNewRecargas);
                    if (oldIdChipsOfRecargasCollectionNewRecargas != null && !oldIdChipsOfRecargasCollectionNewRecargas.equals(chips)) {
                        oldIdChipsOfRecargasCollectionNewRecargas.getRecargasCollection().remove(recargasCollectionNewRecargas);
                        oldIdChipsOfRecargasCollectionNewRecargas = em.merge(oldIdChipsOfRecargasCollectionNewRecargas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = chips.getIdChips();
                if (findChips(id) == null) {
                    throw new NonexistentEntityException("The chips with id " + id + " no longer exists.");
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
            Chips chips;
            try {
                chips = em.getReference(Chips.class, id);
                chips.getIdChips();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The chips with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Recargas> recargasCollectionOrphanCheck = chips.getRecargasCollection();
            for (Recargas recargasCollectionOrphanCheckRecargas : recargasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Chips (" + chips + ") cannot be destroyed since the Recargas " + recargasCollectionOrphanCheckRecargas + " in its recargasCollection field has a non-nullable idChips field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Celulares idCelulares = chips.getIdCelulares();
            if (idCelulares != null) {
                idCelulares.getChipsCollection().remove(chips);
                idCelulares = em.merge(idCelulares);
            }
            em.remove(chips);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Chips> findChipsEntities() {
        return findChipsEntities(true, -1, -1);
    }

    public List<Chips> findChipsEntities(int maxResults, int firstResult) {
        return findChipsEntities(false, maxResults, firstResult);
    }

    private List<Chips> findChipsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Chips.class));
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

    public Chips findChips(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Chips.class, id);
        } finally {
            em.close();
        }
    }

    public int getChipsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Chips> rt = cq.from(Chips.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}