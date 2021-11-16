package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import eu.apenet.persistence.vo.RightsInformation;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.eag.EagService;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Eloy García
 * Date: Sep 23d, 2010
 */
/**
 * This class is in charge of managing each EAG file uploaded or edited by a
 * partner. This class gathers all the operations a partner can do within the
 * Dashboard Content Provider Information option
 */
public class APEnetEAGDashboard{

	// Attributes
	private final Logger log = Logger.getLogger(getClass());

	private String eagPath;
	protected Integer aiId;
	private String id;

	protected List<String> warnings_ead;
	private String name;

	public String getEagPath() {
		return eagPath;
	}

	public void setEagPath(String eagPath) {
		this.eagPath = eagPath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilename() {
		return APEnetUtilities.convertToFilename(this.getId()) + ".xml";
	}

	public Integer getAiId() {
		return aiId;
	}

	public void setAiId(Integer aiId) {
		this.aiId = aiId;
	}

	public List<String> getWarnings_ead() {
		return warnings_ead;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Constructor
	public APEnetEAGDashboard(Integer aiId, String tempEagPath) {
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aiId);

        this.eagPath = archivalInstitution.getEagPath();

		this.aiId = aiId;

		if (this.eagPath == null){
			if (tempEagPath != null) {
	        	this.eagPath = tempEagPath;
	        }
			this.id = "";
			this.name = "";
			if (archivalInstitution != null) {
				this.name = archivalInstitution.getAiname().toString();
			}
		}
		else {
			if (tempEagPath != null) {
	        	this.eagPath = tempEagPath;
	        }
	        else {
				this.eagPath = APEnetUtilities.getConfig().getRepoDirPath() + this.eagPath;
	        }
		}
	}

	// Methods
	public boolean validate() throws APEnetException, SAXException {
		warnings_ead = new ArrayList<String>();
		// EAG file is stored temporally in the location defined in eagPath
		// attribute
		log.debug("Path of EAG: " + this.getEagPath());
		File file = new File(this.getEagPath());
		// Xsd_enum schema = Xsd_enum.XSD_APE_EAG_SCHEMA; //todo: Now we use EAG
		// 2012 for this, but it needs to be completed
		Xsd_enum schema = Xsd_enum.XSD_EAG_2012_SCHEMA;
		try {
			InputStream in = new FileInputStream(file);
			List<SAXParseException> exceptions = DocumentValidation.xmlValidation(in, schema);
			if (exceptions != null) {
				StringBuilder warn;
				for (SAXParseException exception : exceptions) {
					warn = new StringBuilder();
					warn.append("l.").append(exception.getLineNumber()).append(" c.")
							.append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
							.append("<br/>");
					warnings_ead.add(warn.toString());
				}
				return false;
			}
		} catch (SAXException e) {
			throw e;
		} catch (Exception e) {
			throw new APEnetException("Exception while validating an EAG file", e);
		}
		ArchivalInstitution institution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
		//String pattern = institution.getCountry().getIsoname() + "-[a-zA-Z0-9:/\\-]{1,11}";
		String pattern = "(AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BA|BW|BV|BR|IO|BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|IL|IT|JM|JP|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|FM|MD|MC|MN|MS|MA|MZ|MM|NA|NR|NP|NL|AN|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|RO|RU|RW|SH|KN|LC|PM|VC|WS|SM|ST|SA|SN|CS|SC|SL|SG|SK|SI|SB|SO|ZA|GS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW|RS|ME|EU|EUR|IM|XX)-[a-zA-Z0-9:/\\-]{1,11}";
		String repositoryCode = this.lookingForwardElementContent("/eag/control/recordId");
		boolean validRepositoryCode = Pattern.matches(pattern, repositoryCode);
		if (validRepositoryCode) {
			if (DAOFactory.instance().getArchivalInstitutionDAO().isRepositoryCodeAvailable(repositoryCode, aiId)) {
				return true;
			} else {
				warnings_ead.add("recordId: " + repositoryCode + " already used");
				return false;
			}
		} else {
			warnings_ead.add("recordId does not match pattern: " + pattern);
			return false;
		}
	}

	public Boolean convertToAPEnetEAG() throws APEnetException {
		// EAG file is stored temporally in the location defined in eagPath
		// attribute
		File file = new File(this.getEagPath());
		try {
			InputStream in;
			final String xslfilename = "changeNS.xsl";
			File outputfile = new File(file.getParentFile(), "converted_" + file.getName());
			String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
					+ APEnetUtilities.FILESEPARATOR + xslfilename;
			in = new FileInputStream(file);
			TransformationTool.createTransformation(in, outputfile, new File(xslFilePath),
					null, true, true, null, true, null);
			in.close();
			FileUtils.copyFile(outputfile, file);
		} catch (Exception e) {
			throw new APEnetException("Exception while converting in APEnet EAG", e);
		}
		return true;
	}

	public Boolean convertEAG02ToEAG2012() throws APEnetException {
		// EAG file is stored temporally in the location defined in eagPath
		// attribute
		File file = new File(this.getEagPath());
		File outputfile = null;
		try {
			InputStream in;
			final String xslfilename = "eag2eag2012.xsl";
			outputfile = new File(file.getParentFile(), "converted_" + file.getName());
			String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
					+ APEnetUtilities.FILESEPARATOR + xslfilename;
			in = new FileInputStream(file);
			TransformationTool.createTransformation(in, outputfile, new File(xslFilePath),
					null, true, true, null, true, null);
			in.close();
			FileUtils.copyFile(outputfile, file);
		} catch (Exception e) {
			throw new APEnetException("Exception while converting in APEnet EAG", e);
		} finally {
			if (outputfile != null) {
				try {
					FileUtils.forceDelete(outputfile);
				} catch (IOException e) {
					log.error("Error deleting file: " + outputfile.getName());
				}
			}
		}
		return true;
	}

	public List<String> showWarnings() {
		return warnings_ead;
	}

	// This method stores a new EAG file or overwrites an existing one which has
	// been uploaded via HTTP
	public String saveEAGviaHTTP(String sourcePath) {
		return saveEAGviaHTTP(sourcePath, null, null);
	}

	public String saveEAGviaHTTP(String sourcePath, RightsInformation shareWithWikimediaRightsInformation, Boolean doNotShowPopupAgain) {
		String value = "";
		Integer overwrittingEAGProcess = 0;
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.aiId);

		// It is necessary to build the path
		this.setEagPath(sourcePath);
		this.setId(this.extractAttributeFromEag("control/recordId", null, true));
		this.setName(this.extractAttributeFromEag("archguide/identity/autform", null, true));
		
		// It is necessary to check if this EAG has been updated before for
		// another archival institution
		// TODO: Issue #615: remove the check.
//		if (this.isEagAlreadyUploaded()) {
//			value = "error_eagalreadyuploaded";
//		} else {
			this.setEagPath(APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname()
					+ APEnetUtilities.FILESEPARATOR + this.aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG"
					+ APEnetUtilities.FILESEPARATOR + this.getFilename());
			String storagePath = APEnetUtilities.getConfig().getRepoDirPath() + this.getEagPath();
			String oldEAGPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR
					+ archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR
					+ this.aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR
					+ "_remove" + getFilename();
			File source = new File(sourcePath);
			File destination = new File(storagePath);

			try {
				// Begin transaction with Database
				JpaUtil.beginDatabaseTransaction();

				// The new path, autform and repositorycode are stored in
				// archival_institution table
				Date dateNow = new Date();
				archivalInstitution.setRegistrationDate(dateNow);
				archivalInstitution.setEagPath(this.getEagPath());
				archivalInstitution.setAutform(this.getName());
				archivalInstitution.setRepositorycode(this.getId());
				archivalInstitutionDao.insertSimple(archivalInstitution);
				if (shareWithWikimediaRightsInformation != null){
					archivalInstitution.setShareWithWikimedia(shareWithWikimediaRightsInformation);
					archivalInstitution.setShareWithWikimediaId(shareWithWikimediaRightsInformation.getId());
				}
				archivalInstitution.setDoNotShowPopup(doNotShowPopupAgain);

				overwrittingEAGProcess = 1;

				/// UPDATE FILE SYSTEM ///

				// Rename the old EAG to _remove...
				log.debug("Renaming the EAG to _remove for the archival institution with id: " + this.getAiId());
				ContentUtils.renameFileToRemove(storagePath);

				overwrittingEAGProcess = 2;

				// Copy the new EAG
				FileUtils.copyFile(source, destination);

				overwrittingEAGProcess = 3;

				/// FINAL COMMITS ///

				// Final commit in the File system
				// It is necessary to remove the old EAG
				ContentUtils.deleteFile(oldEAGPath);

				// Final commit in Database
				JpaUtil.commitDatabaseTransaction();

				log.info(SecurityContext.get() + "The EAG " + this.getEagPath()
						+ " has been created and stored in repository");
				EagService.publish(archivalInstitution);
				value = "correct";
			} catch (Exception e) {
				value = "error_eagnotstored";

				if (overwrittingEAGProcess == 0) {
					// There were errors during Database Transaction
					// It is necessary to make a Database rollback
					JpaUtil.rollbackDatabaseTransaction();
					JpaUtil.closeDatabaseSession();
					log.error(
							"There were errors during Database Transaction while uploading a new EAG via HTTP protocol for archival institution "
									+ this.getAiId(), e);
				}

				if (overwrittingEAGProcess == 1) {
					// There were errors during File System updating
					// It is necessary to make a Database rollback
					JpaUtil.rollbackDatabaseTransaction();
					JpaUtil.closeDatabaseSession();

					// It is necessary to make a File System rollback
					ContentUtils.rollbackRenameFileFromRemove(oldEAGPath);

					log.error(
							"There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution "
									+ this.getAiId(), e);
				}

				if (overwrittingEAGProcess == 2) {
					// There were errors during File System updating
					// It is necessary to make a Database rollback
					JpaUtil.rollbackDatabaseTransaction();
					JpaUtil.closeDatabaseSession();

					// It is necessary to make a File System rollback
					// Removing the new EAG for restoring the old one
					try {
						ContentUtils.deleteFile(destination.getAbsolutePath());
					} catch (Exception ex) {
						log.error(
								"There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution "
										+ this.getAiId() + ". Error removing the new EAG "
										+ destination.getAbsolutePath(), e);
					}

					ContentUtils.rollbackRenameFileFromRemove(oldEAGPath);

					log.error("There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution "
							+ this.getAiId());
				}

				if (overwrittingEAGProcess == 3) {
					// There were errors during Database, Index or File
					// system commit
					JpaUtil.closeDatabaseSession();
					log.error(
							"FATAL ERROR. Error during Database or File System commits when an EAG file was uploading via HTTP protocol. Please, check inconsistencies in Database and File system for archival institution which id is: "
									+ this.getAiId(), e);
				}
				log.error(e.getMessage());
			}
//		}

		return value;
	}

	/* TODO: Issue #615: remove the check.
	private Boolean isEagAlreadyUploaded() {
		// It will be necessary to check eagid and autform values in
		// archival_institution table
		Boolean eagAlreadyUploaded = false;

		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> archivalInstitutionList = archivalInstitutionDao
				.getArchivalInstitutionsByAutform(this.getName());
		if (archivalInstitutionList.size() > 0) {
			// There is at least one institution with the same EAG uploaded
			for (int i = 0; i < archivalInstitutionList.size(); i++) {
				// If the institution found is the same that the institution
				// which wants to change the EAG file, then the operation is
				// permitted
				if (archivalInstitutionList.get(i).getAiId() != this.getAiId()) {
					log.warn(SecurityContext.get() + "EAG uploaded that contains the name " + this.getName() + " that already exist in another archival institution with id: " + archivalInstitutionList.get(i).getAiId());
					eagAlreadyUploaded = true;
				}
			}
		}
		warnings_ead.add("Archival institution name already used: " + this.getName() + " already used");
		
		return eagAlreadyUploaded;
	} */

	//This method extracts the content within a tag inside an EAG (the first one) or
	//the content within an attribute for the tag
	public String extractAttributeFromEag(String element, String attribute, boolean isReturningFirstInstance) {
        final String CONVERTED_FLAG = "Converted_APEnet_EAG_version_";
        XMLStreamReader input = null;
	    InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {
            sfile = new FileInputStream(this.eagPath);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);

            boolean abort = false;
            boolean isInsideElement = false;
            boolean isInsidePath = false;
            boolean wasInsidePath = false;
		    String importantData = "";

            String[] pathElements = element.split("/");
            int lenghtPath = pathElements.length;
            int pointerPath = 0;

            log.debug("Checking EAG file, looking for element " + element + ", and attribute " + ((attribute==null)?"null":attribute) + ", path begins with " + pathElements[0]);
            while (!abort && input.hasNext()) {
                switch (input.getEventType()) {
					case XMLEvent.START_DOCUMENT:
						break;
					case XMLEvent.START_ELEMENT:
                        if(input.getLocalName().equals(pathElements[pointerPath])){
                            isInsidePath = true;
                            wasInsidePath = true;
                            if(pointerPath == lenghtPath-1){
                                isInsideElement = true;
                                if(attribute != null){
                                    for(int attributeNb = 0; attributeNb < input.getAttributeCount(); attributeNb++){
                                        if(input.getAttributeLocalName(attributeNb).equals(attribute)){
                                            log.debug("Returning " + input.getAttributeValue(attributeNb));
                                            return input.getAttributeValue(attributeNb);
                                        }
                                    }
                                    log.debug("Returning error");
                                    return "error";
                                }
                            }
                            pointerPath++;
                        }
						break;
					case XMLEvent.CHARACTERS:
						if (isInsideElement) {
							importantData = input.getText();
							if (importantData.startsWith(CONVERTED_FLAG)) {
								return "true";
							} else if(isReturningFirstInstance){
                                return importantData;
                            }
						}
						break;
					case XMLEvent.CDATA:
						break;
					case XMLEvent.END_ELEMENT:
                        if(isInsidePath && input.getLocalName().equals(pathElements[pointerPath-1])){
                            pointerPath--;
                            isInsideElement = false;
                            if(pointerPath == 0)
                                isInsidePath = false;
                        }
                        if(!isInsidePath && wasInsidePath)
                            abort = true;
						break;
                }
                if (input.hasNext())
                    input.next();
            }
        } catch (Exception e) {
            log.error("Error parsing StAX for file " + this.eagPath, e);
        } finally {
            try {
                input.close();
                sfile.close();
            } catch (Exception e) {
                log.error("Error closing streams" + e.getMessage(), e);
            }
        }
        log.debug("Returning error");
        return "error";
	}

	//This method returns all the same elements of an EAG xml file found
	public String lookingForward(String element,String attribute,String value){
		XMLStreamReader input = null;
	    InputStream sfile = null;
	    XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
	    xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
	    xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
	    xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
	    xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
	    try {
	        sfile = new FileInputStream(this.eagPath);
	        input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);
	        String[] pathElements = element.split("/");
	        while (input.hasNext()) {
	            switch (input.getEventType()) {
					case XMLEvent.START_ELEMENT:
						for(int i=0;i<pathElements.length;i++){
							if(input.getLocalName().equals(pathElements[i])){ //***********//
								for(int x=0;x<input.getAttributeCount();x++){
									if(input.getAttributeLocalName(x).equals(attribute) && input.getAttributeValue(x).equals(value)){
										input.close();
							            sfile.close();
										return value;
									}
								}
							}
						}
						break;
	            }
	            if (input.hasNext()){
	                input.next();
	            }
	        }
	        return "error"; //The element is not found
	    } catch (Exception e) {
	        log.error("Error parsing StAX for file " + this.eagPath, e);
	    } finally {
	        try {
	            input.close();
	            sfile.close();
	        } catch (Exception e) {
	            log.error("Error closing streams" + e.getMessage(), e);
	        }
	    }
	    log.debug("Returning error");
	    return "error"; //Error
	}

	public String lookingForwardElementContent(String element) {
		String text = null;
        XMLStreamReader input = null;
	    InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {
            sfile = new FileInputStream(this.eagPath);
            input = xmlif.createXMLStreamReader(sfile);

            boolean exit = false;
            boolean found = true;
            String[] pathElements = null;
            if(input!=null && element!=null){
            	if(element.contains("/")){ //check input parameters
            		if(element.startsWith("/")){ 
            			element = element.substring(1);
            		}
            		if(element.endsWith("/") && element.length()>2){
            			element = element.substring(0,element.length()-2);
            		}
            		pathElements = element.split("/");
            	}else{
            		pathElements = new String[1];
            		pathElements[0] = element;
            	}
                List<String> currentElement = new ArrayList<String>();
                log.debug("Checking EAG file, looking for element " + element + ", path begins with " + pathElements[0]);
                while (!exit && input.hasNext()) {
                	switch (input.getEventType()) {
                	case XMLEvent.START_ELEMENT:
                		currentElement.add(input.getLocalName().toString());
                		if(currentElement.size()==pathElements.length){
                			found = true;
                			for(int i=0;i<pathElements.length && found;i++){
                				found = (pathElements[i].trim().equals(currentElement.get(i).trim()));
                			}
                			text = "";
                		}
                		break;
                	case XMLEvent.CHARACTERS:
                	case XMLEvent.CDATA:
                		if(found){
                			text += input.getText();
                		}
                		break;
                	case XMLEvent.END_ELEMENT:
                		currentElement.remove(currentElement.size()-1);
                		if(found){
                			exit = true;
                		}
                		break;
                	}
                	if (input.hasNext()){
    	                input.next();
    	            }
                }
            }
        }catch(Exception e){
        	log.error("Exception getting "+element,e);
        }
		return text;
	}

	/**
	 * Method to recover all the values of the one repeatabale element.
	 *
	 * @param element the repeatable element
	 *
	 * @return all the values of the element
	 */
	public List<String> lookingForwardAllElementContent(String element) {
		final String CONVERTED_FLAG = "Converted_APEnet_EAG_version_";
		XMLStreamReader input = null;
		InputStream sfile = null;
		XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
		xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
		xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
		xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
		xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

		List<String> resultList = new ArrayList<String>();

		try {
			sfile = new FileInputStream(this.eagPath);
			input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);

			boolean abort = false;
			boolean addText = false;
			String importantData = "";

			String[] pathElements = element.split("/");

			log.debug("Checking EAG file, looking for element " + element + ", path begins with " + pathElements[0]);
			while (!abort && input.hasNext()) {
				switch (input.getEventType()) {
					case XMLEvent.START_DOCUMENT:
						break;
					case XMLEvent.START_ELEMENT:
						if (input.getLocalName().equalsIgnoreCase(pathElements[(pathElements.length - 1)])) {
							addText = true;
						}
						break;
					case XMLEvent.CHARACTERS:
						if (addText) {
							importantData = input.getText();
							if (importantData.startsWith(CONVERTED_FLAG)) {
								log.debug("Returning true");
								resultList.add("true");
								return resultList;
							} else {
								log.debug("Adding " + input.getText());
								resultList.add(input.getText());
							}
							addText = false;
						}
						break;
					case XMLEvent.CDATA:
						break;
					case XMLEvent.END_ELEMENT:
						break;
				}
				if (input.hasNext())
					input.next();
			}
		} catch (Exception e) {
			log.error("Error parsing StAX for file " + this.eagPath, e);
		} finally {
			try {
				input.close();
				sfile.close();
			} catch (Exception e) {
				log.error("Error closing streams" + e.getMessage(), e);
			}
		}

		if (resultList.isEmpty()) {
			log.debug("Returning error");
			resultList.add("error");
		}
		return resultList;
	}
}
