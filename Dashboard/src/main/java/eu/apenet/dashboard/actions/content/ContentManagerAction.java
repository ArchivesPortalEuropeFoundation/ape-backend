package eu.apenet.dashboard.actions.content;

import java.util.*;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxConversionOptionsConstants;
import eu.apenet.dashboard.actions.content.eaccpf.EacCpfContentManagerResults;
import eu.apenet.dashboard.actions.content.ead.EadContentManagerResults;
import eu.apenet.dashboard.actions.content.ead3.Ead3ContentManagerResults;
import eu.apenet.dashboard.listener.QueueDaemon;
import eu.apenet.dashboard.queue.DisplayQueueItem;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;

public class ContentManagerAction extends AbstractInstitutionAction {

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    public static final String EAD_SEARCH_OPTIONS = "eadSearchOptions";
    protected static final String CONTENT_MESSAGE_NO = "content.message.no";
    protected static final String CONTENT_MESSAGE_YES = "content.message.yes";
    protected static final String CONTENT_MESSAGE_QUEUE = "content.message.queue";

    protected static final String CONTENT_MESSAGE_ERROR = "content.message.fatalerror";
    protected static final String SUCCESS_AJAX = "success_ajax";

    // Constants for the selects in the "Convert options" colorBox.
    // DAO role.
    private static final String TYPE_3D = "3D"; // Constant for type "3D".
    private static final String TYPE_IMAGE = "IMAGE"; // Constant for type "image".
    private static final String TYPE_SOUND = "TEXT"; // Constant for type "sound".
    private static final String TYPE_TEXT = "TEXT"; // Constant for type "text".
    private static final String TYPE_UNSPECIFIED = "UNSPECIFIED"; // Constant for type "unspecified".
    private static final String TYPE_VIDEO = "VIDEO"; // Constant for type "video".

    /**
     *
     */
    private static final long serialVersionUID = 4513310293148562803L;
    private Map<String, String> typeList = new LinkedHashMap<String, String>();
    private String xmlTypeId = XmlType.EAD_FA.getIdentifier() + "";
    private Map<String, String> convertedStatusList = new LinkedHashMap<String, String>();
    private String[] convertedStatus = new String[]{FALSE, TRUE};
    private Map<String, String> validatedStatusList = new LinkedHashMap<String, String>();
    private String[] validatedStatus = new String[]{ValidatedState.NOT_VALIDATED.toString(),
        ValidatedState.VALIDATED.toString(), ValidatedState.FATAL_ERROR.toString()};
    private String[] linkedStatus = new String[]{FALSE, TRUE};
    private Map<String, String> linkedStatusList = new LinkedHashMap<String, String>();
    private Map<String, String> publishedStatusList = new LinkedHashMap<String, String>();
    private String[] publishedStatus = new String[]{FALSE, TRUE};
    private Map<String, String> europeanaStatusList = new LinkedHashMap<String, String>();
    private String[] europeanaStatus = new String[]{EuropeanaState.NOT_CONVERTED.toString(),
        EuropeanaState.CONVERTED.toString(), EuropeanaState.DELIVERED.toString(),
        EuropeanaState.NO_EUROPEANA_CANDIDATE.toString(), EuropeanaState.FATAL_ERROR.toString()};
    private Map<String, String> queuingStatusList = new LinkedHashMap<String, String>();
    private String[] queuingStatus = new String[]{QueuingState.NO.toString(),
        QueuingState.READY.toString(), QueuingState.BUSY.toString(),
        QueuingState.ERROR.toString()};
    private Map<String, String> searchTermsFieldList = new LinkedHashMap<String, String>();
    private Integer pageNumber = 1;
    private Integer resultPerPage = 30;
    private String orderByField = "id";
    private boolean orderByAscending = false;
    private String searchTerms;
    private String searchTermsField;
    private boolean updateSearchResults = false;
    private boolean ajax = false;
    private boolean errorLinkHgSg = false;

    // Values for selects in the "Convert options" colorBox.
    private Set<SelectItem> daoTypes = new LinkedHashSet<SelectItem>(); // DAO types.
    private Set<SelectItem> rightsDigitalObjects = new LinkedHashSet<SelectItem>(); // Rights for digital objects.
    private Set<SelectItem> rightsEadData = new LinkedHashSet<SelectItem>(); // Rights for EAD data.

    // Select or not the check for the DAO type.
    private boolean daoTypeCheck = true;

    public ContentManagerAction() {
    }

    public void prepareJspLists() {
        convertedStatusList.put(TRUE, getText(CONTENT_MESSAGE_YES));
        convertedStatusList.put(FALSE, getText(CONTENT_MESSAGE_NO));
        validatedStatusList.put(ValidatedState.VALIDATED.toString(), getText(CONTENT_MESSAGE_YES));
        validatedStatusList.put(ValidatedState.NOT_VALIDATED.toString(), getText(CONTENT_MESSAGE_NO));
        validatedStatusList.put(ValidatedState.FATAL_ERROR.toString(), getText(CONTENT_MESSAGE_ERROR));
        linkedStatusList.put(TRUE, getText(CONTENT_MESSAGE_YES));
        linkedStatusList.put(FALSE, getText(CONTENT_MESSAGE_NO));
        publishedStatusList.put(TRUE, getText(CONTENT_MESSAGE_YES));
        publishedStatusList.put(FALSE, getText(CONTENT_MESSAGE_NO));
        europeanaStatusList.put(EuropeanaState.CONVERTED.toString(), getText("content.message.eseedm"));
        europeanaStatusList.put(EuropeanaState.NOT_CONVERTED.toString(), getText(CONTENT_MESSAGE_NO));
        europeanaStatusList.put(EuropeanaState.DELIVERED.toString(), getText("content.message.europeana.delivered"));
        europeanaStatusList.put(EuropeanaState.NO_EUROPEANA_CANDIDATE.toString(), getText("content.message.europeana.nochos"));
        europeanaStatusList.put(EuropeanaState.FATAL_ERROR.toString(), getText(CONTENT_MESSAGE_ERROR));

        queuingStatusList.put(QueuingState.NO.toString(), getText(CONTENT_MESSAGE_NO));
        queuingStatusList.put(QueuingState.READY.toString(), getText("content.message.ready"));
        queuingStatusList.put(QueuingState.BUSY.toString(), getText("content.message.queueprocessing"));
        queuingStatusList.put(QueuingState.ERROR.toString(), getText(CONTENT_MESSAGE_ERROR));
        typeList.put(XmlType.EAD_FA.getIdentifier() + "",
                getText("content.message.ead2002." + XmlType.EAD_FA.getResourceName()));
// HIDE EAD3
//        typeList.put(XmlType.EAD_3.getIdentifier() + "",
//                getText("content.message.fa." + XmlType.EAD_3.getResourceName()));
        typeList.put(XmlType.EAD_HG.getIdentifier() + "",
                getText("content.message." + XmlType.EAD_HG.getResourceName()));
        typeList.put(XmlType.EAD_SG.getIdentifier() + "",
                getText("content.message." + XmlType.EAD_SG.getResourceName()));
        typeList.put(XmlType.EAC_CPF.getIdentifier() + "",
                getText("content.message." + XmlType.EAC_CPF.getResourceName()));

        searchTermsFieldList.put("", getText("content.message.all"));
        searchTermsFieldList.put("eadid", getText("content.message.id"));
        searchTermsFieldList.put("title", getText("content.message.title"));

        // Fill list related to the "Convert options" colorBox.
        // DAO types.
        this.getDaoTypes().add(new SelectItem(ContentManagerAction.TYPE_UNSPECIFIED, getText("ead2ese.content.type.unspecified")));
        this.getDaoTypes().add(new SelectItem(ContentManagerAction.TYPE_3D, getText("ead2ese.content.type.3D")));
        this.getDaoTypes().add(new SelectItem(ContentManagerAction.TYPE_IMAGE, getText("ead2ese.content.type.image")));
        this.getDaoTypes().add(new SelectItem(ContentManagerAction.TYPE_SOUND, getText("ead2ese.content.type.sound")));
        this.getDaoTypes().add(new SelectItem(ContentManagerAction.TYPE_TEXT, getText("ead2ese.content.type.text")));
        this.getDaoTypes().add(new SelectItem(ContentManagerAction.TYPE_VIDEO, getText("ead2ese.content.type.video")));
        // Set of available permissions.
        Set<SelectItem> rightsSet = this.addRightsOptions();
        // Rights for digital objects.
        this.getRightsDigitalObjects().addAll(rightsSet);
        // Rights for EAD data.
        this.getRightsEadData().addAll(rightsSet);

    }

    /**
     * Method to create the set with all the available rights statements for EAD
     * data.
     *
     * @return Set with all the available rights statements for EAD data.
     */
    private Set<SelectItem> addRightsOptions() {
        Set<SelectItem> rightsSet = new LinkedHashSet<SelectItem>();
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.NO_SELECTED, "---"));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.PUBLIC_DOMAIN_MARK, getText("content.message.rights.public.domain")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_CC0_PUBLIC, getText("content.message.rights.creative.public.domain")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION, getText("content.message.rights.creative.attribution")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_SHARE, getText("content.message.rights.creative.attribution.sharealike")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATES, getText("content.message.rights.creative.attribution.no.derivates")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NON_COMERCIAL, getText("content.message.rights.creative.attribution.non.commercial")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_SHARE, getText("content.message.rights.creative.attribution.non.commercial.sharealike")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_NO_DERIVATES, getText("content.message.rights.creative.attribution.non.commercial.no.derivates")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.EUROPEANA_COPYRIGHT_NOT_EVALUATED, getText("ead2ese.content.license.europeana.copyrightnotevaluated")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT, getText("ead2ese.content.license.europeana.incopyright")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT_EU_ORPHAN_WORK, getText("ead2ese.content.license.europeana.incopyright.euorphan")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.EUROPEANA_IN_COPYRIGHT_EDUCATIONAL_USE_ONLY, getText("ead2ese.content.license.europeana.incopyright.eduuse")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.EUROPEANA_NO_COPYRIGHT_NONCOMMERCIAL_USE_ONLY, getText("ead2ese.content.license.europeana.nocopyright.noncommercial")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.EUROPEANA_NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS, getText("ead2ese.content.license.europeana.nocopyright.otherlegal")));

        return rightsSet;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getResultPerPage() {
        return resultPerPage;
    }

    public void setResultPerPage(Integer resultPerPage) {
        this.resultPerPage = resultPerPage;
    }

    public String getOrderByField() {
        return orderByField;
    }

    public void setOrderByField(String orderByField) {
        this.orderByField = orderByField;
    }

    public boolean isOrderByAscending() {
        return orderByAscending;
    }

    public void setOrderByAscending(boolean orderByAscending) {
        this.orderByAscending = orderByAscending;
    }

    @Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("breadcrumb.section.contentmanager"));
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();
    }

    @Override
    public String input() throws Exception {
        prepareJspLists();
        ContentSearchOptions contentSearchOptions = initFromExistingContentSearchOptions();
        if (contentSearchOptions == null) {
            contentSearchOptions = createNewContentSearchOptions();
            contentSearchOptions.setContentClass(FindingAid.class);
            getServletRequest().getSession().setAttribute(EAD_SEARCH_OPTIONS, contentSearchOptions);
        }

        if (xmlTypeId.equals(XmlType.EAC_CPF.getIdentifier() + "")) {
            return processEacCpf(contentSearchOptions);
        } else if (xmlTypeId.equals(XmlType.EAD_3.getIdentifier() + "")) {
            return processEad3(contentSearchOptions);
        } else {
            return processEad(contentSearchOptions);
        }
    }

    @Override
    public String execute() throws Exception {
        ContentSearchOptions eadSearchOptions = null;
        if (updateSearchResults) {
            eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(EAD_SEARCH_OPTIONS);
            if (eadSearchOptions == null) {
                eadSearchOptions = createNewContentSearchOptions();
            }
            eadSearchOptions.setPageNumber(pageNumber);
            eadSearchOptions.setOrderByAscending(orderByAscending);
            eadSearchOptions.setOrderByField(orderByField);
            eadSearchOptions.setPageSize(resultPerPage);
        } else {
            eadSearchOptions = createNewContentSearchOptions();
        }
        getServletRequest().getSession().setAttribute(EAD_SEARCH_OPTIONS, eadSearchOptions);
        return input();
    }

    private ContentSearchOptions initFromExistingContentSearchOptions() {
        ContentSearchOptions contentSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(EAD_SEARCH_OPTIONS);
        if (contentSearchOptions != null) {
            if (contentSearchOptions.getConverted() != null) {
                convertedStatus = new String[]{contentSearchOptions.getConverted().toString()};
            }
            if (contentSearchOptions.getLinked() != null) {
                linkedStatus = new String[]{contentSearchOptions.getLinked().toString()};
            }
            if (contentSearchOptions.getValidated().size() > 0) {
                validatedStatus = new String[contentSearchOptions.getValidated().size()];
                for (int i = 0; i < contentSearchOptions.getValidated().size(); i++) {
                    validatedStatus[i] = contentSearchOptions.getValidated().get(i).toString();
                }
            }
            if (contentSearchOptions.getPublished() != null) {
                publishedStatus = new String[]{contentSearchOptions.getPublished().toString()};
            }
            if (contentSearchOptions.getEuropeana().size() > 0) {
                europeanaStatus = new String[contentSearchOptions.getEuropeana().size()];
                for (int i = 0; i < contentSearchOptions.getEuropeana().size(); i++) {
                    europeanaStatus[i] = contentSearchOptions.getEuropeana().get(i).toString();
                }
            }

            if (contentSearchOptions.getQueuing().size() > 0) {
                queuingStatus = new String[contentSearchOptions.getQueuing().size()];
                for (int i = 0; i < contentSearchOptions.getQueuing().size(); i++) {
                    queuingStatus[i] = contentSearchOptions.getQueuing().get(i).toString();
                }
            }
            xmlTypeId = XmlType.getType(contentSearchOptions.getContentClass()).getIdentifier() + "";
            searchTerms = contentSearchOptions.getSearchTerms();
            orderByField = contentSearchOptions.getOrderByField();
            orderByAscending = contentSearchOptions.isOrderByAscending();
            searchTermsField = contentSearchOptions.getSearchTermsField();
            pageNumber = contentSearchOptions.getPageNumber();
            resultPerPage = contentSearchOptions.getPageSize();
            contentSearchOptions.setArchivalInstitionId(getAiId());
        }

        return contentSearchOptions;
    }

    private ContentSearchOptions createNewContentSearchOptions() {
        ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
        contentSearchOptions.setContentClass(FindingAid.class);

        contentSearchOptions.setPageNumber(pageNumber);
        contentSearchOptions.setPageSize(resultPerPage);
        contentSearchOptions.setOrderByAscending(orderByAscending);
        contentSearchOptions.setOrderByField(orderByField);
        contentSearchOptions.setArchivalInstitionId(getAiId());
        if (convertedStatus != null && convertedStatus.length == 1) {
            contentSearchOptions.setConverted(Boolean.valueOf(convertedStatus[0]));
        }
        if (linkedStatus != null && linkedStatus.length == 1) {
            contentSearchOptions.setLinked(Boolean.valueOf(linkedStatus[0]));
        }
        if (validatedStatus != null && validatedStatus.length >= 1 && validatedStatus.length <= 2) {
            for (String validatedStatusItem : validatedStatus) {
                contentSearchOptions.getValidated().add(ValidatedState.getValidatedState(validatedStatusItem));
            }
        }
        if (publishedStatus != null && publishedStatus.length == 1) {
            contentSearchOptions.setPublished(Boolean.valueOf(publishedStatus[0]));
        }
        if (europeanaStatus != null && europeanaStatus.length >= 1 && europeanaStatus.length <= 3) {
            for (String europeanaStatusItem : europeanaStatus) {
                contentSearchOptions.getEuropeana().add(EuropeanaState.getEuropeanaState(europeanaStatusItem));
            }
        }
        if (queuingStatus != null && queuingStatus.length >= 1 && queuingStatus.length <= 3) {
            for (String queuingStatusItem : queuingStatus) {
                contentSearchOptions.getQueuing().add(QueuingState.getQueuingState(queuingStatusItem));
            }
        }
        contentSearchOptions.setSearchTerms(searchTerms);
        contentSearchOptions.setSearchTermsField(searchTermsField);
        contentSearchOptions.setContentClass(XmlType.getType(Integer.parseInt(xmlTypeId)).getClazz());
        return contentSearchOptions;
    }

    private String processEad(ContentSearchOptions contentSearchOptions) {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        EadContentManagerResults results = new EadContentManagerResults(contentSearchOptions);
        results.setEads(eadDAO.getEads(contentSearchOptions));
        results.setTotalNumberOfResults(eadDAO.countEads(contentSearchOptions));
        Long countResults = results.totalNumberOfResults;
        /*
         * statistics for total converted files
         */
        if (contentSearchOptions.getConverted() == null || contentSearchOptions.getConverted() == true) {
            ContentSearchOptions convertedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            convertedSearchOptions.setConverted(true);
            results.setTotalConvertedFiles(eadDAO.countEads(convertedSearchOptions));
        }
        /*
         * statistics for total validated files
         */
        if (contentSearchOptions.getValidated().isEmpty()
                || contentSearchOptions.getValidated().contains(ValidatedState.VALIDATED)) {
            ContentSearchOptions validatedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            if (validatedSearchOptions.getValidated().isEmpty()) {
                validatedSearchOptions.setValidated(ValidatedState.VALIDATED);
            }
            results.setTotalValidatedFiles(eadDAO.countEads(validatedSearchOptions));
        }
        /*
         * statistics for total published units
         */
        if (contentSearchOptions.getPublished() == null || contentSearchOptions.getPublished() == true) {
            ContentSearchOptions publishedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            publishedSearchOptions.setPublished(true);
            results.setTotalPublishedUnits(eadDAO.countUnits(publishedSearchOptions));
        }
        if (contentSearchOptions.getContentClass().equals(FindingAid.class)) {
            /*
             * statistics for total delivered daos
             */
            if (contentSearchOptions.getEuropeana().isEmpty()
                    || contentSearchOptions.getEuropeana().contains(EuropeanaState.DELIVERED)) {
                ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
                europeanaSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
                results.setTotalChosDeliveredToEuropeana(eadDAO.countChos(europeanaSearchOptions));
            }
            /*
             * statistics for total converted daos
             */
            if (contentSearchOptions.getEuropeana().isEmpty()
                    || contentSearchOptions.getEuropeana().contains(EuropeanaState.CONVERTED)) {
                ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
                europeanaSearchOptions.getEuropeana().clear();
                europeanaSearchOptions.getEuropeana().add(EuropeanaState.CONVERTED);
                europeanaSearchOptions.getEuropeana().add(EuropeanaState.DELIVERED);
                results.setTotalChos(eadDAO.countChos(europeanaSearchOptions));
            }

        }

        ContentSearchOptions dynamicEadSearchOptions = new ContentSearchOptions();
        dynamicEadSearchOptions.setPublished(false);
        dynamicEadSearchOptions.setDynamic(true);
        dynamicEadSearchOptions.setContentClass(HoldingsGuide.class);
        results.setHasDynamicHg(eadDAO.existEads(dynamicEadSearchOptions));
        dynamicEadSearchOptions.setContentClass(SourceGuide.class);
        results.setHasDynamicSg(eadDAO.existEads(dynamicEadSearchOptions));
        getServletRequest().setAttribute("results", results);
        getServletRequest().setAttribute("harvestingStarted", EadService.isHarvestingStarted());
        QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
        UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();
        getServletRequest().setAttribute("totalItemsInQueue", queueDAO.countItems());
        getServletRequest().setAttribute("aiItemsInQueue", queueDAO.countItems(getAiId()));
        getServletRequest().setAttribute("aiUpFiles", upFileDAO.countNewUpFiles(getAiId(), FileType.XML));
        getServletRequest().setAttribute("positionInQueue", queueDAO.getPositionOfFirstItem(getAiId()));
        getServletRequest().setAttribute("queueActive", QueueDaemon.isActive());
        getServletRequest().setAttribute("errorItems", convert(DAOFactory.instance().getQueueItemDAO().getErrorItemsOfInstitution(getAiId())));
        return SUCCESS;
    }

    private String processEad3(ContentSearchOptions contentSearchOptions) {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        Ead3ContentManagerResults results = new Ead3ContentManagerResults(contentSearchOptions);
        results.setEad3s(ead3DAO.getEad3s(contentSearchOptions));
        results.setTotalNumberOfResults(ead3DAO.countEad3s(contentSearchOptions));
        Long countResults = results.totalNumberOfResults;
        /*
         * statistics for total converted files
         */
        if (contentSearchOptions.getConverted() == null || contentSearchOptions.getConverted() == true) {
            ContentSearchOptions convertedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            convertedSearchOptions.setConverted(true);
            results.setTotalConvertedFiles(ead3DAO.countEad3s(convertedSearchOptions));
        }
        /*
         * statistics for total validated files
         */
        if (contentSearchOptions.getValidated().isEmpty()
                || contentSearchOptions.getValidated().contains(ValidatedState.VALIDATED)) {
            ContentSearchOptions validatedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            if (validatedSearchOptions.getValidated().isEmpty()) {
                validatedSearchOptions.setValidated(ValidatedState.VALIDATED);
            }
            results.setTotalValidatedFiles(ead3DAO.countEad3s(validatedSearchOptions));
        }
        /*
         * statistics for total published units
         */
        if (contentSearchOptions.getPublished() == null || contentSearchOptions.getPublished() == true) {
            ContentSearchOptions publishedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            publishedSearchOptions.setPublished(true);
            results.setTotalPublishedUnits(ead3DAO.countUnits(publishedSearchOptions));
        }
        /*
         * statistics for total delivered daos
         */
//            if (contentSearchOptions.getEuropeana().isEmpty()
//                    || contentSearchOptions.getEuropeana().contains(EuropeanaState.DELIVERED)) {
//                ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
//                europeanaSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
//                results.setTotalChosDeliveredToEuropeana(ead3DAO.countChos(europeanaSearchOptions));
//            }
//            /*
//            * statistics for total converted daos
//            */
//            if (contentSearchOptions.getEuropeana().isEmpty()
//                    || contentSearchOptions.getEuropeana().contains(EuropeanaState.CONVERTED)) {
//                ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
//                europeanaSearchOptions.getEuropeana().clear();
//                europeanaSearchOptions.getEuropeana().add(EuropeanaState.CONVERTED);
//                europeanaSearchOptions.getEuropeana().add(EuropeanaState.DELIVERED);
//                results.setTotalChos(ead3DAO.countChos(europeanaSearchOptions));
//            }

        ContentSearchOptions dynamicEadSearchOptions = new ContentSearchOptions();
        dynamicEadSearchOptions.setContentClass(FindingAid.class);
        dynamicEadSearchOptions.setPublished(false);
        dynamicEadSearchOptions.setDynamic(true);
        getServletRequest().setAttribute("results", results);
        getServletRequest().setAttribute("harvestingStarted", EadService.isHarvestingStarted());
        QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
        getServletRequest().setAttribute("totalItemsInQueue", queueDAO.countItems());
        getServletRequest().setAttribute("aiItemsInQueue", queueDAO.countItems(getAiId()));
        getServletRequest().setAttribute("positionInQueue", queueDAO.getPositionOfFirstItem(getAiId()));
        getServletRequest().setAttribute("queueActive", QueueDaemon.isActive());
        getServletRequest().setAttribute("errorItems", convert(DAOFactory.instance().getQueueItemDAO().getErrorItemsOfInstitution(getAiId())));
        return "ead3_success";
    }

    private List<DisplayQueueItem> convert(List<QueueItem> queueItems) {
        List<DisplayQueueItem> results = new ArrayList<DisplayQueueItem>();
        for (QueueItem queueItem : queueItems) {
            DisplayQueueItem displayItem = new DisplayQueueItem();
            displayItem.setId(queueItem.getId());
            displayItem.setAction(queueItem.getAction().toString());
            displayItem.setPriority(queueItem.getPriority());
            displayItem.setErrors(queueItem.getErrors());
            try {
                if (queueItem.getAbstractContent() != null) {
                    AbstractContent content = queueItem.getAbstractContent();
                    displayItem.setEadidOrFilename(content.getIdentifier());
                    displayItem.setArchivalInstitution(content.getArchivalInstitution().getAiname());
                } else if (queueItem.getUpFile() != null) {
                    UpFile upFile = queueItem.getUpFile();
                    displayItem.setEadidOrFilename(upFile.getPath() + upFile.getFilename());
                    displayItem.setArchivalInstitution(upFile.getArchivalInstitution().getAiname());
                }
                if (QueueAction.USE_PROFILE.equals(queueItem.getAction())) {
                    Properties preferences = EadService.readProperties(queueItem.getPreferences());
                    IngestionprofileDefaultUploadAction ingestionprofileDefaultUploadAction = IngestionprofileDefaultUploadAction
                            .getUploadAction(preferences.getProperty(QueueItem.UPLOAD_ACTION));
                    displayItem.setAction(displayItem.getAction() + " ("
                            + getText(ingestionprofileDefaultUploadAction.getResourceName()) + ")");
                }
            } catch (Exception e) {

            }
            results.add(displayItem);
        }
        return results;
    }

    private String processEacCpf(ContentSearchOptions contentSearchOptions) {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpfContentManagerResults results = new EacCpfContentManagerResults(contentSearchOptions);
        results.setEacCpfs(eacCpfDAO.getEacCpfs(contentSearchOptions));
        results.setTotalNumberOfResults(eacCpfDAO.countEacCpfs(contentSearchOptions));
        Long countResults = results.totalNumberOfResults;
        /*
         * statistics for number of converted files
         */
        if (contentSearchOptions.getConverted() == null || contentSearchOptions.getConverted() == true) {
            ContentSearchOptions convertedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            convertedSearchOptions.setConverted(true);
            results.setTotalConvertedFiles(eacCpfDAO.countEacCpfs(convertedSearchOptions));
        }
        /*
         * statistics for number of validated files
         */
        if (contentSearchOptions.getValidated().isEmpty()
                || contentSearchOptions.getValidated().contains(ValidatedState.VALIDATED)) {
            ContentSearchOptions validatedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            if (validatedSearchOptions.getValidated().isEmpty()) {
                validatedSearchOptions.setValidated(ValidatedState.VALIDATED);
            }
            results.setTotalValidatedFiles(eacCpfDAO.countEacCpfs(validatedSearchOptions));
        }
        /*
         * statistics for number of published files
         */
        if (contentSearchOptions.getPublished() == null || contentSearchOptions.getPublished() == true) {
            ContentSearchOptions publishedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            publishedSearchOptions.setPublished(true);
            results.setTotalPublishedUnits(eacCpfDAO.countEacCpfs(publishedSearchOptions));
        }
        /*
         * statistics for number of files delivered to Europeana
         */
        if (contentSearchOptions.getEuropeana().isEmpty()
                || contentSearchOptions.getEuropeana().contains(EuropeanaState.DELIVERED)) {
            ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
            europeanaSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
            results.setTotalChosDeliveredToEuropeana(eacCpfDAO.countEacCpfs(europeanaSearchOptions));
        }
        /*
         * statistics for number of files converted to EDM
         */
        if (contentSearchOptions.getEuropeana().isEmpty()
                || contentSearchOptions.getEuropeana().contains(EuropeanaState.CONVERTED)) {
            ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
            europeanaSearchOptions.getEuropeana().clear();
            europeanaSearchOptions.getEuropeana().add(EuropeanaState.CONVERTED);
            europeanaSearchOptions.getEuropeana().add(EuropeanaState.DELIVERED);
            results.setTotalChos(eacCpfDAO.countEacCpfs(europeanaSearchOptions));
        }
        getServletRequest().setAttribute("results", results);
//            getServletRequest().setAttribute("harvestingStarted", HarvesterDaemon.isHarvesterProcessing() || EadService.isHarvestingStarted());
        getServletRequest().setAttribute("queueActive", QueueDaemon.isActive());
        QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
        getServletRequest().setAttribute("totalItemsInQueue", queueDAO.countItems());
        getServletRequest().setAttribute("aiItemsInQueue", queueDAO.countItems(getAiId()));
        getServletRequest().setAttribute("positionInQueue", queueDAO.getPositionOfFirstItem(getAiId()));
        return "success-eaccpf";
    }

    public Map<String, String> getConvertedStatusList() {
        return convertedStatusList;
    }

    public void setConvertedStatusList(Map<String, String> convertedStatusList) {
        this.convertedStatusList = convertedStatusList;
    }

    public String[] getConvertedStatus() {
        return convertedStatus;
    }

    public void setConvertedStatus(String[] convertedStatus) {
        this.convertedStatus = convertedStatus;
    }

    public Map<String, String> getValidatedStatusList() {
        return validatedStatusList;
    }

    public void setValidatedStatusList(Map<String, String> validatedStatusList) {
        this.validatedStatusList = validatedStatusList;
    }

    public String[] getValidatedStatus() {
        return validatedStatus;
    }

    public void setValidatedStatus(String[] validatedStatus) {
        this.validatedStatus = validatedStatus;
    }

    public String getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    public Map<String, String> getPublishedStatusList() {
        return publishedStatusList;
    }

    public void setPublishedStatusList(Map<String, String> publishedStatusList) {
        this.publishedStatusList = publishedStatusList;
    }

    public String[] getPublishedStatus() {
        return publishedStatus;
    }

    public void setPublishedStatus(String[] publishedStatus) {
        this.publishedStatus = publishedStatus;
    }

    public Map<String, String> getEuropeanaStatusList() {
        return europeanaStatusList;
    }

    public void setEuropeanaStatusList(Map<String, String> europeanaStatusList) {
        this.europeanaStatusList = europeanaStatusList;
    }

    public String[] getEuropeanaStatus() {
        return europeanaStatus;
    }

    public void setEuropeanaStatus(String[] europeanaStatus) {
        this.europeanaStatus = europeanaStatus;
    }

    public Map<String, String> getTypeList() {
        return typeList;
    }

    public void setTypeList(Map<String, String> typeList) {
        this.typeList = typeList;
    }

    public String getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(String xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public Map<String, String> getSearchTermsFieldList() {
        return searchTermsFieldList;
    }

    public void setSearchTermsFieldList(Map<String, String> searchTermsFieldList) {
        this.searchTermsFieldList = searchTermsFieldList;
    }

    public String getSearchTermsField() {
        return searchTermsField;
    }

    public void setSearchTermsField(String searchTermsField) {
        this.searchTermsField = searchTermsField;
    }

    public Map<String, String> getQueuingStatusList() {
        return queuingStatusList;
    }

    public void setQueuingStatusList(Map<String, String> queuingStatusList) {
        this.queuingStatusList = queuingStatusList;
    }

    public String[] getQueuingStatus() {
        return queuingStatus;
    }

    public void setQueuingStatus(String[] queuingStatus) {
        this.queuingStatus = queuingStatus;
    }

    public String[] getLinkedStatus() {
        return linkedStatus;
    }

    public void setLinkedStatus(String[] linkedStatus) {
        this.linkedStatus = linkedStatus;
    }

    public Map<String, String> getLinkedStatusList() {
        return linkedStatusList;
    }

    public void setLinkedStatusList(Map<String, String> linkedStatusList) {
        this.linkedStatusList = linkedStatusList;
    }

    public boolean isUpdateSearchResults() {
        return updateSearchResults;
    }

    public void setUpdateSearchResults(boolean updateSearchResults) {
        this.updateSearchResults = updateSearchResults;
    }

    public boolean isErrorLinkHgSg() {
        return this.errorLinkHgSg;
    }

    public void setErrorLinkHgSg(boolean errorLinkHgSg) {
        this.errorLinkHgSg = errorLinkHgSg;
    }

    /**
     * @return the daoTypes
     */
    public Set<SelectItem> getDaoTypes() {
        return this.daoTypes;
    }

    /**
     * @param daoTypes the daoTypes to set
     */
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

    /**
     * @return the daoTypeCheck
     */
    public boolean isDaoTypeCheck() {
        return this.daoTypeCheck;
    }

    /**
     * @param daoTypeCheck the daoTypeCheck to set
     */
    public void setDaoTypeCheck(boolean daoTypeCheck) {
        this.daoTypeCheck = daoTypeCheck;
    }

}
