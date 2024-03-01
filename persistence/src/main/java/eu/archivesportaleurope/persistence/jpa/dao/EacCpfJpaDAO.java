package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import eu.archivesportaleurope.util.ApeUtil;
import javax.persistence.Query;

public class EacCpfJpaDAO extends AbstractHibernateDAO<EacCpf, Integer> implements EacCpfDAO {

    private final Logger log = Logger.getLogger(EacCpfJpaDAO.class);

    @Override
    public EacCpf getFirstPublishedEacCpfByIdentifier(String identifier, boolean isPublished) {
        Criteria criteria = getSession().createCriteria(EacCpf.class, "eacCpf");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("identifier", ApeUtil.decodeSpecialCharacters(identifier)));
        if (isPublished) {
            criteria.add(Restrictions.eq("published", true));
        }
        criteria.setMaxResults(1);
        List<EacCpf> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public int deleteById(Integer id) {
        JpaUtil.beginDatabaseTransaction();
        Query query = getEntityManager().createQuery(
                "DELETE  FROM EacCpf eacCpf WHERE eacCpf.id = :id");
        query.setParameter("id", id);
        int res = query.executeUpdate();
        JpaUtil.commitDatabaseTransaction();
        return res;
    }

    @Override
    public EacCpf getEacCpfByIdentifier(Integer aiId, String identifier) {
        TypedQuery<EacCpf> query = getEntityManager().createQuery(
                "SELECT eacCpf FROM EacCpf eacCpf WHERE eacCpf.aiId = :aiId AND eacCpf.identifier  = :identifier ", EacCpf.class);
        query.setParameter("identifier", identifier);
        query.setParameter("aiId", aiId);
        query.setMaxResults(1);
        List<EacCpf> list = query.getResultList();
        if (list.size() > 0) {
            EacCpf eacCpf  = list.get(0);
            eacCpf.setAiId(aiId); //No idea why this remain null in some case
            return eacCpf;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EacCpf getEacCpfByIdentifier(String repositorycode, String identifier, boolean onlyPublished) {
        Criteria criteria = getSession().createCriteria(EacCpf.class, "eacCpf");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("eacCpf.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.repositorycode", repositorycode));
        criteria.add(Restrictions.eq("identifier", identifier));
        if (onlyPublished) {
            criteria.add(Restrictions.eq("published", true));
        }
        criteria.setMaxResults(1);
        List<EacCpf> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<EacCpf> getEacCpfs(ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EacCpf> cq = criteriaBuilder.createQuery(EacCpf.class);
        Root<EacCpf> from = cq.from(EacCpf.class);
        cq.where(buildWhere(from, cq, contentSearchOptions));
        cq.select(from);
        /*
         * add ordering
         */
        if (contentSearchOptions.isOrderByAscending()) {
            cq.orderBy(criteriaBuilder.asc(from.get(contentSearchOptions.getOrderByField())));
        } else {
            cq.orderBy(criteriaBuilder.desc(from.get(contentSearchOptions.getOrderByField())));
        }

        /*
         * add pagination
         */
        TypedQuery<EacCpf> query = getEntityManager().createQuery(cq);
        if (contentSearchOptions.getPageSize() > 0) {
            query.setMaxResults(contentSearchOptions.getPageSize());
            if (contentSearchOptions.getFirstResult() >= 0) {
                query.setFirstResult(((Long) contentSearchOptions.getFirstResult()).intValue());
            } else {
                query.setFirstResult(contentSearchOptions.getPageSize() * (contentSearchOptions.getPageNumber() - 1));
            }
        }
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public long countEacCpfs(ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<EacCpf> from = cq.from(EacCpf.class);
        cq.where(buildWhere(from, cq, contentSearchOptions));
        cq.select(criteriaBuilder.countDistinct(from));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean existEacCpfs(ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EacCpf> cq = criteriaBuilder.createQuery(EacCpf.class);
        Root<EacCpf> from = cq.from(EacCpf.class);
        cq.where(buildWhere(from, cq, contentSearchOptions));
        cq.select(from);

        TypedQuery<EacCpf> query = getEntityManager().createQuery(cq);
        query.setMaxResults(1);
        return query.getResultList().size() > 0;
    }

    private Predicate buildWhere(Root<? extends EacCpf> from, CriteriaQuery<?> cq, ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> whereClause = new ArrayList<Predicate>();
        if (contentSearchOptions.getIds() != null && contentSearchOptions.getIds().size() > 0) {
            List<Predicate> idPredicated = new ArrayList<Predicate>();
            for (Integer id : contentSearchOptions.getIds()) {
                idPredicated.add(criteriaBuilder.equal(from.get("id"), id));
            }
            whereClause.add(criteriaBuilder.or(idPredicated.toArray(new Predicate[0])));
        }
        if (contentSearchOptions.getArchivalInstitionId() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("aiId"), contentSearchOptions.getArchivalInstitionId()));
        }
        if (contentSearchOptions.getPublished() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("published"), contentSearchOptions.getPublished()));
        }
        if (contentSearchOptions.getConverted() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("converted"), contentSearchOptions.getConverted()));
        }
        if (StringUtils.isNotBlank(contentSearchOptions.getEadid())) {
            whereClause.add(criteriaBuilder.equal(from.get("identifier"), contentSearchOptions.getEadid()));
        }
        if (contentSearchOptions.getValidated().size() > 0) {
            List<Predicate> validatedPredicated = new ArrayList<Predicate>();
            for (ValidatedState validateState : contentSearchOptions.getValidated()) {
                validatedPredicated.add(criteriaBuilder.equal(from.get("validated"), validateState));
            }
            whereClause.add(criteriaBuilder.or(validatedPredicated.toArray(new Predicate[0])));
        }
        if (contentSearchOptions.getEuropeana().size() > 0) {
            List<Predicate> europeanaPredicated = new ArrayList<Predicate>();
            for (EuropeanaState europeanaState : contentSearchOptions.getEuropeana()) {
                europeanaPredicated.add(criteriaBuilder.equal(from.get("europeana"), europeanaState));
            }
            whereClause.add(criteriaBuilder.or(europeanaPredicated.toArray(new Predicate[0])));

        }
        if (contentSearchOptions.getQueuing().size() > 0) {
            List<Predicate> queuingPredicated = new ArrayList<Predicate>();
            for (QueuingState queuingState : contentSearchOptions.getQueuing()) {
                queuingPredicated.add(criteriaBuilder.equal(from.get("queuing"), queuingState));
            }
            whereClause.add(criteriaBuilder.or(queuingPredicated.toArray(new Predicate[0])));
        }
        if (contentSearchOptions.getPublishedToAll() != null) {
            List<Predicate> orPredicated = new ArrayList<Predicate>();
            if (contentSearchOptions.getPublishedToAll()) {
                orPredicated.add(criteriaBuilder.equal(from.get("published"), true));
                orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.CONVERTED));
                orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.DELIVERED));
            } else {
                orPredicated.add(criteriaBuilder.equal(from.get("published"), false));
                orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.NOT_CONVERTED));
            }
            whereClause.add(criteriaBuilder.or(orPredicated.toArray(new Predicate[0])));
        }
        if (StringUtils.isNotBlank(contentSearchOptions.getSearchTerms())) {

            String[] searchTerms = StringUtils.split(contentSearchOptions.getSearchTerms(), " ");
            if ("identifier".equals(contentSearchOptions.getSearchTermsField())) {
                String searchTerm = contentSearchOptions.getSearchTerms().trim();
                searchTerm = searchTerm.replaceAll("\\*", "%");
                whereClause.add(criteriaBuilder.like(from.<String>get("identifier"), searchTerm));
            } else if ("title".equals(contentSearchOptions.getSearchTermsField())) {
                for (String searchTerm : searchTerms) {
                    whereClause.add(criteriaBuilder.like(from.<String>get("title"), "%" + searchTerm + "%"));
                }
            } else {
                String searchTermId = contentSearchOptions.getSearchTerms().trim();
                searchTermId = searchTermId.replaceAll("\\*", "%");
                Predicate identifierPredicate = criteriaBuilder.like(from.<String>get("identifier"), searchTermId);
                List<Predicate> titleAndPredicated = new ArrayList<Predicate>();
                for (String searchTerm : searchTerms) {
                    titleAndPredicated.add(criteriaBuilder.like(from.<String>get("title"), "%" + searchTerm + "%"));
                }
                whereClause.add(criteriaBuilder.or(criteriaBuilder.and(titleAndPredicated.toArray(new Predicate[0])), identifierPredicate));
            }
        }

        //for metacontent
        if (contentSearchOptions.getHasMetaContent() > 0 ) {
            if (contentSearchOptions.getHasMetaContent() == 1) {
                whereClause.add(criteriaBuilder.isNotNull(from.get("metaContent")));
            }
            else {
                whereClause.add(criteriaBuilder.isNull(from.get("metaContent")));
            }
        }

        return criteriaBuilder.and(whereClause.toArray(new Predicate[0]));
    }

    @Override
    public Integer isEacCpfIdUsed(String identifier, Integer aiId, Class<? extends EacCpf> clazz) {
        Criteria criteria = getSession().createCriteria(clazz, "eac").setProjection(Projections.property("id"));
        criteria.createAlias("eac.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.add(Restrictions.eq("identifier", identifier));
        List<Integer> result = criteria.list();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EacCpf getEacCpfById(Integer aiId, String identifier) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "eac");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("eac.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.add(Restrictions.eq("identifier", identifier));
        List<EacCpf> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Integer> getAllIds(Class<EacCpf> clazz, int aiId) {
        Criteria criteria = getSession().createCriteria(clazz, "eac");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("eac.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.setProjection(Projections.property("id"));
        return criteria.list();
    }

    private Criteria getEacCpfCriteriaByArchivalInstitution(int aiId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "eac");
        criteria = criteria.createAlias("eac.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        return criteria;
    }

    @Override
    public Integer isEacCpfIdIndexed(String identifier, Class<? extends EacCpf> clazz) {
        Criteria criteria = getSession().createCriteria(clazz, "eac").setProjection(Projections.property("id"));
        criteria.createAlias("eac.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("identifier", ApeUtil.decodeSpecialCharacters(identifier)));
        criteria.add(Restrictions.eq("published", true));
        criteria.setMaxResults(1);
        List<Integer> result = criteria.list();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

}
