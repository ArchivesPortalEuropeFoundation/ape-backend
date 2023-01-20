package eu.apenet.api.other;

import com.opensymphony.xwork2.Action;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactFinderApiAction {

    private String aiId;
    private String aiRepositoryCode;
    private Map contactInformation;
    private String type = null;
    private List errors = null;

    private final Logger log = Logger.getLogger(getClass());

    public ContactFinderApiAction() {

    }

    public String execute() throws NoSuchAlgorithmException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        UserDAO userDAO = DAOFactory.instance().getUserDAO();

        ArchivalInstitution archivalInstitution = null;
        if (aiId != null) {
            archivalInstitution = archivalInstitutionDAO.findById(Integer.parseInt(aiId));
            if (archivalInstitution == null){
                errors = new ArrayList();
                Map error2 = new HashMap();
                error2.put("code", 1);
                error2.put("reason", "No institution exists");
                errors.add(error2);
            }
            else {
                aiRepositoryCode = archivalInstitution.getRepositorycode();
            }
        }
        else if (aiRepositoryCode != null) {
            archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);
            if (archivalInstitution == null){
                errors = new ArrayList();
                Map error2 = new HashMap();
                error2.put("code", 1);
                error2.put("reason", "No institution exists");
                errors.add(error2);
            }
            else {
                aiId = "" + archivalInstitution.getAiId();
            }
        }
        else {
            errors = new ArrayList();
            Map error2 = new HashMap();
            error2.put("code", 2);
            error2.put("reason", "Institution is missing");
            errors.add(error2);

        }

        if (!"contact_form_detail_page".equals(type)){
            if (errors==null){
                errors = new ArrayList();
            }
            Map error2 = new HashMap();
            error2.put("code", 3);
            error2.put("reason", "Type is not supported");
            errors.add(error2);
        }


        if (errors == null) {
            contactInformation = new HashMap();

            if (type.equals("contact_form_detail_page")) {
                String cmEmail = null;
                User userCM = userDAO.getCountryManagerOfCountry(archivalInstitution.getCountry());
                if (userCM != null){
                    cmEmail = userCM.getEmailAddress();
                    System.out.println(cmEmail);
                }
                System.out.println("here");
                String feedbackEmail = archivalInstitution.getFeedbackEmail();
                if (feedbackEmail != null) {
                    contactInformation.put("to", feedbackEmail);
                    if (cmEmail != null) {
                        contactInformation.put("cc", cmEmail);
                    }
                }
                else {
                    User user = archivalInstitution.getPartner();
                    if (user != null){
                        contactInformation.put("to", user.getEmailAddress());
                        if (cmEmail != null) {
                            contactInformation.put("cc", cmEmail);
                        }
                    }
                    else {
                        if (cmEmail != null) {
                            contactInformation.put("to", cmEmail);
                        }
                    }
                }
            }

        }

        return Action.SUCCESS;
    }

    public void setAiId(String aiId) {
        this.aiId = aiId;
    }

    public void setAiRepositoryCode(String aiRepositoryCode) {
        this.aiRepositoryCode = aiRepositoryCode;
    }

    public String getAiId() {
        return aiId;
    }

    public String getAiRepositoryCode() {
        return aiRepositoryCode;
    }

    public void setContactInformation(Map contactInformation) {
        this.contactInformation = contactInformation;
    }

    public Map getContactInformation() {
        return contactInformation;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setError(List errors) {
        this.errors = errors;
    }

    public List getErrors() {
        return errors;
    }
}
