package eu.apenet.persistence.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "finding_aid")
public class FindingAid extends Ead {

    /**
     *
     */
    private static final long serialVersionUID = 8833722976902370936L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String eadid;
    @Column(columnDefinition="TEXT")
    private String title;
    @Column(name = "path_apenetead")
    private String pathApenetead;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_date")
    private Date uploadDate;
    @Column(name = "publish_date")
    private Date publishDate;
    /*
     * states
     */
    private boolean converted = false;
    private ValidatedState validated = ValidatedState.NOT_VALIDATED;
    private boolean published = false;
    private boolean dynamic = false;
    private QueuingState queuing = QueuingState.NO;
    private EuropeanaState europeana = EuropeanaState.NOT_CONVERTED;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "um_id", foreignKey = @ForeignKey(name = "finding_aid_um_id_fkey"))
    private UploadMethod uploadMethod;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_id", foreignKey = @ForeignKey(name = "finding_aid_ai_id_fkey"))
    private ArchivalInstitution archivalInstitution;
    @Column(name = "ai_id", updatable = false, insertable = false)
    private Integer aiId;
    @Column(name = "total_number_of_web_resource_edm")
    private Long totalNumberOfWebResourceEdm = 0l;
    private Long totalNumberOfChos = 0l;
    private Long totalNumberOfDaos = 0l;
    private Long totalNumberOfUnits = 0l;
    private Long totalNumberOfUnitsWithDao = 0l;
    @OneToMany(mappedBy = "findingAid")
    private Set<QueueItem> queueItems = new HashSet<QueueItem>(0);
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "findingAid")
    private Set<Warnings> warningses = new HashSet<Warnings>(0);
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "findingAid")
    private Set<EadContent> eadContents = new HashSet<EadContent>(0);
    @OneToMany(mappedBy = "findingAid")
    private Set<HgSgFaRelation> hgSgFaRelations = new HashSet<HgSgFaRelation>();

    @Column(name = "rights_information")
    private Integer rightsInformationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rights_information", foreignKey = @ForeignKey(name = "archival_institution_rinf_id_fkey"), insertable = false, updatable = false)
    private RightsInformation rightsInformation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEadid() {
        return eadid;
    }

    public void setEadid(String eadid) {
        this.eadid = eadid;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getPathApenetead() {
        return pathApenetead;
    }

    public void setPathApenetead(String pathApenetead) {
        this.pathApenetead = pathApenetead;
    }

    public UploadMethod getUploadMethod() {
        return uploadMethod;
    }

    public void setUploadMethod(UploadMethod uploadMethod) {
        this.uploadMethod = uploadMethod;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    public Integer getAiId() {
        return aiId;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

    public Long getTotalNumberOfDaos() {
        return totalNumberOfDaos;
    }

    public void setTotalNumberOfDaos(Long totalNumberOfDaos) {
        this.totalNumberOfDaos = totalNumberOfDaos;
    }

    public Long getTotalNumberOfUnits() {
        return totalNumberOfUnits;
    }

    public void setTotalNumberOfUnits(Long totalNumberOfUnits) {
        this.totalNumberOfUnits = totalNumberOfUnits;
    }

    public Long getTotalNumberOfUnitsWithDao() {
        return totalNumberOfUnitsWithDao;
    }

    public void setTotalNumberOfUnitsWithDao(Long totalNumberOfUnitsWithDao) {
        this.totalNumberOfUnitsWithDao = totalNumberOfUnitsWithDao;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean searchable) {
        this.published = searchable;
    }

    public Set<QueueItem> getQueueItems() {
        return queueItems;
    }

    public void setQueueItems(Set<QueueItem> indexQueues) {
        this.queueItems = indexQueues;
    }

    public Set<Warnings> getWarningses() {
        return warningses;
    }

    public void setWarningses(Set<Warnings> warningses) {
        this.warningses = warningses;
    }

    public Set<EadContent> getEadContents() {
        return eadContents;
    }

    public void setEadContents(Set<EadContent> eadContents) {
        this.eadContents = eadContents;
    }

    @OneToMany(mappedBy = "findingAid")
    private Set<Ese> eses = new HashSet<Ese>(0);

    public Set<Ese> getEses() {
        return this.eses;
    }

    public void setEses(Set<Ese> eses) {
        this.eses = eses;
    }

    public ValidatedState getValidated() {
        return validated;
    }

    public void setValidated(ValidatedState validated) {
        this.validated = validated;
    }

    public boolean isConverted() {
        return converted;
    }

    public void setConverted(boolean converted) {
        this.converted = converted;
    }

    public EuropeanaState getEuropeana() {
        return europeana;
    }

    public void setEuropeana(EuropeanaState europeana) {
        this.europeana = europeana;
    }

    public QueuingState getQueuing() {
        return queuing;
    }

    public void setQueuing(QueuingState queuing) {
        this.queuing = queuing;
    }

    public Long getTotalNumberOfChos() {
        return totalNumberOfChos;
    }

    public void setTotalNumberOfChos(Long totalNumberOfChos) {
        this.totalNumberOfChos = totalNumberOfChos;
    }

    public Set<HgSgFaRelation> getHgSgFaRelations() {
        return hgSgFaRelations;
    }

    public void setHgSgFaRelations(Set<HgSgFaRelation> hgSgFaRelations) {
        this.hgSgFaRelations = hgSgFaRelations;
    }

    public HgSgFaRelation getHgSgFaRelation() {
        Set<HgSgFaRelation> set = getHgSgFaRelations();
        if (set == null || set.isEmpty()) {
            return null;
        }
        return set.iterator().next();
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Long getTotalNumberOfWebResourceEdm() {
        return totalNumberOfWebResourceEdm;
    }

    public void setTotalNumberOfWebResourceEdm(Long totalNumberOfWebResourceEdm) {
        this.totalNumberOfWebResourceEdm = totalNumberOfWebResourceEdm;
    }

    @Override
    public void setPath(String path) {
        this.pathApenetead = path;
    }

    public Integer getRightsInformationId() {
        return rightsInformationId;
    }

    public void setRightsInformationId(Integer rightsInformationId) {
        this.rightsInformationId = rightsInformationId;
    }

    public RightsInformation getRightsInformation() {
        return rightsInformation;
    }

    public void setRightsInformation(RightsInformation rightsInformation) {
        this.rightsInformation = rightsInformation;
    }
}
