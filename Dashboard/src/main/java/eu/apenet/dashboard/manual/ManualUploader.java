package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import eu.apenet.dashboard.manual.eag.Eag2012GeoCoordinatesAction;
import eu.apenet.persistence.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscapeUtils;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.services.ead3.Ead3Service;
import eu.apenet.dashboard.utils.ZipManager;
import eu.apenet.dpt.utils.util.XsltChecker;
import eu.apenet.persistence.factory.DAOFactory;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 *
 * @author eloy
 *
 */
/**
 * This class is in charge of uploading files to APEnet, unzipping them if applicable and storing them in UP FILES
 * container
 *
 */
public abstract class ManualUploader {

    private static final String MAGIC_KEY = "99999999999";

    private final Logger log = Logger.getLogger(getClass());
    protected String uploadingMethod;
    protected ZipManager zipManager;

    private List<String> filesNotUploaded;
    private List<String> filesUploaded;

    private List<String> warnings_eag;

    private List<ArchivalInstitution> archivalInstitutionsToDelete = new ArrayList<ArchivalInstitution>();
    private List<ArchivalInstitution> archivalInstitutionsToInsert = new ArrayList<ArchivalInstitution>();
    private List<AiAlternativeName> archivalInstitutionsNameNotChanged = new ArrayList<AiAlternativeName>();
    private List<AiAlternativeName> archivalInstitutionsNameChanged = new ArrayList<AiAlternativeName>();
    private List<ArchivalInstitution> archivalInstitutionsParentNotChanged = new ArrayList<ArchivalInstitution>();
    private List<ArchivalInstitution> archivalInstitutionsParentChanged = new ArrayList<ArchivalInstitution>();

    private List<FindingAid> fasDeleted = new ArrayList<FindingAid>();
    private List<HoldingsGuide> hgsDeleted = new ArrayList<HoldingsGuide>();

    private RightsInformation shareWithWikimediaRightsInformation;
    private Boolean doNotShowPopupAgain;

    public List<FindingAid> getFasDeleted() {
        return fasDeleted;
    }

    public void setFasDeleted(List<FindingAid> fasDeleted) {
        this.fasDeleted = fasDeleted;
    }

    public void setShareWithWikimediaRightsInformation(RightsInformation shareWithWikimediaRightsInformation) {
        this.shareWithWikimediaRightsInformation = shareWithWikimediaRightsInformation;
    }

    public RightsInformation getShareWithWikimediaRightsInformation() {
        return shareWithWikimediaRightsInformation;
    }

    public void setDoNotShowPopupAgain(Boolean doNotShowPopupAgain) {
        this.doNotShowPopupAgain = doNotShowPopupAgain;
    }

    public Boolean isDoNotShowPopupAgain() {
        return doNotShowPopupAgain;
    }

    public List<HoldingsGuide> getHgsDeleted() {
        return hgsDeleted;
    }

    public void setHgsDeleted(List<HoldingsGuide> hgsDeleted) {
        this.hgsDeleted = hgsDeleted;
    }

    public List<ArchivalInstitution> getArchivalInstitutionsToDelete() {
        return archivalInstitutionsToDelete;
    }

    public void setArchivalInstitutionsToDelete(
            List<ArchivalInstitution> archivalInstitutionsToDelete) {
        this.archivalInstitutionsToDelete = archivalInstitutionsToDelete;
    }

    public String getUploadingMethod() {
        return uploadingMethod;
    }

    public void setUploadingMethod(String uploadingMethod) {
        this.uploadingMethod = uploadingMethod;
    }

    public List<String> getFilesUploaded() {
        return filesUploaded;
    }

    public List<String> getFilesNotUploaded() {
        return filesNotUploaded;
    }

    public List<String> getWarnings_eag() {
        return warnings_eag;
    }

    public ManualUploader() {

    }

    public String upload() {
        return null;
    }

    public List<ArchivalInstitution> getArchivalInstitutionsToInsert() {
        return archivalInstitutionsToInsert;
    }

    public void setArchivalInstitutionsToInsert(
            List<ArchivalInstitution> archivalInstitutionsToInsert) {
        this.archivalInstitutionsToInsert = archivalInstitutionsToInsert;
    }

    public List<AiAlternativeName> getArchivalInstitutionsNameNotChanged() {
        return archivalInstitutionsNameNotChanged;
    }

    public void setArchivalInstitutionsNameNotChanged(
            List<AiAlternativeName> archivalInstitutionsNameNotChanged) {
        this.archivalInstitutionsNameNotChanged = archivalInstitutionsNameNotChanged;
    }

    public List<AiAlternativeName> getArchivalInstitutionsNameChanged() {
        return archivalInstitutionsNameChanged;
    }

    public void setArchivalInstitutionsNameChanged(
            List<AiAlternativeName> archivalInstitutionsNameChanged) {
        this.archivalInstitutionsNameChanged = archivalInstitutionsNameChanged;
    }

    public List<ArchivalInstitution> getArchivalInstitutionsParentNotChanged() {
        return archivalInstitutionsParentNotChanged;
    }

    public void setArchivalInstitutionsParentNotChanged(
            List<ArchivalInstitution> archivalInstitutionsParentNotChanged) {
        this.archivalInstitutionsParentNotChanged = archivalInstitutionsParentNotChanged;
    }

    public List<ArchivalInstitution> getArchivalInstitutionsParentChanged() {
        return archivalInstitutionsParentChanged;
    }

    public void setArchivalInstitutionsParentChanged(
            List<ArchivalInstitution> archivalInstitutionsParentChanged) {
        this.archivalInstitutionsParentChanged = archivalInstitutionsParentChanged;
    }

    public String uploadFile(String uploadType, String fileName, File file, String contentType, Integer archivalInstitutionId, String uploadMethodString) {
        return uploadFile(uploadType, fileName, file, contentType, archivalInstitutionId, uploadMethodString, null);
    }

    //This method uploads only one file to the Dashboard. It stores this file in a temporal directory
    public String uploadFile(String uploadType, String fileName, File file, String contentType, Integer archivalInstitutionId, String uploadMethodString, Ingestionprofile profile) {

        String result = null;
        String path = null;
        if (archivalInstitutionId != null) {
            path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId.toString() + APEnetUtilities.FILESEPARATOR;
        }
        String fullFileName = "";
        String tempPath = path + "tmp" + fileName + new Date().getTime() + APEnetUtilities.FILESEPARATOR;	//This is the path in which the zip files are going to be unzipped
        File tempDir = new File(tempPath);
        this.filesNotUploaded = new ArrayList<String>();
        this.filesUploaded = new ArrayList<String>();
        if (uploadType.equals("EAD")) {

            // Uncomment this line if xsl and xslt files are permitted again
            //if (contentType.equals("zip") || contentType.equals("xml") || contentType.equals("xsl") || contentType.equals("xslt")){
            if (contentType.equals("zip") || contentType.equals("xml")) {
                fileName = APEnetUtilities.convertToFilename(fileName);
                if (contentType.equals("zip")) {
                    fullFileName = tempPath + fileName; //The file uploaded is a ZIP file. It's necessary to store it in a temp directory to unzip it
                } else {
                    fullFileName = path + fileName;
                }

                File theFile = new File(fullFileName);

                if (theFile.exists()) {
                    result = "error";
                } else {
                    try {
                        try {
                            FileUtils.copyFile(file, theFile);
                        } catch (IOException e) {
                            throw new APEnetException("The file " + fileName + " could not be copied from tmp directory (this file is part of a zipped file). Error: " + e.getMessage(), e);
                        }

                        if (!contentType.equals("zip")) {
                            filesUploaded.add(fileName);
                            UpFile upFile;

                            try {
                                //Insert file uploaded into up_file table
                                JpaUtil.beginDatabaseTransaction();

                                String defaultUpDir = APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR;
                                if (contentType.equals("xml")) {
                                    upFile = createUpFile(defaultUpDir, fileName, uploadMethodString, archivalInstitutionId, FileType.XML);
                                } else if (contentType.equals("xsl") || contentType.equals("xslt")) {
                                    upFile = createUpFile(defaultUpDir, fileName, uploadMethodString, archivalInstitutionId, FileType.XSL);
                                    if (!XsltChecker.isXsltParseable(theFile)) {
                                        log.error("Error! The XSLT file should NOT be kept on the server but should be removed and the partner should be advised.");
                                    }
                                } else {
                                    upFile = createUpFile(defaultUpDir, fileName, uploadMethodString, archivalInstitutionId, FileType.ZIP);
                                }

                                DAOFactory.instance().getUpFileDAO().insertSimple(upFile);
                                JpaUtil.commitDatabaseTransaction();
                            } catch (Exception e) {
                                JpaUtil.rollbackDatabaseTransaction();
                                throw new APEnetException("Error inserting the file " + fileName + " in up_table (the user was uploading this file to the Dashboard) [Database Rollback]. Error: " + e.getMessage());
                            }

                            try {
                                //Copy file into /mnt/tmp/up/ folder
                                File destFile = new File(path + fileName);
                                FileUtils.copyFile(file, destFile);
                                //If profile was added to upload, directly add file to queue in order to avoid delays for user, else process as usual
                                if (profile != null) {
                                    processWithProfile(upFile, profile);
                                    return "profile";
                                } else {
                                    return ActionSupport.SUCCESS;
                                }
                            } catch (IOException e) {
                                throw new APEnetException("Error storing the file " + fileName + " in temporal up repository. Error: " + e.getMessage());
                            }
                        } else if (contentType.equals("zip")) {
                            log.info(SecurityContext.get() + "Uploading the zip file " + fileName + ". Unzipping...");
                            zipManager = new ZipManager(tempPath);
                            zipManager.unzip(fullFileName);

                            try {
                                FileUtils.forceDelete(theFile);
                            } catch (IOException e) {
                                throw new APEnetException("The file " + fileName + " could not be removed from tmp directory created to unzip a zipped file. Error: " + e.getMessage());
                            }

                            //If profile was added to upload, directly add file to queue in order to avoid delays for user, else process as usual
                            if (profile != null) {
                                moveToTemp(tempPath, path, "xml", filesNotUploaded, filesUploaded, archivalInstitutionId, uploadMethodString, profile);
                            } else {
                                moveToTemp(tempPath, path, "xml", filesNotUploaded, filesUploaded, archivalInstitutionId, uploadMethodString);
                            }

                            moveToTemp(tempPath, path, "xsl", filesNotUploaded, filesUploaded, archivalInstitutionId, uploadMethodString);
                            moveToTemp(tempPath, path, "other", filesNotUploaded, filesUploaded, archivalInstitutionId, uploadMethodString);

                            try {
                                FileUtils.deleteDirectory(tempDir);
                            } catch (IOException e) {
                                throw new APEnetException("The temporal directory " + tempPath + " could not be removed from tmp directory. Error: " + e.getMessage());
                            }

                            if (profile != null) {
                                return "profile";
                            } else {
                                return ActionSupport.SUCCESS;
                            }
                        }
                    } catch (APEnetException ape) {
                        log.error(SecurityContext.get() + ape.getMessage());
                        return ActionSupport.INPUT;
                    }
                }
            } else {
                //The format is not allowed
                log.warn(SecurityContext.get() + "The file " + fileName + " has a format not allowed. File not uploaded and removed automatically");
                filesNotUploaded.add(fileName);
                result = "success";
            }
        } else if (uploadType.equals("AL")) { //Upload Archival Landscape
            try {
                if ((contentType.equals("xml"))) {
                    ArchivalLandscapeUtils a = new ArchivalLandscapeUtils();
                    path = a.getmyPath(a.getmyCountry());
                    //Create a temporary file
                    fullFileName = path + a.getmyCountry() + "AL.xml"; //fileName
                    File theFile = new File(fullFileName);

                    //There's already an AL uploaded for this partner. Actually, it should always be an AL because is created when check if the file to upload is well-formed
                    if (theFile.exists() && (theFile.length() > 0)) {
                        String pathTemp = path + "temp" + APEnetUtilities.FILESEPARATOR;
                        File TempFile = new File(pathTemp);
                        if (!TempFile.exists()) {
                            TempFile.mkdir();
                        }

                        fullFileName = path + "temp" + APEnetUtilities.FILESEPARATOR + fileName;
                        File sfile = new File(fullFileName);
                        //sfile.mkdir();
                        FileUtils.copyFile(file, sfile);
                        log.info(SecurityContext.get() + "The file: " + fileName + " has been copied to: " + fullFileName);
                        //sfile.renameTo(new File(path + "temp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + "AL.xml"));
                        this.setArchivalInstitutionsToDelete(a.getArchivalInstitutionsToDelete());
                        this.setArchivalInstitutionsNameNotChanged(a.getArchivalInstitutionsNameNotChanged());
                        this.setArchivalInstitutionsNameChanged(a.getArchivalInstitutionsNameChanged());

                        result = "error";
                    } //There's no AL for this country. It has to copy in the repository.
                    else {
                        FileUtils.copyFile(file, theFile);
                        result = "success";
                        this.filesUploaded.add(fileName);
                        log.warn(SecurityContext.get() + "There were no file in the AL repository for " + a.getmyCountry() + ".");
                    }
                } else {
                    log.warn(SecurityContext.get() + "The file AL could not be uploaded because is not a xml");
                    this.filesNotUploaded.add(fileName);
                    result = "input";
                }
            } catch (Exception e) {
                log.error(SecurityContext.get() + "The file AL could not be uploaded. Some errors occurrs in process");
                result = "input";
            }

        } else {
            //HTTP EAG upload
            path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId.toString() + APEnetUtilities.FILESEPARATOR;
            try {
                if (contentType.equals("xml")) {

                    //The format is allowed
                    //The file is copied to /mnt/tmp/tmp/ai_id/
                    fullFileName = path + fileName;
                    File source = new File(fullFileName);
                    FileUtils.copyFile(file, source);

                    //It is necessary to validate the file against APEnet EAG schema
                    APEnetEAGDashboard eag = new APEnetEAGDashboard(archivalInstitutionId, file.getAbsolutePath());
                    Document tempDoc = null;
                    DocumentBuilder docBuilder = null;
                    DocumentBuilderFactory dbfac = null;
                    if (eag.validate()) {
                        // Check if any of the "<autform>" values is the same as the institution name.
                        ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(archivalInstitutionId);
                        String institutionName = archivalInstitution.getAiname();
                        List<String> autformValueList = eag.lookingForwardAllElementContent("/eag/archguide/identity/autform");
                        //Check if there are "<autform>" values with special characters
                        boolean specialCharacters = checkSpecialCharacter(autformValueList);
                        if (specialCharacters) {
                            this.filesNotUploaded.add(fileName);
                            return "error_eaginstitutionnamespecialcharacter";
                        }

                        boolean exists = false;
                        for (int i = 0; !exists && i < autformValueList.size(); i++) {
                            if (institutionName.equalsIgnoreCase(autformValueList.get(i))) {
                                exists = true;
                            }
                        }
                        if (!exists) {
                            this.filesNotUploaded.add(fileName);
                            return "error_eagnoinstitutionname";
                        }

                        //check the <recordId> content
                        //eag.setEagPath(fullFileName); //temp used for looking forward target tag
                        String recordIdValue = eag.lookingForwardElementContent("/eag/control/recordId");
                        boolean changed = false;
                        if (recordIdValue != null && recordIdValue.endsWith(MAGIC_KEY)) {
                            //replace value with a consecutive unique value
                            ArchivalLandscapeUtils archivalLandscape = new ArchivalLandscapeUtils();
                            int zeroes = 11 - archivalInstitutionId.toString().length();
                            String newRecordIdValue = archivalLandscape.getmyCountry() + "-";
                            for (int x = 0; x < zeroes; x++) {
                                newRecordIdValue += "0";
                            }
                            newRecordIdValue += archivalInstitutionId.toString();
                            dbfac = DocumentBuilderFactory.newInstance();
                            dbfac.setNamespaceAware(true);
                            docBuilder = dbfac.newDocumentBuilder();
                            tempDoc = docBuilder.parse(fullFileName);
                            NodeList recordsIds = tempDoc.getElementsByTagName("recordId");
                            for (int i = 0; i < recordsIds.getLength() && !changed; i++) {
                                Node currentNode = recordsIds.item(i);
                                Node parent = currentNode.getParentNode();
                                if (parent != null && parent.getNodeName().equals("control")) {
                                    parent = parent.getParentNode();
                                    if (parent != null && parent.getNodeName().equals("eag")) {
                                        currentNode.setTextContent(newRecordIdValue);
                                        changed = true;
                                    }
                                }
                            }
                        }
                        //TODO, change it with the check for webpages, use the eag.lookingForwardElemens
                        //from APEnetEAGDashboard class. issue #597, this step checks if the user has
                        //written good links into the system (which starts with right prefix).
                        String href = eag.extractAttributeFromEag("eag/relations/resourceRelation", "href", true);
                        boolean showWarnings = false;
                        if (href != null && !href.isEmpty() && !href.equals("error")) {
                            if (tempDoc == null) {
                                dbfac = DocumentBuilderFactory.newInstance();
                                dbfac.setNamespaceAware(true);
                                docBuilder = dbfac.newDocumentBuilder();
                                tempDoc = docBuilder.parse(fullFileName);
                            }
                            //first check resource relations
                            NodeList resourceRelations = tempDoc.getElementsByTagName("resourceRelation");
                            if (resourceRelations != null && resourceRelations.getLength() > 0) {
                                for (int i = 0; !showWarnings && i < resourceRelations.getLength(); i++) {
                                    Node resourceRelation = resourceRelations.item(i);
                                    if (resourceRelation.hasAttributes()) {
                                        NamedNodeMap resourceRelationsAttributes = resourceRelation.getAttributes();
                                        Node resourceRelationHrefAttribute = resourceRelationsAttributes.getNamedItem("href");
                                        if (resourceRelationHrefAttribute != null) {
                                            String hrefValue = resourceRelationHrefAttribute.getTextContent();
                                            if (hrefValue != null && !hrefValue.isEmpty() && !(href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("https://") || href.toLowerCase().startsWith("ftp://"))) {
                                                showWarnings = true; //marked to be used next to inform to user
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //String recordIdValue = eag.lookingForwardElementContent("/eag/control/recordId");
                        if (tempDoc == null) {
                            dbfac = DocumentBuilderFactory.newInstance();
                            dbfac.setNamespaceAware(true);
                            docBuilder = dbfac.newDocumentBuilder();
                            tempDoc = docBuilder.parse(fullFileName);
                        }
                        NodeList controlNodeList = ((Element)(tempDoc.getElementsByTagName("eag").item(0))).getElementsByTagName("control");
                        Node controlNode = controlNodeList.item(0);
                        if (controlNode != null){
                            ArchivalInstitution institution = DAOFactory.instance().getArchivalInstitutionDAO().findById(archivalInstitutionId);

                            NodeList rightsDeclarationNodeList =  ((Element)controlNode).getElementsByTagName("rightsDeclaration");
                            Element oldRightsDeclarationElement = null;
                            if (rightsDeclarationNodeList.getLength() > 0){
                                oldRightsDeclarationElement = (Element) (rightsDeclarationNodeList.item(0));
                            }
                            String defaultNS = "http://www.archivesportaleurope.net/Portal/profiles/eag_2012/";
                            Element newRightsDeclarationElement = tempDoc.createElementNS(defaultNS,"rightsDeclaration");
                            newRightsDeclarationElement.setAttributeNS("http://www.w3.org/XML/1998/namespace","xml:lang", "eng");
                            Element abbreviationElement = tempDoc.createElementNS(defaultNS,"abbreviation");
                            abbreviationElement.appendChild(tempDoc.createTextNode(shareWithWikimediaRightsInformation.getAbbreviation()));
                            Element citationElement = tempDoc.createElementNS(defaultNS,"citation");
                            citationElement.appendChild(tempDoc.createTextNode(shareWithWikimediaRightsInformation.getRightsName()));
                            citationElement.setAttribute("href", shareWithWikimediaRightsInformation.getLink());
                            Element descriptiveNoteElement = tempDoc.createElementNS(defaultNS,"descriptiveNote");
                            Element p1Element = tempDoc.createElementNS(defaultNS,"p");
                            p1Element.appendChild(tempDoc.createTextNode(shareWithWikimediaRightsInformation.getDescription()));
                            Element p2Element = tempDoc.createElementNS(defaultNS,"p");
                            p2Element.appendChild(tempDoc.createTextNode("The rights holder is the " + institution.getAiname()));
                            descriptiveNoteElement.appendChild(p1Element);
                            descriptiveNoteElement.appendChild(p2Element);

                            newRightsDeclarationElement.appendChild(abbreviationElement);
                            newRightsDeclarationElement.appendChild(citationElement);
                            newRightsDeclarationElement.appendChild(descriptiveNoteElement);

                            if (oldRightsDeclarationElement != null) {
                                controlNode.replaceChild(newRightsDeclarationElement, oldRightsDeclarationElement);
                            }
                            else {
                                NodeList nodeList =  ((Element)controlNode).getElementsByTagName("localControl");
                                if (nodeList != null && nodeList.getLength()>0){
                                    controlNode.insertBefore(newRightsDeclarationElement, nodeList.item(0));
                                }
                                else {
                                    nodeList =  ((Element)controlNode).getElementsByTagName("localTypeDeclaration");
                                    if (nodeList != null && nodeList.getLength()>0){
                                        controlNode.insertBefore(newRightsDeclarationElement, nodeList.item(0));
                                    }
                                    else {
                                        nodeList =  ((Element)controlNode).getElementsByTagName("publicationStatus");
                                        if (nodeList != null && nodeList.getLength()>0){
                                            controlNode.insertBefore(newRightsDeclarationElement, nodeList.item(0));
                                        }
                                        else {
                                            nodeList =  ((Element)controlNode).getElementsByTagName("sources");
                                            if (nodeList != null && nodeList.getLength()>0){
                                                controlNode.insertBefore(newRightsDeclarationElement, nodeList.item(0));
                                            }
                                            else {
                                                controlNode.appendChild(newRightsDeclarationElement);
                                            }
                                        }
                                    }
                                }
                            }

                            changed = true;
                        }

                        if (changed) {
                            TransformerFactory tf = TransformerFactory.newInstance(); // Save changes
                            Transformer transformer = tf.newTransformer();
                            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                            transformer.transform(new DOMSource(tempDoc), new StreamResult(new File(fullFileName)));
                        }
                        //The EAG has been validated so it has to be stored in /mnt/repo/country/aiid/EAG/
                        //and it is necessary to update archival_institution table
                        result = eag.saveEAGviaHTTP(fullFileName, shareWithWikimediaRightsInformation, doNotShowPopupAgain);

                        if (result.equals("error_eagnotstored")) {
                            this.filesNotUploaded.add(fileName);
                            result = "error_eagnotstored";
                        } // Issue #615 remove the check.
                        //        				else if (result.equals("error_eagalreadyuploaded")) {
                        //        					this.filesNotUploaded.add(fileName);
                        //        					result = "error_eagalreadyuploaded";
                        //        				}
                        else if (result.equals("error_archivallandscape")) {
                            this.filesNotUploaded.add(fileName);
                            result = "error_archivallandscape";
                        } else {
                            this.filesUploaded.add(fileName);
                            if (showWarnings) {
                                result = "success_with_url_warning";
                            } else /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
                            /////////////////////////////////////////////////////////////
                            // Remove this block and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
                            //            				if (false) {
                            //            					//The EAG file should contain the default message added in case Information is empty
                            //            					result = "success_noInformation";
                            //            				}
                            //            				else {
                            {
                                result = "success";

                                // Try to remove the previous temp file.
                                if (archivalInstitution != null) {
                                    String eagPath = archivalInstitution.getEagPath();
                                    String tempEagPath = APEnetUtilities.getConfig().getRepoDirPath();
                                    String eagTempPath = tempEagPath + eagPath.substring(0, (eagPath.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1)) + Eag2012.EAG_TEMP_FILE_NAME;
                                    File fileTempEag = new File(eagTempPath);
                                    if (fileTempEag.exists()) {
                                        try {
                                            FileUtils.forceDelete(fileTempEag);
                                        } catch (IOException e) {
                                            log.error(e.getMessage(), e);
                                        }
                                    }
                                }

                                Eag2012GeoCoordinatesAction eag2012GeoCoordinatesAction = new Eag2012GeoCoordinatesAction();
                                eag2012GeoCoordinatesAction.insertCoordinates(archivalInstitution);
                            }

                        }

                    } else {
                        //Check if the file is an old EAG
                        dbfac = DocumentBuilderFactory.newInstance();
                        dbfac.setNamespaceAware(true);
                        docBuilder = dbfac.newDocumentBuilder();
                        tempDoc = docBuilder.parse(fullFileName);
                        NodeList eagheader = tempDoc.getElementsByTagName("eagheader");
                        if (eagheader != null && eagheader.getLength() > 0) {
                            this.filesUploaded.add(fileName);
                            result = "display_eag02convertedToeag2012";
                        } else {
                            warnings_eag = eag.showWarnings();
                            //The EAG has been neither validated nor converted
                            log.warn(SecurityContext.get() + "The file " + fileName + " is not valid");
                            this.filesNotUploaded.add(fileName);
                            result = "error_eagnotvalidatednotconverted";
                        }
                    }

                    if (!result.equalsIgnoreCase("display_eag02convertedToeag2012")) {
                        FileUtils.forceDelete(source);
                    }

                } else {
                    //The format is not allowed
                    log.warn(SecurityContext.get() + "The file type is not XML. Format not allowed.");
                    this.filesNotUploaded.add(fileName);
                    result = "error_formatnotallowed";
                }

            } catch (SAXException e) {
                log.error(SecurityContext.get() + e.getMessage());
                warnings_eag = new ArrayList<String>();
                warnings_eag.add("The file uploaded is not a correct XML, please see the following exception to know the problem:<br/>" + e.getMessage());
                this.filesNotUploaded.add(fileName);
                result = "error_parsing";
            } catch (Exception e) {
                log.error(SecurityContext.get() + e.getMessage(), e);
                this.filesNotUploaded.add(fileName);
                result = "error_eagnotstored";
            }
        }
        return result;
    }

    /**
     * Check special characters in the institution's name and alternative's name
     *
     * @param
     * @return true if there are specials characters and false in other case
     */
    private boolean checkSpecialCharacter(List<String> autformValueList) {
        // TODO Auto-generated method stub
        for (int i = 0; i < autformValueList.size(); i++) {
            if (autformValueList.get(i).contains("<") || autformValueList.get(i).contains(">") || autformValueList.get(i).contains("%")
                    || autformValueList.get(i).contains(":") || autformValueList.get(i).contains("\\")) {
                return true;
            }
        }
        return false;
    }

    //Jara: Overwrite a file of Archival Landscape. The existing file is renamed to name_old.
    public String overWriteFile(File file, String fileName, String pathFile, boolean execute) {

        String result;
        ArchivalLandscapeUtils a = new ArchivalLandscapeUtils();
        String fullFileName = "";
        String tmpDirectory = pathFile + "temp" + APEnetUtilities.FILESEPARATOR;
        File[] files = new File(pathFile).listFiles();
        this.filesNotUploaded = new ArrayList<String>();
        this.filesUploaded = new ArrayList<String>();

        try {
            File sfile = new File(pathFile + fileName);
            //Change the database without commit
            String resultStore = a.storeArchives(sfile, execute);

            //this.setFasDeleted(a.getFasDeleted());
            //this.setHgsDeleted(a.getHgsDeleted());
            //Change the repository renaming the files and not deleted the current one
            if (!resultStore.equals("error")) {
                if (files.length > 1) {
                    /*for (int n=0;n< files.length;n++)
                     {
                     if (files[n].getName().contains("AL.xml"))
                     fullFileName = pathFile + files[n].getName();
                     }*/
                    fullFileName = pathFile + a.getmyCountry() + "AL.xml";
                }
                File theFile = new File(fullFileName);
                //theFile.delete();
                //theFile.renameTo(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL_old.xml"));

                FileUtils.copyFile(sfile, new File(fullFileName));
                //theFile.renameTo(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL.xml"));
                File tmpDir = new File(tmpDirectory);
                if (fileName != null) {
                    this.filesUploaded.add(fileName);
                }
                FileUtils.deleteDirectory(tmpDir);

                this.setArchivalInstitutionsToDelete(a.getArchivalInstitutionsToDelete());
                this.setArchivalInstitutionsToInsert(a.getArchivalInstitutionsToInsert());
                this.setArchivalInstitutionsNameNotChanged(a.getArchivalInstitutionsNameNotChanged());
                this.setArchivalInstitutionsNameChanged(a.getArchivalInstitutionsNameChanged());
                this.setArchivalInstitutionsParentChanged(a.getArchivalInstitutionsParentChanged());
                this.setArchivalInstitutionsParentNotChanged(a.getArchivalInstitutionsParentNotChanged());
                result = "success";
            } else {
                result = "error";
                if (files.length > 1) {
                    for (int n = 0; n < files.length; n++) {
                        if (files[n].getName().contains(".xml")) {
                            fullFileName = pathFile + files[n].getName();
                        }
                    }
                }
                //File theFile = new File(fullFileName);
                //theFile.delete();
                //theFile.renameTo(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL_old.xml"));

                //FileUtils.copyFile(sfile, new File(fullFileName));
                //theFile.renameTo(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL.xml"));
                File tmpDir = new File(tmpDirectory);
                if (fileName != null) {
                    this.filesUploaded.add(fileName);
                }
                FileUtils.deleteDirectory(tmpDir);
            }

        } catch (IOException e) {
            result = "error";
            this.filesNotUploaded.add(fileName);
            log.debug("The file could not be remove. Some errors occurs in process.");
            log.error(e.getMessage());
        }

        return result;
    }

    private void moveToTemp(String tempPath, String path, String format, List<String> filesNotUploaded, List<String> filesUploaded, Integer archivalInstitutionId, String uploadMethodString) {
        moveToTemp(tempPath, path, format, filesNotUploaded, filesUploaded, archivalInstitutionId, uploadMethodString, null);
    }

    // This method moves all the files extracted from a Zip file to the previous directory (the root directory for the files recently uploaded)
    private void moveToTemp(String tempPath, String path, String format, List<String> filesNotUploaded, List<String> filesUploaded, Integer archivalInstitutionId, String uploadMethodString, Ingestionprofile profile) {
        File dir = new File(tempPath);
        if (format.equals("other")) {
            String[] files = dir.list();
            for (String fileStr : files) {
                File srcFile = new File(tempPath + fileStr);
                filesNotUploaded.add(fileStr);
                try {
                    FileUtils.forceDelete(srcFile);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        } else {
            String[] files = dir.list(new SuffixFileFilter(format, IOCase.INSENSITIVE));
            final String defaultDirPath = APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR;
            for (String fileStr : files) {
                File srcFile = new File(tempPath + fileStr);

                fileStr = APEnetUtilities.convertToFilename(fileStr);
                File destFile = new File(path + fileStr);
                if (destFile.exists()) {
                    // The file already exists in the upload directory. It's necessary to notify to the user
                    log.warn("File already exists in destination folder: " + destFile.getPath());
                    filesNotUploaded.add(fileStr);
                    try {
                        FileUtils.forceDelete(srcFile);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                } else {
                    try {
                        // Moving the file
                        // Insert file uploaded into up_file table
                        JpaUtil.beginDatabaseTransaction();

                        UpFile upFile;
                        if (format.equals("xml")) {
                            upFile = createUpFile(defaultDirPath, fileStr, uploadMethodString, archivalInstitutionId, FileType.XML);
                        } else if (format.equals("xsl")) {
                            upFile = createUpFile(defaultDirPath, fileStr, uploadMethodString, archivalInstitutionId, FileType.XSL);
                        } else {
                            upFile = createUpFile(defaultDirPath, fileStr, uploadMethodString, archivalInstitutionId, FileType.ZIP);
                        }

                        DAOFactory.instance().getUpFileDAO().insertSimple(upFile);

                        // The file is stored in temp repository and it is necessary to move it
                        log.info("Moving file " + srcFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
                        FileUtils.moveFile(srcFile, destFile);
                        filesUploaded.add(fileStr);

                        JpaUtil.commitDatabaseTransaction();

                        //If profile was added to upload, directly add file to queue in order to avoid delays for user
                        if (profile != null) {
                            processWithProfile(upFile, profile);
                        }
                    } catch (Exception e) {
                        log.error("Error inserting the file " + fileStr + " in up_table (the user was uploading this file to the Dashboard) or error storing the file in temporal up repository [Database and FileSystem Rollback]. Error: " + e.getMessage());
                        JpaUtil.rollbackDatabaseTransaction();
                        // FileSystem Rollback //todo: For Inteco: Shouldn't it be destFile to be erased if it exists?
                        if (srcFile.exists()) {
                            try {
                                FileUtils.forceDelete(srcFile);
                            } catch (IOException e1) {
                                log.error("It was not possible to remove the file " + srcFile.getAbsolutePath() + ". Error: " + e1.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    private UpFile createUpFile(String upDirPath, String filePath, String uploadMethodString, Integer aiId, FileType fileType) {
        UpFile upFile = new UpFile();
        upFile.setFilename(filePath);
        upFile.setPath(upDirPath);

        UploadMethod uploadMethod = DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(uploadMethodString);
        upFile.setUploadMethod(uploadMethod);

        upFile.setAiId(aiId);

        upFile.setFileType(fileType);

        return upFile;
    }

    private static Properties retrieveProperties(Ingestionprofile ingestionprofile) {
        Properties properties = new Properties();
        properties.setProperty(QueueItem.XML_TYPE, ingestionprofile.getFileType() + "");
        properties.setProperty(QueueItem.NO_EADID_ACTION, ingestionprofile.getNoeadidAction().getId() + "");
        properties.setProperty(QueueItem.EXIST_ACTION, ingestionprofile.getExistAction().getId() + "");
        properties.setProperty(QueueItem.DAO_TYPE, ingestionprofile.getDaoType().getId() + "");
        properties.setProperty(QueueItem.DAO_TYPE_CHECK, ingestionprofile.getDaoTypeFromFile() + "");
        properties.setProperty(QueueItem.PERSIST_EACCPF_FROM_EAD3, ingestionprofile.getExtractEacFromEad3() + "");
        properties.setProperty(QueueItem.UPLOAD_ACTION, ingestionprofile.getUploadAction().getId() + "");
        properties.setProperty(QueueItem.CONVERSION_TYPE, ingestionprofile.getEuropeanaConversionType() + "");
        properties.setProperty(QueueItem.DATA_PROVIDER, ingestionprofile.getEuropeanaDataProvider() + "");
        properties.setProperty(QueueItem.DATA_PROVIDER_CHECK, ingestionprofile.getEuropeanaDataProviderFromFile() + "");
        properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE, ingestionprofile.getEuropeanaDaoType() + "");
        properties.setProperty(QueueItem.EUROPEANA_DAO_TYPE_CHECK, ingestionprofile.getEuropeanaDaoTypeFromFile() + "");
        properties.setProperty(QueueItem.LANGUAGES_MATERIAL, ingestionprofile.getEuropeanaLanguagesMaterial() + "");
        properties.setProperty(QueueItem.LANGUAGES_MATERIAL_CHECK, ingestionprofile.getEuropeanaLanguagesMaterialFromFile() + "");
        properties.setProperty(QueueItem.LANGUAGE_DESCRIPTION, ingestionprofile.getEuropeanaLanguageDescription() + "");
        properties.setProperty(QueueItem.LANGUAGE_DESCRIPTION_CHECK, ingestionprofile.getEuropeanaLanguageDescriptionFromFile() + "");
        properties.setProperty(QueueItem.LANGUAGE_DESCRIPTION_SAME_AS_MATERIAL_CHECK, ingestionprofile.getEuropeanaLanguageMaterialDescriptionSame() + "");
        properties.setProperty(QueueItem.LICENSE_CHECK, ingestionprofile.getEuropeanaLicenseFromFile() + "");
        properties.setProperty(QueueItem.LICENSE, ingestionprofile.getEuropeanaLicense() + "");
        properties.setProperty(QueueItem.LICENSE_DETAILS, ingestionprofile.getEuropeanaLicenseDetails() + "");
        properties.setProperty(QueueItem.LICENSE_ADD_INFO, ingestionprofile.getEuropeanaAddRights() + "");
        properties.setProperty(QueueItem.INHERIT_FILE_CHECK, ingestionprofile.getEuropeanaInheritElementsCheck()+"");
        properties.setProperty(QueueItem.INHERIT_FILE, ingestionprofile.getEuropeanaInheritElements()+"");
        properties.setProperty(QueueItem.INHERIT_ORIGINATION_CHECK, ingestionprofile.getEuropeanaInheritOriginCheck()+"");
        properties.setProperty(QueueItem.INHERIT_ORIGINATION, ingestionprofile.getEuropeanaInheritOrigin()+"");
        properties.setProperty(QueueItem.INHERIT_UNITTITLE_CHECK, ingestionprofile.getEuropeanaInheritUnittitleCheck()+"");
        properties.setProperty(QueueItem.INHERIT_UNITTITLE, ingestionprofile.getEuropeanaInheritUnittitle()+"");
        properties.setProperty(QueueItem.SOURCE_OF_IDENTIFIERS, ingestionprofile.getSourceOfIdentifiers()+"");
        properties.setProperty(QueueItem.RIGHTS_OF_DIGITAL_OBJECTS, ingestionprofile.getRightsOfDigitalObjects() + "");
        properties.setProperty(QueueItem.RIGHTS_OF_DIGITAL_OBJECTS_TEXT, ingestionprofile.getRightsOfDigitalObjectsText() + "");
        properties.setProperty(QueueItem.RIGHTS_OF_DIGITAL_DESCRIPTION, ingestionprofile.getRightsOfDigitalDescription() + "");
        properties.setProperty(QueueItem.RIGHTS_OF_DIGITAL_HOLDER, ingestionprofile.getRightsOfDigitalHolder() + "");
        properties.setProperty(QueueItem.RIGHTS_OF_EAD_DATA, ingestionprofile.getRightsOfEADData() + "");
        properties.setProperty(QueueItem.RIGHTS_OF_EAD_DATA_TEXT, ingestionprofile.getRightsOfEADDataText() + "");
        properties.setProperty(QueueItem.RIGHTS_OF_EAD_DESCRIPTION, ingestionprofile.getRightsOfEADDescription() + "");
        properties.setProperty(QueueItem.RIGHTS_OF_EAD_HOLDER, ingestionprofile.getRightsOfEADHolder() + "");
        if (ingestionprofile.getXslUpload() != null) {
            properties.setProperty(QueueItem.XSL_FILE, ingestionprofile.getXslUpload().getName());
        }
        return properties;
    }

    private void processWithProfile(UpFile upFile, Ingestionprofile profile) {
        Properties properties = retrieveProperties(profile);
        try {
            if (profile.getFileType() == 2) {
                EacCpfService.useProfileAction(upFile, properties);
            } else if (profile.getFileType() == XmlType.EAD_3.getIdentifier()) {
                Ead3Service.useProfileAction(upFile, properties);
            } else {
                EadService.useProfileAction(upFile, properties);
            }
        } catch (Exception ex) {
            log.error("Failed when adding the new up files into the queue", ex);
        }
    }

}
