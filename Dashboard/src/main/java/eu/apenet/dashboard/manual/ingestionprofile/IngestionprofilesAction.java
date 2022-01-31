/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.ingestionprofile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxConversionOptionsConstants;
import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.factory.DAOFactory;

/**
 *
 * @author papp
 */
public class IngestionprofilesAction extends AbstractInstitutionAction {

    /**
     * Serializable.
     */
    private static final long serialVersionUID = 292033613637062110L;
    private static final String CREATIVECOMMONS_CPDM = "cpdm";
    private static final String CREATIVECOMMONS_CC0 = "cc0";
    private static final String CREATIVECOMMONS = "creativecommons";
    private static final String EUROPEANA = "europeana";
    private static final String INHERITLANGUAGE_PROVIDE = "provide";
    private static final String OUT_OF_COPYRIGHT = "outofcopyright";
    // Source of identifiers.
    public static final String OPTION_UNITID = "unitid";
    private static final String OPTION_CID = "cid";
    // Source of title for fonds.
    private static final String OPTION_ARCHDESC_UNITTITLE = "archdescUnittitle";
    private static final String OPTION_TITLESTMT_TITLEPROPER = "titlestmtTitleproper";

    private static final String IN_COPYRIGHT_EU_ORPHAN_WORK = "InC-EU-OW";
    private static final String NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS = "NoC-OKLR";
    private static final String COPYRIGHT_NOT_EVALUATED = "CNE";
    public static final String CC_0 = "CC0";

    //Collections for basic tab
    private Set<SelectItem> ingestionprofiles = new LinkedHashSet<>();
    private Set<SelectItem> associatedFiletypes = new LinkedHashSet<>();
    private Set<SelectItem> uploadedFileActions = new LinkedHashSet<>();
    private Set<SelectItem> existingFileActions = new LinkedHashSet<>();
    private Set<SelectItem> noEadidActions = new LinkedHashSet<>();
    private Set<SelectItem> daoTypes = new LinkedHashSet<>();
    private Set<SelectItem> rightsDigitalObjects = new LinkedHashSet<>(); // Rights for digital objects.
    private Set<SelectItem> rightsEadData = new LinkedHashSet<>(); // Rights for EAD data.
    private Set<SelectItem> xslFiles = new LinkedHashSet<>();
    private RightsInformation aiRightsInformation;
    private RightsInformation defaultRightsInformation;

    //Collections for Europeana tab
    private Set<SelectItem> sourceOfIdentifiersSet = new TreeSet<>();
    private Set<SelectItem> sourceOfFondsTitleSet = new TreeSet<>();
    private Set<SelectItem> typeSet = new TreeSet<>();
    private Set<SelectItem> yesNoSet = new TreeSet<>();
    private Set<SelectItem> inheritLanguageSet = new TreeSet<>();
    private Set<SelectItem> languages = new TreeSet<>();
    private Set<SelectItem> licenseSet = new TreeSet<>();
    private Set<SelectItem> europeanaLicenseSet = new TreeSet<>();

    //fields for basic tab components
    private String profilelist;
    private String profileName;
    private String associatedFiletype;
    private String uploadedFileAction;
    private String existingFileAction;
    private String noEadidAction;
    private String daoType;
    private String daoTypeCheck;
    private String rightDigitalObjects;
    private String rightDigitalDescription;
    private String rightDigitalHolder;
    private RightsInformation rightsInformationEadData;
    private String rightEadData;
    private String rightEadDescription;
    private String rightEadHolder;
    private String defaultXslFile;
    private String extractEacFromEad3;

    //fields for Europeana tab components
    private String sourceOfIdentifiers;
    private String sourceOfFondsTitle;
    private String textDataProvider;
    private String dataProviderCheck;
    private String europeanaDaoType;
    private String europeanaDaoTypeCheck;
    private List<String> languageSelectionMaterial = new ArrayList<>();
    private String languageMaterialCheck;
    private String languageSelectionDescription;
    private String languageDescriptionCheck;
    private String languageDescriptionSameAsMaterialCheck;
    private String licenseCheck;
    private String license;
    private String europeanaLicense;
    private String cc_js_result_uri;
    private String licenseAdditionalInformation;
    private String inheritUnittitle;

    //other fields
    private static final Logger LOG = Logger.getLogger(IngestionprofilesAction.class);

    private String lastSelection; //See #1718 description

    @Override
    public String input() {
        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        List<Ingestionprofile> queryResult = profileDAO.getIngestionprofiles(getAiId());
        if (queryResult != null && !queryResult.isEmpty()) {
            for (Ingestionprofile entry : queryResult) {
                ingestionprofiles.add(new SelectItem(Long.toString(entry.getId()), entry.getNameProfile()));
            }
            if (profilelist == null) {
                if (this.lastSelection != null) {
                    this.profilelist = this.lastSelection;
                } else {
                    profilelist = ingestionprofiles.iterator().next().getValue();
                }
            } else {
                this.lastSelection = this.profilelist;
            }
        }

        if (StringUtils.isNotBlank(profilelist)) {

            Long profilelistLong = Long.parseLong(profilelist);
            Ingestionprofile ingestionprofile = profileDAO.findById(profilelistLong);
            if (ingestionprofile != null) {
                profileName = ingestionprofile.getNameProfile();
                associatedFiletype = ingestionprofile.getFileType().toString();
                if (!convertToString(XmlType.EAD_FA.getIdentifier()).equals(associatedFiletype)
                        && ingestionprofile.getUploadAction().getId() == 2) {
                    uploadedFileAction = "1";
                } else {
                    uploadedFileAction = Integer.toString(ingestionprofile.getUploadAction().getId());
                }
                existingFileAction = Integer.toString(ingestionprofile.getExistAction().getId());
                noEadidAction = Integer.toString(ingestionprofile.getNoeadidAction().getId());
                daoType = Integer.toString(ingestionprofile.getDaoType().getId());
                daoTypeCheck = Boolean.toString(ingestionprofile.getDaoTypeFromFile());
                if (null != ingestionprofile.getExtractEacFromEad3()) {
                    extractEacFromEad3 = Boolean.toString(ingestionprofile.getExtractEacFromEad3());
                } else {
                    extractEacFromEad3 = Boolean.toString(false);
                }
                // Rights for digital objects.
                this.setRightDigitalObjects(ingestionprofile.getRightsOfDigitalObjects());
                this.setRightDigitalDescription(ingestionprofile.getRightsOfDigitalDescription());
                this.setRightDigitalHolder(ingestionprofile.getRightsOfDigitalHolder());
                // Rights for EAD data.
                this.setRightEadData(ingestionprofile.getRightsOfEADData());
                this.setRightEadDescription(ingestionprofile.getRightsOfEADDescription());
                this.setRightEadHolder(ingestionprofile.getRightsOfEADHolder());
                this.setRightsInformationEadData(ingestionprofile.getRightsInformationOfEADData());
                this.setDefaultRightsInformation(ingestionprofile.getRightsInformationOfEADData());

                sourceOfIdentifiers = ingestionprofile.getSourceOfIdentifiers();
                if (sourceOfIdentifiers == null || sourceOfIdentifiers.isEmpty()) {
                    sourceOfIdentifiers = IngestionprofilesAction.OPTION_UNITID;
                }
                if (ingestionprofile.getUseArchdescUnittitle() == false) {
                    sourceOfFondsTitle = IngestionprofilesAction.OPTION_TITLESTMT_TITLEPROPER;
                } else {
                    sourceOfFondsTitle = IngestionprofilesAction.OPTION_ARCHDESC_UNITTITLE;
                }

                textDataProvider = ingestionprofile.getEuropeanaDataProvider();
                dataProviderCheck = Boolean.toString(ingestionprofile.getEuropeanaDataProviderFromFile());
                europeanaDaoType = Integer.toString(ingestionprofile.getEuropeanaDaoType());
                europeanaDaoTypeCheck = Boolean.toString(ingestionprofile.getEuropeanaDaoTypeFromFile());
                String[] tempLangMaterial = ingestionprofile.getEuropeanaLanguagesMaterial().split(" ");
                languageSelectionMaterial.addAll(Arrays.asList(tempLangMaterial));
                languageMaterialCheck = Boolean.toString(ingestionprofile.getEuropeanaLanguagesMaterialFromFile());
                languageSelectionDescription = ingestionprofile.getEuropeanaLanguageDescription();
                if (ingestionprofile.getEuropeanaLanguageDescriptionFromFile() == null) {
                    languageDescriptionCheck = Boolean.toString(false);
                } else {
                    languageDescriptionCheck = Boolean.toString(ingestionprofile.getEuropeanaLanguageDescriptionFromFile());
                }
                if (ingestionprofile.getEuropeanaLanguageMaterialDescriptionSame() == null) {
                    languageDescriptionSameAsMaterialCheck = Boolean.toString(true);
                } else {
                    languageDescriptionSameAsMaterialCheck = Boolean.toString(ingestionprofile.getEuropeanaLanguageMaterialDescriptionSame());
                }
                licenseCheck = Boolean.toString(ingestionprofile.getEuropeanaLicenseFromFile());
                license = ingestionprofile.getEuropeanaLicense();
                if (license.equals(EUROPEANA)) {
                    europeanaLicense = ingestionprofile.getEuropeanaLicenseDetails();
                }
                if (license.equals(CREATIVECOMMONS)) {
                    cc_js_result_uri = ingestionprofile.getEuropeanaLicenseDetails();
                }
                licenseAdditionalInformation = ingestionprofile.getEuropeanaAddRights();
                inheritUnittitle = Boolean.toString(ingestionprofile.getEuropeanaInheritUnittitle());
                if (ingestionprofile.getXslUploadId() != null) {
                    setDefaultXslFile(DAOFactory.instance().getXslUploadDAO().findById(ingestionprofile.getXslUploadId()).getId() + "");
                }
            }
        }
        setUp();
        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        Ingestionprofile profile;
        if (profilelist.equals("-1")) {
            profile = new Ingestionprofile(getAiId(), "", 1);
        } else {
            profile = profileDAO.findById(Long.parseLong(profilelist));
        }

        profile.setNameProfile(profileName);
        profile.setFileType(Integer.parseInt(associatedFiletype));
        profile.setUploadAction(IngestionprofileDefaultUploadAction.getUploadAction(uploadedFileAction));
        profile.setExistAction(IngestionprofileDefaultExistingFileAction.getExistingFileAction(existingFileAction));
        profile.setNoeadidAction(IngestionprofileDefaultNoEadidAction.getExistingFileAction(noEadidAction));
        profile.setDaoType(IngestionprofileDefaultDaoType.getDaoType(daoType));
        profile.setDaoTypeFromFile(Boolean.parseBoolean(daoTypeCheck));
        profile.setExtractEacFromEad3(Boolean.parseBoolean(extractEacFromEad3));
        // Only adds the rights options if file type is an EAD.
        if (profile.getFileType() != 2) {
            // Rights for digital objects.
            if (this.getRightDigitalObjects() != null
                    && !this.getRightDigitalObjects().isEmpty()
                    && !this.getRightDigitalObjects().equalsIgnoreCase(AjaxConversionOptionsConstants.NO_SELECTED)) {
                profile.setRightsOfDigitalObjects(this.getRightDigitalObjects());
                profile.setRightsOfDigitalObjectsText(this.recoverRightsStatementText(this.getRightDigitalObjects()));
                profile.setRightsOfDigitalDescription(this.getRightDigitalDescription());
                profile.setRightsOfDigitalHolder(this.getRightDigitalHolder());
            } else {
                profile.setRightsOfDigitalObjects("");
                profile.setRightsOfDigitalObjectsText("");
                profile.setRightsOfDigitalDescription("");
                profile.setRightsOfDigitalHolder("");
            }
        }
        else {
            // Rights for digital objects.
            profile.setRightsOfDigitalObjects("");
            profile.setRightsOfDigitalObjectsText("");
            profile.setRightsOfDigitalDescription("");
            profile.setRightsOfDigitalHolder("");
        }

        // Rights for EAD data.
        if (this.getRightEadData() != null
                && !this.getRightEadData().isEmpty()
                && !this.getRightEadData().equalsIgnoreCase(AjaxConversionOptionsConstants.NO_SELECTED)) {

            RightsInformation selectedRightsInformation = DAOFactory.instance().getRightsInformationDAO().getRightsInformation(Integer.parseInt(this.getRightEadData()));
            profile.setRightsInformationOfEADData(selectedRightsInformation);
            profile.setRightsInformationOfEADDataId(selectedRightsInformation.getId());

            profile.setRightsOfEADData(selectedRightsInformation.getLink());
            profile.setRightsOfEADDataText(selectedRightsInformation.getRightsName());
            profile.setRightsOfEADDescription(this.getRightEadDescription());
            profile.setRightsOfEADHolder(this.getRightEadHolder());
        } else {
            profile.setRightsOfEADData("");
            profile.setRightsOfEADDataText("");
            profile.setRightsOfEADDescription("");
            profile.setRightsOfEADHolder("");
        }

        if (getDefaultXslFile() != null && !getDefaultXslFile().equals("-1")) {
            profile.setXslUploadId(Long.parseLong(getDefaultXslFile()));
        }

        profile.setSourceOfIdentifiers(sourceOfIdentifiers);
        if (sourceOfFondsTitle.equals(IngestionprofilesAction.OPTION_TITLESTMT_TITLEPROPER)) {
            profile.setUseArchdescUnittitle(Boolean.FALSE);
        } else {
            profile.setUseArchdescUnittitle(Boolean.TRUE);
        }
        profile.setEuropeanaDataProvider(textDataProvider);
        profile.setEuropeanaDataProviderFromFile(Boolean.parseBoolean(dataProviderCheck));
        if (europeanaDaoType == null || europeanaDaoType.isEmpty()) {
            profile.setEuropeanaDaoType(0);
        } else {
            profile.setEuropeanaDaoType(Integer.parseInt(europeanaDaoType));
        }
        profile.setEuropeanaDaoTypeFromFile(Boolean.parseBoolean(europeanaDaoTypeCheck));
        StringBuilder langTemp = new StringBuilder("");
        for (int i = 0; i < languageSelectionMaterial.size(); i++) {
            langTemp.append(languageSelectionMaterial.get(i));
            if (i < languageSelectionMaterial.size() - 1) {
                langTemp.append(" ");
            }
        }
        profile.setEuropeanaLanguagesMaterial(langTemp.toString());
        profile.setEuropeanaLanguagesMaterialFromFile(Boolean.parseBoolean(languageMaterialCheck));
        profile.setEuropeanaLanguageMaterialDescriptionSame(Boolean.parseBoolean(languageDescriptionSameAsMaterialCheck));
        if (profile.getEuropeanaLanguageMaterialDescriptionSame()) {
            if (!languageSelectionMaterial.isEmpty()) {
                profile.setEuropeanaLanguageDescription(languageSelectionMaterial.get(0));
            }
        } else {
            profile.setEuropeanaLanguageDescription(languageSelectionDescription);
        }
        profile.setEuropeanaLanguageDescriptionFromFile(Boolean.parseBoolean(languageDescriptionCheck));
        profile.setEuropeanaLicenseFromFile(Boolean.parseBoolean(licenseCheck));
        profile.setEuropeanaLicense(license);
        if (IngestionprofilesAction.EUROPEANA.equals(this.license)) {
            profile.setEuropeanaLicenseDetails(europeanaLicense);
        } else if (IngestionprofilesAction.CREATIVECOMMONS.equals(this.license)) {
            profile.setEuropeanaLicenseDetails(cc_js_result_uri);
        } else if (IngestionprofilesAction.CREATIVECOMMONS_CC0.equals(this.license)) {
            profile.setEuropeanaLicenseDetails("http://creativecommons.org/publicdomain/zero/1.0/");
        } else if (IngestionprofilesAction.CREATIVECOMMONS_CPDM.equals(this.license)) {
            profile.setEuropeanaLicenseDetails("http://creativecommons.org/publicdomain/mark/1.0/");
        } else {
            profile.setEuropeanaLicenseDetails("http://rightsstatements.org/vocab/NoC-NC/1.0/");
        }
        profile.setEuropeanaAddRights(licenseAdditionalInformation);
        profile.setEuropeanaInheritUnittitle(Boolean.parseBoolean(inheritUnittitle));

        if (profilelist.equals("-1")) {
            profile = profileDAO.store(profile);
            this.lastSelection = profile.getId().toString();
            addActionMessage(getText("ingestionprofiles.profilessaved"));
        } else {
            profileDAO.update(profile);
            this.lastSelection = this.profilelist;
            addActionMessage(getText("ingestionprofiles.profilesupdated"));
        }
        return SUCCESS;
    }

    /**
     * Method to recover the rights statement text from the rights statement URL
     * selected.
     *
     * @param option_default_rights rights statement URL selected.
     *
     * @return Rights statement text.
     */
    private String recoverRightsStatementText(String option_default_rights) {
        String option_default_rights_text = null;

        // Check the URL selected
        if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution.
            option_default_rights_text = getText("content.message.rights.creative.attribution");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATES.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, No Derivatives.
            option_default_rights_text = getText("content.message.rights.creative.attribution.no.derivates");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NON_COMERCIAL.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_NO_DERIVATES.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial, No Derivatives.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial.no.derivates");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_SHARE.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial, ShareAlike.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial.sharealike");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_SHARE.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, ShareAlike .
            option_default_rights_text = getText("content.message.rights.creative.attribution.sharealike");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_CC0_PUBLIC.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons CC0 Public Domain Dedication .
            option_default_rights_text = getText("content.message.rights.creative.public.domain");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT.equalsIgnoreCase(option_default_rights)) {
            // Europeana In Copyright
            option_default_rights_text = getText("ead2ese.content.license.europeana.incopyright");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT_EU_ORPHAN_WORK.equalsIgnoreCase(option_default_rights)) {
            // Europeana In Copyright, EU Orphan Works
            option_default_rights_text = getText("ead2ese.content.license.europeana.incopyright.euorphan");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT_EDUCATIONAL_USE_ONLY.equalsIgnoreCase(option_default_rights)) {
            // Europeana In Copyright, educational use only
            option_default_rights_text = getText("ead2ese.content.license.europeana.incopyright.eduuse");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_NO_COPYRIGHT_NONCOMMERCIAL_USE_ONLY.equalsIgnoreCase(option_default_rights)) {
            // Europeana No Copyright, non-commercial use only
            option_default_rights_text = getText("ead2ese.content.license.europeana.nocopyright.noncommercial");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_NO_COPYRIGHT_NONCOMMERCIAL_USE_ONLY.equalsIgnoreCase(option_default_rights)) {
            // Europeana No Copyright, other known legal restrictions
            option_default_rights_text = getText("ead2ese.content.license.europeana.nocopyright.otherlegal");
        } else if (AjaxConversionOptionsConstants.EUROPEANA_COPYRIGHT_NOT_EVALUATED.equalsIgnoreCase(option_default_rights)) {
            // Europeana, copyright not evaluated
            option_default_rights_text = getText("ead2ese.content.license.europeana.copyrightnotevaluated");
        } else if (AjaxConversionOptionsConstants.PUBLIC_DOMAIN_MARK.equalsIgnoreCase(option_default_rights)) {
            // Public Domain Mark.
            option_default_rights_text = getText("content.message.rights.public.domain");
        } else {
            // unknown license
            option_default_rights_text = getText("content.message.rights.unknown");
        }

        return option_default_rights_text;
    }

    public String addIngestionprofile() throws Exception {
        setUp();

        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        List<Ingestionprofile> queryResult = profileDAO.getIngestionprofiles(getAiId());
        if (queryResult != null && !queryResult.isEmpty()) {
            for (Ingestionprofile entry : queryResult) {
                ingestionprofiles.add(new SelectItem(Long.toString(entry.getId()), entry.getNameProfile()));
            }
        } else {
            uploadedFileActions.clear();
            uploadedFileActions.add(new SelectItem("1", getText("ingestionprofiles.upload.convertValidatePublish")));
            uploadedFileActions.add(new SelectItem("2", getText("ingestionprofiles.upload.convertValidatePublishEuropeana")));
            uploadedFileActions.add(new SelectItem("3", getText("ingestionprofiles.upload.convert")));
            uploadedFileActions.add(new SelectItem("4", getText("ingestionprofiles.upload.validate")));
            uploadedFileActions.add(new SelectItem("0", getText("ingestionprofiles.upload.nothing")));

        }
        ingestionprofiles.add(new SelectItem("-1", ""));

        profilelist = "-1";
        profileName = "";
        associatedFiletype = convertToString(XmlType.EAD_FA.getIdentifier());
        uploadedFileAction = "1";
        existingFileAction = "1";
        noEadidAction = "0";
        daoType = "0";
        daoTypeCheck = Boolean.toString(true);
        extractEacFromEad3 = Boolean.toString(false);
        // Rights for digital objects.
        this.setRightDigitalObjects(AjaxConversionOptionsConstants.NO_SELECTED);
        this.setRightDigitalDescription("");
        this.setRightDigitalHolder("");
        // Rights for EAD data.
        this.setRightEadData(AjaxConversionOptionsConstants.NO_SELECTED);
        this.setRightEadDescription("");
        this.setRightEadHolder("");

        sourceOfIdentifiers = IngestionprofilesAction.OPTION_UNITID;
        sourceOfFondsTitle = IngestionprofilesAction.OPTION_ARCHDESC_UNITTITLE;
        textDataProvider = getAiname();
        dataProviderCheck = Boolean.toString(true);
        europeanaDaoType = "";
        europeanaDaoTypeCheck = Boolean.toString(true);
        languageSelectionMaterial = new ArrayList<>();
        languageMaterialCheck = Boolean.toString(true);
        languageDescriptionSameAsMaterialCheck = Boolean.toString(true);
        languageSelectionDescription = "";
        languageDescriptionCheck = Boolean.toString(true);
        licenseCheck = Boolean.toString(true);
        license = EUROPEANA;
        europeanaLicense = "";
        licenseAdditionalInformation = "";
        inheritUnittitle = Boolean.toString(false);

        this.lastSelection = this.profilelist;

        return SUCCESS;
    }

    public String cancel() throws Exception {
        return SUCCESS;
    }

    private void setUp() {
        //basic preferences
        associatedFiletypes.add(new SelectItem(XmlType.EAD_FA.getIdentifier(), getText("content.message.ead2002.fa")));
// HIDE EAD3
//        associatedFiletypes.add(new SelectItem(XmlType.EAD_3.getIdentifier(), getText("content.message.fa.ead3")));
        associatedFiletypes.add(new SelectItem(XmlType.EAD_HG.getIdentifier(), getText("content.message.hg")));
        associatedFiletypes.add(new SelectItem(XmlType.EAD_SG.getIdentifier(), getText("content.message.sg")));
        associatedFiletypes.add(new SelectItem(XmlType.EAC_CPF.getIdentifier(), getText("content.message.ec")));
        uploadedFileActions.add(new SelectItem("1", getText("ingestionprofiles.upload.convertValidatePublish")));
        if (convertToString(XmlType.EAD_FA.getIdentifier()).equals(associatedFiletype)) {
            uploadedFileActions.add(new SelectItem("2", getText("ingestionprofiles.upload.convertValidatePublishEuropeana")));
        }
        uploadedFileActions.add(new SelectItem("3", getText("ingestionprofiles.upload.convert")));
        uploadedFileActions.add(new SelectItem("4", getText("ingestionprofiles.upload.validate")));
        uploadedFileActions.add(new SelectItem("0", getText("ingestionprofiles.upload.nothing")));
        existingFileActions.add(new SelectItem("1", getText("ingestionprofiles.existing.overwrite")));
        existingFileActions.add(new SelectItem("0", getText("ingestionprofiles.existing.keep")));
        existingFileActions.add(new SelectItem("2", getText("ingestionprofiles.existing.ask")));
        noEadidActions.add(new SelectItem("0", getText("ingestionprofiles.noeadid.remove")));
        noEadidActions.add(new SelectItem("1", getText("ingestionprofiles.noeadid.addLater")));
        daoTypes.add(new SelectItem("1", getText("ingestionprofiles.dao.text")));
        daoTypes.add(new SelectItem("2", getText("ingestionprofiles.dao.image")));
        daoTypes.add(new SelectItem("3", getText("ingestionprofiles.dao.sound")));
        daoTypes.add(new SelectItem("4", getText("ingestionprofiles.dao.video")));
        daoTypes.add(new SelectItem("5", getText("ingestionprofiles.dao.3D")));
        daoTypes.add(new SelectItem("0", getText("ingestionprofiles.dao.unspecified")));

        // Set of available permissions.
        Set<SelectItem> rightsSetForDaos = this.addRightsOptions(true);
        Set<SelectItem> rightsSetForData = this.addRightsOptions(false);
        // Rights for digital objects.
        this.getRightsDigitalObjects().addAll(rightsSetForDaos);
        // Rights for EAD data.
        this.getRightsEadData().addAll(rightsSetForData);

        Integer aiId = null;
        if (SecurityContext.get().getSelectedInstitution() != null) {
            aiId = SecurityContext.get().getSelectedInstitution().getId();
        }
        if (aiId != null) {
            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
            this.setAiRightsInformation(archivalInstitution.getRightsInformation());
            if (this.defaultRightsInformation == null){
                this.defaultRightsInformation = archivalInstitution.getRightsInformation();
            }
        }


        //Europeana preferences
        sourceOfIdentifiersSet.add(new SelectItem(IngestionprofilesAction.OPTION_UNITID, this.getText("ead2ese.label.id.unitid").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        sourceOfIdentifiersSet.add(new SelectItem(IngestionprofilesAction.OPTION_CID, this.getText("ead2ese.label.id.c").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        sourceOfFondsTitleSet.add(new SelectItem(IngestionprofilesAction.OPTION_ARCHDESC_UNITTITLE, this.getText("ead2ese.label.fondstitle.archdescUnittitle").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        sourceOfFondsTitleSet.add(new SelectItem(IngestionprofilesAction.OPTION_TITLESTMT_TITLEPROPER, this.getText("ead2ese.label.fondstitle.titlestmtTitleproper").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        String[] isoLanguages = Locale.getISOLanguages();
        for (String language : isoLanguages) {
            String languageDescription = new Locale(language).getDisplayLanguage(Locale.ENGLISH);
            languages.add(new SelectItem(language, languageDescription));
        }
        typeSet.add(new SelectItem("", getText("ead2ese.content.selectone")));
        typeSet.add(new SelectItem("1", getText("ingestionprofiles.dao.text")));
        typeSet.add(new SelectItem("2", getText("ingestionprofiles.dao.image")));
        typeSet.add(new SelectItem("3", getText("ingestionprofiles.dao.sound")));
        typeSet.add(new SelectItem("4", getText("ingestionprofiles.dao.video")));
        typeSet.add(new SelectItem("5", getText("ingestionprofiles.dao.3D")));
        yesNoSet.add(new SelectItem("true", getText("ead2ese.content.yes")));
        yesNoSet.add(new SelectItem("false", getText("ead2ese.content.no")));
        inheritLanguageSet.add(new SelectItem("1", getText("ead2ese.content.yes")));
        inheritLanguageSet.add(new SelectItem("0", getText("ead2ese.content.no")));
        inheritLanguageSet.add(new SelectItem("2", getText("ead2ese.label.language.select")));
        licenseSet.add(new SelectItem(EUROPEANA, getText("ead2ese.content.license.europeana")));
        licenseSet.add(new SelectItem(CREATIVECOMMONS, getText("ead2ese.content.license.creativecommons")));
        licenseSet.add(new SelectItem(CREATIVECOMMONS_CC0, getText("ead2ese.content.license.creativecommons.cc0")));
        licenseSet.add(new SelectItem(CREATIVECOMMONS_CPDM, getText("ead2ese.content.license.creativecommons.publicdomain")));
        europeanaLicenseSet.add(new SelectItem("", getText("ead2ese.content.selectone")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/InC/1.0/", getText("ead2ese.content.license.europeana.incopyright")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/InC-EDU/1.0/", getText("ead2ese.content.license.europeana.incopyright.eduuse")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/InC-OW-EU/1.0/", getText("ead2ese.content.license.europeana.incopyright.euorphan")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/NoC-NC/1.0/", getText("ead2ese.content.license.europeana.nocopyright.noncommercial")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/NoC-OKLR/1.0/", getText("ead2ese.content.license.europeana.nocopyright.otherlegal")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/CNE/1.0/", getText("ead2ese.content.license.europeana.copyrightnotevaluated")));

        if (DAOFactory.instance().getXslUploadDAO().hasXslUpload(getAiId())) {
            List<XslUpload> xslUploads = DAOFactory.instance().getXslUploadDAO().getXslUploads(getAiId());
            getXslFiles().add(new SelectItem("-1", "DEFAULT"));
            for (XslUpload xslUpload : xslUploads) {
                getXslFiles().add(new SelectItem(xslUpload.getId(), xslUpload.getReadableName()));
            }
        }
    }

    /**
     * Method to create the set with all the available rights statements for EAD
     * data.
     *
     * @return Set with all the available rights statements for EAD data.
     */
    private Set<SelectItem> addRightsOptions(boolean addDefault) {
        Set<SelectItem> rightsSet = new LinkedHashSet<>();
        if (addDefault) {
            rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.NO_SELECTED, "---"));
        }

        List<RightsInformation> rightsInformations = DAOFactory.instance().getRightsInformationDAO().getRightsInformations();
        rightsInformations.forEach((rightsInformation) -> {
            if (!(rightsInformation.getAbbreviation().equals(IN_COPYRIGHT_EU_ORPHAN_WORK)
                    || rightsInformation.getAbbreviation().equals(NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS)
                    /*|| rightsInformation.getAbbreviation().equals(COPYRIGHT_NOT_EVALUATED)*/)) {
                SelectItem selectItem = new SelectItem(rightsInformation.getId(), rightsInformation.getRightsName());
                rightsSet.add(selectItem);
            }
        });

        return rightsSet;
    }

    public String showIngestionProfiles() {
        return Action.SUCCESS;
    }

    public Set<SelectItem> getIngestionprofiles() {
        return ingestionprofiles;
    }

    public void setIngestionprofiles(Set<SelectItem> ingestionprofiles) {
        this.ingestionprofiles = ingestionprofiles;
    }

    public String getProfilelist() {
        return profilelist;
    }

    public void setProfilelist(String profilelist) {
        this.profilelist = profilelist;
    }

    public Set<SelectItem> getAssociatedFiletypes() {
        return associatedFiletypes;
    }

    public void setAssociatedFiletypes(Set<SelectItem> associatedFiletypes) {
        this.associatedFiletypes = associatedFiletypes;
    }

    public Set<SelectItem> getUploadedFileActions() {
        return uploadedFileActions;
    }

    public void setUploadedFileActions(Set<SelectItem> uploadedFileActions) {
        this.uploadedFileActions = uploadedFileActions;
    }

    public Set<SelectItem> getExistingFileActions() {
        return existingFileActions;
    }

    public void setExistingFileActions(Set<SelectItem> existingFileActions) {
        this.existingFileActions = existingFileActions;
    }

    public Set<SelectItem> getNoEadidActions() {
        return noEadidActions;
    }

    public void setNoEadidActions(Set<SelectItem> noEadidActions) {
        this.noEadidActions = noEadidActions;
    }

    public Set<SelectItem> getDaoTypes() {
        return daoTypes;
    }

    public void setDaoTypes(Set<SelectItem> daoTypes) {
        this.daoTypes = daoTypes;
    }

    /**
     * @return the rightsDigitalObjects
     */
    public Set<SelectItem> getRightsDigitalObjects() {
        return this.rightsDigitalObjects;
    }

    /**
     * @param rightsDigitalObjects the rightsDigitalObjects to set
     */
    public void setRightsDigitalObjects(Set<SelectItem> rightsDigitalObjects) {
        this.rightsDigitalObjects = rightsDigitalObjects;
    }

    public void setRightsInformationEadData(RightsInformation rightsInformationEadData) {
        this.rightsInformationEadData = rightsInformationEadData;
    }

    public RightsInformation getRightsInformationEadData() {
        return rightsInformationEadData;
    }

    public void setDefaultRightsInformation(RightsInformation defaultRightsInformation) {
        this.defaultRightsInformation = defaultRightsInformation;
    }

    public RightsInformation getDefaultRightsInformation() {
        return defaultRightsInformation;
    }

    /**
     * @return the rightsEadData
     */
    public Set<SelectItem> getRightsEadData() {
        return this.rightsEadData;
    }

    /**
     * @param rightsEadData the rightsEadData to set
     */
    public void setRightsEadData(Set<SelectItem> rightsEadData) {
        this.rightsEadData = rightsEadData;
    }

    public void setAiRightsInformation(RightsInformation aiRightsInformation) {
        this.aiRightsInformation = aiRightsInformation;
    }

    public RightsInformation getAiRightsInformation() {
        return aiRightsInformation;
    }

    /**
     * @return the sourceOfIdentifiersSet
     */
    public Set<SelectItem> getSourceOfIdentifiersSet() {
        return this.sourceOfIdentifiersSet;
    }

    /**
     * @param sourceOfIdentifiersSet the sourceOfIdentifiersSet to set
     */
    public void setSourceOfIdentifiersSet(Set<SelectItem> sourceOfIdentifiersSet) {
        this.sourceOfIdentifiersSet = sourceOfIdentifiersSet;
    }

    public Set<SelectItem> getSourceOfFondsTitleSet() {
        return sourceOfFondsTitleSet;
    }

    public void setSourceOfFondsTitleSet(Set<SelectItem> sourceOfFondsTitleSet) {
        this.sourceOfFondsTitleSet = sourceOfFondsTitleSet;
    }

    public Set<SelectItem> getTypeSet() {
        return typeSet;
    }

    public void setTypeSet(Set<SelectItem> typeSet) {
        this.typeSet = typeSet;
    }

    public Set<SelectItem> getYesNoSet() {
        return yesNoSet;
    }

    public void setYesNoSet(Set<SelectItem> yesNoSet) {
        this.yesNoSet = yesNoSet;
    }

    public Set<SelectItem> getInheritLanguageSet() {
        return inheritLanguageSet;
    }

    public void setInheritLanguageSet(Set<SelectItem> inheritLanguageSet) {
        this.inheritLanguageSet = inheritLanguageSet;
    }

    public Set<SelectItem> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<SelectItem> languages) {
        this.languages = languages;
    }

    public Set<SelectItem> getLicenseSet() {
        return licenseSet;
    }

    public void setLicenseSet(Set<SelectItem> licenseSet) {
        this.licenseSet = licenseSet;
    }

    public Set<SelectItem> getEuropeanaLicenseSet() {
        return europeanaLicenseSet;
    }

    public void setEuropeanaLicenseSet(Set<SelectItem> europeanaLicenseSet) {
        this.europeanaLicenseSet = europeanaLicenseSet;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAssociatedFiletype() {
        return associatedFiletype;
    }

    public void setAssociatedFiletype(String associatedFiletype) {
        this.associatedFiletype = associatedFiletype;
    }

    public String getUploadedFileAction() {
        return uploadedFileAction;
    }

    public void setUploadedFileAction(String uploadedFileAction) {
        this.uploadedFileAction = uploadedFileAction;
    }

    public String getExistingFileAction() {
        return existingFileAction;
    }

    public void setExistingFileAction(String existingFileAction) {
        this.existingFileAction = existingFileAction;
    }

    public String getNoEadidAction() {
        return noEadidAction;
    }

    public void setNoEadidAction(String noEadidAction) {
        this.noEadidAction = noEadidAction;
    }

    public String getDaoType() {
        return daoType;
    }

    public void setDaoType(String daoType) {
        this.daoType = daoType;
    }

    public String getDaoTypeCheck() {
        return daoTypeCheck;
    }

    public void setDaoTypeCheck(String daoTypeCheck) {
        this.daoTypeCheck = daoTypeCheck;
    }

    public String getExtractEacFromEad3() {
        return extractEacFromEad3;
    }

    public void setExtractEacFromEad3(String extractEacFromEad3) {
        this.extractEacFromEad3 = extractEacFromEad3;
    }

    /**
     * @return the rightDigitalObjects
     */
    public String getRightDigitalObjects() {
        return this.rightDigitalObjects;
    }

    /**
     * @param rightDigitalObjects the rightDigitalObjects to set
     */
    public void setRightDigitalObjects(String rightDigitalObjects) {
        this.rightDigitalObjects = rightDigitalObjects;
    }

    /**
     * @return the rightDigitalDescription
     */
    public String getRightDigitalDescription() {
        return this.rightDigitalDescription;
    }

    /**
     * @param rightDigitalDescription the rightDigitalDescription to set
     */
    public void setRightDigitalDescription(String rightDigitalDescription) {
        this.rightDigitalDescription = rightDigitalDescription;
    }

    /**
     * @return the rightDigitalHolder
     */
    public String getRightDigitalHolder() {
        return this.rightDigitalHolder;
    }

    /**
     * @param rightDigitalHolder the rightDigitalHolder to set
     */
    public void setRightDigitalHolder(String rightDigitalHolder) {
        this.rightDigitalHolder = rightDigitalHolder;
    }

    /**
     * @return the rightEadData
     */
    public String getRightEadData() {
        return this.rightEadData;
    }

    /**
     * @param rightEadData the rightEadData to set
     */
    public void setRightEadData(String rightEadData) {
        this.rightEadData = rightEadData;
    }

    /**
     * @return the rightEadDescription
     */
    public String getRightEadDescription() {
        return this.rightEadDescription;
    }

    /**
     * @param rightEadDescription the rightEadDescription to set
     */
    public void setRightEadDescription(String rightEadDescription) {
        this.rightEadDescription = rightEadDescription;
    }

    /**
     * @return the rightEadHolder
     */
    public String getRightEadHolder() {
        return this.rightEadHolder;
    }

    /**
     * @param rightEadHolder the rightEadHolder to set
     */
    public void setRightEadHolder(String rightEadHolder) {
        this.rightEadHolder = rightEadHolder;
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

    public String getSourceOfFondsTitle() {
        return sourceOfFondsTitle;
    }

    public void setSourceOfFondsTitle(String sourceOfFondsTitle) {
        this.sourceOfFondsTitle = sourceOfFondsTitle;
    }

    public String getTextDataProvider() {
        return textDataProvider;
    }

    public void setTextDataProvider(String textDataProvider) {
        this.textDataProvider = textDataProvider;
    }

    public String getDataProviderCheck() {
        return dataProviderCheck;
    }

    public void setDataProviderCheck(String dataProviderCheck) {
        this.dataProviderCheck = dataProviderCheck;
    }

    public String getEuropeanaDaoType() {
        return europeanaDaoType;
    }

    public void setEuropeanaDaoType(String europeanaDaoType) {
        this.europeanaDaoType = europeanaDaoType;
    }

    public String getEuropeanaDaoTypeCheck() {
        return europeanaDaoTypeCheck;
    }

    public void setEuropeanaDaoTypeCheck(String europeanaDaoTypeCheck) {
        this.europeanaDaoTypeCheck = europeanaDaoTypeCheck;
    }

    public List<String> getLanguageSelectionMaterial() {
        return languageSelectionMaterial;
    }

    public void setLanguageSelectionMaterial(List<String> languageSelectionMaterial) {
        this.languageSelectionMaterial = languageSelectionMaterial;
    }

    public String getLanguageMaterialCheck() {
        return languageMaterialCheck;
    }

    public void setLanguageMaterialCheck(String languageMaterialCheck) {
        this.languageMaterialCheck = languageMaterialCheck;
    }

    public String getLanguageSelectionDescription() {
        return languageSelectionDescription;
    }

    public void setLanguageSelectionDescription(String languageSelectionDescription) {
        this.languageSelectionDescription = languageSelectionDescription;
    }

    public String getLanguageDescriptionCheck() {
        return languageDescriptionCheck;
    }

    public void setLanguageDescriptionCheck(String languageDescriptionCheck) {
        this.languageDescriptionCheck = languageDescriptionCheck;
    }

    public String getLanguageDescriptionSameAsMaterialCheck() {
        return languageDescriptionSameAsMaterialCheck;
    }

    public void setLanguageDescriptionSameAsMaterialCheck(String languageDescriptionSameAsMaterialCheck) {
        this.languageDescriptionSameAsMaterialCheck = languageDescriptionSameAsMaterialCheck;
    }

    public String getLicenseCheck() {
        return licenseCheck;
    }

    public void setLicenseCheck(String licenseCheck) {
        this.licenseCheck = licenseCheck;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getEuropeanaLicense() {
        return europeanaLicense;
    }

    public void setEuropeanaLicense(String europeanaLicense) {
        this.europeanaLicense = europeanaLicense;
    }

    public String getCc_js_result_uri() {
        return cc_js_result_uri;
    }

    public void setCc_js_result_uri(String cc_js_result_uri) {
        this.cc_js_result_uri = cc_js_result_uri;
    }

    public String getLicenseAdditionalInformation() {
        return licenseAdditionalInformation;
    }

    public void setLicenseAdditionalInformation(String licenseAdditionalInformation) {
        this.licenseAdditionalInformation = licenseAdditionalInformation;
    }

    public String getInheritUnittitle() {
        return inheritUnittitle;
    }

    public void setInheritUnittitle(String inheritUnittitle) {
        this.inheritUnittitle = inheritUnittitle;
    }

    public String getLastSelection() {
        return lastSelection;
    }

    public void setLastSelection(String lastSelection) {
        this.lastSelection = lastSelection;
    }

    public Set<SelectItem> getXslFiles() {
        return xslFiles;
    }

    public void setXslFiles(Set<SelectItem> xslFiles) {
        this.xslFiles = xslFiles;
    }

    public String getDefaultXslFile() {
        return defaultXslFile;
    }

    public void setDefaultXslFile(String defaultXslFile) {
        this.defaultXslFile = defaultXslFile;
    }

    private String convertToString(int identifier) {
        return identifier + "";
    }
}
