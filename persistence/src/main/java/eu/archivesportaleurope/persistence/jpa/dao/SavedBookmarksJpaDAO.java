package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.SavedBookmarksDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.CollectionContent;
import eu.apenet.persistence.vo.SavedBookmarks;

/***
 * Class SavedBookmarksJpaDAO
 *
 */
public class SavedBookmarksJpaDAO extends AbstractHibernateDAO<SavedBookmarks, Long> implements SavedBookmarksDAO {
	private final Logger log = Logger.getLogger(getClass());
	
	/***
	 * This function counts the saved bookmarks the user has
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return query.getSingleResult() {@link Long} the number of saved boomarks the user has, null if error in the transaction
	 */ 
	@Override
	public Long countSavedBookmarks(Long liferayUserId) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"countSavedBookmarks\"");
		
		try {
			TypedQuery<Long> query = getEntityManager()
					.createQuery(
							"SELECT COUNT (savedBookmarks) FROM SavedBookmarks savedBookmarks WHERE savedBookmarks.liferayUserId = :liferayUserId",
							Long.class);
			query.setParameter("liferayUserId", liferayUserId);
			if (log.isDebugEnabled()) 
				log.debug("Exit \"countSavedBookmarks\"");
			
			return query.getSingleResult();
		} catch (Exception e) {
			if (log.isDebugEnabled()) 
				log.debug("Exit \"countSavedBookmarks\" with error: " + e.toString());
			
			return null;
		}
	}

	@Override
	public Long countSavedBookmarksByModxUserId(Long modxUserId) {
		if (log.isDebugEnabled())
			log.debug("Enter \"countSavedBookmarks\"");

		try {
			TypedQuery<Long> query = getEntityManager()
					.createQuery(
							"SELECT COUNT (savedBookmarks) FROM SavedBookmarks savedBookmarks WHERE savedBookmarks.modxUserId = :modxUserId",
							Long.class);
			query.setParameter("modxUserId", modxUserId);
			if (log.isDebugEnabled())
				log.debug("Exit \"countSavedBookmarks\"");

			return query.getSingleResult();
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Exit \"countSavedBookmarks\" with error: " + e.toString());

			return null;
		}
	}

	/***
	 * This method gets a number of results for a paginated list with the saved bookmarks the user has.
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pagesize {@link int} the number of results results per page
	 * 
	 * @return List {@link SavedBookmarks} a list of the user's saved bookmarks
	 */
	@Override
	public List<SavedBookmarks> getSavedBookmarks(Long liferayUserId, int pageNumber, int pagesize) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getSavedBookmarks\"");
		
		try {
			TypedQuery<SavedBookmarks> query = getEntityManager().createQuery(
					"SELECT savedBookmarks FROM SavedBookmarks savedBookmarks  WHERE savedBookmarks.liferayUserId = :liferayUserId ORDER BY savedBookmarks.modifiedDate DESC ", SavedBookmarks.class);
			query.setParameter("liferayUserId", liferayUserId);
			query.setMaxResults(pagesize);
			query.setFirstResult(pagesize * (pageNumber - 1));
			if (log.isDebugEnabled()) 
				log.debug("Exit \"getSavedBookmarks\"");
			
			return query.getResultList();
		} catch (Exception e) {
			if (log.isDebugEnabled()) 
				log.debug("Exit \"getSavedBookmarks\" with error: " + e.toString());
			
			return null;
		}
	}

	@Override
	public List<SavedBookmarks> getSavedBookmarksByModxUserId(Long modxUserId, int pageNumber, int pagesize) {
		if (log.isDebugEnabled())
			log.debug("Enter \"getSavedBookmarks\"");

		try {
			TypedQuery<SavedBookmarks> query = getEntityManager().createQuery(
					"SELECT savedBookmarks FROM SavedBookmarks savedBookmarks  WHERE savedBookmarks.modxUserId = :modxUserId ORDER BY savedBookmarks.modifiedDate DESC ", SavedBookmarks.class);
			query.setParameter("modxUserId", modxUserId);
			query.setMaxResults(pagesize);
			query.setFirstResult(pagesize * (pageNumber - 1));
			if (log.isDebugEnabled())
				log.debug("Exit \"getSavedBookmarks\"");

			return query.getResultList();
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Exit \"getSavedBookmarks\" with error: " + e.toString());

			return null;
		}
	}

	/***
	 * this method gets a single saved bookmark with a user id and an bookmark id
	 * 
	 * @param liferayUserId {@link Long} current user id
	 * @param id {@link long} bookmark id
	 * 
	 * @return bookmark {@link SavedBookmarks} a single saved bookmark if is correct, null if not
	 */
	@Override
	public SavedBookmarks getSavedBookmark(Long liferayUserId, long id) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getSavedBookmark\"");
		
		SavedBookmarks bookmark =  findById(id);
		if (bookmark != null){
			if (liferayUserId != null && liferayUserId == bookmark.getLiferayUserId()){
				if (log.isDebugEnabled()) 
					log.debug("Exit \"getSavedBookmark\"");
				
				return bookmark;
			}
		}
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getSavedBookmark\" with null result");
		
		return null;
	}

	@Override
	public SavedBookmarks getSavedBookmarkByModxUserId(Long modxUserId, long id) {
		if (log.isDebugEnabled())
			log.debug("Enter \"getSavedBookmark\"");

		SavedBookmarks bookmark =  findById(id);
		if (bookmark != null){
			if (modxUserId != null && modxUserId == bookmark.getModxUserId()){
				if (log.isDebugEnabled())
					log.debug("Exit \"getSavedBookmark\"");

				return bookmark;
			}
		}
		if (log.isDebugEnabled())
			log.debug("Exit \"getSavedBookmark\" with null result");

		return null;
	}

	/***
	 * This method gets a list of saved bookmarks that are not included into a selected collection
	 * 
	 * @param id {@link Long} collection id
	 * @param liferayUserId {@link long} current user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 * 
	 * @return searches List {@link SavedBookmarks} a list of saved bookmarks that are not included into a selected collection
	 */
	@Override
	public List<SavedBookmarks> getSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId, int pageNumber, int pageSize) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		List<SavedBookmarks> searches = null;
		Criteria criteria = getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(id,liferayUserId,pageNumber,pageSize);
		searches = criteria.list();
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		return searches;
	}

	@Override
	public List<SavedBookmarks> getSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(Long id, long modxUserId, int pageNumber, int pageSize) {
		if (log.isDebugEnabled())
			log.debug("Enter \"getSavedBookmarksOutOfCollectionByCollectionIdAndModxUser\"");

		List<SavedBookmarks> searches = null;
		Criteria criteria = getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(id,modxUserId,pageNumber,pageSize);
		searches = criteria.list();
		if (log.isDebugEnabled())
			log.debug("Exit \"getSavedBookmarksOutOfCollectiongetSavedBookmarksOutOfCollectionByCollectionIdAndModxUserByCollectionIdAndLiferayUser\"");

		return searches;
	}

	/***
	 * This method gets a criteria with the saved bookmarks that are not into a user's collection. </br>
	 * This method is used in getSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser method </br>
	 * and in countSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser method too
	 * 
	 * @param id {@link Long} collection id
	 * @param liferayUserId {@link long} current user id
	 * @param pageNumber {@link Integer} the current number of page in the pagination results
	 * @param pageSize {@link Integer} the number of results results per page
	 * 
	 * @return criteria {@link Criteria} that gets the list of user saved bookmarks that are not included into a collection
	 */
	private Criteria getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId, Integer pageNumber, Integer pageSize) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "savedBookmarks");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(liferayUserId>0){
			criteria.add(Restrictions.eq("savedBookmarks.liferayUserId", liferayUserId));
		}
		if(id!=null && id>0){
			DetachedCriteria collectionSubquery = DetachedCriteria.forClass(CollectionContent.class, "collectionContent");
			collectionSubquery.setProjection(Projections.property("collectionContent.savedBookmarks.id"));
			collectionSubquery.add(Restrictions.eq("collectionContent.collection.id",id));
			collectionSubquery.add(Restrictions.isNotNull("collectionContent.savedBookmarks.id"));
			criteria.add(Subqueries.propertyNotIn("savedBookmarks.id",collectionSubquery));
		}
		if(pageNumber!=null && pageNumber>0){
			criteria.setFirstResult((pageNumber-1)*pageSize);
		}
		if(pageSize!=null && pageSize>0){
			criteria.setMaxResults(pageSize);
		}
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		return criteria;
	}

	private Criteria getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(Long id, long modxUserId, Integer pageNumber, Integer pageSize) {
		if (log.isDebugEnabled())
			log.debug("Enter \"getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndModxUser\"");

		Criteria criteria = getSession().createCriteria(getPersistentClass(), "savedBookmarks");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(modxUserId>0){
			criteria.add(Restrictions.eq("savedBookmarks.modxUserId", modxUserId));
		}
		if(id!=null && id>0){
			DetachedCriteria collectionSubquery = DetachedCriteria.forClass(CollectionContent.class, "collectionContent");
			collectionSubquery.setProjection(Projections.property("collectionContent.savedBookmarks.id"));
			collectionSubquery.add(Restrictions.eq("collectionContent.collection.id",id));
			collectionSubquery.add(Restrictions.isNotNull("collectionContent.savedBookmarks.id"));
			criteria.add(Subqueries.propertyNotIn("savedBookmarks.id",collectionSubquery));
		}
		if(pageNumber!=null && pageNumber>0){
			criteria.setFirstResult((pageNumber-1)*pageSize);
		}
		if(pageSize!=null && pageSize>0){
			criteria.setMaxResults(pageSize);
		}
		if (log.isDebugEnabled())
			log.debug("Exit \"getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndModxUser\"");

		return criteria;
	}

	/***
	 * This method gets the number of saved bookmarks that are not included into a selected collection
	 * 
	 * @param id {@link Long} collection id
	 * @param liferayUserId {@link long} current user id
	 * 
	 * @return ({@link Long})criteria.uniqueResult() The number of saved bookmarks that are not included into a selected collection
	 */
	@Override
	public Long countSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"countSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		Criteria criteria = getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(id,liferayUserId,null,null);
		criteria.setProjection(Projections.rowCount());
		if (log.isDebugEnabled()) 
			log.debug("Exit \"countSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser\"");
		
		return (Long)criteria.uniqueResult();
	}

	@Override
	public Long countSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(Long id, long modxUserId) {
		if (log.isDebugEnabled())
			log.debug("Enter \"countSavedBookmarksOutOfCollectionByCollectionIdAndModxUser\"");

		Criteria criteria = getCriteriaSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(id,modxUserId,null,null);
		criteria.setProjection(Projections.rowCount());
		if (log.isDebugEnabled())
			log.debug("Exit \"countSavedBookmarksOutOfCollectionByCollectionIdAndModxUser\"");

		return (Long)criteria.uniqueResult();
	}

	/***
	 * This method gets a list with the saved bookmarks of the user
	 * 
	 * @param bookmarksOut List {@link Long} list of bookmarks
	 * @param liferayUserId {@link Long} current user id
	 * 
	 * @return searches List {@link SavedBookmarks} a list with the saved bookmarks of the user
	 * */
	@Override
	public List<SavedBookmarks> getSavedBookmarksByIdsAndUserid(List<Long> bookmarksOut, Long liferayUserId) {
		if (log.isDebugEnabled()) 
			log.debug("Enter \"getSavedBookmarksByIdsAndUserid\"");
		
		List<SavedBookmarks> searches = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "savedBookmarks");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(liferayUserId>0){
			criteria.add(Restrictions.eq("savedBookmarks.liferayUserId", liferayUserId));
			if(bookmarksOut!=null && bookmarksOut.size()>0){
				Disjunction restrinction = Restrictions.disjunction();
				for(long targetId:bookmarksOut){
					restrinction.add(Restrictions.eq("savedBookmarks.id",targetId));
				}
				criteria.add(restrinction);
			}
			searches = criteria.list();
		}
		if (log.isDebugEnabled()) 
			log.debug("Exit \"getSavedBookmarksByIdsAndUserid\"");
		
		return searches;
	}

	@Override
	public List<SavedBookmarks> getSavedBookmarksByIdsAndModxUserid(List<Long> bookmarksOut, Long modxUserId) {
		if (log.isDebugEnabled())
			log.debug("Enter \"getSavedBookmarksByIdsAndModxUserid\"");

		List<SavedBookmarks> searches = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "savedBookmarks");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(modxUserId>0){
			criteria.add(Restrictions.eq("savedBookmarks.modxUserId", modxUserId));
			if(bookmarksOut!=null && bookmarksOut.size()>0){
				Disjunction restrinction = Restrictions.disjunction();
				for(long targetId:bookmarksOut){
					restrinction.add(Restrictions.eq("savedBookmarks.id",targetId));
				}
				criteria.add(restrinction);
			}
			searches = criteria.list();
		}
		if (log.isDebugEnabled())
			log.debug("Exit \"getSavedBookmarksByIdsAndModxUserid\"");

		return searches;
	}

}
