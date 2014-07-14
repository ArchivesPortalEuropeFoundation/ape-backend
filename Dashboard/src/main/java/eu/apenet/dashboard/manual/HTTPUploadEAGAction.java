package eu.apenet.dashboard.manual;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.ParseEag2012Errors;
import eu.apenet.dashboard.services.eag.EagService;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * User: Eloy García
 * Date: Oct 21, 2010
 *
 * @author Eloy García &  Paul
 */
public class HTTPUploadEAGAction extends AbstractInstitutionAction {

	private static final long serialVersionUID = 5948857869592106742L;

	//Attributes
    private ManualHTTPUploader uploader_http;

    private String httpFileFileName; 		//The uploaded file name
    private File httpFile;					//The uploaded file
    private String httpFileContentType;		//The content type of the file uploaded

    private List<String> filesNotUploaded;	//This attribute contains all the files not uploaded because of they are already in the Dashboard (tmp directory) or their format is not allowed
    private List<String> filesUploaded;		//This attribute contains all the files uploaded

    private List<String> warnings_eag;

	//Getters and Setters
    public void setHttpFileFileName(String httpFileFileName) {
		this.httpFileFileName = httpFileFileName;
	}

	public String getHttpFileFileName() {
		return httpFileFileName;
	}

	public void setHttpFile(File httpFile) {
		this.httpFile = httpFile;
	}

	public File getHttpFile() {
		return httpFile;
	}

	public void setHttpFileContentType(String httpFileContentType) {
		this.httpFileContentType = httpFileContentType;
	}

	public String getHttpFileContentType() {
		return httpFileContentType;
	}

    public List<String> getFilesNotUploaded() {
		return filesNotUploaded;
	}

    public List<String> getFilesUploaded() {
		return filesUploaded;
	}



    public List<String> getWarnings_eag(){
        return warnings_eag;
    }

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.eagupload"));
	}

	//Methods
    /**
     * Simple access to the upload page (no logic associated)
     * @return The code for the Struts2 dispatcher (Here only SUCCESS is returned)
     */
    @Override
    public String execute(){
    	//ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		String alCountry = new ArchivalLandscape().getmyCountry();
		String basePath = APEnetUtilities.FILESEPARATOR + alCountry + APEnetUtilities.FILESEPARATOR +
				this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
		String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;

    	addActionMessage(getText("label.eag.eagfileselection"));
    	addActionMessage(getText("label.eag.warningeagfileuploading"));

    	if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()){
    		return "input2";
    	}
    	return SUCCESS;

    }

	/**
     * Upload a File using HTTP protocol
     * @return The code used by Struts2 dispatcher
     */
    public String httpUpload(){

		//The ai_id is retrieved from the session
    	//This code has been moved to the prepare method

    	//The user is uploading an EAG file, so uploadType will be EAG
    	String result = null;
    	String uploadType = "EAG";
    	String uploadMethod = "HTTP";
    	Integer archivalInstitutionId;
    	String format = null;
    	try{
    		Integer aiId = getAiId();
    		if (this.getHttpFile()!=null){
        		//The user has selected a file to upload
        		format = this.getHttpFileFileName().substring(this.getHttpFileFileName().lastIndexOf(".") + 1).toLowerCase();

                archivalInstitutionId = aiId;
                uploader_http = new ManualHTTPUploader(uploadMethod);
        	    result = uploader_http.uploadFile(uploadType, this.getHttpFileFileName(), this.getHttpFile(), format, archivalInstitutionId, uploadMethod);

        	    if (result.equals("success") || result.equals("success_with_url_warning")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
        	    	this.filesUploaded = this.uploader_http.getFilesUploaded();
        	    	addActionMessage(getText("label.eag.eagcorrectlyuploaded"));
        	    	if(result.equals("success_with_url_warning")){
        	    		addActionMessage(getText("label.eag.eagwithurlwarnings"));
        	    	}
        	    	result = SUCCESS;
        	    }
        	    else if (result.equals("success_noInformation")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.eagcorrectlyuploaded.noinformation.part1") + "'" + getText("label.ai.hg.information.content.default") + "' " + getText("label.eag.eagcorrectlyuploaded.noinformation.part2"));
        	    	addActionMessage(getText("label.eag.eagcorrectlyuploaded"));
                    result = SUCCESS;
        	    }
        	    else if (result.equals("error_eagalreadyuploaded")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.eagalreadyuploaded"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_formatnotallowed")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.one"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_eagnotstored")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.error"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_database")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.database"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_archivallandscape")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.three"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_eagnotvalidatednotconverted")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    warnings_eag = uploader_http.getWarnings_eag();
                    List<String> tmp_warnings_eag = new LinkedList<String>();
                    addActionMessage(getText("label.eag.uploadingerror.two"));
                    for (int i = 0; i < warnings_eag.size(); i++) {
						String warning = warnings_eag.get(i).replace("<br/>", "");
						ParseEag2012Errors parseEag2012Errors = new ParseEag2012Errors(warning,false,this);
						if (this.getActionMessages() != null && !this.getActionMessages().isEmpty()) {
							String currentError = parseEag2012Errors.errorsValidation();
							if (!this.getActionMessages().contains(currentError)) {
								tmp_warnings_eag.add(parseEag2012Errors.errorsValidation());
							}
						} else {
							tmp_warnings_eag.add(parseEag2012Errors.errorsValidation());
						}
                    }
                    warnings_eag = tmp_warnings_eag;
        	    	result = ERROR;
        	    }
        	    else if (result.equals("error_eagnotconverted")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.notconverted"));
        	    	result = ERROR;
        	    }
        	    else if (result.equals("error_parsing")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.parsing"));
        	    	result = ERROR;
        	    }else if (result.equals("display_eag02convertedToeag2012")){
        	    	this.filesUploaded = this.uploader_http.getFilesUploaded();
        	    	addActionMessage(getText("eag2012.commons.oldEag"));
        	    	result = "input2";
        	    }
        	    else if (result.equals("error_eagnoinstitutionname")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("eag2012.commons.errorOnChangeNameOfInstitution"));
        	    	result = ERROR;
        	    } else if (result.equals("error_eaginstitutionnamespecialcharacter")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
        	    	addActionMessage(getText("label.eag.uploadingerror.eagspecialcharacters"));
        	    	result = "specialcharacters";
        	    	 
        	    }
        	}
        	else {
        		//The user has not selected a file to upload
        		addActionMessage(getText("label.uploadfile"));
        	/*	String alCountry = new ArchivalLandscape().getmyCountry();
        		String basePath = APEnetUtilities.FILESEPARATOR + alCountry + APEnetUtilities.FILESEPARATOR +
        				this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
        		String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
        		if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()){
        		   result="invalideag";

        		}*/
        		result = INPUT;
        	}
        	return result;
    	}catch(Exception e){
    		LOG.error("ERROR trying to upload a file ", e);
    	}
    	return ERROR;
    }

    public String parseEag02ToEAG2012(){
    	//This method parse an old EAG to EAG2012
		if (this.filesUploaded == null) {
			this.filesUploaded = new ArrayList<String>();
		}

		if (this.filesNotUploaded == null) {
			this.filesNotUploaded = new ArrayList<String>();
		}

    	String result = null;
    	Integer archivalInstitutionId;

    	try{
    		Integer aiId = getAiId();
    		String path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR;
    		String filename = "";
    		File dirFile = new File(path);
    		if (dirFile.isDirectory()) {
    			File[] fileList = dirFile.listFiles();
    			for (int i = 0; i < fileList.length; i++) {
    				filename = fileList[i].getName();
    			}
    		}
    		File file = new File(path + filename);
    		if (file != null){
                archivalInstitutionId = aiId;
                APEnetEAGDashboard eag = new APEnetEAGDashboard(archivalInstitutionId, file.getAbsolutePath());
        	    if (eag.convertEAG02ToEAG2012()){
    				if (eag.validate()){
    					result = eag.saveEAGviaHTTP(file.getAbsolutePath());
		        	    if (result.equals("error_eagalreadyuploaded")){
		        	    	this.filesNotUploaded.add(filename);
		                    addActionMessage(getText("label.eag.uploadingerror.eagalreadyuploaded"));
		                    result = ERROR;
		        	    }
		        	    else if (result.equals("error_eagnotstored")){
		        	    	this.filesNotUploaded.add(filename);
		                    addActionMessage(getText("label.eag.uploadingerror.error"));
		                    result = ERROR;
		        	    }
		        	    else if (result.equals("error_database")){
		        	    	this.filesNotUploaded.add(filename);
		                    addActionMessage(getText("label.eag.uploadingerror.database"));
		                    result = ERROR;
		        	    }
		        	    else if (result.equals("error_archivallandscape")){
		        	    	this.filesNotUploaded.add(filename);
		                    addActionMessage(getText("label.eag.uploadingerror.three"));
		                    result = ERROR;
		        	    }
		        	    else if (result.equals("error_eagnotvalidatednotconverted")) {
		        	    	this.filesNotUploaded.add(filename);
		                    warnings_eag = uploader_http.getWarnings_eag();
		                    addActionMessage(getText("label.eag.uploadingerror.two"));
		        	    	result = ERROR;
		        	    }
		        	    else if (result.equals("error_eagnotconverted")) {
		        	    	this.filesNotUploaded.add(filename);
		                    addActionMessage(getText("label.eag.uploadingerror.notconverted"));
		        	    	result = ERROR;
		        	    }
		        	    else if (result.equals("error_parsing")) {
		        	    	this.filesNotUploaded.add(filename);
		                    addActionMessage(getText("label.eag.uploadingerror.parsing"));
		        	    	result = ERROR;
		        	    } else {
							//store ddbb path
							ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
							ArchivalInstitution archivalInstitution = archivalInstitutionDao.getArchivalInstitution(archivalInstitutionId);
							if (archivalInstitution != null) {
								archivalInstitution.setEagPath(eag.getEagPath());
								String finalPath = APEnetUtilities.getConfig().getRepoDirPath() + eag.getEagPath();
								eag.setEagPath(finalPath);
								String repositoryCode = eag.lookingForwardElementContent("/eag/control/recordId");
								archivalInstitution.setRepositorycode(repositoryCode);
								archivalInstitutionDao.store(archivalInstitution);
								EagService.publish(archivalInstitution);
							} else {
			        	    	this.filesNotUploaded.add(filename);
			                    addActionMessage(getText("label.eag.notStored"));
			        	    	result = ERROR;
							}
		        	    	this.filesUploaded.add(filename);
		        	    	result = SUCCESS;
		        	    }
		        	}
		        	else {
	        	    	this.filesNotUploaded.add(filename);
		        		for (int i = 0; i < eag.warnings_ead.size(); i++) {
		        			addActionMessage(eag.warnings_ead.get(i));
		        		}
		        		result = ERROR;
		        	}
        	    }

        	    FileUtils.forceDelete(file);
                    if(dirFile.list().length == 0)
                        FileUtils.deleteDirectory(dirFile);

    		}
        	return result;
    	}catch(Exception e){
    		LOG.error("ERROR trying to upload a file ", e);
    	}
    	return ERROR;
    }

	public String removeEag02(){
		 try {
			Integer aiId = getAiId();
			String path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR;
			String filename = "";
			File dirFile = new File(path);
			if (dirFile.isDirectory()) {
				File[] fileList = dirFile.listFiles();
				for (int i = 0; i < fileList.length; i++) {
					filename = fileList[i].getName();
				}
			}
			File file = new File(path + filename);
			if(file!=null){
				FileUtils.forceDelete(file);
                                if(dirFile.list().length == 0)
                                    FileUtils.deleteDirectory(dirFile);

				return SUCCESS;
			}
		} catch (Exception e) {
			LOG.error("ERROR trying to upload a file ", e);
		}
		return ERROR;
	}
}
