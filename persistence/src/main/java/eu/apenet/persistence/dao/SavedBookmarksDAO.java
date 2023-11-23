package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.SavedBookmarks;

/***
 * Interface for SavedBookmarksDAO
 * 
 */

public interface SavedBookmarksDAO extends GenericDAO<SavedBookmarks, Long> {

	/***
	 * this method gets a single saved bookmark with a user id and an bookmark id
	 *
	 * @param liferayUserId {@link Long} current user id
	 * @param id {@link long} bookmark id
	 *
	 * @return a single saved bookmark if is correct, null if not
	 */
	public SavedBookmarks getSavedBookmark(Long liferayUserId,long id);

	/***
	 * this method gets a single saved bookmark with a MODx user id and an bookmark id
	 *
	 * @param modxUserId {@link Long} current user modx id
	 * @param id {@link long} bookmark id
	 *
	 * @return a single saved bookmark if is correct, null if not
	 */
	public SavedBookmarks getSavedBookmarkByModxUserId(Long modxUserId,long id);

	/***
	 * This function counts the saved bookmarks the user has
	 *
	 * @param liferayUserId {@link Long} current user id
	 *
	 * @return query.getSingleResult() {@link Long} the number of saved boomarks the user has, null if error in the transaction
	 */
	public Long countSavedBookmarks(Long liferayUserId);

	/***
	 * This function counts the saved bookmarks the MODx user has
	 *
	 * @param modxUserId {@link Long} current modx user id
	 *
	 * @return query.getSingleResult() {@link Long} the number of saved boomarks the user has, null if error in the transaction
	 */
	public Long countSavedBookmarksByModxUserId(Long modxUserId);

	/***
	 * This method gets a number of results for a paginated list with the saved bookmarks the user has
	 *
	 * @param liferayUserId {@link Long} current user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 *
	 * @return List {@link SavedBookmarks} a list of the user's saved bookmarks
	 */
	public List<SavedBookmarks> getSavedBookmarks(Long liferayUserId, int pageNumber, int pageSize);

	/***
	 * This method gets a number of results for a paginated list with the saved bookmarks the MODx user has
	 *
	 * @param modxUserId {@link Long} current MODx user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 *
	 * @return List {@link SavedBookmarks} a list of the user's saved bookmarks
	 */
	public List<SavedBookmarks> getSavedBookmarksByModxUserId(Long modxUserId, int pageNumber, int pageSize);

	/***
	 * This method gets a list of saved bookmarks that are not included into a selected collection
	 *
	 * @param id {@link Long} collection id
	 * @param liferayUserId {@link long} current user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 *
	 * @return SavedBookmarks List {@link SavedBookmarks} a list of saved bookmarks that are not included into a selected collection
	 */
	public List<SavedBookmarks> getSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId, int pageNumber ,int pageSize);

	/***
	 * This method gets a list of saved bookmarks that are not included into a selected collection
	 *
	 * @param id {@link Long} collection id
	 * @param modxUserId {@link long} current user id
	 * @param pageNumber {@link int} the current number of page in the pagination results
	 * @param pageSize {@link int} the number of results results per page
	 *
	 * @return SavedBookmarks List {@link SavedBookmarks} a list of saved bookmarks that are not included into a selected collection
	 */
	public List<SavedBookmarks> getSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(Long id, long modxUserId, int pageNumber ,int pageSize);

	/***
	 * This method gets a list with the saved bookmarks of the user
	 *
	 * @param bookmarksOut List {@link Long} list of bookmarks
	 * @param liferayUserId {@link Long} current user id
	 *
	 * @return a list with the saved bookmarks of the user
	 * */
	public List<SavedBookmarks> getSavedBookmarksByIdsAndUserid(List<Long> bookmarksOut, Long liferayUserId);

	/***
	 * This method gets a list with the saved bookmarks of the MODx user
	 *
	 * @param bookmarksOut List {@link Long} list of bookmarks
	 * @param modxUserId {@link Long} current MODx user id
	 *
	 * @return a list with the saved bookmarks of the user
	 * */
	public List<SavedBookmarks> getSavedBookmarksByIdsAndModxUserid(List<Long> bookmarksOut, Long modxUserId);

	/***
	 * This method gets the number of saved bookmarks that are not included into a selected collection
	 *
	 * @param id {@link Long} collection id
	 * @param liferayUserId {@link long} current user id
	 *
	 * @return ({@link Long})criteria.uniqueResult() The number of saved bookmarks that are not included into a selected collection
	 */
	public Long countSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId);

	/***
	 * This method gets the number of saved bookmarks that are not included into a selected collection
	 *
	 * @param id {@link Long} collection id
	 * @param modxUserId {@link long} current MODx user id
	 *
	 * @return ({@link Long})criteria.uniqueResult() The number of saved bookmarks that are not included into a selected collection
	 */
	public Long countSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(Long id, long modxUserId);
}