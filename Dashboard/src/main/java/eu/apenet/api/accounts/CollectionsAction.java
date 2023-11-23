package eu.apenet.api.accounts;

import com.opensymphony.xwork2.Action;
import eu.apenet.api.BookmarksHelper;
import eu.apenet.persistence.dao.CollectionContentDAO;
import eu.apenet.persistence.dao.CollectionDAO;
import eu.apenet.persistence.dao.SavedBookmarksDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Collection;
import eu.apenet.persistence.vo.CollectionContent;
import eu.apenet.persistence.vo.EadSavedSearch;
import eu.apenet.persistence.vo.SavedBookmarks;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CollectionsAction {

    private String modxUserId;
    private String collectionId;

    //pagination
    private String max = "10";
    private String page = "1";

    //
    private String orderField = "none";
    private String asc = "true";

    //bookmark within collection pagination
    private String bmax = "10";
    private String bpage = "1";
    private String ccmax = "10";
    private String cpage = "1";

    private final Logger log = Logger.getLogger(getClass());

    public CollectionsAction() {

    }

    public String execute() throws NoSuchAlgorithmException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        CollectionDAO collectionDAO = DAOFactory.instance().getCollectionDAO();
        CollectionContentDAO collectionContentDAO = DAOFactory.instance().getCollectionContentDAO();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm");

        List<Collection> collectionList;
        Long totalCollections;
        collectionList = collectionDAO.getCollectionsByModxUserId(Long.parseLong(modxUserId), Integer.parseInt(page), Integer.parseInt(max), orderField, Boolean.getBoolean(asc));
            totalCollections = collectionDAO.countCollectionsByModxUserId(Long.parseLong(modxUserId));

        JSONObject jo = null;
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("total", totalCollections);
        List<Map<String, Object>> collectionListMap = new ArrayList<>();
        jsonMap.put("collections", collectionListMap);
        if (collectionList!=null) {
            for (Collection collection : collectionList) {
                Map<String, Object> collectionMap = new HashMap<>();
                collectionListMap.add(collectionMap);

                collectionMap.put("name", collection.getTitle());
                collectionMap.put("description", collection.getDescription());
                collectionMap.put("isPublic", collection.isPublic_());
                collectionMap.put("modifiedDate", simpleDateFormat.format(collection.getModified_date()));

                Long savedSearchTotal = collectionContentDAO.countCollectionContentsByCollectionId(collection.getId(), true);
                Long bookmarksTotal = collectionContentDAO.countCollectionContentsByCollectionId(collection.getId(), false);

                collectionMap.put("totalBookmarks", bookmarksTotal);
                collectionMap.put("totalSearches", savedSearchTotal);
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

    public String collectionDetails() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        CollectionDAO collectionDAO = DAOFactory.instance().getCollectionDAO();
        CollectionContentDAO collectionContentDAO = DAOFactory.instance().getCollectionContentDAO();

        Collection collection = collectionDAO.getCollectionByIdAndModxUserId(Long.parseLong(collectionId), Long.parseLong(modxUserId));

        List<CollectionContent> savedBookmarksList;
        Long totalBookmarks;
        List<CollectionContent> eadSavedSearchList;
        Long totalSearches;

        if (collection != null) {
            savedBookmarksList = collectionContentDAO.getCollectionContentsByCollectionId(Long.parseLong(collectionId), false, Integer.parseInt(bpage), Integer.parseInt(bmax));
            totalBookmarks = collectionContentDAO.countCollectionContentsByCollectionId(Long.parseLong(collectionId), false);

            eadSavedSearchList = collectionContentDAO.getCollectionContentsByCollectionId(Long.parseLong(collectionId), true, Integer.parseInt(bpage), Integer.parseInt(bmax));
            totalSearches = collectionContentDAO.countCollectionContentsByCollectionId(Long.parseLong(collectionId), true);
        }
        else {
            totalBookmarks = (long)0;
            totalSearches = (long)0;
            savedBookmarksList = new ArrayList<>();
            eadSavedSearchList = new ArrayList<>();
        }

        JSONObject jo = null;
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("totalBookmarks", totalBookmarks);
        jsonMap.put("totalSearches", totalSearches);
        if (savedBookmarksList!=null) {
            List<Map<String, Object>> bookmarksListMap = new ArrayList<>();
            jsonMap.put("bookmarks", bookmarksListMap);

            for (CollectionContent collectionContent : savedBookmarksList) {
                Map<String, Object> map = BookmarksHelper.getBookmarkMap(collectionContent.getSavedBookmarks());
                bookmarksListMap.add(map);
            }
        }

        if (eadSavedSearchList!=null) {
            List<Map<String, Object>> searchesListMap = new ArrayList<>();
            jsonMap.put("searches", searchesListMap);

            for (CollectionContent collectionContent : eadSavedSearchList) {
                Map<String, Object> map = new HashMap<>();
                searchesListMap.add(map);

                map.put("name", collectionContent.getEadSavedSearch().getDescription());
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

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public void setAsc(String asc) {
        this.asc = asc;
    }

    public String getOrderField() {
        return orderField;
    }

    public String getAsc() {
        return asc;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public void setBmax(String bmax) {
        this.bmax = bmax;
    }

    public void setBpage(String bpage) {
        this.bpage = bpage;
    }

    public void setCcmax(String ccmax) {
        this.ccmax = ccmax;
    }

    public void setCpage(String cpage) {
        this.cpage = cpage;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public String getBmax() {
        return bmax;
    }

    public String getBpage() {
        return bpage;
    }

    public String getCcmax() {
        return ccmax;
    }

    public String getCpage() {
        return cpage;
    }
}
