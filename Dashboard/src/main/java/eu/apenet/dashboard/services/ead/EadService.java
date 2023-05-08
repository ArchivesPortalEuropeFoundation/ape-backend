package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.exceptions.APEnetRuntimeException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.actions.ajax.AjaxConversionOptionsConstants;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.dashboard.manual.ingestionprofile.IngestionprofilesAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.AbstractService;
import eu.apenet.dashboard.services.ead.xml.stream.XmlEadParser;
import eu.apenet.dashboard.services.opendata.OpenDataService;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.dpt.utils.ead2edm.EdmConfig;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.IngestionprofileDefaultDaoType;
import eu.apenet.persistence.vo.IngestionprofileDefaultExistingFileAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultNoEadidAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class EadService extends AbstractService {

    protected static final Logger LOGGER = Logger.getLogger(EadService.class);
    private static final long NOT_USED_TIME = 60 * 60 * 24 * 7;

    public static boolean isHarvestingStarted() {
        return DAOFactory.instance().getResumptionTokenDAO().containsValidResumptionTokens(new Date());
    }

    public static void createPreviewHTML(XmlType xmlType, Integer id) {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (ead.getEadContent() == null) {
            try {
                long startTime = System.currentTimeMillis();
                XmlEadParser.parseEad(ead);
                LOGGER.info("Ead " + ead.getEadid() + "(" + xmlType.getName() + "): generate preview - " + (System.currentTimeMillis() - startTime) + "ms");
            } catch (Exception e) {
                throw new APEnetRuntimeException(e);
            }
        }
    }

    public static boolean hasEdmPublished(Integer eadId) {

        boolean result;

        // At the moment, only FA can have ESE files converted. Please, change
        // this behavior if another kind of EAD can have
        // ESE files converted
        EseDAO eseDao = DAOFactory.instance().getEseDAO();
        EseStateDAO eseStateDao = DAOFactory.instance().getEseStateDAO();
        EseState eseState = eseStateDao.getEseStateByState(EseState.PUBLISHED);
        List<Ese> eseList = eseDao.getEsesByFindingAidAndState(eadId, eseState);
        if (eseList == null || eseList.isEmpty()) {
            result = false;
        } else {
            result = true;
        }

        eseDao = null;
        eseStateDao = null;
        eseState = null;
        return result;
    }

    @Override
    public File download(XmlType xmlType, Integer id) {
        Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        String path = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPath();
        try {
            File file = new File(path);
            if (file.exists()) {
                return file;
            }
        } catch (Exception e) {
            LOGGER.error("Download function error, trying to open the file '" + path + "'", e);
        }
        return null;
    }

    /**
     *
     * @param xmlType
     * @param id
     * @throws java.io.IOException
     */
    @Override
    public void validate(XmlType xmlType, Integer id) throws IOException {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (ValidateTask.valid(ead)) {
            addToQueue(ead, QueueAction.VALIDATE, null);
        }

    }

    @Override
    public void convert(XmlType xmlType, Integer id, Properties properties) throws IOException {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (ConvertTask.valid(ead)) {
            addToQueue(ead, QueueAction.CONVERT, properties);
        }
    }

    @Override
    public void publish(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (PublishTask.valid(ead)) {
            addToQueue(ead, QueueAction.PUBLISH, null);
        }

    }

    public static void deleteEseEdm(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (DeleteEseEdmTask.valid(ead)) {
            addToQueue(ead, QueueAction.DELETE_ESE_EDM, null);
        }

    }

    public static void convertToEseEdm(Integer id, Properties preferences) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, FindingAid.class);
        SecurityContext.get().checkAuthorized(ead);
        if (ConvertToEseEdmTask.valid(ead)) {
            addToQueue(ead, QueueAction.CONVERT_TO_ESE_EDM, preferences);
        }
    }

    public static boolean convertValidate(XmlType xmlType, Integer id, Properties properties) throws IOException {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        addToQueue(ead, QueueAction.CONVERT_VALIDATE, properties);
        return true;
    }

    public static boolean convertValidatePublish(XmlType xmlType, Integer id, Properties properties) throws IOException {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (!ead.isPublished()) {
            addToQueue(ead, QueueAction.CONVERT_VALIDATE_PUBLISH, properties);
        }
        return true;
    }

    @Override
    public void unpublish(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (UnpublishTask.valid(ead)) {
            addToQueue(ead, QueueAction.UNPUBLISH, null);

        }
    }

    @Override
    public void delete(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        addToQueue(ead, QueueAction.DELETE, null);
    }
    
    public static void deleteByHarvester(Ead ead) throws Exception {
        addToQueue(ead, QueueAction.DELETE, null);
    }

    public static void deleteFromEuropeana(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (DeleteFromEuropeanaTask.valid(ead)) {
            addToQueue(ead, QueueAction.DELETE_FROM_EUROPEANA, null);
        }

    }

    public static void deliverToEuropeana(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (DeliverToEuropeanaTask.valid(ead)) {
            addToQueue(ead, QueueAction.DELIVER_TO_EUROPEANA, null);
        }
    }

    public static void makeStatic(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (ChangeStaticTask.valid(ead)) {
            addToQueue(ead, QueueAction.CHANGE_TO_STATIC, null);
        }
    }

    public static void makeDynamic(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (ChangeDynamicTask.valid(ead)) {
            addToQueue(ead, QueueAction.CHANGE_TO_DYNAMIC, null);
        }
    }

    public static void deleteFromQueue(XmlType xmlType, Integer id) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        JpaUtil.beginDatabaseTransaction();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (QueuingState.ERROR.equals(ead.getQueuing()) || QueuingState.READY.equals(ead.getQueuing())) {
            QueueItem queueItem = ead.getQueueItem();
            ead.setQueuing(QueuingState.NO);
            eadDAO.updateSimple(ead);
            deleteFromQueueInternal(queueItem, true);
        }
        JpaUtil.commitDatabaseTransaction();
    }

    private static void deleteFromQueueInternal(QueueItem queueItem, boolean deleteUpFile) throws IOException {
        UpFile upFile = queueItem.getUpFile();
        if (upFile != null && deleteUpFile) {
            String filename = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath() + upFile.getFilename();
            File file = new File(filename);
            File parentDir = file.getParentFile();
            ContentUtils.deleteFile(file, false);
            if (parentDir.exists() && parentDir.listFiles().length == 0) {
                ContentUtils.deleteFile(parentDir, false);
            }
            if (UploadMethod.OAI_PMH.equals(upFile.getUploadMethod().getMethod())) {
                parentDir = parentDir.getParentFile();
                if (parentDir.exists() && parentDir.listFiles().length == 0) {
                    ContentUtils.deleteFile(parentDir, false);
                }
            }

        }
        DAOFactory.instance().getQueueItemDAO().deleteSimple(queueItem);
        if (upFile != null && deleteUpFile) {
            DAOFactory.instance().getUpFileDAO().deleteSimple(upFile);
        }
    }

    public static void deleteAllUnusedUploadFiles() {
        UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();
        List<UpFile> upFiles = upFileDAO.getAllNotAssociatedFiles();
        for (UpFile upFile : upFiles) {
            String filename = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath() + upFile.getFilename();
            try {

                File file = new File(filename);
                boolean shouldDeleted = false;
                if (file.exists() && file.lastModified() < (System.currentTimeMillis() - NOT_USED_TIME)) {
                    LOGGER.info("Delete unused file(" + upFile.getId() + ") : " + filename);
                    File aiDir = file.getParentFile();
                    ContentUtils.deleteFile(file, false);
                    if (aiDir.exists() && aiDir.listFiles().length == 0) {
                        ContentUtils.deleteFile(aiDir, false);
                    }
                    shouldDeleted = true;
                } else if (!file.exists()) {
                    LOGGER.info("Delete not existing file(" + upFile.getId() + ") : " + filename);
                    shouldDeleted = true;
                }
                if (shouldDeleted) {
                    upFileDAO.delete(upFile);
                }
            } catch (Exception e) {
                LOGGER.error("Unable to delete unused file(" + upFile.getId() + ") : " + filename, e);
            }
        }

    }

    public static void fixWrongQueueStates() {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        ContentSearchOptions searchOptions = new ContentSearchOptions();
        searchOptions.setContentClass(FindingAid.class);
        searchOptions.setQueuing(QueuingState.BUSY);
        List<Ead> eads = eadDAO.getEads(searchOptions);
        for (Ead ead : eads) {
            LOGGER.info("Fix wrong queuing state for: " + ead);
            if (ead.getQueueItem() == null) {
                ead.setQueuing(QueuingState.NO);
            } else {
                ead.setQueuing(QueuingState.READY);
            }
            eadDAO.store(ead);
        }
        searchOptions.setContentClass(HoldingsGuide.class);
        eads = eadDAO.getEads(searchOptions);
        for (Ead ead : eads) {
            LOGGER.info("Fix wrong queuing state for: " + ead);
            if (ead.getQueueItem() == null) {
                ead.setQueuing(QueuingState.NO);
            } else {
                ead.setQueuing(QueuingState.READY);
            }
            eadDAO.store(ead);
        }
        searchOptions.setContentClass(SourceGuide.class);
        eads = eadDAO.getEads(searchOptions);
        for (Ead ead : eads) {
            LOGGER.info("Fix wrong queuing state for: " + ead);
            if (ead.getQueueItem() == null) {
                ead.setQueuing(QueuingState.NO);
            } else {
                ead.setQueuing(QueuingState.READY);
            }
            eadDAO.store(ead);
        }
    }

    public static void deleteFromQueue(QueueItem queueItem) throws Exception {
        SecurityContext.get().checkAuthorizedToManageQueue();
        Properties preferences = EadService.readProperties(queueItem.getPreferences());
        //delete open-data operation
        if (preferences.containsKey(OpenDataService.ENABLE_OPEN_DATA_KEY)) {
            if (queueItem.getAiId() != null ) {
                ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(queueItem.getAiId());
                archivalInstitution.setUnprocessedSolrDocs(0);
                DAOFactory.instance().getArchivalInstitutionDAO().store(archivalInstitution);
            }
            DAOFactory.instance().getQueueItemDAO().delete(queueItem);
        } else { //other
            deleteFromQueue(queueItem, true);
        }
    }

    private static void deleteFromQueue(QueueItem queueItem, boolean deleteUpFile) throws Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        JpaUtil.beginDatabaseTransaction();
        Ead ead = queueItem.getEad();
        if (ead != null) {
            ead.setQueuing(QueuingState.NO);
            eadDAO.updateSimple(ead);
        }
        deleteFromQueueInternal(queueItem, deleteUpFile);
        JpaUtil.commitDatabaseTransaction();
    }

    public static void overwrite(Ead oldEad, UpFile upFile) throws Exception {
        SecurityContext.get().checkAuthorized(oldEad);
        addToQueue(oldEad, QueueAction.OVERWRITE, null, upFile);
    }

    public static Ead create(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
        SecurityContext.get().checkAuthorized(aiId);
        Ead ead = new CreateEadTask().execute(xmlType, upFile, aiId);
        DAOFactory.instance().getUpFileDAO().delete(upFile);
        return ead;
    }

    public static void useProfileActionForHarvester(UpFile upFile, Properties preferences) throws Exception {
        QueueItem queueItem = fillQueueItem(upFile, QueueAction.USE_PROFILE, preferences);
        DAOFactory.instance().getQueueItemDAO().insertSimple(queueItem);
    }

    public static void useProfileAction(UpFile upFile, Properties preferences) throws Exception {
        SecurityContext.get().checkAuthorized(upFile.getAiId());
        QueueItem queueItem = fillQueueItem(upFile, QueueAction.USE_PROFILE, preferences);
        DAOFactory.instance().getQueueItemDAO().store(queueItem);
    }

    /**
     * Method to use the profile action associated to the EAD file.
     *
     * @param ead EAD file to process with the preferences.
     * @param preferences Preferences to process the EAD.
     *
     * @throws Exception During the process.
     */
    public static void useProfileAction(Ead ead, Properties preferences) throws Exception {
        SecurityContext.get().checkAuthorized(ead.getAiId());
        QueueItem queueItem = fillQueueItem(ead, QueueAction.USE_PROFILE, preferences);
        ead.setQueuing(QueuingState.READY);
        DAOFactory.instance().getEadDAO().store(ead);
        DAOFactory.instance().getQueueItemDAO().store(queueItem);
    }

    public static QueueAction processQueueItem(QueueItem queueItem) throws Exception {
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        QueueAction queueAction = queueItem.getAction();
        Properties preferences = null;
        if (queueItem.getPreferences() != null) {
            preferences = readProperties(queueItem.getPreferences());
        }

        if (!queueAction.isUseProfileAction()) {
            Ead ead = queueItem.getEad();
            XmlType xmlType = XmlType.getContentType(ead);
            LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + " " + ead.getEadid()
                    + "(" + xmlType.getName() + ")");

            if (queueAction.isOverwriteAction() || queueAction.isDeleteAction()) {
                boolean eadDeleted = false;
                boolean upFileDeleted = false;
                UpFile upFile = queueItem.getUpFile();
                try {

                    queueItem.setEad(null);
                    queueItem.setUpFile(null);
                    queueItemDAO.store(queueItem);

                    if (queueAction.isOverwriteAction()) {
                        Integer aiId = ead.getAiId();
                        new DeleteFromEuropeanaTask().execute(ead, preferences);
                        new DeleteEseEdmTask().execute(ead, preferences);
                        new UnpublishTask().execute(ead, preferences);
                        new DeleteTask().execute(ead, preferences);
                        eadDeleted = true;
                        new CreateEadTask().execute(xmlType, upFile, aiId);
                        DAOFactory.instance().getUpFileDAO().delete(upFile);
                        upFileDeleted = true;
                    } else if (queueAction.isDeleteAction()) {
                        new DeleteFromEuropeanaTask().execute(ead, preferences);
                        new DeleteEseEdmTask().execute(ead, preferences);
                        new UnpublishTask().execute(ead, preferences);
                        new DeleteTask().execute(ead, preferences);
                        eadDeleted = true;
                    }
                    queueItemDAO.delete(queueItem);
                } catch (Exception e) {
                    if (!eadDeleted) {
                        queueItem.setEad(ead);
                        ead.setQueuing(QueuingState.ERROR);
                        eadDAO.store(ead);
                    }
                    if (!upFileDeleted && upFile != null) {
                        queueItem.setUpFile(upFile);
                    }
                    String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
                    LOGGER.error(APEnetUtilities.generateThrowableLog(e));
                    queueItem.setErrors(new Date() + " - " + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
                    queueItem.setPriority(0);
                    queueItemDAO.store(queueItem);
                    /*
                     * throw exception when solr has problem, so the queue will stop for a while.
                     */
                    if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                        throw (Exception) e;

                    }
                }
            } else {
                try {
                    if (queueAction.isValidateAction()) {
                        new ValidateTask().execute(ead, preferences);
                    }
                    
                    if (queueAction.isConvertAction()) {
                        new ConvertTask().execute(ead, preferences);
                    }
                    
                    if (queueAction.isValidateAction()) {
                        new ValidateTask().execute(ead, preferences);
                    }
                    
                    if (queueAction.isPublishAction()) {
                        new PublishTask().execute(ead, preferences);
                    }
                    if (queueAction.isRePublishAction()) {
                        new UnpublishTask().execute(ead, preferences);
                        new PublishTask().execute(ead, preferences);
                    }
                    if (queueAction.isConvertToEseEdmAction()) {
                        new ConvertToEseEdmTask().execute(ead, preferences);
                    }
                    if (queueAction.isUnpublishAction()) {
                        new UnpublishTask().execute(ead, preferences);
                    }
                    if (queueAction.isDeleteFromEuropeanaAction()) {
                        new DeleteFromEuropeanaTask().execute(ead, preferences);
                    }
                    if (queueAction.isDeleteEseEdmAction()) {
                        new DeleteEseEdmTask().execute(ead, preferences);
                    }

                    if (queueAction.isDeliverToEuropeanaAction()) {
                        new DeliverToEuropeanaTask().execute(ead, preferences);
                    }
                    if (queueAction.isCreateStaticEadAction()) {
                        new ChangeStaticTask().execute(ead, preferences);
                    }

                    if (queueAction.isCreateDynamicEadAction()) {
                        new ChangeDynamicTask().execute(ead, preferences);
                    }
                    ead.setQueuing(QueuingState.NO);
                    eadDAO.store(ead);
                    queueItemDAO.delete(queueItem);
                } catch (Exception e) {
                    String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
                    LOGGER.error(APEnetUtilities.generateThrowableLog(e));
                    queueItem.setErrors(new Date() + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
                    ead.setQueuing(QueuingState.ERROR);
                    eadDAO.store(ead);
                    queueItem.setPriority(0);
                    queueItemDAO.store(queueItem);
                    /*
                     * throw exception when solr has problem, so the queue will stop for a while.
                     */
                    if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                        throw (Exception) e;

                    }
                }
            }
        } else//USE_PROFILE
        // Checks if the file should be processed as a new upload of as a
        // file already in the system.
        if (queueItem.getUpFile() != null) {
            processUpFileWithProfile(queueItem, preferences);
        } else {
            processEad(queueItem, queueItem.getEad(), preferences);
        }
        LOGGER.info("Process queue item finished");

        return queueAction;
    }

    /**
     * Method to process the uploaded file using the selected profile.
     *
     * @param queueItem Current item to process.
     * @param preferences Profile preferences.
     *
     * @throws Exception
     */
    private static void processUpFileWithProfile(QueueItem queueItem, Properties preferences) throws Exception {
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        IngestionprofileDefaultNoEadidAction ingestionprofileDefaultNoEadidAction = IngestionprofileDefaultNoEadidAction.getExistingFileAction(preferences.getProperty(QueueItem.NO_EADID_ACTION));
        IngestionprofileDefaultExistingFileAction ingestionprofileDefaultExistingFileAction = IngestionprofileDefaultExistingFileAction.getExistingFileAction(preferences.getProperty(QueueItem.EXIST_ACTION));
        XmlType xmlType = XmlType.getType(Integer.parseInt(preferences.getProperty(QueueItem.XML_TYPE)));

        //About EADID
        UpFile upFile = queueItem.getUpFile();
        LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + ", upFile id: " + queueItem.getUpFileId() + "(" + xmlType.getName() + ")");
        String upFilePath = upFile.getPath() + upFile.getFilename();
        String eadid = ExistingFilesChecker.extractAttributeFromXML(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFilePath, "eadheader/eadid", null, true, false).trim();
        if (StringUtils.isEmpty(eadid) || "empty".equals(eadid) || "error".equals(eadid)) {
            if (ingestionprofileDefaultNoEadidAction.isRemove()) {
                LOGGER.info("File will be removed, because it does not have eadid: " + upFilePath);
                deleteFromQueue(queueItem, true);
            } else {
                LOGGER.info("File will be processed manually later, because it does not have eadid: " + upFilePath);
                deleteFromQueue(queueItem, false);
            }
        } else {
            boolean continueTask = true;
            Ead ead;
            Ead newEad = null;
            if ((ead = doesFileExist(upFile, eadid, xmlType)) != null) {
                if (ingestionprofileDefaultExistingFileAction.isOverwrite()) {
                    boolean eadDeleted = false;
                    try {
                        queueItem.setEad(null);
                        queueItem.setUpFile(null);
                        queueItemDAO.store(queueItem);

                        Integer aiId = ead.getAiId();
                        new DeleteFromEuropeanaTask().execute(ead, preferences);
                        new DeleteEseEdmTask().execute(ead, preferences);
                        new UnpublishTask().execute(ead, preferences);
                        new DeleteTask().execute(ead, preferences);
                        eadDeleted = true;
                        newEad = new CreateEadTask().execute(xmlType, upFile, aiId);
                        DAOFactory.instance().getUpFileDAO().delete(upFile);
                    } catch (Exception e) {
                        if (!eadDeleted) {
                            queueItem.setEad(ead);
                            ead.setQueuing(QueuingState.ERROR);
                            eadDAO.store(ead);
                        }
                        if (upFile != null) {
                            queueItem.setUpFile(upFile);
                        }
                        String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
                        LOGGER.error(APEnetUtilities.generateThrowableLog(e));
                        queueItem.setErrors(new Date() + " - " + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
                        queueItem.setPriority(0);
                        queueItemDAO.store(queueItem);
                        continueTask = false;
                        /*
                         * throw exception when solr has problem, so the queue will stop for a while.
                         */
                        if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                            throw (Exception) e;

                        }
                    }
                } else if (ingestionprofileDefaultExistingFileAction.isKeep()) {
                    LOGGER.info("File will be removed, because there is already one with the same eadid: " + upFilePath);
                    deleteFromQueue(queueItem, true);
                    continueTask = false;
                } else if (ingestionprofileDefaultExistingFileAction.isAsk()) {
                    LOGGER.info("Is needed to ask the user for the action, because there is already one with the same eadid: " + upFilePath);
                    deleteFromQueue(queueItem, false);
                    continueTask = false;
                }
            } else {
                newEad = new CreateEadTask().execute(xmlType, upFile, upFile.getAiId());
                queueItem.setUpFile(null);
//                queueItemDAO.store(queueItem);
//                DAOFactory.instance().getUpFileDAO().delete(upFile);
                queueItemDAO.storeAndDeleteBatch(Arrays.asList(new Object[]{queueItem}),Arrays.asList(new Object[]{upFile}));
            }

            if (continueTask) {
                processEad(queueItem, newEad, preferences);
            }
        }
    }

    /**
     * Method to performs the actions associated to the profile over the file.
     *
     * @param queueItem Current item to process.
     * @param newEad EAD to process using the profile.
     * @param preferences Profile preferences.
     *
     * @throws Exception
     */
    private static void processEad(QueueItem queueItem, Ead newEad, Properties preferences) throws Exception {
        QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        IngestionprofileDefaultUploadAction ingestionprofileDefaultUploadAction = IngestionprofileDefaultUploadAction.getUploadAction(preferences.getProperty(QueueItem.UPLOAD_ACTION));
        XmlType xmlType = XmlType.getType(Integer.parseInt(preferences.getProperty(QueueItem.XML_TYPE)));

        newEad.setQueuing(QueuingState.BUSY);
        eadDAO.store(newEad);

        try {
            Properties conversionProperties = createConversionProperties(preferences);
            if (ingestionprofileDefaultUploadAction.isConvert()) {
                new ConvertTask().execute(newEad, conversionProperties);
            } else if (ingestionprofileDefaultUploadAction.isValidate()) {
                new ValidateTask().execute(newEad);
            } else if (ingestionprofileDefaultUploadAction.isConvertValidatePublish() || ingestionprofileDefaultUploadAction.isConvertValidatePublishEuropeana()) {
                new ValidateTask().execute(newEad);
                new ConvertTask().execute(newEad, conversionProperties);
                new ValidateTask().execute(newEad);
                new PublishTask().execute(newEad);
                if (ingestionprofileDefaultUploadAction.isConvertValidatePublishEuropeana()) {
                    Properties europeanaProperties = createEuropeanaProperties(preferences, newEad);
                    new ConvertToEseEdmTask().execute(newEad, europeanaProperties);
                    new DeliverToEuropeanaTask().execute(newEad);
                }
            }
            newEad.setQueuing(QueuingState.NO);
            eadDAO.store(newEad);
            queueItemDAO.delete(queueItem);
        } catch (Exception e) {
            newEad.setQueuing(QueuingState.ERROR);
            eadDAO.store(newEad);

            String err = "eadid: " + newEad.getEadid() + " - id: " + newEad.getId() + " - type: " + xmlType.getName();
            LOGGER.error(APEnetUtilities.generateThrowableLog(e));
            queueItem.setErrors(new Date() + " - " + err + ". Error: " + APEnetUtilities.generateThrowableLog(e));
            queueItem.setPriority(0);
            queueItem.setEad(newEad);
            queueItemDAO.store(queueItem);
            /*
             * throw exception when solr has problem, so the queue will stop for a while.
             */
            if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                throw (Exception) e;

            }
        }
    }

    private static Ead doesFileExist(UpFile upFile, String eadid, XmlType xmlType) {
        return DAOFactory.instance().getEadDAO().getEadByEadid(xmlType.getEadClazz(), upFile.getAiId(), eadid);
    }

    private static void addToQueue(Ead ead, QueueAction queueAction, Properties preferences, UpFile upFile)
            throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        ead.setQueuing(QueuingState.READY);
        eadDAO.store(ead);
        QueueItem queueItem = fillQueueItem(ead, queueAction, preferences);
        queueItem.setUpFile(upFile);
        indexqueueDao.store(queueItem);
    }

    private static void addToQueue(Ead ead, QueueAction queueAction, Properties preferences) throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        ead.setQueuing(QueuingState.READY);
        eadDAO.store(ead);
        QueueItem queueItem = fillQueueItem(ead, queueAction, preferences);
        indexqueueDao.store(queueItem);
    }

    public static void addBatchToQueue(ContentSearchOptions eadSearchOptions, QueueAction queueAction,
            Properties preferences) throws IOException {
        SecurityContext.get().checkAuthorized(eadSearchOptions.getArchivalInstitionId());
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        eadSearchOptions.setPageSize(0);
        if (QueueAction.CONVERT.equals(queueAction)) {
            eadSearchOptions.setConverted(false);
        } else if (QueueAction.VALIDATE.equals(queueAction)) {
            eadSearchOptions.setValidated(ValidatedState.NOT_VALIDATED);
        } else if (QueueAction.PUBLISH.equals(queueAction)) {
            eadSearchOptions.setValidated(ValidatedState.VALIDATED);
            eadSearchOptions.setPublished(false);
        } else if (QueueAction.CONVERT_VALIDATE_PUBLISH.equals(queueAction)) {
            eadSearchOptions.setPublished(false);
        } else if (QueueAction.UNPUBLISH.equals(queueAction)) {
            eadSearchOptions.setPublished(true);
        } else if (QueueAction.CONVERT_TO_ESE_EDM.equals(queueAction)) {
            eadSearchOptions.setEuropeana(EuropeanaState.NOT_CONVERTED);
            eadSearchOptions.setValidated(ValidatedState.VALIDATED);
        } else if (QueueAction.DELIVER_TO_EUROPEANA.equals(queueAction)) {
            eadSearchOptions.setEuropeana(EuropeanaState.CONVERTED);

        } else if (QueueAction.DELETE_FROM_EUROPEANA.equals(queueAction)) {
            eadSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
        } else if (QueueAction.DELETE_ESE_EDM.equals(queueAction)) {
            eadSearchOptions.setEuropeana(EuropeanaState.CONVERTED);
        } else if (QueueAction.CHANGE_TO_STATIC.equals(queueAction)) {
            eadSearchOptions.setDynamic(true);
        } else if (QueueAction.CHANGE_TO_DYNAMIC.equals(queueAction)) {
            eadSearchOptions.setDynamic(false);
        }
        eadSearchOptions.setQueuing(QueuingState.NO);
        JpaUtil.beginDatabaseTransaction();
        List<Ead> eads = eadDAO.getEads(eadSearchOptions);
        int size = 0;
        while ((size = eads.size()) > 0) {
            Ead ead = eads.get(size - 1);
            if (validState(ead, preferences, queueAction)) {
                QueueItem queueItem;
                if (QueueAction.CONVERT_TO_ESE_EDM.equals(queueAction)) {
                    Properties properties = preferences;
                    String oaiIdentifier = ead.getArchivalInstitution().getRepositorycode()
                            + APEnetUtilities.FILESEPARATOR + "fa"
                            + APEnetUtilities.FILESEPARATOR + ead.getEadid();
                    properties.put("edm_identifier", oaiIdentifier);
                    properties.put("repository_code", ead.getArchivalInstitution().getRepositorycode());
                    properties.put("outputBaseDirectory", EdmFileUtils.getOutputEDMDirPath(APEnetUtilities.getConfig().getRepoDirPath(),
                            ead.getArchivalInstitution().getCountry().getIsoname(), ead.getArchivalInstitution().getAiId()));
                    queueItem = fillQueueItem(ead, queueAction, properties);
                } else {
                    queueItem = fillQueueItem(ead, queueAction, preferences);
                }
                ead.setQueuing(QueuingState.READY);
                eadDAO.updateSimple(ead);
                indexqueueDao.updateSimple(queueItem);
            }
            eads.remove(size - 1);
        }
        JpaUtil.commitDatabaseTransaction();
    }

    private static boolean validState(Ead ead, Properties preferences, QueueAction queueAction) {
        boolean state = !queueAction.isUseProfileAction();
        if (!state) {
            String property = preferences.getProperty(QueueItem.UPLOAD_ACTION);
            if (ead.getEadClass().equals(FindingAid.class)) { //FINDING AID
                //each condition returns true if the state and the profile future action are rights
                state = (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT.getId())) && !ead.isConverted())
                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.VALIDATE.getId())) && !ead.getValidated().equals(ValidatedState.VALIDATED))
                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH.getId())) && !ead.isPublished())
                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH_EUROPEANA.getId()))
                        && ((!((FindingAid) ead).getEuropeana().equals(EuropeanaState.DELIVERED) || ((FindingAid) ead).getEuropeana().equals(EuropeanaState.NO_EUROPEANA_CANDIDATE))
                        || !ead.isPublished()));
            } else if (ead.getEadClass().equals(HoldingsGuide.class) || ead.getEadClass().equals(SourceGuide.class)) {
                //HOLDINGS GUIDE or SOURCE GUIDE
                //each condition returns true if the state and the profile future action are rights
                state = (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT.getId())) && !ead.isConverted())
                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.VALIDATE.getId())) && !ead.getValidated().equals(ValidatedState.VALIDATED))
                        || (property.equals(Integer.toString(IngestionprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH.getId())) && !ead.isPublished());
            }
        }
        return state;
    }

    public static void updateEverything(ContentSearchOptions eadSearchOptions, QueueAction queueAction) throws IOException {
        QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        long itemsLeft = eadDAO.countEads(eadSearchOptions);
        LOGGER.info(itemsLeft + " " + eadSearchOptions.getContentClass().getSimpleName() + " left to add to queue");
        while (itemsLeft > 0) {
            JpaUtil.beginDatabaseTransaction();
            List<Ead> eads = eadDAO.getEads(eadSearchOptions);
            int size = 0;
            while ((size = eads.size()) > 0) {
                Ead ead = eads.get(size - 1);
                QueueItem queueItem = fillQueueItem(ead, queueAction, null, 1);
                ead.setQueuing(QueuingState.READY);
                if (queueAction.isPublishAction()) {
                    ead.setPublished(false);
                }
                eadDAO.updateSimple(ead);
                eads.remove(size - 1);
                indexqueueDao.updateSimple(queueItem);
            }
            JpaUtil.commitDatabaseTransaction();
            itemsLeft = eadDAO.countEads(eadSearchOptions);
            LOGGER.info(itemsLeft + " " + eadSearchOptions.getContentClass().getSimpleName() + " left to add to queue");

        }

    }

    public static void deleteBatchFromQueue(List<Integer> ids, Integer aiId, XmlType xmlType) throws IOException {
        ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
        eadSearchOptions.setPageSize(0);
        eadSearchOptions.setContentClass(xmlType.getClazz());
        eadSearchOptions.setArchivalInstitionId(aiId);
        if (ids != null && ids.size() > 0) {
            eadSearchOptions.setIds(ids);
        }
        deleteBatchFromQueue(eadSearchOptions);

    }

    public static void deleteBatchFromQueue(ContentSearchOptions eadSearchOptions) throws IOException {
        SecurityContext.get().checkAuthorized(eadSearchOptions.getArchivalInstitionId());
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        eadSearchOptions.setPageSize(0);
        List<QueuingState> queueStates = new ArrayList<QueuingState>();
        queueStates.add(QueuingState.READY);
        queueStates.add(QueuingState.ERROR);
        eadSearchOptions.setQueuing(queueStates);
        JpaUtil.beginDatabaseTransaction();
        List<Ead> eads = eadDAO.getEads(eadSearchOptions);
        int size = 0;
        while ((size = eads.size()) > 0) {
            Ead ead = eads.get(size - 1);
            QueueItem queueItem = ead.getQueueItem();
            ead.setQueuing(QueuingState.NO);
            eadDAO.updateSimple(ead);
            if (queueItem != null) {
                deleteFromQueueInternal(queueItem, true);
            }
            eads.remove(size - 1);
        }
        JpaUtil.commitDatabaseTransaction();
    }

    public static void addBatchToQueue(List<Integer> ids, Integer aiId, XmlType xmlType, QueueAction queueAction,
            Properties preferences) throws IOException {
        ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
        eadSearchOptions.setPageSize(0);
        eadSearchOptions.setContentClass(xmlType.getClazz());
        eadSearchOptions.setArchivalInstitionId(aiId);
        if (ids != null && ids.size() > 0) {
            eadSearchOptions.setIds(ids);
        }
        addBatchToQueue(eadSearchOptions, queueAction, preferences);

    }

    public static Properties readProperties(String string) throws IOException {
        StringReader stringReader = new StringReader(string);
        Properties properties = new Properties();
        properties.load(stringReader);
        stringReader.close();
        return properties;
    }

    private static String writeProperties(Properties properties) throws IOException {
        StringWriter stringWriter = new StringWriter();
        properties.store(stringWriter, "");
        String result = stringWriter.toString();
        stringWriter.flush();
        stringWriter.close();
        return result;
    }

    private static QueueItem fillQueueItem(Ead ead, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(ead, queueAction, preferences, 1000);
    }

    private static QueueItem fillQueueItem(Ead ead, QueueAction queueAction, Properties preferences, int basePriority)
            throws IOException {
        QueueItem queueItem = ead.getQueueItem();
        int priority = basePriority;
        if (queueItem == null) {
            queueItem = new QueueItem();
            if (ead instanceof FindingAid) {
                queueItem.setFindingAid((FindingAid) ead);
            } else if (ead instanceof HoldingsGuide) {
                queueItem.setHoldingsGuide((HoldingsGuide) ead);
                priority += 100;
            } else if (ead instanceof SourceGuide) {
                queueItem.setSourceGuide((SourceGuide) ead);
                priority += 50;
            }
            queueItem.setAiId(ead.getAiId());
        } else if (ead instanceof HoldingsGuide) {
            priority += 100;
        } else if (ead instanceof SourceGuide) {
            priority += 50;
        }
        queueItem.setQueueDate(new Date());
        queueItem.setAction(queueAction);
        if (preferences != null) {
            queueItem.setPreferences(writeProperties(preferences));
        }
        if (queueAction.isDeleteAction() || queueAction.isUnpublishAction() || queueAction.isDeleteFromEuropeanaAction() || queueAction.isDeleteEseEdmAction()) {
            priority += 150;
        }
        queueItem.setPriority(priority);
        return queueItem;
    }

    private static QueueItem fillQueueItem(UpFile upFile, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(upFile, queueAction, preferences, 1000);
    }

    private static QueueItem fillQueueItem(UpFile upFile, QueueAction queueAction, Properties preferences, int basePriority) throws IOException {
        QueueItem queueItem = new QueueItem();
        queueItem.setQueueDate(new Date());
        queueItem.setAction(queueAction);
        if (preferences != null) {
            queueItem.setPreferences(writeProperties(preferences));
        }

        queueItem.setUpFile(upFile);

        queueItem.setPriority(basePriority);
        queueItem.setAiId(upFile.getAiId());
        return queueItem;
    }

    /**
     * Method to recover only the properties related to the conversion options
     * from the ingestion profile properties.
     *
     * @param preferences All the properties set in the ingestion profile.
     *
     * @return Conversion properties from the ingestion profile.
     */
    private static Properties createConversionProperties(Properties preferences) {
        Properties conversionProperties = new Properties();

        // Properties related to the DAO type.
        IngestionprofileDefaultDaoType ingestionprofileDefaultDaoType = IngestionprofileDefaultDaoType.getDaoType(preferences.getProperty(QueueItem.DAO_TYPE));
        if (ingestionprofileDefaultDaoType == null) {
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT, "UNSPECIFIED");
        } else {
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT, ingestionprofileDefaultDaoType.getDaoText());
        }

        Boolean daoTypeCheck = "true".equals(preferences.getProperty(QueueItem.DAO_TYPE_CHECK));
        conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_USE_EXISTING, Boolean.toString(daoTypeCheck));

        // Properties related to the rights statement for digital objects.
        String rightsDigitalObjects = preferences.getProperty(QueueItem.RIGHTS_OF_DIGITAL_OBJECTS);
        if (StringUtils.isNotBlank(rightsDigitalObjects)
                && !rightsDigitalObjects.equalsIgnoreCase("null")) {
            // URL.
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL, preferences.getProperty(QueueItem.RIGHTS_OF_DIGITAL_OBJECTS));
            // Text.
            if (preferences.getProperty(QueueItem.RIGHTS_OF_DIGITAL_OBJECTS_TEXT) != null) {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL_TEXT, preferences.getProperty(QueueItem.RIGHTS_OF_DIGITAL_OBJECTS_TEXT));
            } else {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL_TEXT, "");
            }
            // Description.
            if (preferences.getProperty(QueueItem.RIGHTS_OF_DIGITAL_DESCRIPTION) != null) {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_DESCRIPTION, preferences.getProperty(QueueItem.RIGHTS_OF_DIGITAL_DESCRIPTION));
            } else {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_DESCRIPTION, "");
            }
            // Rights holder.
            if (preferences.getProperty(QueueItem.RIGHTS_OF_DIGITAL_HOLDER) != null) {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_HOLDER, preferences.getProperty(QueueItem.RIGHTS_OF_DIGITAL_HOLDER));
            } else {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_HOLDER, "");
            }
        } else {
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL, "");
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL_TEXT, "");
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_DESCRIPTION, "");
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_HOLDER, "");
        }

        // Properties related to the rights statement for EAD data.
        String rightsEADData = preferences.getProperty(QueueItem.RIGHTS_OF_EAD_DATA);
        if (StringUtils.isNotBlank(rightsEADData)
                && !rightsEADData.equalsIgnoreCase("null")) {
            // URL.
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD, preferences.getProperty(QueueItem.RIGHTS_OF_EAD_DATA));
            // Text.
            if (preferences.getProperty(QueueItem.RIGHTS_OF_EAD_DATA_TEXT) != null) {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD_TEXT, preferences.getProperty(QueueItem.RIGHTS_OF_EAD_DATA_TEXT));
            } else {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD_TEXT, "");
            }
            // Description.
            if (preferences.getProperty(QueueItem.RIGHTS_OF_EAD_DESCRIPTION) != null) {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_DESCRIPTION, preferences.getProperty(QueueItem.RIGHTS_OF_EAD_DESCRIPTION));
            } else {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_DESCRIPTION, "");
            }
            // Rights holder.
            if (preferences.getProperty(QueueItem.RIGHTS_OF_EAD_HOLDER) != null) {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_HOLDER, preferences.getProperty(QueueItem.RIGHTS_OF_EAD_HOLDER));
            } else {
                conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_HOLDER, "");
            }
        } else {
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD, "");
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD_TEXT, "");
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_DESCRIPTION, "");
            conversionProperties.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_HOLDER, "");
        }

        if (preferences.getProperty(QueueItem.XSL_FILE) != null) {
            conversionProperties.put(QueueItem.XSL_FILE, preferences.getProperty(QueueItem.XSL_FILE));
        }

        return conversionProperties;
    }

    private static Properties createEuropeanaProperties(Properties preferences, Ead ead) {
        EdmConfig config = new EdmConfig();
        config.setDataProvider(preferences.getProperty(QueueItem.DATA_PROVIDER));
        config.setUseExistingRepository("true".equals(preferences.getProperty(QueueItem.DATA_PROVIDER_CHECK)));
        config.setProvider("Archives Portal Europe");
        config.setType(IngestionprofileDefaultDaoType.getDaoType(preferences.getProperty(QueueItem.EUROPEANA_DAO_TYPE)).getDaoText());
        config.setUseExistingDaoRole("true".equals(preferences.getProperty(QueueItem.EUROPEANA_DAO_TYPE_CHECK)));
        config.setLanguageMaterial(preferences.getProperty(QueueItem.LANGUAGES_MATERIAL));
        config.setUseExistingLanguageMaterial("true".equals(preferences.getProperty(QueueItem.LANGUAGES_MATERIAL_CHECK)));
        config.setLanguageDescriptionSameAsLanguageMaterial("true".equals(preferences.getProperty(QueueItem.LANGUAGE_DESCRIPTION_SAME_AS_MATERIAL_CHECK)));
        config.setLanguageDescription(preferences.getProperty(QueueItem.LANGUAGE_DESCRIPTION));
        config.setUseExistingLanguageDescription("true".equals(preferences.getProperty(QueueItem.LANGUAGE_DESCRIPTION_CHECK)));
        config.setUseExistingRightsInfo("true".equals(preferences.getProperty(QueueItem.LICENSE_CHECK)));
        if ("europeana".equals(preferences.getProperty(QueueItem.LICENSE))) {
            config.setRights(preferences.getProperty(QueueItem.LICENSE_DETAILS));
        } else if ("cc0".equals(preferences.getProperty(QueueItem.LICENSE))) {
            config.setRights("http://creativecommons.org/publicdomain/zero/1.0/");
        } else if ("cpdm".equals(preferences.getProperty(QueueItem.LICENSE))) {
            config.setRights("http://creativecommons.org/publicdomain/mark/1.0/");
        } else {
            config.setRights(preferences.getProperty(QueueItem.LICENSE_DETAILS));
        }
        config.setRightsAdditionalInformation(preferences.getProperty(QueueItem.LICENSE_ADD_INFO));
        String sourceOfIdentifiers = preferences.getProperty(QueueItem.SOURCE_OF_IDENTIFIERS);
        if (StringUtils.isNotBlank(sourceOfIdentifiers)) {
            config.setIdSource(sourceOfIdentifiers);
        } else {
            config.setIdSource(IngestionprofilesAction.OPTION_UNITID);
        }

        String oaiIdentifier = ead.getArchivalInstitution().getRepositorycode()
                + APEnetUtilities.FILESEPARATOR + "fa"
                + APEnetUtilities.FILESEPARATOR + ead.getEadid();
        config.setEdmIdentifier(oaiIdentifier);
        //prefixUrl, repositoryCode and xmlTypeName used for EDM element id generation;
        //repositoryCode is taken from the tool while the other two have fixed values.
        config.setHost(PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN_MODX));
        config.setRepositoryCode(ead.getArchivalInstitution().getRepositorycode());
        config.setXmlTypeName("fa");
        config.setOutputBaseDirectory(EdmFileUtils.getOutputEDMDirPath(APEnetUtilities.getConfig().getRepoDirPath(),
                            ead.getArchivalInstitution().getCountry().getIsoname(), ead.getArchivalInstitution().getAiId()));
                        return config.getProperties();
    }
}
