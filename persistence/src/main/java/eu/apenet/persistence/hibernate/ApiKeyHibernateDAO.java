/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.hibernate;

import eu.apenet.persistence.dao.ApiKeyDAO;
import eu.apenet.persistence.vo.ApiKey;
import eu.apenet.persistence.vo.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author kaisar
 */
public class ApiKeyHibernateDAO extends AbstractHibernateDAO<ApiKey, Integer> implements ApiKeyDAO {

    @Override
    public ApiKey findByLiferayUserId(long liferayUserId) {
        ApiKey apiKey = null;
        getSession().clear();
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "apiKey");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("liferayUserId", liferayUserId));
        criteria.add(Restrictions.like("status", BaseEntity.STATUS_CREATED, MatchMode.EXACT));
        apiKey = (ApiKey) criteria.uniqueResult();
        return apiKey;
    }

    @Override
    public ApiKey findByModxUserId(long modxUserId) {
        ApiKey apiKey = null;
        getSession().clear();
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "apiKey");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("modxUserId", modxUserId));
        criteria.add(Restrictions.like("status", BaseEntity.STATUS_CREATED, MatchMode.EXACT));
        apiKey = (ApiKey) criteria.uniqueResult();
        return apiKey;
    }
}
