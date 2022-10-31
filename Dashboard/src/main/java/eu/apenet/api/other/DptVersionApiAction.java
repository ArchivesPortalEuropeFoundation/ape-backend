package eu.apenet.api.other;

import com.opensymphony.xwork2.Action;
import eu.apenet.persistence.dao.ApiKeyDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ApiKey;
import eu.apenet.persistence.vo.DptUpdate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DptVersionApiAction {

    private String versionNb;

    private final Logger log = Logger.getLogger(getClass());

    public DptVersionApiAction() {

    }

    public String execute() throws NoSuchAlgorithmException, IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        List<DptUpdate> dptUpdate = DAOFactory.instance().getDptUpdateDAO().findAll();
        if(StringUtils.isEmpty(versionNb) || dptUpdate.size() == 0 || dptUpdate.get(0).getNewVersion().equals(versionNb)) {
            response.sendError(404);
            return Action.NONE;
        } else {
            response.setHeader("Content-Type", "text/plain");
            response.setHeader("success", "yes");
            PrintWriter writer = response.getWriter();
            writer.write(dptUpdate.get(0).getNewVersion());
            writer.flush();
            writer.close();
            response.setHeader("Access-Control-Allow-Origin","*");
            return Action.NONE;
        }
    }

    public void setVersionNb(String versionNb) {
        this.versionNb = versionNb;
    }

    public String getVersionNb() {
        return versionNb;
    }
}
