package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import eu.apenet.persistence.vo.RightsInformation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.apenet.dashboard.archivallandscape.ArchivalLandscapeUtils;
import eu.apenet.dashboard.manual.APEnetEAGDashboard;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * Class has been created to manage all EAG2012 information into one object.
 */
public class Eag2012 {
    // Constants.

    private static final String OTHERRECORDID_PATH = "/eag/archguide/otherRecordId";
    private final static Logger log = Logger.getLogger(Eag2012.class);
    public static final String OPTION_YES = "yes";	// Constant for value "yes".
    public static final String OPTION_NO = "no";	// ConsEag2012ant for value "no".

    public static final String OPTION_NONE = "none";	// Constant for value "none".

    // Constants for repository role options.
    public static final String OPTION_ROLE_HEADQUARTERS = "headquarters";	// Constant for value "headquarters".
    public static final String OPTION_ROLE_BRANCH = "branch";				// Constant for value "branch".
    public static final String OPTION_ROLE_INTERIM = "interim";				// Constant for value "interim".

    // Constants for repository role texts.
    public static final String OPTION_ROLE_HEADQUARTERS_TEXT = "Head quarter";	// Constant for value "Head quarter".
    public static final String OPTION_ROLE_BRANCH_TEXT = "Branch";				// Constant for value "Branch".
    public static final String OPTION_ROLE_INTERIM_TEXT = "Interim archive";	// Constant for value "Interim archive".

    // Constants for repository type options.
    public static final String OPTION_NATIONAL = "national";		// Constant for value "national".
    public static final String OPTION_REGIONAL = "regional";		// Constant for value "regional".
    public static final String OPTION_COUNTY = "county";			// Constant for value "county".
    public static final String OPTION_MUNICIPAL = "municipal";		// Constant for value "municipal".
    public static final String OPTION_SPECIALISED = "specialised";	// Constant for value "specialised".
    public static final String OPTION_PRIVATE = "private";			// Constant for value "private".
    public static final String OPTION_CHURCH = "church";			// Constant for value "church".
    public static final String OPTION_BUSINESS = "business";		// Constant for value "business".
    public static final String OPTION_UNIVERSITY = "university";	// Constant for value "university".
    public static final String OPTION_MEDIA = "media";				// Constant for value "media".
    public static final String OPTION_POLITICAL = "political";		// Constant for value "political".
    public static final String OPTION_CULTURAL = "cultural";		// Constant for value "cultural".

    // Constants for repository type texts.
    public static final String OPTION_NATIONAL_TEXT = "National archives";	// Constant for value "national".
    public static final String OPTION_REGIONAL_TEXT = "Regional archives";	// Constant for value "regional".
    public static final String OPTION_COUNTY_TEXT = "County/local authority archives";	// Constant for value "county".
    public static final String OPTION_MUNICIPAL_TEXT = "Municipal archives";	// Constant for value "municipal".
    public static final String OPTION_SPECIALISED_TEXT = "Specialised government archives";	// Constant for value "specialised".
    public static final String OPTION_PRIVATE_TEXT = "Private persons and family archives";	// Constant for value "private".
    public static final String OPTION_CHURCH_TEXT = "Church and religious archives";	// Constant for value "church".
    public static final String OPTION_BUSINESS_TEXT = "Business archives";	// Constant for value "business".
    public static final String OPTION_UNIVERSITY_TEXT = "University and research archives";	// Constant for value "university".
    public static final String OPTION_MEDIA_TEXT = "Media archives";	// Constant for value "media".
    public static final String OPTION_POLITICAL_TEXT = "Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations";	// Constant for value "political".
    public static final String OPTION_CULTURAL_TEXT = "Specialised non-governmental archives and archives of other cultural (heritage) institutions";	// Constant for value "cultural".

    // Constants for continents options.
    public static final String OPTION_EUROPE = "europe";			// Constant for value "europe".
    public static final String OPTION_AFRICA = "africa";			// Constant for value "africa".
    public static final String OPTION_ANTARCTICA = "antarctica";	// Constant for value "antarctica".
    public static final String OPTION_ASIA = "asia";				// Constant for value "asia".
    public static final String OPTION_AUSTRALIA = "australia";		// Constant for value "australia".
    public static final String OPTION_NORTH_AMERICA = "northAmerica";	// Constant for value "northAmerica".
    public static final String OPTION_SOUTH_AMERICA = "southAmerica";	// Constant for value "southAmerica".

    // Constants for continents texts.
    public static final String OPTION_EUROPE_TEXT = "Europe";			// Constant for value "Europe".
    public static final String OPTION_AFRICA_TEXT = "Africa";			// Constant for value "Africa".
    public static final String OPTION_ANTARCTICA_TEXT = "Antarctica";	// Constant for value "Antarctica".
    public static final String OPTION_ASIA_TEXT = "Asia";				// Constant for value "Asia".
    public static final String OPTION_AUSTRALIA_TEXT = "Australia";		// Constant for value "Australia".
    public static final String OPTION_NORTH_AMERICA_TEXT = "North America";	// Constant for value "North America".
    public static final String OPTION_SOUTH_AMERICA_TEXT = "South America";	// Constant for value "South America".

    // Constants for photographAllowance options.
    public static final String OPTION_DEPENDING = "depending";	// Constant for value "depending".
    public static final String OPTION_WITHOUT = "without";		// Constant for value "without".

    // Constants for photographAllowance text.
    public static final String OPTION_DEPENDING_TEXT = "depending on the material";	// Constant for value "depending on the material".
    public static final String OPTION_WITHOUT_TEXT = "yes (without flash)";			// Constant for value "yes (without flash)".

    // Constants for relationType options.
    public static final String OPTION_CREATOR = "creator";	// Constant for value "creator".
    public static final String OPTION_SUBJECT = "subject";	// Constant for value "subject".
    public static final String OPTION_OTHER = "other";		// Constant for value "other".

    // Constants for relationType texts.
    public static final String OPTION_CREATOR_TEXT = "creatorOf";	// Constant for value "creatorOf".
    public static final String OPTION_SUBJECT_TEXT = "subjectOf";	// Constant for value "subjectOf".
    public static final String OPTION_OTHER_TEXT = "other";			// Constant for value "other".

    // Constants for eagRelationType options.
    public static final String OPTION_CHILD = "child";				// Constant for value "child".
    public static final String OPTION_PARENT = "parent";			// Constant for value "parent".
    public static final String OPTION_EARLIER = "earlier";			// Constant for value "earlier".
    public static final String OPTION_LATER = "later";				// Constant for value "later".
    public static final String OPTION_ASSOCIATIVE = "associative";	// Constant for value "associative".

    // Constants for eagRelationType texts.
    public static final String OPTION_CHILD_TEXT = "hierarchical-child";	// Constant for value "hierarchical-child".
    public static final String OPTION_PARENT_TEXT = "hierarchical-parent";	// Constant for value "hierarchical-parent".
    public static final String OPTION_EARLIER_TEXT = "temporal-earlier";	// Constant for value "temporal-earlier".
    public static final String OPTION_LATER_TEXT = "temporal-later";		// Constant for value "temporal-later".
    public static final String OPTION_ASSOCIATIVE_TEXT = "associative";		// Constant for value "associative".

    // Constants for scriptCode options.
    public static final String OPTION_SCRIPT_ARAB = "Arab";	// Constant for value "Arab".
    public static final String OPTION_SCRIPT_ARMN = "Armn";	// Constant for value "Armn".
    public static final String OPTION_SCRIPT_CPRT = "Cprt";	// Constant for value "Cprt".
    public static final String OPTION_SCRIPT_CYRL = "Cyrl";	// Constant for value "Cyrl".
    public static final String OPTION_SCRIPT_GEOR = "Geor";	// Constant for value "Geor".
    public static final String OPTION_SCRIPT_GREK = "Grek";	// Constant for value "Grek".
    public static final String OPTION_SCRIPT_HEBR = "Hebr";	// Constant for value "Hebr".
    public static final String OPTION_SCRIPT_LATN = "Latn";	// Constant for value "Latn".

    // Constants for scriptCode texts.
    public static final String OPTION_SCRIPT_TEXT_ARAB = "Arab";	// Constant for value "Arab".
    public static final String OPTION_SCRIPT_TEXT_ARMN = "Armn";	// Constant for value "Armn".
    public static final String OPTION_SCRIPT_TEXT_CPRT = "Cprt";	// Constant for value "Cprt".
    public static final String OPTION_SCRIPT_TEXT_CYRL = "Cyrl";	// Constant for value "Cyrl".
    public static final String OPTION_SCRIPT_TEXT_GEOR = "Geor";	// Constant for value "Geor".
    public static final String OPTION_SCRIPT_TEXT_GREK = "Grek";	// Constant for value "Grek".
    public static final String OPTION_SCRIPT_TEXT_HEBR = "Hebr";	// Constant for value "Hebr".
    public static final String OPTION_SCRIPT_TEXT_LATN = "Latin";	// Constant for value "Latin".

    public static final String EAG_PATH = "EAG";	// Constat for value "EAG".

    // Constant for the EAG temporary file (when are problems in validation).
    public static final String EAG_TEMP_FILE_NAME = "eag2012_temp.xml";

    // Content for eag.
    public static final String EAG_XMLNS = "http://www.archivesportaleurope.net/profiles/APEnet_EAG/";
    public static final String XML_AUDIENCE = "external";
    public static final String XML_BASE = "http://www.archivesportaleurope.net/";

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_HUMAN = "dd.MM.yyyy";

    // Content for agent.
    public static final String AGENT_AUTOMATICALLY = "automatically created agent";

    // Content for agencyType.
    public static final String AGENT_TYPE_MACHINE = "machine";
    public static final String AGENT_TYPE_HUMAN = "human";

    // Content for otherRecordId.
    public static final String LOCAL_TYPE = "localId";

    // Content for localControl.
    public static final String LOCAL_TYPE_CONTROL = "detailLevel";
    public static final String CONTROL_SCRIPT_COD = "Latn";
    public static final String CONTROL_TRANSLITERATION = "http://www.archivesportaleurope.eu/scripts/EAG/";
    public static final String CONTROL_VOCABULARY_SOURCE = "http://www.archivesportaleurope.eu/vocabularies/EAG/";
    public static final String EXTENDED = "extended";

    // Constants for TABs indexes.
    public static final String TAB_YOUR_INSTITUTION = "your_institution";
    public static final String TAB_IDENTITY = "identity";
    public static final String TAB_CONTACT = "contact";
    public static final String TAB_ACCESS_AND_SERVICES = "access_and_services";
    public static final String TAB_DESCRIPTION = "description";
    public static final String TAB_CONTROL = "control";
    public static final String TAB_RELATION = "relation";

    // Constants for address.
    public static final String VISITORS_ADDRESS = "visitors address";
    public static final String POSTAL_ADDRESS = "postal address";

    // Content for eventType.
    public static final String EVENTTYPE_CANCELLED = "cancelled";
    public static final String EVENTTYPE_CREATED = "created";
    public static final String EVENTTYPE_DELETED = "deleted";
    public static final String EVENTTYPE_DELETED_MERGED = "deletedMerged";
    public static final String EVENTTYPE_DELETED_REPLACED = "deletedReplaced";
    public static final String EVENTTYPE_DELETED_SPLIT = "deletedSplit";
    public static final String EVENTTYPE_DERIVED = "derived";
    public static final String EVENTTYPE_REVISED = "revised";
    public static final String EVENTTYPE_NEW = "new";

    // Content for units.
    public static final String UNIT_SITE = "site";
    public static final String UNIT_BOOK = "book";
    public static final String UNIT_TITLE = "title";
    public static final String UNIT_SQUARE_METRE = "squaremetre";
    public static final String UNIT_LINEAR_METRE = "linearmetre";

    // Constants for section indexes.
    public static final String ROOT = "root";
    public static final String LANGUAGE_DECLARATIONS = "languageDeclaration";
    public static final String RESOURCE_RELATION = "resourceRelation";
    public static final String EAG_RELATION = "eagRelation";
    public static final String LOCAL_TYPE_DECLARATION = "localTypeDeclaration";
    public static final String CONVENTION_DECLARATION = "conventionDeclaration";
    public static final String MAINTENANCE_AGENCY = "maintenanceAgency";
    public static final String SOURCE = "source";
    public static final String REPOSITORY = "repository";
    public static final String OTHER_SERVICES = "otherServices";
    public static final String TOURS_SESSIONS = "toursSessions";
    public static final String EXHIBITION = "exhibition";
    public static final String REFRESHMENT = "refreshment";
    public static final String RESTORATION_LAB = "restorationlab";
    public static final String REPRODUCTIONSER = "reproductionser";
    public static final String INTERNET_ACCESS = "internetAccess";
    public static final String RESEARCH_SERVICES = "researchServices";
    public static final String HOLDINGS = "holdings";
    public static final String BUILDING = "building";
    public static final String REPOSITORHIST = "repositorhist";
    public static final String LIBRARY = "library";
    public static final String SEARCHROOM = "searchroom";
    public static final String INSTITUTION_RELATIONS = "institution_relations";

    // Constants for subsection indexes
    public static final String ROOT_SUBSECTION = "root_section";
    public static final String WORKING_PLACES = "workingPlaces";
    public static final String COMPUTER_PLACES = "computerPlaces";
    public static final String MICROFILM = "microfilm";
    public static final String MONOGRAPHIC_PUBLICATION = "monographicPublication";
    public static final String SERIAL_PUBLICATION = "serialPublication";
    public static final String REPOSITOR_FOUND = "repositorfound";
    public static final String REPOSITOR_SUP = "repositorsup";
    public static final String BUILDING_AREA = "building_area";
    public static final String BUILDING_LENGTH = "building_length";
    public static final String HOLDING_EXTENT = "holding_extent";
    public static final String HOLDING_SUBSECTION = "holding_subsection";

    public static final String SPECIAL_RETURN_STRING_N = "_-_SPECIAL-RETURN-N-CHARACTER_-_";
    public static final String SPECIAL_RETURN_STRING_R = "_-_SPECIAL-RETURN-R-CHARACTER_-_";
    public static final String SPECIAL_RETURN_APOSTROPHE = "%027";

    // Variables.
    private String repositoryId;
    private String otherRepositorId;
    private String autform;
    private String controlId;
    private String controlLanguage;
    private String recordIdId;
    private String recordIdValue;
    private List<String> otherRecordIdId;
    private List<String> otherRecordIdValue;
    private List<String> otherRecordIdLocalType;
    private String sourcesId;
    private String sourcesLang;
    private List<String> sourceHref;
    private List<String> sourceId;
    private List<String> sourceLastDateTimeVerified;
    private String maintenanceAgencyId;
    private String agencyCodeId;
    private String agencyCodeValue;
    private String agencyNameLang;
    private String agencyNameId;
    private String agencyNameValue;
    private List<String> otherAgencyCodeId;
    private List<String> otherAgencyCodeValue;
    private String maintenanceStatusId;
    private String maintenanceStatusValue;
    private String maintenanceHistoryId;
    private String maintenanceHistoryLang;
    private List<String> maintenanceEventId;
    private List<String> maintenanceEventLang;
    private String agentId;
    private String agentLang;
    private String agentValue;
    private String agentTypeId;
    private String agentTypeValue;
    private String eventDateTimeStandardDateTime;
    private String eventDateTimeLang;
    private String eventDateTimeId;
    private String eventDateTimeValue;
    private String eventTypeId;
    private String eventTypeValue;
    private List<String> languageDeclarationId;
    private List<String> languageDeclarationLang;
    private List<String> languageLanguageCode;
    private String languageId;
    private List<String> scriptScriptCode;
    private String scriptId;
    private String scriptLang;
    private String scriptValue;
    private String languageLang;
    private String languageValue;
    private List<String> conventionDeclarationId;
    private List<String> conventionDeclarationLang;
    private List<String> abbreviationId;
    private List<String> abbreviationLang;
    private List<String> abbreviationValue;
    private List<Map<String, List<String>>> citationId; //first index if control tab, next elements are into repo_# --> <repository></directions></citation></repository>
    private List<Map<String, List<String>>> citationLang;
    private List<Map<String, List<String>>> citationLastDateTimeVerified;
    private List<Map<String, List<String>>> citationHref;
    private List<Map<String, List<String>>> citationValue;
    private List<Map<String, String>> descriptiveNoteLang;
    private List<Map<String, String>> descriptiveNoteId;
    private List<Map<String, List<String>>> descriptiveNotePId;
    private List<Map<String, Map<String, List<String>>>> descriptiveNotePLang;
    private List<Map<String, Map<String, List<String>>>> descriptiveNotePValue;
    private List<String> localControlId;
    private List<String> localControlLang;
    private List<String> termLastDateTimeVerified;
    private List<String> termScriptCode;
    private List<String> termId;
    private List<String> termLang;
    private List<String> termValue;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateNotAfter;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateNotBefore;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateStandardDate;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateId;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateLang;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue;
    private List<Map<String, List<String>>> dateRangeId;
    private List<Map<String, List<String>>> dateRangeLang;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateNotAfter;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateNotBefore;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateStandardDate;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateNotAfter;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateNotBefore;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateStandardDate;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateId;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateLang;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateValue;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateId;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateLang;
    private List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateValue;
    private List<String> localTypeDeclarationId;
    private List<String> localTypeDeclarationLang;
    private String publicationStatusId;
    private String publicationStatusValue;
    private String sourceEntryScriptCode;
    private String sourceEntryId;
    private String sourceEntryLang;
    private String sourceEntryValue;
    private List<String> objectXMLWrapId;
    private String objectBinWrapId;
    private String repositoridCountrycode;
    private String repositoridRepositorycode;
    private String otherRepositorIdValue;
    private List<String> autformLang;
    private List<String> autformValue;
    private List<String> parformLang;
    private List<String> parformValue;
    private List<String> repositoryTypeValue;
    private List<String> nonpreformLang;
    private List<String> nonpreformValue;
    private String useDatesId;
    private String useDatesLang;
    private List<Map<String, List<String>>> dateSetId;
    private List<Map<String, List<String>>> dateSetLang;
    private List<String> repositoryNameLang;
    private List<String> repositoryNameValue;
    private List<String> repositoryRoleValue;
    private String geogareaLang;
    private String geogareaValue;
    private List<List<String>> locationLocalType;
    private List<Map<String, List<String>>> locationLatitude;
    private List<Map<String, List<String>>> locationLongitude;
    private List<List<String>> countryLang;
    private List<Map<String, List<String>>> countryValue;
    private List<List<String>> firstdemLang;
    private List<List<String>> firstdemValue;
    private List<List<String>> secondemLang;
    private List<List<String>> secondemValue;
    private List<List<String>> municipalityPostalcodeLang;
    private List<Map<String, List<String>>> municipalityPostalcodeValue;
    private List<Map<String, List<String>>> citiesValue;
    private List<List<String>> citiesLang;
    private List<List<String>> localentityLang;
    private List<List<String>> localentityValue;
    private List<Map<String, List<String>>> streetLang;
    private List<Map<String, List<String>>> streetValue;
    private List<List<String>> directionsLang;
    private List<List<String>> directionsValue;
    private List<List<String>> adminunitLang;
    private List<List<String>> adminunitValue;
    private List<Map<String, List<String>>> openingValue;
    private List<Map<String, List<String>>> openingLang;
    private List<Map<String, List<String>>> openingHref;
    private List<Map<String, List<String>>> closingStandardDate;
    private List<Map<String, List<String>>> closingValue;
    private List<Map<String, List<String>>> closingLang;
    private List<Map<String, String>> accessQuestion;
    private List<Map<String, List<String>>> restaccessLang;
    private List<Map<String, List<String>>> restaccessValue;
    private List<List<String>> termsOfUseHref;
    private List<List<String>> termsOfUseLang;
    private List<List<String>> termsOfUseValue;
    private List<Map<String, String>> accessibilityQuestion;
    private List<Map<String, List<String>>> accessibilityLang;
    private List<Map<String, List<String>>> accessibilityValue;
    private List<String> photographAllowanceValue;
    private List<List<String>> readersTicketHref;
    private List<List<String>> readersTicketLang;
    private List<List<String>> readersTicketValue;
    private List<List<String>> advancedOrdersHref;
    private List<List<String>> advancedOrdersLang;
    private List<List<String>> advancedOrdersValue;
    private List<String> libraryQuestion;
    private List<String> internetAccessQuestion;
    private List<String> reproductionserQuestion;
    private List<String> microformserQuestion;
    private List<String> photographserQuestion;
    private List<String> digitalserQuestion;
    private List<String> photocopyserQuestion;
    private List<String> restorationlabQuestion;
    private List<Map<String, Map<String, Map<String, List<String>>>>> numValue;
    private List<Map<String, List<String>>> ruleLang;
    private List<Map<String, List<String>>> ruleValue;
    private List<Map<String, Map<String, List<String>>>> webpageHref;
    private List<Map<String, Map<String, List<String>>>> webpageValue;
    private List<Map<String, Map<String, List<String>>>> webpageLang;
    private List<Map<String, Map<String, List<String>>>> emailHref;
    private List<Map<String, Map<String, List<String>>>> emailValue;
    private List<Map<String, Map<String, List<String>>>> emailLang;
    private List<Map<String, List<String>>> faxValue;
    private List<Map<String, Map<String, List<String>>>> telephoneValue;
    private List<Map<String, List<String>>> relationEntryScriptCode;
    private List<Map<String, List<String>>> relationEntryId;
    private List<Map<String, List<String>>> relationEntryLang;
    private List<Map<String, List<String>>> relationEntryValue;
    private List<String> eagRelationEagRelationType;
    private List<String> eagRelationHref;
    private List<String> placeEntryAccuracy;
    private List<String> placeEntryAltitude;
    private List<String> placeEntryId;
    private List<String> placeEntryLang;
    private List<String> placeEntryCountryCode;
    private List<String> placeEntryLatitude;
    private List<String> placeEntryLongitude;
    private List<String> placeEntryScriptCode;
    private List<String> resourceRelationResourceRelationType;
    private List<String> resourceRelationLastDateTimeVerified;
    private List<String> resourceRelationId;
    private Map<String, List<String>> resourceRelationLang;
    private Map<String, List<String>> resourceRelationHref;
    private String relationsId;
    private String relationsLang;
    private List<String> placeEntryValue;
    private List<Map<String, List<String>>> postalStreetValue;
    private List<Map<String, List<String>>> postalStreetLang;
    private boolean shareEagWithWikimedia;
    private RightsInformation shareEagWithWikimediaLicence;

    public Eag2012() {
        // TODO put all parameters here
    }

    public List<Map<String, List<String>>> getPostalStreetValue() {
        return this.postalStreetValue;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getOtherRepositorId() {
        return otherRepositorId;
    }

    public void setOtherRepositorId(String otherRepositorId) {
        this.otherRepositorId = otherRepositorId;
    }

    public String getAutform() {
        return autform;
    }

    public void setAutform(String autform) {
        this.autform = autform;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getControlLanguage() {
        return controlLanguage;
    }

    public void setControlLanguage(String controlLanguage) {
        this.controlLanguage = controlLanguage;
    }

    public String getRecordIdId() {
        return recordIdId;
    }

    public void setRecordIdId(String recordIdId) {
        this.recordIdId = recordIdId;
    }

    public String getRecordIdValue() {
        return recordIdValue;
    }

    public void setRecordIdValue(String recordIdValue) {
        this.recordIdValue = recordIdValue;
    }

    public List<String> getOtherRecordIdId() {
        return otherRecordIdId;
    }

    public void setOtherRecordIdId(List<String> otherRecordIdId) {
        this.otherRecordIdId = otherRecordIdId;
    }

    public List<String> getOtherRecordIdValue() {
        return otherRecordIdValue;
    }

    public void setOtherRecordIdValue(List<String> otherRecordIdValue) {
        this.otherRecordIdValue = otherRecordIdValue;
    }

    public List<String> getOtherRecordIdLocalType() {
        return otherRecordIdLocalType;
    }

    public void setOtherRecordIdLocalType(List<String> otherRecordIdLocalType) {
        this.otherRecordIdLocalType = otherRecordIdLocalType;
    }

    public String getSourcesId() {
        return sourcesId;
    }

    public void setSourcesId(String sourcesId) {
        this.sourcesId = sourcesId;
    }

    public String getSourcesLang() {
        return sourcesLang;
    }

    public void setSourcesLang(String sourcesLang) {
        this.sourcesLang = sourcesLang;
    }

    public List<String> getSourceHref() {
        return sourceHref;
    }

    public void setSourceHref(List<String> sourceHref) {
        this.sourceHref = sourceHref;
    }

    public List<String> getSourceId() {
        return sourceId;
    }

    public void setSourceId(List<String> sourceId) {
        this.sourceId = sourceId;
    }

    public List<String> getSourceLastDateTimeVerified() {
        return sourceLastDateTimeVerified;
    }

    public void setSourceLastDateTimeVerified(
            List<String> sourceLastDateTimeVerified) {
        this.sourceLastDateTimeVerified = sourceLastDateTimeVerified;
    }

    public String getMaintenanceAgencyId() {
        return maintenanceAgencyId;
    }

    public void setMaintenanceAgencyId(String maintenanceAgencyId) {
        this.maintenanceAgencyId = maintenanceAgencyId;
    }

    public String getAgencyCodeId() {
        return agencyCodeId;
    }

    public void setAgencyCodeId(String agencyCodeId) {
        this.agencyCodeId = agencyCodeId;
    }

    public String getAgencyCodeValue() {
        return agencyCodeValue;
    }

    public void setAgencyCodeValue(String agencyCodeValue) {
        this.agencyCodeValue = agencyCodeValue;
    }

    public String getAgencyNameLang() {
        return agencyNameLang;
    }

    public void setAgencyNameLang(String agencyNameLang) {
        this.agencyNameLang = agencyNameLang;
    }

    public String getAgencyNameId() {
        return agencyNameId;
    }

    public void setAgencyNameId(String agencyNameId) {
        this.agencyNameId = agencyNameId;
    }

    public String getAgencyNameValue() {
        return agencyNameValue;
    }

    public void setAgencyNameValue(String agencyNameValue) {
        this.agencyNameValue = agencyNameValue;
    }

    public List<String> getOtherAgencyCodeId() {
        return otherAgencyCodeId;
    }

    public void setOtherAgencyCodeId(List<String> otherAgencyCodeId) {
        this.otherAgencyCodeId = otherAgencyCodeId;
    }

    public List<String> getOtherAgencyCodeValue() {
        return otherAgencyCodeValue;
    }

    public void setOtherAgencyCodeValue(List<String> otherAgencyCodeValue) {
        this.otherAgencyCodeValue = otherAgencyCodeValue;
    }

    public String getMaintenanceStatusId() {
        return maintenanceStatusId;
    }

    public void setMaintenanceStatusId(String maintenanceStatusId) {
        this.maintenanceStatusId = maintenanceStatusId;
    }

    public String getMaintenanceStatusValue() {
        return maintenanceStatusValue;
    }

    public void setMaintenanceStatusValue(String maintenanceStatusValue) {
        this.maintenanceStatusValue = maintenanceStatusValue;
    }

    public String getMaintenanceHistoryId() {
        return maintenanceHistoryId;
    }

    public void setMaintenanceHistoryId(String maintenanceHistoryId) {
        this.maintenanceHistoryId = maintenanceHistoryId;
    }

    public String getMaintenanceHistoryLang() {
        return maintenanceHistoryLang;
    }

    public void setMaintenanceHistoryLang(String maintenanceHistoryLang) {
        this.maintenanceHistoryLang = maintenanceHistoryLang;
    }

    public List<String> getMaintenanceEventId() {
        return maintenanceEventId;
    }

    public void setMaintenanceEventId(List<String> maintenanceEventId) {
        this.maintenanceEventId = maintenanceEventId;
    }

    public List<String> getMaintenanceEventLang() {
        return maintenanceEventLang;
    }

    public void setMaintenanceEventLang(List<String> maintenanceEventLang) {
        this.maintenanceEventLang = maintenanceEventLang;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentLang() {
        return agentLang;
    }

    public void setAgentLang(String agentLang) {
        this.agentLang = agentLang;
    }

    public String getAgentValue() {
        return agentValue;
    }

    public void setAgentValue(String agentValue) {
        this.agentValue = agentValue;
    }

    public String getAgentTypeId() {
        return agentTypeId;
    }

    public void setAgentTypeId(String agentTypeId) {
        this.agentTypeId = agentTypeId;
    }

    public String getAgentTypeValue() {
        return agentTypeValue;
    }

    public void setAgentTypeValue(String agentTypeValue) {
        this.agentTypeValue = agentTypeValue;
    }

    public String getEventDateTimeStandardDateTime() {
        return eventDateTimeStandardDateTime;
    }

    public void setEventDateTimeStandardDateTime(
            String eventDateTimeStandardDateTime) {
        this.eventDateTimeStandardDateTime = eventDateTimeStandardDateTime;
    }

    public String getEventDateTimeLang() {
        return eventDateTimeLang;
    }

    public void setEventDateTimeLang(String eventDateTimeLang) {
        this.eventDateTimeLang = eventDateTimeLang;
    }

    public String getEventDateTimeId() {
        return eventDateTimeId;
    }

    public void setEventDateTimeId(String eventDateTimeId) {
        this.eventDateTimeId = eventDateTimeId;
    }

    public String getEventDateTimeValue() {
        return eventDateTimeValue;
    }

    public void setEventDateTimeValue(String eventDateTimeValue) {
        this.eventDateTimeValue = eventDateTimeValue;
    }

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeValue() {
        return eventTypeValue;
    }

    public void setEventTypeValue(String eventTypeValue) {
        this.eventTypeValue = eventTypeValue;
    }

    public List<String> getLanguageDeclarationId() {
        return languageDeclarationId;
    }

    public void setLanguageDeclarationId(List<String> languageDeclarationId) {
        this.languageDeclarationId = languageDeclarationId;
    }

    public List<String> getLanguageDeclarationLang() {
        return languageDeclarationLang;
    }

    public void setLanguageDeclarationLang(List<String> languageDeclarationLang) {
        this.languageDeclarationLang = languageDeclarationLang;
    }

    public List<String> getLanguageLanguageCode() {
        return languageLanguageCode;
    }

    public void setLanguageLanguageCode(List<String> languageLanguageCode) {
        this.languageLanguageCode = languageLanguageCode;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public List<String> getScriptScriptCode() {
        return scriptScriptCode;
    }

    public void setScriptScriptCode(List<String> scriptScriptCode) {
        this.scriptScriptCode = scriptScriptCode;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getScriptLang() {
        return scriptLang;
    }

    public void setScriptLang(String scriptLang) {
        this.scriptLang = scriptLang;
    }

    public String getScriptValue() {
        return scriptValue;
    }

    public void setScriptValue(String scriptValue) {
        this.scriptValue = scriptValue;
    }

    public String getLanguageLang() {
        return languageLang;
    }

    public void setLanguageLang(String languageLang) {
        this.languageLang = languageLang;
    }

    public String getLanguageValue() {
        return languageValue;
    }

    public void setLanguageValue(String languageValue) {
        this.languageValue = languageValue;
    }

    public List<String> getConventionDeclarationId() {
        return conventionDeclarationId;
    }

    public void setConventionDeclarationId(List<String> conventionDeclarationId) {
        this.conventionDeclarationId = conventionDeclarationId;
    }

    public List<String> getConventionDeclarationLang() {
        return conventionDeclarationLang;
    }

    public void setConventionDeclarationLang(List<String> conventionDeclarationLang) {
        this.conventionDeclarationLang = conventionDeclarationLang;
    }

    public List<String> getAbbreviationId() {
        return abbreviationId;
    }

    public void setAbbreviationId(List<String> abbreviationId) {
        this.abbreviationId = abbreviationId;
    }

    public List<String> getAbbreviationLang() {
        return abbreviationLang;
    }

    public void setAbbreviationLang(List<String> abbreviationLang) {
        this.abbreviationLang = abbreviationLang;
    }

    public List<String> getAbbreviationValue() {
        return abbreviationValue;
    }

    public void setAbbreviationValue(List<String> abbreviationValue) {
        this.abbreviationValue = abbreviationValue;
    }

    public List<Map<String, List<String>>> getCitationId() {
        return citationId;
    }

    public void setCitationId(List<Map<String, List<String>>> citationId) {
        this.citationId = citationId;
    }

    public List<Map<String, List<String>>> getCitationLang() {
        return citationLang;
    }

    public void setCitationLang(List<Map<String, List<String>>> citationLang) {
        this.citationLang = citationLang;
    }

    public List<Map<String, List<String>>> getCitationLastDateTimeVerified() {
        return citationLastDateTimeVerified;
    }

    public void setCitationLastDateTimeVerified(
            List<Map<String, List<String>>> citationLastDateTimeVerified) {
        this.citationLastDateTimeVerified = citationLastDateTimeVerified;
    }

    public List<Map<String, List<String>>> getCitationHref() {
        return citationHref;
    }

    public void setCitationHref(List<Map<String, List<String>>> citationHref) {
        this.citationHref = citationHref;
    }

    public List<Map<String, List<String>>> getCitationValue() {
        return citationValue;
    }

    public void setCitationValue(List<Map<String, List<String>>> citationValue) {
        this.citationValue = citationValue;
    }

    public List<Map<String, String>> getDescriptiveNoteLang() {
        return descriptiveNoteLang;
    }

    public void setDescriptiveNoteLang(List<Map<String, String>> descriptiveNoteLang) {
        this.descriptiveNoteLang = descriptiveNoteLang;
    }

    public List<Map<String, String>> getDescriptiveNoteId() {
        return descriptiveNoteId;
    }

    public void setDescriptiveNoteId(List<Map<String, String>> descriptiveNoteId) {
        this.descriptiveNoteId = descriptiveNoteId;
    }

    public List<Map<String, List<String>>> getDescriptiveNotePId() {
        return descriptiveNotePId;
    }

    public void setDescriptiveNotePId(List<Map<String, List<String>>> descriptiveNotePId) {
        this.descriptiveNotePId = descriptiveNotePId;
    }

    public List<Map<String, Map<String, List<String>>>> getDescriptiveNotePLang() {
        return descriptiveNotePLang;
    }

    public void setDescriptiveNotePLang(List<Map<String, Map<String, List<String>>>> descriptiveNotePLang) {
        this.descriptiveNotePLang = descriptiveNotePLang;
    }

    public List<Map<String, Map<String, List<String>>>> getDescriptiveNotePValue() {
        return descriptiveNotePValue;
    }

    public void setDescriptiveNotePValue(List<Map<String, Map<String, List<String>>>> descriptiveNotePValue) {
        this.descriptiveNotePValue = descriptiveNotePValue;
    }

    public List<String> getLocalControlId() {
        return localControlId;
    }

    public void setLocalControlId(List<String> localControlId) {
        this.localControlId = localControlId;
    }

    public List<String> getLocalControlLang() {
        return localControlLang;
    }

    public void setLocalControlLang(List<String> localControlLang) {
        this.localControlLang = localControlLang;
    }

    public List<String> getTermLastDateTimeVerified() {
        return termLastDateTimeVerified;
    }

    public void setTermLastDateTimeVerified(List<String> termLastDateTimeVerified) {
        this.termLastDateTimeVerified = termLastDateTimeVerified;
    }

    public List<String> getTermScriptCode() {
        return termScriptCode;
    }

    public void setTermScriptCode(List<String> termScriptCode) {
        this.termScriptCode = termScriptCode;
    }

    public List<String> getTermId() {
        return termId;
    }

    public void setTermId(List<String> termId) {
        this.termId = termId;
    }

    public List<String> getTermLang() {
        return termLang;
    }

    public void setTermLang(List<String> termLang) {
        this.termLang = termLang;
    }

    public List<String> getTermValue() {
        return termValue;
    }

    public void setTermValue(List<String> termValue) {
        this.termValue = termValue;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getDateNotAfter() {
        return dateNotAfter;
    }

    public void setDateNotAfter(List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateNotAfter) {
        this.dateNotAfter = dateNotAfter;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getDateNotBefore() {
        return dateNotBefore;
    }

    public void setDateNotBefore(List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateNotBefore) {
        this.dateNotBefore = dateNotBefore;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getDateStandardDate() {
        return dateStandardDate;
    }

    public void setDateStandardDate(List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateStandardDate) {
        this.dateStandardDate = dateStandardDate;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getDateId() {
        return dateId;
    }

    public void setDateId(List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateId) {
        this.dateId = dateId;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getDateLang() {
        return dateLang;
    }

    public void setDateLang(List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateLang) {
        this.dateLang = dateLang;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getDateValue() {
        return dateValue;
    }

    public void setDateValue(List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue) {
        this.dateValue = dateValue;
    }

    public List<Map<String, List<String>>> getDateRangeId() {
        return dateRangeId;
    }

    public void setDateRangeId(List<Map<String, List<String>>> dateRangeId) {
        this.dateRangeId = dateRangeId;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getToDateNotAfter() {
        return toDateNotAfter;
    }

    public void setToDateNotAfter(List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateNotAfter) {
        this.toDateNotAfter = toDateNotAfter;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getToDateNotBefore() {
        return toDateNotBefore;
    }

    public void setToDateNotBefore(List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateNotBefore) {
        this.toDateNotBefore = toDateNotBefore;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getToDateStandardDate() {
        return toDateStandardDate;
    }

    public void setToDateStandardDate(List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateStandardDate) {
        this.toDateStandardDate = toDateStandardDate;
    }

    public List<Map<String, List<String>>> getDateRangeLang() {
        return dateRangeLang;
    }

    public void setDateRangeLang(List<Map<String, List<String>>> dateRangeLang) {
        this.dateRangeLang = dateRangeLang;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getFromDateNotAfter() {
        return fromDateNotAfter;
    }

    public void setFromDateNotAfter(List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateNotAfter) {
        this.fromDateNotAfter = fromDateNotAfter;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getFromDateNotBefore() {
        return fromDateNotBefore;
    }

    public void setFromDateNotBefore(List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateNotBefore) {
        this.fromDateNotBefore = fromDateNotBefore;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getFromDateStandardDate() {
        return fromDateStandardDate;
    }

    public void setFromDateStandardDate(List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateStandardDate) {
        this.fromDateStandardDate = fromDateStandardDate;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getFromDateId() {
        return fromDateId;
    }

    public void setFromDateId(List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateId) {
        this.fromDateId = fromDateId;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getFromDateLang() {
        return fromDateLang;
    }

    public void setFromDateLang(List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateLang) {
        this.fromDateLang = fromDateLang;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getFromDateValue() {
        return fromDateValue;
    }

    public void setFromDateValue(List<Map<String, Map<String, Map<String, List<List<String>>>>>> fromDateValue) {
        this.fromDateValue = fromDateValue;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getToDateId() {
        return toDateId;
    }

    public void setToDateId(List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateId) {
        this.toDateId = toDateId;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getToDateLang() {
        return toDateLang;
    }

    public void setToDateLang(List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateLang) {
        this.toDateLang = toDateLang;
    }

    public List<Map<String, Map<String, Map<String, List<List<String>>>>>> getToDateValue() {
        return toDateValue;
    }

    public void setToDateValue(List<Map<String, Map<String, Map<String, List<List<String>>>>>> toDateValue) {
        this.toDateValue = toDateValue;
    }

    public List<String> getLocalTypeDeclarationId() {
        return localTypeDeclarationId;
    }

    public void setLocalTypeDeclarationId(List<String> localTypeDeclarationId) {
        this.localTypeDeclarationId = localTypeDeclarationId;
    }

    public List<String> getLocalTypeDeclarationLang() {
        return localTypeDeclarationLang;
    }

    public void setLocalTypeDeclarationLang(List<String> localTypeDeclarationLang) {
        this.localTypeDeclarationLang = localTypeDeclarationLang;
    }

    public String getPublicationStatusId() {
        return publicationStatusId;
    }

    public void setPublicationStatusId(String publicationStatusId) {
        this.publicationStatusId = publicationStatusId;
    }

    public String getPublicationStatusValue() {
        return publicationStatusValue;
    }

    public void setPublicationStatusValue(String publicationStatusValue) {
        this.publicationStatusValue = publicationStatusValue;
    }

    public String getSourceEntryScriptCode() {
        return sourceEntryScriptCode;
    }

    public void setSourceEntryScriptCode(String sourceEntryScriptCode) {
        this.sourceEntryScriptCode = sourceEntryScriptCode;
    }

    public String getSourceEntryId() {
        return sourceEntryId;
    }

    public void setSourceEntryId(String sourceEntryId) {
        this.sourceEntryId = sourceEntryId;
    }

    public String getSourceEntryLang() {
        return sourceEntryLang;
    }

    public void setSourceEntryLang(String sourceEntryLang) {
        this.sourceEntryLang = sourceEntryLang;
    }

    public String getSourceEntryValue() {
        return sourceEntryValue;
    }

    public void setSourceEntryValue(String sourceEntryValue) {
        this.sourceEntryValue = sourceEntryValue;
    }

    public List<String> getObjectXMLWrapId() {
        return objectXMLWrapId;
    }

    public void setObjectXMLWrapId(List<String> objectXMLWrapId) {
        this.objectXMLWrapId = objectXMLWrapId;
    }

    public String getObjectBinWrapId() {
        return objectBinWrapId;
    }

    public void setObjectBinWrapId(String objectBinWrapId) {
        this.objectBinWrapId = objectBinWrapId;
    }

    public String getRepositoridCountrycode() {
        return repositoridCountrycode;
    }

    public void setRepositoridCountrycode(String repositoridCountrycode) {
        this.repositoridCountrycode = repositoridCountrycode;
    }

    public String getRepositoridRepositorycode() {
        return repositoridRepositorycode;
    }

    public void setRepositoridRepositorycode(String repositoridRepositorycode) {
        this.repositoridRepositorycode = repositoridRepositorycode;
    }

    public String getOtherRepositorIdValue() {
        return otherRepositorIdValue;
    }

    public void setOtherRepositorIdValue(String otherRepositorIdValue) {
        this.otherRepositorIdValue = otherRepositorIdValue;
    }

    public List<String> getAutformLang() {
        return autformLang;
    }

    public void setAutformLang(List<String> autformLang) {
        this.autformLang = autformLang;
    }

    public List<String> getAutformValue() {
        return autformValue;
    }

    public void setAutformValue(List<String> autformValue) {
        this.autformValue = autformValue;
    }

    public List<String> getParformLang() {
        return parformLang;
    }

    public void setParformLang(List<String> parformLang) {
        this.parformLang = parformLang;
    }

    public List<String> getParformValue() {
        return parformValue;
    }

    public void setParformValue(List<String> parformValue) {
        this.parformValue = parformValue;
    }

    public List<String> getRepositoryTypeValue() {
        return repositoryTypeValue;
    }

    public void setRepositoryTypeValue(List<String> repositoryTypeValue) {
        this.repositoryTypeValue = repositoryTypeValue;
    }

    public List<String> getNonpreformLang() {
        return nonpreformLang;
    }

    public void setNonpreformLang(List<String> nonpreformLang) {
        this.nonpreformLang = nonpreformLang;
    }

    public List<String> getNonpreformValue() {
        return nonpreformValue;
    }

    public void setNonpreformValue(List<String> nonpreformValue) {
        this.nonpreformValue = nonpreformValue;
    }

    public String getUseDatesId() {
        return useDatesId;
    }

    public void setUseDatesId(String useDatesId) {
        this.useDatesId = useDatesId;
    }

    public String getUseDatesLang() {
        return useDatesLang;
    }

    public void setUseDatesLang(String useDatesLang) {
        this.useDatesLang = useDatesLang;
    }

    public List<Map<String, List<String>>> getDateSetId() {
        return dateSetId;
    }

    public void setDateSetId(List<Map<String, List<String>>> dateSetId) {
        this.dateSetId = dateSetId;
    }

    public List<Map<String, List<String>>> getDateSetLang() {
        return dateSetLang;
    }

    public void setDateSetLang(List<Map<String, List<String>>> dateSetLang) {
        this.dateSetLang = dateSetLang;
    }

    public List<String> getRepositoryNameLang() {
        return repositoryNameLang;
    }

    public void setRepositoryNameLang(List<String> repositoryNameLang) {
        this.repositoryNameLang = repositoryNameLang;
    }

    public List<String> getRepositoryNameValue() {
        return repositoryNameValue;
    }

    public void setRepositoryNameValue(List<String> repositoryNameValue) {
        this.repositoryNameValue = repositoryNameValue;
    }

    public List<String> getRepositoryRoleValue() {
        return repositoryRoleValue;
    }

    public void setRepositoryRoleValue(List<String> repositoryRoleValue) {
        this.repositoryRoleValue = repositoryRoleValue;
    }

    public String getGeogareaLang() {
        return geogareaLang;
    }

    public void setGeogareaLang(String geogareaLang) {
        this.geogareaLang = geogareaLang;
    }

    public String getGeogareaValue() {
        return geogareaValue;
    }

    public void setGeogareaValue(String geogareaValue) {
        this.geogareaValue = geogareaValue;
    }

    public List<List<String>> getLocationLocalType() {
        return locationLocalType;
    }

    public void setLocationLocalType(List<List<String>> locationLocalType) {
        this.locationLocalType = locationLocalType;
    }

    public List<Map<String, List<String>>> getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(List<Map<String, List<String>>> locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public List<Map<String, List<String>>> getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(List<Map<String, List<String>>> locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public List<List<String>> getCountryLang() {
        return countryLang;
    }

    public void setCountryLang(List<List<String>> countryLang) {
        this.countryLang = countryLang;
    }

    public List<Map<String, List<String>>> getCountryValue() {
        return countryValue;
    }

    public void setCountryValue(List<Map<String, List<String>>> countryValue) {
        this.countryValue = countryValue;
    }

    public List<List<String>> getFirstdemLang() {
        return firstdemLang;
    }

    public void setFirstdemLang(List<List<String>> firstdemLang) {
        this.firstdemLang = firstdemLang;
    }

    public List<List<String>> getFirstdemValue() {
        return firstdemValue;
    }

    public void setFirstdemValue(List<List<String>> firstdemValue) {
        this.firstdemValue = firstdemValue;
    }

    public List<List<String>> getSecondemLang() {
        return secondemLang;
    }

    public void setSecondemLang(List<List<String>> secondemLang) {
        this.secondemLang = secondemLang;
    }

    public List<List<String>> getSecondemValue() {
        return secondemValue;
    }

    public void setSecondemValue(List<List<String>> secondemValue) {
        this.secondemValue = secondemValue;
    }

    public List<List<String>> getMunicipalityPostalcodeLang() {
        return municipalityPostalcodeLang;
    }

    public void setMunicipalityPostalcodeLang(List<List<String>> municipalityPostalcodeLang) {
        this.municipalityPostalcodeLang = municipalityPostalcodeLang;
    }

    public List<Map<String, List<String>>> getMunicipalityPostalcodeValue() {
        return municipalityPostalcodeValue;
    }

    public void setMunicipalityPostalcodeValue(List<Map<String, List<String>>> municipalityPostalcodeValue) {
        this.municipalityPostalcodeValue = municipalityPostalcodeValue;
    }

    public List<List<String>> getLocalentityLang() {
        return localentityLang;
    }

    public void setLocalentityLang(List<List<String>> localentityLang) {
        this.localentityLang = localentityLang;
    }

    public List<List<String>> getLocalentityValue() {
        return localentityValue;
    }

    public void setLocalentityValue(List<List<String>> localentityValue) {
        this.localentityValue = localentityValue;
    }

    public List<Map<String, List<String>>> getStreetLang() {
        return streetLang;
    }

    public void setStreetLang(List<Map<String, List<String>>> streetLang) {
        this.streetLang = streetLang;
    }

    public List<Map<String, List<String>>> getStreetValue() {
        return streetValue;
    }

    public void setPostalStreetValue(List<Map<String, List<String>>> postalStreetValue) {
        this.postalStreetValue = postalStreetValue;
    }

    public void setStreetValue(List<Map<String, List<String>>> streetValue) {
        this.streetValue = streetValue;
    }

    public List<List<String>> getDirectionsLang() {
        return directionsLang;
    }

    public void setDirectionsLang(List<List<String>> directionsLang) {
        this.directionsLang = directionsLang;
    }

    public List<List<String>> getDirectionsValue() {
        return directionsValue;
    }

    public void setDirectionsValue(List<List<String>> directionsValue) {
        this.directionsValue = directionsValue;
    }

    public List<List<String>> getAdminunitLang() {
        return adminunitLang;
    }

    public void setAdminunitLang(List<List<String>> adminunitLang) {
        this.adminunitLang = adminunitLang;
    }

    public List<List<String>> getAdminunitValue() {
        return adminunitValue;
    }

    public void setAdminunitValue(List<List<String>> adminunitValue) {
        this.adminunitValue = adminunitValue;
    }

    public List<Map<String, List<String>>> getOpeningValue() {
        return openingValue;
    }

    /**
     * @return the openingLang
     */
    public List<Map<String, List<String>>> getOpeningLang() {
        return openingLang;
    }

    /**
     * @param openingLang the openingLang to set
     */
    public void setOpeningLang(List<Map<String, List<String>>> openingLang) {
        this.openingLang = openingLang;
    }

    public void setOpeningValue(List<Map<String, List<String>>> openingValue) {
        this.openingValue = openingValue;
    }

    public List<Map<String, List<String>>> getOpeningHref() {
        return openingHref;
    }

    public void setOpeningHref(List<Map<String, List<String>>> openingHref) {
        this.openingHref = openingHref;
    }

    public List<Map<String, List<String>>> getClosingStandardDate() {
        return closingStandardDate;
    }

    public void setClosingStandardDate(List<Map<String, List<String>>> closingStandardDate) {
        this.closingStandardDate = closingStandardDate;
    }

    public List<Map<String, List<String>>> getClosingValue() {
        return closingValue;
    }

    /**
     * @return the closingLang
     */
    public List<Map<String, List<String>>> getClosingLang() {
        return closingLang;
    }

    /**
     * @param closingLang the closingLang to set
     */
    public void setClosingLang(List<Map<String, List<String>>> closingLang) {
        this.closingLang = closingLang;
    }

    public void setClosingValue(List<Map<String, List<String>>> closingValue) {
        this.closingValue = closingValue;
    }

    public List<Map<String, String>> getAccessQuestion() {
        return accessQuestion;
    }

    public void setAccessQuestion(List<Map<String, String>> accessQuestion) {
        this.accessQuestion = accessQuestion;
    }

    public List<Map<String, List<String>>> getRestaccessLang() {
        return restaccessLang;
    }

    public void setRestaccessLang(List<Map<String, List<String>>> restaccessLang) {
        this.restaccessLang = restaccessLang;
    }

    public List<Map<String, List<String>>> getRestaccessValue() {
        return restaccessValue;
    }

    public void setRestaccessValue(List<Map<String, List<String>>> restaccessValue) {
        this.restaccessValue = restaccessValue;
    }

    public List<List<String>> getTermsOfUseHref() {
        return termsOfUseHref;
    }

    public void setTermsOfUseHref(List<List<String>> termsOfUseHref) {
        this.termsOfUseHref = termsOfUseHref;
    }

    public List<List<String>> getTermsOfUseLang() {
        return termsOfUseLang;
    }

    public void setTermsOfUseLang(List<List<String>> termsOfUseLang) {
        this.termsOfUseLang = termsOfUseLang;
    }

    public List<List<String>> getTermsOfUseValue() {
        return termsOfUseValue;
    }

    public void setTermsOfUseValue(List<List<String>> termsOfUseValue) {
        this.termsOfUseValue = termsOfUseValue;
    }

    public List<Map<String, String>> getAccessibilityQuestion() {
        return accessibilityQuestion;
    }

    public void setAccessibilityQuestion(List<Map<String, String>> accessibilityQuestion) {
        this.accessibilityQuestion = accessibilityQuestion;
    }

    public List<Map<String, List<String>>> getAccessibilityLang() {
        return accessibilityLang;
    }

    public void setAccessibilityLang(List<Map<String, List<String>>> accessibilityLang) {
        this.accessibilityLang = accessibilityLang;
    }

    public List<Map<String, List<String>>> getAccessibilityValue() {
        return accessibilityValue;
    }

    public void setAccessibilityValue(List<Map<String, List<String>>> accessibilityValue) {
        this.accessibilityValue = accessibilityValue;
    }

    public List<String> getPhotographAllowanceValue() {
        return photographAllowanceValue;
    }

    public void setPhotographAllowanceValue(List<String> photographAllowanceValue) {
        this.photographAllowanceValue = photographAllowanceValue;
    }

    public List<List<String>> getReadersTicketHref() {
        return readersTicketHref;
    }

    public void setReadersTicketHref(List<List<String>> readersTicketHref) {
        this.readersTicketHref = readersTicketHref;
    }

    public List<List<String>> getReadersTicketLang() {
        return readersTicketLang;
    }

    public void setReadersTicketLang(List<List<String>> readersTicketLang) {
        this.readersTicketLang = readersTicketLang;
    }

    public List<List<String>> getReadersTicketValue() {
        return readersTicketValue;
    }

    public void setReadersTicketValue(List<List<String>> readersTicketValue) {
        this.readersTicketValue = readersTicketValue;
    }

    public List<List<String>> getAdvancedOrdersHref() {
        return advancedOrdersHref;
    }

    public void setAdvancedOrdersHref(List<List<String>> advancedOrdersHref) {
        this.advancedOrdersHref = advancedOrdersHref;
    }

    public List<List<String>> getAdvancedOrdersLang() {
        return advancedOrdersLang;
    }

    public void setAdvancedOrdersLang(List<List<String>> advancedOrdersLang) {
        this.advancedOrdersLang = advancedOrdersLang;
    }

    public List<List<String>> getAdvancedOrdersValue() {
        return advancedOrdersValue;
    }

    public void setAdvancedOrdersValue(List<List<String>> advancedOrdersValue) {
        this.advancedOrdersValue = advancedOrdersValue;
    }

    public List<String> getLibraryQuestion() {
        return libraryQuestion;
    }

    public void setLibraryQuestion(List<String> libraryQuestion) {
        this.libraryQuestion = libraryQuestion;
    }

    public List<String> getInternetAccessQuestion() {
        return internetAccessQuestion;
    }

    public void setInternetAccessQuestion(List<String> internetAccessQuestion) {
        this.internetAccessQuestion = internetAccessQuestion;
    }

    public List<String> getReproductionserQuestion() {
        return reproductionserQuestion;
    }

    public void setReproductionserQuestion(List<String> reproductionserQuestion) {
        this.reproductionserQuestion = reproductionserQuestion;
    }

    public List<String> getMicroformserQuestion() {
        return microformserQuestion;
    }

    public void setMicroformserQuestion(List<String> microformserQuestion) {
        this.microformserQuestion = microformserQuestion;
    }

    public List<String> getPhotographserQuestion() {
        return photographserQuestion;
    }

    public void setPhotographserQuestion(List<String> photographserQuestion) {
        this.photographserQuestion = photographserQuestion;
    }

    public List<String> getDigitalserQuestion() {
        return digitalserQuestion;
    }

    public void setDigitalserQuestion(List<String> digitalserQuestion) {
        this.digitalserQuestion = digitalserQuestion;
    }

    public List<String> getPhotocopyserQuestion() {
        return photocopyserQuestion;
    }

    public void setPhotocopyserQuestion(List<String> photocopyserQuestion) {
        this.photocopyserQuestion = photocopyserQuestion;
    }

    public List<String> getRestorationlabQuestion() {
        return restorationlabQuestion;
    }

    public void setRestorationlabQuestion(List<String> restorationlabQuestion) {
        this.restorationlabQuestion = restorationlabQuestion;
    }

    public List<Map<String, Map<String, Map<String, List<String>>>>> getNumValue() {
        return numValue;
    }

    public void setNumValue(List<Map<String, Map<String, Map<String, List<String>>>>> numValue) {
        this.numValue = numValue;
    }

    public List<Map<String, List<String>>> getRuleLang() {
        return ruleLang;
    }

    public void setRuleLang(List<Map<String, List<String>>> ruleLang) {
        this.ruleLang = ruleLang;
    }

    public List<Map<String, List<String>>> getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(List<Map<String, List<String>>> ruleValue) {
        this.ruleValue = ruleValue;
    }

    public List<Map<String, Map<String, List<String>>>> getWebpageHref() {
        return webpageHref;
    }

    public void setWebpageHref(List<Map<String, Map<String, List<String>>>> webpageHref) {
        this.webpageHref = webpageHref;
    }

    public List<Map<String, Map<String, List<String>>>> getWebpageValue() {
        return webpageValue;
    }

    public void setWebpageValue(List<Map<String, Map<String, List<String>>>> webpageValue) {
        this.webpageValue = webpageValue;
    }

    public List<Map<String, Map<String, List<String>>>> getEmailHref() {
        return emailHref;
    }

    public void setEmailHref(List<Map<String, Map<String, List<String>>>> emailHref) {
        this.emailHref = emailHref;
    }

    public List<Map<String, Map<String, List<String>>>> getEmailValue() {
        return emailValue;
    }

    public void setEmailValue(List<Map<String, Map<String, List<String>>>> emailValue) {
        this.emailValue = emailValue;
    }

    public List<Map<String, List<String>>> getFaxValue() {
        return faxValue;
    }

    public void setFaxValue(List<Map<String, List<String>>> faxValue) {
        this.faxValue = faxValue;
    }

    public List<Map<String, Map<String, List<String>>>> getTelephoneValue() {
        return telephoneValue;
    }

    public void setTelephoneValue(List<Map<String, Map<String, List<String>>>> telephoneValue) {
        this.telephoneValue = telephoneValue;
    }

    public List<Map<String, List<String>>> getRelationEntryScriptCode() {
        return relationEntryScriptCode;
    }

    public void setRelationEntryScriptCode(List<Map<String, List<String>>> relationEntryScriptCode) {
        this.relationEntryScriptCode = relationEntryScriptCode;
    }

    public List<Map<String, List<String>>> getRelationEntryId() {
        return relationEntryId;
    }

    public void setRelationEntryId(List<Map<String, List<String>>> relationEntryId) {
        this.relationEntryId = relationEntryId;
    }

    public List<Map<String, List<String>>> getRelationEntryLang() {
        return relationEntryLang;
    }

    public void setRelationEntryLang(List<Map<String, List<String>>> relationEntryLang) {
        this.relationEntryLang = relationEntryLang;
    }

    public List<Map<String, List<String>>> getRelationEntryValue() {
        return relationEntryValue;
    }

    public void setRelationEntryValue(List<Map<String, List<String>>> relationEntryValue) {
        this.relationEntryValue = relationEntryValue;
    }

    public List<String> getEagRelationEagRelationType() {
        return eagRelationEagRelationType;
    }

    public void setEagRelationEagRelationType(
            List<String> eagRelationEagRelationType) {
        this.eagRelationEagRelationType = eagRelationEagRelationType;
    }

    public List<String> getEagRelationHref() {
        return eagRelationHref;
    }

    public void setEagRelationHref(List<String> eagRelationHref) {
        this.eagRelationHref = eagRelationHref;
    }

    public List<String> getPlaceEntryAccuracy() {
        return placeEntryAccuracy;
    }

    public void setPlaceEntryAccuracy(List<String> placeEntryAccuracy) {
        this.placeEntryAccuracy = placeEntryAccuracy;
    }

    public List<String> getPlaceEntryAltitude() {
        return placeEntryAltitude;
    }

    public void setPlaceEntryAltitude(List<String> placeEntryAltitude) {
        this.placeEntryAltitude = placeEntryAltitude;
    }

    public List<String> getPlaceEntryId() {
        return placeEntryId;
    }

    public void setPlaceEntryId(List<String> placeEntryId) {
        this.placeEntryId = placeEntryId;
    }

    public List<String> getPlaceEntryLang() {
        return placeEntryLang;
    }

    public void setPlaceEntryLang(List<String> placeEntryLang) {
        this.placeEntryLang = placeEntryLang;
    }

    public List<String> getPlaceEntryCountryCode() {
        return placeEntryCountryCode;
    }

    public void setPlaceEntryCountryCode(List<String> placeEntryCountryCode) {
        this.placeEntryCountryCode = placeEntryCountryCode;
    }

    public List<String> getPlaceEntryLatitude() {
        return placeEntryLatitude;
    }

    public void setPlaceEntryLatitude(List<String> placeEntryLatitude) {
        this.placeEntryLatitude = placeEntryLatitude;
    }

    public List<String> getPlaceEntryLongitude() {
        return placeEntryLongitude;
    }

    public void setPlaceEntryLongitude(List<String> placeEntryLongitude) {
        this.placeEntryLongitude = placeEntryLongitude;
    }

    public List<String> getPlaceEntryScriptCode() {
        return placeEntryScriptCode;
    }

    public void setPlaceEntryScriptCode(List<String> placeEntryScriptCode) {
        this.placeEntryScriptCode = placeEntryScriptCode;
    }

    public List<String> getResourceRelationResourceRelationType() {
        return resourceRelationResourceRelationType;
    }

    public void setResourceRelationResourceRelationType(
            List<String> resourceRelationResourceRelationType) {
        this.resourceRelationResourceRelationType = resourceRelationResourceRelationType;
    }

    public List<String> getResourceRelationLastDateTimeVerified() {
        return resourceRelationLastDateTimeVerified;
    }

    public void setResourceRelationLastDateTimeVerified(
            List<String> resourceRelationLastDateTimeVerified) {
        this.resourceRelationLastDateTimeVerified = resourceRelationLastDateTimeVerified;
    }

    public List<String> getResourceRelationId() {
        return resourceRelationId;
    }

    public void setResourceRelationId(List<String> resourceRelationId) {
        this.resourceRelationId = resourceRelationId;
    }

    public Map<String, List<String>> getResourceRelationLang() {
        return resourceRelationLang;
    }

    public void setResourceRelationLang(Map<String, List<String>> resourceRelationLang) {
        this.resourceRelationLang = resourceRelationLang;
    }

    public Map<String, List<String>> getResourceRelationHref() {
        return resourceRelationHref;
    }

    public void setResourceRelationHref(Map<String, List<String>> resourceRelationHref) {
        this.resourceRelationHref = resourceRelationHref;
    }

    public String getRelationsId() {
        return relationsId;
    }

    public void setRelationsId(String relationsId) {
        this.relationsId = relationsId;
    }

    public String getRelationsLang() {
        return relationsLang;
    }

    public void setRelationsLang(String relationsLang) {
        this.relationsLang = relationsLang;
    }

    public List<Map<String, List<String>>> getPostalStreetLang() {
        return postalStreetLang;
    }

    public void setPostalStreetLang(List<Map<String, List<String>>> postalStreetLang) {
        this.postalStreetLang = postalStreetLang;
    }

    public List<String> getPlaceEntryValue() {
        return placeEntryValue;
    }

    public void setPlaceEntryValue(List<String> placeEntryValue) {
        this.placeEntryValue = placeEntryValue;
    }

    public List<Map<String, List<String>>> getCitiesValue() {
        return citiesValue;
    }

    public void setCitiesValue(List<Map<String, List<String>>> citiesValue) {
        this.citiesValue = citiesValue;
    }

    public List<List<String>> getCitiesLang() {
        return citiesLang;
    }

    public void setCitiesLang(List<List<String>> citiesLang) {
        this.citiesLang = citiesLang;
    }

    public List<Map<String, Map<String, List<String>>>> getEmailLang() {
        return emailLang;
    }

    public void setEmailLang(List<Map<String, Map<String, List<String>>>> emailLang) {
        this.emailLang = emailLang;
    }

    public List<Map<String, Map<String, List<String>>>> getWebpageLang() {
        return webpageLang;
    }

    public void setWebpageLang(List<Map<String, Map<String, List<String>>>> webpageLang) {
        this.webpageLang = webpageLang;
    }

    public boolean isShareEagWithWikimedia() {
        return shareEagWithWikimedia;
    }

    public RightsInformation getShareEagWithWikimediaLicence() {
        return shareEagWithWikimediaLicence;
    }

    public void setShareEagWithWikimedia(boolean shareEagWithWikimedia) {
        this.shareEagWithWikimedia = shareEagWithWikimedia;
    }

    public void setShareEagWithWikimediaLicence(RightsInformation shareEagWithWikimediaLicence) {
        this.shareEagWithWikimediaLicence = shareEagWithWikimediaLicence;
    }

    /**
     *
     * Through this procedure can be ensured that an institution within the
     * Dashboard will have its unique ISO 15511 compliant identifier.
     *
     * @param archivalInstitutionId {@link Integer} the archivalInstitutionId
     * @param fullFilePath {@link String} the fullFilePath
     * @return boolean {@link boolean} the changed
     * @throws TransformerException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean checkAndFixRepositorId(Integer archivalInstitutionId, String fullFilePath) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        log.debug("Method start: \"checkAndFixRepositorId\"");
        boolean changed = false;
        APEnetEAGDashboard eag = new APEnetEAGDashboard(archivalInstitutionId, fullFilePath);
        eag.setEagPath(fullFilePath);
        //this information must be encoded in the element <otherRepositorId>;
        String otherRepositorId = eag.lookingForwardElementContent(OTHERRECORDID_PATH);

        if (otherRepositorId != null && !otherRepositorId.isEmpty()) {
            if (!isWrongRepositorId(otherRepositorId)) {
                //in case it isn’t, a code compliant with ISO 15511 will be created automatically 
                //for @repositorycode, using the country code plus an ascending counter
                otherRepositorId = generatesISOCode(archivalInstitutionId);
            }
        } else {
            //code is not provided (doesn't exists target tag). In this case a code 
            //compliant with ISO 15511 will be created automatically for @repositorycode
            otherRepositorId = generatesISOCode(archivalInstitutionId);
        }
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(true);
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document tempDoc = docBuilder.parse(fullFilePath);
        Element currentNode = null;
        //at this case the code provided is compliant with ISO 15511, it will 
        //be copied to the attribute @repositorycode coming with <repositorid>;
        NodeList recordsIds = tempDoc.getElementsByTagName("repositorid");
        for (int i = 0; i < recordsIds.getLength() && !changed; i++) {
            currentNode = (Element) recordsIds.item(i);
            Node parent = currentNode.getParentNode();
            if (parent != null && parent.getNodeName().equals("identity")) {
                parent = parent.getParentNode();
                if (parent != null && parent.getNodeName().equals("archguide")) {
                    parent = parent.getParentNode();
                    if (parent != null && parent.getNodeName().equals("eag")) {
                        currentNode.setAttribute("repositorycode", otherRepositorId);
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            TransformerFactory tf = TransformerFactory.newInstance(); // Save changes
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(tempDoc), new StreamResult(new File(fullFilePath)));
        }
        log.debug("End method: \"checkAndFixRepositorId\"");
        return changed;
    }

    /**
     * Generates a ISO-code based on internal archivalInstitutionId. This
     * ISO-code should be unique for each institution.
     *
     * @param archivalInstitutionId {@link Integer} the archivalInstitutionId
     * @return {@link String} the otherRepositorId
     */
    public static String generatesISOCode(Integer archivalInstitutionId) {
        log.debug("Method start: \"generatesISOCode\"");
        String otherRepositorId = null;
        if (archivalInstitutionId > 0) {
            int zeroes = 11 - archivalInstitutionId.toString().length();
            String countryCode = new ArchivalLandscapeUtils().getmyCountry() + "-";
            otherRepositorId = countryCode;
            for (int x = 0; x < zeroes; x++) {
                otherRepositorId += "0";
            }
            otherRepositorId += archivalInstitutionId.toString();
            ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
            Long finalFigure = new Long("99999999999");
            // TODO: Improve performance.
            while (!aiDao.isRepositoryCodeAvailable(otherRepositorId, archivalInstitutionId)) { //check value
                otherRepositorId = countryCode + finalFigure.toString(); //generate by final list of possible values
                finalFigure--;
            }
        }
        log.debug("End method: \"generatesISOCode\"");
        return otherRepositorId;
    }

    /**
     * Checks if it's a valid code. The code provided must be compliant with ISO
     * 15511. In case it isn’t or the code provided could not be used returns
     * false
     *
     * @param repositorId {@link String} the repositorId
     * @return {@link boolean}
     */
    private static boolean isWrongRepositorId(String repositorId) {
        log.debug("Method start: \"isWrongRepositorId\"");
        String isoCountry = new ArchivalLandscapeUtils().getmyCountry();
        if (repositorId.length() == 14 && repositorId.substring(0, 2).toLowerCase().equals(isoCountry) && StringUtils.isNumeric(repositorId.substring(3))) {
            //TODO could be helpful store all identifiers or check all existings eags to get all ingested ISO-codes into repositorycode attribute
            ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
            Integer archivalInstitutionId = new Integer(repositorId.substring(3));
            if (archivalInstitutionId != null || aiDao.isRepositoryCodeAvailable(repositorId, archivalInstitutionId)) { //TODO check if isRepositoryCodeAvailable is useful
                ArchivalInstitution archivalInstitution = aiDao.getArchivalInstitution(archivalInstitutionId);
                if (archivalInstitution == null || archivalInstitution.getCountry().getIsoname().equals(isoCountry)) {
                    return true; //the ISO code used could not be unique because it's reserved to existing other institution of this country
                }
            }
        }
        log.debug("End method: \"isWrongRepositorId\"");
        return false;
    }
}
