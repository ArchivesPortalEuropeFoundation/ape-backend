package eu.apenet.api.other;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.infraestructure.ArchivalInstitutionUnit;
import eu.apenet.commons.infraestructure.CountryUnit;
import eu.apenet.commons.infraestructure.NavigationTree;
import eu.apenet.persistence.dao.ApiKeyDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.ApiKeyHibernateDAO;
import eu.apenet.persistence.vo.ApiKey;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class ApiKeyApiAction {

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String domain;
    private String modxUserId;
    private String action;

    private final Logger log = Logger.getLogger(getClass());

    public ApiKeyApiAction() {

    }

    public String execute() throws NoSuchAlgorithmException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        ApiKeyDAO apiKeyDAO = DAOFactory.instance().getApiKeyDAO();

        ApiKey apiKey = apiKeyDAO.findByModxUserId(Long.parseLong(this.modxUserId));

        JSONObject jo = null;
        if (apiKey == null){
            if (this.action.equals("create")){
                apiKey = new eu.apenet.persistence.vo.ApiKey();
                apiKey.setApiKey(generateApiKey(email));
                apiKey.setEmailAddress(email);
                apiKey.setFirstName(firstName);
                apiKey.setLastName(lastName);
                apiKey.setUrl(domain);
                apiKey.setModxUserId(Long.parseLong(this.modxUserId));
                apiKey.setStatus(ApiKey.STATUS_CREATED);
                apiKeyDAO.store(apiKey);

                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("error", null);
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("modxUserId", modxUserId);
                infoMap.put("apiKey", apiKey.getApiKey());
                jsonMap.put("response", infoMap);

                jo = new JSONObject(jsonMap);
            }
            else if (this.action.equals("regenerate")){
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("error", true);
                jsonMap.put("errorDescription", "Api Key does not exist for the specific user");
                jo = new JSONObject(jsonMap);
            }
            else if (this.action.equals("show")){
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("error", true);
                jsonMap.put("errorDescription", "Api Key does not exist for the specific user");
                jo = new JSONObject(jsonMap);
            }
        }
        else {
            if (this.action.equals("create")){
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("error", true);
                jsonMap.put("errorDescription", "User already has an Api Key");
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("modxUserId", modxUserId);
                infoMap.put("apiKey", apiKey.getApiKey());
                jsonMap.put("response", infoMap);

                jo = new JSONObject(jsonMap);
            }
            else if (this.action.equals("regenerate")){
                apiKey.setStatus(ApiKey.STATUS_DELETED);
                apiKeyDAO.store(apiKey);

                ApiKey apiKey2 = new eu.apenet.persistence.vo.ApiKey();
                apiKey2.setApiKey(generateApiKey(email));
                apiKey2.setEmailAddress(email);
                apiKey2.setFirstName(firstName);
                apiKey2.setLastName(lastName);
                apiKey2.setUrl(domain);
                apiKey2.setModxUserId(Long.parseLong(this.modxUserId));
                apiKey2.setStatus(ApiKey.STATUS_CREATED);
                apiKeyDAO.store(apiKey2);

                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("error", null);
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("modxUserId", modxUserId);
                infoMap.put("apiKey", apiKey2.getApiKey());
                jsonMap.put("response", infoMap);

                jo = new JSONObject(jsonMap);
            }
            else if (this.action.equals("show")){
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("error", null);
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("modxUserId", modxUserId);
                infoMap.put("apiKey", apiKey.getApiKey());
                jsonMap.put("response", infoMap);

                jo = new JSONObject(jsonMap);
            }
        }

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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setModxUserId(String modxUserId) {
        this.modxUserId = modxUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getModxUserId() {
        return modxUserId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEmail() {
        return email;
    }

    public String getDomain() {
        return domain;
    }

    public String getAction() {
        return action;
    }

    public static String generateApiKey(String email) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        StringBuilder stringToBeHashed = new StringBuilder();
        stringToBeHashed.append(getSecureRandomToken());
//        stringToBeHashed.append(user.getPassword());
        stringToBeHashed.append(email);
        messageDigest.update(stringToBeHashed.toString().getBytes());

        byte[] digest = messageDigest.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String keyText = bigInt.toString(16);
        return keyText;
    }

    private static String getSecureRandomToken() {
        long value;
        SecureRandom rand = new SecureRandom();
        value = rand.nextLong();

        value = Math.abs(value);
        String token = Long.toString(value);
        return token;
    }
}
