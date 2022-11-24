package eu.apenet.persistence.hibernate;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.util.ApeUtil;
import java.util.ArrayList;

public class CLevelHibernateDAO extends AbstractHibernateDAO<CLevel, Long> implements CLevelDAO {

    private final static Logger LOG = Logger.getLogger(CLevelHibernateDAO.class);

    @Override
    public CLevel getChildCLevel(Long parentId, Integer orderId) {
        String jpaQuery = "SELECT clevel FROM CLevel clevel WHERE clevel.parentId = :parentId AND clevel.orderId = :orderId";
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("orderId", orderId);
        query.setParameter("parentId", parentId);
        query.setMaxResults(1);
        List<CLevel> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Long getChildCLevelId(Long parentId, Integer orderId) {
        String jpaQuery = "SELECT clevel.id FROM CLevel clevel WHERE clevel.parentId = :parentId AND clevel.orderId = :orderId";
        TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);
        query.setParameter("orderId", orderId);
        query.setParameter("parentId", parentId);
        query.setMaxResults(1);
        List<Long> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public CLevel getTopCLevel(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, Integer orderId) {
        String varName = "findingAid";
        if (FindingAid.class.equals(clazz)) {
            varName = "findingAid";
        }
        if (SourceGuide.class.equals(clazz)) {
            varName = "sourceGuide";
        } else if (HoldingsGuide.class.equals(clazz)) {
            varName = "holdingsGuide";
        }

        String jpaQuery = "SELECT clevel FROM CLevel clevel JOIN clevel.eadContent eadContent JOIN eadContent." + varName + " ead JOIN ead.archivalInstitution archivalInstitution WHERE ead.eadid= :eadid AND ead.published = true AND archivalInstitution.repositorycode = :repoCode AND clevel.parentId IS NULL AND clevel.orderId = :orderId";
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("orderId", orderId);
        query.setParameter("eadid", ApeUtil.decodeSpecialCharacters(eadid));
        query.setParameter("repoCode", ApeUtil.decodeRepositoryCode(repositoryCode));
        query.setMaxResults(1);
        List<CLevel> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Long getTopCLevelId(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, Integer orderId) {
        String varName = "findingAid";
        if (FindingAid.class.equals(clazz)) {
            varName = "findingAid";
        }
        if (SourceGuide.class.equals(clazz)) {
            varName = "sourceGuide";
        } else if (HoldingsGuide.class.equals(clazz)) {
            varName = "holdingsGuide";
        }

        String jpaQuery = "SELECT clevel.id FROM CLevel clevel JOIN clevel.eadContent eadContent JOIN eadContent." + varName + " ead JOIN ead.archivalInstitution archivalInstitution WHERE ead.eadid= :eadid AND ead.published = true AND archivalInstitution.repositorycode = :repoCode AND clevel.parentId IS NULL AND clevel.orderId = :orderId";
        TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);
        query.setParameter("orderId", orderId);
        query.setParameter("eadid", ApeUtil.decodeSpecialCharacters(eadid));
        query.setParameter("repoCode", ApeUtil.decodeRepositoryCode(repositoryCode));
        query.setMaxResults(1);
        List<Long> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public CLevel getCLevelByCid(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, String cid) {
        String varName = "findingAid";
        if (FindingAid.class.equals(clazz)) {
            varName = "findingAid";
        }
        if (SourceGuide.class.equals(clazz)) {
            varName = "sourceGuide";
        } else if (HoldingsGuide.class.equals(clazz)) {
            varName = "holdingsGuide";
        }

        String jpaQuery = "SELECT clevel FROM CLevel clevel JOIN clevel.eadContent eadContent JOIN eadContent." + varName + " ead JOIN ead.archivalInstitution archivalInstitution WHERE clevel.cid  = :cid AND ead.eadid= :eadid AND ead.published = true AND archivalInstitution.repositorycode = :repoCode";
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("cid", ApeUtil.decodeSpecialCharacters(cid));
        query.setParameter("eadid", ApeUtil.decodeSpecialCharacters(eadid));
        query.setParameter("repoCode", ApeUtil.decodeRepositoryCode(repositoryCode));
        query.setMaxResults(1);
        if (query.getResultList().size() > 0) {
            return query.getResultList().get(0);
        }
        return null;
    }

    @Override
    public CLevel getCLevelByUnitId(String unitId, String repositoryCode, String ead3Id) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("clevel.unitid", unitId));
        criteria.createAlias("clevel.ead3", "ead3");
        criteria.add(Restrictions.eq("ead3.id", Integer.parseInt(ead3Id)));
        criteria.setMaxResults(1);
        List<CLevel> results = criteria.list();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public List<CLevel> getCLevel(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, String unitid) {
        String varName = "findingAid";
        if (FindingAid.class.equals(clazz)) {
            varName = "findingAid";
        }
        if (SourceGuide.class.equals(clazz)) {
            varName = "sourceGuide";
        } else if (HoldingsGuide.class.equals(clazz)) {
            varName = "holdingsGuide";
        }

        String jpaQuery = "SELECT clevel FROM CLevel clevel JOIN clevel.eadContent eadContent JOIN eadContent." + varName + " ead JOIN ead.archivalInstitution archivalInstitution WHERE clevel.unitid  = :unitid AND clevel.duplicateUnitid = false AND ead.eadid= :eadid AND ead.published = true AND archivalInstitution.repositorycode = :repoCode";
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("unitid", ApeUtil.decodeSpecialCharacters(unitid));
        query.setParameter("eadid", ApeUtil.decodeSpecialCharacters(eadid));
        query.setParameter("repoCode", ApeUtil.decodeRepositoryCode(repositoryCode));
        query.setMaxResults(2);
        return query.getResultList();
    }

    @Override
    public CLevel getCLevel(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, Long id) {
        String varName = "findingAid";
        if (FindingAid.class.equals(clazz)) {
            varName = "findingAid";
        }
        if (SourceGuide.class.equals(clazz)) {
            varName = "sourceGuide";
        } else if (HoldingsGuide.class.equals(clazz)) {
            varName = "holdingsGuide";
        }

        String jpaQuery = "SELECT clevel FROM CLevel clevel JOIN clevel.eadContent eadContent JOIN eadContent." + varName + " ead JOIN ead.archivalInstitution archivalInstitution WHERE clevel.id  = :id AND ead.eadid= :eadid AND ead.published = true AND archivalInstitution.repositorycode = :repoCode";
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("id", id);
        query.setParameter("eadid", ApeUtil.decodeSpecialCharacters(eadid));
        query.setParameter("repoCode", ApeUtil.decodeRepositoryCode(repositoryCode));
        query.setMaxResults(1);
        List<CLevel> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

//    long startTime = System.currentTimeMillis();
//        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
//        criteria.add(Restrictions.ge("clevel.orderId", orderId));
//        criteria.createAlias("clevel.ead3", "ead3");
//        criteria.add(Restrictions.eq("ead3.id", ead3Id));
//        criteria.setMaxResults(maxNumberOfItems);
//        criteria.addOrder(Order.asc("clevel.orderId"));
//        List<CLevel> results = criteria.list();
//        long endTime = System.currentTimeMillis();
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
//        }
//
//        return results;
    /*
    @Override
    public List<CLevel> getCLevelWithEad3Id(String repositoryCode, String ead3Id, String unitid) {
        
        LOG.debug("here: "+repositoryCode+" ead3Id:"+ead3Id+" unitid:"+unitid);
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");

        criteria.add(Restrictions.eq("unitid", unitid));
//        criteria.createAlias("clevel.ead3", "ead3");
        criteria.add(Restrictions.eq("ead3.identifier", ead3Id));
//        criteria.createAlias("clevel.ead3.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("ead3.archivalInstitution.repositorycode", repositoryCode));
        criteria.setMaxResults(2);
        criteria.addOrder(Order.asc("orderId")); //clevel.
        List<CLevel> results = criteria.list();
        return results;
    }
    //*/
    //*
    @Override
    public List<CLevel> getCLevelWithEad3Id(String repositoryCode, String ead3Id, String unitid) {
        
        LOG.debug("here: "+repositoryCode+" ead3Id:"+ead3Id+" unitid:"+unitid);
        String jpaQuery = "SELECT clevel FROM CLevel clevel JOIN clevel.ead3 ead3 JOIN ead3.archivalInstitution archivalInstitution WHERE clevel.unitid  = :unitid AND ead3.identifier= :identifier AND ead3.published = true AND archivalInstitution.repositorycode = :repoCode";
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("unitid", unitid);
        query.setParameter("identifier", ApeUtil.decodeSpecialCharacters(ead3Id));
        query.setParameter("repoCode", ApeUtil.decodeRepositoryCode(repositoryCode));
        query.setMaxResults(1);
//        List<CLevel> results = new ArrayList<>();
        List<CLevel> results = query.getResultList();
        
        return results;
    }
    //*/

    @Override
    public Long countCLevels(Class<? extends AbstractContent> clazz, Integer id) {
        String propertyName = "faId";
        if (clazz.equals(HoldingsGuide.class)) {
            propertyName = "hgId";
        } else if (clazz.equals(SourceGuide.class)) {
            propertyName = "sgId";
        }
        TypedQuery<Long> query = getEntityManager().createQuery(
                "SELECT COUNT(clevel) FROM CLevel clevel WHERE clevel.eadContent." + propertyName + " = :fileId", Long.class);
        query.setParameter("fileId", id);
        return query.getSingleResult();
    }

    @Override
    public List<CLevel> getCLevels(Class<? extends AbstractContent> clazz, Integer id, int pageNumber, int pageSize) {
        String propertyName = "faId";
        if (clazz.equals(HoldingsGuide.class)) {
            propertyName = "hgId";
        } else if (clazz.equals(SourceGuide.class)) {
            propertyName = "sgId";
        }
        TypedQuery<CLevel> query = getEntityManager().createQuery(
                "SELECT clevel FROM CLevel clevel WHERE clevel.eadContent." + propertyName + " = :fileId", CLevel.class);
        query.setParameter("fileId", id);
        query.setMaxResults(pageSize);
        query.setFirstResult(pageSize * (pageNumber - 1));
        return query.getResultList();
    }

    @Override
    public List<CLevel> getCLevels(int pageNumber, int pageSize) {
        TypedQuery<CLevel> query = getEntityManager().createQuery(
                "SELECT clevel FROM CLevel clevel", CLevel.class);
        query.setMaxResults(pageSize);
        query.setFirstResult(pageSize * (pageNumber - 1));
        return query.getResultList();
    }

    @Override
    public Long countAllCLevels(){
        TypedQuery<Long> query = getEntityManager().createQuery(
                "SELECT COUNT(clevel) FROM CLevel clevel", Long.class);
        return query.getSingleResult();
    }

    public List<CLevel> getTopClevelsByFileId(Integer fileId, Class<? extends AbstractContent> clazz, int firstResult, int maxResult) {
        String propertyName = "faId";
        if (clazz.equals(HoldingsGuide.class)) {
            propertyName = "hgId";
        } else if (clazz.equals(SourceGuide.class)) {
            propertyName = "sgId";
        }
        TypedQuery<CLevel> query = getEntityManager().createQuery(
                "SELECT clevel FROM CLevel clevel WHERE clevel.eadContent." + propertyName
                + " = :fileId AND clevel.parentId IS NULL ORDER BY clevel.orderId ASC", CLevel.class);
        query.setParameter("fileId", fileId);
        query.setMaxResults(maxResult);
        query.setFirstResult(firstResult);
        return query.getResultList();
    }

    public CLevel getTopClevelByFileId(Integer fileId, Class<? extends AbstractContent> clazz, int orderId) {
        String propertyName = "faId";
        if (clazz.equals(HoldingsGuide.class)) {
            propertyName = "hgId";
        } else if (clazz.equals(SourceGuide.class)) {
            propertyName = "sgId";
        }
        TypedQuery<CLevel> query = getEntityManager().createQuery(
                "SELECT clevel FROM CLevel clevel WHERE clevel.eadContent." + propertyName
                + " = :fileId AND clevel.parentId IS NULL AND clevel.orderId = :orderId", CLevel.class);
        query.setParameter("fileId", fileId);
        query.setParameter("orderId", orderId);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CLevel> findChilds(Long parentId) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = createChildCLevelsCriteria(parentId);
        criteria.addOrder(Order.asc("clevel.orderId"));
        List<CLevel> results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (LOG.isDebugEnabled()) {
            LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @Override
    public Long countChildCLevels(Long parentCLevelId) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = createChildCLevelsCriteria(parentCLevelId);
        criteria.setProjection(Projections.countDistinct("clevel.id"));
        Object object = criteria.list().get(0);

        long endTime = System.currentTimeMillis();
        if (LOG.isDebugEnabled()) {
            LOG.debug("query took " + (endTime - startTime) + " ms to countChildCLevels");
        }
        if (object instanceof Integer) {
            return ((Integer) object).longValue();
        } else if (object instanceof Long) {
            return (Long) object;
        }
        return (Long) new Long(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CLevel> findChildCLevels(Long parentCLevelId, Integer orderId, Integer maxNumberOfItems) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = createChildCLevelsCriteria(parentCLevelId);
        criteria.add(Restrictions.ge("clevel.orderId", orderId));
        criteria.setMaxResults(maxNumberOfItems);
        criteria.addOrder(Order.asc("clevel.orderId"));
        List<CLevel> results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (LOG.isDebugEnabled()) {
            LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @Override
    public Long countTopCLevels(Long eadContentId) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = createTopCLevelsCriteria(eadContentId);
        criteria.setProjection(Projections.countDistinct("clevel.id"));
        Object object = criteria.list().get(0);

        long endTime = System.currentTimeMillis();
        if (LOG.isDebugEnabled()) {
            LOG.debug("query took " + (endTime - startTime) + " ms to countTopClevels");
        }
        if (object instanceof Integer) {
            return ((Integer) object).longValue();
        } else if (object instanceof Long) {
            return (Long) object;
        }
        return (Long) new Long(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CLevel> findTopCLevels(Long eadContentId, Integer orderId, Integer maxNumberOfItems) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = createTopCLevelsCriteria(eadContentId);
        criteria.add(Restrictions.ge("clevel.orderId", orderId));
        criteria.setMaxResults(maxNumberOfItems);
        criteria.addOrder(Order.asc("clevel.orderId"));
        List<CLevel> results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (LOG.isDebugEnabled()) {
            LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CLevel> findTopCLevels(Long eadContentId) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = createTopCLevelsCriteria(eadContentId);
        criteria.addOrder(Order.asc("clevel.orderId"));
        List<CLevel> results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (LOG.isDebugEnabled()) {
            LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    public List<String> findChildrenLevels(Long parentId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel")
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("level"))))
                .add(Restrictions.eq("clevel.parentId", parentId));
        return criteria.list();
    }

    private Criteria createTopCLevelsCriteria(Long eadContentId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.isNull("clevel.parentId"));
        criteria.add(Restrictions.eq("clevel.ecId", eadContentId));

        return criteria;
    }

    private Criteria createChildCLevelsCriteria(Long parentId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("clevel.parentId", parentId));
        return criteria;
    }

    public Long getClIdByUnitid(String unitid, Long eadContentId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setProjection(
                Projections.distinct(Projections.projectionList().add(Projections.property("id"))));
        criteria.add(Restrictions.eq("clevel.ecId", eadContentId));
        criteria.add(Restrictions.eq("clevel.unitid", unitid));
        try {
            return (Long) criteria.uniqueResult();
        } catch (Exception e) {
            LOG.error("Error with unitid " + unitid + ", there are more than one, we return the first one");
            return (Long) criteria.list().get(0);
        }
    }

    @Override
    public Long countPossibleLinkedCLevels(Integer id, Class<? extends AbstractContent> clazz) {
        String jpaQuery = "SELECT count(clevel)" + buildPossibleLinkedCLevels(id, clazz);
        TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    private String buildPossibleLinkedCLevels(Integer id, Class<? extends AbstractContent> clazz) {
        String varName = "hgId";
        if (SourceGuide.class.equals(clazz)) {
            varName = "sgId";
        }
        return " FROM CLevel clevel JOIN clevel.eadContent eadContent WHERE eadContent." + varName
                + " = :id AND clevel.hrefEadid IS NOT NULL";
    }

    @Override
    public List<CLevel> getNotLinkedCLevels(Integer id, Class<? extends AbstractContent> clazz) {
        String jpaQuery = "SELECT clevel" + buildNotLinkedCLevelsFromQuery(id, clazz);
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    private String buildNotLinkedCLevelsFromQuery(Integer id, Class<? extends AbstractContent> clazz) {
        String varName = "hgId";
        if (SourceGuide.class.equals(clazz)) {
            varName = "sgId";
        }
        return " FROM CLevel clevel JOIN clevel.eadContent eadContent WHERE eadContent."
                + varName
                + " = :id AND clevel.hrefEadid IS NOT NULL AND clevel.id NOT IN (SELECT hgSgFaRelation.hgSgClevelId FROM HgSgFaRelation hgSgFaRelation WHERE hgSgFaRelation."
                + varName + " = :id)";
    }

    @Override
    public List<CLevel> getClevelsFromSgOrHg(Integer aiId, String eadid) {
        String jpaQuery = "SELECT clevel FROM CLevel clevel JOIN clevel.eadContent eadContent WHERE clevel.hrefEadid = :eadid AND "
                + "clevel.id NOT IN (SELECT hgSgFaRelation.hgSgClevelId FROM HgSgFaRelation hgSgFaRelation WHERE hgSgFaRelation.hgSgClevelId =  clevel.id) AND"
                + "(eadContent.hgId IN "
                + "(SELECT holdingsGuide.id FROM HoldingsGuide holdingsGuide WHERE holdingsGuide.aiId = :aiId)"
                + "OR eadContent.sgId IN "
                + "(SELECT sourceGuide.id FROM SourceGuide sourceGuide WHERE sourceGuide.aiId = :aiId)" + ")";
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("aiId", aiId);
        query.setParameter("eadid", eadid);
        return query.getResultList();
    }

    @Override
    public List<CLevel> getCLevelsNodes(Long eadContentId) {
        String jpaQuery = "SELECT clevel FROM CLevel clevel WHERE clevel.leaf = false AND clevel.ecId = :eadContentId  ORDER BY clevel.unittitle";
        TypedQuery<CLevel> query = getEntityManager().createQuery(jpaQuery, CLevel.class);
        query.setParameter("eadContentId", eadContentId);
        return query.getResultList();
    }

    @Override
    public List<CLevel> findTopEad3CLevels(Integer ead3Id, Integer orderId, Integer maxNumberOfItems) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "clevel");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.isNull("clevel.parentId"));
        criteria.add(Restrictions.ge("clevel.orderId", orderId));
        criteria.createAlias("clevel.ead3", "ead3");
        criteria.add(Restrictions.eq("ead3.id", ead3Id));
        criteria.setMaxResults(maxNumberOfItems);
        criteria.addOrder(Order.asc("clevel.orderId"));
        List<CLevel> results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (LOG.isDebugEnabled()) {
            LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @Override
    public CLevel getCLevelByUnitId(String unitId, String ead3Id) {
        return getCLevelByUnitId(unitId, null, ead3Id);
    }

}
