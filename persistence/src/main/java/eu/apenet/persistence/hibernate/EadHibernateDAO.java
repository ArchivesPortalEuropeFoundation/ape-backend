package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.util.ApeUtil;

public class EadHibernateDAO extends AbstractHibernateDAO<Ead, Integer> implements EadDAO {

    private static final Logger LOG = Logger.getLogger(EadHibernateDAO.class);

    @Override
    public Integer isEadidIndexed(String eadid, Integer aiId, Class<? extends AbstractContent> clazz) {
        Criteria criteria = getSession().createCriteria(clazz, "ead").setProjection(Projections.property("id"));
        criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.add(Restrictions.eq("eadid", ApeUtil.decodeSpecialCharacters(eadid)));
        criteria.add(Restrictions.eq("published", true));
        criteria.setMaxResults(1);
        List<Integer> result = criteria.list();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<Ead> getEads(ContentSearchOptions eadSearchOptions) {
        if (eadSearchOptions.getContentClass()==null) {
            eadSearchOptions.setContentClass(FindingAid.class);
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Ead> cq = criteriaBuilder.createQuery(Ead.class);
        Root<? extends Ead> from = (Root<? extends Ead>) cq.from(eadSearchOptions.getContentClass());
        cq.where(buildWhere(from, cq, eadSearchOptions));
        cq.select(from);
        /*
		 * add ordering
         */
        if (eadSearchOptions.isOrderByAscending()) {
            cq.orderBy(criteriaBuilder.asc(from.get(eadSearchOptions.getOrderByField())));
        } else {
            cq.orderBy(criteriaBuilder.desc(from.get(eadSearchOptions.getOrderByField())));
        }

        /*
		 * add pagination
         */
        TypedQuery<Ead> query = getEntityManager().createQuery(cq);
        if (eadSearchOptions.getPageSize() > 0) {
            query.setMaxResults(eadSearchOptions.getPageSize());
            if (eadSearchOptions.getFirstResult() >= 0) {
                query.setFirstResult(((Long) eadSearchOptions.getFirstResult()).intValue());
            } else {
                query.setFirstResult(eadSearchOptions.getPageSize() * (eadSearchOptions.getPageNumber() - 1));
            }
        }
        return query.getResultList();
    }

    @Override
    public boolean existEads(ContentSearchOptions eadSearchOptions) {
        if (eadSearchOptions.getContentClass()==null) {
            eadSearchOptions.setContentClass(FindingAid.class);
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Ead> cq = criteriaBuilder.createQuery(Ead.class);
        Root<? extends Ead> from = (Root<? extends Ead>) cq.from(eadSearchOptions.getContentClass());
        cq.where(buildWhere(from, cq, eadSearchOptions));
        cq.select(from);

        TypedQuery<Ead> query = getEntityManager().createQuery(cq);
        query.setMaxResults(1);
        return query.getResultList().size() > 0;
    }

    @Override
    public Long countEads(ContentSearchOptions eadSearchOptions) {
        if (eadSearchOptions.getContentClass()==null) {
            eadSearchOptions.setContentClass(FindingAid.class);
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<? extends Ead> from = (Root<? extends Ead>) cq.from(eadSearchOptions.getContentClass());
        cq.where(buildWhere(from, cq, eadSearchOptions));
        cq.select(criteriaBuilder.countDistinct(from));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @Override
    public Long countUnits(ContentSearchOptions eadSearchOptions) {
        if (eadSearchOptions.getContentClass()==null) {
            eadSearchOptions.setContentClass(FindingAid.class);
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<? extends Ead> from = (Root<? extends Ead>) cq.from(eadSearchOptions.getContentClass());
        cq.where(criteriaBuilder.and(buildWhere(from, cq, eadSearchOptions),
                criteriaBuilder.greaterThan(from.<Integer>get("totalNumberOfUnits"), 0)));
        cq.select(criteriaBuilder.sum(from.<Long>get("totalNumberOfUnits")));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @Override
    public Long countWebResources(ContentSearchOptions eadSearchOptions) {
        if (eadSearchOptions.getContentClass()==null) {
            eadSearchOptions.setContentClass(FindingAid.class);
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<? extends Ead> from = (Root<? extends Ead>) cq.from(eadSearchOptions.getContentClass());
        cq.where(criteriaBuilder.and(buildWhere(from, cq, eadSearchOptions),
                criteriaBuilder.greaterThan(from.<Integer>get("totalNumberOfWebResourceEdm"), 0)));
        cq.select(criteriaBuilder.sum(from.<Long>get("totalNumberOfWebResourceEdm")));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @Override
    public Long countDaos(ContentSearchOptions eadSearchOptions) {
        if (eadSearchOptions.getContentClass()==null) {
            eadSearchOptions.setContentClass(FindingAid.class);
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<? extends Ead> from = (Root<? extends Ead>) cq.from(eadSearchOptions.getContentClass());
        cq.where(criteriaBuilder.and(buildWhere(from, cq, eadSearchOptions),
                criteriaBuilder.greaterThan(from.<Integer>get("totalNumberOfDaos"), 0)));
        cq.select(criteriaBuilder.sum(from.<Long>get("totalNumberOfDaos")));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @Override
    public Long countChos(ContentSearchOptions eadSearchOptions) {
        if (eadSearchOptions.getContentClass()==null) {
            eadSearchOptions.setContentClass(FindingAid.class);
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<? extends Ead> from = (Root<? extends Ead>) cq.from(eadSearchOptions.getContentClass());
        cq.where(criteriaBuilder.and(buildWhere(from, cq, eadSearchOptions),
                criteriaBuilder.greaterThan(from.<Integer>get("totalNumberOfChos"), 0)));
        cq.select(criteriaBuilder.sum(from.<Long>get("totalNumberOfChos")));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    private Predicate buildWhere(Root<? extends Ead> from, CriteriaQuery<?> cq, ContentSearchOptions eadSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> whereClause = new ArrayList<Predicate>();
        if (eadSearchOptions.getIds() != null && eadSearchOptions.getIds().size() > 0) {
            List<Predicate> idPredicated = new ArrayList<Predicate>();
            for (Integer id : eadSearchOptions.getIds()) {
                idPredicated.add(criteriaBuilder.equal(from.get("id"), id));
            }
            whereClause.add(criteriaBuilder.or(idPredicated.toArray(new Predicate[0])));
        }
        if (eadSearchOptions.getArchivalInstitionId() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("aiId"), eadSearchOptions.getArchivalInstitionId()));
        }
        if (eadSearchOptions.getPublished() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("published"), eadSearchOptions.getPublished()));
        }
        if (eadSearchOptions.getConverted() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("converted"), eadSearchOptions.getConverted()));
        }
        if (eadSearchOptions.getDynamic() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("dynamic"), eadSearchOptions.getDynamic()));
        }
        if (StringUtils.isNotBlank(eadSearchOptions.getEadid())) {
            whereClause.add(criteriaBuilder.equal(from.get("eadid"), ApeUtil.decodeSpecialCharacters(eadSearchOptions.getEadid())));
        }
        if (eadSearchOptions.getValidated().size() > 0) {
            List<Predicate> validatedPredicated = new ArrayList<Predicate>();
            for (ValidatedState validateState : eadSearchOptions.getValidated()) {
                validatedPredicated.add(criteriaBuilder.equal(from.get("validated"), validateState));
            }
            whereClause.add(criteriaBuilder.or(validatedPredicated.toArray(new Predicate[0])));
        }
        /*
		 * only if findingaid
         */
        if (FindingAid.class.equals(eadSearchOptions.getContentClass())) {
            if (eadSearchOptions.getEuropeana().size() > 0) {
                List<Predicate> europeanaPredicated = new ArrayList<Predicate>();
                for (EuropeanaState europeanaState : eadSearchOptions.getEuropeana()) {
                    europeanaPredicated.add(criteriaBuilder.equal(from.get("europeana"), europeanaState));
                }
                whereClause.add(criteriaBuilder.or(europeanaPredicated.toArray(new Predicate[0])));

            }
            if (eadSearchOptions.getLinked() != null) {
                Subquery<Long> subquery = cq.subquery(Long.class);
                Root<HgSgFaRelation> fromHgSgFaRelation = subquery.from(HgSgFaRelation.class);
                subquery.select(fromHgSgFaRelation.<Long>get("faId"));
                List<Predicate> subQueryWhereClause = new ArrayList<Predicate>();
                subQueryWhereClause.add(criteriaBuilder.equal(fromHgSgFaRelation.get("aiId"), eadSearchOptions.getArchivalInstitionId()));
                subQueryWhereClause.add(criteriaBuilder.equal(fromHgSgFaRelation.<Long>get("faId"), from.get("id")));
                if (eadSearchOptions.getLinkedId() != null) {
                    if (HoldingsGuide.class.equals(eadSearchOptions.getLinkedWithEadClass())) {
                        subQueryWhereClause.add(criteriaBuilder.equal(fromHgSgFaRelation.<Long>get("hgId"), eadSearchOptions.getLinkedId()));
                    } else if (SourceGuide.class.equals(eadSearchOptions.getLinkedWithEadClass())) {
                        subQueryWhereClause.add(criteriaBuilder.equal(fromHgSgFaRelation.<Long>get("sgId"), eadSearchOptions.getLinkedId()));
                    }
                }
                subquery.where(criteriaBuilder.and(subQueryWhereClause.toArray(new Predicate[0])));
                if (eadSearchOptions.getLinked()) {
                    whereClause.add(criteriaBuilder.exists(subquery));
                } else {
                    whereClause.add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));

                }
            }
        }
        if (eadSearchOptions.getQueuing().size() > 0) {
            List<Predicate> queuingPredicated = new ArrayList<Predicate>();
            for (QueuingState queuingState : eadSearchOptions.getQueuing()) {
                queuingPredicated.add(criteriaBuilder.equal(from.get("queuing"), queuingState));
            }
            whereClause.add(criteriaBuilder.or(queuingPredicated.toArray(new Predicate[0])));
        }
        if (eadSearchOptions.getPublishedToAll() != null) {
            List<Predicate> orPredicated = new ArrayList<Predicate>();
            if (eadSearchOptions.getPublishedToAll()) {
                orPredicated.add(criteriaBuilder.equal(from.get("published"), true));
                if (FindingAid.class.equals(eadSearchOptions.getContentClass())) {
                    orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.CONVERTED));
                    orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.DELIVERED));
                }
            } else {
                orPredicated.add(criteriaBuilder.equal(from.get("published"), false));
                if (FindingAid.class.equals(eadSearchOptions.getContentClass())) {
                    orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.NOT_CONVERTED));
                }
            }
            whereClause.add(criteriaBuilder.or(orPredicated.toArray(new Predicate[0])));
        }
        if (StringUtils.isNotBlank(eadSearchOptions.getSearchTerms())) {
            String[] searchTerms = StringUtils.split(eadSearchOptions.getSearchTerms(), " ");
            if ("eadid".equals(eadSearchOptions.getSearchTermsField())) {
                String searchTerm = eadSearchOptions.getSearchTerms().trim();
                searchTerm = searchTerm.replaceAll("\\*", "%");
                whereClause.add(criteriaBuilder.like(from.<String>get("eadid"), searchTerm));
            } else if ("title".equals(eadSearchOptions.getSearchTermsField())) {
                for (String searchTerm : searchTerms) {
                    whereClause.add(criteriaBuilder.like(from.<String>get("title"), "%" + searchTerm + "%"));
                }
            } else if ("path".equals(eadSearchOptions.getSearchTermsField())) {
                whereClause.add(criteriaBuilder.equal(from.<String>get("pathApenetead"), eadSearchOptions.getSearchTerms()));
            } else {
                String searchTermId = eadSearchOptions.getSearchTerms().trim();
                searchTermId = searchTermId.replaceAll("\\*", "%");
                Predicate identifierPredicate = criteriaBuilder.like(from.<String>get("eadid"), searchTermId);
                List<Predicate> titleAndPredicated = new ArrayList<Predicate>();
                for (String searchTerm : searchTerms) {
                    titleAndPredicated.add(criteriaBuilder.like(from.<String>get("title"), "%" + searchTerm + "%"));
                }
                whereClause.add(criteriaBuilder.or(criteriaBuilder.and(titleAndPredicated.toArray(new Predicate[0])), identifierPredicate));
            }
        }

        //for metacontent
        if (eadSearchOptions.getHasMetaContent() > 0 ) {
            if (eadSearchOptions.getHasMetaContent() == 1) {
                whereClause.add(criteriaBuilder.isNotNull(from.get("metaContent")));
            }
            else {
                whereClause.add(criteriaBuilder.isNull(from.get("metaContent")));
            }
        }

        return criteriaBuilder.and(whereClause.toArray(new Predicate[0]));
    }

    @Override
    public Integer isEadidUsed(String eadid, Integer aiId, Class<? extends AbstractContent> clazz) {
        Criteria criteria = getSession().createCriteria(clazz, "ead").setProjection(Projections.property("id"));
        criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.add(Restrictions.eq("eadid", ApeUtil.decodeSpecialCharacters(eadid)));
        List<Integer> result = criteria.list();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ead> getEadsByAiId(Class<? extends Ead> clazz, Integer aiId) {
        Criteria criteria = getSession().createCriteria(clazz, "ead");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));

        List<Ead> list = criteria.list();
        return list;
    }

    @Override
    public Ead getFirstPublishedEadByEadid(Class<? extends Ead> clazz, String eadid) {
        Criteria criteria = getSession().createCriteria(clazz, "ead");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("eadid", ApeUtil.decodeSpecialCharacters(eadid)));
        criteria.add(Restrictions.eq("published", true));
        List<Ead> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Ead getEadByEadid(Class<? extends AbstractContent> clazz, Integer aiId, String eadid) {
        Criteria criteria = getSession().createCriteria(clazz, "ead");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.add(Restrictions.eq("eadid", ApeUtil.decodeSpecialCharacters(eadid)));
        List<Ead> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Ead getEadByEadid(Class<? extends AbstractContent> clazz, String repositorycode, String eadid, boolean onlyPublished) {
        Criteria criteria = getSession().createCriteria(clazz, "ead");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.repositorycode", ApeUtil.decodeRepositoryCode(repositorycode)));
        criteria.add(Restrictions.eq("eadid", ApeUtil.decodeSpecialCharacters(eadid)));
        if (onlyPublished) {
            criteria.add(Restrictions.eq("published", true));
        }
        criteria.setMaxResults(1);
        List<Ead> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getAllIds(Class<? extends Ead> clazz, Integer aiId) {
        Criteria criteria = getSession().createCriteria(clazz, "ead");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.setProjection(Projections.property("id"));
        return criteria.list();
    }

    @SuppressWarnings({"unchecked"})
    public Long getTotalCountOfUnits() {

        Criteria criteria = null;
        List<Long> result = null;
        Long value = 0L;

        criteria = getSession().createCriteria(getPersistentClass(), "ead");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        result = criteria.setProjection(Projections.sum("totalNumberOfUnits")).list();
        if (result.size() > 0) {
            int i = 0;
            while (result.size() > i) {
                if (result.get(i) != null) {
                    value += result.get(i);
                }
                i++;
            }
        }
        return value;

    }

    @SuppressWarnings({"unchecked"})
    public Long getTotalCountOfUnitsWithDao() {

        Criteria criteria = null;
        List<Long> result = null;
        Long value = 0L;

        criteria = getSession().createCriteria(getPersistentClass(), "ead");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        result = criteria.setProjection(Projections.sum("totalNumberOfUnitsWithDao")).list();
        if (result.size() > 0) {
            int i = 0;
            while (result.size() > i) {
                if (result.get(i) != null) {
                    value += result.get(i);
                }
                i++;
            }
        }
        return value;
    }

}
