package eu.apenet.api.other;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.analyzers.eag.SocialInfoExtractor;
import eu.apenet.commons.utils.analyzers.utils.SocialUtils;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MetatagsApiAction {

    private String aiRepositoryCode;
    private String recordId; //eadid for FA/SG/HG, identifier for EAC
    private String xmlType;
    private String clevelId; //clevelid for c component of FA/SG/HG
    private String clevelUnitId; //clevelid for c component of FA/SG/HG

    private final Logger log = Logger.getLogger(getClass());

    public MetatagsApiAction() {

    }

    public String execute() throws NoSuchAlgorithmException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        String repoPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();

        JSONObject jo = null;

        Map jsonMap = new HashMap();

        ArchivalInstitution archivalInstitution = null;
        boolean shouldProceed = true;
        if (getAiRepositoryCode() == null){
            jsonMap.put("error", "AI repository code not provided!");
            shouldProceed = false;
        }
        else {
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(getAiRepositoryCode());

            if (archivalInstitution == null){
                jsonMap.put("error", "AI repository could not be found!");
                shouldProceed = false;
            }
        }

        if (shouldProceed){
            if (recordId == null && clevelId == null && clevelUnitId == null) { //EAG
                SocialInfoExtractor socialInfoExtractor = new SocialInfoExtractor();
                jsonMap = SocialUtils.getJsonInfoForEag(archivalInstitution);
            }
            else {
                XmlType xmlType = XmlType.getTypeByResourceName(getXmlType());
                if (getXmlType().equals("ec")) { //EAC-CPF
                    EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
                    EacCpf eaccpf = eacCpfDAO.getEacCpfByIdentifier(archivalInstitution.getAiId(), recordId);

                    jsonMap = SocialUtils.getJsonInfoForEacCpf(repoPath, eaccpf);
                }
                else {
                    if (clevelId == null && clevelUnitId == null) {
                        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
                        Ead ead = eadDAO.getEadByEadid(xmlType.getEadClazz(), archivalInstitution.getAiId(), recordId);
                        EadContent eadContent = ead.getEadContent();

                        jsonMap = SocialUtils.getJsonInfoForEadContent(repoPath, eadContent);
                    }
                    else {
                        CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
                        CLevel cLevel = null;

                        if (clevelId != null){
                            cLevel = cLevelDAO.findById(Long.parseLong(clevelId));
                        }
                        else {
                            List<CLevel> cLevels = cLevelDAO.getCLevel(aiRepositoryCode, xmlType.getEadClazz(), recordId, clevelUnitId);
                            if (cLevels != null && cLevels.size()>0) {
                                cLevel = cLevels.get(0);
                            }
                        }

                        jsonMap = SocialUtils.getJsonInfoForCLevel(repoPath, cLevel);
                    }
                }
            }
        }

        jo = new JSONObject(jsonMap);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try {
            Writer writer =  new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            writer.write(jo.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setHeader("Access-Control-Allow-Origin","*");
        return Action.SUCCESS;
    }

    public void setAiRepositoryCode(String aiRepositoryCode) {
        this.aiRepositoryCode = aiRepositoryCode;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getAiRepositoryCode() {
        return aiRepositoryCode;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setXmlType(String xmlType) {
        this.xmlType = xmlType;
    }

    public String getXmlType() {
        return xmlType;
    }

    public void setClevelId(String clevelId) {
        this.clevelId = clevelId;
    }

    public String getClevelId() {
        return clevelId;
    }

    public void setClevelUnitId(String clevelUnitId) {
        this.clevelUnitId = clevelUnitId;
    }

    public String getClevelUnitId() {
        return clevelUnitId;
    }

    public static Map<String, Object> convertJsonToMap(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> map = new HashMap<>();
        Iterator iterator = jsonObject.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            Object value = jsonObject.get(key);
            map.put(key, value);
        }
        return map;
    }
}
