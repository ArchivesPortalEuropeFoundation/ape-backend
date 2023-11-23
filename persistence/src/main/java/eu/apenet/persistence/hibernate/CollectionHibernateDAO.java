package eu.apenet.persistence.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CollectionDAO;
import eu.apenet.persistence.vo.Collection;

/***
 * class CollectionHibernateDAO
 *
 */
public class CollectionHibernateDAO extends AbstractHibernateDAO<Collection, Long> implements CollectionDAO{
	private static final Logger LOGGER = Logger.getLogger(CollectionHibernateDAO.class);
	private static final String ORDER = "modified_date";
	
	/***
	 * Function that gets a list with the user collections by user id
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param pageNumber {@link Integer} the current number of page in the pagination results
	 * @param pageSize {@link Integer} the number of results results per page
	 * @param sortValue {@link String} By default order descendant by date (none), if it has values it is the number of the field to sort
	 * @param ascending {@link boolean} true for ascending or false for descending
	 * 
	 * @return criteria.list() if there is a valid user, null if not
	 */
	@Override
	public List<Collection> getCollectionsByUserId(Long liferayUserId,Integer pageNumber,Integer pageSize, String sortValue,
	boolean ascending) {
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Enter \"getCollectionsByUserId\"");
		
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(liferayUserId!=null && liferayUserId>0){
				criteria.add(Restrictions. eq("collection.liferayUserId", liferayUserId));
				
				if (sortValue != null && !sortValue.equalsIgnoreCase("none")) {
					if (ascending) {
						criteria.addOrder(Order.asc(sortValue));
					} else {
						criteria.addOrder(Order.desc(sortValue));
					}
				} else {
					// By default order descendant by date.
					criteria.addOrder(Order.desc("modified_date"));
				}
				
			}
			if(pageNumber!=null && pageNumber>0){
				criteria.setFirstResult((pageNumber-1)*pageSize);
			}
			if(pageSize!=null && pageSize>0){
				criteria.setMaxResults(pageSize);
			}
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exit \"getCollectionsByUserId\"");
			
			return criteria.list();
		}
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Exit \"getCollectionsByUserId\"");
		
		return null;
	}

	@Override
	public List<Collection> getCollectionsByModxUserId(Long modxUserId, Integer pageNumber, Integer pagesize, String field, boolean showDesc) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Enter \"getCollectionsByModxUserId\"");

		if(modxUserId!=null && modxUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(modxUserId!=null && modxUserId>0){
				criteria.add(Restrictions. eq("collection.modxUserId", modxUserId));

				if (field != null && !field.equalsIgnoreCase("none")) {
					if (showDesc) {
						criteria.addOrder(Order.asc(field));
					} else {
						criteria.addOrder(Order.desc(field));
					}
				} else {
					// By default order descendant by date.
					criteria.addOrder(Order.desc("modified_date"));
				}

			}
			if(pageNumber!=null && pageNumber>0){
				criteria.setFirstResult((pageNumber-1)*pagesize);
			}
			if(pagesize!=null && pagesize>0){
				criteria.setMaxResults(pagesize);
			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Exit \"getCollectionsByModxUserId\"");

			return criteria.list();
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exit \"getCollectionsByModxUserId\"");

		return null;
	}

	/***
	 * Function that gets a bookmarks or searches list by user id and element id
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param table {@link String} Bookmark or Search
	 * @param elemetId {@link String} the id of the bookmark or the search
	 * 
	 * @return criteria.list() if there is a valid user, null if not
	 */
	@Override
	public List<Collection> getCollectionsByIdAndUserId(Long liferayUserId, String table, String elemetId) {
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Enter \"getCollectionsByIdAndUserId\"");
		
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection"); //collection
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(liferayUserId!=null && liferayUserId>0){
				criteria.add(Restrictions. eq("collection.liferayUserId", liferayUserId));
				
				if (table.equals("Bookmark"))
					criteria.add(Restrictions.not(Restrictions.eq("collection.collectionContents.id_bookmarks.id", elemetId)));
				else
					criteria.add(Restrictions.not(Restrictions.eq("collection.collectionContents.id_search.id", elemetId)));

			}
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exit \"getCollectionsByIdAndUserId\"");
			
			return criteria.list();
		}
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Exit \"getCollectionsByIdAndUserId\"");
		
		return null;
	}

	@Override
	public List<Collection> getCollectionsByIdAndModxUserId(Long modxUserId, String table, String elemetId) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Enter \"getCollectionsByIdAndModxUserId\"");

		if(modxUserId!=null && modxUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection"); //collection
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(modxUserId!=null && modxUserId>0){
				criteria.add(Restrictions. eq("collection.modxUserId", modxUserId));

				if (table.equals("Bookmark"))
					criteria.add(Restrictions.not(Restrictions.eq("collection.collectionContents.id_bookmarks.id", elemetId)));
				else
					criteria.add(Restrictions.not(Restrictions.eq("collection.collectionContents.id_search.id", elemetId)));

			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Exit \"getCollectionsByIdAndModxUserId\"");

			return criteria.list();
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exit \"getCollectionsByIdAndModxUserId\"");

		return null;
	}

	/***
	 * Function counts the number of the collections by user
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return results {@link Long} number of collections, 0 if there is no results.
	 */
	@Override
	public Long countCollectionsByUserId(Long liferayUserId) {
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Enter \"countCollectionsByUserId\"");
		
		long results = 0;
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.setProjection(Projections.count("collection.id"));
			criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
			results = (Long)criteria.uniqueResult();
		}
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Exit \"countCollectionsByUserId\"");
		
		return results;
	}

	@Override
	public Long countCollectionsByModxUserId(Long modxUserId) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Enter \"countCollectionsByModxUserId\"");

		long results = 0;
		if(modxUserId!=null && modxUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.setProjection(Projections.count("collection.id"));
			criteria.add(Restrictions.eq("collection.modxUserId", modxUserId));
			results = (Long)criteria.uniqueResult();
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exit \"countCollectionsByModxUserId\"");

		return results;
	}

	/***
	 * Function gets a single collection by collection id and user id
	 * 
	 * @param id {@link Long} collection id.
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return collection {@link Collection} single collection.
	 */
	@Override
	public Collection getCollectionByIdAndUserId(Long id, Long liferayUserId) {
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Enter \"getCollectionByIdAndUserId\"");
		
		Collection collection = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(liferayUserId!=null && liferayUserId>0){
			criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
			if(id!=null && id>0){
				criteria.add(Restrictions.eq("collection.id", id));
				collection = (Collection)criteria.uniqueResult();
			}
		}
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Exit \"getCollectionByIdAndUserId\"");
		
		return collection;
	}

	@Override
	public Collection getCollectionByIdAndModxUserId(Long id, Long modxUserId) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Enter \"getCollectionByIdAndModxUserId\"");

		Collection collection = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(modxUserId!=null && modxUserId>0){
			criteria.add(Restrictions.eq("collection.modxUserId", modxUserId));
			if(id!=null && id>0){
				criteria.add(Restrictions.eq("collection.id", id));
				collection = (Collection)criteria.uniqueResult();
			}
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exit \"getCollectionByIdAndModxUserId\"");

		return collection;
	}

	/***
	 * Function gets a single collection by collection id
	 * 
	 * @param id {@link Long} collection id
	 * 
	 * @return collection{@link Collection} single collection
	 */
	@Override
	public Collection getCollectionById(Long id) {
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Enter \"getCollectionById\"");
		
		Collection collection = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(id!=null && id>0){
			criteria.add(Restrictions.eq("collection.id", id));
			collection = (Collection)criteria.uniqueResult();
		}
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Exit \"getCollectionById\"");
		
		return collection;
	}
		
	/***
	 * Function gets a list of collections by a pattern in the name
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param title {@link String} the pattern to search in the title of the collection
	 * @param pageSize {@link int} the number of results results per page
	 * 
	 * @return criteria.list() if there is a valid user, null if not
	 */
	@Override
	public List<Collection> getCollectionByName(Long liferayUserId, String title, int pageSize) {
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Enter \"getCollectionByName\"");
		
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(liferayUserId!=null && liferayUserId>0){
				criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
				criteria.addOrder(Order.desc("modified_date"));
				criteria.setMaxResults(pageSize);
				criteria.add(Restrictions.ilike("collection.title",title, MatchMode.ANYWHERE));
			}
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exit \"getCollectionByName\"");
			
			return criteria.list();
		}
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Exit \"getCollectionByName\" with no results");
		
		return null;
	}

	@Override
	public List<Collection> getCollectionByModxUserIdAndName(Long modxUserId, String name, int pageSize) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Enter \"getCollectionByModxUserIdAndName\"");

		if(modxUserId!=null && modxUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if(modxUserId!=null && modxUserId>0){
				criteria.add(Restrictions.eq("collection.modxUserId", modxUserId));
				criteria.addOrder(Order.desc("modified_date"));
				criteria.setMaxResults(pageSize);
				criteria.add(Restrictions.ilike("collection.title",name, MatchMode.ANYWHERE));
			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Exit \"getCollectionByModxUserIdAndName\"");

			return criteria.list();
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exit \"getCollectionByModxUserIdAndName\" with no results");

		return null;
	}

	/***
	 * Function returns a list of user collections that NOT contains the elements of the list
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param ids List {@link Long} the list of collection ids
	 * @param pageNumber {@link Integer} the current number of page in the pagination results
	 * @param pageSize {@link Integer} the number of results results per page
	 * 
	 * @return criteria.list() if there is a valid user, null if not
	 */
	public List<Collection> getUserCollectionsWithoutIds(Long liferayUserId, List<Long> ids, Integer pageNumber, Integer pageSize){
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Enter \"getUserCollectionsWithoutIds\"");
		
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
			criteria.addOrder(Order.desc("modified_date"));	
 			Iterator<Long> itcontents = ids.iterator();
			while(itcontents.hasNext()){
				Long collectionId = itcontents.next();
				criteria.add(Restrictions.not(Restrictions.eq("collection.id", collectionId)));
			}
			
			if(pageNumber!=null && pageNumber>0){
				criteria.setFirstResult((pageNumber-1)*pageSize);
			}
			if(pageSize!=null && pageSize>0){
				criteria.setMaxResults(pageSize);
			}
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exit \"getUserCollectionsWithoutIds\"");
			
			return criteria.list();
		}
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Exit \"getUserCollectionsWithoutIds\" with no results");
		
		return null;
	}

	@Override
	public List<Collection> getUserCollectionsForModxUserWithoutIds(Long modxUserId, List<Long> ids, Integer pageNumber, Integer pageSize) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Enter \"getUserCollectionsForModxUserWithoutIds\"");

		if(modxUserId!=null && modxUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collection.modxUserId", modxUserId));
			criteria.addOrder(Order.desc("modified_date"));
			Iterator<Long> itcontents = ids.iterator();
			while(itcontents.hasNext()){
				Long collectionId = itcontents.next();
				criteria.add(Restrictions.not(Restrictions.eq("collection.id", collectionId)));
			}

			if(pageNumber!=null && pageNumber>0){
				criteria.setFirstResult((pageNumber-1)*pageSize);
			}
			if(pageSize!=null && pageSize>0){
				criteria.setMaxResults(pageSize);
			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Exit \"getUserCollectionsForModxUserWithoutIds\"");

			return criteria.list();
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exit \"getUserCollectionsForModxUserWithoutIds\" with no results");

		return null;
	}

	/***
	 * Function returns the number of user collections that NOT contains the elements of the list
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param ids List {@link Long} the list of collection ids
	 * 
	 * @return ({@link Long})criteria.uniqueResult() if there is a valid user, null if not
	 */
	public Long countUserCollectionsWithoutIds(Long liferayUserId, List<Long> ids){
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Enter \"countUserCollectionsWithoutIds\"");
		
		if(liferayUserId!=null && liferayUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collection.liferayUserId", liferayUserId));
 			Iterator<Long> itcontents = ids.iterator();
			while(itcontents.hasNext()){
				Long collectionId = itcontents.next();
				criteria.add(Restrictions.not(Restrictions.eq("collection.id", collectionId)));
			}
			criteria.setProjection(Projections.rowCount());
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exit \"countUserCollectionsWithoutIds\"");
			
			return (Long)criteria.uniqueResult();
		}
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("Exit \"countUserCollectionsWithoutIds\" with no results");
		
		return null;
	}

	@Override
	public Long countUserCollectionsForModxUserWithoutIds(Long modxUserId, List<Long> ids) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Enter \"countUserCollectionsForModxUserWithoutIds\"");

		if(modxUserId!=null && modxUserId>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collection");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collection.modxUserId", modxUserId));
			Iterator<Long> itcontents = ids.iterator();
			while(itcontents.hasNext()){
				Long collectionId = itcontents.next();
				criteria.add(Restrictions.not(Restrictions.eq("collection.id", collectionId)));
			}
			criteria.setProjection(Projections.rowCount());
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Exit \"countUserCollectionsForModxUserWithoutIds\"");

			return (Long)criteria.uniqueResult();
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exit \"countUserCollectionsForModxUserWithoutIds\" with no results");

		return null;
	}
}

