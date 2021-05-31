/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.RightsInformationDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.RightsInformation;
import java.util.List;
import javax.persistence.TypedQuery;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author apef
 */
public class RightsInformationJpaDAO extends AbstractHibernateDAO<RightsInformation, Integer> implements RightsInformationDAO {

    @Override
    public List<RightsInformation> getRightsInformations() {
        String query = "SELECT rightsInformation FROM RightsInformation rightsInformation ORDER BY rightsInformation.id ASC";
        TypedQuery<RightsInformation> typedQuery = getEntityManager().createQuery(query, RightsInformation.class);
        return typedQuery.getResultList();
    }

    @Override
    public RightsInformation getRightsInformation(Integer rightsInformationId) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "rightsInformation");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("id", rightsInformationId));
        List<RightsInformation> list = criteria.list();
        if (list.size() > 0) {
            return (RightsInformation) list.get(0);
        }
        return null;
    }

    @Override
    public RightsInformation getRightsInformation(String rightsInformationAbbreviation) {
        String query = "SELECT rightsInformation FROM RightsInformation rightsInformation WHERE abbreviation = :rightsInformationAbbreviation";
        TypedQuery<RightsInformation> typedQuery = getEntityManager().createQuery(query, RightsInformation.class);
        typedQuery.setParameter("rightsInformationAbbreviation", rightsInformationAbbreviation);
        typedQuery.setMaxResults(1);
        return typedQuery.getSingleResult();
    }

}
