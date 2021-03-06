package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.QueueItem;

public class QueueItemJpaDAO extends AbstractHibernateDAO<QueueItem, Integer> implements QueueItemDAO {

    private final Logger log = Logger.getLogger(getClass());

    @Override
    public QueueItem getFirstItem() {
        TypedQuery<QueueItem> query = getEntityManager().createQuery(
                "SELECT queueItem FROM QueueItem queueItem WHERE priority > 0 ORDER BY priority desc, id asc",
                QueueItem.class);
        query.setMaxResults(1);
        List<QueueItem> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }
    
    @Override
    public QueueItem getFirstItemWithAI() {
        TypedQuery<QueueItem> query = getEntityManager().createQuery(
                "SELECT queueItem FROM QueueItem queueItem WHERE priority > 0 ORDER BY priority desc, id asc",
                QueueItem.class);
        query.setMaxResults(1);
        List<QueueItem> results = query.getResultList();
        if (results.size() > 0) {
            QueueItem queueItem = results.get(0);
            //Lazy Load
            ArchivalInstitution ai = queueItem.getArchivalInstitution();
            ai.getAiId();
            return queueItem;
        }
        return null;
    }

    public List<QueueItem> getFirstItems() {
        TypedQuery<QueueItem> query = getEntityManager().createQuery(
                "SELECT queueItem FROM QueueItem queueItem WHERE priority > 0 ORDER BY priority desc, id asc",
                QueueItem.class);
        query.setMaxResults(50);
        return query.getResultList();

    }

    public List<QueueItem> getDisabledItems() {
        TypedQuery<QueueItem> query = getEntityManager().createQuery(
                "SELECT queueItem FROM QueueItem queueItem WHERE priority = 0 AND errors IS NULL ORDER BY priority desc, id asc",
                QueueItem.class);
        query.setMaxResults(50);
        return query.getResultList();

    }

    @Override
    public Long countItems() {
        TypedQuery<Long> query = getEntityManager().createQuery(
                "SELECT count(queueItem) FROM QueueItem queueItem WHERE priority > 0",
                Long.class);
        query.setMaxResults(1);
        return query.getResultList().get(0);
    }

    @Override
    public Long countItems(int aiId) {
        TypedQuery<Long> query = getEntityManager().createQuery(
                "SELECT count(queueItem) FROM QueueItem queueItem WHERE priority > 0 AND aiId = :aiId",
                Long.class);
        query.setParameter("aiId", aiId);
        query.setMaxResults(1);
        return query.getResultList().get(0);
    }

    public List<QueueItem> getItemsWithErrors() {
        TypedQuery<QueueItem> query = getEntityManager()
                .createQuery(
                        "SELECT queueItem FROM QueueItem queueItem WHERE priority = 0 AND errors IS NOT NULL ORDER BY priority desc, id asc",
                        QueueItem.class);
        query.setMaxResults(50);
        return query.getResultList();
    }

    @Override
    public boolean hasItemsWithErrors(int aiId) {
        TypedQuery<Integer> query = getEntityManager()
                .createQuery(
                        "SELECT queueItem.id FROM QueueItem queueItem WHERE priority = 0 AND errors IS NOT NULL AND aiId = :aiId ORDER BY priority desc, id asc",
                        Integer.class);
        query.setParameter("aiId", aiId);
        query.setMaxResults(1);
        return query.getResultList().size() > 0;
    }

    public QueueItem getFirstItem(int aiId) {
        TypedQuery<QueueItem> query = getEntityManager().createQuery(
                "SELECT queueItem FROM QueueItem queueItem WHERE priority > 0 AND aiId = :aiId ORDER BY priority desc, id asc",
                QueueItem.class);
        query.setParameter("aiId", aiId);
        query.setMaxResults(1);
        List<QueueItem> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Long getPositionOfFirstItem(int aiId) {
        QueueItem firstQueueItem = getFirstItem(aiId);
        if (firstQueueItem == null) {
            return null;
        } else {
            TypedQuery<Long> query = getEntityManager().createQuery(
                    "SELECT count(queueItem) FROM QueueItem queueItem WHERE priority > :priority OR (id < :id AND priority = :priority)",
                    Long.class);
            query.setParameter("id", firstQueueItem.getId());
            query.setParameter("priority", firstQueueItem.getPriority());
            query.setMaxResults(1);
            return query.getResultList().get(0);
        }
    }

    public List<Object[]> countByArchivalInstitutions() {
        TypedQuery<Object[]> query = getEntityManager().createQuery(
                "SELECT archivalInstitution, count(queueItem) FROM QueueItem queueItem JOIN queueItem.archivalInstitution archivalInstitution WHERE priority > 0 GROUP BY archivalInstitution",
                Object[].class);
        return query.getResultList();
    }

    public List<QueueItem> getItemsOfInstitution(Integer aiId) {
        TypedQuery<QueueItem> query = getEntityManager().createQuery(
                "SELECT queueItem FROM QueueItem queueItem WHERE aiId = :aiId ORDER BY priority desc, id asc",
                QueueItem.class);
                query.setParameter("aiId", aiId);
        return query.getResultList();
    }

    public List<QueueItem> getErrorItemsOfInstitution(Integer aiId) {
        TypedQuery<QueueItem> query = getEntityManager().createQuery(
                "SELECT queueItem FROM QueueItem queueItem WHERE aiId = :aiId AND priority = 0 AND errors IS NOT NULL ORDER BY priority desc, id asc",
                QueueItem.class);
        query.setParameter("aiId", aiId);
        return query.getResultList();
    }

    public void setPriorityToQueueOfArchivalInstitution(Integer aiId, Integer priority) {
        getEntityManager().getTransaction().begin();
        Query query = getEntityManager().createQuery(
                "UPDATE QueueItem queueItem SET queueItem.priority = :priority WHERE aiId = :aiId AND (queueItem.priority > 0 OR queueItem.errors is null)");
        query.setParameter("priority", priority);
        query.setParameter("aiId", aiId);
        query.executeUpdate();
        getEntityManager().getTransaction().commit();
    }
}
