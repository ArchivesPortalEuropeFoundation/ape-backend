package eu.apenet.api.eag;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.EagXslt;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import net.sf.saxon.s9api.SaxonApiException;

import java.io.File;
import java.io.StringWriter;
import java.util.*;

public class EagApiAction {
    private String html = "";

    private String aiId;
    private Boolean preview = false;

    public EagApiAction() {

    }

    public String execute() {

        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDAO.findById(Integer.parseInt(aiId));
        String eagPath = APEnetUtilities.getDashboardConfig().getRepoDirPath() + archivalInstitution.getEagPath();

        StringWriter stringWriter = new StringWriter();
        try {
            EagXslt.displayAiDetails(preview, stringWriter, new File(eagPath), new StrutsResourceBundleSource(), null, null);
        } catch (SaxonApiException e) {
            e.printStackTrace();
        }

        this.html = stringWriter.toString();

        return Action.SUCCESS;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setAiId(String aiId) {
        this.aiId = aiId;
    }

    public String getAiId() {
        return aiId;
    }

    public Boolean getPreview() {
        return preview;
    }

    public void setPreview(Boolean preview) {
        this.preview = preview;
    }
}
