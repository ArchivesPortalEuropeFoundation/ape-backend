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
@Table(name = "eac_cpf")
public class EacCpf extends AbstractContent {

    /**
     *
     */
    private static final long serialVersionUID = 6760184769676870729L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition="TEXT")
    private String title;
    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    private String path;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "um_id", foreignKey = @ForeignKey(name = "eac_cpf_um_id_fkey"))
    private UploadMethod uploadMethod;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_id", foreignKey = @ForeignKey(name = "eac_cpf_ai_id_fkey"))
    private ArchivalInstitution archivalInstitution;
    @Column(name = "ai_id", updatable = false, insertable = false)
    private Integer aiId;
    private boolean published = false;
    private boolean converted = false;
    private ValidatedState validated = ValidatedState.NOT_VALIDATED;
    private EuropeanaState europeana = EuropeanaState.NOT_CONVERTED;
    private long cpfRelations = 0l;
    private long resourceRelations = 0l;
    private long functionRelations = 0l;
    private QueuingState queuing = QueuingState.NO;
    @Column(name = "publish_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date publishDate;
    @Column(nullable = false)
    private String identifier;

    @OneToMany(mappedBy = "eacCpf")
    private Set<QueueItem> queueItems = new HashSet<QueueItem>(0);
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eacCpf")
    private Set<Warnings> warningses = new HashSet<Warnings>(0);

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ead3_id", foreignKey = @ForeignKey(name = "index_eac_cpf_ead3_id_fkey"))
    private Ead3 ead3;

    @Column(name = "rights_information")
    private Integer rightsInformationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rights_information", foreignKey = @ForeignKey(name = "archival_institution_rinf_id_fkey"), insertable = false, updatable = false)
    private RightsInformation rightsInformation;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    @Override
    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    @Override
    public Integer getAiId() {
        return aiId;
    }

    @Override
    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

    @Override
    public UploadMethod getUploadMethod() {
        return uploadMethod;
    }

    @Override
    public void setUploadMethod(UploadMethod uploadMethod) {
        this.uploadMethod = uploadMethod;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String nameEntry) {
        this.title = nameEntry;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean isPublished() {
        return published;
    }

    @Override
    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public boolean isConverted() {
        return converted;
    }

    @Override
    public void setConverted(boolean converted) {
        this.converted = converted;
    }

    @Override
    public ValidatedState getValidated() {
        return validated;
    }

    @Override
    public void setValidated(ValidatedState validated) {
        this.validated = validated;
    }

    public long getCpfRelations() {
        return cpfRelations;
    }

    public void setCpfRelations(long cpfRelations) {
        this.cpfRelations = cpfRelations;
    }

    public long getResourceRelations() {
        return resourceRelations;
    }

    public void setResourceRelations(long resourceRelations) {
        this.resourceRelations = resourceRelations;
    }

    public long getFunctionRelations() {
        return functionRelations;
    }

    public void setFunctionRelations(long functionRelations) {
        this.functionRelations = functionRelations;
    }

    public EuropeanaState getEuropeana() {
        return europeana;
    }

    public void setEuropeana(EuropeanaState europeana) {
        this.europeana = europeana;
    }

    @Override
    public QueuingState getQueuing() {
        return queuing;
    }

    @Override
    public void setQueuing(QueuingState queuing) {
        this.queuing = queuing;
    }

    @Override
    public Date getUploadDate() {
        return uploadDate;
    }

    @Override
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public Date getPublishDate() {
        return publishDate;
    }

    @Override
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public Set<QueueItem> getQueueItems() {
        return queueItems;
    }

    @Override
    public void setQueueItems(Set<QueueItem> queueItems) {
        this.queueItems = queueItems;
    }

    @Override
    public Set<Warnings> getWarningses() {
        return warningses;
    }

    @Override
    public void setWarningses(Set<Warnings> warningses) {
        this.warningses = warningses;
    }

    public Ead3 getEad3() {
        return ead3;
    }

    public void setEad3(Ead3 ead3) {
        this.ead3 = ead3;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - (" + getIdentifier() + "," + getId() + ") ";
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
