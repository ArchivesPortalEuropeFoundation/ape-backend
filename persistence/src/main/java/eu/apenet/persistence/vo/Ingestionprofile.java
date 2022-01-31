/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ingestionprofile")
public class Ingestionprofile implements Serializable {

    private static final long serialVersionUID = 1383138313831383138L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ai_id")
    private Integer aiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_id", foreignKey = @ForeignKey(name = "ingestionprofile_ai_id_fkey"), insertable = false, updatable = false)
    private ArchivalInstitution archivalInstitution;

    @Column(name = "name_profile")
    private String nameProfile;
    @Column(name = "file_type")
    private Integer fileType;
    @Column(name = "upload_action")
    private IngestionprofileDefaultUploadAction uploadAction = IngestionprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH;
    @Column(name = "exist_action")
    private IngestionprofileDefaultExistingFileAction existAction = IngestionprofileDefaultExistingFileAction.KEEP;
    @Column(name = "noeadid_action")
    private IngestionprofileDefaultNoEadidAction noeadidAction = IngestionprofileDefaultNoEadidAction.REMOVE;
    @Column(name = "dao_type")
    private IngestionprofileDefaultDaoType daoType = IngestionprofileDefaultDaoType.UNSPECIFIED;
    @Column(name = "dao_type_from_file")
    private Boolean daoTypeFromFile = true;
    @Column(name = "europeana_conversion_type")
    private Boolean europeanaConversionType;
    @Column(name = "europeana_dp")
    private String europeanaDataProvider;
    @Column(name = "europeana_dp_from_file")
    private Boolean europeanaDataProviderFromFile = true;
    @Column(name = "europeana_dao_type")
    private Integer europeanaDaoType;
    @Column(name = "europeana_dao_type_from_file")
    private Boolean europeanaDaoTypeFromFile = true;
    @Column(name = "europeana_languages_material")
    private String europeanaLanguagesMaterial;
    @Column(name = "europeana_languages_material_from_file")
    private Boolean europeanaLanguagesMaterialFromFile = true;
    @Column(name = "europeana_language_description")
    private String europeanaLanguageDescription;
    @Column(name = "europeana_language_description_from_file")
    private Boolean europeanaLanguageDescriptionFromFile = true;
    @Column(name = "europeana_language_material_description_same")
    private Boolean europeanaLanguageMaterialDescriptionSame = true;
    @Column(name = "europeana_license_from_file")
    private Boolean europeanaLicenseFromFile = true;
    @Column(name = "europeana_license")
    private String europeanaLicense;
    @Column(name = "europeana_license_details")
    private String europeanaLicenseDetails;
    @Column(name = "europeana_add_rights")
    private String europeanaAddRights;
    @Column(name = "europeana_inh_elements_check")
    private Boolean europeanaInheritElementsCheck = true;
    @Column(name = "europeana_inh_elements")
    private Boolean europeanaInheritElements = false;
    @Column(name = "europeana_inh_origin_check")
    private Boolean europeanaInheritOriginCheck = true;
    @Column(name = "europeana_inh_origin")
    private Boolean europeanaInheritOrigin = false;
    @Column(name = "europeana_inh_unittitle_check")
    private Boolean europeanaInheritUnittitleCheck = true;
    @Column(name = "europeana_inh_unittitle")
    private Boolean europeanaInheritUnittitle = false;
    @Column(name = "source_of_identifiers")
    private String sourceOfIdentifiers;
    @Column(name = "use_archdesc_unittitle")
    private Boolean useArchdescUnittitle;
    @Column(name = "extract_eac_from_ead3")
    private Boolean extractEacFromEad3;
    @Column(name = "rights_of_digital_objects")
    private String rightsOfDigitalObjects;
    @Column(name = "rights_of_digital_description")
    private String rightsOfDigitalDescription;
    @Column(name = "rights_of_digital_holder")
    private String rightsOfDigitalHolder;
    @Column(name = "rights_of_ead_data")
    private String rightsOfEADData;
    @Column(name = "rights_of_ead_description")
    private String rightsOfEADDescription;
    @Column(name = "rights_of_ead_holder")
    private String rightsOfEADHolder;
    @Column(name = "rights_of_digital_objects_text")
    private String rightsOfDigitalObjectsText;
    @Column(name = "rights_of_ead_data_text")
    private String rightsOfEADDataText;
    @Column(name = "xsl_upload_id")
    private Long xslUploadId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xsl_upload_id", foreignKey = @ForeignKey(name = "xsl_upload_id_fkey"), insertable = false, updatable = false)
    private XslUpload xslUpload;

    @Column(name = "rights_of_ead_data_id")
    private Integer rightsInformationOfEADDataId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rights_of_ead_data_id", foreignKey = @ForeignKey(name = "ingestion_profile_rinf_id_fkey"), insertable = false, updatable = false)
    private RightsInformation rightsInformationOfEADData;

    public Ingestionprofile() {
    }

    public Ingestionprofile(Integer aiId, String nameProfile, Integer fileType) {
        this.aiId = aiId;
        this.nameProfile = nameProfile;
        this.fileType = fileType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAiId() {
        return aiId;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    public String getNameProfile() {
        return nameProfile;
    }

    public void setNameProfile(String nameProfile) {
        this.nameProfile = nameProfile;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public IngestionprofileDefaultUploadAction getUploadAction() {
        return uploadAction;
    }

    public void setUploadAction(IngestionprofileDefaultUploadAction uploadAction) {
        this.uploadAction = uploadAction;
    }

    public IngestionprofileDefaultExistingFileAction getExistAction() {
        return existAction;
    }

    public void setExistAction(IngestionprofileDefaultExistingFileAction existAction) {
        this.existAction = existAction;
    }

    public IngestionprofileDefaultNoEadidAction getNoeadidAction() {
        return noeadidAction;
    }

    public void setNoeadidAction(IngestionprofileDefaultNoEadidAction noeadidAction) {
        this.noeadidAction = noeadidAction;
    }

    public IngestionprofileDefaultDaoType getDaoType() {
        return daoType;
    }

    public void setDaoType(IngestionprofileDefaultDaoType daoType) {
        this.daoType = daoType;
    }

    public Boolean getDaoTypeFromFile() {
        return daoTypeFromFile;
    }

    public void setDaoTypeFromFile(Boolean daoTypeFromFile) {
        this.daoTypeFromFile = daoTypeFromFile;
    }

    public Boolean getEuropeanaConversionType() {
        return europeanaConversionType;
    }

    public void setEuropeanaConversionType(Boolean europeanaConversionType) {
        this.europeanaConversionType = europeanaConversionType;
    }

    public String getEuropeanaDataProvider() {
        return europeanaDataProvider;
    }

    public void setEuropeanaDataProvider(String europeanaDataProvider) {
        this.europeanaDataProvider = europeanaDataProvider;
    }

    public Boolean getEuropeanaDataProviderFromFile() {
        return europeanaDataProviderFromFile;
    }

    public void setEuropeanaDataProviderFromFile(Boolean europeanaDataProviderFromFile) {
        this.europeanaDataProviderFromFile = europeanaDataProviderFromFile;
    }

    public Integer getEuropeanaDaoType() {
        return europeanaDaoType;
    }

    public void setEuropeanaDaoType(Integer europeanaDaoType) {
        this.europeanaDaoType = europeanaDaoType;
    }

    public Boolean getEuropeanaDaoTypeFromFile() {
        return europeanaDaoTypeFromFile;
    }

    public void setEuropeanaDaoTypeFromFile(Boolean europeanaDaoTypeFromFile) {
        this.europeanaDaoTypeFromFile = europeanaDaoTypeFromFile;
    }

    public String getEuropeanaLanguagesMaterial() {
        return europeanaLanguagesMaterial;
    }

    public void setEuropeanaLanguagesMaterial(String europeanaLanguagesMaterial) {
        this.europeanaLanguagesMaterial = europeanaLanguagesMaterial;
    }

    public Boolean getEuropeanaLanguagesMaterialFromFile() {
        return europeanaLanguagesMaterialFromFile;
    }

    public void setEuropeanaLanguagesMaterialFromFile(Boolean europeanaLanguagesMaterialFromFile) {
        this.europeanaLanguagesMaterialFromFile = europeanaLanguagesMaterialFromFile;
    }

    public String getEuropeanaLanguageDescription() {
        return europeanaLanguageDescription;
    }

    public void setEuropeanaLanguageDescription(String europeanaLanguageDescription) {
        this.europeanaLanguageDescription = europeanaLanguageDescription;
    }

    public Boolean getEuropeanaLanguageDescriptionFromFile() {
        return europeanaLanguageDescriptionFromFile;
    }

    public void setEuropeanaLanguageDescriptionFromFile(Boolean europeanaLanguageDescriptionFromFile) {
        this.europeanaLanguageDescriptionFromFile = europeanaLanguageDescriptionFromFile;
    }

    public Boolean getEuropeanaLanguageMaterialDescriptionSame() {
        return europeanaLanguageMaterialDescriptionSame;
    }

    public void setEuropeanaLanguageMaterialDescriptionSame(Boolean europeanaLanguageMaterialDescriptionSame) {
        this.europeanaLanguageMaterialDescriptionSame = europeanaLanguageMaterialDescriptionSame;
    }

    public Boolean getEuropeanaLicenseFromFile() {
        return europeanaLicenseFromFile;
    }

    public void setEuropeanaLicenseFromFile(Boolean europeanaLicenseFromFile) {
        this.europeanaLicenseFromFile = europeanaLicenseFromFile;
    }

    public String getEuropeanaLicense() {
        return europeanaLicense;
    }

    public void setEuropeanaLicense(String europeanaLicense) {
        this.europeanaLicense = europeanaLicense;
    }

    public String getEuropeanaLicenseDetails() {
        return europeanaLicenseDetails;
    }

    public void setEuropeanaLicenseDetails(String europeanaLicenseDetails) {
        this.europeanaLicenseDetails = europeanaLicenseDetails;
    }

    public String getEuropeanaAddRights() {
        return europeanaAddRights;
    }

    public void setEuropeanaAddRights(String europeanaAddRights) {
        this.europeanaAddRights = europeanaAddRights;
    }

    public Boolean getEuropeanaInheritElementsCheck() {
        return europeanaInheritElementsCheck;
    }

    public void setEuropeanaInheritElementsCheck(Boolean europeanaInheritElementsCheck) {
        this.europeanaInheritElementsCheck = europeanaInheritElementsCheck;
    }

    public Boolean getEuropeanaInheritElements() {
        return europeanaInheritElements;
    }

    public void setEuropeanaInheritElements(Boolean europeanaInheritElements) {
        this.europeanaInheritElements = europeanaInheritElements;
    }

    public Boolean getEuropeanaInheritOriginCheck() {
        return europeanaInheritOriginCheck;
    }

    public void setEuropeanaInheritOriginCheck(Boolean europeanaInheritOriginCheck) {
        this.europeanaInheritOriginCheck = europeanaInheritOriginCheck;
    }

    public Boolean getEuropeanaInheritOrigin() {
        return europeanaInheritOrigin;
    }

    public void setEuropeanaInheritOrigin(Boolean europeanaInheritOrigin) {
        this.europeanaInheritOrigin = europeanaInheritOrigin;
    }

    public Boolean getEuropeanaInheritUnittitleCheck() {
        return europeanaInheritUnittitleCheck;
    }

    public void setEuropeanaInheritUnittitleCheck(Boolean europeanaInheritUnittitleCheck) {
        this.europeanaInheritUnittitleCheck = europeanaInheritUnittitleCheck;
    }

    public Boolean getEuropeanaInheritUnittitle() {
        return europeanaInheritUnittitle;
    }

    public void setEuropeanaInheritUnittitle(Boolean europeanaInheritUnittitle) {
        this.europeanaInheritUnittitle = europeanaInheritUnittitle;
    }

    public Boolean getExtractEacFromEad3() {
        return extractEacFromEad3;
    }

    public void setExtractEacFromEad3(Boolean extractEacFromEad3) {
        this.extractEacFromEad3 = extractEacFromEad3;
    }

    /**
     * @return the sourceOfIdentifiers
     */
    public String getSourceOfIdentifiers() {
        return this.sourceOfIdentifiers;
    }

    /**
     * @param sourceOfIdentifiers the sourceOfIdentifiers to set
     */
    public void setSourceOfIdentifiers(String sourceOfIdentifiers) {
        this.sourceOfIdentifiers = sourceOfIdentifiers;
    }

    public Boolean getUseArchdescUnittitle() {
        return useArchdescUnittitle;
    }

    public void setUseArchdescUnittitle(Boolean useArchdescUnittitle) {
        this.useArchdescUnittitle = useArchdescUnittitle;
    }

    /**
     * @return the rightsOfDigitalObjects
     */
    public String getRightsOfDigitalObjects() {
        return this.rightsOfDigitalObjects;
    }

    /**
     * @param rightsOfDigitalObjects the rightsOfDigitalObjects to set
     */
    public void setRightsOfDigitalObjects(String rightsOfDigitalObjects) {
        this.rightsOfDigitalObjects = rightsOfDigitalObjects;
    }

    /**
     * @return the rightsOfDigitalDescription
     */
    public String getRightsOfDigitalDescription() {
        return this.rightsOfDigitalDescription;
    }

    /**
     * @param rightsOfDigitalDescription the rightsOfDigitalDescription to set
     */
    public void setRightsOfDigitalDescription(String rightsOfDigitalDescription) {
        this.rightsOfDigitalDescription = rightsOfDigitalDescription;
    }

    /**
     * @return the rightsOfDigitalHolder
     */
    public String getRightsOfDigitalHolder() {
        return this.rightsOfDigitalHolder;
    }

    /**
     * @param rightsOfDigitalHolder the rightsOfDigitalHolder to set
     */
    public void setRightsOfDigitalHolder(String rightsOfDigitalHolder) {
        this.rightsOfDigitalHolder = rightsOfDigitalHolder;
    }

    /**
     * @return the rightsOfEADData
     */
    public String getRightsOfEADData() {
        return this.rightsOfEADData;
    }

    /**
     * @param rightsOfEADData the rightsOfEADData to set
     */
    public void setRightsOfEADData(String rightsOfEADData) {
        this.rightsOfEADData = rightsOfEADData;
    }

    /**
     * @return the rightsOfEADDescription
     */
    public String getRightsOfEADDescription() {
        return this.rightsOfEADDescription;
    }

    /**
     * @param rightsOfEADDescription the rightsOfEADDescription to set
     */
    public void setRightsOfEADDescription(String rightsOfEADDescription) {
        this.rightsOfEADDescription = rightsOfEADDescription;
    }

    /**
     * @return the rightsOfEADHolder
     */
    public String getRightsOfEADHolder() {
        return this.rightsOfEADHolder;
    }

    /**
     * @param rightsOfEADHolder the rightsOfEADHolder to set
     */
    public void setRightsOfEADHolder(String rightsOfEADHolder) {
        this.rightsOfEADHolder = rightsOfEADHolder;
    }

    /**
     * @return the rightsOfDigitalObjectsText
     */
    public String getRightsOfDigitalObjectsText() {
        return this.rightsOfDigitalObjectsText;
    }

    /**
     * @param rightsOfDigitalObjectsText the rightsOfDigitalObjectsText to set
     */
    public void setRightsOfDigitalObjectsText(String rightsOfDigitalObjectsText) {
        this.rightsOfDigitalObjectsText = rightsOfDigitalObjectsText;
    }

    /**
     * @return the rightsOfEADDataText
     */
    public String getRightsOfEADDataText() {
        return this.rightsOfEADDataText;
    }

    /**
     * @param rightsOfEADDataText the rightsOfEADDataText to set
     */
    public void setRightsOfEADDataText(String rightsOfEADDataText) {
        this.rightsOfEADDataText = rightsOfEADDataText;
    }

    public Long getXslUploadId() {
        return xslUploadId;
    }

    public void setXslUploadId(Long xslUploadId) {
        this.xslUploadId = xslUploadId;
    }

    public XslUpload getXslUpload() {
        return xslUpload;
    }

    public void setXslUpload(XslUpload xslUpload) {
        this.xslUpload = xslUpload;
    }

    public void setRightsInformationOfEADDataId(Integer rightsInformationOfEADDataId) {
        this.rightsInformationOfEADDataId = rightsInformationOfEADDataId;
    }

    public void setRightsInformationOfEADData(RightsInformation rightsInformationOfEADData) {
        this.rightsInformationOfEADData = rightsInformationOfEADData;
    }

    public Integer getRightsInformationOfEADDataId() {
        return rightsInformationOfEADDataId;
    }

    public RightsInformation getRightsInformationOfEADData() {
        return rightsInformationOfEADData;
    }
}
