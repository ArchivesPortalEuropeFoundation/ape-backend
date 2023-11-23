package eu.apenet.persistence.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/***
 *  This class contains a saved bookmark object
 *
 */
@Entity
@Table(name = "saved_bookmarks")
public class SavedBookmarks{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "liferay_user_id")
	private long liferayUserId;
	@Column(name = "modx_user_id")
	private long modxUserId;
	@Column(name = "modified_date")
	private	Date modifiedDate;
	@Column(name = "description")
	private String description;
	@Column(name = "bookmark_name")
	private String name;
	@Column(name = "persistent_link")
	private String link;
	@Column(name = "modx_relative_link")
	private String modxRelativeLink;
	@Column(name = "is_valid")
	private Boolean isValid;
	@Column(name = "typedocument")
	private String typedocument;
	@Column(name = "content_title")
	private String contentTitle;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getLiferayUserId() {
		return liferayUserId;
	}
	public void setLiferayUserId(long liferayUserId) {
		this.liferayUserId = liferayUserId;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTypedocument() {
		return typedocument;
	}
	public void setTypedocument(String typedocument) {
		this.typedocument = typedocument;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setModxUserId(long modxUserId) {
		this.modxUserId = modxUserId;
	}

	public long getModxUserId() {
		return modxUserId;
	}

	public void setModxRelativeLink(String modxRelativeLink) {
		this.modxRelativeLink = modxRelativeLink;
	}

	public void setValid(Boolean valid) {
		isValid = valid;
	}

	public String getModxRelativeLink() {
		return modxRelativeLink;
	}

	public Boolean getValid() {
		return isValid;
	}
}
