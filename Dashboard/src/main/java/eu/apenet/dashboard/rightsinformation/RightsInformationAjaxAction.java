/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.rightsinformation;

import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.persistence.dao.RightsInformationDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.RightsInformation;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author apef
 */
public class RightsInformationAjaxAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private final static Logger LOGGER = Logger.getLogger(RightsInformationAjaxAction.class);
    private static final String UTF8 = "UTF-8";

    private HttpServletRequest request;
    private HttpServletResponse response;

    private String rightsInformationDescription;

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public String execute() {
        LOGGER.debug("Entering method \"execute\".");
        fetchRightsDescriptionFromDatabase();
        LOGGER.debug("Leaving method \"execute\".");
        return SUCCESS;
    }

    private void fetchRightsDescriptionFromDatabase() {
        try {
            request.setCharacterEncoding(UTF8);
            response.setCharacterEncoding(UTF8);
            response.setContentType("text/plain");
            try (Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8)) {
                int rightsId = Integer.parseInt(request.getParameter("rightsId"));
                RightsInformationDAO rightsInformationDAO = DAOFactory.instance().getRightsInformationDAO();
                RightsInformation rightsInformation = rightsInformationDAO.getRightsInformation(rightsId);
                rightsInformationDescription = rightsInformation.getDescription();
                writer.write(rightsInformationDescription);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }

    public String getRightsInformationDescription() {
        return rightsInformationDescription;
    }

}
