package eu.apenet.api.accounts;

import com.opensymphony.xwork2.Action;
import eu.apenet.api.BookmarksHelper;
import eu.apenet.persistence.dao.ApiKeyDAO;
import eu.apenet.persistence.dao.SavedBookmarksDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ApiKey;
import eu.apenet.persistence.vo.SavedBookmarks;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarksAction {

    private String modxUserId;
    private String collectionId;

    //pagination
    private String max = "100";
    private String page = "1";

    private final Logger log = Logger.getLogger(getClass());

    public BookmarksAction() {

    }

    public String execute() throws NoSuchAlgorithmException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        SavedBookmarksDAO savedBookmarksDAO = DAOFactory.instance().getSavedBookmarksDAO();
        List<SavedBookmarks> savedBookmarksList;
        Long totalSavedBookmarks;
        if (collectionId == null) {
            savedBookmarksList = savedBookmarksDAO.getSavedBookmarksByModxUserId(Long.parseLong(modxUserId), Integer.parseInt(page), Integer.parseInt(max));
            totalSavedBookmarks = savedBookmarksDAO.countSavedBookmarksByModxUserId(Long.parseLong(modxUserId));
        }
        else {
            savedBookmarksList = savedBookmarksDAO.getSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(Long.parseLong(collectionId), Long.parseLong(modxUserId), Integer.parseInt(page), Integer.parseInt(max));
            totalSavedBookmarks = savedBookmarksDAO.countSavedBookmarksOutOfCollectionByCollectionIdAndModxUser(Long.parseLong(collectionId), Long.parseLong(modxUserId));
        }

        JSONObject jo = null;
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("total", totalSavedBookmarks);
        List<Map<String, Object>> bookmarksListMap = new ArrayList<>();
        jsonMap.put("bookmarks", bookmarksListMap);
        if (savedBookmarksList!=null) {
            for (SavedBookmarks savedBookmarks : savedBookmarksList) {
                Map<String, Object> bookmarkMap = BookmarksHelper.getBookmarkMap(savedBookmarks);
                bookmarksListMap.add(bookmarkMap);
            }
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        jo = new JSONObject(jsonMap);
        try {
            Writer writer =  new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            writer.write(jo.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setHeader("Access-Control-Allow-Origin","*");
        return Action.NONE;
    }

    public String storeBookmark() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        System.out.println("store bookmark");

        JSONObject jo = null;
        Map<String, Object> jsonMap = new HashMap<>();
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
        return Action.NONE;
    }

    public void setModxUserId(String modxUserId) {
        this.modxUserId = modxUserId;
    }

    public String getModxUserId() {
        return modxUserId;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getMax() {
        return max;
    }

    public String getPage() {
        return page;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionId() {
        return collectionId;
    }
}
