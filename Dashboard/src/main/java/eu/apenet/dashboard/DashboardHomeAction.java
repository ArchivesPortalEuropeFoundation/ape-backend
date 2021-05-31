package eu.apenet.dashboard;

import java.io.File;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * Paul
 */
public class DashboardHomeAction extends AbstractInstitutionAction {

    private static final long serialVersionUID = -7244833981839205973L;

    private Boolean errorFiles = false;

    public Boolean getErrorFiles() {
        return errorFiles;
    }

    public void setErrorFiles(Boolean errorFiles) {
        this.errorFiles = errorFiles;
    }

    @Override
    public String execute() throws Exception {

        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
        String alCountry = SecurityContext.get().getCountryIsoname();
        String basePath = APEnetUtilities.FILESEPARATOR + alCountry + APEnetUtilities.FILESEPARATOR
                + this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
        String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
        this.setErrorFiles(DAOFactory.instance().getQueueItemDAO().hasItemsWithErrors(getAiId()));
        //This is the first time that partner enters to the Dashboard
        //It first is necessary to declare a metadata licence for the institution
        if (archivalInstitution.getEagPath() == null) {
            if (archivalInstitution.getRightsInformation() == null) {
                return "inputRights";
            }
            //It then is necessary to upload or create an EAG file			
            addActionMessage(getText("label.eag.firstcreation"));
            return INPUT;
        } else if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()) {
            if (archivalInstitution.getRightsInformation() == null) {
                return "inputRights";
            }
            return "input2";

        } else {
            return SUCCESS;
        }
    }
}
