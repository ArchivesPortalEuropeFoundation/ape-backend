package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.util.ApeUtil;

public class ArchivalInstitutionHibernateDAO extends AbstractHibernateDAO<ArchivalInstitution, Integer> implements
        ArchivalInstitutionDAO {

    private final Logger log = Logger.getLogger(getClass());

    @SuppressWarnings("unchecked")
    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByParentAiId(Integer parentAiId, boolean order) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
        Criteria criteria = createArchivalInstitutionByParentAiIdCriteria(parentAiId, order);
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    public List<ArchivalInstitution> getGroupsAndArchivalInstitutionsByCountryId(Integer countryId, String sortValue,
            boolean ascending) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("countryId", countryId));
        if ("countryId".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("countryId"));
            } else {
                criteria.addOrder(Order.desc("countryId"));
            }
        }
        if ("ainame".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("ainame"));
            } else {
                criteria.addOrder(Order.desc("ainame"));
            }
        }
        if ("aiId".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("aiId"));
            } else {
                criteria.addOrder(Order.desc("aiId"));
            }
        }
        if ("alorder".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("alorder"));
            } else {
                criteria.addOrder(Order.desc("alorder"));
            }
        }
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    public Integer countArchivalInstitutionsByCountryId(Integer pId) {
        Criteria criteria = createCountCriteriaByCountryId(pId);
        criteria.setProjection(Projections.rowCount());
        Object object = criteria.list().get(0);

        if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof Long) {
            return ((Long) object).intValue();
        }
        return 0;

    }

    @Override
    public Integer countArchivalInstitutionsByParentAiId(Integer parentAiId) {
        Criteria criteria = createCountCriteriaByParentAiId(parentAiId);
        criteria.setProjection(Projections.rowCount());
        Object object = criteria.uniqueResult();
        if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof Long) {
            return ((Long) object).intValue();
        }
        return 0;

    }

    @Override
    public Long countTotalArchivalInstitutions() {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("group", false));
        criteria.add(Restrictions.isNotNull("eagPath"));
        criteria.setProjection(Projections.countDistinct("archivalInstitution.aiId")); // select
        // count(aiId)
        return (Long) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArchivalInstitution getArchivalInstitution(Integer aiId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("aiId", aiId));
        List<ArchivalInstitution> list = criteria.list();
        if (list.size() > 0) {
            return (ArchivalInstitution) list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public ArchivalInstitution getArchivalInstitutionByAiName(String InstitutionName) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("ainame", InstitutionName));
        List<ArchivalInstitution> list = criteria.list();
        if (list.size() > 0) {
            return (ArchivalInstitution) list.get(0);
        }
        return null;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByAiNameForCountryId(String InstitutionName, Integer countryId) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();

        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("ainame", InstitutionName));
        criteria.add(Restrictions.eq("countryId", countryId));

        results = criteria.list();

        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByPartnerId(Integer pId) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
        Criteria criteria = createArchivalInstitutionCriteria(false, "archivalInstitution", true);
        criteria.add(Restrictions.eq("archivalInstitution.partnerId", pId));
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryId(Integer countryId, boolean onlyWithoutPartnerIds) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = null;
        Criteria criteria = createArchivalInstitutionCriteria(false, "ainame", true);
        criteria.add(Restrictions.eq("countryId", countryId));
        if (onlyWithoutPartnerIds) {
            criteria.add(Restrictions.isNull("partnerId"));
        }
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryIdForAL(Integer countryId,
            boolean onlyWithoutPartnerIds) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = null;
        Criteria criteria = buildArchivalInstitutionCriteriaByCountryId(countryId, null, onlyWithoutPartnerIds, null);
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    /**
     * Criteria instance has been called to "archivalInstitution"
     */
    private Criteria buildArchivalInstitutionCriteriaByCountryId(Integer countryId, Collection<String> internalAlIds,
            boolean onlyWithoutParentIds, Boolean exclude) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("archivalInstitution.countryId", countryId));
        if (onlyWithoutParentIds) {
            criteria.add(Restrictions.isNull("archivalInstitution.parentAiId"));
        }
        if (internalAlIds != null && exclude != null && internalAlIds.size() > 0) {
            Criterion restriction = Restrictions.in("archivalInstitution.internalAlId", internalAlIds);
            if (exclude) {
                restriction = Restrictions.not(restriction);
            }
            criteria.add(restriction);
        }
        criteria.addOrder(Order.asc("archivalInstitution.alorder"));
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryId(Integer countryId) {
        return getArchivalInstitutionsByCountryId(countryId, false);
    }

    @SuppressWarnings("unchecked")
    public List<ArchivalInstitution> getArchivalInstitutionsWithRepositoryCode() {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("archivalInstitution.repositorycode"));
        criteria.add(Restrictions.isNotNull("repositorycode"));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public boolean isRepositoryCodeAvailable(String repositorycode, Integer aiId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("archivalInstitution.repositorycode"));
        criteria.add(Restrictions.eq("repositorycode", repositorycode));
        criteria.add(Restrictions.ne("aiId", aiId));
        criteria.setMaxResults(1);
        List<ArchivalInstitution> results = criteria.list();
        if (results.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private Criteria createArchivalInstitutionCriteria(boolean group, String sortValue, boolean ascending) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        criteria.add(Restrictions.eq("group", group));
        if ("countryId".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("archivalInstitution.countryId"));
            } else {
                criteria.addOrder(Order.desc("archivalInstitution.countryId"));
            }
        }
        if ("pId".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("archivalInstitution.pId"));
            } else {
                criteria.addOrder(Order.desc("archivalInstitution.pId"));
            }
        }
        if ("ainame".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("archivalInstitution.ainame"));
            } else {
                criteria.addOrder(Order.desc("archivalInstitution.ainame"));
            }
        }
        if ("aiId".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("archivalInstitution.aiId"));
            } else {
                criteria.addOrder(Order.desc("archivalInstitution.aiId"));
            }
        }
        if ("alorder".equals(sortValue)) {
            if (ascending) {
                criteria.addOrder(Order.asc("archivalInstitution.alorder"));
            } else {
                criteria.addOrder(Order.desc("archivalInstitution.alorder"));
            }
        }

        return criteria;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArchivalInstitution> getRootArchivalInstitutionsByCountryId(Integer countryId) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("countryId", countryId));
        criteria.add(Restrictions.isNull("parent"));
        List<ArchivalInstitution> results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsWithSearchableItems(Integer countryId, Integer parentAiId) {
        long startTime = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArchivalInstitution> cq = criteriaBuilder.createQuery(ArchivalInstitution.class);
        Root<ArchivalInstitution> from = cq.from(ArchivalInstitution.class);
        List<Predicate> whereClause = new ArrayList<Predicate>();
        if (countryId != null) {
            whereClause.add(criteriaBuilder.equal(from.get("countryId"), countryId));
        }
        if (parentAiId == null) {
            whereClause.add(criteriaBuilder.isNull(from.get("parentAiId")));
        } else {
            whereClause.add(criteriaBuilder.equal(from.get("parentAiId"), parentAiId));
        }
        whereClause.add(criteriaBuilder.equal(from.get("containSearchableItems"), true));
        cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
        cq.orderBy(criteriaBuilder.asc(from.get("alorder")));
        List<ArchivalInstitution> results = getEntityManager().createQuery(cq).getResultList();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsWithoutGroupsWithSearchableItems() {
        long startTime = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArchivalInstitution> cq = criteriaBuilder.createQuery(ArchivalInstitution.class);
        Root<ArchivalInstitution> from = cq.from(ArchivalInstitution.class);
        List<Predicate> whereClause = new ArrayList<Predicate>();
        whereClause.add(criteriaBuilder.equal(from.get("containSearchableItems"), true));
        whereClause.add(criteriaBuilder.equal(from.get("group"), false));
        cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
        List<ArchivalInstitution> results = getEntityManager().createQuery(cq).getResultList();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsWithoutGroups() {
        long startTime = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArchivalInstitution> cq = criteriaBuilder.createQuery(ArchivalInstitution.class);
        Root<ArchivalInstitution> from = cq.from(ArchivalInstitution.class);
        List<Predicate> whereClause = new ArrayList<Predicate>();
        whereClause.add(criteriaBuilder.equal(from.get("group"), false));
        whereClause.add(criteriaBuilder.isNotNull(from.get("eagPath")));
        cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
        List<ArchivalInstitution> results = getEntityManager().createQuery(cq).getResultList();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    private Criteria createArchivalInstitutionByParentAiIdCriteria(Integer parentAiId, boolean order) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (parentAiId != null) {
            criteria.add(Restrictions.eq("archivalInstitution.parent.aiId", parentAiId.intValue()));
        } else {
            criteria.add(Restrictions.isNull("archivalInstitution.parent.aiId"));
        }

        if (order) {
            criteria.addOrder(Order.asc("archivalInstitution.alorder"));
        }

        return criteria;
    }

    private Criteria createCountCriteriaByParentAiId(Integer parentAiId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (parentAiId != null) {
            criteria.add(Restrictions.eq("archivalInstitution.parent.aiId", parentAiId.intValue()));
        }
        return criteria;
    }

    private Criteria createCountCriteriaByCountryId(Integer countryId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (countryId != null) {
            criteria.add(Restrictions.eq("countryId", countryId));
        }
        return criteria;
    }

    @SuppressWarnings("unchecked")
    public ArchivalInstitution getArchivalInstitutionByRepositoryCode(String repositorycode) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (repositorycode != null) {
            criteria.add(Restrictions.eq("repositorycode", ApeUtil.decodeRepositoryCode(repositorycode)));
        }
        List<ArchivalInstitution> archivalInstitutions = criteria.list();
        if (archivalInstitutions.size() > 0) {
            return archivalInstitutions.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<ArchivalInstitution> getArchivalInstitutionsByAutform(String autform) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
        Criteria criteria = createArchivalInstitutionsByAutformCriteria(autform);
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsGroupsByCountryId(Integer couId) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = new ArrayList<ArchivalInstitution>();
        Criteria criteria = getCriteriaByGroups(couId);
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    private Criteria getCriteriaByGroups(Integer couId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (couId != null) {
            criteria.add(Restrictions.eq("countryId", couId));
        }
        criteria.add(Restrictions.eq("group", true));
        return criteria;
    }

    @Override
	public List<ArchivalInstitution> getArchivalInstitutionsGroupsByCountryId(Integer couId,boolean hasParent,boolean orderAsc) {
        List<ArchivalInstitution> results = null;
        Criteria criteria = getCriteriaByGroups(couId);
		criteria.add((hasParent)?Restrictions.isNotNull("archivalInstitution.parentAiId"):Restrictions.isNull("archivalInstitution.parentAiId"));
		if(hasParent){
            criteria.addOrder(Order.asc("archivalInstitution.parentAiId"));
        }
		criteria.addOrder((orderAsc)?Order.asc("archivalInstitution.alorder"):Order.desc("archivalInstitution.alorder"));
        results = criteria.list();
        return results;
    }

    private Criteria createArchivalInstitutionsByAutformCriteria(String autform) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (autform != null) {
            criteria.add(Restrictions.eq("autform", autform));
        }
        return criteria;
    }

    @Override
    public ArchivalInstitution getArchivalInstitutionsByCountryIdandAlIdentifier(Integer countryId, String internalAlId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("countryId", countryId));
        if (internalAlId != null) {
            criteria.add(Restrictions.eq("internalAlId", internalAlId));
        }
        return ((ArchivalInstitution) criteria.uniqueResult());
    }

    @Override
    public ArchivalInstitution getArchivalInstitutionByInternalAlId(String identifier, Integer countryIdentifier) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("archivalInstitution.countryId", countryIdentifier));
        criteria.add(Restrictions.eq("archivalInstitution.internalAlId", identifier));
        return (ArchivalInstitution) criteria.uniqueResult();
    }

    // This method retrieves the number of archival institutions which have
    // content indexed in the System
    public Long countArchivalInstitutionsWithEag() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<ArchivalInstitution> from = cq.from(ArchivalInstitution.class);
        List<Predicate> whereClause = new ArrayList<Predicate>();
        whereClause.add(criteriaBuilder.equal(from.get("group"), false));
        whereClause.add(criteriaBuilder.isNotNull(from.get("eagPath")));
        cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
        cq.select(criteriaBuilder.countDistinct(from));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryIdUnless(Integer countryId,
            List<ArchivalInstitution> archivalInstitutionUnless, boolean onlyWithoutPartnerIds) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = null;
        Iterator<ArchivalInstitution> it = archivalInstitutionUnless.iterator();
        Set<String> internalAlIds = new HashSet<String>();
        while (it.hasNext()) {
            ArchivalInstitution aiLess = it.next();
            if (aiLess.getInternalAlId() != null) { // better could be used
                // aiId, but by now is a
                // primitive value, and null
                // checks are not available
                internalAlIds.add(aiLess.getInternalAlId());
            }
        }
        Criteria criteria = buildArchivalInstitutionCriteriaByCountryId(countryId, internalAlIds,
                onlyWithoutPartnerIds, true);
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryIdUnless(Integer countryId,
            Collection<String> internalAlIds, boolean onlyWithoutPartnerIds) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = null;
        Criteria criteria = buildArchivalInstitutionCriteriaByCountryId(countryId, internalAlIds,
                onlyWithoutPartnerIds, true);
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryIdIncluded(Integer countryId,
            List<ArchivalInstitution> archivalInstitutionUnless, boolean onlyWithoutPartnerIds) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = null;
        Iterator<ArchivalInstitution> it = archivalInstitutionUnless.iterator();
        Set<String> internalAlIds = new HashSet<String>();
        while (it.hasNext()) {
            ArchivalInstitution aiLess = it.next();
            if (aiLess.getInternalAlId() != null) { // better could be used
                // aiId, but by now is a
                // primitive value, and null
                // checks are not available
                internalAlIds.add(aiLess.getInternalAlId());
            }
        }
        Criteria criteria = buildArchivalInstitutionCriteriaByCountryId(countryId, internalAlIds,
                onlyWithoutPartnerIds, false);
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryIdIncluded(Integer countryId,
            Collection<String> internalAlIds, boolean onlyWithoutPartnerIds) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = null;
        Criteria criteria = buildArchivalInstitutionCriteriaByCountryId(countryId, internalAlIds,
                onlyWithoutPartnerIds, false);
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryId(Integer countryId,
            boolean onlyWithoutPartnerIds, boolean hasContentIndexed) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = null;
        Criteria criteria = buildArchivalInstitutionCriteriaByCountryId(countryId, null, onlyWithoutPartnerIds, null); // archivalInstitution
        criteria.add(Restrictions.eq("archivalInstitution.containSearchableItems", hasContentIndexed));
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<String> getArchivalInstitutionInternalIdentifiersByCountryId(Integer countryId) {
        Criteria criteria = buildArchivalInstitutionCriteriaByCountryId(countryId, null, false, null); // archivalInstitution
        criteria.setProjection(Projections.property("internalAlId"));
        return criteria.list();
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsNoGroups(Integer countryId, Integer userId) {
        long startTime = System.currentTimeMillis();
        List<ArchivalInstitution> results = null;
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("archivalInstitution.countryId", countryId));
        criteria.add(Restrictions.eq("archivalInstitution.group", false));
        criteria.addOrder(Order.asc("archivalInstitution.ainame"));
        if (userId == null) {
            criteria.add(Restrictions.isNull("archivalInstitution.partnerId"));
        } else {
            criteria.add(Restrictions.eq("archivalInstitution.partnerId", userId));
        }
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsByOaiPmhSets(List<String> oaiPmhSets) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArchivalInstitution> cq = criteriaBuilder.createQuery(ArchivalInstitution.class);
        Root<ArchivalInstitution> from = cq.from(ArchivalInstitution.class);
        List<Predicate> whereClause = new ArrayList<Predicate>();
        for (String oaiPmhSet : oaiPmhSets) {
            whereClause.add(criteriaBuilder.equal(from.get("repositorycode"), oaiPmhSet));
        }
        cq.where(criteriaBuilder.or(whereClause.toArray(new Predicate[0])));
        cq.select(from);
        cq.orderBy(criteriaBuilder.asc(from.get("repositorycode")));
        TypedQuery<ArchivalInstitution> query = getEntityManager().createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutions(List<Integer> aiIds) {
        if (aiIds != null && aiIds.size() > 0) {
            CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<ArchivalInstitution> cq = criteriaBuilder.createQuery(ArchivalInstitution.class);
            Root<ArchivalInstitution> from = cq.from(ArchivalInstitution.class);
            List<Predicate> whereClause = new ArrayList<Predicate>();
            for (Integer aiId : aiIds) {
                whereClause.add(criteriaBuilder.equal(from.get("aiId"), aiId));
            }
            cq.where(criteriaBuilder.or(whereClause.toArray(new Predicate[0])));
            cq.orderBy(criteriaBuilder.asc(from.get("ainame")));
            return getEntityManager().createQuery(cq).getResultList();
        } else {
            return new ArrayList<ArchivalInstitution>();
        }

    }

    @Override
    public Integer countArchivalInstitutionsWithOpenDataEnabled() {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("archivalInstitution.openDataEnabled", true));
        criteria.setProjection(Projections.rowCount());
        Object object = criteria.uniqueResult();
        if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof Long) {
            return ((Long) object).intValue();
        }
        return 0;
    }

    @Override
    public List<ArchivalInstitution> getArchivalInstitutionsWithOpenDataEnabled(Integer start, Integer limit) {
        List<ArchivalInstitution> results = null;

        Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        criteria.add(Restrictions.eq("archivalInstitution.openDataEnabled", true));
        criteria.addOrder(Order.asc("archivalInstitution.alorder"));

        results = criteria.list();
        return results;
    }

}
