package Logica;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Chips;
import Clases.Recargas;
import Logica.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RecargasJpaController implements Serializable {

    public RecargasJpaController() {
        this.emf = Persistence.createEntityManagerFactory("TallerSemana03PU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recargas recargas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Chips idChips = recargas.getIdChips();
            if (idChips != null) {
                idChips = em.getReference(idChips.getClass(), idChips.getIdChips());
                recargas.setIdChips(idChips);
            }
            em.persist(recargas);
            if (idChips != null) {
                idChips.setSaldo(idChips.getSaldo().add(recargas.getValor()));
                idChips.getRecargasCollection().add(recargas);
                idChips = em.merge(idChips);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recargas recargas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recargas persistentRecargas = em.find(Recargas.class, recargas.getIdRecargas());
            Chips idChipsOld = persistentRecargas.getIdChips();
            Chips idChipsNew = recargas.getIdChips();
            if (idChipsNew != null) {
                idChipsNew = em.getReference(idChipsNew.getClass(), idChipsNew.getIdChips());
                recargas.setIdChips(idChipsNew);
            }
            recargas = em.merge(recargas);
            if (idChipsOld != null && !idChipsOld.equals(idChipsNew)) {
                idChipsOld.getRecargasCollection().remove(recargas);
                idChipsOld = em.merge(idChipsOld);
            }
            if (idChipsNew != null && !idChipsNew.equals(idChipsOld)) {
                idChipsNew.getRecargasCollection().add(recargas);
                idChipsNew = em.merge(idChipsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recargas.getIdRecargas();
                if (findRecargas(id) == null) {
                    throw new NonexistentEntityException("The recargas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recargas recargas;
            try {
                recargas = em.getReference(Recargas.class, id);
                recargas.getIdRecargas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recargas with id " + id + " no longer exists.", enfe);
            }
            Chips idChips = recargas.getIdChips();
            if (idChips != null) {
                idChips.getRecargasCollection().remove(recargas);
                idChips = em.merge(idChips);
            }
            em.remove(recargas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recargas> findRecargasEntities() {
        return findRecargasEntities(true, -1, -1);
    }

    public List<Recargas> findRecargasEntities(int maxResults, int firstResult) {
        return findRecargasEntities(false, maxResults, firstResult);
    }

    private List<Recargas> findRecargasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recargas.class));
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

    public Recargas findRecargas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recargas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecargasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recargas> rt = cq.from(Recargas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}