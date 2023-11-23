package eu.apenet.api;

import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarksHelper {
    
    public static Map<String, Object> getBookmarkMap(SavedBookmarks savedBookmarks){
        Map<String, Object> map = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm");

        checkSavedBookmark(savedBookmarks);

        map.put("name", savedBookmarks.getName());
        map.put("description", savedBookmarks.getDescription());
        map.put("typeofdocument", savedBookmarks.getTypedocument());
        map.put("modifiedDate", simpleDateFormat.format(savedBookmarks.getModifiedDate()));
        map.put("link", savedBookmarks.getLink());
        map.put("modxRelativeLink", savedBookmarks.getModxRelativeLink());
        map.put("id", savedBookmarks.getId());
        map.put("contentTitle", savedBookmarks.getContentTitle());
        map.put("valid", savedBookmarks.getValid()==null?false:savedBookmarks.getValid());

        return map;
    }

    private static void checkSavedBookmark(SavedBookmarks savedBookmark){

        if (savedBookmark.getValid() != null) {
            if (savedBookmark.getValid()){ //If it is true, we need to check it every time we use it... it may become invalid at any time!

            }
        }

        SavedBookmarksDAO savedBookmarksDAO = DAOFactory.instance().getSavedBookmarksDAO();
        calculateModxRelativeURL(savedBookmark);
        SavedBookmarks bookmark = savedBookmarksDAO.store(savedBookmark);
    }

    private static void calculateModxRelativeURL(SavedBookmarks savedBookmark){
        String originalLink = savedBookmark.getLink();

        try {
            URL url = new URL(originalLink);
            String path = url.getPath();
            if (path.startsWith("/web/guest")) {
                path = path.replace("/web/guest", "");
                String modxRelativeLink = null;
                if (savedBookmark.getTypedocument().equals("ead")) {
                    modxRelativeLink = handlePortalEadDisplay(path, url.getQuery());
                }
                else {
                    modxRelativeLink = handlePortalEacDisplay(path, url.getQuery());
                }
                if (modxRelativeLink != null){
                    savedBookmark.setModxRelativeLink(modxRelativeLink);
                    savedBookmark.setValid(true);
                }
                else {
                    savedBookmark.setValid(false);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private static String handlePortalEadDisplay(String path, String queryString){
        if (matchesPattern("\\/ead-display\\/-\\/ead\\/pl\\/aicode\\/.+\\/type\\/(fa|hg|sg)\\/id\\/.+\\/(unitid|dbid)\\/.+(\\/search\\/.+)?", path)) {
            String temp = path.replace("/ead-display/-/ead/pl/aicode/","");
            String aiRepositoryCode = temp.substring(0, temp.indexOf("/"));

            if (!checkArchivalInstitution(aiRepositoryCode)){
                return null;
            }

            temp = temp.replace(aiRepositoryCode+"/type/","");
            String type = temp.substring(0, temp.indexOf("/"));
            temp = temp.replace(type+"/id/","");
            String id = temp.substring(0, temp.indexOf("/"));

            if (!checkEad(decodeString(id))){
                return null;
            }

            String term = null;
            String termType = null;
            String cid = null;
            if (temp.startsWith(id+"/unitid/")) {
                temp = temp.replace(id + "/unitid/", "");
                id = decodeString(id);
                try {
                    id = URLDecoder.decode(id, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String unitid = temp.substring(0, temp.indexOf("/")!=-1?temp.indexOf("/"):temp.length());
                if (temp.startsWith(unitid + "/search/")){
                    temp = temp.replace(unitid + "/search/", "");
                    term = temp.substring(0, temp.length());
                    termType = term.substring(0, term.indexOf("/"));
                    term = term.substring(term.indexOf("/")+1, term.length());
                }
                unitid = decodeString(unitid);
                try {
                    unitid = URLDecoder.decode(unitid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
                List<CLevel> clevels = cLevelDAO.getCLevel(aiRepositoryCode, type.equals("fa")? FindingAid.class:(type.equals("hg")? HoldingsGuide.class : SourceGuide.class), id, unitid);
                if (clevels != null && clevels.size() > 0) {
                    cid = "C" + clevels.get(0).getId();
                }
                else {
                    return null;
                }
            }
            else if (temp.startsWith(id+"/dbid/")) {
                temp = temp.replace(id + "/dbid/", "");
                id = decodeString(id);

                try {
                    id = URLDecoder.decode(id, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                cid = temp.substring(0, temp.indexOf("/")!=-1?temp.indexOf("/"):temp.length());
                if (temp.startsWith(cid + "/search/")){
                    temp = temp.replace(cid + "/search/", "");
                    term = temp.substring(0, temp.length());
                    termType = term.substring(0, term.indexOf("/"));
                    term = term.substring(term.indexOf("/")+1, term.length());
                }
                cid = decodeString(cid);

                CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
                CLevel cLevel = cLevelDAO.findById(Long.parseLong(cid.substring(1)));
                if (cLevel == null){
                    return null;
                }
            }

            String newUrl = "/advanced-search/search-in-archives/results-(archives)/?&repositoryCode="+aiRepositoryCode+(term != null ? ("&term="+term+"&using="+getEadSearchType(termType)) : "")+"&levelName=clevel&t="+type+"&recordId="+id+"&c="+cid;
            return newUrl;
        }
        else if (matchesPattern("\\/ead-display\\/-\\/ead\\/pl\\/aicode\\/.+\\/type\\/(fa|hg|sg)\\/id\\/.+(\\/search\\/.+)?", path)) {
            String temp = path.replace("/ead-display/-/ead/pl/aicode/","");
            String aiRepositoryCode = temp.substring(0, temp.indexOf("/"));

            if (!checkArchivalInstitution(aiRepositoryCode)){
                return null;
            }

            temp = temp.replace(aiRepositoryCode+"/type/","");
            String type = temp.substring(0, temp.indexOf("/"));
            temp = temp.replace(type+"/id/","");
            String id = temp.substring(0, temp.indexOf("/")!=-1?temp.indexOf("/"):temp.length());

            if (!checkEad(decodeString(id))){
                return null;
            }

            String term = null;
            String termType = null;
            if (temp.startsWith(id + "/search/")){
                temp = temp.replace(id + "/search/", "");
                term = temp.substring(0, temp.length());
                termType = term.substring(0, term.indexOf("/"));
                term = term.substring(term.indexOf("/")+1, term.length());
            }
            id = decodeString(id);
            try {
                id = URLDecoder.decode(id, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String newUrl = "/advanced-search/search-in-archives/results-(archives)/?&repositoryCode="+aiRepositoryCode+(term != null ? ("&term="+term+"&using="+getEadSearchType(termType)) : "")+"&levelName=archdesc&t="+type+"&recordId="+id;
            return newUrl;
        }
        else {
            return null;
        }
    }

    private static String handlePortalEacDisplay(String path, String queryString) {
        if (matchesPattern("\\/eac-display\\/-\\/eac\\/pl\\/aicode\\/.+\\/type\\/ec\\/id\\/.+(\\/search\\/.+)?", path)) {
            String temp = path.replace("/eac-display/-/eac/pl/aicode/","");
            String aiRepositoryCode = temp.substring(0, temp.indexOf("/"));

            if (!checkArchivalInstitution(aiRepositoryCode)){
                return null;
            }

            temp = temp.replace(aiRepositoryCode+"/type/","");
            String type = temp.substring(0, temp.indexOf("/"));
            temp = temp.replace(type+"/id/","");
            String id = temp.substring(0, temp.indexOf("/")!=-1?temp.indexOf("/"):temp.length());

            if (!checkEac(decodeString(id), aiRepositoryCode)){
                return null;
            }

            String term = null;
            String termType = null;

            if (temp.startsWith(id + "/search/")){
                temp = temp.replace(id + "/search/", "");
                term = temp.substring(0, temp.length());
                termType = term.substring(0, term.indexOf("/"));
                term = term.substring(term.indexOf("/")+1, term.length());
            }
            id = decodeString(id);
            try {
                id = URLDecoder.decode(id, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String newUrl = "/advanced-search/search-in-names/results-(names)/?&repositoryCode="+aiRepositoryCode+(term != null ? ("&term="+term+"&using="+getEacSearchType(termType)) : "")+"&recordId="+id;
            return newUrl;
        }
        else {
            return null;
        }
    }

    private static boolean matchesPattern(String pattern, String text){
        return text.matches(pattern);
    }

    private static boolean checkArchivalInstitution(String aiRepositoryCode){
        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);

        if (archivalInstitution == null){
            return false;
        }

        return true;
    }

    private static boolean checkEad(String id){
        EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
        EadContent eadContent = eadContentDAO.getEadContentByEadid(id);

        if (eadContent == null){
            return false;
        }

        return true;
    }

    private static boolean checkEac(String id, String aiRepositoryCode){
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.getEacCpfByIdentifier(aiRepositoryCode, id, false);

        if (eacCpf == null){
            return false;
        }

        return true;
    }

    private static String decodeString(String s){
        String response = s;

        response = response.replace("_SLASH_", "/");
        response = response.replace("_PLUS_", "+");
        response = response.replace("_COLON_", ":");
        response = response.replace("_ASTERISK_", "*");
        response = response.replace("_AMP_", "&");
        response = response.replace("_COMMA_", ",");
        response = response.replace("_LT_", "<");
        response = response.replace("_RT_", ">");
        response = response.replace("_TILDE_", "~");
        response = response.replace("_LSQBRKT_", "[");
        response = response.replace("_RSQBRKT_", "]");
        response = response.replace("_PERCENT_", "%");
        response = response.replace("_ATCHAR_", "@");
        response = response.replace("_QUOTE_", "\"");
        response = response.replace("_DOLLAR_", "$");
        response = response.replace("_COMP_", "=");
        response = response.replace("_HASH_", "#");
        response = response.replace("_CFLEX_", "^");
        response = response.replace("_LRDBRKT_", "(");
        response = response.replace("_RRDBRKT_", ")");
        response = response.replace("_EXCLMARK_", "!");
        response = response.replace("_SEMICOLON_", ";");
        response = response.replace("_BSLASH_", "\\");

        return response;
    }

    private static String getEadSearchType(String type){
        if (type.equals("0")){
            return "all";
        }
        else if (type.equals("1")){
            return "title";
        }
        else if (type.equals("2")){
            return "content_summary";
        }
        else if (type.equals("3")){
            return "reference_code";
        }
        return "all";
    }

    private static String getEacSearchType(String type){
        return type;
    }
}
