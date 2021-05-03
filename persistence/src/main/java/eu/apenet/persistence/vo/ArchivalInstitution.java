package eu.apenet.persistence.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eu.archivesportaleurope.util.ApeUtil;
import javax.persistence.CascadeType;
import javax.persistence.ForeignKey;

@Entity
@Table(name = "archival_institution")
public class ArchivalInstitution implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5793664005280731903L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int aiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_ai_id", foreignKey = @ForeignKey(name = "archival_institution_parent_ai_id_fkey"))
    private ArchivalInstitution parent;

    @Column(name = "parent_ai_id", insertable = false, updatable = false)
    private Integer parentAiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "archival_institution_p_id_fkey"), insertable = false, updatable = false)
    private User partner;

    @Column(name = "user_id")
    private Integer partnerId;

    @Column(name = "country_id")
    private Integer countryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", foreignKey = @ForeignKey(name = "archival_institution_cou_id_fkey"), insertable = false, updatable = false)
    private Country country;

    private String ainame;

    @Temporal(TemporalType.DATE)
    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(name = "eag_path")
    private String eagPath;

    private String repositorycode;

    private String autform;

    @Column(name = "isgroup")
    private boolean group;

    @Column(name = "internal_al_id")
    private String internalAlId;

    private int alorder;

    @Column(name = "contain_searchable_items")
    private boolean containSearchableItems;

    @Column(name = "content_lastmodified_date")
    private Date contentLastModifiedDate;

    @Column(name = "feedback_email")
    private String feedbackEmail;

    @Column(name = "using_mets")
    private boolean usingMets;

    @OneToMany(mappedBy = "archivalInstitution")
    private Set<AiAlternativeName> aiAlternativeNames = new HashSet<AiAlternativeName>(0);

    @OneToMany(mappedBy = "parent")
    @OrderColumn(name = "alorder")
    private List<ArchivalInstitution> childArchivalInstitutions = new ArrayList<ArchivalInstitution>(0);

    @OneToMany(mappedBy = "archivalInstitution")
    private Set<FindingAid> findingAids = new HashSet<FindingAid>(0);

    @OneToMany(mappedBy = "archivalInstitution")
    private Set<HoldingsGuide> holdingsGuides = new HashSet<HoldingsGuide>(0);

    @OneToMany(mappedBy = "archivalInstitution")
    private Set<SourceGuide> sourceGuides = new HashSet<SourceGuide>(0);

    @OneToMany(mappedBy = "archivalInstitution")
    private Set<Ead3> ead3 = new HashSet<>(0);

    @OneToMany(mappedBy = "archivalInstitution", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ingestionprofile> ingestionprofile = new HashSet<Ingestionprofile>();

    @Column(nullable = true)
    private Boolean openDataEnabled;
    @Column(nullable = true)
    private Long totalSolrDocsForOpenData;
    @Column(nullable = true)
    private Long unprocessedSolrDocs;
    @Column(nullable = true)
    private Integer openDataQueueId;
    
    @Column(name = "rights_information")
    private Integer rightsInformationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rights_information", foreignKey = @ForeignKey(name = "archival_institution_rinf_id_fkey"), insertable = false, updatable = false)
    private RightsInformation rightsInformation;
    
    @Column(name = "share_with_wikimedia")
    private Boolean shareWithWikimedia;

    public int getAiId() {
        return this.aiId;
    }

    public void setAiId(int aiId) {
        this.aiId = aiId;
    }

    public ArchivalInstitution getParent() {
        return this.parent;
    }

    public void setParent(ArchivalInstitution archivalInstitution) {
        this.parent = archivalInstitution;
    }

    public Integer getParentAiId() {
        return parentAiId;
    }

    public void setParentAiId(Integer parentAiId) {
        this.parentAiId = parentAiId;
    }

    public User getPartner() {
        return this.partner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }

    public String getAiname() {
        return this.ainame;
    }

    public void setAiname(String ainame) {
        this.ainame = ainame;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEagPath() {
        return this.eagPath;
    }

    public void setEagPath(String eagPath) {
        this.eagPath = eagPath;
    }

    public String getEncodedRepositorycode() {
        return ApeUtil.encodeRepositoryCode(this.repositorycode);
    }

    public void setRepositorycode(String repositorycode) {
        this.repositorycode = repositorycode;
    }

    public boolean isContainSearchableItems() {
        return containSearchableItems;
    }

    public String getRepositorycode() {
        return this.repositorycode;
    }

    public void setContainSearchableItems(boolean containSearchableItems) {
        this.containSearchableItems = containSearchableItems;
    }

    public String getAutform() {
        return this.autform;
    }

    public void setAutform(String autform) {
        this.autform = autform;
    }

    public boolean isGroup() {
        return this.group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public Set<HoldingsGuide> getHoldingsGuides() {
        return this.holdingsGuides;
    }

    public void setHoldingsGuides(Set<HoldingsGuide> holdingsGuides) {
        this.holdingsGuides = holdingsGuides;
    }

    public Set<SourceGuide> getSourceGuides() {
        return sourceGuides;
    }

    public void setSourceGuides(Set<SourceGuide> sourceGuides) {
        this.sourceGuides = sourceGuides;
    }

    public Set<AiAlternativeName> getAiAlternativeNames() {
        return this.aiAlternativeNames;
    }

    public void setAiAlternativeNames(Set<AiAlternativeName> aiAlternativeNames) {
        this.aiAlternativeNames = aiAlternativeNames;
    }

    public Set<FindingAid> getFindingAids() {
        return this.findingAids;
    }

    public void setFindingAids(Set<FindingAid> findingAids) {
        this.findingAids = findingAids;
    }

    public Set<Ead3> getEad3() {
        return ead3;
    }

    public void setEad3(Set<Ead3> ead3) {
        this.ead3 = ead3;
    }

    public List<ArchivalInstitution> getChildArchivalInstitutions() {
        return childArchivalInstitutions;
    }

    public void setChildArchivalInstitutions(List<ArchivalInstitution> childArchivalInstitutions) {
        this.childArchivalInstitutions = childArchivalInstitutions;
    }

    public Set<Ingestionprofile> getIngestionprofile() {
        return ingestionprofile;
    }

    public void setIngestionprofile(Set<Ingestionprofile> ingestionprofile) {
        this.ingestionprofile = ingestionprofile;
    }

    @Override
    public String toString() {
        return "ArchivalInstitution{"
                + "aiId=" + aiId
                + ", ainame='" + ainame + '\''
                + ", eagPath='" + eagPath + '\''
                + ", repositorycode='" + repositorycode + '\''
                + ", autform='" + autform + '\''
                + '}';
    }

    public void setAlorder(int alorder) {
        this.alorder = alorder;
    }

    public int getAlorder() {
        return alorder;
    }

    public String getInternalAlId() {
        return internalAlId;
    }

    public void setInternalAlId(String internalAlId) {
        this.internalAlId = internalAlId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Date getContentLastModifiedDate() {
        return contentLastModifiedDate;
    }

    public void setContentLastModifiedDate(Date contentLastModifiedDate) {
        this.contentLastModifiedDate = contentLastModifiedDate;
    }

    public boolean isUsingMets() {
        return usingMets;
    }

    public void setUsingMets(boolean usingMets) {
        this.usingMets = usingMets;
    }

    public String getFeedbackEmail() {
        return feedbackEmail;
    }

    public void setFeedbackEmail(String feedbackEmail) {
        this.feedbackEmail = feedbackEmail;
    }

    public boolean isOpenDataEnabled() {
        return openDataEnabled == null ? false : (boolean) openDataEnabled;
    }

    public void setOpenDataEnabled(boolean openDataEnabled) {
        this.openDataEnabled = openDataEnabled;
    }

    public long getTotalSolrDocsForOpenData() {
        return totalSolrDocsForOpenData == null ? 0 : (long) totalSolrDocsForOpenData;
    }

    public void setTotalSolrDocsForOpenData(long totalSolrDocsCount) {
        this.totalSolrDocsForOpenData = totalSolrDocsCount;
    }

    public long getUnprocessedSolrDocs() {
        return unprocessedSolrDocs == null ? 0 : (long) unprocessedSolrDocs;
    }

    public void setUnprocessedSolrDocs(long unprocessedSolrDocs) {
        this.unprocessedSolrDocs = unprocessedSolrDocs;
    }

    public Integer getOpenDataQueueId() {
        return openDataQueueId;
    }

    public void setOpenDataQueueId(Integer openDataQueueId) {
        this.openDataQueueId = openDataQueueId;
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

    public Boolean getShareWithWikimedia() {
        return shareWithWikimedia;
    }

    public void setShareWithWikimedia(Boolean shareWithWikimedia) {
        this.shareWithWikimedia = shareWithWikimedia;
    }
}
