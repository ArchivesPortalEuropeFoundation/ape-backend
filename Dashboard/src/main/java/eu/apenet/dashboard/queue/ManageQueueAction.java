package eu.apenet.dashboard.queue;

import eu.apenet.commons.exceptions.ProcessBusyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ChangeControl;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.solr.SolrUtil;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.listener.HarvesterDaemon;
import eu.apenet.dashboard.listener.QueueDaemon;
import eu.apenet.dashboard.reindex.ReIndexAllDocumentsManager;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.services.ead3.Ead3Service;
import eu.apenet.dashboard.services.eag.xml.stream.XmlEagParser;
import eu.apenet.dashboard.services.eag.xml.stream.publish.EagSolrPublisher;
import eu.apenet.dashboard.services.opendata.OpenDataService;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.UpFile;
import java.io.IOException;

public class ManageQueueAction extends AbstractAction {

    private static final Logger LOGGER = Logger.getLogger(ManageQueueAction.class);
    private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Integer queueItemId;
    private Integer aiId;
    private String selectedAction;

    private List<String> selections;
    private List<String> checkedSelection;

    private static final String INSTITUTIONS = "numberOfInstitutions";
    private static final String EAD_FA = "numberOfFindingAids";
    private static final String EAD_HG = "numberOfHoldingsGuide";
    private static final String EAD_SG = "numberOfSourceGuide";
    private static final String EAC_CPF_UNITS = "numberOfEacCpfs";
    private static final String EAD3_UNITS = "numberOfEad3s";
    private static final String REINDEX_ON_PROGRESS = "reIndexOnProgress";
    private static final String REINDEX_ON_PROGRESS_ADDITIONAL = "reIndexOnProgressAdditional";
    /**
     *
     */
    private static final long serialVersionUID = 7015833987047809962L;

    public Integer getQueueItemId() {
        return queueItemId;
    }

    public void setQueueItemId(Integer queueItemId) {
        this.queueItemId = queueItemId;
    }

    public Integer getAiId() {
        return aiId;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

    public String getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    public List<String> getSelections() {
        return selections;
    }

    public void setSelections(List<String> selections) {
        this.selections = selections;
    }

    public List<String> getCheckedSelection() {
        return checkedSelection;
    }

    public void setCheckedSelection(List<String> checkedSelection) {
        this.checkedSelection = checkedSelection;
    }

    @Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("admin.queuemanagement.title"));
    }

    public String execute() throws Exception {
        QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
        getServletRequest().setAttribute("numberOfItemsInQueue", queueDAO.countItems());
        getServletRequest().setAttribute("firstItems", convert(queueDAO.getFirstItems()));
        getServletRequest().setAttribute("countsByArchivalInstitutions", queueDAO.countByArchivalInstitutions());
        getServletRequest().setAttribute("disabledItems", convert(queueDAO.getDisabledItems()));
        getServletRequest().setAttribute("itemsWithErrors", convert(queueDAO.getItemsWithErrors()));
        getServletRequest().setAttribute("queueActive", QueueDaemon.isActive());
        getServletRequest().setAttribute("queueStatus", QueueDaemon.getQueueStatus());
        getServletRequest().setAttribute("queueStatusCss", QueueDaemon.getQueueStatusCss());

        getServletRequest().setAttribute("queueProcessing", QueueDaemon.isQueueProcessing());
        getServletRequest().setAttribute("europeanaHarvestingStarted", EadService.isHarvestingStarted());
        getServletRequest().setAttribute("dashboardHarvestingStarted", HarvesterDaemon.isHarvesterProcessing());
        getServletRequest().setAttribute("maintenanceMode", APEnetUtilities.getDashboardConfig().isMaintenanceMode());
        getServletRequest().setAttribute("currentTime", DATE_TIME.format(new Date()));
        Date endDateTime = DAOFactory.instance().getResumptionTokenDAO().getPossibleEndDateTime();
        if (endDateTime != null) {
            getServletRequest().setAttribute("europeanaHarvestingEndTime", DATE_TIME.format(endDateTime));
        }
        if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()) {
            this.buildSelectionForReindex();
            this.countTotalNumberOfElementsToBeReindexed();
        }
        return SUCCESS;
    }

    private void buildSelectionForReindex() {
        this.selections = new ArrayList<>();

// HIDE EAD3
//        this.selections.add(XmlType.EAD_3.getName());
        this.selections.add(XmlType.EAD_FA.getName());
        this.selections.add(XmlType.EAD_HG.getName());
        this.selections.add(XmlType.EAD_SG.getName());
        this.selections.add(XmlType.EAC_CPF.getName());

        getServletRequest().setAttribute("selections", this.selections);

        getServletRequest().setAttribute(REINDEX_ON_PROGRESS, ReIndexAllDocumentsManager.getInstance().isReIndexInProgress());
    }

    private void countTotalNumberOfElementsToBeReindexed() {
        ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
        contentSearchOptions.setPublished(true);

        long institutions = DAOFactory.instance().getArchivalInstitutionDAO().countArchivalInstitutionsWithEag();

        contentSearchOptions.setContentClass(FindingAid.class);
        long FAUnits = DAOFactory.instance().getEadDAO().countEads(contentSearchOptions);
        contentSearchOptions.setContentClass(HoldingsGuide.class);
        long HGUnits = DAOFactory.instance().getEadDAO().countEads(contentSearchOptions);
        contentSearchOptions.setContentClass(SourceGuide.class);
        long SGUnits = DAOFactory.instance().getEadDAO().countEads(contentSearchOptions);
        LOGGER.info(FAUnits + ":" + HGUnits + ":" + SGUnits);

        contentSearchOptions.setContentClass(EacCpf.class);
        long eacCpfUnits = DAOFactory.instance().getEacCpfDAO().countEacCpfs(contentSearchOptions);

        contentSearchOptions.setContentClass(Ead3.class);
        long ead3Units = DAOFactory.instance().getEad3DAO().countEad3s(contentSearchOptions);

        getServletRequest().setAttribute(INSTITUTIONS, institutions);
        getServletRequest().setAttribute(EAD_FA, FAUnits);
        getServletRequest().setAttribute(EAD_HG, HGUnits);
        getServletRequest().setAttribute(EAD_SG, SGUnits);
        getServletRequest().setAttribute(EAC_CPF_UNITS, eacCpfUnits);
        getServletRequest().setAttribute(EAD3_UNITS, ead3Units);
        long total = ReIndexAllDocumentsManager.getInstance().getTotalToBeReindexed();
        long alreadyAdded = ReIndexAllDocumentsManager.getInstance().getAlreadyAdded();
        double percent = alreadyAdded * 100.0 / total;
        getServletRequest().setAttribute(REINDEX_ON_PROGRESS_ADDITIONAL, " ( " + alreadyAdded + " / " + total + " ) - " + String.format("%.2f", percent) + "%");
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
                } else if (queueItem.getArchivalInstitution() != null) {
                    Properties preferences = EadService.readProperties(queueItem.getPreferences());
                    if (preferences.containsKey(OpenDataService.ENABLE_OPEN_DATA_KEY)) {
                        boolean openData = Boolean.valueOf(preferences.getProperty(OpenDataService.ENABLE_OPEN_DATA_KEY));
                        displayItem.setEadidOrFilename(getText("admin.queuemanagement.openData") + ": " + queueItem.getArchivalInstitution().getUnprocessedSolrDocs());
                        displayItem.setAction(displayItem.getAction() + ": " + (openData ? getText("admin.queuemanagement.enable") : getText("admin.queuemanagement.disable")));
                    }
                    displayItem.setArchivalInstitution(queueItem.getArchivalInstitution().getAiname());
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

    public String manageQueueItem() throws Exception {
        if (SecurityContext.get().isAdmin()) {
            QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
            QueueItem queueItem = queueDAO.findById(queueItemId);
            if ("DELETE".equals(selectedAction)) {
                deleteQueueItem(queueItem);
            } else {
                queueItem.setErrors(null);
                if ("DISABLE".equals(selectedAction)) {
                    queueItem.setPriority(0);
                } else if ("ENABLE".equals(selectedAction)) {
                    queueItem.setPriority(1000);
                } else if ("HIGHEST".equals(selectedAction)) {
                    queueItem.setPriority(5000);
                } else if ("LOWEST".equals(selectedAction)) {
                    queueItem.setPriority(1);
                }
                queueDAO.store(queueItem);
            }
        }
        return SUCCESS;
    }

    public String deleteAllQueueItemsWithErrors() throws Exception {
        if (SecurityContext.get().isAdmin()) {
            QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
            List<QueueItem> queueItems = queueDAO.getItemsWithErrors();
            for (QueueItem queueItem : queueItems) {
                try {
                    deleteQueueItem(queueItem);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        return SUCCESS;
    }

    public String deleteAllUnusedUploadFiles() throws Exception {
        if (SecurityContext.get().isAdmin()) {
            EadService.deleteAllUnusedUploadFiles();
        }
        return SUCCESS;
    }

    public String forceSolrCommit() throws Exception {
        if (SecurityContext.get().isAdmin()) {
            try {
                SolrUtil.forceSolrCommit();
            } catch (Exception de) {
                LOGGER.error(de.getMessage(), de);
            }
        }
        return SUCCESS;
    }

    public String solrOptimize() throws Exception {
        if (SecurityContext.get().isAdmin()) {
            try {
                SolrUtil.solrOptimize();
            } catch (Exception de) {
                LOGGER.error(de.getMessage(), de);
            }
        }
        return SUCCESS;
    }

    public String republishAllEagFiles() {
        if (SecurityContext.get().isAdmin()) {
            EagSolrPublisher publisher = new EagSolrPublisher();
            try {
                publisher.deleteEverything();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsWithRepositoryCode();
            for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
                try {
                    LOGGER.info("Publish : " + archivalInstitution.getAiId() + " " + archivalInstitution.getAiname());
                    XmlEagParser.parseAndPublish(archivalInstitution);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        return SUCCESS;
    }

    public String rebuildAutosuggestion() throws SolrServerException, IOException {
        if (SecurityContext.get().isAdmin()) {
            SolrUtil.rebuildAutosuggestion();
        }
        return SUCCESS;
    }

    public String stopReindex() {
        LOGGER.info("Stop Reindex");
        if (SecurityContext.get().isAdmin()) {
            ReIndexAllDocumentsManager riManager = ReIndexAllDocumentsManager.getInstance();
            riManager.stopReindex();
        }
        return SUCCESS;
    }

    public String reindexTest() {
        LOGGER.info("Reindex test");
        if (SecurityContext.get().isAdmin()) {
            ReIndexAllDocumentsManager riManager = ReIndexAllDocumentsManager.getInstance();
            try {
                List<XmlType> types = new ArrayList<>();
//                types.add(XmlType.EAD_3);
                types.add(XmlType.EAD_FA);
                riManager.redindex(true, types);

            } catch (ProcessBusyException ex) {
                LOGGER.info("Function " + ex.getMessage());
            }
        }
        return SUCCESS;
    }

    public String reindex() {
        if (SecurityContext.get().isAdmin()) {
            List<XmlType> types = new ArrayList<>();
            for (String selectedType : checkedSelection) {
                types.add(XmlType.getType(selectedType));
            }
            ReIndexAllDocumentsManager riManager = ReIndexAllDocumentsManager.getInstance();
            try {
                riManager.redindex(false, types);

            } catch (ProcessBusyException ex) {
                LOGGER.info("Function " + ex.getMessage());
            }
        }
        return SUCCESS;
    }

    public String changeMaintenanceMode() {
        if (SecurityContext.get().isAdmin()) {
            if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()) {
                APEnetUtilities.getDashboardConfig().setMaintenanceMode(false);
                ChangeControl.logOperation(ChangeControl.MAINTENANCE_MODE_DEACTIVATE);
            } else {
                APEnetUtilities.getDashboardConfig().setMaintenanceMode(true);
                ChangeControl.logOperation(ChangeControl.MAINTENANCE_MODE_ACTIVATE);
            }
        }
        return SUCCESS;
    }

    public String startStopQueue() {
        if (SecurityContext.get().isAdmin()) {
            if (QueueDaemon.isActive()) {
                QueueDaemon.stop();
            } else {
                QueueDaemon.start();
            }
        }
        return SUCCESS;
    }

    public String manageQueueItemOfInstitution() throws Exception {
        if (SecurityContext.get().isAdmin()) {
            QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
            if ("DELETE".equals(selectedAction)) {
                List<QueueItem> queueItems = queueDAO.getItemsOfInstitution(aiId);
                for (QueueItem queueItem : queueItems) {
                    deleteQueueItem(queueItem);
                }
            } else if ("DISABLE".equals(selectedAction)) {
                queueDAO.setPriorityToQueueOfArchivalInstitution(aiId, 0);
            } else if ("ENABLE".equals(selectedAction)) {
                queueDAO.setPriorityToQueueOfArchivalInstitution(aiId, 1000);
            } else if ("HIGHEST".equals(selectedAction)) {
                queueDAO.setPriorityToQueueOfArchivalInstitution(aiId, 5000);
            } else if ("LOWEST".equals(selectedAction)) {
                queueDAO.setPriorityToQueueOfArchivalInstitution(aiId, 1);
            }
        }
        return SUCCESS;
    }

    private static void deleteQueueItem(QueueItem queueItem) throws Exception {
        if (queueItem.getAbstractContent() instanceof Ead3) {
            Ead3Service.deleteFromQueue(queueItem);
        } else {
            EadService.deleteFromQueue(queueItem);
        }
    }
}
