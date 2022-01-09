package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.persistence.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscapeUtils;
import eu.apenet.dashboard.manual.APEnetEAGDashboard;
import eu.apenet.dashboard.manual.eag.utils.CreateEAG2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dashboard.manual.eag.utils.ParseEag2012Errors;
import eu.apenet.dashboard.manual.eag.utils.parseJsonObj.AccessAndServicesJsonObjToEag2012;
import eu.apenet.dashboard.manual.eag.utils.parseJsonObj.ContactJsonObjToEag2012;
import eu.apenet.dashboard.manual.eag.utils.parseJsonObj.ControlJsonObjToEag2012;
import eu.apenet.dashboard.manual.eag.utils.parseJsonObj.DescriptionJsonObjToEag2012;
import eu.apenet.dashboard.manual.eag.utils.parseJsonObj.IdentityJsonObjToEag2012;
import eu.apenet.dashboard.manual.eag.utils.parseJsonObj.RelationJsonObjToEag2012;
import eu.apenet.dashboard.manual.eag.utils.parseJsonObj.YourInstitutionJsonObjToEag2012;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.eag.EagService;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.namespace.EagNamespaceMapper;
import eu.apenet.dpt.utils.util.LanguageIsoList;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CoordinatesDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Class is used to manage and store the {@link EAG2012} EAG2012. To be able to
 * realize those actions
 */
public class WebFormEAG2012Action extends AbstractInstitutionAction {

    /**
     * Serializable.
     */
    private static final long serialVersionUID = 732801399037503323L;

    private final Logger log = Logger.getLogger(getClass());

    /**
     * This action is used to render the EAG2012 webform.
     */
    private Map<String, String> yesNoMap = new HashMap<String, String>();
    private Map<String, String> noneYesNoMap = new LinkedHashMap<String, String>();
    private Map<String, String> repositoryRoleMap = new LinkedHashMap<String, String>();
    private Map<String, String> typeOfInstitutionMap = new LinkedHashMap<String, String>();
    private Map<String, String> continentOfInstitutionMap = new LinkedHashMap<String, String>();
    private Map<String, String> photographMap = new LinkedHashMap<String, String>();
    private Map<String, String> typeYourRelationMap = new LinkedHashMap<String, String>();
    private Map<String, String> typeTheRelationMap = new LinkedHashMap<String, String>();
    private Map<String, String> languageISOMap = new LinkedHashMap<String, String>();
    private Map<String, String> scriptMap = new LinkedHashMap<String, String>();

    private List<String> warnings = new ArrayList<String>();

	// Attributes.
    // Common to various tabs.
    private String personResponsibleForDescription;
    private String idOfInstitution;
    private String nameOfInstitution;
    private String nameOfInstitutionLanguage;
    private String parallelNameOfInstitution;
    private String parallelNameOfInstitutionLanguage;

    private String CountryOfTheInstitution;
    private String repositoridCountryCode;
    private String otherRepositorIdValue;
    private String recordIdValue;
    private String autformValue;			// Will be an ordered list.
    private String autformLang;				// Will be an ordered list.
    private String parformValue;			// Will be an ordered list.
    private String parformLang;				// Will be an ordered list.

	// Your institution tab.
    // Identity tab.
    private String nonpreformValue;			// Will be an ordered list.
    private String nonpreformLang;			// Will be an ordered list.
    private String fromDateStandardDate;
    private String toDateStandardDate;
    private String repositoryTypeValue;		// Will be an ordered list.

    private String eagPath;

    private static final String IN_COPYRIGHT_EU_ORPHAN_WORK = "InC-EU-OW";
    private static final String NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS = "NoC-OKLR";
    private static final String COPYRIGHT_NOT_EVALUATED = "CNE";
    public static final String CC_0 = "CC0";

    private List<SelectItem> rightsList = new ArrayList<>();

    /**
     * @return {@link String} eagPath to get
     */
    public String getEagPath() {
        return this.eagPath;
    }

    /**
     * @return {
     * @linkMap<String,String>} languageList to get
     */
    public Map<String, String> getLanguageList() {
        Map<String, String> languages = new LinkedHashMap<String, String>();
        languages.put(Eag2012.OPTION_NONE, "---");
        List<String> languagesList = LanguageIsoList.getLanguageIsoList();
        String[] isoLanguages = languagesList.toArray(new String[]{});
        for (String language : isoLanguages) {
            String languageDescription = LanguageIsoList.getIsoCode(language);
            languages.put(languageDescription, language);
        }
        return languages;
    }

    /**
     *
     * @return Map<String,String> YesNoList to get
     */
    public Map<String, String> getYesNoList() {
        this.getYesNoMap().put(Eag2012.OPTION_YES, getText("eag2012.commons.yes"));
        this.getYesNoMap().put(Eag2012.OPTION_NO, getText("eag2012.commons.no"));
        return this.getYesNoMap();
    }

    /**
     *
     * @return Map<String,String> NoneYesNoList to get
     */
    public Map<String, String> getNoneYesNoList() {
        this.getNoneYesNoMap().put(Eag2012.OPTION_NONE, "---");
        this.getNoneYesNoMap().put(Eag2012.OPTION_YES, getText("eag2012.commons.yes"));
        this.getNoneYesNoMap().put(Eag2012.OPTION_NO, getText("eag2012.commons.no"));
        return this.getNoneYesNoMap();
    }

    /**
     * @return Map<String,String> RepositoryRoleList to get
     */
    public Map<String, String> getRepositoryRoleList() {
        this.getRepositoryRoleMap().put(Eag2012.OPTION_NONE, "---");
        this.getRepositoryRoleMap().put(Eag2012.OPTION_ROLE_HEADQUARTERS, getText("eag2012.options.role.headquarters"));
        this.getRepositoryRoleMap().put(Eag2012.OPTION_ROLE_BRANCH, getText("eag2012.options.role.branch"));
        this.getRepositoryRoleMap().put(Eag2012.OPTION_ROLE_INTERIM, getText("eag2012.options.role.interimArchive"));
        return this.getRepositoryRoleMap();

    }

    /**
     * @return Map<String,String> TypeOfInstitutionList to get
     */
    public Map<String, String> getTypeOfInstitutionList() {
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_NATIONAL, getText("eag2012.options.institutionType.nationalArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_REGIONAL, getText("eag2012.options.institutionType.regionalArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_COUNTY, getText("eag2012.options.institutionType.countyArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_MUNICIPAL, getText("eag2012.options.institutionType.municipalArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_SPECIALISED, getText("eag2012.options.institutionType.specialisedArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_PRIVATE, getText("eag2012.options.institutionType.privateArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_CHURCH, getText("eag2012.options.institutionType.churchArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_BUSINESS, getText("eag2012.options.institutionType.businessArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_UNIVERSITY, getText("eag2012.options.institutionType.universityArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_MEDIA, getText("eag2012.options.institutionType.mediaArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_POLITICAL, getText("eag2012.options.institutionType.politicalArchives"));
        this.getTypeOfInstitutionMap().put(Eag2012.OPTION_CULTURAL, getText("eag2012.options.institutionType.culturalArchives"));
        return this.getTypeOfInstitutionMap();
    }

    /**
     * @return Map<String,String> ContinentOfTheInstitutionList to get
     */
    public Map<String, String> getContinentOfTheInstitutionList() {
        this.getContinentOfInstitutionMap().put(Eag2012.OPTION_EUROPE, getText("eag2012.options.continent.europe"));
        this.getContinentOfInstitutionMap().put(Eag2012.OPTION_AFRICA, getText("eag2012.options.continent.africa"));
        this.getContinentOfInstitutionMap().put(Eag2012.OPTION_ANTARCTICA, getText("eag2012.options.continent.antarctica"));
        this.getContinentOfInstitutionMap().put(Eag2012.OPTION_ASIA, getText("eag2012.options.continent.asia"));
        this.getContinentOfInstitutionMap().put(Eag2012.OPTION_AUSTRALIA, getText("eag2012.options.continent.australia"));
        this.getContinentOfInstitutionMap().put(Eag2012.OPTION_NORTH_AMERICA, getText("eag2012.options.continent.northAmerica"));
        this.getContinentOfInstitutionMap().put(Eag2012.OPTION_SOUTH_AMERICA, getText("eag2012.options.continent.southAmerica"));

        return this.getContinentOfInstitutionMap();
    }

    /**
     * @return Map<String,String> PhotographList to get
     */
    public Map<String, String> getPhotographList() {
        this.getPhotographMap().put(Eag2012.OPTION_NONE, "---");
        this.getPhotographMap().put(Eag2012.OPTION_DEPENDING, getText("eag2012.options.photograph.depending"));
        this.getPhotographMap().put(Eag2012.OPTION_YES, getText("eag2012.commons.yes"));
        this.getPhotographMap().put(Eag2012.OPTION_WITHOUT, getText("eag2012.options.photograph.without"));
        this.getPhotographMap().put(Eag2012.OPTION_NO, getText("eag2012.commons.no"));

        return this.getPhotographMap();
    }

    /**
     * @return Map<String,String> YourRelationList to get
     */
    public Map<String, String> getTypeYourRelationList() {
        this.getTypeYourRelationMap().put(Eag2012.OPTION_CREATOR, getText("eag2012.options.typeYourRelation.creator"));
        this.getTypeYourRelationMap().put(Eag2012.OPTION_SUBJECT, getText("eag2012.options.typeYourRelation.subject"));
        this.getTypeYourRelationMap().put(Eag2012.OPTION_OTHER, getText("eag2012.options.typeYourRelation.other"));

        return this.getTypeYourRelationMap();
    }

    /**
     * @return Map<String,String> TypeTheRelationMap to get
     */
    public Map<String, String> getTypeTheRelationList() {
        this.getTypeTheRelationMap().put(Eag2012.OPTION_NONE, "---");
        this.getTypeTheRelationMap().put(Eag2012.OPTION_CHILD, getText("eag2012.options.typeTheRelation.child"));
        this.getTypeTheRelationMap().put(Eag2012.OPTION_PARENT, getText("eag2012.options.typeTheRelation.parent"));
        this.getTypeTheRelationMap().put(Eag2012.OPTION_EARLIER, getText("eag2012.options.typeTheRelation.earlier"));
        this.getTypeTheRelationMap().put(Eag2012.OPTION_LATER, getText("eag2012.options.typeTheRelation.later"));
        this.getTypeTheRelationMap().put(Eag2012.OPTION_ASSOCIATIVE, getText("eag2012.options.typeTheRelation.associative"));
        return this.getTypeTheRelationMap();
    }

    /**
     * @return Map<String,String> ScriptList to get
     */
    public Map<String, String> getScriptList() {
        this.getScriptMap().put(Eag2012.OPTION_NONE, "---");

        this.getScriptMap().put(Eag2012.OPTION_SCRIPT_ARAB, Eag2012.OPTION_SCRIPT_TEXT_ARAB);
        this.getScriptMap().put(Eag2012.OPTION_SCRIPT_ARMN, Eag2012.OPTION_SCRIPT_TEXT_ARMN);
        this.getScriptMap().put(Eag2012.OPTION_SCRIPT_CPRT, Eag2012.OPTION_SCRIPT_TEXT_CPRT);
        this.getScriptMap().put(Eag2012.OPTION_SCRIPT_CYRL, Eag2012.OPTION_SCRIPT_TEXT_CYRL);
        this.getScriptMap().put(Eag2012.OPTION_SCRIPT_GEOR, Eag2012.OPTION_SCRIPT_TEXT_GEOR);
        this.getScriptMap().put(Eag2012.OPTION_SCRIPT_GREK, Eag2012.OPTION_SCRIPT_TEXT_GREK);
        this.getScriptMap().put(Eag2012.OPTION_SCRIPT_HEBR, Eag2012.OPTION_SCRIPT_TEXT_HEBR);
        this.getScriptMap().put(Eag2012.OPTION_SCRIPT_LATN, Eag2012.OPTION_SCRIPT_TEXT_LATN);

        return this.getScriptMap();
    }

    // Getters and setters.
    /**
     * @return Map<String,String> the yesNoMap
     */
    public Map<String, String> getYesNoMap() {
        return this.yesNoMap;
    }

    /**
     *
     * @return Map<String,String> noneYesNoMap
     */
    public Map<String, String> getNoneYesNoMap() {
        return this.noneYesNoMap;
    }

    /**
     * @param yesNoSet Map<String, String> the yesNoMap to set
     */
    public void setYesNoMap(Map<String, String> yesNoMap) {
        this.yesNoMap = yesNoMap;
    }

    /**
     * @return Map<String, String> the repositoryRoleMap
     */
    public Map<String, String> getRepositoryRoleMap() {
        return this.repositoryRoleMap;
    }

    /**
     * @param repositoryRoleMap Map<String, String> the repositoryRoleMap to set
     */
    public void setRepositoryRoleMap(Map<String, String> repositoryRoleMap) {
        this.repositoryRoleMap = repositoryRoleMap;
    }

    public void setRightsList(List<SelectItem> rightsList) {
        this.rightsList = rightsList;
    }

    public List<SelectItem> getRightsList() {
        return rightsList;
    }

    /**
     * @return Map<String, String> the typeOfInstitutionMap to get
     */
    public Map<String, String> getTypeOfInstitutionMap() {
        return this.typeOfInstitutionMap;
    }

    /**
     * @param typeOfInstitutionMap Map<String, String> the typeOfInstitutionMap
     * to set
     */
    public void setTypeOfInstitutionMap(Map<String, String> typeOfInstitutionMap) {
        this.typeOfInstitutionMap = typeOfInstitutionMap;
    }

    /**
     * @return Map<String, String> the continentOfInstitutionMap to get
     */
    public Map<String, String> getContinentOfInstitutionMap() {
        return this.continentOfInstitutionMap;
    }

    /**
     * @param continentOfInstitutionMap Map<String, String> the
     * continentOfInstitutionMap to set
     */
    public void setContinentOfInstitutionMap(
            Map<String, String> continentOfInstitutionMap) {
        this.continentOfInstitutionMap = continentOfInstitutionMap;
    }

    /**
     * @return {@link String} the CountryOfTheInstitution to get
     */
    public String getCountryOfTheInstitution() {
        return CountryOfTheInstitution;
    }

    /**
     * @param countryOfTheInstitution {@link String} the CountryOfTheInstitution
     * to set
     */
    public void setCountryOfTheInstitution(String countryOfTheInstitution) {
        CountryOfTheInstitution = countryOfTheInstitution;
    }

    /**
     * @return Map<String, String> the photographMap to get
     */
    public Map<String, String> getPhotographMap() {
        return this.photographMap;
    }

    /**
     * @param photographMap Map<String, String> the photographMap to set
     */
    public void setPhotographMap(Map<String, String> photographMap) {
        this.photographMap = photographMap;
    }

    /**
     * @return Map<String, String> the typeYourRelationMap to get
     */
    public Map<String, String> getTypeYourRelationMap() {
        return this.typeYourRelationMap;
    }

    /**
     * @param typeYourRelationMap Map<String, String> the typeYourRelationMap to
     * set
     */
    public void setTypeYourRelationMap(Map<String, String> typeYourRelationMap) {
        this.typeYourRelationMap = typeYourRelationMap;
    }

    /**
     * @return Map<String, String> the typetheRelationMap to get
     */
    public Map<String, String> getTypeTheRelationMap() {
        return this.typeTheRelationMap;
    }

    /**
     * @param typetheRelationMap Map<String, String> the typeTheRelationMap to
     * set
     */
    public void setTypeTheRelationMap(Map<String, String> typeTheRelationMap) {
        this.typeTheRelationMap = typeTheRelationMap;
    }

    /**
     * @return Map<String, String> the languageISOMap to get
     */
    public Map<String, String> getLanguageISOMap() {
        return this.languageISOMap;
    }

    /**
     * @param languageISOMap Map<String, String> the languageISOMap to set
     */
    public void setLanguageISOMap(Map<String, String> languageISOMap) {
        this.languageISOMap = languageISOMap;
    }

    /**
     * @return Map<String, String> the scriptMap to get
     */
    public Map<String, String> getScriptMap() {
        return this.scriptMap;
    }

    /**
     * @param scriptMap Map<String, String> the scriptMap to set
     */
    public void setScriptMap(Map<String, String> scriptMap) {
        this.scriptMap = scriptMap;
    }

    /**
     * @return Map<String, String> the personResponsibleForDescription to get
     */
    public String getPersonResponsibleForDescription() {
        return this.personResponsibleForDescription;
    }

    /**
     * @param personResponsibleForDescription {@link String} the
     * personResponsibleForDescription to set
     */
    public void setPersonResponsibleForDescription(
            String personResponsibleForDescription) {
        this.personResponsibleForDescription = personResponsibleForDescription;
    }

    /**
     * @return {@link String} the idOfInstitution to get
     */
    public String getIdOfInstitution() {
        return this.idOfInstitution;
    }

    /**
     * @param idOfInstitution {@link String} the idOfInstitution to set
     */
    public void setIdOfInstitution(String idOfInstitution) {
        this.idOfInstitution = idOfInstitution;
    }

    /**
     * @return {@link String} the nameOfInstitution to get
     */
    public String getInitialAutformValue() {
        String aiName = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId()).getAiname();
        return loader.removeDuplicateWhiteSpaces(aiName);
    }

    /**
     * @return {@link String} the nameOfInstitution to get
     */
    public String getNameOfInstitution() {
        return this.nameOfInstitution;
    }

    /**
     * @param nameOfInstitution {@link String} the nameOfInstitution to set
     */
    public void setNameOfInstitution(String nameOfInstitution) {
        this.nameOfInstitution = nameOfInstitution;
    }

    /**
     * @return {@link String} the nameOfInstitutionLanguage to get
     */
    public String getNameOfInstitutionLanguage() {
        return this.nameOfInstitutionLanguage;
    }

    /**
     * @param nameOfInstitutionLanguage {@link String} the
     * nameOfInstitutionLanguage to set
     */
    public void setNameOfInstitutionLanguage(String nameOfInstitutionLanguage) {
        this.nameOfInstitutionLanguage = nameOfInstitutionLanguage;
    }

    /**
     * @return {@link String} the parallelNameOfInstitution to get
     */
    public String getParallelNameOfInstitution() {
        return this.parallelNameOfInstitution;
    }

    /**
     * @param parallelNameOfInstitution {@link String} the
     * parallelNameOfInstitution to set
     */
    public void setParallelNameOfInstitution(String parallelNameOfInstitution) {
        this.parallelNameOfInstitution = parallelNameOfInstitution;
    }

    /**
     * @return {@link String} the parallelNameOfInstitutionLanguage to get
     */
    public String getParallelNameOfInstitutionLanguage() {
        return this.parallelNameOfInstitutionLanguage;
    }

    /**
     * @param parallelNameOfInstitutionLanguage {@link String} the
     * parallelNameOfInstitutionLanguage to set
     */
    public void setParallelNameOfInstitutionLanguage(
            String parallelNameOfInstitutionLanguage) {
        this.parallelNameOfInstitutionLanguage = parallelNameOfInstitutionLanguage;
    }

    /**
     * @return {@link String} the repositoridCountryCode to get
     */
    public String getRepositoridCountryCode() {
        return this.repositoridCountryCode;
    }

    /**
     * @param repositoridCountryCode {@link String} the repositoridCountryCode
     * to set
     */
    public void setRepositoridCountryCode(String repositoridCountryCode) {
        this.repositoridCountryCode = repositoridCountryCode;
    }

    /**
     * @return {@link String} the otherRepositorIdValue to get
     */
    public String getOtherRepositorIdValue() {
        return this.otherRepositorIdValue;
    }

    /**
     * @param otherRepositorIdValue {@link String} the otherRepositorIdValue to
     * set
     */
    public void setOtherRepositorIdValue(String otherRepositorIdValue) {
        this.otherRepositorIdValue = otherRepositorIdValue;
    }

    /**
     * @return {@link String} the recordIdValue to get
     */
    public String getRecordIdValue() {
        return this.recordIdValue;
    }

    /**
     * @param recordIdValue {@link String} the recordIdValue to set
     */
    public void setRecordIdValue(String recordIdValue) {
        this.recordIdValue = recordIdValue;
    }

    /**
     * @return {@link String} the autformValue to get
     */
    public String getAutformValue() {
        return this.autformValue;
    }

    /**
     * @param autformValue {@link String} the autformValue to set
     */
    public void setAutformValue(String autformValue) {
        this.autformValue = autformValue;
    }

    /**
     * @return {@link String} the autformLang to get
     */
    public String getAutformLang() {
        return this.autformLang;
    }

    /**
     * @param autformLang {@link String} the autformLang to set
     */
    public void setAutformLang(String autformLang) {
        this.autformLang = autformLang;
    }

    /**
     * @return {@link String} the parformValue to get
     */
    public String getParformValue() {
        return this.parformValue;
    }

    /**
     * @param parformValue {@link String} the parformValue to set
     */
    public void setParformValue(String parformValue) {
        this.parformValue = parformValue;
    }

    /**
     * @return {@link String} the parformLang to get
     */
    public String getParformLang() {
        return this.parformLang;
    }

    /**
     * @param parformLang {@link String} the parformLang to set
     */
    public void setParformLang(String parformLang) {
        this.parformLang = parformLang;
    }

    /**
     * @return {@link String} the nonpreformValue to get
     */
    public String getNonpreformValue() {
        return this.nonpreformValue;
    }

    /**
     * @param nonpreformValue {@link String} the nonpreformValue to set
     */
    public void setNonpreformValue(String nonpreformValue) {
        this.nonpreformValue = nonpreformValue;
    }

    /**
     * @return {@link String} the nonpreformLang to get
     */
    public String getNonpreformLang() {
        return this.nonpreformLang;
    }

    /**
     * @param nonpreformLang {@link String} the nonpreformLang to set
     */
    public void setNonpreformLang(String nonpreformLang) {
        this.nonpreformLang = nonpreformLang;
    }

    /**
     * @return {@link String} the fromDateStandardDate to get
     */
    public String getFromDateStandardDate() {
        return this.fromDateStandardDate;
    }

    /**
     * @param fromDateStandardDate {@link String} the fromDateStandardDate to
     * set
     */
    public void setFromDateStandardDate(String fromDateStandardDate) {
        this.fromDateStandardDate = fromDateStandardDate;
    }

    /**
     * @return {@link String} the toDateStandardDate to get
     */
    public String getToDateStandardDate() {
        return this.toDateStandardDate;
    }

    /**
     * @param toDateStandardDate {@link String} the toDateStandardDate to set
     */
    public void setToDateStandardDate(String toDateStandardDate) {
        this.toDateStandardDate = toDateStandardDate;
    }

    /**
     * @return {@link String} the repositoryTypeValue to get
     */
    public String getRepositoryTypeValue() {
        return this.repositoryTypeValue;
    }

    /**
     * @param repositoryTypeValue {@link String} the repositoryTypeValue to set
     */
    public void setRepositoryTypeValue(String repositoryTypeValue) {
        this.repositoryTypeValue = repositoryTypeValue;
    }

    /**
     * @return {@link String} the warnings to get
     */
    public List<String> getWarnings() {
        return this.warnings;
    }

    /**
     * @param warnings {@link String} the warnings to set
     */
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    @Override
    public String execute() throws Exception {
        this.log.debug("Method start: \"execute\"");
        ArchivalInstitution archivalInstitution = null;
        String state = SUCCESS;
        try {
            archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
            if (archivalInstitution != null) {
                eagPath = archivalInstitution.getEagPath();
                if (eagPath != null && !eagPath.isEmpty()) {
                    state = INPUT;
                }
                String basePath = APEnetUtilities.FILESEPARATOR + this.getCountryCode() + APEnetUtilities.FILESEPARATOR
                        + this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
                String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
                this.loader = new EAG2012Loader(getAiId());
                if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()) {
                    boolean result = this.loader.fillEag2012();
                    log.info("Loader: " + this.loader.toString() + " has been charged.");
                    if (!result) {
                        addActionMessage(this.getText("eag2012.errors.loadingEAGFile"));
                    }
                    state = INPUT;
                } else {
                    this.loader.setCountryCode(new ArchivalLandscapeUtils().getmyCountry());
                    this.loader.setRecordId(Eag2012.generatesISOCode(this.loader.getId()));
                    this.loader.fillIntitialAutformEscaped();
                }
                this.loader.setInitialAutform(getInitialAutformValue());
            }
        } catch (Exception e) {
            log.error("Show/Edit EAG2012 Exception: " + e.getMessage());
        }
        if (state.equals(SUCCESS)) {
            fillDefaultLoaderValues();
        } else {
            this.newEag = false;
        }
        this.log.debug("End method: \"execute\"");
        return state;
    }

    /**
     * Returns the into ISO_2Characteres.
     *
     * @return {@link String} CC
     */
    public String getCountryCode() {
        return new ArchivalLandscapeUtils().getmyCountry();
    }

    /**
     * Generates unique isocode for ID used in APE.
     *
     * @return ISO_CODE
     */
    public String getIdUsedInAPE() {
        return Eag2012.generatesISOCode(getAiId());
    }
    private String form;

    private EAG2012Loader loader;

    private boolean newEag;

    // Constant to decide id only save or save and exit.
    private String saveOrExit;

    /**
     *
     * @return {@link boolean} newEeag
     */
    public Boolean isNewEag() {
        return this.newEag;
    }

    /**
     *
     * @return {@link Eag2012} loader to get
     */
    public EAG2012Loader getLoader() {
        return this.loader;
    }

    /**
     *
     * @param form {@link String} the form to set
     */
    public void setForm(String form) {
        this.form = form;
    }

    /**
     *
     * @return {@link String} form to get
     */
    public String getForm() {
        return this.form;
    }

    /**
     *
     * @return {@link String} saveOrExit to get
     */
    public String getSaveOrExit() {
        return this.saveOrExit;
    }

    /**
     *
     * @param saveOrExit {@link String} to set
     */
    public void setSaveOrExit(String saveOrExit) {
        this.saveOrExit = saveOrExit;
    }

    /**
     * Create edition form if it is the first thing created by default
     *
     * @return {@link String} state to set
     */
    public String editWebFormEAG2012() {
        this.log.debug("Method start: \"editWebFormEAG2012\"");
        String basePath = APEnetUtilities.FILESEPARATOR + this.getCountryCode() + APEnetUtilities.FILESEPARATOR
                + this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
        ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
        String path = archivalInstitution.getEagPath();
        String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
        String state = INPUT;
        if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()) {
            newEag = false;
            this.loader = new EAG2012Loader(getAiId());
            boolean result = this.loader.fillEag2012();
            log.info("Loader: " + this.loader.toString() + " has been charged.");

            try {
                // Check if messages already added.
                if (getActionMessages() == null || getActionMessages().isEmpty()) {
                    log.debug("Beginning EAG validation");
                    APEnetEAGDashboard apEnetEAGDashboard = new APEnetEAGDashboard(this.getAiId(), new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).getAbsolutePath());
                    if (!apEnetEAGDashboard.validate()) {
                        state = "input2"; //define different view for special warning cases
                        this.setWarnings(apEnetEAGDashboard.showWarnings());
                        //The EAG has been neither validated nor converted.
                        log.warn("The file " + new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).getName() + " is not valid");
                        for (int i = 0; i < this.getWarnings().size(); i++) {
                            String warning = this.getWarnings().get(i).replace("<br/>", "");
                            log.debug(warning);
                            ParseEag2012Errors parseEag2012Errors = new ParseEag2012Errors(warning, false, this);
                            if (this.getActionMessages() != null && !this.getActionMessages().isEmpty()) {
                                String currentError = parseEag2012Errors.errorsValidation();
                                if (!this.getActionMessages().contains(currentError)) {
                                    addActionMessage(parseEag2012Errors.errorsValidation());
                                }
                            } else {
                                addActionMessage(parseEag2012Errors.errorsValidation());
                            }

                        }
                    }
                } else {
                    Iterator<String> warningsIt = this.getActionMessages().iterator();
                    state = "input2"; //define different view for special warning cases
                    while (warningsIt.hasNext()) {
                        String warning = warningsIt.next();
                        if (warning != null && (warning.contains("of element 'recordId' is not valid")
                                || warning.contains("recordId already used"))) {
                            this.loader.setRecordId(this.getIdUsedInAPE());
                            this.loader.setRecordIdISIL(Eag2012.OPTION_NO);
                        }
                    }
                }
            } catch (SAXException saxE) {
                log.error(saxE.getMessage(), saxE);
            } catch (APEnetException apeE) {
                log.error(apeE.getMessage(), apeE);
            }

            if (!result) {
                getActionMessages();
                addActionMessage(this.getText("eag2012.errors.loadingEAGFile"));
            }
        } else if (new File(APEnetUtilities.getConfig().getRepoDirPath() + path).exists()) {
            newEag = false;
            this.loader = new EAG2012Loader(getAiId());
            boolean result = this.loader.fillEag2012();
            log.info("Loader: " + this.loader.toString() + " has been charged.");
            if (!result) {
                addActionMessage(this.getText("eag2012.errors.loadingEAGFile"));
            }
        } else {
            newEag = true;
            try {
                execute(); //default action
            } catch (Exception e) {
                log.error("ERROR trying to launch default action (execute): " + e.getCause());
            }
            state = SUCCESS;
        }
        this.log.debug("End method: \"editWebFormEAG2012\"");
        return state;
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();
        List<RightsInformation> rightsInformations = DAOFactory.instance().getRightsInformationDAO().getRightsInformations();
        rightsInformations.forEach((rightsInformation) -> {
            if (!(rightsInformation.getAbbreviation().equals(IN_COPYRIGHT_EU_ORPHAN_WORK)
                    || rightsInformation.getAbbreviation().equals(NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS)
                    || rightsInformation.getAbbreviation().equals(COPYRIGHT_NOT_EVALUATED))) {
                SelectItem selectItem = new SelectItem(rightsInformation.getId(), rightsInformation.getRightsName());
                rightsList.add(selectItem);
            }
        });
    }

    /**
     * Method for:<br>
     * - Load XML. - Create {@link Eag2012} EAG2012 JAXB object and fill
     * eag<Eag>
     * - Save XML.
     *
     * @return {@link String} result.
     */
    public String createEAG2012() {
        this.log.debug("Method start: \"createEAG2012\"");
        //log.error("");
        Eag2012 eag2012 = null;

        boolean existEag = false;
        try {
            eag2012 = getAndFillEag2012Object();
        } catch (JSONException e) {
            log.error(SecurityContext.get() + " unable to submit JSON parameters");
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }

        }
        if (eag2012 != null) {
            String basePath = APEnetUtilities.FILESEPARATOR + this.getCountryCode() + APEnetUtilities.FILESEPARATOR
                    + this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
            String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;

            // Load XML.
            Eag eag = null;
            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
            String loadPath = archivalInstitution.getEagPath();
            File eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + tempPath));

            if (!eagFile.exists() && loadPath != null && !loadPath.isEmpty()) {
                eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + loadPath));
            }

            if (eagFile.exists()) {
                try {
                    InputStream eagStream = FileUtils.openInputStream(eagFile);

                    JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    eag = (Eag) jaxbUnmarshaller.unmarshal(eagStream);

                    eagStream.close();
                } catch (JAXBException jaxbe) {
                    log.info(jaxbe.getMessage());
                } catch (IOException ioe) {
                    log.info(ioe.getMessage());
                }
            }

            // Fill EAG2012 JAXB object.
            CreateEAG2012 createEAG2012 = new CreateEAG2012(eag2012, eag);
            eag = createEAG2012.fillEAG2012();

            // Save XML.
            try {
                String path = basePath + APEnetUtilities.convertToFilename(eag.getControl().getRecordId().getValue()) + ".xml";

                JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.archivesportaleurope.net/Portal/profiles/eag_2012/ http://www.archivesportaleurope.net/Portal/profiles/eag_2012.xsd");
                jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EagNamespaceMapper());
                // Save in a temporal file.
                File eagTempFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + tempPath));

                if (!eagTempFile.exists()) {
                    FileUtils.writeStringToFile(eagTempFile, "new file");
                }

                jaxbMarshaller.marshal(eag, eagTempFile);

                // It is necessary to validate the file against APEnet EAG schema.
                log.debug("Beginning EAG validation");
                APEnetEAGDashboard apEnetEAGDashboard = new APEnetEAGDashboard(this.getAiId(), eagTempFile.getAbsolutePath());
                if (apEnetEAGDashboard.validate()) {
                    log.info("EAG is valid");

                    // Move temp file to final file.
                    File eagFinalFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + path));
                    if (eagFinalFile.exists()) {
                        try {
                            FileUtils.forceDelete(eagFinalFile);
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    // Windows file lock workaround; uncomment if necessary
                    // System.gc();
                    // Thread.sleep(2000);
                    FileUtils.moveFile(eagTempFile, eagFinalFile);

                    //store ddbb path
                    ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
                    archivalInstitution = archivalInstitutionDao.getArchivalInstitution(getAiId());
                    if (archivalInstitution != null) {
                        if (archivalInstitution.getEagPath() != null && !archivalInstitution.getEagPath().isEmpty()) {
                            existEag = true;
                        }
                        archivalInstitution.setEagPath(path);
                        archivalInstitution.setRepositorycode(eag2012.getRecordIdValue());

                        archivalInstitution.setShareWithWikimedia(eag2012.getShareEagWithWikimediaLicence());
                        archivalInstitution.setShareWithWikimediaId(eag2012.getShareEagWithWikimediaLicence().getId());

                        archivalInstitutionDao.store(archivalInstitution);
                        log.info("EAG2012 stored to " + path);
                        EagService.publish(archivalInstitution);
                    } else {
                        log.error("Could not be stored EAG2012 path, reason: null archival institution");
                    }

                    // After store the new EAG, review the values to fill the coordinates table.
                    this.insertCoordinatesValues(archivalInstitution);
                } else {

                    this.setWarnings(apEnetEAGDashboard.showWarnings());
                    //The EAG has been neither validated nor converted.
                    log.warn("The file " + eagFile.getName() + " is not valid");
                    for (int i = 0; i < this.getWarnings().size(); i++) {
                        String warning = this.getWarnings().get(i).replace("<br/>", "");
                        log.debug(warning);
                        ParseEag2012Errors parseEag2012Errors = new ParseEag2012Errors(warning, false, this);
                        if (this.getActionMessages() != null && !this.getActionMessages().isEmpty()) {
                            String currentError = parseEag2012Errors.errorsValidation();
                            if (!this.getActionMessages().contains(currentError)) {
                                addActionMessage(parseEag2012Errors.errorsValidation());
                            }
                        } else {
                            addActionMessage(parseEag2012Errors.errorsValidation());
                        }
                    }
                }
            } catch (JAXBException jaxbe) {
                log.info(jaxbe.getMessage());
            } catch (Exception e) {
                log.error(APEnetUtilities.generateThrowableLog(e));
            }

        }
        String result = this.editWebFormEAG2012();

        if (!existEag && result.equals(INPUT)) {
            result = "successCreated";
        }

        if ((result.equals(INPUT) || result.equals("successCreated"))
                && (this.getSaveOrExit() != null && this.getSaveOrExit().equalsIgnoreCase("saveAndExit"))) {
            result = "saveAndExit";
        }
        this.log.debug("End method: \"createEAG2012\"");
        return result;
    }

    /**
     *
     * @return removeInvalidEAG
     */
    public String removeInvalidEAG2012() {
        this.log.debug("Method start: \"removeInvalidEAG2012\"");
        this.log.debug("End method: \"removeInvalidEAG2012\"");
        return this.removeInvalidEAG(this.getAiId());
    }

    /**
     * Method for fill default loader values, now only works with main
     * repository.
     */
    private void fillDefaultLoaderValues() { //TODO, now only works with main repository
        this.log.debug("Method start: \"fillDefaultLoaderValues\"");
        this.newEag = true;
        this.loader = new EAG2012Loader();
        //your institution
        this.loader.setAgent(getPersonResponsibleForDescription());
        this.loader.setCountryCode(getCountryCode());
        this.loader.setOtherRepositorId(getIdOfInstitution());
        this.loader.setRecordId(getIdUsedInAPE());
        this.loader.setSelfRecordId(getIdUsedInAPE());
        String nameOfInstitution = "";
        if (this.getNameOfInstitution() != null
                && !this.getNameOfInstitution().isEmpty()) {
            nameOfInstitution = getNameOfInstitution();
        } else {
            nameOfInstitution = getInitialAutformValue();
        }
        this.loader.setInitialAutform(getInitialAutformValue());
        this.loader.setId(getAiId());
        this.loader.fillIntitialAutformEscaped();
        this.loader.setAutform(nameOfInstitution);
        this.loader.addIdAutform(nameOfInstitution);
        this.loader.setParform(getParallelNameOfInstitution());
		//identity
//		this.loader.setCountryCode(getCountryCode());
//		this.loader.setOtherRepositorId(getIdOfInstitution());
//		this.loader.setRecordId(getIdUsedInAPE());
//		this.loader.setAutform(getNameOfInstitution());
//		this.loader.setParform(getParallelNameOfInstitution());
        //contact
//		this.loader.setStreet(getStreetOfTheInstitution());
//		this.loader.setMunicipalityPostalcode(getCityOfTheInstitution());
//		this.loader.setLocalentity(getDistrictOfTheInstitution());
//		this.loader.setSecondem(getCountyOfTheInstitution());
//		this.loader.setFirstdem(getRegionOfTheInstitution());
//		this.loader.setCountry(getCountryOfTheInstitution());
        this.loader.setCountry(getCountryNameOfTheInstitution(getCountryCode()));
//		this.loader.setLatitude(getLatitudeOfTheInstitution());
//		this.loader.setLongitude(getLongitudeOfTheInstitution());
//		this.loader.setTelephone(getTelephoneOfTheInstitution());
//		this.loader.setFax(getFaxOfTheInstitution());
//		this.loader.setEmail(getEmailOfTheInstitution());
//		this.loader.setEmailTitle(getLinkTitleForEmailOfTheInstitution());
//		this.loader.setWebpage(getWebOfTheInstitution());
//		this.loader.setWebpageTitle(getLinkTitleForWebOfTheInstitution());
        //access and services
        //description
        //control
//		this.loader.setRecordId(getIdUsedInAPE());
//		this.loader.setAgent(getPersonResponsibleForDescription());
        this.loader.setControlAbbreviation(getContactAbbreviation());
        this.loader.setControlCitation(getContactFullName());
        this.loader.setControlNumberOfRules(getNumberOfRules());
        //relations
//		this.loader.setResourceRelationHref(getWebsiteOfResource());
//		this.loader.setResourceRelationrelationEntry(getTitleOfRelatedMaterial());
//		this.loader.setResourceRelationrelationEntryDescription(getDescriptionOfRelation());
//		this.loader.setEagRelationHref(getWebsiteOfDescription());
//		this.loader.setEagRelationrelationEntry(getTitleOfRelatedInstitution());
//		this.loader.setEagRelationrelationEntryDescription(getInstitutionDescriptionOfRelation());
        this.log.debug("End method: \"fillDefaultLoaderValues\"");
    }

    /**
     * Method for create List<String> number and loader
     * {@link ControlAbbreviation} control abbreviation.
     *
     * @return List<String> number.
     */
    private List<String> getNumberOfRules() {
        this.log.debug("Method start: \"getNumberOfRules\"");
        List<String> number = new ArrayList<String>();
        for (int i = 0; i < loader.getControlAbbreviation().size(); i++) {
            number.add("");
        }
        this.log.debug("End method: \"getNumberOfRules\"");
        return number;
    }

    /**
     * Method for create List<String> citations and add.
     *
     * @return List<String> citations.
     */
    private List<String> getContactFullName() {
        this.log.debug("Method start: \"getContactFullName\"");
        List<String> citations = new LinkedList<String>();
        citations.add("EAG (Encoded Archival Guide) 2012");
        citations.add("International Standard for Describing Institutions with Archival Holdings");
        citations.add("Codes for the representation of names of languages — Part 2: Alpha-3 code");
        citations.add("Codes for the representation of names of countries and their subdivisions – Part 1: Country codes");
        citations.add("Data elements and interchange formats – Information interchange – Representation of dates and times");
        citations.add("International Standard Identifier for Libraries and Related Organisations");
        citations.add("Codes for the representation of names of scripts");
        this.log.debug("End method: \"getContactFullName\"");
        return citations;
    }

    /**
     * Method for get contact abbreviation
     *
     * @return List<String> abbreviations
     */
    private List<String> getContactAbbreviation() {
        this.log.debug("Method start: \"getContactAbbreviation\"");
        List<String> abbreviations = new LinkedList<String>();
        abbreviations.add("EAG");
        abbreviations.add("ISDIAH");
        abbreviations.add("ISO 639-2b");
        abbreviations.add("ISO 3166-1");
        abbreviations.add("ISO 8601");
        abbreviations.add("ISO 15511");
        abbreviations.add("ISO 15924");
        this.log.debug("End method: \"getContactAbbreviation\"");
        return abbreviations;
    }

    /**
     * Method for fill {@link Eag2012} Eag2012
     *
     * @return {@link Eag2012} eag2012
     * @throws JSONException
     */
    private Eag2012 getAndFillEag2012Object() throws JSONException {
        this.log.debug("Method start: \"getAndFillEag2012Object\"");
        Eag2012 eag2012 = new Eag2012();
        eag2012 = null;
        if (this.form != null && !this.form.isEmpty()) {
            eag2012 = new Eag2012();
            this.form = this.form.trim();
            if (this.form.contains("\n")) {
                this.form = this.form.replace("\n", Eag2012.SPECIAL_RETURN_STRING_N);
            }
            if (this.form.contains("\r")) {
                this.form = this.form.replace("\r", Eag2012.SPECIAL_RETURN_STRING_R);
            }
            if (this.form.contains("%27")) {
                this.form = this.form.replace("%27", Eag2012.SPECIAL_RETURN_APOSTROPHE);
            }
//			JSONUtil util = new JSONUtil();
            JSONObject jsonObj = new JSONObject(this.form.trim());
            this.form = null;
            if (jsonObj.has("yourInstitution")) {
                YourInstitutionJsonObjToEag2012 yourInstitutionJsonObjToEag2012 = new YourInstitutionJsonObjToEag2012();
                eag2012 = yourInstitutionJsonObjToEag2012.JsonObjToEag2012(eag2012, jsonObj);
            }

            if (jsonObj.has("identity")) {
                IdentityJsonObjToEag2012 identityJsonObjToEag2012 = new IdentityJsonObjToEag2012();
                eag2012 = identityJsonObjToEag2012.JsonObjToEag2012(eag2012, jsonObj);
            }
            if (jsonObj.has("contact")) {
                ContactJsonObjToEag2012 contactJsonObjToEag2012 = new ContactJsonObjToEag2012();
                eag2012 = contactJsonObjToEag2012.JsonObjToEag2012(eag2012, jsonObj);
            }
            if (jsonObj.has("accessAndServices")) {
                AccessAndServicesJsonObjToEag2012 accesAndServicesJsonObjToEag2012 = new AccessAndServicesJsonObjToEag2012();
                eag2012 = accesAndServicesJsonObjToEag2012.JsonObjToEag2012(eag2012, jsonObj);
            }
            if (jsonObj.has("description")) {
                DescriptionJsonObjToEag2012 descriptionJsonObjToEag2012 = new DescriptionJsonObjToEag2012();
                eag2012 = descriptionJsonObjToEag2012.JsonObjToEag2012(eag2012, jsonObj);
            }
            if (jsonObj.has("control")) {
                ControlJsonObjToEag2012 controlJsonObjToEag2012 = new ControlJsonObjToEag2012();
                eag2012 = controlJsonObjToEag2012.JsonObjToEag2012(eag2012, jsonObj);
            }
            if (jsonObj.has("relations")) {
                RelationJsonObjToEag2012 relationJsonObjToEag2012 = new RelationJsonObjToEag2012();
                eag2012 = relationJsonObjToEag2012.JsonObjToEag2012(eag2012, jsonObj);
            }
        }
        this.log.debug("End method: \"getAndFillEag2012Object\"");
        return eag2012;
    }

    /**
     * Method to insert the new values in Coordinates table.
     *
     * @param archivalInstitution {@link ArchivalInstitution} the
     * archivalInstitution
     */
    private void insertCoordinatesValues(ArchivalInstitution archivalInstitution) {
        this.log.debug("Method start: \"insertCoordinatesValues\"");
        CoordinatesDAO coordinatesDAO = DAOFactory.instance().getCoordinatesDAO();
        List<Coordinates> coordinatesList = coordinatesDAO.findCoordinatesByArchivalInstitution(archivalInstitution);
        // Remove the previous coordinates.
        if (coordinatesList != null && !coordinatesList.isEmpty()) {
            for (int i = 0; i < coordinatesList.size(); i++) {
                try {
                    JpaUtil.beginDatabaseTransaction();
                    coordinatesDAO.deleteSimple(coordinatesList.get(i));
                    JpaUtil.commitDatabaseTransaction();
                } catch (Exception e) {
                    // Rollback current database transaction.
                    JpaUtil.rollbackDatabaseTransaction();
                    log.error("Error trying to delete: " + coordinatesList.get(i).getNameInstitution());
                    log.error(e.getCause());
                }
            }
        }

        Eag2012GeoCoordinatesAction eag2012GeoCoordinatesAction = new Eag2012GeoCoordinatesAction();
        eag2012GeoCoordinatesAction.insertCoordinates(archivalInstitution);
        this.log.debug("End method: \"insertCoordinatesValues\"");
    }

    /**
     * Method to recover the name of the country in the country language. If the
     * information is not available the method returns the country name in
     * English.
     *
     * @param countryCode {@link String} The current country code.
     * @return The countryName {@link String}
     */
    private String getCountryNameOfTheInstitution(String countryCode) {
        this.log.debug("Method start: \"getCountryNameOfTheInstitution\"");
        String countryName = "";
        CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
        List<Country> countryList = countryDAO.getCountries(countryCode);
        if (countryList != null && !countryList.isEmpty()) {
            Country country = countryList.get(0);
            countryName = getText("country." + country.getCname().toLowerCase().replace(" ", "_"));
        }
        this.log.debug("End method: \"getCountryNameOfTheInstitution\"");
        return countryName;
        //countryName =getCountryNameDAO(countryCode);		
    }

    /**
     * *
     * waiting for the implementation of #988
     *
     * @param countryCode 2 {@link String} letter code for the country (ES, GB,
     * DE ...)
     * @return the country name {@link String}
     */
    @SuppressWarnings("unused")
    private String getCountryNameDAO(String countryCode) {
        this.log.debug("Method start: \"getCountryNameDAO\"");
        String countryName = "";
        CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
        List<Country> countryList = countryDAO.getCountries(countryCode);
        if (countryList != null && !countryList.isEmpty()) {
            Country country = countryList.get(0);
            Set<CouAlternativeName> couAlternativeNameSet = country.getCouAlternativeNames();
            //check if there is a valid list or a empty one
            if (couAlternativeNameSet != null) {
                //Get the country name in the language of the country
                LangDAO langDAO = DAOFactory.instance().getLangDAO();
                //Get locale language
                Lang lang = null;
                Locale local = getLocale();
                String langCode = local.getLanguage();
				// TODO, if there isn't a default language,  #988				
                //			if (getDefaultLang()!=null)
                //				lang = getDefaultLang();
                //			else
                lang = langDAO.getLangByIso2Name(langCode);

                //Assign language if there exists in database
                if (lang != null) {
                    countryName = iterate(lang, couAlternativeNameSet);
                }
                //if there is not language in database assign default
                if (countryName.isEmpty()) {
                    lang = langDAO.getLangByIso2Name("EN");
                    countryName = iterate(lang, couAlternativeNameSet);
                }
            }
        }
        this.log.debug("End method: \"getCountryNameDAO\"");
        return countryName;
    }

    /**
     * Iterate the list of countries to get the country in its own language
     *
     * @param lang angDAO.getLangByIso2Name(countryCode)
     * @param couAlternativeNameSet {@link String}
     * ountry.getCouAlternativeNames()
     * @return countryName {@link String}
     */
    private String iterate(Lang lang, Set<CouAlternativeName> couAlternativeNameSet) {
        this.log.debug("Method start: \"iterate\"");
        String result = "";
        Iterator<CouAlternativeName> couAlternativeNameIt = couAlternativeNameSet.iterator();
        boolean found = false;
        while (!found && couAlternativeNameIt.hasNext()) {
            CouAlternativeName couAlternativeName = couAlternativeNameIt.next();
            if (couAlternativeName.getLang().getId() == lang.getId()) {
                result = couAlternativeName.getCouAnName();
                found = true;
            }
        }
        this.log.debug("End method: \"iterate\"");
        return result;
    }

}
