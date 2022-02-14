package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class ArchivalInstitutionOaiPmhJpaDAO extends AbstractHibernateDAO<ArchivalInstitutionOaiPmh, Long> implements ArchivalInstitutionOaiPmhDAO {
    @Override
    public List<ArchivalInstitutionOaiPmh> getArchivalInstitutionOaiPmhs(Integer aiId) {
        String query = "SELECT archivalInstitutionOaiPmh FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh WHERE archivalInstitutionOaiPmh.aiId  = :aiId ORDER BY archivalInstitutionOaiPmh.id";
        TypedQuery<ArchivalInstitutionOaiPmh> typedQuery = getEntityManager().createQuery(query, ArchivalInstitutionOaiPmh.class);
        typedQuery.setParameter("aiId", aiId);
        return typedQuery.getResultList();
    }
    

    @Override
	public List<ArchivalInstitutionOaiPmh> getArchivalInstitutionOaiPmhs() {
        String query = "SELECT archivalInstitutionOaiPmh FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh ORDER BY archivalInstitutionOaiPmh.id ASC";
        TypedQuery<ArchivalInstitutionOaiPmh> typedQuery = getEntityManager().createQuery(query, ArchivalInstitutionOaiPmh.class);
        return typedQuery.getResultList();
	}


	@Override
    public Long countEnabledItems() {
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT count(archivalInstitutionOaiPmh) FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh WHERE archivalInstitutionOaiPmh.enabled = 'TRUE'", Long.class);
        query.setMaxResults(1);
        return query.getResultList().get(0);
    }

    public List<ArchivalInstitutionOaiPmh> getFirstItems() {
        TypedQuery<ArchivalInstitutionOaiPmh> query = getEntityManager().createQuery("SELECT archivalInstitutionOaiPmh FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh "
        		+ "WHERE archivalInstitutionOaiPmh.enabled = true "
        		+ "ORDER BY archivalInstitutionOaiPmh.newHarvesting ASC", ArchivalInstitutionOaiPmh.class);
        query.setMaxResults(20);
        return query.getResultList();
    }

	@Override
	public  List<ArchivalInstitutionOaiPmh> getReadyItems() {
		Date currentDate = new Date();
        TypedQuery<ArchivalInstitutionOaiPmh> typedQuery = getEntityManager().createQuery("SELECT archivalInstitutionOaiPmh FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh "
        		+ "WHERE archivalInstitutionOaiPmh.enabled = true AND archivalInstitutionOaiPmh.newHarvesting < :currentDate "
        		+ "ORDER BY archivalInstitutionOaiPmh.newHarvesting ASC", ArchivalInstitutionOaiPmh.class);
        typedQuery.setParameter("currentDate", currentDate);
        return typedQuery.getResultList();
	}

	@Override
	public List<String> getSets(String url) {
        TypedQuery<ArchivalInstitutionOaiPmh> typedQuery = getEntityManager().createQuery("SELECT aoai FROM ArchivalInstitutionOaiPmh aoai "
        		+ "WHERE aoai.url  = :url ", ArchivalInstitutionOaiPmh.class);
        typedQuery.setParameter("url", url);
        List<ArchivalInstitutionOaiPmh> results = typedQuery.getResultList();
        List<String> response = new ArrayList<>();
        for (ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh : results){
            response.add(archivalInstitutionOaiPmh.getSet());
        }
        return response;//typedQuery.getResultList();

	}

}

