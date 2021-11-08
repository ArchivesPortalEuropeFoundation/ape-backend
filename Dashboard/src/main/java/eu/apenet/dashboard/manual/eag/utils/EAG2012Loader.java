package eu.apenet.dashboard.manual.eag.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import eu.apenet.dashboard.manual.eag.WebFormEAG2012Action;
import eu.apenet.persistence.vo.RightsInformation;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscapeUtils;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.loaderEAG2012.LoadAccessAndServicesTabValues;
import eu.apenet.dashboard.manual.eag.utils.loaderEAG2012.LoadContactTabValues;
import eu.apenet.dashboard.manual.eag.utils.loaderEAG2012.LoadControlTabValues;
import eu.apenet.dashboard.manual.eag.utils.loaderEAG2012.LoadDescriptionTabValues;
import eu.apenet.dashboard.manual.eag.utils.loaderEAG2012.LoadIdentityTabValues;
import eu.apenet.dashboard.manual.eag.utils.loaderEAG2012.LoadRelationsTabValues;
import eu.apenet.dashboard.manual.eag.utils.loaderEAG2012.LoadYourinstitutionTabValues;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * Class for load an {@link Eag2012} EAG2012 XML file into a JAXB object.
 */
public class EAG2012Loader {

    /**
     * Logger.
     */
    private final Logger log = Logger.getLogger(getClass());
    /**
     * EAG2012 {@link Eag2012} JAXB object.
     */
    protected Eag eag;
    private String eagPath;
    private Integer aiId;
    // Attributes.
    // Common to various tabs.
    private String agent;
    private String countryCode;
    private List<String> otherRecordId;
    private List<String> otherRecordIdLocalType;
    private String otherRepositorId;
    private String recordId;
    private String selfRecordId;
    private String initialAutform;
    private String initialAutformEscaped;
    private String autform;
    private String autformLang;
    private List<String> idAutform;
    private List<String> idAutformLang;
    private String parform;
    private String parformLang;
    private List<String> idParform;
    private List<String> idParformLang;
    private String localType;
    private String longitude;
    private String latitude;
    private String country;
    private String countryLang;
    private String municipalityPostalcode;
    private String municipalityPostalcodeLang;
    private String street;
    private String streetLang;
    private List<String> yiNumberOfVisitorsAddress;
    private List<String> yiLongitude;
    private List<String> yiLatitude;
    private List<String> yiCountry;
    private List<String> yiCountryLang;
    private List<String> yiMunicipalityPostalcode;
    private List<String> yiMunicipalityPostalcodeLang;
    private List<String> yiStreet;
    private List<String> yiStreetLang;
    private String municipalityPostalcodePostal;
    private String municipalityPostalcodePostalLang;
    private String streetPostal;
    private String streetPostalLang;
    private List<String> yiNumberOfPostalAddress;
    private List<String> yiMunicipalityPostalcodePostal;
    private List<String> yiMunicipalityPostalcodePostalLang;
    private List<String> yiStreetPostal;
    private List<String> yiStreetPostalLang;
    private String geogarea;
    private String telephone;
    private String email;
    private String emailTitle;
    private String emailLang;
    private List<String> yiNumberOfEmailAddress;
    private List<String> yiEmail;
    private List<String> yiEmailTitle;
    private List<String> yiEmailLang;
    private String webpage;
    private String webpageTitle;
    private String webpageLang;
    private List<String> yiNumberOfWebpageAddress;
    private List<String> yiWebpage;
    private List<String> yiWebpageTitle;
    private List<String> yiWebpageLang;
    private List<String> yiNumberOfOpening;
    private List<String> yiOpeningContent;
    private List<String> yiOpeningLang;
    private List<String> yiOpeningHref;
    private String closing;
    private String closingLang;
    private List<String> yiClosing;
    private List<String> yiClosingLang;
    private String accessQuestion;
    private String restaccess;
    private String restaccessLang;
    private List<String> yiRestaccess;
    private List<String> yiRestaccessLang;
    private String accessibilityQuestion;
    private String accessibility;
    private String accessibilityLang;
    private List<String> yiAccessibilityQuestion;
    private List<String> yiAccessibility;
    private List<String> yiAccessibilityLang;
    private List<String> yiResourceRelationHref;
    private List<String> yiResourceRelationrelationEntry;
    // Repo tabs.
    private int numberOfRepositories;
    // Your institution tab.
    private String recordIdISIL;
    private List<String> yiResourceRelationLang;
    // Identity tab.
    private List<List<String>> repositoryName;
    private List<List<String>> repositoryRole;
    private List<String> nonpreform;
    private List<String> nonpreformLang;
    private List<List<String>> nonpreformDate;
    private List<List<String>> nonpreformDateFrom;
    private List<List<String>> nonpreformDateTo;
    private List<String> repositoryType;
    // Contact.
    private List<List<String>> contactNumberOfVisitorsAddress;
    private List<List<String>> contactLatitude;
    private List<List<String>> contactLongitude;
    private List<List<String>> contactCountry;
    private List<List<String>> contactCountryLang;
    private List<List<String>> contactFirstdem;
    private List<List<String>> contactFirstdemLang;
    private List<List<String>> contactSecondem;
    private List<List<String>> contactSecondemLang;
    private List<List<String>> contactMunicipality;
    private List<List<String>> contactMunicipalityLang;
    private List<List<String>> contactLocalentity;
    private List<List<String>> contactLocalentityLang;
    private List<List<String>> contactStreet;
    private List<List<String>> contactStreetLang;
    private List<List<String>> contactNumberOfPostalAddress;
    private List<List<String>> contactPostalCountry;
    private List<List<String>> contactPostalCountryLang;
    private List<List<String>> contactPostalMunicipality;
    private List<List<String>> contactPostalMunicipalityLang;
    private List<List<String>> contactPostalStreet;
    private List<List<String>> contactPostalStreetLang;
    private List<List<String>> contactContinent;
    private List<List<String>> contactTelephone;
    private List<List<String>> contactFax;
    private List<List<String>> contactNumberOfEmailAddress;
    private List<List<String>> contactEmailHref;
    private List<List<String>> contactEmailTitle;
    private List<List<String>> contactEmailLang;
    private List<List<String>> contactNumberOfWebpageAddress;
    private List<List<String>> contactWebpageHref;
    private List<List<String>> contactWebpageTitle;
    private List<List<String>> contactWebpageLang;
    // Access and Services, general.
    private List<List<String>> asNumberOfOpening;
    private List<List<String>> asOpeningContent;
    private List<List<String>> asOpeningLang;
    private List<List<String>> asOpeningHref;
    private List<List<String>> asClosing;
    private List<List<String>> asClosingLang;
    private List<List<String>> asNumberOfDirections;
    private List<List<String>> asDirections;
    private List<List<String>> asDirectionsLang;
    private List<List<String>> asDirectionsCitationHref;
    private List<List<String>> asAccessQuestion;
    private List<List<String>> asRestaccess;
    private List<List<String>> asRestaccessLang;
    private List<List<String>> asNumberOfTermsOfUse;
    private List<List<String>> asTermsOfUse;
    private List<List<String>> asTermsOfUseLang;
    private List<List<String>> asTermsOfUseHref;
    private List<List<String>> asAccessibilityQuestion;
    private List<List<String>> asAccessibility;
    private List<List<String>> asAccessibilityLang;
    // Access and Services, searchroom.
    private List<List<String>> asSearchRoomTelephone;
    private List<List<String>> asSearchRoomNumberOfEmail;
    private List<List<String>> asSearchRoomEmailHref;
    private List<List<String>> asSearchRoomEmailTitle;
    private List<List<String>> asSearchRoomEmailLang;
    private List<List<String>> asSearchRoomNumberOfWebpage;
    private List<List<String>> asSearchRoomWebpageHref;
    private List<List<String>> asSearchRoomWebpageTitle;
    private List<List<String>> asSearchRoomWebpageLang;
    private List<List<String>> asSearchRoomWorkPlaces;
    private List<List<String>> asSearchRoomComputerPlaces;
    private List<List<String>> asSearchRoomComputerPlacesDescription;
    private List<List<String>> asSearchRoomComputerPlacesDescriptionLang;
    private List<List<String>> asSearchRoomMicrofilmReaders;
    private List<List<String>> asSearchRoomPhotographAllowance;
    private List<List<String>> asSearchRoomNumberOfReadersTicket;
    private List<List<String>> asSearchRoomReadersTicketHref;
    private List<List<String>> asSearchRoomReadersTicketContent;
    private List<List<String>> asSearchRoomReadersTicketLang;
    private List<List<String>> asSearchRoomNumberOfAdvancedOrders;
    private List<List<String>> asSearchRoomAdvancedOrdersHref;
    private List<List<String>> asSearchRoomAdvancedOrdersContent;
    private List<List<String>> asSearchRoomAdvancedOrdersLang;
    private List<List<String>> asSearchRoomResearchServicesContent;
    private List<List<String>> asSearchRoomResearchServicesLang;
    // Access and Services, library.
    private List<List<String>> asLibraryQuestion;
    private List<List<String>> asLibraryTelephone;
    private List<List<String>> asLibraryNumberOfEmail;
    private List<List<String>> asLibraryEmailHref;
    private List<List<String>> asLibraryEmailTitle;
    private List<List<String>> asLibraryEmailLang;
    private List<List<String>> asLibraryNumberOfWebpage;
    private List<List<String>> asLibraryWebpageHref;
    private List<List<String>> asLibraryWebpageTitle;
    private List<List<String>> asLibraryWebpageLang;
    private List<List<String>> asLibraryMonographPublication;
    private List<List<String>> asLibrarySerialPublication;
    // Access and Services, internet access.
    private List<List<String>> asInternetAccessQuestion;
    private List<List<String>> asInternetAccessDescription;
    private List<List<String>> asInternetAccessDescriptionLang;
    // Access and Services, technical services, restauration laboratory.
    private List<List<String>> asRestorationlabQuestion;
    private List<List<String>> asRestorationlabDescription;
    private List<List<String>> asRestorationlabDescriptionLang;
    private List<List<String>> asRestorationlabTelephone;
    private List<List<String>> asRestorationlabNumberOfEmail;
    private List<List<String>> asRestorationlabEmailHref;
    private List<List<String>> asRestorationlabEmailTitle;
    private List<List<String>> asRestorationlabEmailLang;
    private List<List<String>> asRestorationlabNumberOfWebpage;
    private List<List<String>> asRestorationlabWebpageHref;
    private List<List<String>> asRestorationlabWebpageTitle;
    private List<List<String>> asRestorationlabWebpageLang;
    // Access and Services, technical services, reproduction service.
    private List<List<String>> asReproductionserQuestion;
    private List<List<String>> asReproductionserDescription;
    private List<List<String>> asReproductionserDescriptionLang;
    private List<List<String>> asReproductionserTelephone;
    private List<List<String>> asReproductionserNumberOfEmail;
    private List<List<String>> asReproductionserEmailHref;
    private List<List<String>> asReproductionserEmailTitle;
    private List<List<String>> asReproductionserEmailLang;
    private List<List<String>> asReproductionserNumberOfWebpage;
    private List<List<String>> asReproductionserWebpageHref;
    private List<List<String>> asReproductionserWebpageTitle;
    private List<List<String>> asReproductionserWebpageLang;
    private List<List<String>> asReproductionserMicrofilmServices;
    private List<List<String>> asReproductionserPhotographicServices;
    private List<List<String>> asReproductionserDigitisationServices;
    private List<List<String>> asReproductionserPhotocopyingServices;
    // Access and Services, recreational services.
    private List<List<String>> asRecreationalServicesRefreshmentArea;
    private List<List<String>> asRecreationalServicesRefreshmentAreaLang;
    private List<List<String>> asRSNumberOfExhibition;
    private List<List<String>> asRSExhibition;
    private List<List<String>> asRSExhibitionLang;
    private List<List<String>> asRSExhibitionWebpageHref;
    private List<List<String>> asRSExhibitionWebpageTitle;
    private List<List<String>> asRSExhibitionWebpageLang;
    private List<List<String>> asRSNumberOfToursSessions;
    private List<List<String>> asRSToursSessions;
    private List<List<String>> asRSToursSessionsLang;
    private List<List<String>> asRSToursSessionsWebpageHref;
    private List<List<String>> asRSToursSessionsWebpageTitle;
    private List<List<String>> asRSToursSessionsWebpageLang;
    private List<List<String>> asRSNumberOfOtherServices;
    private List<List<String>> asRSOtherServices;
    private List<List<String>> asRSOtherServicesLang;
    private List<List<String>> asRSOtherServicesWebpageHref;
    private List<List<String>> asRSOtherServicesWebpageTitle;
    private List<List<String>> asRSOtherServicesWebpageLang;
    // Description, repository description.
    private List<List<String>> descRepositorhist;
    private List<List<String>> descRepositorhistLang;
    private List<List<String>> descRepositorFoundDate;
    private List<List<String>> descRepositorFoundRule;
    private List<List<String>> descRepositorFoundRuleLang;
    private List<List<String>> descRepositorSupDate;
    private List<List<String>> descRepositorSupRule;
    private List<List<String>> descRepositorSupRuleLang;
    // Description, administrative structure.
    private List<List<String>> descAdminunit;
    private List<List<String>> descAdminunitLang;
    // Description, building description.
    private List<List<String>> descBuilding;
    private List<List<String>> descBuildingLang;
    private List<List<String>> descRepositorarea;
    private List<List<String>> descRepositorareaUnit;
    private List<List<String>> descLengthshelf;
    private List<List<String>> descLengthshelfUnit;
    // Description, holding description.
    private List<List<String>> descHoldings;
    private List<List<String>> descHoldingsLang;
    private List<List<String>> descHoldingsDate;
    private List<List<String>> descNumberOfHoldingsDateRange;
    private List<List<String>> descHoldingsDateRangeFromDate;
    private List<List<String>> descHoldingsDateRangeToDate;
    private List<List<String>> descExtent;
    private List<List<String>> descExtentUnit;
    // Control.
    private String controlAgentLang;
    private String controlAgencyCode;
    private List<String> controlNumberOfLanguages;
    private List<String> controlLanguageDeclaration;
    private List<String> controlScript;
    private List<String> controlNumberOfRules;
    private List<String> controlAbbreviation;
    private List<String> controlCitation;
    // Relations.
    private List<String> relationsResourceRelationType;
    private List<String> relationsResourceRelationHref;
    private List<String> relationsResourceRelationEntry;
    private List<String> relationsResourceRelationEntryLang;
    private List<String> relationsResourceRelationEntryDescription;
    private List<String> relationsResourceRelationEntryDescriptionLang;
    private List<String> relationsNumberOfEagRelations;
    private List<String> relationsEagRelationType;
    private List<String> relationsEagRelationHref;
    private List<String> relationsEagRelationEntry;
    private List<String> relationsEagRelationEntryLang;
    private List<String> relationsEagRelationEntryDescription;
    private List<String> relationsEagRelationEntryDescriptionLang;

    private boolean shareEagWithWikimedia;
    private RightsInformation shareEagWithWikimediaLicence;

    /**
     * constructor
     *
     * @param aiId {@link Integer}
     */
    public EAG2012Loader(Integer aiId) {
        this.aiId = aiId;
    }

    public EAG2012Loader() {
    }
    // Getters and setters.

    /**
     * Method for fill Eag2012 {@link Eag2012}
     *
     * @return result {@link boolean}
     */
    public boolean fillEag2012() {
        boolean result = true;
        Eag eag = null;
        fillIntitialAutformEscaped();
        ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getId());
        this.shareEagWithWikimediaLicence = archivalInstitution.getShareWithWikimedia();
        if (this.shareEagWithWikimediaLicence != null) {
            if (this.shareEagWithWikimediaLicence.getAbbreviation().equals(WebFormEAG2012Action.CC_0)) {
                this.shareEagWithWikimedia = true;
            } else {
                this.shareEagWithWikimedia = false;
            }
        }
        String path = archivalInstitution.getEagPath();
        String alCountry = new ArchivalLandscapeUtils().getmyCountry();
        String basePath = APEnetUtilities.FILESEPARATOR + alCountry + APEnetUtilities.FILESEPARATOR
                + this.getId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
        String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
        File eagFile = null;
        if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()) {
            eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + tempPath));
        } else {
            eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + path));
        }
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
        this.setRecordId(this.getIdUsedInAPE());
        this.setSelfRecordId(this.getIdUsedInAPE());
        if (eag == null) {
            this.setCountryCode(this.getInitialCountryCode());
            log.info("no previous EAG file");
        } else {
            this.eag = eag;
            result = this.loadValuesEAG2012();
        }
        return result;
    }

    /**
     *
     * @return {@link Integer} aiId to get
     */
    public Integer getId() {
        return this.aiId;
    }

    /**
     *
     * @param {@link Integer} aiId to set
     */
    public void setId(Integer aiId) {
        this.aiId = aiId;
    }

    /**
     * @return {@link String} the agent to get
     */
    public String getAgent() {
        return this.agent;
    }

    /**
     * @param {@link String} agent the agent to set
     */
    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     * @return {@link String} the countryCode to get
     */
    public String getCountryCode() {
        return this.countryCode;
    }

    /**
     * @param countryCode {@link String} the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return {@link List<String>} the otherRecordId
     */
    public List<String> getOtherRecordId() {
        if (this.otherRecordId == null) {
            this.otherRecordId = new ArrayList<String>();
        }
        return this.otherRecordId;
    }

    /**
     * @param otherRecordId {@link List<String>} the otherRecordId to set
     */
    public void setOtherRecordId(List<String> otherRecordId) {
        this.otherRecordId = otherRecordId;
    }

    /**
     * @param otherRecordId the otherRecordId to add
     */
    /*public void addOtherRecordId(String otherRecordId) {
		this.getOtherRecordId().add(otherRecordId);
	}*/
    /**
     * @return {@link List<String>} the otherRecordIdLocalType
     */
    public List<String> getOtherRecordIdLocalType() {
        if (this.otherRecordIdLocalType == null) {
            this.otherRecordIdLocalType = new ArrayList<String>();
        }
        return this.otherRecordIdLocalType;
    }

    /**
     * @param otherRecordIdLocalType {@link List<String>} the
     * otherRecordIdLocalType to set
     */
    public void setOtherRecordIdLocalType(List<String> otherRecordIdLocalType) {
        this.otherRecordIdLocalType = otherRecordIdLocalType;
    }

    /**
     * @param otherRecordIdLocalType {@link String} the otherRecordIdLocalType
     * to add
     */
    public void addOtherRecordIdLocalType(String otherRecordIdLocalType) {
        this.getOtherRecordIdLocalType().add(otherRecordIdLocalType);
    }

    /**
     * @return {@link String} the otherRepositorId
     */
    public String getOtherRepositorId() {
        return this.otherRepositorId;
    }

    /**
     * @param otherRepositorId {@link String} the otherRepositorId to set
     */
    public void setOtherRepositorId(String otherRepositorId) {
        this.otherRepositorId = otherRepositorId;
    }

    /**
     * Returns the into ISO_2Characteres.
     *
     * @return CC
     */
    public String getInitialCountryCode() {
        return new ArchivalLandscapeUtils().getmyCountry();
    }

    /**
     *
     * @return {@link String} the RecordId to get
     */
    public String getRecordId() {
        return this.recordId;
    }

    /**
     *
     * @return {@link String} the selfRecordId to get
     */
    public String getSelfRecordId() {
        return selfRecordId;
    }

    /**
     *
     * @param selfRecordId {@link String} the selfRecordId to set
     */
    public void setSelfRecordId(String selfRecordId) {
        this.selfRecordId = selfRecordId;
    }

    /**
     * Generates unique isocode for ID used in APE.
     *
     * @return ISO_CODE
     */
    public String getIdUsedInAPE() {
        return Eag2012.generatesISOCode(getId());
    }

    /**
     * @param recordId {@link String} the recordId to set
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * @return {@link String} the initialAutform
     */
    public String getInitialAutform() {
        if (this.initialAutform == null
                || this.initialAutform.isEmpty()) {
            this.initialAutform = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getId()).getAiname();
        }
        this.initialAutform = removeDuplicateWhiteSpaces(this.initialAutform);
        return this.initialAutform;
    }

    /**
     * @param initialAutform {@link String} the initialAutform to set
     */
    public void setInitialAutform(String initialAutform) {
        this.initialAutform = initialAutform;
    }

    /**
     * @return {@link String} the initialAutformEscaped to get
     */
    public String getInitialAutformEscaped() {
        return this.initialAutformEscaped;
    }

    /**
     * method for fill IntitialAutformEscaped
     */
    public void fillIntitialAutformEscaped() {
        if (this.initialAutformEscaped == null
                || this.initialAutformEscaped.isEmpty()) {
            this.initialAutformEscaped = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getId()).getAiname();
        }
        this.initialAutformEscaped = removeDuplicateWhiteSpaces(this.initialAutformEscaped);
        if (this.initialAutformEscaped.contains("\'")) {
            this.initialAutformEscaped = this.initialAutformEscaped.replaceAll("\'", "%27");
        }
        if (this.initialAutformEscaped.contains("\"")) {
            this.initialAutformEscaped = this.initialAutformEscaped.replaceAll("\"", "%22");
        }
    }

    /**
     * @param initialAutformEscaped {@link String} the initialAutformEscaped to
     * set
     */
    public void setInitialAutformEscaped(String initialAutformEscaped) {
        this.initialAutformEscaped = removeDuplicateWhiteSpaces(initialAutformEscaped);

        if (this.initialAutformEscaped.contains("\'")) {
            this.initialAutformEscaped = this.initialAutformEscaped.replaceAll("\'", "%27");
        }
        if (this.initialAutformEscaped.contains("\"")) {
            this.initialAutformEscaped = this.initialAutformEscaped.replaceAll("\"", "%22");
        }
    }

    /**
     *
     * @return {@link String} Autform to get
     */
    public String getAutform() {
        return this.autform;
    }

    /**
     *
     * @param autform {@link String} the autform to set
     */
    public void setAutform(String autform) {
        this.autform = autform;
    }

    /**
     *
     * @return {@link String} AutformLang to get
     */
    public String getAutformLang() {
        return this.autformLang;
    }

    /**
     *
     * @param autformLang {@link String} AutformLang to set
     */
    public void setAutformLang(String autformLang) {
        this.autformLang = autformLang;
    }

    /**
     * @return the idAutform {@link List<String>} the idAutform to get
     */
    public List<String> getIdAutform() {
        if (this.idAutform == null) {
            this.idAutform = new ArrayList<String>();
        }
        return this.idAutform;
    }

    /**
     * @param idAutform {@link List<String>} the idAutform to set
     */
    public void setIdAutform(List<String> idAutform) {
        this.idAutform = idAutform;
    }

    /**
     * @param idAutform {@link String} the idAutform to add
     */
    public void addIdAutform(String idAutform) {
        this.getIdAutform().add(idAutform);
    }

    /**
     * @return {@link List<String>} the idAutformLang to get
     */
    public List<String> getIdAutformLang() {
        if (this.idAutformLang == null) {
            this.idAutformLang = new ArrayList<String>();
        }
        return this.idAutformLang;
    }

    /**
     * @param idAutformLang {@link List<String>} the idAutformLang to set
     */
    public void setIdAutformLang(List<String> idAutformLang) {
        this.idAutformLang = idAutformLang;
    }

    /**
     * @param idAutformLang the idAutformLang to add
     */
    /*public void addIdAutformLang(String idAutformLang) {
		this.getIdAutformLang().add(idAutformLang);
	}*/
    /**
     *
     * @return {@link String} Parform to get
     */
    public String getParform() {
        return this.parform;
    }

    /**
     *
     * @param parform {@link String} Parform to set
     */
    public void setParform(String parform) {
        this.parform = parform;
    }

    /**
     *
     * @return {@link String} ParformLang to get
     */
    public String getParformLang() {
        return this.parformLang;
    }

    /**
     *
     * @param parformLang {@link String} ParformLang to set
     */
    public void setParformLang(String parformLang) {
        this.parformLang = parformLang;
    }

    /**
     * @return {@link List<String>} the idParform
     */
    public List<String> getIdParform() {
        if (this.idParform == null) {
            this.idParform = new ArrayList<String>();
        }
        return this.idParform;
    }

    /**
     * @param idParform {@link List<String>} the idParform to set
     */
    public void setIdParform(List<String> idParform) {
        this.idParform = idParform;
    }

    /**
     * @param idParform {@link String} the idParform to add
     */
    public void addIdParform(String idParform) {
        this.getIdParform().add(idParform);
    }

    /**
     * @return {@link List<String>} the idParformLang to get
     */
    public List<String> getIdParformLang() {
        if (this.idParformLang == null) {
            this.idParformLang = new ArrayList<String>();
        }
        return this.idParformLang;
    }

    /**
     * @param idParformLang {@link List<String>} the idParformLang to set
     */
    public void setIdParformLang(List<String> idParformLang) {
        this.idParformLang = idParformLang;
    }

    /**
     * @param idParformLang {@link String} the idParformLang to add
     */
    public void addIdParformLang(String idParformLang) {
        this.getIdParformLang().add(idParformLang);
    }

    /**
     *
     * @return {@link String} LocalType to get
     */
    public String getLocalType() {
        return this.localType;
    }

    /**
     * @param localType {@link String} the localType to set
     */
    public void setLocalType(String localType) {
        this.localType = localType;
    }

    /**
     *
     * @return {@link String} Longitude to get
     */
    public String getLongitude() {
        return this.longitude;
    }

    /**
     *
     * @param longitude {@link String} the Longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return {@link String} Latitude to get
     */
    public String getLatitude() {
        return this.latitude;
    }

    /**
     *
     * @param latitude {@link String} the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return {@link String} Country to get
     */
    public String getCountry() {
        return this.country;
    }

    /**
     *
     * @param country {@link String} the Country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return {@link String} CountryLang to get
     */
    public String getCountryLang() {
        return this.countryLang;
    }

    /**
     *
     * @param countryLang {@link String} the CountryLang to set
     */
    public void setCountryLang(String countryLang) {
        this.countryLang = countryLang;
    }

    /**
     *
     * @return {@link String} MunicipalityPostalcode to get
     */
    public String getMunicipalityPostalcode() {
        return this.municipalityPostalcode;
    }

    /**
     *
     * @param municipalityPostalcode {@link String} the municipalityPostalcode
     * to set
     */
    public void setMunicipalityPostalcode(String municipalityPostalcode) {
        this.municipalityPostalcode = municipalityPostalcode;
    }

    /**
     *
     * @return {@link String} MunicipalityPostalcodeLang to get
     */
    public String getMunicipalityPostalcodeLang() {
        return this.municipalityPostalcodeLang;
    }

    /**
     *
     * @param municipalityPostalcodeLang {@link String} the
     * MunicipalityPostalcodeLang to set
     */
    public void setMunicipalityPostalcodeLang(String municipalityPostalcodeLang) {
        this.municipalityPostalcodeLang = municipalityPostalcodeLang;
    }

    /**
     *
     * @return {@link String} Street to get
     */
    public String getStreet() {
        return this.street;
    }

    /**
     *
     * @param street {@link String} the Street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     *
     * @return {@link String} StreetLang to get
     */
    public String getStreetLang() {
        return this.streetLang;
    }

    /**
     *
     * @param streetLang {@link String} the StreetLang to set
     */
    public void setStreetLang(String streetLang) {
        this.streetLang = streetLang;
    }

    /**
     * @return {@link List<String>} the yiLongitude to get
     */
    public List<String> getYiLongitude() {
        if (this.yiLongitude == null) {
            this.yiLongitude = new ArrayList<String>();
        }
        return this.yiLongitude;
    }

    /**
     * @param yiLongitude {@link List<String>} the yiLongitude to set
     */
    public void setYiLongitude(List<String> yiLongitude) {
        this.yiLongitude = yiLongitude;
    }

    /**
     * @param yiLongitude the yiLongitude to add
     */
    /*public void addYiLongitude(String yiLongitude) {
		this.getYiLongitude().add(yiLongitude);
	}*/
    /**
     * @return {@link List<String>} the yiLatitude to get
     */
    public List<String> getYiLatitude() {
        if (this.yiLatitude == null) {
            this.yiLatitude = new ArrayList<String>();
        }
        return this.yiLatitude;
    }

    /**
     * @param yiLatitude {@link List<String>} the yiLatitude to set
     */
    public void setYiLatitude(List<String> yiLatitude) {
        this.yiLatitude = yiLatitude;
    }

    /**
     * @param yiLatitude the yiLatitude to add
     */
    /*public void addYiLatitude(String yiLatitude) {
		this.getYiLatitude().add(yiLatitude);
	}*/
    /**
     * @return {@link List<String>} the yiCountry to get
     */
    public List<String> getYiCountry() {
        if (this.yiCountry == null) {
            this.yiCountry = new ArrayList<String>();
        }
        return this.yiCountry;
    }

    /**
     * @param yiCountry {@link List<String>} the yiCountry to set
     */
    public void setYiCountry(List<String> yiCountry) {
        this.yiCountry = yiCountry;
    }

    /**
     * @param yiCountry the yiCountry to add
     */
    /*public void addYiCountry(String yiCountry) {
		this.getYiCountry().add(yiCountry);
	}*/
    /**
     * @return {@link List<String>} the yiCountryLang to get
     */
    public List<String> getYiCountryLang() {
        if (this.yiCountryLang == null) {
            this.yiCountryLang = new ArrayList<String>();
        }
        return this.yiCountryLang;
    }

    /**
     * @param yiCountryLang {@link List<String>} the yiCountryLang to set
     */
    public void setYiCountryLang(List<String> yiCountryLang) {
        this.yiCountryLang = yiCountryLang;
    }

    /**
     * @param yiCountryLang the yiCountryLang to add
     */
    /*public void addYiCountryLang(String yiCountryLang) {
		this.getYiCountryLang().add(yiCountryLang);
	}*/
    /**
     * @return {@link List<String>} the yiMunicipalityPostalcode to get
     */
    public List<String> getYiMunicipalityPostalcode() {
        if (this.yiMunicipalityPostalcode == null) {
            this.yiMunicipalityPostalcode = new ArrayList<String>();
        }
        return this.yiMunicipalityPostalcode;
    }

    /**
     * @param yiMunicipalityPostalcode {@link List<String>} the
     * yiMunicipalityPostalcode to set
     */
    public void setYiMunicipalityPostalcode(List<String> yiMunicipalityPostalcode) {
        this.yiMunicipalityPostalcode = yiMunicipalityPostalcode;
    }

    /**
     * @param yiMunicipalityPostalcode the yiMunicipalityPostalcode to add
     */
    /*public void addYiMunicipalityPostalcode(String yiMunicipalityPostalcode) {
		this.getYiMunicipalityPostalcode().add(yiMunicipalityPostalcode);
	}*/
    /**
     * @return {@link List<String>} the yiMunicipalityPostalcodeLang to get
     */
    public List<String> getYiMunicipalityPostalcodeLang() {
        if (this.yiMunicipalityPostalcodeLang == null) {
            this.yiMunicipalityPostalcodeLang = new ArrayList<String>();
        }
        return this.yiMunicipalityPostalcodeLang;
    }

    /**
     * @param yiMunicipalityPostalcodeLang {@link List<String>} the
     * yiMunicipalityPostalcodeLang to set
     */
    public void setYiMunicipalityPostalcodeLang(
            List<String> yiMunicipalityPostalcodeLang) {
        this.yiMunicipalityPostalcodeLang = yiMunicipalityPostalcodeLang;
    }

    /**
     * @param yiMunicipalityPostalcodeLang the yiMunicipalityPostalcodeLang to
     * add
     */
    /*public void addYiMunicipalityPostalcodeLang(String yiMunicipalityPostalcodeLang) {
		this.getYiMunicipalityPostalcodeLang().add(yiMunicipalityPostalcodeLang);
	}*/
    /**
     * @return {@link List<String>} the yiStreet to get
     */
    public List<String> getYiStreet() {
        if (this.yiStreet == null) {
            this.yiStreet = new ArrayList<String>();
        }
        return this.yiStreet;
    }

    /**
     * @param yiStreet {@link List<String>} the yiStreet to set
     */
    public void setYiStreet(List<String> yiStreet) {
        this.yiStreet = yiStreet;
    }

    /**
     * @param yiStreet the yiStreet to add
     */
    /*public void addYiStreet(String yiStreet) {
		this.getYiStreet().add(yiStreet);
	}*/
    /**
     * @return {@link List<String>} the yiStreetLang to get
     */
    public List<String> getYiStreetLang() {
        if (this.yiStreetLang == null) {
            this.yiStreetLang = new ArrayList<String>();
        }
        return this.yiStreetLang;
    }

    /**
     * @param yiStreetLang {@link List<String>} the yiStreetLang to set
     */
    public void setYiStreetLang(List<String> yiStreetLang) {
        this.yiStreetLang = yiStreetLang;
    }

    /**
     * @param yiStreetLang the yiStreetLang to add
     */
    /*public void addYiStreetLang(String yiStreetLang) {
		this.getYiStreetLang().add(yiStreetLang);
	}*/
    /**
     *
     * @return {@link String} MunicipalityPostalcodePostal to get
     */
    public String getMunicipalityPostalcodePostal() {
        return this.municipalityPostalcodePostal;
    }

    /**
     *
     * @param municipalityPostalcodePostal {@link String} the
     * MunicipalityPostalcodePostal to set
     */
    public void setMunicipalityPostalcodePostal(String municipalityPostalcodePostal) {
        this.municipalityPostalcodePostal = municipalityPostalcodePostal;
    }

    /**
     *
     * @return {@link String} MunicipalityPostalcodePostalLang to get
     */
    public String getMunicipalityPostalcodePostalLang() {
        return this.municipalityPostalcodePostalLang;
    }

    /**
     *
     * @param municipalityPostalcodePostalLang {@link String} the
     * MunicipalityPostalcodePostalLnag to set
     */
    public void setMunicipalityPostalcodePostalLang(String municipalityPostalcodePostalLang) {
        this.municipalityPostalcodePostalLang = municipalityPostalcodePostalLang;
    }

    /**
     *
     * @return {@link String} StreetPostal to get
     */
    public String getStreetPostal() {
        return this.streetPostal;
    }

    /**
     *
     * @param streetPostal {@link String} the StreetPostal to set
     */
    public void setStreetPostal(String streetPostal) {
        this.streetPostal = streetPostal;
    }

    /**
     *
     * @return {@link String} StreetPostalLang to get
     */
    public String getStreetPostalLang() {
        return this.streetPostalLang;
    }

    /**
     *
     * @param streetPostalLang {@link String} the StreetPostalLang to set
     */
    public void setStreetPostalLang(String streetPostalLang) {
        this.streetPostalLang = streetPostalLang;
    }

    /**
     * @return {@link String} yiMunicipalityPostalcodePostal to get
     */
    public List<String> getYiMunicipalityPostalcodePostal() {
        if (this.yiMunicipalityPostalcodePostal == null) {
            this.yiMunicipalityPostalcodePostal = new ArrayList<String>();
        }
        return this.yiMunicipalityPostalcodePostal;
    }

    /**
     * @param yiMunicipalityPostalcodePostal {@link String} the
     * yiMunicipalityPostalcodePostal to set
     */
    public void setYiMunicipalityPostalcodePostal(
            List<String> yiMunicipalityPostalcodePostal) {
        this.yiMunicipalityPostalcodePostal = yiMunicipalityPostalcodePostal;
    }

    /**
     * @param yiMunicipalityPostalcodePostal the yiMunicipalityPostalcodePostal
     * to add
     */
    /*public void addYiMunicipalityPostalcodePostal(String yiMunicipalityPostalcodePostal) {
		this.getYiMunicipalityPostalcodePostal().add(yiMunicipalityPostalcodePostal);
	}*/
    /**
     * @return {@link List<String>} the yiMunicipalityPostalcodePostalLang to
     * get
     */
    public List<String> getYiMunicipalityPostalcodePostalLang() {
        if (this.yiMunicipalityPostalcodePostalLang == null) {
            this.yiMunicipalityPostalcodePostalLang = new ArrayList<String>();
        }
        return this.yiMunicipalityPostalcodePostalLang;
    }

    /**
     * @param yiMunicipalityPostalcodePostalLang {@link List<String>} the
     * yiMunicipalityPostalcodePostalLang to set
     */
    public void setYiMunicipalityPostalcodePostalLang(
            List<String> yiMunicipalityPostalcodePostalLang) {
        this.yiMunicipalityPostalcodePostalLang = yiMunicipalityPostalcodePostalLang;
    }

    /**
     * @param yiMunicipalityPostalcodePostalLang the
     * yiMunicipalityPostalcodePostalLang to add
     */
    /*public void addYiMunicipalityPostalcodePostalLang(String yiMunicipalityPostalcodePostalLang) {
		this.getYiMunicipalityPostalcodePostalLang().add(yiMunicipalityPostalcodePostalLang);
	}*/
    /**
     * @return {@link List<String>} the yiStreetPostal to get
     */
    public List<String> getYiStreetPostal() {
        if (this.yiStreetPostal == null) {
            this.yiStreetPostal = new ArrayList<String>();
        }
        return this.yiStreetPostal;
    }

    /**
     * @param yiStreetPostal {@link List<String>} the yiStreetPostal to set
     */
    public void setYiStreetPostal(List<String> yiStreetPostal) {
        this.yiStreetPostal = yiStreetPostal;
    }

    /**
     * @param yiStreetPostal the yiStreetPostal to add
     */
    /*public void addYiStreetPostal(String yiStreetPostal) {
		this.getYiStreetPostal().add(yiStreetPostal);
	}*/
    /**
     * @return {@link List<String>} the yiStreetPostalLang to get
     */
    public List<String> getYiStreetPostalLang() {
        if (this.yiStreetPostalLang == null) {
            this.yiStreetPostalLang = new ArrayList<String>();
        }
        return this.yiStreetPostalLang;
    }

    /**
     * @param yiStreetPostalLang {@link List<String>} the yiStreetPostalLang to
     * set
     */
    public void setYiStreetPostalLang(List<String> yiStreetPostalLang) {
        this.yiStreetPostalLang = yiStreetPostalLang;
    }

    /**
     * @param yiStreetPostalLang the yiStreetPostalLang to add
     */
    /*public void addYiStreetPostalLang(String yiStreetPostalLang) {
		this.getYiStreetPostalLang().add(yiStreetPostalLang);
	}*/
    /**
     *
     * @return {@link String} Geogarea to get
     */
    public String getGeogarea() {
        return this.geogarea;
    }

    /**
     *
     * @param geogarea {@link String} the Geogarea to set
     */
    public void setGeogarea(String geogarea) {
        this.geogarea = geogarea;
    }

    /**
     *
     * @return {@link String} Telephone to get
     */
    public String getTelephone() {
        return this.telephone;
    }

    /**
     *
     * @param telephone {@link String} the Telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     *
     * @return {@link String} Email to get
     */
    public String getEmail() {
        return this.email;
    }

    /**
     *
     * @param email {@link String} the Email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return {@link String} EmailTitle to get
     */
    public String getEmailTitle() {
        return this.emailTitle;
    }

    /**
     *
     * @param emailTitle {@link String} the EmailTitle to set
     */
    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    /**
     *
     * @return {@link String} EmailLang to get
     */
    public String getEmailLang() {
        return this.emailLang;
    }

    /**
     *
     * @param emailLang {@link String} the EmailLang to set
     */
    public void setEmailLang(String emailLang) {
        this.emailLang = emailLang;
    }

    /**
     * @return {@link String} the yiEmail to get
     */
    public List<String> getYiEmail() {
        if (this.yiEmail == null) {
            this.yiEmail = new ArrayList<String>();
        }
        return this.yiEmail;
    }

    /**
     * @param yiEmail {@link String} the yiEmail to set
     */
    public void setYiEmail(List<String> yiEmail) {
        this.yiEmail = yiEmail;
    }

    /**
     * @param yiEmail the yiEmail to add
     */
    /*public void addYiEmail(String yiEmail) {
		this.getYiEmail().add(yiEmail);
	}*/
    /**
     * @return {@link String} the yiEmailTitle to get
     */
    public List<String> getYiEmailTitle() {
        if (this.yiEmailTitle == null) {
            this.yiEmailTitle = new ArrayList<String>();
        }
        return this.yiEmailTitle;
    }

    /**
     * @param yiEmailTitle {@link String} the yiEmailTitle to set
     */
    public void setYiEmailTitle(List<String> yiEmailTitle) {
        this.yiEmailTitle = yiEmailTitle;
    }

    /**
     * @param yiEmailTitle the yiEmailTitle to add
     */
    /*public void addYiEmailTitle(String yiEmailTitle) {
		this.getYiEmailTitle().add(yiEmailTitle);
	}*/
    /**
     * @return {@link String} the yiEmailLang to get
     */
    public List<String> getYiEmailLang() {
        if (this.yiEmailLang == null) {
            this.yiEmailLang = new ArrayList<String>();
        }
        return this.yiEmailLang;
    }

    /**
     * @param yiEmailLang {@link String} the yiEmailLang to set
     */
    public void setYiEmailLang(List<String> yiEmailLang) {
        this.yiEmailLang = yiEmailLang;
    }

    /**
     * @param yiEmailLang the yiEmailLang to add
     */
    /*public void addYiEmailLang(String yiEmailLang) {
		this.getYiEmailLang().add(yiEmailLang);
	}*/
    /**
     *
     * @return {@link String} Webpage to get
     */
    public String getWebpage() {
        return this.webpage;
    }

    /**
     *
     * @param webpage {@link String} the Webpage to set
     */
    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    /**
     *
     * @return {@link String} WebpageTitle to get
     */
    public String getWebpageTitle() {
        return this.webpageTitle;
    }

    /**
     *
     * @param webpageTitle {@link String} the WebpageTitle to set
     */
    public void setWebpageTitle(String webpageTitle) {
        this.webpageTitle = webpageTitle;
    }

    /**
     *
     * @return {@link String} WebpageLang to get
     */
    public String getWebpageLang() {
        return this.webpageLang;
    }

    /**
     *
     * @param webpageLang {@link String} the WebpageLang to set
     */
    public void setWebpageLang(String webpageLang) {
        this.webpageLang = webpageLang;
    }

    /**
     * @return the yiWebpage {@link List<String>} to get
     */
    public List<String> getYiWebpage() {
        if (this.yiWebpage == null) {
            this.yiWebpage = new ArrayList<String>();
        }
        return this.yiWebpage;
    }

    /**
     * @param yiWebpage {@link List<String>} the yiWebpage to set
     */
    public void setYiWebpage(List<String> yiWebpage) {
        this.yiWebpage = yiWebpage;
    }

    /**
     * @param yiWebpage the yiWebpage to add
     */
    /*public void addYiWebpage(String yiWebpage) {
		this.getYiWebpage().add(yiWebpage);
	}*/
    /**
     * @return {@link List<String>} the yiWebpageTitle to get
     */
    public List<String> getYiWebpageTitle() {
        if (this.yiWebpageTitle == null) {
            this.yiWebpageTitle = new ArrayList<String>();
        }
        return this.yiWebpageTitle;
    }

    /**
     * @param yiWebpageTitle {@link List<String>} the yiWebpageTitle to set
     */
    public void setYiWebpageTitle(List<String> yiWebpageTitle) {
        this.yiWebpageTitle = yiWebpageTitle;
    }

    /**
     * @param yiWebpageTitle the yiWebpageTitle to add
     */
    /*public void addYiWebpageTitle(String yiWebpageTitle) {
		this.getYiWebpageTitle().add(yiWebpageTitle);
	}*/
    /**
     * @return {@link List<String>} the yiWebpageLang to get
     */
    public List<String> getYiWebpageLang() {
        if (this.yiWebpageLang == null) {
            this.yiWebpageLang = new ArrayList<String>();
        }
        return this.yiWebpageLang;
    }

    /**
     * @param yiWebpageLang {@link List<String>} the yiWebpageLang to set
     */
    public void setYiWebpageLang(List<String> yiWebpageLang) {
        this.yiWebpageLang = yiWebpageLang;
    }

    /**
     * @return the number of Opening instances for the Your Institution tab
     */
    public List<String> getYiNumberOfOpening() {
        if (this.yiNumberOfOpening == null) {
            this.yiNumberOfOpening = new ArrayList<>();
        }
        return yiNumberOfOpening;
    }

    /**
     * @param yiNumberOfOpening the number of Opening instances from the Your Institution tab
     */
    public void setYiNumberOfOpening(List<String> yiNumberOfOpening) {
        this.yiNumberOfOpening = yiNumberOfOpening;
    }
    
    /**
     * @return the yiOpeningContent {@link List<String>} to get
     */
    public List<String> getYiOpeningContent() {
        if (this.yiOpeningContent == null) {
            this.yiOpeningContent = new ArrayList<String>();
        }
        return this.yiOpeningContent;
    }

    /**
     * @param yiOpeningContent {@link List<String>} the yiOpeningContent to set
     */
    public void setYiOpeningContent(List<String> yiOpeningContent) {
        this.yiOpeningContent = yiOpeningContent;
    }

    /**
     * @return the yiOpeningLang {@link List<String>} to get
     */
    public List<String> getYiOpeningLang() {
        if (this.yiOpeningLang == null) {
            this.yiOpeningLang = new ArrayList<String>();
        }
        return this.yiOpeningLang;
    }

    /**
     * @param yiOpeningLang {@link List<String>} the yiOpeningLang to set
     */
    public void setYiOpeningLang(List<String> yiOpeningLang) {
        this.yiOpeningLang = yiOpeningLang;
    }

    /**
     * @param yiOpeningLang the yiOpeningLang to add
     */
    /*public void addYiOpeningLang(String yiOpeningLang) {
    this.getYiOpeningLang().add(yiOpeningLang);
    }*/
    
    /**
     * @return the yiOpeningHref {@link List<String>} to get
     */
    public List<String> getYiOpeningHref() {
        if (this.yiOpeningHref == null) {
            this.yiOpeningHref = new ArrayList<String>();
        }
        return yiOpeningHref;
    }

    /**
     * @param yiOpeningHref {@link List<String>} the yiOpeningHref to set
     */
    public void setYiOpeningHref(List<String> yiOpeningHref) {
        this.yiOpeningHref = yiOpeningHref;
    }

    public String getClosing() {
        return this.closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getClosingLang() {
        return this.closingLang;
    }

    public void setClosingLang(String closingLang) {
        this.closingLang = closingLang;
    }

    /**
     * @return {@link List<String>} the yiClosing to get
     */
    public List<String> getYiClosing() {
        if (this.yiClosing == null) {
            this.yiClosing = new ArrayList<String>();
        }
        return this.yiClosing;
    }

    /**
     * @param yiClosing {@link List<String>} the yiClosing to set
     */
    public void setYiClosing(List<String> yiClosing) {
        this.yiClosing = yiClosing;
    }

    /**
     * @param yiClosing the yiClosing to add
     */
    /*public void addYiClosing(String yiClosing) {
		this.getYiClosing().add(yiClosing);
	}*/
    /**
     * @return the yiClosingLang {@link List<String>} to get
     */
    public List<String> getYiClosingLang() {
        if (this.yiClosingLang == null) {
            this.yiClosingLang = new ArrayList<String>();
        }
        return this.yiClosingLang;
    }

    /**
     * @param yiClosingLang {@link List<String>} the yiClosingLang to set
     */
    public void setYiClosingLang(List<String> yiClosingLang) {
        this.yiClosingLang = yiClosingLang;
    }

    /**
     * @param yiClosingLang the yiClosingLang to add
     */
    /*public void addYiClosingLang(String yiClosingLang) {
		this.getYiClosingLang().add(yiClosingLang);
	}*/
    /**
     *
     * @return {@link String} AccessQuestion to get
     */
    public String getAccessQuestion() {
        return this.accessQuestion;
    }

    /**
     *
     * @param accessQuestion {@link String} the AccessQuestion to set
     */
    public void setAccessQuestion(String accessQuestion) {
        this.accessQuestion = accessQuestion;
    }

    /**
     *
     * @return {@link String} Restaccess to get
     */
    public String getRestaccess() {
        return this.restaccess;
    }

    /**
     *
     * @param restaccess {@link String} the Restaccess to set
     */
    public void setRestaccess(String restaccess) {
        this.restaccess = restaccess;
    }

    /**
     *
     * @return {@link String} RestaccessLang to get
     */
    public String getRestaccessLang() {
        return this.restaccessLang;
    }

    /**
     *
     * @param restaccessLang {@link String} the RestaccessLang to set
     */
    public void setRestaccessLang(String restaccessLang) {
        this.restaccessLang = restaccessLang;
    }

    /**
     * @return the yiRestaccess {@link List<String>} to get
     */
    public List<String> getYiRestaccess() {
        if (this.yiRestaccess == null) {
            this.yiRestaccess = new ArrayList<String>();
        }
        return this.yiRestaccess;
    }

    /**
     * @param yiRestaccess {@link List<String>} the yiRestaccess to set
     */
    public void setYiRestaccess(List<String> yiRestaccess) {
        this.yiRestaccess = yiRestaccess;
    }

    /**
     * @param yiRestaccess the yiRestaccess to add
     */
    /*public void addYiRestaccess(String yiRestaccess) {
		this.getYiRestaccess().add(yiRestaccess);
	}*/
    /**
     * @return {@link List<String>} the yiRestaccessLang to get
     */
    public List<String> getYiRestaccessLang() {
        if (this.yiRestaccessLang == null) {
            this.yiRestaccessLang = new ArrayList<String>();
        }
        return this.yiRestaccessLang;
    }

    /**
     * @param yiRestaccessLang {@link List<String>} the yiRestaccessLang to set
     */
    public void setYiRestaccessLang(List<String> yiRestaccessLang) {
        this.yiRestaccessLang = yiRestaccessLang;
    }

    /**
     * @param yiRestaccessLang the yiRestaccessLang to add
     */
    /*public void addYiRestaccessLang(String yiRestaccessLang) {
		this.getYiRestaccessLang().add(yiRestaccessLang);
	}*/
    /**
     *
     * @return {@link String} Accessibility to get
     */
    public String getAccessibility() {
        return this.accessibility;
    }

    /**
     *
     * @param accessibility {@link String} the Accessibility to set
     */
    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    /**
     *
     * @return {@link String} Accessibility to get
     */
    public String getAccessibilityQuestion() {
        return this.accessibilityQuestion;
    }

    /**
     *
     * @param accessibilityQuestion {@link String} the Accessibility to set
     */
    public void setAccessibilityQuestion(String accessibilityQuestion) {
        this.accessibilityQuestion = accessibilityQuestion;
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
     * @return {@link String} AccessibilityLang to get
     */
    public String getAccessibilityLang() {
        return this.accessibilityLang;
    }

    /**
     *
     * @param accessibilityLang {@link String} the AccessibilityLang to set
     */
    public void setAccessibilityLang(String accessibilityLang) {
        this.accessibilityLang = accessibilityLang;
    }

    /**
     * @return {@link List<String>} the yiAccessibilityQuestion to get
     */
    public List<String> getYiAccessibilityQuestion() {
        if (this.yiAccessibilityQuestion == null) {
            this.yiAccessibilityQuestion = new ArrayList<String>();
        }
        return this.yiAccessibilityQuestion;
    }

    /**
     * @param yiAccessibilityQuestion {@link List<String>} the
     * yiAccessibilityQuestion to set
     */
    public void setYiAccessibilityQuestion(List<String> yiAccessibilityQuestion) {
        this.yiAccessibilityQuestion = yiAccessibilityQuestion;
    }

    /**
     * @param yiAccessibilityQuestion the yiAccessibilityQuestion to add
     */
    /*public void addYiAccessibilityQuestion(String yiAccessibilityQuestion) {
		this.getYiAccessibilityQuestion().add(yiAccessibilityQuestion);
	}*/
    /**
     * @return {@link List<String>} the yiAccessibility to get
     */
    public List<String> getYiAccessibility() {
        if (this.yiAccessibility == null) {
            this.yiAccessibility = new ArrayList<String>();
        }
        return this.yiAccessibility;
    }

    /**
     * @param yiAccessibility {@link List<String>} the yiAccessibility to set
     */
    public void setYiAccessibility(List<String> yiAccessibility) {
        this.yiAccessibility = yiAccessibility;
    }

    /**
     * @param yiAccessibility the yiAccessibility to add
     */
    /*public void addYiAccessibility(String yiAccessibility) {
		this.getYiAccessibility().add(yiAccessibility);
	}*/
    /**
     * @return {@link List<String>} the yiAccessibilityLang to get
     */
    public List<String> getYiAccessibilityLang() {
        if (this.yiAccessibilityLang == null) {
            this.yiAccessibilityLang = new ArrayList<String>();
        }
        return this.yiAccessibilityLang;
    }

    /**
     * @param yiAccessibilityLang {@link List<String>} the yiAccessibilityLang
     * to set
     */
    public void setYiAccessibilityLang(List<String> yiAccessibilityLang) {
        this.yiAccessibilityLang = yiAccessibilityLang;
    }

    /**
     * @param yiAccessibilityLang the yiAccessibilityLang to add
     */
    /*public void addYiAccessibilityLang(String yiAccessibilityLang) {
		this.getYiAccessibilityLang().add(yiAccessibilityLang);
	}*/
    /**
     * @return {@link List<String>} the yiResourceRelationHref to get
     */
    public List<String> getYiResourceRelationHref() {
        if (this.yiResourceRelationHref == null) {
            this.yiResourceRelationHref = new ArrayList<String>();
        }
        return this.yiResourceRelationHref;
    }

    /**
     * @param yiResourceRelationHref {@link List<String>} the
     * yiResourceRelationHref to set
     */
    public void setYiResourceRelationHref(List<String> yiResourceRelationHref) {
        this.yiResourceRelationHref = yiResourceRelationHref;
    }

    /**
     * @param yiResourceRelationHref the yiResourceRelationHref to add
     */
    /*public void addYiResourceRelationHref(String yiResourceRelationHref) {
		this.getYiResourceRelationHref().add(yiResourceRelationHref);
	}*/
    /**
     * @return {@link List<String>} the yiResourceRelationrelationEntry to get
     */
    public List<String> getYiResourceRelationrelationEntry() {
        if (this.yiResourceRelationrelationEntry == null) {
            this.yiResourceRelationrelationEntry = new ArrayList<String>();
        }
        return this.yiResourceRelationrelationEntry;
    }

    /**
     * @param yiResourceRelationrelationEntry {@link List<String>} the
     * yiResourceRelationrelationEntry to set
     */
    public void setYiResourceRelationrelationEntry(List<String> yiResourceRelationrelationEntry) {
        this.yiResourceRelationrelationEntry = yiResourceRelationrelationEntry;
    }

    /**
     * @param yiResourceRelationrelationEntry the
     * yiResourceRelationrelationEntry to add
     */
    /*public void addYiResourceRelationrelationEntry(String yiResourceRelationrelationEntry) {
		this.getYiResourceRelationrelationEntry().add(yiResourceRelationrelationEntry);
	}*/
    /**
     * @return {@link List<String>} the yiResourceRelationLang to get
     */
    public List<String> getYiResourceRelationLang() {
        if (this.yiResourceRelationLang == null) {
            this.yiResourceRelationLang = new ArrayList<String>();
        }
        return this.yiResourceRelationLang;
    }

    /**
     * @param yiResourceRelationLang {@link List<String>} the
     * yiResourceRelationLang to set
     */
    public void setYiResourceRelationLang(List<String> yiResourceRelationLang) {
        this.yiResourceRelationLang = yiResourceRelationLang;
    }

    /**
     * @param yiResourceRelationLang the yiResourceRelationLang to add
     */
    /*public void addYiResourceRelationLang(String yiResourceRelationLang) {
		this.getYiResourceRelationLang().add(yiResourceRelationLang);
	}
     */
    /**
     * @return {@link List<String>} the yiNumberOfVisitorsAddress to get
     */
    public List<String> getYiNumberOfVisitorsAddress() {
        if (this.yiNumberOfVisitorsAddress == null) {
            this.yiNumberOfVisitorsAddress = new ArrayList<String>();
        }
        return this.yiNumberOfVisitorsAddress;
    }

    /**
     * @param yiNumberOfVisitorsAddress {@link List<String>} the
     * yiNumberOfVisitorsAddress to set
     */
    public void setYiNumberOfVisitorsAddress(List<String> yiNumberOfVisitorsAddress) {
        this.yiNumberOfVisitorsAddress = yiNumberOfVisitorsAddress;
    }

    /**
     * @param yiNumberOfVisitorsAddress the yiNumberOfVisitorsAddress to add
     */
    /*public void addYiNumberOfVisitorsAddress(String yiNumberOfVisitorsAddress) {
		this.getYiNumberOfVisitorsAddress().add(yiNumberOfVisitorsAddress);
	}*/
    /**
     * @return {@link List<String>} the yiNumberOfPostalAddress to get
     */
    public List<String> getYiNumberOfPostalAddress() {
        if (this.yiNumberOfPostalAddress == null) {
            this.yiNumberOfPostalAddress = new ArrayList<String>();
        }
        return this.yiNumberOfPostalAddress;
    }

    /**
     * @param yiNumberOfPostalAddress {@link List<String>} the
     * yiNumberOfPostalAddress to set
     */
    public void setYiNumberOfPostalAddress(List<String> yiNumberOfPostalAddress) {
        this.yiNumberOfPostalAddress = yiNumberOfPostalAddress;
    }

    /**
     * @param yiNumberOfPostalAddress the yiNumberOfPostalAddress to add
     */
    /*public void addYiNumberOfPostalAddress(String yiNumberOfPostalAddress) {
		this.getYiNumberOfPostalAddress().add(yiNumberOfPostalAddress);
	}*/
    /**
     * @return {@link List<String>} the yiNumberOfEmailAddress to get
     */
    public List<String> getYiNumberOfEmailAddress() {
        if (this.yiNumberOfEmailAddress == null) {
            this.yiNumberOfEmailAddress = new ArrayList<String>();
        }
        return this.yiNumberOfEmailAddress;
    }

    /**
     * @param yiNumberOfEmailAddress {@link List<String>} the
     * yiNumberOfEmailAddress to set
     */
    public void setYiNumberOfEmailAddress(List<String> yiNumberOfEmailAddress) {
        this.yiNumberOfEmailAddress = yiNumberOfEmailAddress;
    }

    /**
     * @param yiNumberOfEmailAddress the yiNumberOfEmailAddress to add
     */
    /*public void addYiNumberOfEmailAddress(String yiNumberOfEmailAddress) {
		this.getYiNumberOfEmailAddress().add(yiNumberOfEmailAddress);
	}*/
    /**
     * @return {@link List<String>} the yiNumberOfWebpageAddress to get
     */
    public List<String> getYiNumberOfWebpageAddress() {
        if (this.yiNumberOfWebpageAddress == null) {
            this.yiNumberOfWebpageAddress = new ArrayList<String>();
        }
        return this.yiNumberOfWebpageAddress;
    }

    /**
     * @param yiNumberOfWebpageAddress {@link List<String>} the
     * yiNumberOfWebpageAddress to set
     */
    public void setYiNumberOfWebpageAddress(List<String> yiNumberOfWebpageAddress) {
        this.yiNumberOfWebpageAddress = yiNumberOfWebpageAddress;
    }

    /**
     * @param yiNumberOfWebpageAddress the yiNumberOfWebpageAddress to add
     */
    /*public void addYiNumberOfWebpageAddress(String yiNumberOfWebpageAddress) {
		this.getYiNumberOfWebpageAddress().add(yiNumberOfWebpageAddress);
	}*/
    /**
     * @return {@link Int} the numberOfRepositories to get
     */
    public int getNumberOfRepositories() {
        return this.numberOfRepositories;
    }

    /**
     * @param numberOfRepositories {@link Int} the numberOfRepositories to set
     */
    public void setNumberOfRepositories(int numberOfRepositories) {
        this.numberOfRepositories = numberOfRepositories;
    }

    /**
     *
     * @return {@link List<String>} RecordIdISIL to get
     */
    public String getRecordIdISIL() {
        return this.recordIdISIL;
    }

    /**
     *
     * @param recordIdISIL {@link List<String>} the RecordIdISIL to set
     */
    public void setRecordIdISIL(String recordIdISIL) {
        this.recordIdISIL = recordIdISIL;
    }

    /**
     * @return {@link List<List<String>>} the repositoryName to get
     */
    public List<List<String>> getRepositoryName() {
        if (this.repositoryName == null) {
            this.repositoryName = new ArrayList<List<String>>();
        }
        return this.repositoryName;
    }

    /**
     * @param repositoryName {@link List<List<String>>} the repositoryName to
     * set
     */
    public void setRepositoryName(List<List<String>> repositoryName) {
        this.repositoryName = repositoryName;
    }

    /**
     * @param repositoryName the repositoryName to add
     */
    /*public void addRepositoryName(List<String> repositoryName) {
		this.getRepositoryName().add(repositoryName);
	}*/
    /**
     * @return {@link List<List<String>>} the repositoryRole to get
     */
    public List<List<String>> getRepositoryRole() {
        if (this.repositoryRole == null) {
            this.repositoryRole = new ArrayList<List<String>>();
        }
        return this.repositoryRole;
    }

    /**
     * @param repositoryRole {@link List<List<String>>} the repositoryRole to
     * set
     */
    public void setRepositoryRole(List<List<String>> repositoryRole) {
        this.repositoryRole = repositoryRole;
    }

    /**
     * @param repositoryRole the repositoryRole to add
     */
    /*public void addRepositoryRole(List<String> repositoryRole) {
		this.getRepositoryRole().add(repositoryRole);
	}*/
    /**
     * @return {@link  List<String>} the nonpreform to get
     */
    public List<String> getNonpreform() {
        if (this.nonpreform == null) {
            this.nonpreform = new ArrayList<String>();
        }
        return this.nonpreform;
    }

    /**
     * @param nonpreform {@link  List<String>} the nonpreform to set
     */
    public void setNonpreform(List<String> nonpreform) {
        this.nonpreform = nonpreform;
    }

    /**
     * @param nonpreform the nonpreform to add
     */
    /*public void addNonpreform(String nonpreform) {
		this.getNonpreform().add(nonpreform);
	}*/
    /**
     * @return {@link  List<String>} the nonpreformLang to get
     */
    public List<String> getNonpreformLang() {
        if (this.nonpreformLang == null) {
            this.nonpreformLang = new ArrayList<String>();
        }
        return this.nonpreformLang;
    }

    /**
     * @param nonpreformLang {@link  List<String>} the nonpreformLang to set
     */
    public void setNonpreformLang(List<String> nonpreformLang) {
        this.nonpreformLang = nonpreformLang;
    }

    /**
     * @param nonpreformLang the nonpreformLang to add
     */
    /*public void addNonpreformLang(String nonpreformLang) {
		this.getNonpreformLang().add(nonpreformLang);
	}*/
    /**
     * @return {@link List<List<String>>} the nonpreformDate to get
     */
    public List<List<String>> getNonpreformDate() {
        if (this.nonpreformDate == null) {
            this.nonpreformDate = new ArrayList<List<String>>();

        }
        return this.nonpreformDate;
    }

    /**
     * @param nonpreformDate {@link List<List<String>>} the nonpreformDate to
     * set
     */
    public void setNonpreformDate(List<List<String>> nonpreformDate) {
        this.nonpreformDate = nonpreformDate;
    }

    /**
     * @param nonpreformDate the nonpreformDate to add
     */
    /*public void addNonpreformDate(List<String> nonpreformDate) {
		this.getNonpreformDate().add(nonpreformDate);
	}*/
    /**
     * @return {@link List<List<String>>} the nonpreformDateFrom to get
     */
    public List<List<String>> getNonpreformDateFrom() {
        if (this.nonpreformDateFrom == null) {
            this.nonpreformDateFrom = new ArrayList<List<String>>();
        }
        return this.nonpreformDateFrom;
    }

    /**
     * @param nonpreformDateFrom {@link List<List<String>>} the
     * nonpreformDateFrom to set
     */
    public void setNonpreformDateFrom(List<List<String>> nonpreformDateFrom) {
        this.nonpreformDateFrom = nonpreformDateFrom;
    }

    /**
     * @param nonpreformDateFrom the nonpreformDateFrom to add
     */
    /*public void addNonpreformDateFrom(List<String> nonpreformDateFrom) {
		this.getNonpreformDateFrom().add(nonpreformDateFrom);
	}*/
    /**
     * @return {@link List<List<String>>} the nonpreformDateTo to get
     */
    public List<List<String>> getNonpreformDateTo() {
        if (this.nonpreformDateTo == null) {
            this.nonpreformDateTo = new ArrayList<List<String>>();
        }
        return this.nonpreformDateTo;
    }

    /**
     * @param nonpreformDateTo {@link List<List<String>>} the nonpreformDateTo
     * to set
     */
    public void setNonpreformDateTo(List<List<String>> nonpreformDateTo) {
        this.nonpreformDateTo = nonpreformDateTo;
    }

    /**
     * @param nonpreformDateTo the nonpreformDateTo to add
     */
    /*public void addNonpreformDateTo(List<String> nonpreformDateTo) {
		this.getNonpreformDateTo().add(nonpreformDateTo);
	}*/
    /**
     *
     * @return {@link List<String>} RepositoryType to get
     */
    public List<String> getRepositoryType() {
        if (this.repositoryType == null) {
            this.repositoryType = new ArrayList<String>();
        }
        return this.repositoryType;
    }

    /**
     *
     * @param repositoryType {@link List<String>} the RepositoryType to set
     */
    public void setRepositoryType(List<String> repositoryType) {
        this.repositoryType = repositoryType;
    }

    /**
     * @param repositoryType the repositoryType to add
     */
    /*public void addRepositoryType(String repositoryType) {
		this.getRepositoryType().add(repositoryType);
	}*/
    /**
     * @return {@link List<List<String>>} the contactLatitude to get
     */
    public List<List<String>> getContactLatitude() {
        if (this.contactLatitude == null) {
            this.contactLatitude = new ArrayList<List<String>>();
        }
        return this.contactLatitude;
    }

    /**
     * @param contactLatitude {@link List<List<String>>} the contactLatitude to
     * set
     */
    public void setContactLatitude(List<List<String>> contactLatitude) {
        this.contactLatitude = contactLatitude;
    }

    /**
     * @param contactLatitude the contactLatitude to add
     */
    /*public void addContactLatitude(List<String> contactLatitude) {
		this.getContactLatitude().add(contactLatitude);
	}*/
    /**
     * @return {@link List<List<String>>} the contactLongitude to get
     */
    public List<List<String>> getContactLongitude() {
        if (this.contactLongitude == null) {
            this.contactLongitude = new ArrayList<List<String>>();
        }
        return this.contactLongitude;
    }

    /**
     * @param contactLongitude {@link List<List<String>>} the contactLongitude
     * to set
     */
    public void setContactLongitude(List<List<String>> contactLongitude) {
        this.contactLongitude = contactLongitude;
    }

    /**
     * @param contactLongitude the contactLongitude to add
     */
    /*public void addContactLongitude(List<String> contactLongitude) {
		this.getContactLongitude().add(contactLongitude);
	}*/
    /**
     * @return {@link List<List<String>>} the contactCountry to get
     */
    public List<List<String>> getContactCountry() {
        if (this.contactCountry == null) {
            this.contactCountry = new ArrayList<List<String>>();
        }
        return this.contactCountry;
    }

    /**
     * @param contactCountry {@link List<List<String>>} the contactCountry to
     * set
     */
    public void setContactCountry(List<List<String>> contactCountry) {
        this.contactCountry = contactCountry;
    }

    /**
     * @param contactCountry the contactCountry to add
     */
    /*public void addContactCountry(List<String> contactCountry) {
		this.getContactCountry().add(contactCountry);
	}*/
    /**
     * @return {@link List<List<String>>} the contactCountryLang to get
     */
    public List<List<String>> getContactCountryLang() {
        if (this.contactCountryLang == null) {
            this.contactCountryLang = new ArrayList<List<String>>();
        }
        return this.contactCountryLang;
    }

    /**
     * @param contactCountryLang {@link List<List<String>>} the
     * contactCountryLang to set
     */
    public void setContactCountryLang(List<List<String>> contactCountryLang) {
        this.contactCountryLang = contactCountryLang;
    }

    /**
     * @param contactCountryLang the contactCountryLang to add
     */
    /*public void addContactCountryLang(List<String> contactCountryLang) {
		this.getContactCountryLang().add(contactCountryLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactFirstdem to get
     */
    public List<List<String>> getContactFirstdem() {
        if (this.contactFirstdem == null) {
            this.contactFirstdem = new ArrayList<List<String>>();
        }
        return this.contactFirstdem;
    }

    /**
     * @param contactFirstdem {@link List<List<String>>} the contactFirstdem to
     * set
     */
    public void setContactFirstdem(List<List<String>> contactFirstdem) {
        this.contactFirstdem = contactFirstdem;
    }

    /**
     * @param contactFirstdem the contactFirstdem to add
     */
    /*public void addContactFirstdem(List<String> contactFirstdem) {
		this.getContactFirstdem().add(contactFirstdem);
	}*/
    /**
     * @return {@link List<List<String>>} the contactFirstdemLang to get
     */
    public List<List<String>> getContactFirstdemLang() {
        if (this.contactFirstdemLang == null) {
            this.contactFirstdemLang = new ArrayList<List<String>>();
        }
        return this.contactFirstdemLang;
    }

    /**
     * @param contactFirstdemLang {@link List<List<String>>} the
     * contactFirstdemLang to set
     */
    public void setContactFirstdemLang(List<List<String>> contactFirstdemLang) {
        this.contactFirstdemLang = contactFirstdemLang;
    }

    /**
     * @param contactFirstdemLang the contactFirstdemLang to add
     */
    /*public void addContactFirstdemLang(List<String> contactFirstdemLang) {
		this.getContactFirstdemLang().add(contactFirstdemLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactSecondem to get
     */
    public List<List<String>> getContactSecondem() {
        if (this.contactSecondem == null) {
            this.contactSecondem = new ArrayList<List<String>>();
        }
        return this.contactSecondem;
    }

    /**
     * @param contactSecondem {@link List<List<String>>} the contactSecondem to
     * set
     */
    public void setContactSecondem(List<List<String>> contactSecondem) {
        this.contactSecondem = contactSecondem;
    }

    /**
     * @param contactSecondem the contactSecondem to add
     */
    /*public void addContactSecondem(List<String> contactSecondem) {
		this.getContactSecondem().add(contactSecondem);
	}*/
    /**
     * @return {@link List<List<String>>} the contactSecondemLang to get
     */
    public List<List<String>> getContactSecondemLang() {
        if (this.contactSecondemLang == null) {
            this.contactSecondemLang = new ArrayList<List<String>>();
        }
        return this.contactSecondemLang;
    }

    /**
     * @param contactSecondemLang {@link List<List<String>>} the
     * contactSecondemLang to set
     */
    public void setContactSecondemLang(List<List<String>> contactSecondemLang) {
        this.contactSecondemLang = contactSecondemLang;
    }

    /**
     * @param contactSecondemLang the contactSecondemLang to add
     */
    /*public void addContactSecondemLang(List<String> contactSecondemLang) {
		this.getContactSecondemLang().add(contactSecondemLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactMunicipality to get
     */
    public List<List<String>> getContactMunicipality() {
        if (this.contactMunicipality == null) {
            this.contactMunicipality = new ArrayList<List<String>>();
        }
        return this.contactMunicipality;
    }

    /**
     * @param contactMunicipality {@link List<List<String>>} the
     * contactMunicipality to set
     */
    public void setContactMunicipality(List<List<String>> contactMunicipality) {
        this.contactMunicipality = contactMunicipality;
    }

    /**
     * @param contactMunicipality the contactMunicipality to add
     */
    /*public void addContactMunicipality(List<String> contactMunicipality) {
		this.getContactMunicipality().add(contactMunicipality);
	}*/
    /**
     * @return {@link List<List<String>>} the contactMunicipalityLang to get
     */
    public List<List<String>> getContactMunicipalityLang() {
        if (this.contactMunicipalityLang == null) {
            this.contactMunicipalityLang = new ArrayList<List<String>>();
        }
        return this.contactMunicipalityLang;
    }

    /**
     * @param contactMunicipalityLang {@link List<List<String>>} the
     * contactMunicipalityLang to set
     */
    public void setContactMunicipalityLang(
            List<List<String>> contactMunicipalityLang) {
        this.contactMunicipalityLang = contactMunicipalityLang;
    }

    /**
     * @param contactMunicipalityLang the contactMunicipalityLang to add
     */
    /*public void addContactMunicipalityLang(List<String> contactMunicipalityLang) {
		this.getContactMunicipalityLang().add(contactMunicipalityLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactLocalentity to get
     */
    public List<List<String>> getContactLocalentity() {
        if (this.contactLocalentity == null) {
            this.contactLocalentity = new ArrayList<List<String>>();
        }
        return this.contactLocalentity;
    }

    /**
     * @param contactLocalentity {@link List<List<String>>} the
     * contactLocalentity to set
     */
    public void setContactLocalentity(List<List<String>> contactLocalentity) {
        this.contactLocalentity = contactLocalentity;
    }

    /**
     * @param contactLocalentity the contactLocalentity to add
     */
    /*public void addContactLocalentity(List<String> contactLocalentity) {
		this.getContactLocalentity().add(contactLocalentity);
	}*/
    /**
     * @return {@link List<List<String>>} the contactLocalentityLang to get
     */
    public List<List<String>> getContactLocalentityLang() {
        if (this.contactLocalentityLang == null) {
            this.contactLocalentityLang = new ArrayList<List<String>>();
        }
        return this.contactLocalentityLang;
    }

    /**
     * @param contactLocalentityLang {@link List<List<String>>} the
     * contactLocalentityLang to set
     */
    public void setContactLocalentityLang(List<List<String>> contactLocalentityLang) {
        this.contactLocalentityLang = contactLocalentityLang;
    }

    /**
     * @param contactLocalentityLang the contactLocalentityLang to add
     */
    /*public void addContactLocalentityLang(List<String> contactLocalentityLang) {
		this.getContactLocalentityLang().add(contactLocalentityLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactStreet to get
     */
    public List<List<String>> getContactStreet() {
        if (this.contactStreet == null) {
            this.contactStreet = new ArrayList<List<String>>();
        }
        return this.contactStreet;
    }

    /**
     * @param contactStreet {@link List<List<String>>} the contactStreet to set
     */
    public void setContactStreet(List<List<String>> contactStreet) {
        this.contactStreet = contactStreet;
    }

    /**
     * @param contactStreet the contactStreet to add
     */
    /*public void addContactStreet(List<String> contactStreet) {
		this.getContactStreet().add(contactStreet);
	}*/
    /**
     * @return {@link List<List<String>>} the contactStreetLang to get
     */
    public List<List<String>> getContactStreetLang() {
        if (this.contactStreetLang == null) {
            this.contactStreetLang = new ArrayList<List<String>>();
        }
        return this.contactStreetLang;
    }

    /**
     * @param contactStreetLang {@link List<List<String>>} the contactStreetLang
     * to set
     */
    public void setContactStreetLang(List<List<String>> contactStreetLang) {
        this.contactStreetLang = contactStreetLang;
    }

    /**
     * @param contactStreetLang the contactStreetLang to add
     */
    /*public void addContactStreetLang(List<String> contactStreetLang) {
		this.getContactStreetLang().add(contactStreetLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactPostalCountry to get
     */
    public List<List<String>> getContactPostalCountry() {
        if (this.contactPostalCountry == null) {
            this.contactPostalCountry = new ArrayList<List<String>>();
        }
        return this.contactPostalCountry;
    }

    /**
     * @param contactPostalCountry {@link List<List<String>>} the
     * contactPostalCountry to set
     */
    public void setContactPostalCountry(List<List<String>> contactPostalCountry) {
        this.contactPostalCountry = contactPostalCountry;
    }

    /**
     * @param contactPostalCountry the contactPostalCountry to add
     */
    /*public void addContactPostalCountry(List<String> contactPostalCountry) {
		this.getContactPostalCountry().add(contactPostalCountry);
	}*/
    /**
     * @return {@link List<List<String>>} the contactPostalCountryLang to get
     */
    public List<List<String>> getContactPostalCountryLang() {
        if (this.contactPostalCountryLang == null) {
            this.contactPostalCountryLang = new ArrayList<List<String>>();
        }
        return this.contactPostalCountryLang;
    }

    /**
     * @param contactPostalCountryLang {@link List<List<String>>} the
     * contactPostalCountryLang to set
     */
    public void setContactPostalCountryLang(
            List<List<String>> contactPostalCountryLang) {
        this.contactPostalCountryLang = contactPostalCountryLang;
    }

    /**
     * @param contactPostalCountryLang the contactPostalCountryLang to add
     */
    /*public void addContactPostalCountryLang(List<String> contactPostalCountryLang) {
		this.getContactPostalCountryLang().add(contactPostalCountryLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactPostalMunicipality to get
     */
    public List<List<String>> getContactPostalMunicipality() {
        if (this.contactPostalMunicipality == null) {
            this.contactPostalMunicipality = new ArrayList<List<String>>();
        }
        return this.contactPostalMunicipality;
    }

    /**
     * @param contactPostalMunicipality {@link List<List<String>>} the
     * contactPostalMunicipality to set
     */
    public void setContactPostalMunicipality(
            List<List<String>> contactPostalMunicipality) {
        this.contactPostalMunicipality = contactPostalMunicipality;
    }

    /**
     * @param contactPostalMunicipality the contactPostalMunicipality to add
     */
    /*public void addContactPostalMunicipality(List<String> contactPostalMunicipality) {
		this.getContactPostalMunicipality().add(contactPostalMunicipality);
	}*/
    /**
     * @return {@link List<List<String>>} the contactPostalMunicipalityLang to
     * get
     */
    public List<List<String>> getContactPostalMunicipalityLang() {
        if (this.contactPostalMunicipalityLang == null) {
            this.contactPostalMunicipalityLang = new ArrayList<List<String>>();
        }
        return this.contactPostalMunicipalityLang;
    }

    /**
     * @param contactPostalMunicipalityLang {@link List<List<String>>} the
     * contactPostalMunicipalityLang to set
     */
    public void setContactPostalMunicipalityLang(
            List<List<String>> contactPostalMunicipalityLang) {
        this.contactPostalMunicipalityLang = contactPostalMunicipalityLang;
    }

    /**
     * @param contactPostalMunicipalityLang the contactPostalMunicipalityLang to
     * add
     */
    /*public void addContactPostalMunicipalityLang(List<String> contactPostalMunicipalityLang) {
		this.getContactPostalMunicipalityLang().add(contactPostalMunicipalityLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactPostalStreet to get
     */
    public List<List<String>> getContactPostalStreet() {
        if (this.contactPostalStreet == null) {
            this.contactPostalStreet = new ArrayList<List<String>>();
        }
        return this.contactPostalStreet;
    }

    /**
     * @param contactPostalStreet {@link List<List<String>>} the
     * contactPostalStreet to set
     */
    public void setContactPostalStreet(List<List<String>> contactPostalStreet) {
        this.contactPostalStreet = contactPostalStreet;
    }

    /**
     * @param contactPostalStreet the contactPostalStreet to add
     */
    /*public void addContactPostalStreet(List<String> contactPostalStreet) {
		this.getContactPostalStreet().add(contactPostalStreet);
	}*/
    /**
     * @return {@link List<List<String>>} the contactPostalStreetLang to get
     */
    public List<List<String>> getContactPostalStreetLang() {
        if (this.contactPostalStreetLang == null) {
            this.contactPostalStreetLang = new ArrayList<List<String>>();
        }
        return this.contactPostalStreetLang;
    }

    /**
     * @param contactPostalStreetLang {@link List<List<String>>} the
     * contactPostalStreetLang to set
     */
    public void setContactPostalStreetLang(
            List<List<String>> contactPostalStreetLang) {
        this.contactPostalStreetLang = contactPostalStreetLang;
    }

    /**
     * @param contactPostalStreetLang the contactPostalStreetLang to add
     */
    /*public void addContactPostalStreetLang(List<String> contactPostalStreetLang) {
		this.getContactPostalStreetLang().add(contactPostalStreetLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactContinent to get
     */
    public List<List<String>> getContactContinent() {
        if (this.contactContinent == null) {
            this.contactContinent = new ArrayList<List<String>>();
        }
        return this.contactContinent;
    }

    /**
     * @param contactContinent {@link List<List<String>>} the contactContinent
     * to set
     */
    public void setContactContinent(List<List<String>> contactContinent) {
        this.contactContinent = contactContinent;
    }

    /**
     * @param contactPostalStreetLang the contactPostalStreetLang to add
     */
    /*public void addContactContinent(List<String> contactContinent) {
		this.getContactContinent().add(contactContinent);
	}*/
    /**
     * @return {@link List<List<String>>} the contactTelephone to get
     */
    public List<List<String>> getContactTelephone() {
        if (this.contactTelephone == null) {
            this.contactTelephone = new ArrayList<List<String>>();
        }
        return this.contactTelephone;
    }

    /**
     * @param contactTelephone {@link List<List<String>>} the contactTelephone
     * to set
     */
    public void setContactTelephone(List<List<String>> contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

    /**
     * @param contactTelephone the contactTelephone to add
     */
    /*public void addContactTelephone(List<String> contactTelephone) {
		this.getContactTelephone().add(contactTelephone);
	}*/
    /**
     * @return {@link List<List<String>>} the contactFax to get
     */
    public List<List<String>> getContactFax() {
        if (this.contactFax == null) {
            this.contactFax = new ArrayList<List<String>>();
        }
        return this.contactFax;
    }

    /**
     * @param contactFax {@link List<List<String>>} the contactFax to set
     */
    public void setContactFax(List<List<String>> contactFax) {
        this.contactFax = contactFax;
    }

    /**
     * @param contactFax the contactFax to add
     */
    /*public void addContactFax(List<String> contactFax) {
		this.getContactFax().add(contactFax);
	}*/
    /**
     * @return {@link List<List<String>>} the contactEmailHref to get
     */
    public List<List<String>> getContactEmailHref() {
        if (this.contactEmailHref == null) {
            this.contactEmailHref = new ArrayList<List<String>>();
        }
        return this.contactEmailHref;
    }

    /**
     * @param contactEmailHref {@link List<List<String>>} the contactEmailHref
     * to set
     */
    public void setContactEmailHref(List<List<String>> contactEmailHref) {
        this.contactEmailHref = contactEmailHref;
    }

    /**
     * @param contactEmailHref the contactEmailHref to add
     */
    /*public void addContactEmailHref(List<String> contactEmailHref) {
		this.getContactEmailHref().add(contactEmailHref);
	}*/
    /**
     * @return {@link List<List<String>>} the contactEmailTitle to get
     */
    public List<List<String>> getContactEmailTitle() {
        if (this.contactEmailTitle == null) {
            this.contactEmailTitle = new ArrayList<List<String>>();
        }
        return this.contactEmailTitle;
    }

    /**
     * @param contactEmailTitle {@link List<List<String>>} the contactEmailTitle
     * to set
     */
    public void setContactEmailTitle(List<List<String>> contactEmailTitle) {
        this.contactEmailTitle = contactEmailTitle;
    }

    /**
     * @param contactEmailTitle the contactEmailTitle to add
     */
    /*public void addContactEmailTitle(List<String> contactEmailTitle) {
		this.getContactEmailTitle().add(contactEmailTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the contactEmailLang to get
     */
    public List<List<String>> getContactEmailLang() {
        if (this.contactEmailLang == null) {
            this.contactEmailLang = new ArrayList<List<String>>();
        }
        return this.contactEmailLang;
    }

    /**
     * @param contactEmailLang {@link List<List<String>>} the contactEmailLang
     * to set
     */
    public void setContactEmailLang(List<List<String>> contactEmailLang) {
        this.contactEmailLang = contactEmailLang;
    }

    /**
     * @param contactEmailLang the contactEmailLang to add
     */
    /*public void addContactEmailLang(List<String> contactEmailLang) {
		this.getContactEmailLang().add(contactEmailLang);
	}
     */
    /**
     * @return {@link List<List<String>>} the contactWebpageHref to get
     */
    public List<List<String>> getContactWebpageHref() {
        if (this.contactWebpageHref == null) {
            this.contactWebpageHref = new ArrayList<List<String>>();
        }
        return this.contactWebpageHref;
    }

    /**
     * @param contactWebpageHref {@link List<List<String>>} the
     * contactWebpageHref to set
     */
    public void setContactWebpageHref(List<List<String>> contactWebpageHref) {
        this.contactWebpageHref = contactWebpageHref;
    }

    /**
     * @param contactWebpageHref the contactWebpageHref to add
     */
    /*public void addContactWebpageHref(List<String> contactWebpageHref) {
		this.getContactWebpageHref().add(contactWebpageHref);
	}*/
    /**
     * @return {@link List<List<String>>} the contactWebpageTitle to get
     */
    public List<List<String>> getContactWebpageTitle() {
        if (this.contactWebpageTitle == null) {
            this.contactWebpageTitle = new ArrayList<List<String>>();
        }
        return this.contactWebpageTitle;
    }

    /**
     * @param contactWebpageTitle {@link List<List<String>>} the
     * contactWebpageTitle to set
     */
    public void setContactWebpageTitle(List<List<String>> contactWebpageTitle) {
        this.contactWebpageTitle = contactWebpageTitle;
    }

    /**
     * @param contactWebpageTitle the contactWebpageTitle to add
     */
    /*public void addContactWebpageTitle(List<String> contactWebpageTitle) {
		this.getContactWebpageTitle().add(contactWebpageTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the contactWebpageLang to get
     */
    public List<List<String>> getContactWebpageLang() {
        if (this.contactWebpageLang == null) {
            this.contactWebpageLang = new ArrayList<List<String>>();
        }
        return this.contactWebpageLang;
    }

    /**
     * @param contactWebpageLang {@link List<List<String>>} the
     * contactWebpageLang to set
     */
    public void setContactWebpageLang(List<List<String>> contactWebpageLang) {
        this.contactWebpageLang = contactWebpageLang;
    }

    /**
     * @param contactWebpageLang the contactWebpageLang to add
     */
    /*public void addContactWebpageLang(List<String> contactWebpageLang) {
		this.getContactWebpageLang().add(contactWebpageLang);
	}*/
    /**
     * @return {@link List<List<String>>} the contactNumberOfVisitorsAddress to
     * get
     */
    public List<List<String>> getContactNumberOfVisitorsAddress() {
        if (this.contactNumberOfVisitorsAddress == null) {
            this.contactNumberOfVisitorsAddress = new ArrayList<List<String>>();
        }
        return this.contactNumberOfVisitorsAddress;
    }

    /**
     * @param contactNumberOfVisitorsAddress {@link List<List<String>>} the
     * contactNumberOfVisitorsAddress to set
     */
    public void setContactNumberOfVisitorsAddress(
            List<List<String>> contactNumberOfVisitorsAddress) {
        this.contactNumberOfVisitorsAddress = contactNumberOfVisitorsAddress;
    }

    /**
     * @param contactNumberOfVisitorsAddress the contactNumberOfVisitorsAddress
     * to add
     */
    /*public void addContactNumberOfVisitorsAddress(List<String> contactNumberOfVisitorsAddress) {
		this.getContactNumberOfVisitorsAddress().add(contactNumberOfVisitorsAddress);
	}*/
    /**
     * @return {@link List<List<String>>} the contactNumberOfPostalAddress to
     * get
     */
    public List<List<String>> getContactNumberOfPostalAddress() {
        if (this.contactNumberOfPostalAddress == null) {
            this.contactNumberOfPostalAddress = new ArrayList<List<String>>();
        }
        return this.contactNumberOfPostalAddress;
    }

    /**
     * @param contactNumberOfPostalAddress {@link List<List<String>>} the
     * contactNumberOfPostalAddress to set
     */
    public void setContactNumberOfPostalAddress(
            List<List<String>> contactNumberOfPostalAddress) {
        this.contactNumberOfPostalAddress = contactNumberOfPostalAddress;
    }

    /**
     * @param contactNumberOfPostalAddress the contactNumberOfPostalAddress to
     * add
     */
    /*public void addContactNumberOfPostalAddress(List<String> contactNumberOfPostalAddress) {
		this.getContactNumberOfPostalAddress().add(contactNumberOfPostalAddress);
	}*/
    /**
     * @return {@link List<List<String>>} the contactNumberOfEmailAddress to get
     */
    public List<List<String>> getContactNumberOfEmailAddress() {
        if (this.contactNumberOfEmailAddress == null) {
            this.contactNumberOfEmailAddress = new ArrayList<List<String>>();
        }
        return this.contactNumberOfEmailAddress;
    }

    /**
     * @param contactNumberOfEmailAddress {@link List<List<String>>} the
     * contactNumberOfEmailAddress to set
     */
    public void setContactNumberOfEmailAddress(List<List<String>> contactNumberOfEmailAddress) {
        this.contactNumberOfEmailAddress = contactNumberOfEmailAddress;
    }

    /**
     * @param contactNumberOfEmailAddress the contactNumberOfEmailAddress to add
     */
    /*public void addContactNumberOfEmailAddress(List<String> contactNumberOfEmailAddress) {
		this.getContactNumberOfEmailAddress().add(contactNumberOfEmailAddress);
	}*/
    /**
     * @return {@link List<List<String>>} the contactNumberOfWebpageAddress to
     * get
     */
    public List<List<String>> getContactNumberOfWebpageAddress() {
        if (this.contactNumberOfWebpageAddress == null) {
            this.contactNumberOfWebpageAddress = new ArrayList<List<String>>();
        }
        return this.contactNumberOfWebpageAddress;
    }

    /**
     * @param contactNumberOfWebpageAddress {@link List<List<String>>} the
     * contactNumberOfWebpageAddress to set
     */
    public void setContactNumberOfWebpageAddress(List<List<String>> contactNumberOfWebpageAddress) {
        this.contactNumberOfWebpageAddress = contactNumberOfWebpageAddress;
    }

    /**
     * @return the number of Opening instances from the Access and Services tab
     */
    public List<List<String>> getAsNumberOfOpening() {
        if (this.asNumberOfOpening == null) {
            this.asNumberOfOpening = new ArrayList<>();
        }
        return asNumberOfOpening;
    }

    /**
     * @param asNumberOfOpening the number of Opening instances for the Access and Services tab
     */
    public void setAsNumberOfOpening(List<List<String>> asNumberOfOpening) {
        this.asNumberOfOpening = asNumberOfOpening;
    }

    /**
     * @return {@link List<List<String>>} the asOpeningContent to get
     */
    public List<List<String>> getAsOpeningContent() {
        if (this.asOpeningContent == null) {
            this.asOpeningContent = new ArrayList<>();
        }
        return this.asOpeningContent;
    }

    /**
     * @param asOpeningContent {@link List<List<String>>} the asOpeningContent
     * to set
     */
    public void setAsOpeningContent(List<List<String>> asOpeningContent) {
        this.asOpeningContent = asOpeningContent;
    }

    /**
     * @return {@link List<List<String>>} the asOpeningLang to get
     */
    public List<List<String>> getAsOpeningLang() {
        if (this.asOpeningLang == null) {
            this.asOpeningLang = new ArrayList<>();
        }
        return this.asOpeningLang;
    }

    /**
     * @param asOpeningLang {@link List<List<String>>} the asOpeningLang to set
     */
    public void setAsOpeningLang(List<List<String>> asOpeningLang) {
        this.asOpeningLang = asOpeningLang;
    }
    
    /**
     * @return {@link List<List<String>>} the asOpeningHref to get
     */
    public List<List<String>> getAsOpeningHref() {
        if (this.asOpeningHref == null) {
            this.asOpeningHref = new ArrayList<>();
        }
        return asOpeningHref;
    }

    /**
     * @param asOpeningHref {@link List<List<String>>} the asOpeningHref to set
     */
    public void setAsOpeningHref(List<List<String>> asOpeningHref) {
        this.asOpeningHref = asOpeningHref;
    }

    /**
     * @return {@link List<List<String>>} the asClosing to get
     */
    public List<List<String>> getAsClosing() {
        if (this.asClosing == null) {
            this.asClosing = new ArrayList<List<String>>();
        }
        return this.asClosing;
    }

    /**
     * @param asClosing {@link List<List<String>>} the asClosing to set
     */
    public void setAsClosing(List<List<String>> asClosing) {
        this.asClosing = asClosing;
    }

    /**
     * @param asClosing the asClosing to add
     */
    /*public void addAsClosing(List<String> asClosing) {
		this.getAsClosing().add(asClosing);
	}*/
    /**
     * @return {@link List<List<String>>} the asClosingLang to get
     */
    public List<List<String>> getAsClosingLang() {
        if (this.asClosingLang == null) {
            this.asClosingLang = new ArrayList<List<String>>();
        }
        return this.asClosingLang;
    }

    /**
     * @param asClosingLang {@link List<List<String>>} the asClosingLang to set
     */
    public void setAsClosingLang(List<List<String>> asClosingLang) {
        this.asClosingLang = asClosingLang;
    }

    /**
     * @param asClosingLang the asClosingLang to add
     */
    /*public void addAsClosingLang(List<String> asClosingLang) {
		this.getAsClosingLang().add(asClosingLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asNumberOfDirections to get
     */
    public List<List<String>> getAsNumberOfDirections() {
        if (this.asNumberOfDirections == null) {
            this.asNumberOfDirections = new ArrayList<List<String>>();
        }
        return this.asNumberOfDirections;
    }

    /**
     * @param asNumberOfDirections {@link List<List<String>>} the
     * asNumberOfDirections to set
     */
    public void setAsNumberOfDirections(List<List<String>> asNumberOfDirections) {
        this.asNumberOfDirections = asNumberOfDirections;
    }

    /**
     * @param asNumberOfDirections the asNumberOfDirections to add
     */
    /*public void addAsNumberOfDirections(List<String> asNumberOfDirections) {
		this.getAsNumberOfDirections().add(asNumberOfDirections);
	}*/
    /**
     * @return {@link List<List<String>>} the asDirections to get
     */
    public List<List<String>> getAsDirections() {
        if (this.asDirections == null) {
            this.asDirections = new ArrayList<List<String>>();
        }
        return this.asDirections;
    }

    /**
     * @param asDirections {@link List<List<String>>} the asDirections to set
     */
    public void setAsDirections(List<List<String>> asDirections) {
        this.asDirections = asDirections;
    }

    /**
     * @param asDirections the asDirections to add
     */
    /*public void addAsDirections(List<String> asDirections) {
		this.getAsDirections().add(asDirections);
	}*/
    /**
     * @return {@link List<List<String>>} the asDirectionsLang to get
     */
    public List<List<String>> getAsDirectionsLang() {
        if (this.asDirectionsLang == null) {
            this.asDirectionsLang = new ArrayList<List<String>>();
        }
        return this.asDirectionsLang;
    }

    /**
     * @param asDirectionsLang {@link List<List<String>>} the asDirectionsLang
     * to set
     */
    public void setAsDirectionsLang(List<List<String>> asDirectionsLang) {
        this.asDirectionsLang = asDirectionsLang;
    }

    /**
     * @param asDirectionsLang the asDirectionsLang to add
     */
    /*public void addAsDirectionsLang(List<String> asDirectionsLang) {
		this.getAsDirectionsLang().add(asDirectionsLang);
	}
	 **/
    /**
     * @return {@link List<List<String>>} the asDirectionsCitationHref to get
     */
    public List<List<String>> getAsDirectionsCitationHref() {
        if (this.asDirectionsCitationHref == null) {
            this.asDirectionsCitationHref = new ArrayList<List<String>>();
        }
        return this.asDirectionsCitationHref;
    }

    /**
     * @param asDirectionsCitationHref {@link List<List<String>>} the
     * asDirectionsCitationHref to set
     */
    public void setAsDirectionsCitationHref(
            List<List<String>> asDirectionsCitationHref) {
        this.asDirectionsCitationHref = asDirectionsCitationHref;
    }

    /**
     * @param asDirectionsCitationHref the asDirectionsCitationHref to add
     */
    /*public void addAsDirectionsCitationHref(List<String> asDirectionsCitationHref) {
		this.getAsDirectionsCitationHref().add(asDirectionsCitationHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asAccessQuestion to get
     */
    public List<List<String>> getAsAccessQuestion() {
        if (this.asAccessQuestion == null) {
            this.asAccessQuestion = new ArrayList<List<String>>();
        }
        return this.asAccessQuestion;
    }

    /**
     * @param asAccessQuestion {@link List<List<String>>} the asAccessQuestion
     * to set
     */
    public void setAsAccessQuestion(List<List<String>> asAccessQuestion) {
        this.asAccessQuestion = asAccessQuestion;
    }

    /**
     * @param asAccessQuestion the asAccessQuestion to add
     */
    /*public void addAsAccessQuestion(List<String> asAccessQuestion) {
		this.getAsAccessQuestion().add(asAccessQuestion);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestaccess to get
     */
    public List<List<String>> getAsRestaccess() {
        if (this.asRestaccess == null) {
            this.asRestaccess = new ArrayList<List<String>>();
        }
        return this.asRestaccess;
    }

    /**
     * @param asRestaccess {@link List<List<String>>} the asRestaccess to set
     */
    public void setAsRestaccess(List<List<String>> asRestaccess) {
        this.asRestaccess = asRestaccess;
    }

    /**
     * @param asRestaccess the asRestaccess to add
     */
    /*public void addAsRestaccess(List<String> asRestaccess) {
		this.getAsRestaccess().add(asRestaccess);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestaccessLang to get
     */
    public List<List<String>> getAsRestaccessLang() {
        if (this.asRestaccessLang == null) {
            this.asRestaccessLang = new ArrayList<List<String>>();
        }
        return this.asRestaccessLang;
    }

    /**
     * @param asRestaccessLang {@link List<List<String>>} the asRestaccessLang
     * to set
     */
    public void setAsRestaccessLang(List<List<String>> asRestaccessLang) {
        this.asRestaccessLang = asRestaccessLang;
    }

    /**
     * @param asRestaccessLang the asRestaccessLang to add
     */
    /*public void addAsRestaccessLang(List<String> asRestaccessLang) {
		this.getAsRestaccessLang().add(asRestaccessLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asNumberOfTermsOfUse to get
     */
    public List<List<String>> getAsNumberOfTermsOfUse() {
        if (this.asNumberOfTermsOfUse == null) {
            this.asNumberOfTermsOfUse = new ArrayList<List<String>>();
        }
        return this.asNumberOfTermsOfUse;
    }

    /**
     * @param asNumberOfTermsOfUse {@link List<List<String>>} the
     * asNumberOfTermsOfUse to set
     */
    public void setAsNumberOfTermsOfUse(List<List<String>> asNumberOfTermsOfUse) {
        this.asNumberOfTermsOfUse = asNumberOfTermsOfUse;
    }

    /**
     * @param asNumberOfTermsOfUse the asNumberOfTermsOfUse to add
     */
    /*public void addAsNumberOfTermsOfUse(List<String> asNumberOfTermsOfUse) {
		this.getAsNumberOfTermsOfUse().add(asNumberOfTermsOfUse);
	}*/
    /**
     * @return {@link List<List<String>>} the asTermsOfUse to get
     */
    public List<List<String>> getAsTermsOfUse() {
        if (this.asTermsOfUse == null) {
            this.asTermsOfUse = new ArrayList<List<String>>();
        }
        return this.asTermsOfUse;
    }

    /**
     * @param asTermsOfUse {@link List<List<String>>} the asTermsOfUse to set
     */
    public void setAsTermsOfUse(List<List<String>> asTermsOfUse) {
        this.asTermsOfUse = asTermsOfUse;
    }

    /**
     * @param asTermsOfUse the asTermsOfUse to add
     */
    /*public void addAsTermsOfUse(List<String> asTermsOfUse) {
		this.getAsTermsOfUse().add(asTermsOfUse);
	}*/
    /**
     * @return {@link List<List<String>>} the asTermsOfUseLang to get
     */
    public List<List<String>> getAsTermsOfUseLang() {
        if (this.asTermsOfUseLang == null) {
            this.asTermsOfUseLang = new ArrayList<List<String>>();
        }
        return this.asTermsOfUseLang;
    }

    /**
     * @param asTermsOfUseLang {@link List<List<String>>} the asTermsOfUseLang
     * to set
     */
    public void setAsTermsOfUseLang(List<List<String>> asTermsOfUseLang) {
        this.asTermsOfUseLang = asTermsOfUseLang;
    }

    /**
     * @param asTermsOfUseLang the asTermsOfUseLang to add
     */
    /*public void addAsTermsOfUseLang(List<String> asTermsOfUseLang) {
		this.getAsTermsOfUseLang().add(asTermsOfUseLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asTermsOfUseHref to get
     */
    public List<List<String>> getAsTermsOfUseHref() {
        if (this.asTermsOfUseHref == null) {
            this.asTermsOfUseHref = new ArrayList<List<String>>();
        }
        return this.asTermsOfUseHref;
    }

    /**
     * @param asTermsOfUseHref {@link List<List<String>>} the asTermsOfUseHref
     * to set
     */
    public void setAsTermsOfUseHref(List<List<String>> asTermsOfUseHref) {
        this.asTermsOfUseHref = asTermsOfUseHref;
    }

    /**
     * @param asTermsOfUseHref the asTermsOfUseHref to add
     */
    /*public void addAsTermsOfUseHref(List<String> asTermsOfUseHref) {
		this.getAsTermsOfUseHref().add(asTermsOfUseHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asAccessibilityQuestion to get
     */
    public List<List<String>> getAsAccessibilityQuestion() {
        if (this.asAccessibilityQuestion == null) {
            this.asAccessibilityQuestion = new ArrayList<List<String>>();
        }
        return this.asAccessibilityQuestion;
    }

    /**
     * @param asAccessibilityQuestion {@link List<List<String>>} the
     * asAccessibilityQuestion to set
     */
    public void setAsAccessibilityQuestion(
            List<List<String>> asAccessibilityQuestion) {
        this.asAccessibilityQuestion = asAccessibilityQuestion;
    }

    /**
     * @param asAccessibilityQuestion the asAccessibilityQuestion to add
     */
    /*public void addAsAccessibilityQuestion(List<String> asAccessibilityQuestion) {
		this.getAsAccessibilityQuestion().add(asAccessibilityQuestion);
	}*/
    /**
     * @return {@link List<List<String>>} the asAccessibility to get
     */
    public List<List<String>> getAsAccessibility() {
        if (this.asAccessibility == null) {
            this.asAccessibility = new ArrayList<List<String>>();
        }
        return this.asAccessibility;
    }

    /**
     * @param asAccessibility {@link List<List<String>>} the asAccessibility to
     * set
     */
    public void setAsAccessibility(List<List<String>> asAccessibility) {
        this.asAccessibility = asAccessibility;
    }

    /**
     * @param asAccessibility the asAccessibility to add
     */
    /*public void addAsAccessibility(List<String> asAccessibility) {
		this.getAsAccessibility().add(asAccessibility);
	}*/
    /**
     * @return {@link List<List<String>>} the asAccessibilityLang to get
     */
    public List<List<String>> getAsAccessibilityLang() {
        if (this.asAccessibilityLang == null) {
            this.asAccessibilityLang = new ArrayList<List<String>>();
        }
        return this.asAccessibilityLang;
    }

    /**
     * @param asAccessibilityLang {@link List<List<String>>} the
     * asAccessibilityLang to set
     */
    public void setAsAccessibilityLang(List<List<String>> asAccessibilityLang) {
        this.asAccessibilityLang = asAccessibilityLang;
    }

    /**
     * @param asAccessibilityLang the asAccessibilityLang to add
     */
    /*public void addAsAccessibilityLang(List<String> asAccessibilityLang) {
		this.getAsAccessibilityLang().add(asAccessibilityLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomTelephone to get
     */
    public List<List<String>> getAsSearchRoomTelephone() {
        if (this.asSearchRoomTelephone == null) {
            this.asSearchRoomTelephone = new ArrayList<List<String>>();
        }
        return this.asSearchRoomTelephone;
    }

    /**
     * @param asSearchRoomTelephone {@link List<List<String>>} the
     * asSearchRoomTelephone to set
     */
    public void setAsSearchRoomTelephone(List<List<String>> asSearchRoomTelephone) {
        this.asSearchRoomTelephone = asSearchRoomTelephone;
    }

    /**
     * @param asSearchRoomTelephone the asSearchRoomTelephone to add
     */
    /*public void addAsSearchRoomTelephone(List<String> asSearchRoomTelephone) {
		this.getAsSearchRoomTelephone().add(asSearchRoomTelephone);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomNumberOfEmail to get
     */
    public List<List<String>> getAsSearchRoomNumberOfEmail() {
        if (this.asSearchRoomNumberOfEmail == null) {
            this.asSearchRoomNumberOfEmail = new ArrayList<List<String>>();
        }
        return this.asSearchRoomNumberOfEmail;
    }

    /**
     * @param asSearchRoomNumberOfEmail {@link List<List<String>>} the
     * asSearchRoomNumberOfEmail to set
     */
    public void setAsSearchRoomNumberOfEmail(
            List<List<String>> asSearchRoomNumberOfEmail) {
        this.asSearchRoomNumberOfEmail = asSearchRoomNumberOfEmail;
    }

    /**
     * @param asSearchRoomNumberOfEmail the asSearchRoomNumberOfEmail to add
     */
    /*public void addAsSearchRoomNumberOfEmail(List<String> asSearchRoomNumberOfEmail) {
		this.getAsSearchRoomNumberOfEmail().add(asSearchRoomNumberOfEmail);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomEmailHref to get
     */
    public List<List<String>> getAsSearchRoomEmailHref() {
        if (this.asSearchRoomEmailHref == null) {
            this.asSearchRoomEmailHref = new ArrayList<List<String>>();
        }
        return this.asSearchRoomEmailHref;
    }

    /**
     * @param asSearchRoomEmailHref {@link List<List<String>>} the
     * asSearchRoomEmailHref to set
     */
    public void setAsSearchRoomEmailHref(List<List<String>> asSearchRoomEmailHref) {
        this.asSearchRoomEmailHref = asSearchRoomEmailHref;
    }

    /**
     * @param asSearchRoomEmailHref the asSearchRoomEmailHref to add
     */
    /*public void addAsSearchRoomEmailHref(List<String> asSearchRoomEmailHref) {
		this.getAsSearchRoomEmailHref().add(asSearchRoomEmailHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomEmailTitle to get
     */
    public List<List<String>> getAsSearchRoomEmailTitle() {
        if (this.asSearchRoomEmailTitle == null) {
            this.asSearchRoomEmailTitle = new ArrayList<List<String>>();
        }
        return this.asSearchRoomEmailTitle;
    }

    /**
     * @param asSearchRoomEmailTitle {@link List<List<String>>} the
     * asSearchRoomEmailTitle to set
     */
    public void setAsSearchRoomEmailTitle(List<List<String>> asSearchRoomEmailTitle) {
        this.asSearchRoomEmailTitle = asSearchRoomEmailTitle;
    }

    /**
     * @param asSearchRoomEmailTitle the asSearchRoomEmailTitle to add
     */
    /*public void addAsSearchRoomEmailTitle(List<String> asSearchRoomEmailTitle) {
		this.getAsSearchRoomEmailTitle().add(asSearchRoomEmailTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomEmailLang to get
     */
    public List<List<String>> getAsSearchRoomEmailLang() {
        if (this.asSearchRoomEmailLang == null) {
            this.asSearchRoomEmailLang = new ArrayList<List<String>>();
        }
        return this.asSearchRoomEmailLang;
    }

    /**
     * @param asSearchRoomEmailLang {@link List<List<String>>} the
     * asSearchRoomEmailLang to set
     */
    public void setAsSearchRoomEmailLang(List<List<String>> asSearchRoomEmailLang) {
        this.asSearchRoomEmailLang = asSearchRoomEmailLang;
    }

    /**
     * @param asSearchRoomEmailLang the asSearchRoomEmailLang to add
     */
    /*public void addAsSearchRoomEmailLang(List<String> asSearchRoomEmailLang) {
		this.getAsSearchRoomEmailLang().add(asSearchRoomEmailLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomNumberOfWebpage to get
     */
    public List<List<String>> getAsSearchRoomNumberOfWebpage() {
        if (this.asSearchRoomNumberOfWebpage == null) {
            this.asSearchRoomNumberOfWebpage = new ArrayList<List<String>>();
        }
        return this.asSearchRoomNumberOfWebpage;
    }

    /**
     * @param asSearchRoomNumberOfWebpage {@link List<List<String>>} the
     * asSearchRoomNumberOfWebpage to set
     */
    public void setAsSearchRoomNumberOfWebpage(
            List<List<String>> asSearchRoomNumberOfWebpage) {
        this.asSearchRoomNumberOfWebpage = asSearchRoomNumberOfWebpage;
    }

    /**
     * @param asSearchRoomNumberOfWebpage the asSearchRoomNumberOfWebpage to add
     */
    /*public void addAsSearchRoomNumberOfWebpage(List<String> asSearchRoomNumberOfWebpage) {
		this.getAsSearchRoomNumberOfWebpage().add(asSearchRoomNumberOfWebpage);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomWebpageHref to get
     */
    public List<List<String>> getAsSearchRoomWebpageHref() {
        if (this.asSearchRoomWebpageHref == null) {
            this.asSearchRoomWebpageHref = new ArrayList<List<String>>();
        }
        return this.asSearchRoomWebpageHref;
    }

    /**
     * @param asSearchRoomWebpageHref {@link List<List<String>>} the
     * asSearchRoomWebpageHref to set
     */
    public void setAsSearchRoomWebpageHref(
            List<List<String>> asSearchRoomWebpageHref) {
        this.asSearchRoomWebpageHref = asSearchRoomWebpageHref;
    }

    /**
     * @param asSearchRoomWebpageHref the asSearchRoomWebpageHref to add
     */
    /*public void addAsSearchRoomWebpageHref(List<String> asSearchRoomWebpageHref) {
		this.getAsSearchRoomWebpageHref().add(asSearchRoomWebpageHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomWebpageTitle to get
     */
    public List<List<String>> getAsSearchRoomWebpageTitle() {
        if (this.asSearchRoomWebpageTitle == null) {
            this.asSearchRoomWebpageTitle = new ArrayList<List<String>>();
        }
        return this.asSearchRoomWebpageTitle;
    }

    /**
     * @param asSearchRoomWebpageTitle {@link List<List<String>>} the
     * asSearchRoomWebpageTitle to set
     */
    public void setAsSearchRoomWebpageTitle(
            List<List<String>> asSearchRoomWebpageTitle) {
        this.asSearchRoomWebpageTitle = asSearchRoomWebpageTitle;
    }

    /**
     * @param asSearchRoomWebpageTitle the asSearchRoomWebpageTitle to add
     */
    /*public void addAsSearchRoomWebpageTitle(List<String> asSearchRoomWebpageTitle) {
		this.getAsSearchRoomWebpageTitle().add(asSearchRoomWebpageTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomWebpageLang to get
     */
    public List<List<String>> getAsSearchRoomWebpageLang() {
        if (this.asSearchRoomWebpageLang == null) {
            this.asSearchRoomWebpageLang = new ArrayList<List<String>>();
        }
        return this.asSearchRoomWebpageLang;
    }

    /**
     * @param asSearchRoomWebpageLang {@link List<List<String>>} the
     * asSearchRoomWebpageLang to set
     */
    public void setAsSearchRoomWebpageLang(
            List<List<String>> asSearchRoomWebpageLang) {
        this.asSearchRoomWebpageLang = asSearchRoomWebpageLang;
    }

    /**
     * @param asSearchRoomWebpageLang the asSearchRoomWebpageLang to add
     */
    /*public void addAsSearchRoomWebpageLang(List<String> asSearchRoomWebpageLang) {
		this.getAsSearchRoomWebpageLang().add(asSearchRoomWebpageLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomWorkPlaces to get
     */
    public List<List<String>> getAsSearchRoomWorkPlaces() {
        if (this.asSearchRoomWorkPlaces == null) {
            this.asSearchRoomWorkPlaces = new ArrayList<List<String>>();
        }
        return this.asSearchRoomWorkPlaces;
    }

    /**
     * @param asSearchRoomWorkPlaces {@link List<List<String>>} the
     * asSearchRoomWorkPlaces to set
     */
    public void setAsSearchRoomWorkPlaces(List<List<String>> asSearchRoomWorkPlaces) {
        this.asSearchRoomWorkPlaces = asSearchRoomWorkPlaces;
    }

    /**
     * @param asSearchRoomWorkPlaces the asSearchRoomWorkPlaces to add
     */
    /*public void addAsSearchRoomWorkPlaces(List<String> asSearchRoomWorkPlaces) {
		this.getAsSearchRoomWorkPlaces().add(asSearchRoomWorkPlaces);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomComputerPlaces to get
     */
    public List<List<String>> getAsSearchRoomComputerPlaces() {
        if (this.asSearchRoomComputerPlaces == null) {
            this.asSearchRoomComputerPlaces = new ArrayList<List<String>>();
        }
        return this.asSearchRoomComputerPlaces;
    }

    /**
     * @param asSearchRoomComputerPlaces {@link List<List<String>>} the
     * asSearchRoomComputerPlaces to set
     */
    public void setAsSearchRoomComputerPlaces(
            List<List<String>> asSearchRoomComputerPlaces) {
        this.asSearchRoomComputerPlaces = asSearchRoomComputerPlaces;
    }

    /**
     * @param asSearchRoomComputerPlaces the asSearchRoomComputerPlaces to add
     */
    /*public void addAsSearchRoomComputerPlaces(List<String> asSearchRoomComputerPlaces) {
		this.getAsSearchRoomComputerPlaces().add(asSearchRoomComputerPlaces);
	}*/
    /**
     * @return {@link List<List<String>>} the
     * asSearchRoomComputerPlacesDescription to get
     */
    public List<List<String>> getAsSearchRoomComputerPlacesDescription() {
        if (this.asSearchRoomComputerPlacesDescription == null) {
            this.asSearchRoomComputerPlacesDescription = new ArrayList<List<String>>();
        }
        return this.asSearchRoomComputerPlacesDescription;
    }

    /**
     * @param asSearchRoomComputerPlacesDescription {@link List<List<String>>}
     * the asSearchRoomComputerPlacesDescription to set
     */
    public void setAsSearchRoomComputerPlacesDescription(
            List<List<String>> asSearchRoomComputerPlacesDescription) {
        this.asSearchRoomComputerPlacesDescription = asSearchRoomComputerPlacesDescription;
    }

    /**
     * @param asSearchRoomComputerPlacesDescription the
     * asSearchRoomComputerPlacesDescription to add
     */
    /*public void addAsSearchRoomComputerPlacesDescription(List<String> asSearchRoomComputerPlacesDescription) {
		this.getAsSearchRoomComputerPlacesDescription().add(asSearchRoomComputerPlacesDescription);
	}*/
    /**
     * @return {@link List<List<String>>} the
     * asSearchRoomComputerPlacesDescriptionLang to get
     */
    public List<List<String>> getAsSearchRoomComputerPlacesDescriptionLang() {
        if (this.asSearchRoomComputerPlacesDescriptionLang == null) {
            this.asSearchRoomComputerPlacesDescriptionLang = new ArrayList<List<String>>();
        }
        return this.asSearchRoomComputerPlacesDescriptionLang;
    }

    /**
     * @param asSearchRoomComputerPlacesDescriptionLang
     * {@link List<List<String>>} the asSearchRoomComputerPlacesDescriptionLang
     * to set
     */
    public void setAsSearchRoomComputerPlacesDescriptionLang(
            List<List<String>> asSearchRoomComputerPlacesDescriptionLang) {
        this.asSearchRoomComputerPlacesDescriptionLang = asSearchRoomComputerPlacesDescriptionLang;
    }

    /**
     * @param asSearchRoomComputerPlacesDescriptionLang the
     * asSearchRoomComputerPlacesDescriptionLang to add
     */
    /*public void addAsSearchRoomComputerPlacesDescriptionLang(List<String> asSearchRoomComputerPlacesDescriptionLang) {
		this.getAsSearchRoomComputerPlacesDescriptionLang().add(asSearchRoomComputerPlacesDescriptionLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomMicrofilmReaders to
     * get
     */
    public List<List<String>> getAsSearchRoomMicrofilmReaders() {
        if (this.asSearchRoomMicrofilmReaders == null) {
            this.asSearchRoomMicrofilmReaders = new ArrayList<List<String>>();
        }
        return this.asSearchRoomMicrofilmReaders;
    }

    /**
     * @param asSearchRoomMicrofilmReaders {@link List<List<String>>} the
     * asSearchRoomMicrofilmReaders to set
     */
    public void setAsSearchRoomMicrofilmReaders(
            List<List<String>> asSearchRoomMicrofilmReaders) {
        this.asSearchRoomMicrofilmReaders = asSearchRoomMicrofilmReaders;
    }

    /**
     * @param asSearchRoomMicrofilmReaders the asSearchRoomMicrofilmReaders to
     * add
     */
    /*public void addAsSearchRoomMicrofilmReaders(List<String> asSearchRoomMicrofilmReaders) {
		this.getAsSearchRoomMicrofilmReaders().add(asSearchRoomMicrofilmReaders);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomPhotographAllowance to
     * get
     */
    public List<List<String>> getAsSearchRoomPhotographAllowance() {
        if (this.asSearchRoomPhotographAllowance == null) {
            this.asSearchRoomPhotographAllowance = new ArrayList<List<String>>();
        }
        return this.asSearchRoomPhotographAllowance;
    }

    /**
     * @param asSearchRoomPhotographAllowance {@link List<List<String>>} the
     * asSearchRoomPhotographAllowance to set
     */
    public void setAsSearchRoomPhotographAllowance(
            List<List<String>> asSearchRoomPhotographAllowance) {
        this.asSearchRoomPhotographAllowance = asSearchRoomPhotographAllowance;
    }

    /**
     * @param asSearchRoomPhotographAllowance the
     * asSearchRoomPhotographAllowance to add
     */
    /*public void addAsSearchRoomPhotographAllowance(List<String> asSearchRoomPhotographAllowance) {
		this.getAsSearchRoomPhotographAllowance().add(asSearchRoomPhotographAllowance);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomNumberOfReadersTicket
     * to get
     */
    public List<List<String>> getAsSearchRoomNumberOfReadersTicket() {
        if (this.asSearchRoomNumberOfReadersTicket == null) {
            this.asSearchRoomNumberOfReadersTicket = new ArrayList<List<String>>();
        }
        return this.asSearchRoomNumberOfReadersTicket;
    }

    /**
     * @param asSearchRoomNumberOfReadersTicket {@link List<List<String>>} the
     * asSearchRoomNumberOfReadersTicket to set
     */
    public void setAsSearchRoomNumberOfReadersTicket(
            List<List<String>> asSearchRoomNumberOfReadersTicket) {
        this.asSearchRoomNumberOfReadersTicket = asSearchRoomNumberOfReadersTicket;
    }

    /**
     * @param asSearchRoomNumberOfReadersTicket the
     * asSearchRoomNumberOfReadersTicket to add
     */
    /*public void addAsSearchRoomNumberOfReadersTicket(List<String> asSearchRoomNumberOfReadersTicket) {
		this.getAsSearchRoomNumberOfReadersTicket().add(asSearchRoomNumberOfReadersTicket);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomReadersTicketHref to
     * get
     */
    public List<List<String>> getAsSearchRoomReadersTicketHref() {
        if (this.asSearchRoomReadersTicketHref == null) {
            this.asSearchRoomReadersTicketHref = new ArrayList<List<String>>();
        }
        return this.asSearchRoomReadersTicketHref;
    }

    /**
     * @param asSearchRoomReadersTicketHref {@link List<List<String>>} the
     * asSearchRoomReadersTicketHref to set
     */
    public void setAsSearchRoomReadersTicketHref(
            List<List<String>> asSearchRoomReadersTicketHref) {
        this.asSearchRoomReadersTicketHref = asSearchRoomReadersTicketHref;
    }

    /**
     * @param asSearchRoomReadersTicketHref the asSearchRoomReadersTicketHref to
     * add
     */
    /*public void addAsSearchRoomReadersTicketHref(List<String> asSearchRoomReadersTicketHref) {
		this.getAsSearchRoomReadersTicketHref().add(asSearchRoomReadersTicketHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomReadersTicketContent
     * to get
     */
    public List<List<String>> getAsSearchRoomReadersTicketContent() {
        if (this.asSearchRoomReadersTicketContent == null) {
            this.asSearchRoomReadersTicketContent = new ArrayList<List<String>>();
        }
        return this.asSearchRoomReadersTicketContent;
    }

    /**
     * @param asSearchRoomReadersTicketContent {@link List<List<String>>} the
     * asSearchRoomReadersTicketContent to set
     */
    public void setAsSearchRoomReadersTicketContent(
            List<List<String>> asSearchRoomReadersTicketContent) {
        this.asSearchRoomReadersTicketContent = asSearchRoomReadersTicketContent;
    }

    /**
     * @param asSearchRoomReadersTicketContent the
     * asSearchRoomReadersTicketContent to add
     */
    /*public void addAsSearchRoomReadersTicketContent(List<String> asSearchRoomReadersTicketContent) {
		this.getAsSearchRoomReadersTicketContent().add(asSearchRoomReadersTicketContent);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomReadersTicketLang to
     * get
     */
    public List<List<String>> getAsSearchRoomReadersTicketLang() {
        if (this.asSearchRoomReadersTicketLang == null) {
            this.asSearchRoomReadersTicketLang = new ArrayList<List<String>>();
        }
        return this.asSearchRoomReadersTicketLang;
    }

    /**
     * @param asSearchRoomReadersTicketLang {@link List<List<String>>} the
     * asSearchRoomReadersTicketLang to set
     */
    public void setAsSearchRoomReadersTicketLang(
            List<List<String>> asSearchRoomReadersTicketLang) {
        this.asSearchRoomReadersTicketLang = asSearchRoomReadersTicketLang;
    }

    /**
     * @param asSearchRoomReadersTicketLang the asSearchRoomReadersTicketLang to
     * add
     */
    /*public void addAsSearchRoomReadersTicketLang(List<String> asSearchRoomReadersTicketLang) {
		this.getAsSearchRoomReadersTicketLang().add(asSearchRoomReadersTicketLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomNumberOfAdvancedOrders
     * to get
     */
    public List<List<String>> getAsSearchRoomNumberOfAdvancedOrders() {
        if (this.asSearchRoomNumberOfAdvancedOrders == null) {
            this.asSearchRoomNumberOfAdvancedOrders = new ArrayList<List<String>>();
        }
        return this.asSearchRoomNumberOfAdvancedOrders;
    }

    /**
     * @param asSearchRoomNumberOfAdvancedOrders {@link List<List<String>>} the
     * asSearchRoomNumberOfAdvancedOrders to set
     */
    public void setAsSearchRoomNumberOfAdvancedOrders(
            List<List<String>> asSearchRoomNumberOfAdvancedOrders) {
        this.asSearchRoomNumberOfAdvancedOrders = asSearchRoomNumberOfAdvancedOrders;
    }

    /**
     * @param asSearchRoomNumberOfAdvancedOrders the
     * asSearchRoomNumberOfAdvancedOrders to add
     */
    /*public void addAsSearchRoomNumberOfAdvancedOrders(List<String> asSearchRoomNumberOfAdvancedOrders) {
		this.getAsSearchRoomNumberOfAdvancedOrders().add(asSearchRoomNumberOfAdvancedOrders);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomAdvancedOrdersHref to
     * get
     */
    public List<List<String>> getAsSearchRoomAdvancedOrdersHref() {
        if (this.asSearchRoomAdvancedOrdersHref == null) {
            this.asSearchRoomAdvancedOrdersHref = new ArrayList<List<String>>();
        }
        return this.asSearchRoomAdvancedOrdersHref;
    }

    /**
     * @param asSearchRoomAdvancedOrdersHref {@link List<List<String>>} the
     * asSearchRoomAdvancedOrdersHref to set
     */
    public void setAsSearchRoomAdvancedOrdersHref(
            List<List<String>> asSearchRoomAdvancedOrdersHref) {
        this.asSearchRoomAdvancedOrdersHref = asSearchRoomAdvancedOrdersHref;
    }

    /**
     * @param asSearchRoomAdvancedOrdersHref the asSearchRoomAdvancedOrdersHref
     * to add
     */
    /*public void addAsSearchRoomAdvancedOrdersHref(List<String> asSearchRoomAdvancedOrdersHref) {
		this.getAsSearchRoomAdvancedOrdersHref().add(asSearchRoomAdvancedOrdersHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomAdvancedOrdersContent
     * to get
     */
    public List<List<String>> getAsSearchRoomAdvancedOrdersContent() {
        if (this.asSearchRoomAdvancedOrdersContent == null) {
            this.asSearchRoomAdvancedOrdersContent = new ArrayList<List<String>>();
        }
        return this.asSearchRoomAdvancedOrdersContent;
    }

    /**
     * @param asSearchRoomAdvancedOrdersContent {@link List<List<String>>} the
     * asSearchRoomAdvancedOrdersContent to set
     */
    public void setAsSearchRoomAdvancedOrdersContent(
            List<List<String>> asSearchRoomAdvancedOrdersContent) {
        this.asSearchRoomAdvancedOrdersContent = asSearchRoomAdvancedOrdersContent;
    }

    /**
     * @param asSearchRoomAdvancedOrdersContent the
     * asSearchRoomAdvancedOrdersContent to add
     */
    /*public void addAsSearchRoomAdvancedOrdersContent(List<String> asSearchRoomAdvancedOrdersContent) {
		this.getAsSearchRoomAdvancedOrdersContent().add(asSearchRoomAdvancedOrdersContent);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomAdvancedOrdersLang to
     * get
     */
    public List<List<String>> getAsSearchRoomAdvancedOrdersLang() {
        if (this.asSearchRoomAdvancedOrdersLang == null) {
            this.asSearchRoomAdvancedOrdersLang = new ArrayList<List<String>>();
        }
        return this.asSearchRoomAdvancedOrdersLang;
    }

    /**
     * @param asSearchRoomAdvancedOrdersLang {@link List<List<String>>} the
     * asSearchRoomAdvancedOrdersLang to set
     */
    public void setAsSearchRoomAdvancedOrdersLang(
            List<List<String>> asSearchRoomAdvancedOrdersLang) {
        this.asSearchRoomAdvancedOrdersLang = asSearchRoomAdvancedOrdersLang;
    }

    /**
     * @param asSearchRoomAdvancedOrdersLang the asSearchRoomAdvancedOrdersLang
     * to add
     */
    /*public void addAsSearchRoomAdvancedOrdersLang(List<String> asSearchRoomAdvancedOrdersLang) {
		this.getAsSearchRoomAdvancedOrdersLang().add(asSearchRoomAdvancedOrdersLang);
	}*/
    /**
     * @return {@link List<List<String>>} the
     * asSearchRoomResearchServicesContent to get
     */
    public List<List<String>> getAsSearchRoomResearchServicesContent() {
        if (this.asSearchRoomResearchServicesContent == null) {
            this.asSearchRoomResearchServicesContent = new ArrayList<List<String>>();
        }
        return this.asSearchRoomResearchServicesContent;
    }

    /**
     * @param asSearchRoomResearchServicesContent {@link List<List<String>>} the
     * asSearchRoomResearchServicesContent to set
     */
    public void setAsSearchRoomResearchServicesContent(
            List<List<String>> asSearchRoomResearchServicesContent) {
        this.asSearchRoomResearchServicesContent = asSearchRoomResearchServicesContent;
    }

    /**
     * @param asSearchRoomResearchServicesContent the
     * asSearchRoomResearchServicesContent to add
     */
    /*public void addAsSearchRoomResearchServicesContent(List<String> asSearchRoomResearchServicesContent) {
		this.getAsSearchRoomResearchServicesContent().add(asSearchRoomResearchServicesContent);
	}*/
    /**
     * @return {@link List<List<String>>} the asSearchRoomResearchServicesLang
     * to get
     */
    public List<List<String>> getAsSearchRoomResearchServicesLang() {
        if (this.asSearchRoomResearchServicesLang == null) {
            this.asSearchRoomResearchServicesLang = new ArrayList<List<String>>();
        }
        return this.asSearchRoomResearchServicesLang;
    }

    /**
     * @param asSearchRoomResearchServicesLang {@link List<List<String>>} the
     * asSearchRoomResearchServicesLang to set
     */
    public void setAsSearchRoomResearchServicesLang(
            List<List<String>> asSearchRoomResearchServicesLang) {
        this.asSearchRoomResearchServicesLang = asSearchRoomResearchServicesLang;
    }

    /**
     * @param asSearchRoomResearchServicesLang the
     * asSearchRoomResearchServicesLang to add
     */
    /*public void addAsSearchRoomResearchServicesLang(List<String> asSearchRoomResearchServicesLang) {
		this.getAsSearchRoomResearchServicesLang().add(asSearchRoomResearchServicesLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryQuestion to get
     */
    public List<List<String>> getAsLibraryQuestion() {
        if (this.asLibraryQuestion == null) {
            this.asLibraryQuestion = new ArrayList<List<String>>();
        }
        return this.asLibraryQuestion;
    }

    /**
     * @param asLibraryQuestion {@link List<List<String>>} the asLibraryQuestion
     * to set
     */
    public void setAsLibraryQuestion(List<List<String>> asLibraryQuestion) {
        this.asLibraryQuestion = asLibraryQuestion;
    }

    /**
     * @param asLibraryQuestion the asLibraryQuestion to add
     */
    /*public void addAsLibraryQuestion(List<String> asLibraryQuestion) {
		this.getAsLibraryQuestion().add(asLibraryQuestion);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryTelephone to get
     */
    public List<List<String>> getAsLibraryTelephone() {
        if (this.asLibraryTelephone == null) {
            this.asLibraryTelephone = new ArrayList<List<String>>();
        }
        return this.asLibraryTelephone;
    }

    /**
     * @param asLibraryTelephone {@link List<List<String>>} the
     * asLibraryTelephone to set
     */
    public void setAsLibraryTelephone(List<List<String>> asLibraryTelephone) {
        this.asLibraryTelephone = asLibraryTelephone;
    }

    /**
     * @param asLibraryTelephone the asLibraryTelephone to add
     */
    /*public void addAsLibraryTelephone(List<String> asLibraryTelephone) {
		this.getAsLibraryTelephone().add(asLibraryTelephone);
	}
     */
    /**
     * @return {@link List<List<String>>} the asLibraryNumberOfEmail to get
     */
    public List<List<String>> getAsLibraryNumberOfEmail() {
        if (this.asLibraryNumberOfEmail == null) {
            this.asLibraryNumberOfEmail = new ArrayList<List<String>>();
        }
        return this.asLibraryNumberOfEmail;
    }

    /**
     * @param asLibraryNumberOfEmail {@link List<List<String>>} the
     * asLibraryNumberOfEmail to set
     */
    public void setAsLibraryNumberOfEmail(
            List<List<String>> asLibraryNumberOfEmail) {
        this.asLibraryNumberOfEmail = asLibraryNumberOfEmail;
    }

    /**
     * @param asLibraryNumberOfEmail the asLibraryNumberOfEmail to add
     */
    /*public void addAsLibraryNumberOfEmail(List<String> asLibraryNumberOfEmail) {
		this.getAsLibraryNumberOfEmail().add(asLibraryNumberOfEmail);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryEmailHref to get
     */
    public List<List<String>> getAsLibraryEmailHref() {
        if (this.asLibraryEmailHref == null) {
            this.asLibraryEmailHref = new ArrayList<List<String>>();
        }
        return this.asLibraryEmailHref;
    }

    /**
     * @param asLibraryEmailHref {@link List<List<String>>} the
     * asLibraryEmailHref to set
     */
    public void setAsLibraryEmailHref(List<List<String>> asLibraryEmailHref) {
        this.asLibraryEmailHref = asLibraryEmailHref;
    }

    /**
     * @param asLibraryEmailHref the asLibraryEmailHref to add
     */
    /*public void addAsLibraryEmailHref(List<String> asLibraryEmailHref) {
		this.getAsLibraryEmailHref().add(asLibraryEmailHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryEmailTitle to get
     */
    public List<List<String>> getAsLibraryEmailTitle() {
        if (this.asLibraryEmailTitle == null) {
            this.asLibraryEmailTitle = new ArrayList<List<String>>();
        }
        return this.asLibraryEmailTitle;
    }

    /**
     * @param asLibraryEmailTitle {@link List<List<String>>} the
     * asLibraryEmailTitle to set
     */
    public void setAsLibraryEmailTitle(List<List<String>> asLibraryEmailTitle) {
        this.asLibraryEmailTitle = asLibraryEmailTitle;
    }

    /**
     * @param asLibraryEmailTitle the asLibraryEmailTitle to add
     */
    /*public void addAsLibraryEmailTitle(List<String> asLibraryEmailTitle) {
		this.getAsLibraryEmailTitle().add(asLibraryEmailTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryEmailLang to get
     */
    public List<List<String>> getAsLibraryEmailLang() {
        if (this.asLibraryEmailLang == null) {
            this.asLibraryEmailLang = new ArrayList<List<String>>();
        }
        return this.asLibraryEmailLang;
    }

    /**
     * @param asLibraryEmailLang {@link List<List<String>>} the
     * asLibraryEmailLang to set
     */
    public void setAsLibraryEmailLang(List<List<String>> asLibraryEmailLang) {
        this.asLibraryEmailLang = asLibraryEmailLang;
    }

    /**
     * @param asLibraryEmailLang the asLibraryEmailLang to add
     */
    /*public void addAsLibraryEmailLang(List<String> asLibraryEmailLang) {
		this.getAsLibraryEmailLang().add(asLibraryEmailLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryNumberOfWebpage to get
     */
    public List<List<String>> getAsLibraryNumberOfWebpage() {
        if (this.asLibraryNumberOfWebpage == null) {
            this.asLibraryNumberOfWebpage = new ArrayList<List<String>>();
        }
        return this.asLibraryNumberOfWebpage;
    }

    /**
     * @param asLibraryNumberOfWebpage {@link List<List<String>>} the
     * asLibraryNumberOfWebpage to set
     */
    public void setAsLibraryNumberOfWebpage(
            List<List<String>> asLibraryNumberOfWebpage) {
        this.asLibraryNumberOfWebpage = asLibraryNumberOfWebpage;
    }

    /**
     * @param asLibraryNumberOfWebpage the asLibraryNumberOfWebpage to add
     */
    /*public void addAsLibraryNumberOfWebpage(List<String> asLibraryNumberOfWebpage) {
		this.getAsLibraryNumberOfWebpage().add(asLibraryNumberOfWebpage);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryWebpageHref to get
     */
    public List<List<String>> getAsLibraryWebpageHref() {
        if (this.asLibraryWebpageHref == null) {
            this.asLibraryWebpageHref = new ArrayList<List<String>>();
        }
        return this.asLibraryWebpageHref;
    }

    /**
     * @param asLibraryWebpageHref {@link List<List<String>>} the
     * asLibraryWebpageHref to set
     */
    public void setAsLibraryWebpageHref(List<List<String>> asLibraryWebpageHref) {
        this.asLibraryWebpageHref = asLibraryWebpageHref;
    }

    /**
     * @param asLibraryWebpageHref the asLibraryWebpageHref to add
     */
    /*public void addAsLibraryWebpageHref(List<String> asLibraryWebpageHref) {
		this.getAsLibraryWebpageHref().add(asLibraryWebpageHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryWebpageTitle to get
     */
    public List<List<String>> getAsLibraryWebpageTitle() {
        if (this.asLibraryWebpageTitle == null) {
            this.asLibraryWebpageTitle = new ArrayList<List<String>>();
        }
        return this.asLibraryWebpageTitle;
    }

    /**
     * @param asLibraryWebpageTitle {@link List<List<String>>} the
     * asLibraryWebpageTitle to set
     */
    public void setAsLibraryWebpageTitle(List<List<String>> asLibraryWebpageTitle) {
        this.asLibraryWebpageTitle = asLibraryWebpageTitle;
    }

    /**
     * @param asLibraryWebpageTitle the asLibraryWebpageTitle to add
     */
    /*public void addAsLibraryWebpageTitle(List<String> asLibraryWebpageTitle) {
		this.getAsLibraryWebpageTitle().add(asLibraryWebpageTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryWebpageLang to get
     */
    public List<List<String>> getAsLibraryWebpageLang() {
        if (this.asLibraryWebpageLang == null) {
            this.asLibraryWebpageLang = new ArrayList<List<String>>();
        }
        return this.asLibraryWebpageLang;
    }

    /**
     * @param asLibraryWebpageLang {@link List<List<String>>} the
     * asLibraryWebpageLang to set
     */
    public void setAsLibraryWebpageLang(List<List<String>> asLibraryWebpageLang) {
        this.asLibraryWebpageLang = asLibraryWebpageLang;
    }

    /**
     * @param asLibraryWebpageLang the asLibraryWebpageLang to add
     */
    /*public void addAsLibraryWebpageLang(List<String> asLibraryWebpageLang) {
		this.getAsLibraryWebpageLang().add(asLibraryWebpageLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibraryMonographPublication to
     * get
     */
    public List<List<String>> getAsLibraryMonographPublication() {
        if (this.asLibraryMonographPublication == null) {
            this.asLibraryMonographPublication = new ArrayList<List<String>>();
        }
        return this.asLibraryMonographPublication;
    }

    /**
     * @param asLibraryMonographPublication {@link List<List<String>>} the
     * asLibraryMonographPublication to set
     */
    public void setAsLibraryMonographPublication(
            List<List<String>> asLibraryMonographPublication) {
        this.asLibraryMonographPublication = asLibraryMonographPublication;
    }

    /**
     * @param asLibraryMonographPublication the asLibraryMonographPublication to
     * add
     */
    /*public void addAsLibraryMonographPublication(List<String> asLibraryMonographPublication) {
		this.getAsLibraryMonographPublication().add(asLibraryMonographPublication);
	}*/
    /**
     * @return {@link List<List<String>>} the asLibrarySerialPublication to get
     */
    public List<List<String>> getAsLibrarySerialPublication() {
        if (this.asLibrarySerialPublication == null) {
            this.asLibrarySerialPublication = new ArrayList<List<String>>();
        }
        return this.asLibrarySerialPublication;
    }

    /**
     * @param asLibrarySerialPublication {@link List<List<String>>} the
     * asLibrarySerialPublication to set
     */
    public void setAsLibrarySerialPublication(
            List<List<String>> asLibrarySerialPublication) {
        this.asLibrarySerialPublication = asLibrarySerialPublication;
    }

    /**
     * @param asLibrarySerialPublication the asLibrarySerialPublication to add
     */
    /*public void addAsLibrarySerialPublication(List<String> asLibrarySerialPublication) {
		this.getAsLibrarySerialPublication().add(asLibrarySerialPublication);
	}*/
    /**
     * @return {@link List<List<String>>} the asInternetAccessQuestion to get
     */
    public List<List<String>> getAsInternetAccessQuestion() {
        if (this.asInternetAccessQuestion == null) {
            this.asInternetAccessQuestion = new ArrayList<List<String>>();
        }
        return this.asInternetAccessQuestion;
    }

    /**
     * @param asInternetAccessQuestion {@link List<List<String>>} the
     * asInternetAccessQuestion to set
     */
    public void setAsInternetAccessQuestion(
            List<List<String>> asInternetAccessQuestion) {
        this.asInternetAccessQuestion = asInternetAccessQuestion;
    }

    /**
     * @param asInternetAccessQuestion the asInternetAccessQuestion to add
     */
    /*	public void addAsInternetAccessQuestion(List<String> asInternetAccessQuestion) {
		this.getAsInternetAccessQuestion().add(asInternetAccessQuestion);
	}*/
    /**
     * @return {@link List<List<String>>} the asInternetAccessDescription to get
     */
    public List<List<String>> getAsInternetAccessDescription() {
        if (this.asInternetAccessDescription == null) {
            this.asInternetAccessDescription = new ArrayList<List<String>>();
        }
        return this.asInternetAccessDescription;
    }

    /**
     * @param asInternetAccessDescription {@link List<List<String>>} the
     * asInternetAccessDescription to set
     */
    public void setAsInternetAccessDescription(
            List<List<String>> asInternetAccessDescription) {
        this.asInternetAccessDescription = asInternetAccessDescription;
    }

    /**
     * @param asInternetAccessDescription the asInternetAccessDescription to add
     */
    /*public void addAsInternetAccessDescription(List<String> asInternetAccessDescription) {
		this.getAsInternetAccessDescription().add(asInternetAccessDescription);
	}*/
    /**
     * @return {@link List<List<String>>} the asInternetAccessDescriptionLang to
     * get
     */
    public List<List<String>> getAsInternetAccessDescriptionLang() {
        if (this.asInternetAccessDescriptionLang == null) {
            this.asInternetAccessDescriptionLang = new ArrayList<List<String>>();
        }
        return this.asInternetAccessDescriptionLang;
    }

    /**
     * @param asInternetAccessDescriptionLang {@link List<List<String>>} the
     * asInternetAccessDescriptionLang to set
     */
    public void setAsInternetAccessDescriptionLang(
            List<List<String>> asInternetAccessDescriptionLang) {
        this.asInternetAccessDescriptionLang = asInternetAccessDescriptionLang;
    }

    /**
     * @param asInternetAccessDescriptionLang the
     * asInternetAccessDescriptionLang to add
     */
    /*public void addAsInternetAccessDescriptionLang(List<String> asInternetAccessDescriptionLang) {
		this.getAsInternetAccessDescriptionLang().add(asInternetAccessDescriptionLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabQuestion to get
     */
    public List<List<String>> getAsRestorationlabQuestion() {
        if (this.asRestorationlabQuestion == null) {
            this.asRestorationlabQuestion = new ArrayList<List<String>>();
        }
        return this.asRestorationlabQuestion;
    }

    /**
     * @param asRestorationlabQuestion {@link List<List<String>>} the
     * asRestorationlabQuestion to set
     */
    public void setAsRestorationlabQuestion(
            List<List<String>> asRestorationlabQuestion) {
        this.asRestorationlabQuestion = asRestorationlabQuestion;
    }

    /**
     * @param asRestorationlabQuestion the asRestorationlabQuestion to add
     */
    /*public void addAsRestorationlabQuestion(List<String> asRestorationlabQuestion) {
		this.getAsRestorationlabQuestion().add(asRestorationlabQuestion);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabDescription to get
     */
    public List<List<String>> getAsRestorationlabDescription() {
        if (this.asRestorationlabDescription == null) {
            this.asRestorationlabDescription = new ArrayList<List<String>>();
        }
        return this.asRestorationlabDescription;
    }

    /**
     * @param asRestorationlabDescription {@link List<List<String>>} the
     * asRestorationlabDescription to set
     */
    public void setAsRestorationlabDescription(
            List<List<String>> asRestorationlabDescription) {
        this.asRestorationlabDescription = asRestorationlabDescription;
    }

    /**
     * @param asRestorationlabDescription the asRestorationlabDescription to add
     */
    /*public void addAsRestorationlabDescription(List<String> asRestorationlabDescription) {
		this.getAsRestorationlabDescription().add(asRestorationlabDescription);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabDescriptionLang to
     * get
     */
    public List<List<String>> getAsRestorationlabDescriptionLang() {
        if (this.asRestorationlabDescriptionLang == null) {
            this.asRestorationlabDescriptionLang = new ArrayList<List<String>>();
        }
        return this.asRestorationlabDescriptionLang;
    }

    /**
     * @param asRestorationlabDescriptionLang {@link List<List<String>>} the
     * asRestorationlabDescriptionLang to set
     */
    public void setAsRestorationlabDescriptionLang(
            List<List<String>> asRestorationlabDescriptionLang) {
        this.asRestorationlabDescriptionLang = asRestorationlabDescriptionLang;
    }

    /**
     * @param asRestorationlabDescriptionLang the
     * asRestorationlabDescriptionLang to add
     */
    /*public void addAsRestorationlabDescriptionLang(List<String> asRestorationlabDescriptionLang) {
		this.getAsRestorationlabDescriptionLang().add(asRestorationlabDescriptionLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabTelephone to get
     */
    public List<List<String>> getAsRestorationlabTelephone() {
        if (this.asRestorationlabTelephone == null) {
            this.asRestorationlabTelephone = new ArrayList<List<String>>();
        }
        return this.asRestorationlabTelephone;
    }

    /**
     * @param asRestorationlabTelephone {@link List<List<String>>} the
     * asRestorationlabTelephone to set
     */
    public void setAsRestorationlabTelephone(
            List<List<String>> asRestorationlabTelephone) {
        this.asRestorationlabTelephone = asRestorationlabTelephone;
    }

    /**
     * @param asRestorationlabTelephone the asRestorationlabTelephone to add
     */
    /*public void addAsRestorationlabTelephone(List<String> asRestorationlabTelephone) {
		this.getAsRestorationlabTelephone().add(asRestorationlabTelephone);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabNumberOfEmail to
     * get
     */
    public List<List<String>> getAsRestorationlabNumberOfEmail() {
        if (this.asRestorationlabNumberOfEmail == null) {
            this.asRestorationlabNumberOfEmail = new ArrayList<List<String>>();
        }
        return this.asRestorationlabNumberOfEmail;
    }

    /**
     * @param asRestorationlabNumberOfEmail {@link List<List<String>>} the
     * asRestorationlabNumberOfEmail to set
     */
    public void setAsRestorationlabNumberOfEmail(
            List<List<String>> asRestorationlabNumberOfEmail) {
        this.asRestorationlabNumberOfEmail = asRestorationlabNumberOfEmail;
    }

    /**
     * @param asRestorationlabNumberOfEmail the asRestorationlabNumberOfEmail to
     * add
     */
    /*public void addAsRestorationlabNumberOfEmail(List<String> asRestorationlabNumberOfEmail) {
		this.getAsRestorationlabNumberOfEmail().add(asRestorationlabNumberOfEmail);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabEmailHref to get
     */
    public List<List<String>> getAsRestorationlabEmailHref() {
        if (this.asRestorationlabEmailHref == null) {
            this.asRestorationlabEmailHref = new ArrayList<List<String>>();
        }
        return this.asRestorationlabEmailHref;
    }

    /**
     * @param asRestorationlabEmailHref {@link List<List<String>>} the
     * asRestorationlabEmailHref to set
     */
    public void setAsRestorationlabEmailHref(
            List<List<String>> asRestorationlabEmailHref) {
        this.asRestorationlabEmailHref = asRestorationlabEmailHref;
    }

    /**
     * @param asRestorationlabEmailHref the asRestorationlabEmailHref to add
     */
    /*public void addAsRestorationlabEmailHref(List<String> asRestorationlabEmailHref) {
		this.getAsRestorationlabEmailHref().add(asRestorationlabEmailHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabEmailTitle to get
     */
    public List<List<String>> getAsRestorationlabEmailTitle() {
        if (this.asRestorationlabEmailTitle == null) {
            this.asRestorationlabEmailTitle = new ArrayList<List<String>>();
        }
        return this.asRestorationlabEmailTitle;
    }

    /**
     * @param asRestorationlabEmailTitle {@link List<List<String>>} the
     * asRestorationlabEmailTitle to set
     */
    public void setAsRestorationlabEmailTitle(
            List<List<String>> asRestorationlabEmailTitle) {
        this.asRestorationlabEmailTitle = asRestorationlabEmailTitle;
    }

    /**
     * @param asRestorationlabEmailTitle the asRestorationlabEmailTitle to add
     */
    /*public void addAsRestorationlabEmailTitle(List<String> asRestorationlabEmailTitle) {
		this.getAsRestorationlabEmailTitle().add(asRestorationlabEmailTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabEmailLang to get
     */
    public List<List<String>> getAsRestorationlabEmailLang() {
        if (this.asRestorationlabEmailLang == null) {
            this.asRestorationlabEmailLang = new ArrayList<List<String>>();
        }
        return this.asRestorationlabEmailLang;
    }

    /**
     * @param asRestorationlabEmailLang {@link List<List<String>>} the
     * asRestorationlabEmailLang to set
     */
    public void setAsRestorationlabEmailLang(
            List<List<String>> asRestorationlabEmailLang) {
        this.asRestorationlabEmailLang = asRestorationlabEmailLang;
    }

    /**
     * @param asRestorationlabEmailLang the asRestorationlabEmailLang to add
     */
    /*public void addAsRestorationlabEmailLang(List<String> asRestorationlabEmailLang) {
		this.getAsRestorationlabEmailLang().add(asRestorationlabEmailLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabNumberOfWebpage to
     * get
     */
    public List<List<String>> getAsRestorationlabNumberOfWebpage() {
        if (this.asRestorationlabNumberOfWebpage == null) {
            this.asRestorationlabNumberOfWebpage = new ArrayList<List<String>>();
        }
        return this.asRestorationlabNumberOfWebpage;
    }

    /**
     * @param asRestorationlabNumberOfWebpage {@link List<List<String>>} the
     * asRestorationlabNumberOfWebpage to set
     */
    public void setAsRestorationlabNumberOfWebpage(
            List<List<String>> asRestorationlabNumberOfWebpage) {
        this.asRestorationlabNumberOfWebpage = asRestorationlabNumberOfWebpage;
    }

    /**
     * @param asRestorationlabNumberOfWebpage the
     * asRestorationlabNumberOfWebpage to add
     */
    /*public void addAsRestorationlabNumberOfWebpage(List<String> asRestorationlabNumberOfWebpage) {
		this.getAsRestorationlabNumberOfWebpage().add(asRestorationlabNumberOfWebpage);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabWebpageHref to get
     */
    public List<List<String>> getAsRestorationlabWebpageHref() {
        if (this.asRestorationlabWebpageHref == null) {
            this.asRestorationlabWebpageHref = new ArrayList<List<String>>();
        }
        return this.asRestorationlabWebpageHref;
    }

    /**
     * @param asRestorationlabWebpageHref {@link List<List<String>>} the
     * asRestorationlabWebpageHref to set
     */
    public void setAsRestorationlabWebpageHref(
            List<List<String>> asRestorationlabWebpageHref) {
        this.asRestorationlabWebpageHref = asRestorationlabWebpageHref;
    }

    /**
     * @param asRestorationlabWebpageHref the asRestorationlabWebpageHref to add
     */
    /*public void addAsRestorationlabWebpageHref(List<String> asRestorationlabWebpageHref) {
		this.getAsRestorationlabWebpageHref().add(asRestorationlabWebpageHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabWebpageTitle to
     * get
     */
    public List<List<String>> getAsRestorationlabWebpageTitle() {
        if (this.asRestorationlabWebpageTitle == null) {
            this.asRestorationlabWebpageTitle = new ArrayList<List<String>>();
        }
        return this.asRestorationlabWebpageTitle;
    }

    /**
     * @param asRestorationlabWebpageTitle {@link List<List<String>>} the
     * asRestorationlabWebpageTitle to set
     */
    public void setAsRestorationlabWebpageTitle(
            List<List<String>> asRestorationlabWebpageTitle) {
        this.asRestorationlabWebpageTitle = asRestorationlabWebpageTitle;
    }

    /**
     * @param asRestorationlabWebpageTitle the asRestorationlabWebpageTitle to
     * add
     */
    /*public void addAsRestorationlabWebpageTitle(List<String> asRestorationlabWebpageTitle) {
		this.getAsRestorationlabWebpageTitle().add(asRestorationlabWebpageTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asRestorationlabWebpageLang to get
     */
    public List<List<String>> getAsRestorationlabWebpageLang() {
        if (this.asRestorationlabWebpageLang == null) {
            this.asRestorationlabWebpageLang = new ArrayList<List<String>>();
        }
        return this.asRestorationlabWebpageLang;
    }

    /**
     * @param asRestorationlabWebpageLang {@link List<List<String>>} the
     * asRestorationlabWebpageLang to set
     */
    public void setAsRestorationlabWebpageLang(
            List<List<String>> asRestorationlabWebpageLang) {
        this.asRestorationlabWebpageLang = asRestorationlabWebpageLang;
    }

    /**
     * @param asRestorationlabWebpageLang the asRestorationlabWebpageLang to add
     */
    /*public void addAsRestorationlabWebpageLang(List<String> asRestorationlabWebpageLang) {
		this.getAsRestorationlabWebpageLang().add(asRestorationlabWebpageLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserQuestion to get
     */
    public List<List<String>> getAsReproductionserQuestion() {
        if (this.asReproductionserQuestion == null) {
            this.asReproductionserQuestion = new ArrayList<List<String>>();
        }
        return this.asReproductionserQuestion;
    }

    /**
     * @param asReproductionserQuestion {@link List<List<String>>} the
     * asReproductionserQuestion to set
     */
    public void setAsReproductionserQuestion(
            List<List<String>> asReproductionserQuestion) {
        this.asReproductionserQuestion = asReproductionserQuestion;
    }

    /**
     * @param asReproductionserQuestion the asReproductionserQuestion to add
     */
    /*public void addAsReproductionserQuestion(List<String> asReproductionserQuestion) {
		this.getAsReproductionserQuestion().add(asReproductionserQuestion);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserDescription to
     * get
     */
    public List<List<String>> getAsReproductionserDescription() {
        if (this.asReproductionserDescription == null) {
            this.asReproductionserDescription = new ArrayList<List<String>>();
        }
        return this.asReproductionserDescription;
    }

    /**
     * @param asReproductionserDescription {@link List<List<String>>} the
     * asReproductionserDescription to set
     */
    public void setAsReproductionserDescription(
            List<List<String>> asReproductionserDescription) {
        this.asReproductionserDescription = asReproductionserDescription;
    }

    /**
     * @param asReproductionserDescription the asReproductionserDescription to
     * add
     */
    /*public void addAsReproductionserDescription(List<String> asReproductionserDescription) {
		this.getAsReproductionserDescription().add(asReproductionserDescription);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserDescriptionLang
     * to get
     */
    public List<List<String>> getAsReproductionserDescriptionLang() {
        if (this.asReproductionserDescriptionLang == null) {
            this.asReproductionserDescriptionLang = new ArrayList<List<String>>();
        }
        return this.asReproductionserDescriptionLang;
    }

    /**
     * @param asReproductionserDescriptionLang {@link List<List<String>>} the
     * asReproductionserDescriptionLang to set
     */
    public void setAsReproductionserDescriptionLang(
            List<List<String>> asReproductionserDescriptionLang) {
        this.asReproductionserDescriptionLang = asReproductionserDescriptionLang;
    }

    /**
     * @param asReproductionserDescriptionLang the
     * asReproductionserDescriptionLang to add
     */
    /*public void addAsReproductionserDescriptionLang(List<String> asReproductionserDescriptionLang) {
		this.getAsReproductionserDescriptionLang().add(asReproductionserDescriptionLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserTelephone to get
     */
    public List<List<String>> getAsReproductionserTelephone() {
        if (this.asReproductionserTelephone == null) {
            this.asReproductionserTelephone = new ArrayList<List<String>>();
        }
        return this.asReproductionserTelephone;
    }

    /**
     * @param asReproductionserTelephone {@link List<List<String>>} the
     * asReproductionserTelephone to set
     */
    public void setAsReproductionserTelephone(
            List<List<String>> asReproductionserTelephone) {
        this.asReproductionserTelephone = asReproductionserTelephone;
    }

    /**
     * @param asReproductionserTelephone the asReproductionserTelephone to add
     */
    /*public void addAsReproductionserTelephone(List<String> asReproductionserTelephone) {
		this.getAsReproductionserTelephone().add(asReproductionserTelephone);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserNumberOfEmail to
     * get
     */
    public List<List<String>> getAsReproductionserNumberOfEmail() {
        if (this.asReproductionserNumberOfEmail == null) {
            this.asReproductionserNumberOfEmail = new ArrayList<List<String>>();
        }
        return this.asReproductionserNumberOfEmail;
    }

    /**
     * @param asReproductionserNumberOfEmail {@link List<List<String>>} the
     * asReproductionserNumberOfEmail to set
     */
    public void setAsReproductionserNumberOfEmail(
            List<List<String>> asReproductionserNumberOfEmail) {
        this.asRestorationlabNumberOfEmail = asReproductionserNumberOfEmail;
    }

    /**
     * @param asReproductionserNumberOfEmail the asReproductionserNumberOfEmail
     * to add
     */
    /*public void addAsReproductionserNumberOfEmail(List<String> asReproductionserNumberOfEmail) {
		this.getAsReproductionserNumberOfEmail().add(asReproductionserNumberOfEmail);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserEmailHref to get
     */
    public List<List<String>> getAsReproductionserEmailHref() {
        if (this.asReproductionserEmailHref == null) {
            this.asReproductionserEmailHref = new ArrayList<List<String>>();
        }
        return this.asReproductionserEmailHref;
    }

    /**
     * @param asReproductionserEmailHref {@link List<List<String>>} the
     * asReproductionserEmailHref to set
     */
    public void setAsReproductionserEmailHref(
            List<List<String>> asReproductionserEmailHref) {
        this.asReproductionserEmailHref = asReproductionserEmailHref;
    }

    /**
     * @param asReproductionserEmailHref the asReproductionserEmailHref to add
     */
    /*public void addAsReproductionserEmailHref(List<String> asReproductionserEmailHref) {
		this.getAsReproductionserEmailHref().add(asReproductionserEmailHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserEmailTitle to get
     */
    public List<List<String>> getAsReproductionserEmailTitle() {
        if (this.asReproductionserEmailTitle == null) {
            this.asReproductionserEmailTitle = new ArrayList<List<String>>();
        }
        return this.asReproductionserEmailTitle;
    }

    /**
     * @param asReproductionserEmailTitle {@link List<List<String>>} the
     * asReproductionserEmailTitle to set
     */
    public void setAsReproductionserEmailTitle(
            List<List<String>> asReproductionserEmailTitle) {
        this.asReproductionserEmailTitle = asReproductionserEmailTitle;
    }

    /**
     * @param asReproductionserEmailTitle the asReproductionserEmailTitle to add
     */
    /*public void addAsReproductionserEmailTitle(List<String> asReproductionserEmailTitle) {
		this.getAsReproductionserEmailTitle().add(asReproductionserEmailTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserEmailLang to get
     */
    public List<List<String>> getAsReproductionserEmailLang() {
        if (this.asReproductionserEmailLang == null) {
            this.asReproductionserEmailLang = new ArrayList<List<String>>();
        }
        return this.asReproductionserEmailLang;
    }

    /**
     * @param asReproductionserEmailLang {@link List<List<String>>} the
     * asReproductionserEmailLang to set
     */
    public void setAsReproductionserEmailLang(
            List<List<String>> asReproductionserEmailLang) {
        this.asReproductionserEmailLang = asReproductionserEmailLang;
    }

    /**
     * @param asReproductionserEmailLang the asReproductionserEmailLang to add
     */
    /*public void addAsReproductionserEmailLang(List<String> asReproductionserEmailLang) {
		this.getAsReproductionserEmailLang().add(asReproductionserEmailLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserNumberOfWebpage
     * to get
     */
    public List<List<String>> getAsReproductionserNumberOfWebpage() {
        if (this.asReproductionserNumberOfWebpage == null) {
            this.asReproductionserNumberOfWebpage = new ArrayList<List<String>>();
        }
        return this.asReproductionserNumberOfWebpage;
    }

    /**
     * @param asReproductionserNumberOfWebpage {@link List<List<String>>} the
     * asReproductionserNumberOfWebpage to set
     */
    public void setAsReproductionserNumberOfWebpage(
            List<List<String>> asReproductionserNumberOfWebpage) {
        this.asReproductionserNumberOfWebpage = asReproductionserNumberOfWebpage;
    }

    /**
     * @param asReproductionserNumberOfWebpage the
     * asReproductionserNumberOfWebpage to add
     */
    /*public void addAsReproductionserNumberOfWebpage(List<String> asReproductionserNumberOfWebpage) {
		this.getAsReproductionserNumberOfWebpage().add(asReproductionserNumberOfWebpage);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserWebpageHref to
     * get
     */
    public List<List<String>> getAsReproductionserWebpageHref() {
        if (this.asReproductionserWebpageHref == null) {
            this.asReproductionserWebpageHref = new ArrayList<List<String>>();
        }
        return this.asReproductionserWebpageHref;
    }

    /**
     * @param asReproductionserWebpageHref {@link List<List<String>>} the
     * asReproductionserWebpageHref to set
     */
    public void setAsReproductionserWebpageHref(
            List<List<String>> asReproductionserWebpageHref) {
        this.asReproductionserWebpageHref = asReproductionserWebpageHref;
    }

    /**
     * @param asReproductionserWebpageHref the asReproductionserWebpageHref to
     * add
     */
    /*public void addAsReproductionserWebpageHref(List<String> asReproductionserWebpageHref) {
		this.getAsReproductionserWebpageHref().add(asReproductionserWebpageHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserWebpageTitle to
     * get
     */
    public List<List<String>> getAsReproductionserWebpageTitle() {
        if (this.asReproductionserWebpageTitle == null) {
            this.asReproductionserWebpageTitle = new ArrayList<List<String>>();
        }
        return this.asReproductionserWebpageTitle;
    }

    /**
     * @param asReproductionserWebpageTitle {@link List<List<String>>} the
     * asReproductionserWebpageTitle to set
     */
    public void setAsReproductionserWebpageTitle(
            List<List<String>> asReproductionserWebpageTitle) {
        this.asReproductionserWebpageTitle = asReproductionserWebpageTitle;
    }

    /**
     * @param asReproductionserWebpageTitle the asReproductionserWebpageTitle to
     * add
     */
    /*public void addAsReproductionserWebpageTitle(List<String> asReproductionserWebpageTitle) {
		this.getAsReproductionserWebpageTitle().add(asReproductionserWebpageTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserWebpageLang to
     * get
     */
    public List<List<String>> getAsReproductionserWebpageLang() {
        if (this.asReproductionserWebpageLang == null) {
            this.asReproductionserWebpageLang = new ArrayList<List<String>>();
        }
        return this.asReproductionserWebpageLang;
    }

    /**
     * @param asReproductionserWebpageLang {@link List<List<String>>} the
     * asReproductionserWebpageLang to set
     */
    public void setAsReproductionserWebpageLang(
            List<List<String>> asReproductionserWebpageLang) {
        this.asReproductionserWebpageLang = asReproductionserWebpageLang;
    }

    /**
     * @param asReproductionserWebpageLang the asReproductionserWebpageLang to
     * add
     */
    /*public void addAsReproductionserWebpageLang(List<String> asReproductionserWebpageLang) {
		this.getAsReproductionserWebpageLang().add(asReproductionserWebpageLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asReproductionserMicrofilmServices
     * to get
     */
    public List<List<String>> getAsReproductionserMicrofilmServices() {
        if (this.asReproductionserMicrofilmServices == null) {
            this.asReproductionserMicrofilmServices = new ArrayList<List<String>>();
        }
        return this.asReproductionserMicrofilmServices;
    }

    /**
     * @param asReproductionserMicrofilmServices {@link List<List<String>>} the
     * asReproductionserMicrofilmServices to set
     */
    public void setAsReproductionserMicrofilmServices(
            List<List<String>> asReproductionserMicrofilmServices) {
        this.asReproductionserMicrofilmServices = asReproductionserMicrofilmServices;
    }

    /**
     * @param asReproductionserMicrofilmServices the
     * asReproductionserMicrofilmServices to add
     */
    /*public void addAsReproductionserMicrofilmServices(List<String> asReproductionserMicrofilmServices) {
		this.getAsReproductionserMicrofilmServices().add(asReproductionserMicrofilmServices);
	}*/
    /**
     * @return {@link List<List<String>>} the
     * asReproductionserPhotographicServices to get
     */
    public List<List<String>> getAsReproductionserPhotographicServices() {
        if (this.asReproductionserPhotographicServices == null) {
            this.asReproductionserPhotographicServices = new ArrayList<List<String>>();
        }
        return this.asReproductionserPhotographicServices;
    }

    /**
     * @param asReproductionserPhotographicServices {@link List<List<String>>}
     * the asReproductionserPhotographicServices to set
     */
    public void setAsReproductionserPhotographicServices(
            List<List<String>> asReproductionserPhotographicServices) {
        this.asReproductionserPhotographicServices = asReproductionserPhotographicServices;
    }

    /**
     * @param asReproductionserPhotographicServices the
     * asReproductionserPhotographicServices to add
     */
    /*public void addAsReproductionserPhotographicServices(List<String> asReproductionserPhotographicServices) {
		this.getAsReproductionserPhotographicServices().add(asReproductionserPhotographicServices);
	}*/
    /**
     * @return {@link List<List<String>>} the
     * asReproductionserDigitisationServices to get
     */
    public List<List<String>> getAsReproductionserDigitisationServices() {
        if (this.asReproductionserDigitisationServices == null) {
            this.asReproductionserDigitisationServices = new ArrayList<List<String>>();
        }
        return this.asReproductionserDigitisationServices;
    }

    /**
     * @param asReproductionserDigitisationServices {@link List<List<String>>}
     * the asReproductionserDigitisationServices to set
     */
    public void setAsReproductionserDigitisationServices(
            List<List<String>> asReproductionserDigitisationServices) {
        this.asReproductionserDigitisationServices = asReproductionserDigitisationServices;
    }

    /**
     * @param asReproductionserDigitisationServices the
     * asReproductionserDigitisationServices to add
     */
    /*public void addAsReproductionserDigitisationServices(List<String> asReproductionserDigitisationServices) {
		this.getAsReproductionserDigitisationServices().add(asReproductionserDigitisationServices);
	}*/
    /**
     * @return {@link List<List<String>>} the
     * asReproductionserPhotocopyingServices to get
     */
    public List<List<String>> getAsReproductionserPhotocopyingServices() {
        if (this.asReproductionserPhotocopyingServices == null) {
            this.asReproductionserPhotocopyingServices = new ArrayList<List<String>>();
        }
        return this.asReproductionserPhotocopyingServices;
    }

    /**
     * @param asReproductionserPhotocopyingServices {@link List<List<String>>}
     * the asReproductionserPhotocopyingServices to set
     */
    public void setAsReproductionserPhotocopyingServices(
            List<List<String>> asReproductionserPhotocopyingServices) {
        this.asReproductionserPhotocopyingServices = asReproductionserPhotocopyingServices;
    }

    /**
     * @param asReproductionserPhotocopyingServices the
     * asReproductionserPhotocopyingServices to add
     */
    /*public void addAsReproductionserPhotocopyingServices(List<String> asReproductionserPhotocopyingServices) {
		this.getAsReproductionserPhotocopyingServices().add(asReproductionserPhotocopyingServices);
	}*/
    /**
     * @return {@link List<List<String>>} the
     * asRecreationalServicesRefreshmentArea to get
     */
    public List<List<String>> getAsRecreationalServicesRefreshmentArea() {
        if (this.asRecreationalServicesRefreshmentArea == null) {
            this.asRecreationalServicesRefreshmentArea = new ArrayList<List<String>>();
        }
        return this.asRecreationalServicesRefreshmentArea;
    }

    /**
     * @param asRecreationalServicesRefreshmentArea {@link List<List<String>>}
     * the asRecreationalServicesRefreshmentArea to set
     */
    public void setAsRecreationalServicesRefreshmentArea(
            List<List<String>> asRecreationalServicesRefreshmentArea) {
        this.asRecreationalServicesRefreshmentArea = asRecreationalServicesRefreshmentArea;
    }

    /**
     * @param asRecreationalServicesRefreshmentArea the
     * asRecreationalServicesRefreshmentArea to add
     */
    /*public void addAsRecreationalServicesRefreshmentArea(List<String> asRecreationalServicesRefreshmentArea) {
		this.getAsRecreationalServicesRefreshmentArea().add(asRecreationalServicesRefreshmentArea);
	}*/
    /**
     * @return {@link List<List<String>>} the
     * asRecreationalServicesRefreshmentAreaLang to get
     */
    public List<List<String>> getAsRecreationalServicesRefreshmentAreaLang() {
        if (this.asRecreationalServicesRefreshmentAreaLang == null) {
            this.asRecreationalServicesRefreshmentAreaLang = new ArrayList<List<String>>();
        }
        return this.asRecreationalServicesRefreshmentAreaLang;
    }

    /**
     * @param asRecreationalServicesRefreshmentAreaLang
     * {@link List<List<String>>} the asRecreationalServicesRefreshmentAreaLang
     * to set
     */
    public void setAsRecreationalServicesRefreshmentAreaLang(
            List<List<String>> asRecreationalServicesRefreshmentAreaLang) {
        this.asRecreationalServicesRefreshmentAreaLang = asRecreationalServicesRefreshmentAreaLang;
    }

    /**
     * @param asRecreationalServicesRefreshmentAreaLang the
     * asRecreationalServicesRefreshmentAreaLang to add
     */
    /*public void addAsRecreationalServicesRefreshmentAreaLang(List<String> asRecreationalServicesRefreshmentAreaLang) {
		this.getAsRecreationalServicesRefreshmentAreaLang().add(asRecreationalServicesRefreshmentAreaLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSNumberOfExhibition to get
     */
    public List<List<String>> getAsRSNumberOfExhibition() {
        if (this.asRSNumberOfExhibition == null) {
            this.asRSNumberOfExhibition = new ArrayList<List<String>>();
        }
        return this.asRSNumberOfExhibition;
    }

    /**
     * @param asRSNumberOfExhibition {@link List<List<String>>} the
     * asRSNumberOfExhibition to set
     */
    public void setAsRSNumberOfExhibition(List<List<String>> asRSNumberOfExhibition) {
        this.asRSNumberOfExhibition = asRSNumberOfExhibition;
    }

    /**
     * @param asRSNumberOfExhibition the asRSNumberOfExhibition to add
     */
    /*public void addAsRSNumberOfExhibition(List<String> asRSNumberOfExhibition) {
		this.getAsRSNumberOfExhibition().add(asRSNumberOfExhibition);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSExhibition to get
     */
    public List<List<String>> getAsRSExhibition() {
        if (this.asRSExhibition == null) {
            this.asRSExhibition = new ArrayList<List<String>>();
        }
        return this.asRSExhibition;
    }

    /**
     * @param asRSExhibition {@link List<List<String>>} the asRSExhibition to
     * set
     */
    public void setAsRSExhibition(List<List<String>> asRSExhibition) {
        this.asRSExhibition = asRSExhibition;
    }

    /**
     * @param asRSExhibition the asRSExhibition to add
     */
    /*public void addAsRSExhibition(List<String> asRSExhibition) {
		this.getAsRSExhibition().add(asRSExhibition);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSExhibitionLang to get
     */
    public List<List<String>> getAsRSExhibitionLang() {
        if (this.asRSExhibitionLang == null) {
            this.asRSExhibitionLang = new ArrayList<List<String>>();
        }
        return this.asRSExhibitionLang;
    }

    /**
     * @param asRSExhibitionLang {@link List<List<String>>} the
     * asRSExhibitionLang to set
     */
    public void setAsRSExhibitionLang(List<List<String>> asRSExhibitionLang) {
        this.asRSExhibitionLang = asRSExhibitionLang;
    }

    /**
     * @param asRSExhibitionLang the asRSExhibitionLang to add
     */
    /*public void addAsRSExhibitionLang(List<String> asRSExhibitionLang) {
		this.getAsRSExhibitionLang().add(asRSExhibitionLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSExhibitionWebpageHref to get
     */
    public List<List<String>> getAsRSExhibitionWebpageHref() {
        if (this.asRSExhibitionWebpageHref == null) {
            this.asRSExhibitionWebpageHref = new ArrayList<List<String>>();
        }
        return this.asRSExhibitionWebpageHref;
    }

    /**
     * @param asRSExhibitionWebpageHref {@link List<List<String>>} the
     * asRSExhibitionWebpageHref to set
     */
    public void setAsRSExhibitionWebpageHref(
            List<List<String>> asRSExhibitionWebpageHref) {
        this.asRSExhibitionWebpageHref = asRSExhibitionWebpageHref;
    }

    /**
     * @param asRSExhibitionWebpageHref the asRSExhibitionWebpageHref to add
     */
    /*public void addAsRSExhibitionWebpageHref(List<String> asRSExhibitionWebpageHref) {
		this.getAsRSExhibitionWebpageHref().add(asRSExhibitionWebpageHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSExhibitionWebpageTitle to get
     */
    public List<List<String>> getAsRSExhibitionWebpageTitle() {
        if (this.asRSExhibitionWebpageTitle == null) {
            this.asRSExhibitionWebpageTitle = new ArrayList<List<String>>();
        }
        return this.asRSExhibitionWebpageTitle;
    }

    /**
     * @param asRSExhibitionWebpageTitle {@link List<List<String>>} the
     * asRSExhibitionWebpageTitle to set
     */
    public void setAsRSExhibitionWebpageTitle(
            List<List<String>> asRSExhibitionWebpageTitle) {
        this.asRSExhibitionWebpageTitle = asRSExhibitionWebpageTitle;
    }

    /**
     * @param asRSExhibitionWebpageTitle the asRSExhibitionWebpageTitle to add
     */
    /*public void addAsRSExhibitionWebpageTitle(List<String> asRSExhibitionWebpageTitle) {
		this.getAsRSExhibitionWebpageTitle().add(asRSExhibitionWebpageTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSExhibitionWebpageLangto get
     */
    public List<List<String>> getAsRSExhibitionWebpageLang() {
        if (this.asRSExhibitionWebpageLang == null) {
            this.asRSExhibitionWebpageLang = new ArrayList<List<String>>();
        }
        return this.asRSExhibitionWebpageLang;
    }

    /**
     * @param asRSExhibitionWebpageLang {@link List<List<String>>} the
     * asRSExhibitionWebpageLang to set
     */
    public void setAsRSExhibitionWebpageLang(
            List<List<String>> asRSExhibitionWebpageLang) {
        this.asRSExhibitionWebpageLang = asRSExhibitionWebpageLang;
    }

    /**
     * @param asRSExhibitionWebpageLang the asRSExhibitionWebpageLang to add
     */
    /*public void addAsRSExhibitionWebpageLang(List<String> asRSExhibitionWebpageLang) {
		this.getAsRSExhibitionWebpageLang().add(asRSExhibitionWebpageLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSNumberOfToursSessions to get
     */
    public List<List<String>> getAsRSNumberOfToursSessions() {
        if (this.asRSNumberOfToursSessions == null) {
            this.asRSNumberOfToursSessions = new ArrayList<List<String>>();
        }
        return this.asRSNumberOfToursSessions;
    }

    /**
     * @param asRSNumberOfToursSessions {@link List<List<String>>} the
     * asRSNumberOfToursSessions to set
     */
    public void setAsRSNumberOfToursSessions(
            List<List<String>> asRSNumberOfToursSessions) {
        this.asRSNumberOfToursSessions = asRSNumberOfToursSessions;
    }

    /**
     * @param asRSNumberOfToursSessions the asRSNumberOfToursSessions to add
     */
    /*public void addAsRSNumberOfToursSessions(List<String> asRSNumberOfToursSessions) {
		this.getAsRSNumberOfToursSessions().add(asRSNumberOfToursSessions);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSToursSessions to get
     */
    public List<List<String>> getAsRSToursSessions() {
        if (this.asRSToursSessions == null) {
            this.asRSToursSessions = new ArrayList<List<String>>();
        }
        return this.asRSToursSessions;
    }

    /**
     * @param asRSToursSessions {@link List<List<String>>} the asRSToursSessions
     * to set
     */
    public void setAsRSToursSessions(List<List<String>> asRSToursSessions) {
        this.asRSToursSessions = asRSToursSessions;
    }

    /**
     * @param asRSToursSessions the asRSToursSessions to add
     */
    /*public void addAsRSToursSessions(List<String> asRSToursSessions) {
		this.getAsRSToursSessions().add(asRSToursSessions);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSToursSessionsLang to get
     */
    public List<List<String>> getAsRSToursSessionsLang() {
        if (this.asRSToursSessionsLang == null) {
            this.asRSToursSessionsLang = new ArrayList<List<String>>();
        }
        return this.asRSToursSessionsLang;
    }

    /**
     * @param asRSToursSessionsLang {@link List<List<String>>} the
     * asRSToursSessionsLang to set
     */
    public void setAsRSToursSessionsLang(List<List<String>> asRSToursSessionsLang) {
        this.asRSToursSessionsLang = asRSToursSessionsLang;
    }

    /**
     * @param asRSToursSessionsLang the asRSToursSessionsLang to add
     */
    /*public void addAsRSToursSessionsLang(List<String> asRSToursSessionsLang) {
		this.getAsRSToursSessionsLang().add(asRSToursSessionsLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSToursSessionsWebpageHref to
     * get
     */
    public List<List<String>> getAsRSToursSessionsWebpageHref() {
        if (this.asRSToursSessionsWebpageHref == null) {
            this.asRSToursSessionsWebpageHref = new ArrayList<List<String>>();
        }
        return this.asRSToursSessionsWebpageHref;
    }

    /**
     * @param asRSToursSessionsWebpageHref {@link List<List<String>>} the
     * asRSToursSessionsWebpageHref to set
     */
    public void setAsRSToursSessionsWebpageHref(
            List<List<String>> asRSToursSessionsWebpageHref) {
        this.asRSToursSessionsWebpageHref = asRSToursSessionsWebpageHref;
    }

    /**
     * @param asRSToursSessionsWebpageHref the asRSToursSessionsWebpageHref to
     * add
     */
    /*public void addAsRSToursSessionsWebpageHref(List<String> asRSToursSessionsWebpageHref) {
		this.getAsRSToursSessionsWebpageHref().add(asRSToursSessionsWebpageHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSToursSessionsWebpageTitle to
     * get
     */
    public List<List<String>> getAsRSToursSessionsWebpageTitle() {
        if (this.asRSToursSessionsWebpageTitle == null) {
            this.asRSToursSessionsWebpageTitle = new ArrayList<List<String>>();
        }
        return this.asRSToursSessionsWebpageTitle;
    }

    /**
     * @param asRSToursSessionsWebpageTitle {@link List<List<String>>} the
     * asRSToursSessionsWebpageTitle to set
     */
    public void setAsRSToursSessionsWebpageTitle(
            List<List<String>> asRSToursSessionsWebpageTitle) {
        this.asRSToursSessionsWebpageTitle = asRSToursSessionsWebpageTitle;
    }

    /**
     * @param asRSToursSessionsWebpageTitle the asRSToursSessionsWebpageTitle to
     * add
     */
    /*public void addAsRSToursSessionsWebpageTitle(List<String> asRSToursSessionsWebpageTitle) {
		this.getAsRSToursSessionsWebpageTitle().add(asRSToursSessionsWebpageTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSToursSessionsWebpageLang to
     * get
     */
    public List<List<String>> getAsRSToursSessionsWebpageLang() {
        if (this.asRSToursSessionsWebpageLang == null) {
            this.asRSToursSessionsWebpageLang = new ArrayList<List<String>>();
        }
        return this.asRSToursSessionsWebpageLang;
    }

    /**
     * @param asRSToursSessionsWebpageLang {@link List<List<String>>} the
     * asRSToursSessionsWebpageLang to set
     */
    public void setAsRSToursSessionsWebpageLang(
            List<List<String>> asRSToursSessionsWebpageLang) {
        this.asRSToursSessionsWebpageLang = asRSToursSessionsWebpageLang;
    }

    /**
     * @param asRSToursSessionsWebpageLang the asRSToursSessionsWebpageLang to
     * add
     */
    /*public void addAsRSToursSessionsWebpageLang(List<String> asRSToursSessionsWebpageLang) {
		this.getAsRSToursSessionsWebpageLang().add(asRSToursSessionsWebpageLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSNumberOfOtherServices to get
     */
    public List<List<String>> getAsRSNumberOfOtherServices() {
        if (this.asRSNumberOfOtherServices == null) {
            this.asRSNumberOfOtherServices = new ArrayList<List<String>>();
        }
        return this.asRSNumberOfOtherServices;
    }

    /**
     * @param asRSNumberOfOtherServices {@link List<List<String>>} the
     * asRSNumberOfOtherServices to set
     */
    public void setAsRSNumberOfOtherServices(
            List<List<String>> asRSNumberOfOtherServices) {
        this.asRSNumberOfOtherServices = asRSNumberOfOtherServices;
    }

    /**
     * @param asRSNumberOfOtherServices the asRSNumberOfOtherServices to add
     */
    /*public void addAsRSNumberOfOtherServices(List<String> asRSNumberOfOtherServices) {
		this.getAsRSNumberOfOtherServices().add(asRSNumberOfOtherServices);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSOtherServices to get
     */
    public List<List<String>> getAsRSOtherServices() {
        if (this.asRSOtherServices == null) {
            this.asRSOtherServices = new ArrayList<List<String>>();
        }
        return this.asRSOtherServices;
    }

    /**
     * @param asRSOtherServices {@link List<List<String>>} the asRSOtherServices
     * to set
     */
    public void setAsRSOtherServices(List<List<String>> asRSOtherServices) {
        this.asRSOtherServices = asRSOtherServices;
    }

    /**
     * @param asRSOtherServices the asRSOtherServices to add
     */
    /*public void addAsRSOtherServices(List<String> asRSOtherServices) {
		this.getAsRSOtherServices().add(asRSOtherServices);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSOtherServicesLang to get
     */
    public List<List<String>> getAsRSOtherServicesLang() {
        if (this.asRSOtherServicesLang == null) {
            this.asRSOtherServicesLang = new ArrayList<List<String>>();
        }
        return this.asRSOtherServicesLang;
    }

    /**
     * @param asRSOtherServicesLang {@link List<List<String>>} the
     * asRSOtherServicesLang to set
     */
    public void setAsRSOtherServicesLang(List<List<String>> asRSOtherServicesLang) {
        this.asRSOtherServicesLang = asRSOtherServicesLang;
    }

    /**
     * @param asRSOtherServicesLang the asRSOtherServicesLang to add
     */
    /*public void addAsRSOtherServicesLang(List<String> asRSOtherServicesLang) {
		this.getAsRSOtherServicesLang().add(asRSOtherServicesLang);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSOtherServicesWebpageHref to
     * get
     */
    public List<List<String>> getAsRSOtherServicesWebpageHref() {
        if (this.asRSOtherServicesWebpageHref == null) {
            this.asRSOtherServicesWebpageHref = new ArrayList<List<String>>();
        }
        return this.asRSOtherServicesWebpageHref;
    }

    /**
     * @param asRSOtherServicesWebpageHref {@link List<List<String>>} the
     * asRSOtherServicesWebpageHref to set
     */
    public void setAsRSOtherServicesWebpageHref(
            List<List<String>> asRSOtherServicesWebpageHref) {
        this.asRSOtherServicesWebpageHref = asRSOtherServicesWebpageHref;
    }

    /**
     * @param asRSOtherServicesWebpageHref the asRSOtherServicesWebpageHref to
     * add
     */
    /*public void addAsRSOtherServicesWebpageHref(List<String> asRSOtherServicesWebpageHref) {
		this.getAsRSOtherServicesWebpageHref().add(asRSOtherServicesWebpageHref);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSOtherServicesWebpageTitle to
     * get
     */
    public List<List<String>> getAsRSOtherServicesWebpageTitle() {
        if (this.asRSOtherServicesWebpageTitle == null) {
            this.asRSOtherServicesWebpageTitle = new ArrayList<List<String>>();
        }
        return this.asRSOtherServicesWebpageTitle;
    }

    /**
     * @param asRSOtherServicesWebpageTitle {@link List<List<String>>} the
     * asRSOtherServicesWebpageTitle to set
     */
    public void setAsRSOtherServicesWebpageTitle(
            List<List<String>> asRSOtherServicesWebpageTitle) {
        this.asRSOtherServicesWebpageTitle = asRSOtherServicesWebpageTitle;
    }

    /**
     * @param asRSOtherServicesWebpageTitle the asRSOtherServicesWebpageTitle to
     * add
     */
    /*public void addAsRSOtherServicesWebpageTitle(List<String> asRSOtherServicesWebpageTitle) {
		this.getAsRSOtherServicesWebpageTitle().add(asRSOtherServicesWebpageTitle);
	}*/
    /**
     * @return {@link List<List<String>>} the asRSOtherServicesWebpageLang to
     * get
     */
    public List<List<String>> getAsRSOtherServicesWebpageLang() {
        if (this.asRSOtherServicesWebpageLang == null) {
            this.asRSOtherServicesWebpageLang = new ArrayList<List<String>>();
        }
        return this.asRSOtherServicesWebpageLang;
    }

    /**
     * @param asRSOtherServicesWebpageLang {@link List<List<String>>} the
     * asRSOtherServicesWebpageLang to set
     */
    public void setAsRSOtherServicesWebpageLang(
            List<List<String>> asRSOtherServicesWebpageLang) {
        this.asRSOtherServicesWebpageLang = asRSOtherServicesWebpageLang;
    }

    /**
     * @param asRSOtherServicesWebpageLang the asRSOtherServicesWebpageLang to
     * add
     */
    /*public void addAsRSOtherServicesWebpageLang(List<String> asRSOtherServicesWebpageLang) {
		this.getAsRSOtherServicesWebpageLang().add(asRSOtherServicesWebpageLang);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorhist to get
     */
    public List<List<String>> getDescRepositorhist() {
        if (this.descRepositorhist == null) {
            this.descRepositorhist = new ArrayList<List<String>>();
        }
        return this.descRepositorhist;
    }

    /**
     * @param descRepositorhist {@link List<List<String>>} the descRepositorhist
     * to set
     */
    public void setDescRepositorhist(List<List<String>> descRepositorhist) {
        this.descRepositorhist = descRepositorhist;
    }

    /**
     * @param descRepositorhist the descRepositorhist to add
     */
    /*public void addDescRepositorhist(List<String> descRepositorhist) {
		this.getDescRepositorhist().add(descRepositorhist);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorhistLang to get
     */
    public List<List<String>> getDescRepositorhistLang() {
        if (this.descRepositorhistLang == null) {
            this.descRepositorhistLang = new ArrayList<List<String>>();
        }
        return this.descRepositorhistLang;
    }

    /**
     * @param descRepositorhistLang {@link List<List<String>>} the
     * descRepositorhistLang to set
     */
    public void setDescRepositorhistLang(List<List<String>> descRepositorhistLang) {
        this.descRepositorhistLang = descRepositorhistLang;
    }

    /**
     * @param descRepositorhistLang the descRepositorhistLang to add
     */
    /*public void addDescRepositorhistLang(List<String> descRepositorhistLang) {
		this.getDescRepositorhistLang().add(descRepositorhistLang);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorFoundDate to get
     */
    public List<List<String>> getDescRepositorFoundDate() {
        if (this.descRepositorFoundDate == null) {
            this.descRepositorFoundDate = new ArrayList<List<String>>();
        }
        return this.descRepositorFoundDate;
    }

    /**
     * @param descRepositorFoundDate {@link List<List<String>>} the
     * descRepositorFoundDate to set
     */
    public void setDescRepositorFoundDate(List<List<String>> descRepositorFoundDate) {
        this.descRepositorFoundDate = descRepositorFoundDate;
    }

    /**
     * @param descRepositorFoundDate the descRepositorFoundDate to add
     */
    /*public void addDescRepositorFoundDate(List<String> descRepositorFoundDate) {
		this.getDescRepositorFoundDate().add(descRepositorFoundDate);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorFoundRule to get
     */
    public List<List<String>> getDescRepositorFoundRule() {
        if (this.descRepositorFoundRule == null) {
            this.descRepositorFoundRule = new ArrayList<List<String>>();
        }
        return this.descRepositorFoundRule;
    }

    /**
     * @param descRepositorFoundRule {@link List<List<String>>} the
     * descRepositorFoundRule to set
     */
    public void setDescRepositorFoundRule(List<List<String>> descRepositorFoundRule) {
        this.descRepositorFoundRule = descRepositorFoundRule;
    }

    /**
     * @param descRepositorFoundRule the descRepositorFoundRule to add
     */
    /*public void addDescRepositorFoundRule(List<String> descRepositorFoundRule) {
		this.getDescRepositorFoundRule().add(descRepositorFoundRule);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorFoundRuleLang to get
     */
    public List<List<String>> getDescRepositorFoundRuleLang() {
        if (this.descRepositorFoundRuleLang == null) {
            this.descRepositorFoundRuleLang = new ArrayList<List<String>>();
        }
        return this.descRepositorFoundRuleLang;
    }

    /**
     * @param descRepositorFoundRuleLang {@link List<List<String>>} the
     * descRepositorFoundRuleLang to set
     */
    public void setDescRepositorFoundRuleLang(
            List<List<String>> descRepositorFoundRuleLang) {
        this.descRepositorFoundRuleLang = descRepositorFoundRuleLang;
    }

    /**
     * @param descRepositorFoundRuleLang the descRepositorFoundRuleLang to add
     */
    /*public void addDescRepositorFoundRuleLang(List<String> descRepositorFoundRuleLang) {
		this.getDescRepositorFoundRuleLang().add(descRepositorFoundRuleLang);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorSupDate to get
     */
    public List<List<String>> getDescRepositorSupDate() {
        if (this.descRepositorSupDate == null) {
            this.descRepositorSupDate = new ArrayList<List<String>>();
        }
        return this.descRepositorSupDate;
    }

    /**
     * @param descRepositorSupDate {@link List<List<String>>} the
     * descRepositorSupDate to set
     */
    public void setDescRepositorSupDate(List<List<String>> descRepositorSupDate) {
        this.descRepositorSupDate = descRepositorSupDate;
    }

    /**
     * @param descRepositorSupDate the descRepositorSupDate to add
     */
    /*public void addDescRepositorSupDate(List<String> descRepositorSupDate) {
		this.getDescRepositorSupDate().add(descRepositorSupDate);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorSupRule to get
     */
    public List<List<String>> getDescRepositorSupRule() {
        if (this.descRepositorSupRule == null) {
            this.descRepositorSupRule = new ArrayList<List<String>>();
        }
        return this.descRepositorSupRule;
    }

    /**
     * @param descRepositorSupRule {@link List<List<String>>} the
     * descRepositorSupRule to set
     */
    public void setDescRepositorSupRule(List<List<String>> descRepositorSupRule) {
        this.descRepositorSupRule = descRepositorSupRule;
    }

    /**
     * @param descRepositorSupRule the descRepositorSupRule to add
     */
    /*public void addDescRepositorSupRule(List<String> descRepositorSupRule) {
		this.getDescRepositorSupRule().add(descRepositorSupRule);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorSupRuleLang to get
     */
    public List<List<String>> getDescRepositorSupRuleLang() {
        if (this.descRepositorSupRuleLang == null) {
            this.descRepositorSupRuleLang = new ArrayList<List<String>>();
        }
        return this.descRepositorSupRuleLang;
    }

    /**
     * @param descRepositorSupRuleLang {@link List<List<String>>} the
     * descRepositorSupRuleLang to set
     */
    public void setDescRepositorSupRuleLang(
            List<List<String>> descRepositorSupRuleLang) {
        this.descRepositorSupRuleLang = descRepositorSupRuleLang;
    }

    /**
     * @param descRepositorSupRuleLang the descRepositorSupRuleLang to add
     */
    /*public void addDescRepositorSupRuleLang(List<String> descRepositorSupRuleLang) {
		this.getDescRepositorSupRuleLang().add(descRepositorSupRuleLang);
	}*/
    /**
     * @return {@link List<List<String>>} the descAdminunit to get
     */
    public List<List<String>> getDescAdminunit() {
        if (this.descAdminunit == null) {
            this.descAdminunit = new ArrayList<List<String>>();
        }
        return this.descAdminunit;
    }

    /**
     * @param descAdminunit {@link List<List<String>>} the descAdminunit to set
     */
    public void setDescAdminunit(List<List<String>> descAdminunit) {
        this.descAdminunit = descAdminunit;
    }

    /**
     * @param descAdminunit the descAdminunit to add
     */
    /*public void addDescAdminunit(List<String> descAdminunit) {
		this.getDescAdminunit().add(descAdminunit);
	}*/
    /**
     * @return {@link List<List<String>>} the descAdminunitLang to get
     */
    public List<List<String>> getDescAdminunitLang() {
        if (this.descAdminunitLang == null) {
            this.descAdminunitLang = new ArrayList<List<String>>();
        }
        return this.descAdminunitLang;
    }

    /**
     * @param descAdminunitLang {@link List<List<String>>} the descAdminunitLang
     * to set
     */
    public void setDescAdminunitLang(List<List<String>> descAdminunitLang) {
        this.descAdminunitLang = descAdminunitLang;
    }

    /**
     * @param descAdminunitLang the descAdminunitLang to add
     */
    /*public void addDescAdminunitLang(List<String> descAdminunitLang) {
		this.getDescAdminunitLang().add(descAdminunitLang);
	}*/
    /**
     * @return {@link List<List<String>>} the descBuilding to get
     */
    public List<List<String>> getDescBuilding() {
        if (this.descBuilding == null) {
            this.descBuilding = new ArrayList<List<String>>();
        }
        return this.descBuilding;
    }

    /**
     * @param descBuilding {@link List<List<String>>} the descBuilding to set
     */
    public void setDescBuilding(List<List<String>> descBuilding) {
        this.descBuilding = descBuilding;
    }

    /**
     * @param descBuilding the descBuilding to add
     */
    /*public void addDescBuilding(List<String> descBuilding) {
		this.getDescBuilding().add(descBuilding);
	}*/
    /**
     * @return {@link List<List<String>>} the descBuildingLang to get
     */
    public List<List<String>> getDescBuildingLang() {
        if (this.descBuildingLang == null) {
            this.descBuildingLang = new ArrayList<List<String>>();
        }
        return this.descBuildingLang;
    }

    /**
     * @param descBuildingLang {@link List<List<String>>} the descBuildingLang
     * to set
     */
    public void setDescBuildingLang(List<List<String>> descBuildingLang) {
        this.descBuildingLang = descBuildingLang;
    }

    /**
     * @param descBuildingLang the descBuildingLang to add
     */
    /*public void addDescBuildingLang(List<String> descBuildingLang) {
		this.getDescBuildingLang().add(descBuildingLang);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorarea to get
     */
    public List<List<String>> getDescRepositorarea() {
        if (this.descRepositorarea == null) {
            this.descRepositorarea = new ArrayList<List<String>>();
        }
        return this.descRepositorarea;
    }

    /**
     * @param descRepositorarea {@link List<List<String>>} the descRepositorarea
     * to set
     */
    public void setDescRepositorarea(List<List<String>> descRepositorarea) {
        this.descRepositorarea = descRepositorarea;
    }

    /**
     * @param descRepositorarea the descRepositorarea to add
     */
    /*public void addDescRepositorarea(List<String> descRepositorarea) {
		this.getDescRepositorarea().add(descRepositorarea);
	}*/
    /**
     * @return {@link List<List<String>>} the descRepositorareaUnit to get
     */
    public List<List<String>> getDescRepositorareaUnit() {
        if (this.descRepositorareaUnit == null) {
            this.descRepositorareaUnit = new ArrayList<List<String>>();
        }
        return this.descRepositorareaUnit;
    }

    /**
     * @param descRepositorareaUnit {@link List<List<String>>} the
     * descRepositorareaUnit to set
     */
    public void setDescRepositorareaUnit(List<List<String>> descRepositorareaUnit) {
        this.descRepositorareaUnit = descRepositorareaUnit;
    }

    /**
     * @param descRepositorareaUnit the descRepositorareaUnit to add
     */
    /*public void addDescRepositorareaUnit(List<String> descRepositorareaUnit) {
		this.getDescRepositorareaUnit().add(descRepositorareaUnit);
	}*/
    /**
     * @return {@link List<List<String>>} the descLengthshelf to get
     */
    public List<List<String>> getDescLengthshelf() {
        if (this.descLengthshelf == null) {
            this.descLengthshelf = new ArrayList<List<String>>();
        }
        return this.descLengthshelf;
    }

    /**
     * @param descLengthshelf {@link List<List<String>>} the descLengthshelf to
     * set
     */
    public void setDescLengthshelf(List<List<String>> descLengthshelf) {
        this.descLengthshelf = descLengthshelf;
    }

    /**
     * @param descLengthshelf the descLengthshelf to add
     */
    /*public void addDescLengthshelf(List<String> descLengthshelf) {
		this.getDescLengthshelf().add(descLengthshelf);
	}*/
    /**
     * @return {@link List<List<String>>} the descLengthshelfUnit to get
     */
    public List<List<String>> getDescLengthshelfUnit() {
        if (this.descLengthshelfUnit == null) {
            this.descLengthshelfUnit = new ArrayList<List<String>>();
        }
        return this.descLengthshelfUnit;
    }

    /**
     * @param descLengthshelfUnit {@link List<List<String>>} the
     * descLengthshelfUnit to set
     */
    public void setDescLengthshelfUnit(List<List<String>> descLengthshelfUnit) {
        this.descLengthshelfUnit = descLengthshelfUnit;
    }

    /**
     * @param descLengthshelfUnit the descLengthshelfUnit to add
     */
    /*public void addDescLengthshelfUnit(List<String> descLengthshelfUnit) {
		this.getDescLengthshelfUnit().add(descLengthshelfUnit);
	}*/
    /**
     * @return {@link List<List<String>>} the descHoldings to get
     */
    public List<List<String>> getDescHoldings() {
        if (this.descHoldings == null) {
            this.descHoldings = new ArrayList<List<String>>();
        }
        return this.descHoldings;
    }

    /**
     * @param descHoldings {@link List<List<String>>} the descHoldings to set
     */
    public void setDescHoldings(List<List<String>> descHoldings) {
        this.descHoldings = descHoldings;
    }

    /**
     * @param descHoldings the descHoldings to add
     */
    /*public void addDescHoldings(List<String> descHoldings) {
		this.getDescHoldings().add(descHoldings);
	}*/
    /**
     * @return {@link List<List<String>>} the descHoldingsLang to get
     */
    public List<List<String>> getDescHoldingsLang() {
        if (this.descHoldingsLang == null) {
            this.descHoldingsLang = new ArrayList<List<String>>();
        }
        return this.descHoldingsLang;
    }

    /**
     * @param descHoldingsLang {@link List<List<String>>} the descHoldingsLang
     * to set
     */
    public void setDescHoldingsLang(List<List<String>> descHoldingsLang) {
        this.descHoldingsLang = descHoldingsLang;
    }

    /**
     * @param descHoldingsLang the descHoldingsLang to add
     */
    /*public void addDescHoldingsLang(List<String> descHoldingsLang) {
		this.getDescHoldingsLang().add(descHoldingsLang);
	}*/
    /**
     * @return {@link List<List<String>>} the descHoldingsDate to get
     */
    public List<List<String>> getDescHoldingsDate() {
        if (this.descHoldingsDate == null) {
            this.descHoldingsDate = new ArrayList<List<String>>();
        }
        return this.descHoldingsDate;
    }

    /**
     * @param descHoldingsDate {@link List<List<String>>} the descHoldingsDate
     * to set
     */
    public void setDescHoldingsDate(List<List<String>> descHoldingsDate) {
        this.descHoldingsDate = descHoldingsDate;
    }

    /**
     * @param descHoldingsDate the descHoldingsDate to add
     */
    /*public void addDescHoldingsDate(List<String> descHoldingsDate) {
		this.getDescHoldingsDate().add(descHoldingsDate);
	}*/
    /**
     * @return {@link List<List<String>>} the descNumberOfHoldingsDateRange to
     * get
     */
    public List<List<String>> getDescNumberOfHoldingsDateRange() {
        if (this.descNumberOfHoldingsDateRange == null) {
            this.descNumberOfHoldingsDateRange = new ArrayList<List<String>>();
        }
        return this.descNumberOfHoldingsDateRange;
    }

    /**
     * @param descNumberOfHoldingsDateRange {@link List<List<String>>} the
     * descNumberOfHoldingsDateRange to set
     */
    public void setDescNumberOfHoldingsDateRange(
            List<List<String>> descNumberOfHoldingsDateRange) {
        this.descNumberOfHoldingsDateRange = descNumberOfHoldingsDateRange;
    }

    /**
     * @param descNumberOfHoldingsDateRange the descNumberOfHoldingsDateRange to
     * add
     */
    /*public void addDescNumberOfHoldingsDateRange(List<String> descNumberOfHoldingsDateRange) {
		this.getDescNumberOfHoldingsDateRange().add(descNumberOfHoldingsDateRange);
	}*/
    /**
     * @return {@link List<List<String>>} the descHoldingsDateRangeFromDate to
     * get
     */
    public List<List<String>> getDescHoldingsDateRangeFromDate() {
        if (this.descHoldingsDateRangeFromDate == null) {
            this.descHoldingsDateRangeFromDate = new ArrayList<List<String>>();
        }
        return this.descHoldingsDateRangeFromDate;
    }

    /**
     * @param descHoldingsDateRangeFromDate {@link List<List<String>>} the
     * descHoldingsDateRangeFromDate to set
     */
    public void setDescHoldingsDateRangeFromDate(
            List<List<String>> descHoldingsDateRangeFromDate) {
        this.descHoldingsDateRangeFromDate = descHoldingsDateRangeFromDate;
    }

    /**
     * @param descHoldingsDateRangeFromDate the descHoldingsDateRangeFromDate to
     * add
     */
    /*public void addDescHoldingsDateRangeFromDate(List<String> descHoldingsDateRangeFromDate) {
		this.getDescHoldingsDateRangeFromDate().add(descHoldingsDateRangeFromDate);
	}*/
    /**
     * @return {@link List<List<String>>} the descHoldingsDateRangeToDate to get
     */
    public List<List<String>> getDescHoldingsDateRangeToDate() {
        if (this.descHoldingsDateRangeToDate == null) {
            this.descHoldingsDateRangeToDate = new ArrayList<List<String>>();
        }
        return this.descHoldingsDateRangeToDate;
    }

    /**
     * @param descHoldingsDateRangeToDate {@link List<List<String>>} the
     * descHoldingsDateRangeToDate to set
     */
    public void setDescHoldingsDateRangeToDate(
            List<List<String>> descHoldingsDateRangeToDate) {
        this.descHoldingsDateRangeToDate = descHoldingsDateRangeToDate;
    }

    /**
     * @param descHoldingsDateRangeToDate the descHoldingsDateRangeToDate to add
     */
    /*public void addDescHoldingsDateRangeToDate(List<String> descHoldingsDateRangeToDate) {
		this.getDescHoldingsDateRangeToDate().add(descHoldingsDateRangeToDate);
	}*/
    /**
     * @return {@link List<List<String>>} the descExtent to get
     */
    public List<List<String>> getDescExtent() {
        if (this.descExtent == null) {
            this.descExtent = new ArrayList<List<String>>();
        }
        return this.descExtent;
    }

    /**
     * @param descExtent {@link List<List<String>>} the descExtent to set
     */
    public void setDescExtent(List<List<String>> descExtent) {
        this.descExtent = descExtent;
    }

    /**
     * @param descExtent the descExtent to add
     */
    /*public void addDescExtent(List<String> descExtent) {
		this.getDescExtent().add(descExtent);
	}*/
    /**
     * @return {@link List<List<String>>} the descExtentUnit to get
     */
    public List<List<String>> getDescExtentUnit() {
        if (this.descExtentUnit == null) {
            this.descExtentUnit = new ArrayList<List<String>>();
        }
        return this.descExtentUnit;
    }

    /**
     * @param descExtentUnit {@link List<List<String>>} the descExtentUnit to
     * set
     */
    public void setDescExtentUnit(List<List<String>> descExtentUnit) {
        this.descExtentUnit = descExtentUnit;
    }

    /**
     * @param descExtentUnit the descExtentUnit to add
     */
    /*public void addDescExtentUnit(List<String> descExtentUnit) {
		this.getDescExtentUnit().add(descExtentUnit);
	}*/
    /**
     * @return {@link String} the controlAgentLang to get
     */
    public String getControlAgentLang() {
        return this.controlAgentLang;
    }

    /**
     * @param controlAgentLang {@link String} the controlAgentLang to set
     */
    public void setControlAgentLang(String controlAgentLang) {
        this.controlAgentLang = controlAgentLang;
    }

    /**
     * @return {@link String} the controlAgencyCode to get
     */
    public String getControlAgencyCode() {
        return this.controlAgencyCode;
    }

    /**
     * @param controlAgencyCode {@link String} the controlAgencyCode to set
     */
    public void setControlAgencyCode(String controlAgencyCode) {
        this.controlAgencyCode = controlAgencyCode;
    }

    /**
     * @return {@link List<String>} the controlLanguageDeclaration to get
     */
    public List<String> getControlLanguageDeclaration() {
        if (this.controlLanguageDeclaration == null) {
            this.controlLanguageDeclaration = new ArrayList<String>();
        }
        return this.controlLanguageDeclaration;
    }

    /**
     * @param controlLanguageDeclaration {@link List<String>} the
     * controlLanguageDeclaration to set
     */
    public void setControlLanguageDeclaration(
            List<String> controlLanguageDeclaration) {
        this.controlLanguageDeclaration = controlLanguageDeclaration;
    }

    /**
     * @return {@link List<String>} the controlScript to get
     */
    public List<String> getControlScript() {
        if (this.controlScript == null) {
            this.controlScript = new ArrayList<String>();
        }
        return this.controlScript;
    }

    /**
     * @param controlScript {@link List<String>} the controlScript to set
     */
    public void setControlScript(List<String> controlScript) {
        this.controlScript = controlScript;
    }

    /**
     * @return {@link List<String>} the controlNumberOfLanguages to get
     */
    public List<String> getControlNumberOfLanguages() {
        if (this.controlNumberOfLanguages == null) {
            this.controlNumberOfLanguages = new ArrayList<String>();
        }
        return this.controlNumberOfLanguages;
    }

    /**
     * @param controlNumberOfLanguages {@link List<String>} the
     * controlNumberOfLanguages to set
     */
    public void setControlNumberOfLanguages(List<String> controlNumberOfLanguages) {
        this.controlNumberOfLanguages = controlNumberOfLanguages;
    }

    /**
     * @return {@link List<String>} the controlNumberOfRules to get
     */
    public List<String> getControlNumberOfRules() {
        if (this.controlNumberOfRules == null) {
            this.controlNumberOfRules = new ArrayList<String>();
        }
        return this.controlNumberOfRules;
    }

    /**
     * @param controlNumberOfRules {@link List<String>} the controlNumberOfRules
     * to set
     */
    public void setControlNumberOfRules(List<String> controlNumberOfRules) {
        this.controlNumberOfRules = controlNumberOfRules;
    }

    /**
     * @return {@link List<String>} the controlAbbreviation to get
     */
    public List<String> getControlAbbreviation() {
        if (this.controlAbbreviation == null) {
            this.controlAbbreviation = new ArrayList<String>();
        }
        return this.controlAbbreviation;
    }

    /**
     * @param controlAbbreviation {@link List<String>} the controlAbbreviation
     * to set
     */
    public void setControlAbbreviation(List<String> controlAbbreviation) {
        this.controlAbbreviation = controlAbbreviation;
    }

    /**
     * @return {@link List<String>} the controlCitation to get
     */
    public List<String> getControlCitation() {
        if (this.controlCitation == null) {
            this.controlCitation = new ArrayList<String>();
        }
        return this.controlCitation;
    }

    /**
     * @param controlCitation {@link List<String>} the controlCitation to set
     */
    public void setControlCitation(List<String> controlCitation) {
        this.controlCitation = controlCitation;
    }

    /**
     * @return {@link List<String>} the relationsResourceRelationType to get
     */
    public List<String> getRelationsResourceRelationType() {
        if (this.relationsResourceRelationType == null) {
            this.relationsResourceRelationType = new ArrayList<String>();
        }
        return this.relationsResourceRelationType;
    }

    /**
     * @param relationsResourceRelationType {@link List<String>} the
     * relationsResourceRelationType to set
     */
    public void setRelationsResourceRelationType(
            List<String> relationsResourceRelationType) {
        this.relationsResourceRelationType = relationsResourceRelationType;
    }

    /**
     * @param relationsResourceRelationType {@link List<String>} the
     * relationsResourceRelationType to add
     */
    public void addRelationsResourceRelationType(String relationsResourceRelationType) {
        this.getRelationsResourceRelationType().add(relationsResourceRelationType);
    }

    /**
     * @return {@link List<String>} the relationsResourceRelationHref to get
     */
    public List<String> getRelationsResourceRelationHref() {
        if (this.relationsResourceRelationHref == null) {
            this.relationsResourceRelationHref = new ArrayList<String>();
        }
        return this.relationsResourceRelationHref;
    }

    /**
     * @param relationsResourceRelationHref {@link List<String>} the
     * relationsResourceRelationHref to set
     */
    public void setRelationsResourceRelationHref(
            List<String> relationsResourceRelationHref) {
        this.relationsResourceRelationHref = relationsResourceRelationHref;
    }

    /**
     * @param relationsResourceRelationHref {@link List<String>} the
     * relationsResourceRelationHref to add
     */
    public void addRelationsResourceRelationHref(String relationsResourceRelationHref) {
        this.getRelationsResourceRelationHref().add(relationsResourceRelationHref);
    }

    /**
     * @return {@link List<String>} the relationsResourceRelationEntry to get
     */
    public List<String> getRelationsResourceRelationEntry() {
        if (this.relationsResourceRelationEntry == null) {
            this.relationsResourceRelationEntry = new ArrayList<String>();
        }
        return this.relationsResourceRelationEntry;
    }

    /**
     * @param relationsResourceRelationEntry {@link List<String>} the
     * relationsResourceRelationEntry to set
     */
    public void setRelationsResourceRelationEntry(
            List<String> relationsResourceRelationEntry) {
        this.relationsResourceRelationEntry = relationsResourceRelationEntry;
    }

    /**
     * @param relationsResourceRelationEntry {@link List<String>} the
     * relationsResourceRelationEntry to add
     */
    public void addRelationsResourceRelationEntry(String relationsResourceRelationEntry) {
        this.getRelationsResourceRelationEntry().add(relationsResourceRelationEntry);
    }

    /**
     * @return {@link List<String>} the relationsResourceRelationEntryLang to
     * get
     */
    public List<String> getRelationsResourceRelationEntryLang() {
        if (this.relationsResourceRelationEntryLang == null) {
            this.relationsResourceRelationEntryLang = new ArrayList<String>();
        }
        return this.relationsResourceRelationEntryLang;
    }

    /**
     * @param relationsResourceRelationEntryLang {@link List<String>} the
     * relationsResourceRelationEntryLang to set
     */
    public void setRelationsResourceRelationEntryLang(
            List<String> relationsResourceRelationEntryLang) {
        this.relationsResourceRelationEntryLang = relationsResourceRelationEntryLang;
    }

    /**
     * @param relationsResourceRelationEntryLang {@link List<String>} the
     * relationsResourceRelationEntryLang to add
     */
    public void addRelationsResourceRelationEntryLang(String relationsResourceRelationEntryLang) {
        this.getRelationsResourceRelationEntryLang().add(relationsResourceRelationEntryLang);
    }

    /**
     * @return {@link List<String>} the
     * relationsResourceRelationEntryDescription to get
     */
    public List<String> getRelationsResourceRelationEntryDescription() {
        if (this.relationsResourceRelationEntryDescription == null) {
            this.relationsResourceRelationEntryDescription = new ArrayList<String>();
        }
        return this.relationsResourceRelationEntryDescription;
    }

    /**
     * @param relationsResourceRelationEntryDescription {@link List<String>} the
     * relationsResourceRelationEntryDescription to set
     */
    public void setRelationsResourceRelationEntryDescription(
            List<String> relationsResourceRelationEntryDescription) {
        this.relationsResourceRelationEntryDescription = relationsResourceRelationEntryDescription;
    }

    /**
     * @param relationsResourceRelationEntryDescription {@link List<String>} the
     * relationsResourceRelationEntryDescription to add
     */
    public void addRelationsResourceRelationEntryDescription(String relationsResourceRelationEntryDescription) {
        this.getRelationsResourceRelationEntryDescription().add(relationsResourceRelationEntryDescription);
    }

    /**
     * @return {@link List<String>} the
     * relationsResourceRelationEntryDescriptionLang to get
     */
    public List<String> getRelationsResourceRelationEntryDescriptionLang() {
        if (this.relationsResourceRelationEntryDescriptionLang == null) {
            this.relationsResourceRelationEntryDescriptionLang = new ArrayList<String>();
        }
        return this.relationsResourceRelationEntryDescriptionLang;
    }

    /**
     * @param relationsResourceRelationEntryDescriptionLang {@link List<String>}
     * the relationsResourceRelationEntryDescriptionLang to set
     */
    public void setRelationsResourceRelationEntryDescriptionLang(
            List<String> relationsResourceRelationEntryDescriptionLang) {
        this.relationsResourceRelationEntryDescriptionLang = relationsResourceRelationEntryDescriptionLang;
    }

    /**
     * @param relationsResourceRelationEntryDescriptionLang {@link List<String>}
     * the relationsResourceRelationEntryDescriptionLang to add
     */
    public void addRelationsResourceRelationEntryDescriptionLang(String relationsResourceRelationEntryDescriptionLang) {
        this.getRelationsResourceRelationEntryDescriptionLang().add(relationsResourceRelationEntryDescriptionLang);
    }

    /**
     * @return {@link List<String>} the relationsNumberOfEagRelations to get
     */
    public List<String> getRelationsNumberOfEagRelations() {
        if (this.relationsNumberOfEagRelations == null) {
            this.relationsNumberOfEagRelations = new ArrayList<String>();
        }
        return this.relationsNumberOfEagRelations;
    }

    /**
     * @param relationsNumberOfEagRelations {@link List<String>} the
     * relationsNumberOfEagRelations to set
     */
    public void setRelationsNumberOfEagRelations(
            List<String> relationsNumberOfEagRelations) {
        this.relationsNumberOfEagRelations = relationsNumberOfEagRelations;
    }

    /**
     * @param relationsNumberOfEagRelations {@link String} the
     * relationsNumberOfEagRelations to add
     */
    public void addRelationsNumberOfEagRelations(String relationsNumberOfEagRelations) {
        this.getRelationsNumberOfEagRelations().add(relationsNumberOfEagRelations);
    }

    /**
     * @return {@link List<String>} the relationsEagRelationType to get
     */
    public List<String> getRelationsEagRelationType() {
        if (this.relationsEagRelationType == null) {
            this.relationsEagRelationType = new ArrayList<String>();
        }
        return this.relationsEagRelationType;
    }

    /**
     * @param relationsEagRelationType {@link List<String>} the
     * relationsEagRelationType to set
     */
    public void setRelationsEagRelationType(List<String> relationsEagRelationType) {
        this.relationsEagRelationType = relationsEagRelationType;
    }

    /**
     * @param relationsEagRelationType {@link String} the
     * relationsEagRelationType to add
     */
    public void addRelationsEagRelationType(String relationsEagRelationType) {
        this.getRelationsEagRelationType().add(relationsEagRelationType);
    }

    /**
     * @return {@link List<String>} the relationsEagRelationHref to get
     */
    public List<String> getRelationsEagRelationHref() {
        if (this.relationsEagRelationHref == null) {
            this.relationsEagRelationHref = new ArrayList<String>();
        }
        return this.relationsEagRelationHref;
    }

    /**
     * @param relationsEagRelationHref {@link List<String>} the
     * relationsEagRelationHref to set
     */
    public void setRelationsEagRelationHref(List<String> relationsEagRelationHref) {
        this.relationsEagRelationHref = relationsEagRelationHref;
    }

    /**
     * @param relationsEagRelationHref {@link String} the
     * relationsEagRelationHref to add
     */
    public void addRelationsEagRelationHref(String relationsEagRelationHref) {
        this.getRelationsEagRelationHref().add(relationsEagRelationHref);
    }

    /**
     * @return {@link List<String>} the relationsEagRelationEntry to get
     */
    public List<String> getRelationsEagRelationEntry() {
        if (this.relationsEagRelationEntry == null) {
            this.relationsEagRelationEntry = new ArrayList<String>();
        }
        return this.relationsEagRelationEntry;
    }

    /**
     * @param relationsEagRelationEntry {@link List<String>} the
     * relationsEagRelationEntry to set
     */
    public void setRelationsEagRelationEntry(List<String> relationsEagRelationEntry) {
        this.relationsEagRelationEntry = relationsEagRelationEntry;
    }

    /**
     * @param relationsEagRelationEntry {@link String} the
     * relationsEagRelationEntry to add
     */
    public void addRelationsEagRelationEntry(String relationsEagRelationEntry) {
        this.getRelationsEagRelationEntry().add(relationsEagRelationEntry);
    }

    /**
     * @return {@link List<String>} the relationsEagRelationEntryLang to get
     */
    public List<String> getRelationsEagRelationEntryLang() {
        if (this.relationsEagRelationEntryLang == null) {
            this.relationsEagRelationEntryLang = new ArrayList<String>();
        }
        return this.relationsEagRelationEntryLang;
    }

    /**
     * @param relationsEagRelationEntryLang {@link List<String>} the
     * relationsEagRelationEntryLang to set
     */
    public void setRelationsEagRelationEntryLang(
            List<String> relationsEagRelationEntryLang) {
        this.relationsEagRelationEntryLang = relationsEagRelationEntryLang;
    }

    /**
     * @param relationsEagRelationEntryLang {@link String} the
     * relationsEagRelationEntryLang to add
     */
    public void addRelationsEagRelationEntryLang(String relationsEagRelationEntryLang) {
        this.getRelationsEagRelationEntryLang().add(relationsEagRelationEntryLang);
    }

    /**
     * @return {@link List<String>} the relationsEagRelationEntryDescription to
     * get
     */
    public List<String> getRelationsEagRelationEntryDescription() {
        if (this.relationsEagRelationEntryDescription == null) {
            this.relationsEagRelationEntryDescription = new ArrayList<String>();
        }
        return this.relationsEagRelationEntryDescription;
    }

    /**
     * @param relationsEagRelationEntryDescription {@link List<String>} the
     * relationsEagRelationEntryDescription to set
     */
    public void setRelationsEagRelationEntryDescription(
            List<String> relationsEagRelationEntryDescription) {
        this.relationsEagRelationEntryDescription = relationsEagRelationEntryDescription;
    }

    /**
     * @param relationsEagRelationEntryDescription {@link String} the
     * relationsEagRelationEntryDescription to add
     */
    public void addRelationsEagRelationEntryDescription(String relationsEagRelationEntryDescription) {
        this.getRelationsEagRelationEntryDescription().add(relationsEagRelationEntryDescription);
    }

    /**
     * @return {@link List<String>} the relationsEagRelationEntryDescriptionLang
     * to get
     */
    public List<String> getRelationsEagRelationEntryDescriptionLang() {
        if (this.relationsEagRelationEntryDescriptionLang == null) {
            this.relationsEagRelationEntryDescriptionLang = new ArrayList<String>();
        }
        return this.relationsEagRelationEntryDescriptionLang;
    }

    /**
     * @param relationsEagRelationEntryDescriptionLang {@link List<String>} the
     * relationsEagRelationEntryDescriptionLang to set
     */
    public void setRelationsEagRelationEntryDescriptionLang(
            List<String> relationsEagRelationEntryDescriptionLang) {
        this.relationsEagRelationEntryDescriptionLang = relationsEagRelationEntryDescriptionLang;
    }

    /**
     * @param relationsEagRelationEntryDescriptionLang {@link String} the
     * relationsEagRelationEntryDescriptionLang to add
     */
    public void addRelationsEagRelationEntryDescriptionLang(String relationsEagRelationEntryDescriptionLang) {
        this.getRelationsEagRelationEntryDescriptionLang().add(relationsEagRelationEntryDescriptionLang);
    }

    /**
     * Method to launch the edition form for the EAG2012.
     *
     * @return success {@link String}
     * @throws Exception
     */
    public String editWebFormEAG2012() throws Exception {
        if (this.eagPath != null && !this.eagPath.isEmpty() && this.aiId != null) {
            try {
                log.debug("launching loader for institution with aiId: " + this.aiId);
                new EAG2012Loader(this.aiId);
            } catch (Exception e) {
                log.error("ERROR trying to put EAG parameters to webFormEag2012");
            }
        } else {
            log.warn("aiId or eagPath are wrong");
        }
        return "success";
    }

    public void setEagPath(String eagPath) {
        this.eagPath = eagPath;
    }

    /**
     * Method for load values {@link Eag2012} EAG2012: fill number of
     * repositories and fill values of tabs.
     *
     * @return {@link boolean} true o false if {@link eag} eag is null.
     */
    public boolean loadValuesEAG2012() {
        if (this.eag == null
                || (this.eag.getControl() == null && this.eag.getArchguide() == null
                && this.eag.getRelations() == null)) {
            this.setCountryCode(new ArchivalLandscapeUtils().getmyCountry());
            this.setRecordId(Eag2012.generatesISOCode(this.getId()));
            return false;
        }
        // Fill number of repositories.
        if (this.eag != null && this.eag.getArchguide() != null
                && this.eag.getArchguide().getDesc() != null
                && this.eag.getArchguide().getDesc().getRepositories() != null
                && !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
            this.setNumberOfRepositories(this.eag.getArchguide().getDesc().getRepositories().getRepository().size());
        }
        // Fill values of "Your institution" tab.
        LoadYourinstitutionTabValues loadYourinstitutionTabValues = new LoadYourinstitutionTabValues();
        loadYourinstitutionTabValues.LoaderEAG2012(eag, this);
        // Fill values of "Identity" tab.
        LoadIdentityTabValues loadIdentityTabValues = new LoadIdentityTabValues();
        loadIdentityTabValues.LoaderEAG2012(eag, this);
        // Fill values of "Contact" tab.
        LoadContactTabValues loadContactTabValues = new LoadContactTabValues();
        loadContactTabValues.LoaderEAG2012(eag, this);
        // Fill values of "Access and Services" tab.
        LoadAccessAndServicesTabValues loadAccessAndServicesTabValues = new LoadAccessAndServicesTabValues();
        loadAccessAndServicesTabValues.LoaderEAG2012(eag, this);
        // Fill values of "Description" tab.
        LoadDescriptionTabValues loadDescriptionTabValues = new LoadDescriptionTabValues();
        loadDescriptionTabValues.LoaderEAG2012(eag, this);
        // Fill values of "Control" tab.
        LoadControlTabValues loadControlTabValues = new LoadControlTabValues();
        loadControlTabValues.LoaderEAG2012(eag, this);
        // Fill values of "Relations" tab.
        LoadRelationsTabValues Relations = new LoadRelationsTabValues();
        Relations.LoaderEAG2012(eag, this);
        return true;
    }

    /**
     * Method to replace the duplicated whitespaces inside a string.
     *
     * @param str {@link String}
     * @return The string without whitespaces.
     */
    public String removeDuplicateWhiteSpaces(final String str) {
        this.log.debug("Method start: \"removeDuplicateWhiteSpaces\"");
        String strWith = str.trim().replaceAll("[\\s+]", " ");
        StringBuilder sb = new StringBuilder();
        boolean space = false;
        for (int i = 0; i < strWith.length(); i++) {
            if (!space && strWith.charAt(i) == ' ') {
                sb.append(' ');
                space = true;
            } else if (strWith.charAt(i) != ' ') {
                sb.append(strWith.charAt(i));
                space = false;
            }
        }
        this.log.debug("End method: \"removeDuplicateWhiteSpaces\"");
        return sb.toString();
    }
}
