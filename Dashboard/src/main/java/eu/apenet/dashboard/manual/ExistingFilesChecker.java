package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ctc.wstx.exc.WstxParsingException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.services.ead3.Ead3Service;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dpt.utils.util.XmlChecker;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.UpFile;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Eloy García Date: Sep 23d, 2010
 */
/**
 * This class is in charge of checking if the files uploaded are already stored
 * in APE and allowing the user to perform several actions if it happens: remove
 * the old files and store the new ones.
 */
public class ExistingFilesChecker {
    
    public final static String STATUS_EMPTY = "empty";
    public final static String STATUS_EADID_TOO_LONG = "eadid too long";
    public final static String STATUS_EXISTS = "exists";
    public final static String STATUS_ERROR = "error";
    public final static String STATUS_NO_EXIST = "no exists";
    public final static String STATUS_BLOCKED = "blocked";

    // Attributes
    private static final Logger LOG = Logger.getLogger(ExistingFilesChecker.class);
    private Integer archivalInstitutionId;
    private String uploadedFilesPath;
    private String xslPath;
    private String repoPath;
    private UpFileDAO upFileDao;
    private String archivalInstitutionCountry;
    
    private String additionalErrors = "";

    // Getters and Setters
    // Constructor
    /**
     * Constructor with parameters.
     *
     * @param archivalInstitutionId {@link Integer} Identifier of
     * archivalInstitution in database.
     */
    public ExistingFilesChecker(Integer archivalInstitutionId) {
        
        this.upFileDao = DAOFactory.instance().getUpFileDAO();
        this.archivalInstitutionId = archivalInstitutionId;

        // Getting Archival Institution country
        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        this.archivalInstitutionCountry = archivalInstitutionDao.findById(this.archivalInstitutionId).getCountry().getIsoname();
        this.uploadedFilesPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath();
        this.repoPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR;
        this.xslPath = this.repoPath + this.archivalInstitutionCountry + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionId.toString() + APEnetUtilities.FILESEPARATOR + "XSL" + APEnetUtilities.FILESEPARATOR;
    }
    
    public String getAdditionalErrors() {
        return additionalErrors;
    }

    // This method retrieves all the files recently uploaded via FTP, HTTP or
    // OAI-PMH for a partner
    /**
     * Retrieves all the files recently uploaded via FTP, HTTP or OAI-PMH for a
     * partner.
     *
     * @param existingNewXmlFilesUploaded
     * {@link List}{@code <}{@link FileUnit}{@code >} List of files xml to
     * uploaded.
     * @param existingNewXslFilesUploaded
     * {@link List}{@code <}{@link FileUnit}{@code >} List of files xsl to
     * uploaded.
     * @throws WstxParsingException
     *
     * @see eu.apenet.persistence.dao.UpFileDAO
     * @see eu.apenet.persistence.vo.UpFile
     */
    public void retrieveUploadedFiles(List<FileUnit> existingNewXmlFilesUploaded,
            List<FileUnit> existingNewXslFilesUploaded) throws WstxParsingException {
        
        String fileType = "";
        // Retrieving all XML files uploaded
        List<UpFile> listXml = upFileDao.getNewUpFiles(this.archivalInstitutionId, FileType.XML);
        for (UpFile aListXml : listXml) {
            FileUnit fileUnit = new FileUnit();
            fileUnit.setFileId(aListXml.getId());
            fileUnit.setFileName(aListXml.getFilename());
            fileUnit.setFilePath(aListXml.getPath());
            fileUnit.setFileType(aListXml.getFileType());
            File file = new File(this.uploadedFilesPath + aListXml.getPath() + aListXml.getFilename());
            LOG.info(file.getAbsolutePath());
            if (file.exists()) {
                //It is necessary to check the type of the uploaded XML file in order to determine further actions to be executed
                try {
                    String tmpFileFullPath = this.uploadedFilesPath + aListXml.getPath() + aListXml.getFilename();
                    
                    if (isElementContent(this.uploadedFilesPath + aListXml.getPath() + aListXml.getFilename(), "eac-cpf")) { //we can upload EAC-CPF file
                        fileType = "eac-cpf";
                    } else if (isElementContent(tmpFileFullPath, "eadheader")) {
                        fileType = extractAttributeFromXML(this.uploadedFilesPath + aListXml.getPath() + aListXml.getFilename(), "archdesc", "type", true, false);
                    } else if (isElementContent(tmpFileFullPath, "control")) {
                        fileType = "ead3";
                    }
                } catch (WstxParsingException e) {
                    //We get the exception just after - so nothing to do here.
                }
                if (fileType.equals("inventory")) {
                    fileType = XmlType.EAD_FA.getName();
                    fileUnit.setEadTypeId(XmlType.EAD_FA.getIdentifier());
                } else if (fileType.equals("holdings_guide")) {
                    fileType = XmlType.EAD_HG.getName();
                    fileUnit.setEadTypeId(XmlType.EAD_HG.getIdentifier());
                } else if (fileType.equals("eac-cpf")) {
                    fileType = XmlType.EAC_CPF.getName();
                    fileUnit.setEadTypeId(XmlType.EAC_CPF.getIdentifier());
                } else if (fileType.equals("ead3")) {
                    fileType = "EAD3";
                    fileUnit.setEadTypeId(4);
                } else {
                    //The XML is not an APEnet EAD
                    fileType = "Undefined";
                }
                
                fileUnit.setEadType(fileType);
                fileUnit.setEadid("");
                fileUnit.setPermId(null);
                existingNewXmlFilesUploaded.add(fileUnit);
            }
        }

        // Retrieving all XSL files uploaded
        List<UpFile> listXsl = upFileDao.getNewUpFiles(this.archivalInstitutionId, FileType.XSL);
        for (UpFile aListXsl : listXsl) {
            FileUnit fileUnit = new FileUnit();
            fileUnit.setFileId(aListXsl.getId());
            fileUnit.setFileName(aListXsl.getFilename());
            fileUnit.setFilePath(aListXsl.getPath());
            fileUnit.setFileType(aListXsl.getFileType());
            fileUnit.setEadType("");
            fileUnit.setEadid("");
            fileUnit.setPermId(null);
            existingNewXslFilesUploaded.add(fileUnit);
        }
        
    }

    /**
     * <p>
     * Checks if the file to upload is an EAC-CPF file.
     * <p>
     * To do this, reads the file with StAX and searchs the element
     * <code>&lt;eac-cpf&gt;</code>.
     *
     * @param uploadedFilesPath String The path file where is the file.
     * @param target String The type of the file, in this case "eac-cpf".
     * @return boolean If it is an eac-cpf file or not.
     * @see org.codehaus.stax2.XMLInputFactory2
     * @see org.codehaus.stax2.XMLStreamReader2
     * @see javax.xml.stream.XMLInputFactory
     *
     */
    public static boolean isElementContent(String uploadedFilesPath, String target) {
        // Check if the file to upload is an EAC-CPF file
        boolean found = false;
        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();
        XMLStreamReader2 r = null;
        try {
            r = (XMLStreamReader2) xmlif.createXMLStreamReader(new FileReader(uploadedFilesPath));
            while (!found && r.hasNext()) {
                Integer event = r.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String name = r.getLocalName();
                    found = (name != null && name.equals(target));
                }
            }
        } catch (FileNotFoundException e) {
            LOG.error("The path: " + uploadedFilesPath + " is not found", e);
        } catch (XMLStreamException e) {
            LOG.error("The file: " + uploadedFilesPath + " has an invalid structure", e.getCause());
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (XMLStreamException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return found;
    }

    // This method checks if the file is already stored in the Dashboard
    // It returns "exists" if it exists, "no exists" if it doesn't exist or
    // "error" in other case
    // This method fills fileUnit with eadid and permId if it is needed
    // If the file doesn't exist, then it will store it in the System (file
    // system and database)
    /**
     * <p>
     * Checks if the file is already stored in the Dashboard.
     * <p>
     * Fills fileUnit with eadid and permId if it is needed.
     * <p>
     * If the file doesn't exist, then it will store in the System (file system
     * and database).
     *
     * @param fileUnit {@link FileUnit} The file to check.
     * @param xmlType {@link XmlType} The type of the xml file.
     * @return String
     * <ul>
     * <li>{@link ExistingFilesChecker#STATUS_ERROR} Occurs an error with the
     * transaction in the database.
     * <li>{@link ExistingFilesChecker#STATUS_EMPTY} The
     * <code>&lt;eadid&gt;</code> is empty.
     * <li>{@link ExistingFilesChecker#STATUS_EXISTS} The
     * <code>&lt;eadid&gt;</code> already exists in the database.
     * <li>{@link ExistingFilesChecker#STATUS_BLOCKED} The EAD is a FA, exists,
     * has ESE files published and Europeana is performing a Harvesting process.
     * <li>{@link ExistingFilesChecker#STATUS_NO_EXIST} The
     * <code>&lt;eadid&gt;</code> doesn't exist in the database.
     * </ul>
     * @see XmlChecker#isXmlParseable(File)
     * @see EadService#create(XmlType, UpFile, Integer)
     * @see EadService#isHarvestingStarted()
     * @see EadService#hasEdmPublished(Integer)
     */
    public String checkFile(FileUnit fileUnit, XmlType xmlType) {
        
        Boolean dataBaseCommitError = false;
        additionalErrors = "";
        String result = "no exists";
        
        LOG.info("Checking file: " + fileUnit.getFileName());
        
        if (fileUnit.getFileType().equals("xsl")) {
            // The file has XSL format
            File file = new File(this.xslPath + fileUnit.getFileName());
            
            if (file.exists()) {
                LOG.info("The file " + fileUnit.getFileName() + " is an XSL file and already exists");
                result = "exists";
            } else {
                // The xsl file doesn't exist
                // It is necessary to move the file to /mnt/repo/
                // and remove it from /mnt/tmp/up/ folder
                // It is necessary to remove the entry from up_file table
                LOG.info("The file " + fileUnit.getFileName() + " is an XSL file and it will be stored in the repository");
                
                try {
                    
                    this.deleteFileFromDDBB(fileUnit.getFileId());
                    
                } catch (Exception e) {
                    dataBaseCommitError = true;
                    
                }
                
                if (!dataBaseCommitError) {
                    try {
                        insertFileToTempFiles(this.uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), this.repoPath
                                + this.archivalInstitutionCountry + APEnetUtilities.FILESEPARATOR
                                + this.archivalInstitutionId + APEnetUtilities.FILESEPARATOR + "XSL"
                                + APEnetUtilities.FILESEPARATOR + fileUnit.getFileName(), fileUnit.getFilePath());
                    } catch (APEnetException e) {
                        LOG.error("The file " + fileUnit.getFileName() + " could not be removed from the up repository or stored in the temporal repository");
                    }
                }
            }
            
        } else if (xmlType == XmlType.EAC_CPF) {
            LOG.info("We try to insert an EAC-CPF file");
            result = insertEacCpfFile(fileUnit, xmlType);
            
        } else if (xmlType == XmlType.EAD_3) {
            result = this.insertEad3File(fileUnit, xmlType);
        } else {
            // The file has XML format
            String eadid = "";
            try {
                eadid = extractAttributeFromXML(this.uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "eadheader/eadid", null, true, false).trim();
            } catch (WstxParsingException e) {
                LOG.error("File was not correct XML, cause: " + e.getMessage());
                additionalErrors += e.getMessage();
            }
            String err;
            if ((err = XmlChecker.isXmlParseable(new File(this.uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName()))) != null) {
                eadid = "error";
                LOG.error("File was not correct XML.");
                additionalErrors += "File was not correct XML. Impossible to parse it. Please check if file is XML. Error: " + err;
            }
            
            boolean isConverted;
            try {
                isConverted = Boolean.valueOf(extractAttributeFromXML(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "eadheader/revisiondesc/change/item", null, false, false));
            } catch (Exception e) {
                if (e instanceof WstxParsingException) {
                    eadid = "error";
                    additionalErrors = e.getMessage();
                }
                LOG.info("The file " + fileUnit.getFileName() + " was tried to be converted but failed");
                isConverted = false;
            }
            if (xmlType == XmlType.EAD_SG || xmlType == XmlType.EAD_FA || xmlType == XmlType.EAD_HG) {
                if (eadid.equals(STATUS_EMPTY)) { //eadid is empty
                    fileUnit.setEadType(xmlType.getName());
                    result = STATUS_EMPTY;
                } else if (StringUtils.length(eadid) > 255) {
                    fileUnit.setEadType(xmlType.getName());
                    result = STATUS_EADID_TOO_LONG;
                } else if (eadid.equals(STATUS_ERROR) || StringUtils.isBlank(eadid)) { //No eadid or several eadid
                    LOG.info("The " + xmlType.getName() + " " + fileUnit.getFileName() + " doesn't have a proper format: it doesn't have eadid or it has several");
                    try {
                        deleteFileFromDDBB(fileUnit.getFileId()); // It is necessary to remove the entry from up_file table
                    } catch (Exception e) {
                        dataBaseCommitError = true;
                    }
                    
                    if (!dataBaseCommitError) {
                        try {
                            File file = new File(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName());
                            if (file.exists()) {
                                //Windows file lock workaround; uncomment if necessary
                                //System.gc();
                                //Thread.sleep(2000);
                                FileUtils.forceDelete(file);
                            }
                            
                            File uploadDir = new File(uploadedFilesPath + fileUnit.getFilePath());
                            if (uploadDir.listFiles().length == 0) // There aren't any file in the directory, so it should be removed
                            {
                                FileUtils.forceDelete(uploadDir);
                            }
                        } catch (IOException ex) {
                            LOG.error("The file " + fileUnit.getFileName() + " could not be removed: " + ex.getMessage(), ex);
                            // belonging to Windows file lock workaround (see above)
//                        } catch (InterruptedException ex) {
//                            LOG.error(ex);
                        }
                    }
                    result = STATUS_ERROR;
                } else {
                    // It is necessary to check if the eadid is already in the Table for the current Archival Institution
                    Integer identifier;
                    if ((identifier = DAOFactory.instance().getEadDAO().isEadidUsed(eadid, archivalInstitutionId, xmlType.getEadClazz())) != null) {
                        // The EADID already exists
                        LOG.warn("Eadid '" + eadid + "' is already existing with id '" + identifier + "' in the table of '" + xmlType.getName() + "'");
                        fileUnit.setEadid(eadid);
                        fileUnit.setPermId(identifier.intValue());
                        result = STATUS_EXISTS;
                        if (xmlType == XmlType.EAD_FA && EadService.isHarvestingStarted() && EadService.hasEdmPublished(fileUnit.getPermId())) {
                            // The EAD is a FA, exists, has ESE files published and Europeana is performing a Harvesting process
                            result = STATUS_BLOCKED;
                        }
                    } else {
                        
                        try {
                            EadService.create(xmlType, upFileDao.findById(fileUnit.getFileId()), archivalInstitutionId);
                        } catch (Exception e) {
                            LOG.error("The " + xmlType.getName() + " which eadid is " + eadid + " could not be stored in the table [Database Rollback]. Error:" + e.getMessage(), e);
                            dataBaseCommitError = true;
                        } finally {
                        }
                        result = STATUS_NO_EXIST;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Inserts an EAC-CPF file in the dashboard.
     *
     * @param fileUnit {@link FileUnit} The file to insert in the dashboard.
     * @param xmlType {@link XmlType} The type of xml file.
     * @return String
     * <ul>
     * <li>{@link ExistingFilesChecker#STATUS_EMPTY} The identifier of the
     * eac-cpf file is empty.
     * <li>{@link ExistingFilesChecke#STATUS_EXISTS} Exists in the system.
     * <li>{@link ExistingFilesChecke#STATUS_NO_EXIST} No exists in the system.
     * <li>{@link ExistingFilesChecke#STATUS_ERROR} Occurs an error with the
     * database.
     * </ul>
     * @see XmlChecker#isXmlParseable(File)
     * @see EacCpfService#create(XmlType, UpFile, Integer)
     */
    private String insertEacCpfFile(FileUnit fileUnit, XmlType xmlType) {
        //This method insert an EAC-CPF file in the dashboard
        Boolean dataBaseCommitError = false;
        String result = STATUS_NO_EXIST;
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        UpFile upFile = upFileDao.findById(fileUnit.getFileId());
        String cpfId = "";

        //Check for various possible errors in file; set cpfId to "error" if something is wrong
        try {
            cpfId = extractAttributeFromXML(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "eac-cpf/control/recordId", null, true, true);
        } catch (WstxParsingException e1) {
            LOG.error("File was not correct XML, cause: " + e1.getMessage());
            additionalErrors += e1.getMessage();
        }
        String err;
        if ((err = XmlChecker.isXmlParseable(new File(this.uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName()))) != null) {
            cpfId = "error";
            LOG.error("File was not correct XML.");
            additionalErrors += "File was not correct XML. Impossible to parse it. Please check if file is XML. Error: " + err;
        }
        boolean isConverted;
        try {
            isConverted = Boolean.valueOf(extractAttributeFromXML(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "eac-cpf/control/maintenanceHistory/maintenanceEvent/eventDescription", null, false, true));
        } catch (Exception e) {
            if (e instanceof WstxParsingException) {
                cpfId = "error";
                additionalErrors = e.getMessage();
            }
            LOG.info("The file " + fileUnit.getFileName() + " was tried to be converted but failed");
            isConverted = false;
        }

        //compare cpfId to possible result values and trigger respective action
        if (cpfId.equals(STATUS_EMPTY)) { //cpfId is empty
            fileUnit.setEadType(XmlType.EAC_CPF.getName());
            return STATUS_EMPTY;
        } else if (StringUtils.isBlank(cpfId) || cpfId.equals(STATUS_ERROR)) {
            LOG.info("recordId is empty in the file " + fileUnit.getFileName() + ", so we remove everything");
            try {
                deleteFileFromDDBB(fileUnit.getFileId());
            } catch (Exception ex) {
                LOG.error("We could not erase the file from the temp database");
                dataBaseCommitError = true;
            }
            if (!dataBaseCommitError) {
                try {
                    File file = new File(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName());
                    if (file.exists()) {
                        //Windows file lock workaround; uncomment if necessary
                        //System.gc();
                        //Thread.sleep(2000);
                        FileUtils.forceDelete(file);
                    }
                    
                    File uploadDir = new File(uploadedFilesPath + fileUnit.getFilePath());
                    if (uploadDir.listFiles().length == 0) // There aren't any file in the directory, so it should be removed
                    {
                        FileUtils.forceDelete(uploadDir);
                    }
                } catch (IOException ex) {
                    LOG.error("The file " + fileUnit.getFileName() + " could not be removed: " + ex.getMessage(), ex);
                    // belonging to Windows file lock workaround (see above)
//                } catch (InterruptedException ex) {
//                    LOG.error(ex);
                }
            }
            result = STATUS_ERROR;
        } else {
            Integer identifier = eacCpfDAO.isEacCpfIdUsed(cpfId, archivalInstitutionId, EacCpf.class);
            if (identifier != null) { //The cpf_id is already stored in the table

                // The cpfId already exists
                LOG.warn("EAC-CPF identifier '" + cpfId + "' is already existing with id '" + identifier + "' in the table of eac_cpf'");
                fileUnit.setEadid(cpfId);
                fileUnit.setPermId(identifier.intValue());
                result = STATUS_EXISTS;
            } else {
                try {
                    EacCpfService.create(XmlType.EAC_CPF, upFile, archivalInstitutionId);
                } catch (Exception e) {
                    LOG.error("The " + XmlType.EAC_CPF.getName() + " which recordId is " + cpfId + " could not be stored in the table [Database Rollback]. Error:" + e.getMessage(), e);
                    dataBaseCommitError = true;
                }
                result = STATUS_NO_EXIST;
            }
        }
        return result;
    }

    /**
     * Inserts an EAD3 file in the dashboard.
     *
     * @param fileUnit {@link FileUnit} The file to insert in the dashboard.
     * @param xmlType {@link XmlType} The type of xml file.
     * @return String
     * <ul>
     * <li>{@link ExistingFilesChecker#STATUS_EMPTY} The identifier of the
     * eac-cpf file is empty.
     * <li>{@link ExistingFilesChecke#STATUS_EXISTS} Exists in the system.
     * <li>{@link ExistingFilesChecke#STATUS_NO_EXIST} No exists in the system.
     * <li>{@link ExistingFilesChecke#STATUS_ERROR} Occurs an error with the
     * database.
     * </ul>
     * @see XmlChecker#isXmlParseable(File)
     * @see EacCpfService#create(XmlType, UpFile, Integer)
     */
    private String insertEad3File(FileUnit fileUnit, XmlType xmlType) {
        //This method insert an EAD3 file in the dashboard

        String result = STATUS_NO_EXIST;
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        UpFile upFile = upFileDao.findById(fileUnit.getFileId());
        String ead3Id = "";

        //Check for various possible errors in file; set cpfId to "error" if something is wrong
        try {
            ead3Id = extractAttributeFromXML(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "ead/control/recordId", null, true, false);
        } catch (WstxParsingException e1) {
            LOG.error("File was not correct XML, cause: " + e1.getMessage());
            additionalErrors += e1.getMessage();
        }
        String title = "";
        try {
            title = ExistingFilesChecker.extractAttributeFromXML(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(),
                    "ead/control/filedesc/titlestmt/titleproper", null, true, false);
        } catch (WstxParsingException e) {
            title = "";
        }
        String err;
        if ((err = XmlChecker.isXmlParseable(new File(this.uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName()))) != null) {
            ead3Id = "error";
            LOG.error("File was not correct XML.");
            additionalErrors += "File was not correct XML. Impossible to parse it. Please check if file is XML. Error: " + err;
        }

        //compare cpfId to possible result values and trigger respective action
        if (ead3Id.equals(STATUS_EMPTY)) { //cpfId is empty
            fileUnit.setEadType(XmlType.EAD_3.getName());
            return STATUS_EMPTY;
        } else if (StringUtils.isBlank(ead3Id) || ead3Id.equals(STATUS_ERROR)) {
            LOG.info("recordId is empty in the file " + fileUnit.getFileName() + ", so we remove everything");
            try {
                deleteFileFromDDBB(fileUnit.getFileId());
                
                removeFile(uploadedFilesPath + fileUnit.getFilePath(), fileUnit.getFileName());
                
            } catch (APEnetException ex) {
                LOG.error("We could not erase the file from the temp database");
            } catch (IOException ex) {
                LOG.error("The file " + fileUnit.getFileName() + " could not be removed: " + ex.getMessage(), ex);
            }
            
            result = STATUS_ERROR;
        } else {
            Integer identifier = ead3DAO.isEad3IdUsed(ead3Id, archivalInstitutionId, Ead3.class);
            if (identifier != null) { //The cpf_id is already stored in the table

                // The cpfId already exists
                LOG.warn("EAD3 identifier '" + ead3Id + "' is already existing with id '" + identifier + "' in the table of Ead3'");
                fileUnit.setEadid(ead3Id);
                fileUnit.setPermId(identifier.intValue());
                result = STATUS_EXISTS;
            } else {
                try {
                    new Ead3Service().create(XmlType.EAD_3, upFile, archivalInstitutionId, ead3Id, title);
                } catch (Exception e) {
                    LOG.error("The " + XmlType.EAD_3.getName() + " which recordId is " + ead3Id + " could not be stored in the table [Database Rollback]. Error:" + e.getMessage(), e);
                }
                result = STATUS_NO_EXIST;
            }
        }
        return result;
    }
    
    private void removeFile(String dir, String fileName) throws IOException {
        File file = new File(dir + fileName);
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
        
        File uploadDir = new File(dir);
        if (uploadDir.listFiles().length == 0) { // There aren't any file in the directory, so it should be removed
            FileUtils.forceDelete(uploadDir);
        }
    }

    /**
     * This method is implemented by some developer and it's not used.
     *
     * @param xmlType {@link XmlType} The type of the file.
     * @return String
     */
    public String instantiateCorrectDirPath(XmlType xmlType) {
        String startPath = APEnetUtilities.FILESEPARATOR + archivalInstitutionCountry + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR;
        if (xmlType == XmlType.EAD_FA) {
            return startPath + "FA" + APEnetUtilities.FILESEPARATOR;
        } else if (xmlType == XmlType.EAD_HG) {
            return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
        } else if (xmlType == XmlType.EAD_SG) {
            return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
        }
        return null;
    }

    /**
     * This method is implemented by some developer and it's not used.
     *
     * @param xmlType {@code XmlType} The type of the file.
     * @return {@link Ead}
     */
    public Ead instantiateCorrectEadType(XmlType xmlType) {
        if (xmlType == XmlType.EAD_FA) {
            return new FindingAid();
        } else if (xmlType == XmlType.EAD_HG) {
            return new HoldingsGuide();
        } else if (xmlType == XmlType.EAD_SG) {
            return new SourceGuide();
        }
        return null;
    }

    /**
     * <p>
     * Extracts the attribute or the element from XML.
     * <p>
     * To read the XML is used the library {@code StAX}.
     * <p>
     * If the attribute is null, doesn't extract the element from XML.
     * <p>
     * If "isReturningFirstInstance" is "true" extracts the data and stops to
     * read the file.
     *
     * @param path String The path of the file XML.
     * @param element String The element in the XML.
     * @param attribute String The attribute in the XML.
     * @param isReturningFirstInstance boolean If it's the first instance of the
     * element in the XML.
     * @param eacCpf boolean It's an eac-cpf file or not.
     * @return String
     * <ul>
     * <li>The attribute or the element in the XML.
     * <li>"error" An error occurs when reading the file.
     * <li>"empty" The XML hasn't the attribute or the element.
     * </ul>
     * @throws WstxParsingException
     * @see org.codehaus.stax2.XMLInputFactory2
     * @see org.codehaus.stax2.XMLStreamReader2
     * @see javax.xml.stream.XMLInputFactory
     */
    public static String extractAttributeFromXML(String path, String element, String attribute, boolean isReturningFirstInstance, boolean eacCpf) throws WstxParsingException {
        final String CONVERTED_FLAG;
        final String CONVERTED_FLAG_NEW;
        final String CREATED_FLAG_DASHBOARD = "Created_with_apeEAC-CPF_form";
        XMLStreamReader2 input = null;
        InputStream sfile = null;
        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();
        if (eacCpf) {
            CONVERTED_FLAG = "Converted_apeEAC-CPF_version_";
            CONVERTED_FLAG_NEW = "Converted_apeEAC-CPF_version_";
        } else {
            CONVERTED_FLAG = "Converted_APEnet_EAD_version_";
            CONVERTED_FLAG_NEW = "Converted_apeEAD_version_";
        }
        try {
            sfile = new FileInputStream(path);
            input = (XMLStreamReader2) xmlif.createXMLStreamReader(sfile);
            
            boolean abort = false;
            boolean isInsideElement = false;
            boolean isInsidePath = false;
            boolean wasInsidePath = false;
            boolean eventType = false;
            boolean derived = false;
            boolean created = false;
            String importantData = "";
            
            String[] pathElements = element.split("/");
            int lenghtPath = pathElements.length;
            int pointerPath = 0;
            
            LOG.debug("Checking file, looking for element " + element + ", and attribute " + ((attribute == null) ? "null" : attribute) + ", path begins with " + pathElements[0]);
            while (!abort && input.hasNext()) {
                switch (input.getEventType()) {
                    case XMLEvent.START_DOCUMENT:
                        break;
                    case XMLEvent.START_ELEMENT:
                        if (pointerPath < lenghtPath && input.getLocalName().equalsIgnoreCase(pathElements[pointerPath])) {
                            LOG.debug("We arrive in " + input.getLocalName());
                            isInsidePath = true;
                            wasInsidePath = true;
                            LOG.debug("pointerPath = " + pointerPath + ", lenghtPath = " + lenghtPath);
                            if (pointerPath == lenghtPath - 1) {
                                isInsideElement = true;
                                if (attribute != null) {
                                    for (int attributeNb = 0; attributeNb < input.getAttributeCount(); attributeNb++) {
                                        if (input.getAttributeLocalName(attributeNb).equals(attribute)) {
                                            LOG.debug("Returning " + input.getAttributeValue(attributeNb));
                                            return input.getAttributeValue(attributeNb);
                                        }
                                    }
                                    LOG.debug("Returning error");
                                    return "error";
                                }

                                //TODO: Here add the check of empty element by Patricia and call the function with extractAttributeFromEad([path], "eadid", null, true);
                                if (input.getLocalName().equals("eadid") || input.getLocalName().equals("recordId")) {
                                    if (input.isEmptyElement()) {
                                        LOG.debug("Returning empty");
                                        return ("empty");
                                    }
                                }
                            }
                            pointerPath++;
                        }
                        if (input.getLocalName().equalsIgnoreCase("eventType") && eacCpf) {
                            eventType = true;
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                        if (eventType) {
                            String target = input.getText();
                            if (target.equalsIgnoreCase("derived")) {
                                derived = true;
                            } else if (target.equalsIgnoreCase("created")) {
                                created = true;
                            }
                        }
                        if (isInsideElement) {
                            importantData = input.getText();
                            if (importantData != null && (importantData.isEmpty() || importantData.trim()
                                    .replaceAll("[\\s &&[^\\n]] ", " ") //1. reduce all non-newline whitespaces to a unique space
                                    .replaceAll("(?m)^\\s |\\s$", "") //2. remove spaces from start or end of the lines
                                    .replaceAll("\\n ", " ") //3. remove all newlines, compress it in a unique line))
                                    .length() == 0)) {
                                return "empty";
                            }
                            if (((importantData.startsWith(CONVERTED_FLAG) || importantData.startsWith(CONVERTED_FLAG_NEW)) && !eacCpf)
                                    || (((importantData.startsWith(CONVERTED_FLAG) || importantData.startsWith(CONVERTED_FLAG_NEW)) && derived)
                                    || (importantData.startsWith(CREATED_FLAG_DASHBOARD) && created) && eacCpf)) {
                                return "true";
                            } else if (isReturningFirstInstance) {
                                return importantData;
                            }
                        }
                        break;
                    case XMLEvent.CDATA:
                        break;
                    case XMLEvent.END_ELEMENT:
                        if (isInsidePath && input.getLocalName().equals(pathElements[pointerPath - 1])) {
                            //TODO: Check added of empty element for Patricia, call the function with extractAttributeFromEad([path], "eadid", null, true);
                            if (isInsideElement && isReturningFirstInstance) {
                                LOG.debug("Returning empty");
                                return "empty";
                            }
                            pointerPath--;
                            LOG.debug("We leave from " + input.getLocalName());
                            isInsideElement = false;
                            LOG.debug("pointerPath = " + pointerPath);
                            if (pointerPath == 0) {
                                isInsidePath = false;
                            }
                        }
                        if (!isInsidePath && wasInsidePath) {
                            abort = true;
                        }
                        break;
                }
                if (input.hasNext()) {
                    input.next();
                }
            }
        } catch (Exception e) {
            if (e instanceof WstxParsingException) {
                throw (WstxParsingException) e;
            }
            LOG.error("Error parsing StAX for file " + path, e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (sfile != null) {
                    sfile.close();
                }
            } catch (Exception e) {
                LOG.error("Error closing streams" + e.getMessage(), e);
            }
        }
        LOG.debug("Returning error");
        return "error";
    }

    /**
     * <p>
     * Cancels, the answer of the user.
     * <p>
     * The user has decided not to overwrite the file in the dashboard
     * <p>
     * so it's necessary to delete the file from the directory
     * <p>
     * and delete its entry in <b>up_file</b> table.
     *
     * @param fileUnit {@link FileUnit} The file to delete in the dashboard.
     * @return String
     * <ul>
     * <li>"error" Occurs an error when delete the file.
     * <li>"oK" The file is deleted from the database and the file system.
     * </ul>
     * @see ContentUtils#deleteFile(String)
     */
    public String cancelAnswer(FileUnit fileUnit) {
        // The user has decided not to overwrite the file in the Dashboard so it is necessary to delete the file from up directory and delete its entry in up_file table
        try {
            deleteFileFromDDBB(fileUnit.getFileId());
            try {
                ContentUtils.deleteFile(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName());
                File uploadDir = new File(uploadedFilesPath + fileUnit.getFilePath());
                if (uploadDir.listFiles().length == 0) {
                    FileUtils.forceDelete(uploadDir);
                }
                LOG.info("The file " + fileUnit.getFileName() + " has been deleted successfully.");
            } catch (Exception e) {
                throw new APEnetException("The file " + fileUnit.getFileName() + " or the directory " + uploadedFilesPath + fileUnit.getFilePath() + " could not be removed. Error: " + e.getMessage());
            }
        } catch (APEnetException ape) {
            LOG.error(ape.getMessage(), ape);
            return "error";
        }
        return "ok";
    }

    /**
     * <p>
     * Overwrites, the answer of the user.
     * <p>
     * The user has decided to overwrite the file.
     *
     * @param fileUnit {@link FileUnit} The file to overwrite for the user.
     * @return String
     * <ul>
     * <li>"error" Occurs an error to overwrite the file.
     * <li>"ok" The file is overwrite in the system.
     * </ul>
     * @see EacCpfService#overwrite(EacCpf, UpFile)
     * @see EadService#overwrite(Ead, UpFile)
     */
    public String overwriteAnswer(FileUnit fileUnit) {
        // The user has decided to overwrite the file
        if (fileUnit.getFileType().equals("xsl")) {
            // The file is an XSL(t) file
            try {
                deleteFileFromDDBB(fileUnit.getFileId());
                insertFileToTempFiles(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), xslPath + fileUnit.getFileName(), fileUnit.getFilePath());
                LOG.info("The file " + fileUnit.getFileName() + " has been overwritten");
            } catch (APEnetException ape) {
                LOG.error(ape.getMessage(), ape);
                return "error";
            }
        } else // The file is an EAD or EAC-CPF
        {
            if (fileUnit.getEadType().equals(XmlType.EAD_FA.getName()) || fileUnit.getEadType().equals(XmlType.EAD_HG.getName()) || fileUnit.getEadType().equals(XmlType.EAD_SG.getName()) || fileUnit.getEadType().equals(XmlType.EAC_CPF.getName()) || fileUnit.getEadType().equals(XmlType.EAD_3.getName())) {
                try {
                    XmlType xmlType = XmlType.getType(fileUnit.getEadType());
                    if (xmlType == XmlType.EAC_CPF) {
                        EacCpf eacToOverwrite = DAOFactory.instance().getEacCpfDAO().getEacCpfById(archivalInstitutionId, fileUnit.getEadid());
                        EacCpfService.overwrite(eacToOverwrite, upFileDao.findById(fileUnit.getFileId()));
                    } else if (xmlType == XmlType.EAD_3) {
                        Ead3 ead3ToOverwrite = DAOFactory.instance().getEad3DAO().getEad3ByIdentifier(archivalInstitutionId, fileUnit.getEadid());
                        Ead3Service.overwrite(ead3ToOverwrite, upFileDao.findById(fileUnit.getFileId()));
                    } else {
                        Ead eadToOverwrite = DAOFactory.instance().getEadDAO().getEadByEadid(xmlType.getEadClazz(), archivalInstitutionId, fileUnit.getEadid());
                        EadService.overwrite(eadToOverwrite, upFileDao.findById(fileUnit.getFileId()));
                    }
                    //overwrite(fileUnit);
                } catch (Exception e) {
                    return "error";
                }
            }
        }
        return "ok";
    }
    
    public void deleteUpFile(FileUnit fileUnit) {
        try {
            deleteFileFromDDBB(fileUnit.getFileId());
            File file = new File(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName());
            if (file.exists()) {
                FileUtils.forceDelete(file);
                LOG.info("The file " + fileUnit.getFileName() + " has been removed from Dashboard and its file system");
            }
        } catch (Exception e) {
            LOG.error("We could not delete the upFile", e);
        }
    }

    // This method overwrite (or not) a file in the Dashboard for a user
    // If everything is ok then it returns "ok" but if it was a problem, then it
    // returns "error"
    /**
     * <p>
     * Overwrites, cancels or changes the identifier of a file in the Dashboard
     * for a user.
     *
     * @param fileUnit {@link FileUnit} The file to overwrite for the user.
     * @param answer String The answer of the user in the Dashboard.
     * @param savechangesIDanswer String The answer gets the values "OK" or
     * "KO".
     * @param canceloverwriteanswer String The answer "Overwrite" or "Cancel".
     * @param fileType String The type of the file (EAC-CPF, FA, SG, HG).
     * @param newIdentifier String The new identifier to the file.
     * @return String
     * <ul>
     * <li>{@link ExistingFilesChecke#STATUS_ERROR} Occurs an error when
     * overwrites the file.
     * <li>{@link ExistingFilesChecke#STATUS_EXISTS} The identifier exists in
     * the system.
     * <li>{@link ExistingFilesChecke#STATUS_NO_EXIST} The file doesn't exist.
     * </ul>
     * @see EacCpfService#create(XmlType, UpFile, Integer)
     * @see EadService#create(XmlType, UpFile, Integer)
     */
    public String overwriteFile(FileUnit fileUnit, String answer, String savechangesIDanswer, String canceloverwriteanswer, String fileType, String newIdentifier) {
        String result = "ok";
        Boolean dataBaseCommitError = false;
        
        if (answer.equalsIgnoreCase("Cancel")) {
            result = cancelAnswer(fileUnit);
        } else if (answer.equalsIgnoreCase("overwrite")) {
            result = overwriteAnswer(fileUnit);
        } else //Change EADID
        //Check the content of savechangesEADIDanswer OK or KO.
        {
            if ((savechangesIDanswer.equals("OK")) || (canceloverwriteanswer.equals("Overwrite"))) {
                //Change into the XML the EADID
                //There is not any FA with this new EADID.
                //Edit the file and update the eadid for the new.
                //1st obtain the file's url.

                String identifier = changeIdentifierUsingDOM(fileUnit, newIdentifier, fileType);
                
                boolean isConverted;
                try {
                    if (fileType.equals(XmlType.EAC_CPF.getName())) {
                        isConverted = Boolean.valueOf(extractAttributeFromXML(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "eac-cpf/control/maintenanceHistory/maintenanceEvent/eventDescription", null, false, true));
                    } else {
                        isConverted = Boolean.valueOf(extractAttributeFromXML(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "eadheader/revisiondesc/change/item", null, false, false));
                    }
                } catch (Exception e) {
                    if (e instanceof WstxParsingException) {
                        identifier = STATUS_ERROR;
                        additionalErrors = e.getMessage();
                    }
                    LOG.info("The file " + fileUnit.getFileName() + " was tryed to be converted but failed");
                    isConverted = false;
                }
                /**/
                
                if (fileType.equals(XmlType.EAD_3.getName()) || fileType.equals(XmlType.EAD_FA.getName()) || fileType.equals(XmlType.EAD_HG.getName()) || fileType.equals(XmlType.EAD_SG.getName()) || fileType.equals(XmlType.EAC_CPF.getName())) {
                    if (identifier.equals(STATUS_ERROR) || "".equals(identifier)) {
                        // It is necessary to remove the file from /mnt/tmp/up folder
                        LOG.info("The file " + fileUnit.getFileName() + " doesn't have a proper format: it doesn't have eadid or it has several");
                        File file = new File(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName());
                        File uploadDir = new File(uploadedFilesPath + fileUnit.getFilePath());
                        try {
                            deleteFileFromDDBB(fileUnit.getFileId());
                            try {
                                if (file.exists()) {
                                    FileUtils.forceDelete(file);
                                    LOG.info("The file " + fileUnit.getFileName() + " has been removed from Dashboard");
                                }
                                if (uploadDir.listFiles().length == 0) {
                                    FileUtils.forceDelete(uploadDir);
                                }
                            } catch (IOException ex) {
                                throw new APEnetException("The file " + fileUnit.getFileName() + " could not be removed: " + ex.getMessage(), ex);
                            }
                        } catch (Exception e) {
                            LOG.error(e.getMessage(), e);
                        }
                        return STATUS_ERROR;
                    } else {
                        // It is necessary to check if the identifier is already in Finding Aid Table or eac-cpf Table for the current Archival Institution
                        XmlType xmlType = XmlType.getType(fileType);
                        Integer fileIdentifier;
                        if (xmlType == XmlType.EAC_CPF) {
                            fileIdentifier = DAOFactory.instance().getEacCpfDAO().isEacCpfIdUsed(identifier, archivalInstitutionId, EacCpf.class);
                            
                        } else if (xmlType == XmlType.EAD_3) {
                            fileIdentifier = DAOFactory.instance().getEad3DAO().isEad3IdUsed(identifier, archivalInstitutionId, Ead3.class);
                        } else {
                            fileIdentifier = DAOFactory.instance().getEadDAO().isEadidUsed(identifier, archivalInstitutionId, xmlType.getEadClazz());
                        }
                        if (fileIdentifier != null) {
                            // The ID already exists
                            fileUnit.setEadid(identifier);
                            fileUnit.setPermId(fileIdentifier);
                            if ((xmlType == XmlType.EAD_3 || xmlType == XmlType.EAD_FA) || (xmlType == XmlType.EAC_CPF) && canceloverwriteanswer.equals("Overwrite")) { //todo: Why only for FA?
                                overwriteFile(fileUnit, "Overwrite", savechangesIDanswer, canceloverwriteanswer, fileType, newIdentifier);
                            } else {
                                LOG.error("identifier '" + identifier + "' is already existing in the table with id '" + fileIdentifier + "'");
                                return STATUS_EXISTS;
                            }
                        } else {
                            // The EAD or EAC-CPF doesn't exist
                            // It is necessary to move the file to /mnt/tmp/tmp/ and remove it from /mnt/tmp/up/ folder
                            // It is necessary to insert an entry in finding_aid table or eac-cpf table
                            // It is necessary to remove the entry from up_file table

                            //Ead ead = instantiateCorrectEadType(xmlType);
                            try {
                                if (fileType.equals(XmlType.EAC_CPF.getName())) {
                                    EacCpfService.create(xmlType, upFileDao.findById(fileUnit.getFileId()), archivalInstitutionId);
                                } else if (fileType.equals(XmlType.EAD_3.getName())) {
                                    String title = "";
                                    try {
                                        title = ExistingFilesChecker.extractAttributeFromXML(uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(),
                                                "ead/control/filedesc/titlestmt/titleproper", null, true, false);
                                    } catch (WstxParsingException e) {
                                        title = "";
                                    }
                                    new Ead3Service().create(xmlType, upFileDao.findById(fileUnit.getFileId()), archivalInstitutionId, fileUnit.getEadid(), title);
                                } else {
                                    EadService.create(xmlType, upFileDao.findById(fileUnit.getFileId()), archivalInstitutionId);
                                }
                            } catch (Exception e) {
                                LOG.error("The file which identifier is '" + identifier + "' could not be stored in table [Database Rollback]. Error:" + e.getMessage());
                                
                                dataBaseCommitError = true;
                            } finally {
                                
                            }
                            return STATUS_NO_EXIST;
                        }
                    }
                }
            } else if (savechangesIDanswer.equals("KO")) {
                //In this case, the change of the EADID has failed.
                //Then, the file will be accessible to Change the EADID in next access to Dashboard.
                LOG.error("The file " + fileUnit.getFileName() + " has not been changed correctly");
                if (canceloverwriteanswer.equals("Overwrite")) {
                    result = overwriteAnswer(fileUnit);
                } else if (canceloverwriteanswer.equals("Cancel")) {
                    result = cancelAnswer(fileUnit);
                }
            }
        }
        return result;
    }

    /**
     * Deletes the file in the system.
     *
     * @param path String The path file in the system.
     * @return
     * @throws IOException
     */
    public Boolean deleteFile(String path) throws IOException {
        File srcFile = new File(path);
        FileUtils.forceDelete(srcFile);
        return null;
    }

    // This method deletes the destination file, copies the source file to
    // temporal directory and finally deletes the source file if everything is
    // ok
    // If the source folder is empty, then the folder will be removed
    /**
     * <p>
     * Deletes the destination file, copies the source file to temporal
     * directory and finally deletes the source file
     * <p>
     * if everything is right. If the source folder is empty, then the folder
     * will be deleted.
     *
     * @param srcFilePath String The source path of the file.
     * @param destFilePath String The destination path of the file.
     * @param fileUnitFilePath String The path file in the system.
     * @throws APEnetException
     */
    private void insertFileToTempFiles(String srcFilePath, String destFilePath, String fileUnitFilePath) throws APEnetException {
        try {
            File srcFile = new File(srcFilePath);
            File destFile = new File(destFilePath);
            File uploadDir = new File(uploadedFilesPath + fileUnitFilePath);
            if (destFile.exists()) {
                FileUtils.forceDelete(destFile);
            }
            
            FileUtils.copyFile(srcFile, destFile);
            FileUtils.forceDelete(srcFile);
            
            if (uploadDir.listFiles().length == 0) {
                FileUtils.forceDelete(uploadDir);
            }
        } catch (IOException e) {
            throw new APEnetException("The file '" + srcFilePath + "' could not be removed. Error: " + e.getMessage(), e);
        }
    }

    // This method removes the entry which has ufId as the primary key from up_files table
    /**
     * Removes the entry which has <i>ufId</i> as primary key from
     * <b>up_files</b> table.
     *
     * @param ufId {@link Integer} The entry "ufId" in the table
     * <b>up_files</b>.
     * @throws APEnetException
     */
    private void deleteFileFromDDBB(Integer ufId) throws APEnetException {
        try {
            JpaUtil.beginDatabaseTransaction();
            
            UpFile upFile = upFileDao.findById(ufId);
            upFileDao.deleteSimple(upFile);
            
            JpaUtil.commitDatabaseTransaction();
        } catch (Exception e) {
            LOG.error("The file uploaded with ID = '" + ufId.toString() + "' couldn't be removed from up_file table [Database Rollback]. Error: " + e.getMessage());
            JpaUtil.rollbackDatabaseTransaction();
            throw new APEnetException("Error deleting file from up_file table", e);
        }
    }

    /**
     * Changes the old identifier for a new one using DOM.
     *
     * @param fileUnit {@link FileUnit} The XML file.
     * @param newIdentifier String The new identifier.
     * @param eac boolean It's an EAC-CPF file or not.
     * @return String The new identifier or error in other case.
     * @see javax.xml.transform.Transformer;
     * @see javax.xml.transform.TransformerFactory;
     * @see javax.xml.transform.dom.DOMSource;
     * @see javax.xml.transform.stream.StreamResult;
     */
    private String changeIdentifierUsingDOM(FileUnit fileUnit, String newIdentifier, String fileType) {
        // Recovers the current uploaded file.
        UpFileDAO upFileDao = DAOFactory.instance().getUpFileDAO();
        UpFile upfile = upFileDao.findById(fileUnit.getFileId());
        String path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath();
        String filePath = path + upfile.getPath() + upfile.getFilename();
        String oldFilePath = filePath.replace(filePath.substring(filePath.lastIndexOf(".")), "_old.xml");
        File file = new File(filePath);
        File oldFile = new File(oldFilePath);

        // Creates the DOM object.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);

        factory.setNamespaceAware(true);
        try {
            factory.setFeature("http://xml.org/sax/features/namespaces", false);
            factory.setFeature("http://xml.org/sax/features/validation", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // Try to recover the current EADID in the file.
        String oldIdentifier = "";
        Node identifierNode = null;
        Document doc = null;

        try {
            if (fileType.equalsIgnoreCase(XmlType.EAC_CPF.getName())) {
                oldIdentifier = this.extractAttributeFromXML(this.uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "eac-cpf/control/recordId", null, true, true).trim();
            } else if (fileType.equalsIgnoreCase(XmlType.EAD_3.getName())) {
                oldIdentifier = this.extractAttributeFromXML(this.uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "ead/control/recordid", null, true, true).trim();
            } else {
                oldIdentifier = this.extractAttributeFromXML(this.uploadedFilesPath + fileUnit.getFilePath() + fileUnit.getFileName(), "eadheader/eadid", null, true, false).trim();
            }

            // Creates a copy of the current uploaded file.
            FileUtils.copyFile(file, oldFile);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream in = new FileInputStream(filePath);
            doc = builder.parse(in);
            doc.getDocumentElement().normalize();
            NodeList nodeList;
            if (fileType.equalsIgnoreCase(XmlType.EAC_CPF.getName())) {
                nodeList = doc.getElementsByTagName("recordId");
            }else if(fileType.equalsIgnoreCase(XmlType.EAD_3.getName())){
                nodeList = doc.getElementsByTagName("recordid");
            } else {
                nodeList = doc.getElementsByTagName("eadid");
            }
            identifierNode = nodeList.item(0);
            String currentIdentifier = identifierNode.getTextContent();

            // Escape char '&' in the newIdentifier.
            if (newIdentifier.contains("&amp;")) {
                newIdentifier = newIdentifier.replaceAll("&amp;", "&");
            }
            if (newIdentifier.contains("&")) {
                newIdentifier = newIdentifier.replaceAll("&", "&amp;");
            }

            // Checks if the currenteadid is the same as the newIdentifier.
            if (!currentIdentifier.equals(newIdentifier) && (!newIdentifier.isEmpty())) {
                LOG.info("Changing the identifier into the file");
                identifierNode.setTextContent(newIdentifier);
                Result result1 = new StreamResult(new File(filePath));
                Source source = new DOMSource(doc);
                Transformer transformer;
                transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result1);

                // Save the information and delete the old file.
                fileUnit.setEadid(newIdentifier);
                FileUtils.forceDelete(oldFile);
                
                return newIdentifier;
            }
        } catch (Exception ex) {
            LOG.error("overwrite: " + ex.getMessage());
            // Some error occurred, trying to revert the changes.
            boolean rollback = false;
            if (oldIdentifier != null && !oldIdentifier.isEmpty() && identifierNode != null && doc != null) {
                LOG.info("Removing the new identifier into the original file because of fail renaming process");
                identifierNode.setTextContent(oldIdentifier);
                Result result2 = new StreamResult(new File(filePath));
                Source source2 = new DOMSource(doc);
                Transformer transformer2;
                try {
                    transformer2 = TransformerFactory.newInstance().newTransformer();
                    transformer2.transform(source2, result2);
                    rollback = true;
                } catch (Exception e) {
                    LOG.error("undo-overwrite: " + e.getMessage());
                }
            }
            
            if (oldFile.exists()) {
                oldFile.renameTo(file);
            }
            if (rollback) {
                return STATUS_ERROR;
            }
        }
        return "";
    }
    
}
