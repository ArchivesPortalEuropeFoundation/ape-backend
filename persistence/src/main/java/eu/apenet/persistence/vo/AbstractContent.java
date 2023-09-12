package eu.apenet.persistence.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import eu.archivesportaleurope.util.ApeUtil;

public abstract class AbstractContent  implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3522597311166738909L;
	public abstract Integer getId();
	public abstract void setId(Integer id);
	public abstract UploadMethod getUploadMethod();
	public abstract void setUploadMethod(UploadMethod uploadMethod);
	public abstract ArchivalInstitution getArchivalInstitution();
	public abstract void setArchivalInstitution(ArchivalInstitution archivalInstitution) ;
	public abstract String getTitle();
	public abstract void setTitle(String title);
	public abstract Date getUploadDate();
	public abstract void setUploadDate(Date uploadDate);
	public abstract Date getPublishDate();
	public abstract void setPublishDate(Date publishDate);
	public abstract String getPath();
	public abstract void setPath(String path);
	public abstract String getIdentifier();
	public abstract void setIdentifier(String identifier);
	public abstract boolean isPublished();
	public abstract void setPublished(boolean searchable);
	public abstract ValidatedState getValidated();
	public abstract void setValidated(ValidatedState validated);
	public abstract boolean isConverted();
	public abstract void setConverted(boolean converted);
	public abstract QueuingState getQueuing();
	public abstract void setQueuing(QueuingState queingState);
    @Deprecated
	public abstract Set<QueueItem> getQueueItems();
	public abstract void setQueueItems(Set<QueueItem> indexQueues);
	public abstract Integer getAiId();
	public abstract void setAiId(Integer aiId) ;
    public QueueItem getQueueItem() {
        Set<QueueItem> set = getQueueItems();
        if(set == null || set.isEmpty())
            return null;
        return set.iterator().next();
    }
    public abstract Set<Warnings> getWarningses();
    public abstract void setWarningses(Set<Warnings> warningses);
	public String getEncodedIdentifier(){
		return ApeUtil.encodeSpecialCharacters(getIdentifier());
	}
	public abstract String getMetaContent();
	public abstract void setMetaContent(String metaContent);
}
