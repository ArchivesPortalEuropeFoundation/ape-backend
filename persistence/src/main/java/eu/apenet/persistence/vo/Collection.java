package eu.apenet.persistence.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This class contains a collection object
 */
@Entity
@Table(name = "collection")
/***
 * 
 * This class contains a collection object
 *
 */
public class Collection implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	@Column(name = "title", length = 200)
	private String title;
	@Column(name = "liferay_user_id", nullable = true)
	private long liferayUserId;
	@Column(name = "modx_user_id", nullable = true)
	private long modxUserId;
	@Column(name = "public_collection", nullable = false)
	private boolean public_collection;
	@Column(name = "edit", nullable = false)
	private boolean edit;
	@Column(name = "description", columnDefinition="TEXT")
	private String description;
	@Column(name = "modified_date")
	private Date modified_date;
	@OneToMany(mappedBy = "collection")
	private Set<CollectionContent> collectionContents = new HashSet<CollectionContent>();

	public Collection() {
	}

	/***
	 * Collection object
	 * 
	 * @param id {@link long} collection identifier
	 * @param liferayUserId {@link long} user id in Liferay
	 * @param public_ {@link boolean} true if collection is public, false if private
	 * @param edit {@link boolean} true if collection is editable, false if not
	 */
	public Collection(long id, long liferayUserId, boolean public_, boolean edit) {
		this.id = id;
		this.liferayUserId = liferayUserId;
		this.public_collection = public_;
		this.edit = edit;
	}

	/***
	 * Collection object with all params
	 * 
	 * @param id {@link long} collection identifier
	 * @param title {@link String} collection title
	 * @param liferayUserId {@link long} user id in Liferay
	 * @param public_ {@link boolean} true if collection is public, false if private
	 * @param edit {@link boolean} true if collection is editable, false if not
	 * @param description {@link String} collection description 
	 * @param modified_date {@link Date} time and date from last modification
	 * @param collectionContents Set {@link CollectionContent} the content of the collection
	 */
	public Collection(long id, String title, long liferayUserId,
			boolean public_, boolean edit, String description, Date modified_date, 
			Set<CollectionContent> collectionContents) {
		this.id = id;
		this.title = title;
		this.liferayUserId = liferayUserId;
		this.public_collection = public_;
		this.edit = edit;
		this.description = description;
		this.modified_date = modified_date;
		this.collectionContents = collectionContents;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getLiferayUserId() {
		return this.liferayUserId;
	}

	public void setLiferayUserId(long liferayUserId) {
		this.liferayUserId = liferayUserId;
	}

	public boolean isPublic_() {
		return this.public_collection;
	}

	public void setPublic_(boolean public_) {
		this.public_collection = public_;
	}

	public boolean isEdit() {
		return this.edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<CollectionContent> getCollectionContents() {
		return this.collectionContents;
	}

	public void setCollectionContents(Set<CollectionContent> collectionContents) {
		this.collectionContents = collectionContents;
	}
	
	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date timeZone) {
		this.modified_date = timeZone;
	}

	public void setModxUserId(long modxUserId) {
		this.modxUserId = modxUserId;
	}

	public long getModxUserId() {
		return modxUserId;
	}
}
