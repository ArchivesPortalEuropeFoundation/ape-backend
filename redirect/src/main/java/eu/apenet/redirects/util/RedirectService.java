package eu.apenet.redirects.util;

import eu.apenet.commons.listener.RedirectsPropertiesUtil;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedirectService {

    private static Logger loggerRequests = Logger.getLogger("file-requests-logger");
    private static Logger logger = Logger.getLogger(RedirectService.class);

    static RedirectService instance;
    /*
     * Factory method for instantiation of default factory.
     */
    public static RedirectService instance() {
        if (instance == null) {
            instance = new RedirectService();
        }
        return instance;
    }

    public Redirection handleRequest(HttpServletRequest httpServletRequest, String type){

//        String scheme = httpServletRequest.getScheme();
//        String serverName = httpServletRequest.getServerName();
//        int serverPort = httpServletRequest.getServerPort();
//
//        StringBuilder baseUrl = new StringBuilder();
//        baseUrl.append(scheme).append("://").append(serverName);
//
//        if (serverPort != 80 && serverPort != 443) {
//            baseUrl.append(":").append(serverPort);
//        }

        Redirection redirection = null;
        if (type.equals(Redirection.REDIRECTION_TYPE_PORTAL)) {
            redirection = handlePortalQueryString(httpServletRequest.getPathInfo(), httpServletRequest.getQueryString(), httpServletRequest.getHeader("Referer"));
        }
        else if (type.equals(Redirection.REDIRECTION_TYPE_WIKI)) {
            redirection = handleWikiQueryString(httpServletRequest.getPathInfo(), httpServletRequest.getQueryString(), httpServletRequest.getHeader("Referer"));
        }
        else if (type.equals(Redirection.REDIRECTION_TYPE_APEF)) {
            redirection = handleApefQueryString(httpServletRequest.getPathInfo(), httpServletRequest.getQueryString(), httpServletRequest.getHeader("Referer"));
        }
        else if (type.equals(Redirection.REDIRECTION_TYPE_BLOG)) {
            redirection = handleBlogQueryString(httpServletRequest.getPathInfo(), httpServletRequest.getQueryString(), httpServletRequest.getHeader("Referer"));
        }
        else if (type.equals(Redirection.REDIRECTION_TYPE_XSD)) {
            redirection = handleXsdQueryString(httpServletRequest.getPathInfo(), httpServletRequest.getQueryString(), httpServletRequest.getHeader("Referer"));
        }
        else if (type.equals(Redirection.REDIRECTION_TYPE_DPT)) {
            redirection = handleDptQueryString(httpServletRequest.getPathInfo(), httpServletRequest.getQueryString(), httpServletRequest.getHeader("Referer"));
        }

        loggerRequests.info(redirection.getPath()+"|"+redirection.getQueryString()+"|"+redirection.getNewUrl()+"|"+redirection.isHandled()+"|"+redirection.isIdNotFound()+"|"+redirection.getReferer()+"|"+redirection.getLocale()+"|"+redirection.getType());

        return redirection;

//        return handleUrl(httpServletRequest.getRequestURL().toString());
    }

//    public String handleUrl (String urlS){
//        try {
//            URL url = new URL(urlS);
//
//            String scheme = url.getProtocol();
//            String serverName = url.getHost();
//            int serverPort = url.getPort();
//
//            StringBuilder baseUrl = new StringBuilder();
//            baseUrl.append(scheme).append("://").append(serverName);
//
//            if (serverPort != 80 && serverPort != 443) {
//                baseUrl.append(":").append(serverPort);
//            }
//
//            return handleQueryString(url.getPath().substring(0), url.getQuery());
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    private Redirection handlePortalQueryString(String path, String queryString, String referer){

        String baseUrl = RedirectsPropertiesUtil.get("ape.portal.domain");
        Redirection redirection = new Redirection();
        redirection.setPath(path);
        redirection.setQueryString(queryString);
        redirection.setReferer(referer);
        redirection.setType(Redirection.REDIRECTION_TYPE_PORTAL);

        //Remove language from the begging of the path, if exists

        if (path.startsWith("/bg/") || path.startsWith("/hr/") || path.startsWith("/de/") || path.startsWith("/et/") ||
                path.startsWith("/el") || path.startsWith("/en/") || path.startsWith("/es/") || path.startsWith("/fr/") ||
                path.startsWith("/ga/") || path.startsWith("/is/") || path.startsWith("/it/") || path.startsWith("/ka/") ||
                path.startsWith("/lv/") || path.startsWith("/lt/") || path.startsWith("/hu/") || path.startsWith("/mt/") ||
                path.startsWith("/nl/") || path.startsWith("/no/") || path.startsWith("/pl/") || path.startsWith("/pt/") ||
                path.startsWith("/sk/") || path.startsWith("/sl/") || path.startsWith("/fi/") || path.startsWith("/sv/") ||
                path.startsWith("/sr/")){
            redirection.setLocale(path.substring(1,3));
            path = path.substring(3);
        }

        if (path.startsWith("/directory")){
            handlePortalDirectory(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/web")){
            handlePortalWeb(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/home")){
            handlePortalHome(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/search") && !path.contains("/topic/")){
            handlePortalSearch(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/name-search")){
            handlePortalNameSearch(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/institution-search")){
            handlePortalInsttutionSearch(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/ead-display")){
            handlePortalEadDisplay(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/eac-display")){
            handlePortalEacDisplay(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/featured-document")){
            handlePortalFeatureDocument(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/information-api")){
//            String newUrl = "/tools/api/get-your-api-key";
//            redirection.setNewUrl(baseUrl+newUrl);

            String oldBaseUrl = RedirectsPropertiesUtil.get("ape.portal.old.domain");
            String newUrl = oldBaseUrl+ "/information-api";
            redirection.setPassThrough(true); // do not show landing page
            redirection.setNewUrl(newUrl);
        }
        else  if (path.startsWith("/sign-in")){
            handlePortalSignIn(baseUrl, path, queryString, redirection);
        }
        else  if (path.startsWith("/topics") || path.contains("/topic/")){
            handlePortalTopic(baseUrl, path, queryString, redirection);
        }
        else {
            Map<String, String> mapping = getGenericMapping();
            if (mapping.containsKey(path)){
                String newUrl = mapping.get(path);
                redirection.setNewUrl(baseUrl + newUrl);
            }
            else {
                redirection.setHandled(false);
                redirection.setNewUrl(baseUrl);
            }
        }

        return redirection;
    }

    private Redirection handleWikiQueryString(String path, String queryString, String referer) {

        String baseUrl = RedirectsPropertiesUtil.get("ape.portal.domain");
        Redirection redirection = new Redirection();
        redirection.setPath(path);
        redirection.setQueryString(queryString);
        redirection.setReferer(referer);
        redirection.setType(Redirection.REDIRECTION_TYPE_WIKI);

        Map<String, String> mapping = getWikiMapping();

        if (path == null || path.length()==0 || path.equals("/")){
            redirection.setNewUrl(baseUrl+"/tools");
        }
        else if (mapping.containsKey(path)){
            redirection.setNewUrl(baseUrl+mapping.get(path));
        }
        else {
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
        }

        return redirection;
    }

    private Redirection handleApefQueryString(String path, String queryString, String referer) {

        String baseUrl = RedirectsPropertiesUtil.get("ape.portal.domain");
        Redirection redirection = new Redirection();
        redirection.setPath(path);
        redirection.setQueryString(queryString);
        redirection.setReferer(referer);
        redirection.setType(Redirection.REDIRECTION_TYPE_APEF);

        Map<String, String> mapping = getApefMapping();

        if (path == null || path.length()==0 || path.equals("/")){
            redirection.setNewUrl(baseUrl+"/about-us");
        }
        else if (mapping.containsKey(path)){
            redirection.setNewUrl(baseUrl+mapping.get(path));
        }
        else {
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
        }

        return redirection;
    }

    private Redirection handleBlogQueryString(String path, String queryString, String referer) {

        String baseUrl = RedirectsPropertiesUtil.get("ape.portal.domain");
        Redirection redirection = new Redirection();
        redirection.setPath(path);
        redirection.setQueryString(queryString);
        redirection.setReferer(referer);
        redirection.setType(Redirection.REDIRECTION_TYPE_BLOG);

        Map<String, String> mapping = getBlogMapping();

        if (path == null || path.length()==0 || path.equals("/")){
            redirection.setNewUrl(baseUrl+"/blog");
        }
        else if (path.startsWith("/category/")){
            String category = path.replace("category/", "");
            String newUrl = "/blog/"+category;
            redirection.setNewUrl(baseUrl + newUrl );
        }
        else if (matchesPattern("\\/\\d\\d\\d\\d\\/\\d\\d\\/\\d\\d\\/.+", path)){
            String newUrl = "/blog"+path;
            redirection.setNewUrl(baseUrl + newUrl );
        }
        else if (mapping.containsKey(path)){
            redirection.setNewUrl(baseUrl+mapping.get(path));
        }
        else {
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
        }

        return redirection;
    }

    private Redirection handleXsdQueryString(String path, String queryString, String referer) {

        String baseUrl = RedirectsPropertiesUtil.get("ape.portal.domain");
        Redirection redirection = new Redirection();
        redirection.setPath(path);
        redirection.setQueryString(queryString);
        redirection.setReferer(referer);
        redirection.setType(Redirection.REDIRECTION_TYPE_XSD);

        if (path == null || path.length()==0 || path.equals("/")){
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
            return redirection;
        }

        String schemasURL = RedirectsPropertiesUtil.get("ape.portal.schemas.domain");
        Map<String, String> mapping = getXsdMapping();

        if (mapping.containsKey(path)){
            String newUrl = schemasURL + mapping.get(path);
            redirection.setPassThrough(true); // do not show landing page
            redirection.setNewUrl(newUrl);

            return redirection;
        }
        else {
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
            return redirection;
        }
    }

    private Redirection handleDptQueryString(String path, String queryString, String referer) {

//        http://www.archivesportaleurope.net/Portal/dptupdate/version?versionNb=
//        http://dpt.archivesportaleurope.net/APE_data_preparation_tool_

        Redirection redirection = new Redirection();
        redirection.setPath(path);
        redirection.setQueryString(queryString);
        redirection.setReferer(referer);
        redirection.setType(Redirection.REDIRECTION_TYPE_DPT);

        if (path.startsWith("/Portal/dptupdate")){
            String versionNb = queryString.replace("versionNb=", "");

            String baseUrl = RedirectsPropertiesUtil.get("ape.portal.domain");
            redirection.setPassThrough(true); // do not show landing page
            redirection.setNewUrl(baseUrl+"/Dashboard/dptVersionApi.action?versionNb="+versionNb);
        }
        else if (path.startsWith("/APE_data_preparation_tool_")) {
                String versionNb = path.replace("/APE_data_preparation_tool_", "");

            redirection.setPassThrough(true); // do not show landing page
            redirection.setNewUrl("https://github.com/ArchivesPortalEuropeFoundation/ape-dpt/releases/tag/DPT-project-" + versionNb);
        }
        else{
            String baseUrl = RedirectsPropertiesUtil.get("ape.portal.domain");
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
            return redirection;
        }

        return redirection;
    }

    private boolean matchesPattern(String pattern, String text){
        return text.matches(pattern);
    }

    private void handlePortalDirectory(String baseUrl, String path, String queryString, Redirection redirection){
        if (path.equals("/directory") || path.equals("/directory/")){
//            String newUrl = "/find-an-institution";
//            redirection.setNewUrl(baseUrl+newUrl);

            String oldBaseUrl = RedirectsPropertiesUtil.get("ape.portal.old.domain");
            String newUrl = oldBaseUrl+ "/directory";
            redirection.setPassThrough(true); // do not show landing page
            redirection.setNewUrl(newUrl);

        }
        else {
            String aiRepositoryCode = null;
            if (matchesPattern("\\/directory\\/-\\/dir\\/ai\\/code\\/.+\\/search\\/.+", path)) {
                String temp = path.replace("/directory/-/dir/ai/code/","");
                aiRepositoryCode = temp.substring(0, temp.indexOf("/"));
            }
            else  if (matchesPattern("\\/directory\\/-\\/dir\\/ai\\/code\\/.+", path)) {
                aiRepositoryCode = path.substring(path.lastIndexOf("/") + 1, path.length());
            }
            else if (matchesPattern("\\/directory\\/-\\/dir\\/content\\/.+\\/(fa|hg|sg|ec)(\\/\\d+)?", path)) {
                String temp = path.replace("/directory/-/dir/content/","");
                aiRepositoryCode = temp.substring(0, temp.indexOf("/"));
            }
            else {
                redirection.setHandled(false);
                redirection.setNewUrl(baseUrl);
                return;
            }

            if (aiRepositoryCode != null) {
                ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
                ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);

                if (!checkArchivalInstitution(aiRepositoryCode)){
                    redirection.setIdNotFound(true);
                    redirection.setNewUrl(baseUrl);
                    return;
                }

                String newUrl = "/advanced-search/search-in-institutions/results-(institutions)/?repositoryCode=";
                redirection.setNewUrl(baseUrl + newUrl + aiRepositoryCode);
            }
            else {
                redirection.setHandled(false);
                redirection.setNewUrl(baseUrl);
                return;
            }
        }
    }

    private void handlePortalHome(String baseUrl, String path, String queryString, Redirection redirection){
//        if (path.equals("/home")){
            redirection.setNewUrl(baseUrl);
//        }
    }

    private void handlePortalWeb(String baseUrl, String path, String queryString, Redirection redirection){
        String newUrl = "";
        if (path.equals("/web/guest/help")) {
            newUrl = "?show=help";
        }
        else if (path.startsWith("/web/guest/help/searching")) {
            newUrl = "/tools/research-tools";
        }
        else if (path.startsWith("/web/guest/help/search-results")) {
            newUrl = "/tools/research-tools";
        }
        else if (path.startsWith("/web/guest/help/topics")) {
            newUrl = "/tools/research-tools";
        }
        else if (path.startsWith("/web/guest/help/finding-institutions")) {
            newUrl = "/tools/research-tools";
        }
        else if (path.startsWith("/web/guest/help/sign-in")) {
            newUrl = "/about-us/join-us/?tab=registered-user";
        }
        else if (path.startsWith("/web/guest/help/glossary")) {
            newUrl = "?show=help";
        }
//        else {
//            redirection.setNewUrl(baseUrl);
//        }

        redirection.setNewUrl(baseUrl+newUrl);
    }

    private void handlePortalSearch(String baseUrl, String path, String queryString, Redirection redirection){
        String newUrl = "/advanced-search/search-in-archives";
        redirection.setNewUrl(baseUrl+newUrl);
    }

    private void handlePortalNameSearch(String baseUrl, String path, String queryString, Redirection redirection){
        String newUrl = "/advanced-search/search-in-names";
        redirection.setNewUrl(baseUrl+newUrl);
    }

    private void handlePortalInsttutionSearch(String baseUrl, String path, String queryString, Redirection redirection){
        String newUrl = "/advanced-search/search-in-institutions";
        redirection.setNewUrl(baseUrl+newUrl);
    }

    private void handlePortalEadDisplay(String baseUrl, String path, String queryString, Redirection redirection){
        if (matchesPattern("\\/ead-display\\/-\\/ead\\/pl\\/aicode\\/.+\\/type\\/(fa|hg|sg)\\/id\\/.+\\/(unitid|dbid)\\/.+(\\/search\\/.+)?", path)) {
            String temp = path.replace("/ead-display/-/ead/pl/aicode/","");
            String aiRepositoryCode = temp.substring(0, temp.indexOf("/"));

            if (!checkArchivalInstitution(aiRepositoryCode)){
                redirection.setIdNotFound(true);
                redirection.setNewUrl(baseUrl);
                return;
            }

            temp = temp.replace(aiRepositoryCode+"/type/","");
            String type = temp.substring(0, temp.indexOf("/"));
            temp = temp.replace(type+"/id/","");
            String id = temp.substring(0, temp.indexOf("/"));

            if (!checkEad(decodeString(id))){
                redirection.setIdNotFound(true);
                redirection.setNewUrl(baseUrl);
                return;
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
                List<CLevel> clevels = cLevelDAO.getCLevel(aiRepositoryCode, type.equals("fa")?FindingAid.class:(type.equals("hg")? HoldingsGuide.class : SourceGuide.class), id, unitid);
                if (clevels != null && clevels.size() > 0) {
                    cid = "C" + clevels.get(0).getId();
                }
                else {
                    redirection.setIdNotFound(true);
                    redirection.setNewUrl(baseUrl);
                    return;
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
                    redirection.setIdNotFound(true);
                    redirection.setNewUrl(baseUrl);
                    return;
                }
            }

            String newUrl = "/advanced-search/search-in-archives/results-(archives)/?&repositoryCode="+aiRepositoryCode+(term != null ? ("&term="+term+"&using="+getEadSearchType(termType)) : "")+"&levelName=clevel&t="+type+"&recordId="+id+"&c="+cid;
            redirection.setNewUrl(baseUrl + newUrl);
        }
        else if (matchesPattern("\\/ead-display\\/-\\/ead\\/pl\\/aicode\\/.+\\/type\\/(fa|hg|sg)\\/id\\/.+(\\/search\\/.+)?", path)) {
            String temp = path.replace("/ead-display/-/ead/pl/aicode/","");
            String aiRepositoryCode = temp.substring(0, temp.indexOf("/"));

            if (!checkArchivalInstitution(aiRepositoryCode)){
                redirection.setIdNotFound(true);
                redirection.setNewUrl(baseUrl);
                return;
            }

            temp = temp.replace(aiRepositoryCode+"/type/","");
            String type = temp.substring(0, temp.indexOf("/"));
            temp = temp.replace(type+"/id/","");
            String id = temp.substring(0, temp.indexOf("/")!=-1?temp.indexOf("/"):temp.length());

            if (!checkEad(decodeString(id))){
                redirection.setIdNotFound(true);
                redirection.setNewUrl(baseUrl);
                return;
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
            redirection.setNewUrl(baseUrl + newUrl);
        }
        else {
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
        }
    }

    private void handlePortalEacDisplay(String baseUrl, String path, String queryString, Redirection redirection) {
        if (matchesPattern("\\/eac-display\\/-\\/eac\\/pl\\/aicode\\/.+\\/type\\/ec\\/id\\/.+(\\/search\\/.+)?", path)) {
            String temp = path.replace("/eac-display/-/eac/pl/aicode/","");
            String aiRepositoryCode = temp.substring(0, temp.indexOf("/"));

            if (!checkArchivalInstitution(aiRepositoryCode)){
                redirection.setIdNotFound(true);
                redirection.setNewUrl(baseUrl);
                return;
            }

            temp = temp.replace(aiRepositoryCode+"/type/","");
            String type = temp.substring(0, temp.indexOf("/"));
            temp = temp.replace(type+"/id/","");
            String id = temp.substring(0, temp.indexOf("/")!=-1?temp.indexOf("/"):temp.length());

            if (!checkEac(decodeString(id), aiRepositoryCode)){
                redirection.setIdNotFound(true);
                redirection.setNewUrl(baseUrl);
                return;
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
            redirection.setNewUrl(baseUrl + newUrl);
        }
        else {
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
        }
    }

    private void handlePortalFeatureDocument(String baseUrl, String path, String queryString, Redirection redirection) {
        if (path.equals("/featured-document") || path.startsWith("/featured-document#")){
            String newUrl = "/explore/?tab=documents";
            redirection.setNewUrl(baseUrl + newUrl);
        }
        else if (path.startsWith("/featured-document/-/fed/pk/")){
            String id = path.replace("/featured-document/-/fed/pk/", "");
            if (id.indexOf("#") != -1){
                id = id.substring(0, id.indexOf("#"));
            }

            Map<String, String> mapping = getFeaureDocumentMapping();

            if (mapping.containsKey(id)) {
                String newUrl = "/explore/highlights/"+mapping.get(id);
                redirection.setNewUrl(baseUrl + newUrl);
            }
            else {
                redirection.setHandled(false);
                redirection.setNewUrl(baseUrl);
            }
        }
        else {
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
        }
    }

    private void handlePortalSignIn(String baseUrl, String path, String queryString, Redirection redirection) {

        String oldBaseUrl = RedirectsPropertiesUtil.get("ape.portal.old.domain");
        String newUrl = oldBaseUrl+ path + (queryString!=null ? ("?" + queryString) : "");
        redirection.setPassThrough(true); // do not show landing page
        redirection.setNewUrl(newUrl);

//        if (path.equals("/sign-in") || path.equals("/sign-in/")){
//
//        }
//        else {
//            redirection.setHandled(false);
//            redirection.setNewUrl(baseUrl);
//        }
    }

    private void handlePortalTopic(String baseUrl, String path, String queryString, Redirection redirection) {
        if (path.equals("/topics") || path.startsWith("/topics/")){
            String newUrl = "/explore/?tab=topics";
            redirection.setNewUrl(baseUrl + newUrl);
        }
        else if (path.startsWith("/search/-/s/n/topic/")){
            String id = path.replace("/search/-/s/n/topic/", "");

            Map<String, String> mapping = getTopicsMapping();

            if (mapping.containsKey(id)) {
                String newUrl = "/explore/topics/"+mapping.get(id);
                redirection.setNewUrl(baseUrl + newUrl);
            }
            else {
                redirection.setHandled(false);
                redirection.setNewUrl(baseUrl);
            }
        }
        else {
            redirection.setHandled(false);
            redirection.setNewUrl(baseUrl);
        }
    }

    private boolean checkArchivalInstitution(String aiRepositoryCode){
        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);

        if (archivalInstitution == null){
            return false;
        }

        return true;
    }

    private boolean checkEad(String id){
        EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
        EadContent eadContent = eadContentDAO.getEadContentByEadid(id);

        if (eadContent == null){
            return false;
        }

        return true;
    }

    private boolean checkEac(String id, String aiRepositoryCode){
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = eacCpfDAO.getEacCpfByIdentifier(aiRepositoryCode, id, false);

        if (eacCpf == null){
            return false;
        }

        return true;
    }

    private String decodeString(String s){
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

    private Map<String, String> getWikiMapping(){
        Map<String, String> response = new HashMap<>();

        response.put("/index.php/Main_Page","/tools");
        response.put("/index.php/Category:Introduction","/about-us");
        response.put("/index.php/Background_Archives_Portal_Europe","/about-us/background");
        response.put("/index.php/Concept_Archives_Portal_Europe","/about-us/the-portal");
        response.put("/index.php/How_to_join_the_network","/about-us/join-us/?tab=content-provider");
        response.put("/index.php/How_to_contribute_content","/about-us/join-us/?tab=content-provider");
        response.put("/index.php/How_to_use_topics","/tools/research-tools");
        response.put("/index.php/Category:Portal_search_and_find","/tools/research-tools");
        response.put("/index.php/Searching","/tools/research-tools");
        response.put("/index.php/Search_results","/tools/research-tools");
        response.put("/index.php/Searching_with_topics","/tools/research-tools");
        response.put("/index.php/Finding_institutions","/tools/research-tools");
        response.put("/index.php/Exploring_featured_documents","/explore");
        response.put("/index.php/Using_My_Pages","/about-us/join-us/?tab=registered-user");
        response.put("/index.php/Multilingual_search","/tools/research-tools");
        response.put("/index.php/Glossary","/?show=help");
        response.put("/index.php/Category:Dashboard_IM_manual","/tools/for-content-providers/data-preparation/institution-manager-manual");
        response.put("/index.php/Institution_Manager_manual_-_General_overview","/tools/for-content-providers/data-preparation/institution-manager-manual");
        response.put("/index.php/Institution_Manager_manual_-_Your_account","/tools/for-content-providers/data-preparation/institution-manager-manual");
        response.put("/index.php/Institution_Manager_manual_-_Manage_your_institution","/tools/for-content-providers/data-preparation/institution-manager-manual");
        response.put("/index.php/Institution_Manager_manual_-_Manage_your_EAD_and_EAC-CPF_files","/tools/for-content-providers/data-preparation/institution-manager-manual");
        response.put("/index.php/Institution_Manager_manual_-_Optional_actions","/tools/for-content-providers/data-preparation/institution-manager-manual");
        response.put("/index.php/Category:Dashboard_CM_manual","/tools/for-content-providers/data-preparation/country-manager-manual");
        response.put("/index.php/CM_manual_Introduction","/tools/for-content-providers/data-preparation/country-manager-manual");
        response.put("/index.php/CM_manual_Your_account","/tools/for-content-providers/data-preparation/country-manager-manual");
        response.put("/index.php/CM_manual_Manage_your_archival_landscape","/tools/for-content-providers/data-preparation/country-manager-manual");
        response.put("/index.php/CM_manual_Manage_topic_mappings_and_content","/tools/for-content-providers/data-preparation/country-manager-manual");
        response.put("/index.php/CM_manual_Create_manage_institution_managers","/tools/for-content-providers/data-preparation/country-manager-manual");
        response.put("/index.php/CM_manual_Monitor_your_country","/tools/for-content-providers/data-preparation/country-manager-manual");
        response.put("/index.php/Category:Tools_DPT_manual","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_Introduction","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_Installation_and_launch","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_Overview_main_page","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_The_Menus","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_The_List","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_The_Tabs","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_Additional_functionalities","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_Update_of_the_tool","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_Troubleshooting","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/DPT_manual_Annexes","/tools/for-content-providers/data-preparation/data-preparation-tool-manual");
        response.put("/index.php/Category:Tools_OAI_Harvester_manual","/tools/for-content-providers/data-preparation/oai-harvester-manual");
        response.put("/index.php/OAI_manual_Introduction","/tools/for-content-providers/data-preparation/oai-harvester-manual");
        response.put("/index.php/OAI_manual_Installation_and_launch","/tools/for-content-providers/data-preparation/oai-harvester-manual");
        response.put("/index.php/OAI_manual_Set_up_the_harvest","/tools/for-content-providers/data-preparation/oai-harvester-manual");
        response.put("/index.php/OAI_manual_Troubleshooting","/tools/for-content-providers/data-preparation/oai-harvester-manual");
        response.put("/index.php/Category:Standards","/tools/for-content-providers/standards");
        response.put("/index.php/apeEAD","/tools/for-content-providers/standards/apeead");
        response.put("/index.php/EAD3","/tools/for-content-providers/standards/ead3-in-archives-portal-europe");
        response.put("/index.php/EAD3_implementation_guideline","/tools/for-content-providers/standards/ead3-in-archives-portal-europe");
        response.put("/index.php/EAD3_apetypes","/uploads/generated_pdfs/ead3_apetypes.pdf");
        response.put("/index.php/apeEAC-CPF","/tools/for-content-providers/standards/apeeac-cpf");
        response.put("/index.php/apeMETS","/tools/for-content-providers/standards/apemets");
        response.put("/index.php/EAG2012","/tools/for-content-providers/standards/eag2012/");
        response.put("/index.php/APE_API_documentation","/tools/api/how-to-use-the-api");
        response.put("/index.php/searchEad","/tools/api/how-to-use-the-api");
        response.put("/index.php/searchEadDoclist","/tools/api/how-to-use-the-api");
        response.put("/index.php/searchEadDescendants","/tools/api/how-to-use-the-api");
        response.put("/index.php/searchEadChildren","/tools/api/how-to-use-the-api");
        response.put("/index.php/searchEadDescendantsWithAncestors","/tools/api/how-to-use-the-api");
        response.put("/index.php/searchEadFindingAidNo","/tools/api/how-to-use-the-api");
        response.put("/index.php/searchEac","/tools/api/how-to-use-the-api");
        response.put("/index.php/contentEadArchdesc","/tools/api/how-to-use-the-api");
        response.put("/index.php/contentEadClevel","/tools/api/how-to-use-the-api");
        response.put("/index.php/contentEac-cpf","/tools/api/how-to-use-the-api");
        response.put("/index.php/hierarchyEadChildren","/tools/api/how-to-use-the-api");
        response.put("/index.php/hierarchyEadAncestors","/tools/api/how-to-use-the-api");
        response.put("/index.php/downloadEad","/tools/api/how-to-use-the-api");
        response.put("/index.php/downloadEac-cpf","/tools/api/how-to-use-the-api");
        response.put("/index.php/getInstitutes","/tools/api/how-to-use-the-api");
        response.put("/index.php/getDocs","/tools/api/how-to-use-the-api");
        response.put("/index.php/Category:APE_supporting_software","/tools/for-content-providers/supporting-software");
        response.put("/index.php/How_to_create_EAD_from_scratch_using_ICA-AtoM","/tools/for-content-providers/supporting-software/create-ead-using-atom");
        response.put("/index.php/Installing_Wamp_server_v2.2_on_Windows_7_32-bits","/tools/for-content-providers/supporting-software/create-ead-using-atom");
        response.put("/index.php/Installing_ICA-AtoM_v1.3.0_on_Wamp_server_v2.2","/tools/for-content-providers/supporting-software/create-ead-using-atom");
        response.put("/index.php/Using_ICA-AtoM_v1.3.0_to_create_EAD/XML_finding_aids","/tools/for-content-providers/supporting-software/create-ead-using-atom");
        response.put("/index.php/Overview_APE_supporting_archival_management_software","/tools/for-content-providers/supporting-software");
        response.put("/index.php/NAH_Scope2apeEAD_export","/tools/for-content-providers/supporting-software/export-apeead-from-scopearchiv/");
        response.put("/index.php/Website_design_and_interface","/tools/useful-links");
        response.put("/index.php/Tools","/tools/useful-links");

        return response;
    }

    private Map<String, String> getFeaureDocumentMapping() {
        Map<String, String> response = new HashMap<>();

        response.put("32559", "highlight-adalen-sweden");
        response.put("44018", "highlight-confederation-of-warsaw-poland");
        response.put("53884", "highlight-board-game-united-kingdom");
        response.put("66452", "highlight-charta-of-greece-greece");
        response.put("70342", "highlight-treaty-netherlands-sweden-netherlands");
        response.put("70628", "highlight-ambassador-for-netherlands-sweden");
        response.put("70829", "highlight-treaty-of-friendship-truce-spain");
        response.put("72271", "highlight-dragon-order-hungary");
        response.put("73638", "highlight-gap-iron-curtain-hungary");
        response.put("74117", "highlight-baltic-way-lithuania");
        response.put("77455", "highlight-evangelion-greece");
        response.put("78020", "highlight-will-private-john-brady-ireland");
        response.put("78457", "highlight-fall-of-the-berlin-wall-germany");
        response.put("79103", "highlight-engraving-old-jewish-book-bulgaria");
        response.put("81705", "highlight-acropolis-of-athens-greece");
        response.put("82418", "highlight-maps-of-hayfields-iceland");
        response.put("82726", "highlight-ancha-gospel-georgia");
        response.put("85390", "highlight-map-belgrade-zoo-serbia");
        response.put("92538", "highlight-britain-can-make-it-united-kingdom");
        response.put("93131", "highlight-tavole-di-biccherna-italy");
        response.put("93735", "highlight-pyrrhic-victory-austro-prussian-war-slovenia");
        response.put("94219", "highlight-birth-of-royal-house-romania");
        response.put("94804", "highlight-charter-of-charles-iv-czech-republic");
        response.put("95337", "highlight-panorama-pictures-first-world-war-germany");
        response.put("95770", "highlight-krusensterns-circumnavigation-estonia");
        response.put("96356", "highlight-cartographic-collection-croatia");
        response.put("96714", "highlight-carl-durheims-police-photographs-switzerland");
        response.put("97289", "highlight-pope-gregory-xiii-bull-university-of-vilnius-lithuania");
        response.put("98310", "highlight-allez-democracy-geneva-conventions-switzer.and");
        response.put("98655", "highlight-allez-democracy-law-of-epidaurus-greece");
        response.put("99588", "highlight-coronation-diploma-king-francis-joseph-i-hungary");
        response.put("101013", "highlight-building-peace-peace-of-brest-litovsk-germany");
        response.put("103080", "highlight-allez-democracy-slovenian-independenc-referendum-slovenia");
        response.put("103978", "highlight-first-sketch-eiffel-tower-switzerland");
        response.put("104296", "highlight-industrial-heritage-freedmans-tag-hungary");
        response.put("105838", "highlight-industrial-heritage-from-mines-to-luxury-products-albania");
        response.put("106436", "highlight-industrial-heritage-changing-everyday-life-lithuania");
        response.put("106703", "highlight-industrial-heritage-privilege-from-tailor-and-cropper-guild-of-zilah-hungary");
        response.put("106868", "highlight-industrial-heritage-practice-makes-the-master-germany");
        response.put("107009", "highlight-industrial-heritage-setting-europe-in-motion-greece");
        response.put("108703", "highlight-building-peace-conditions-germany");
        response.put("109358", "highlight-building-peace-treaties-serbia");
        response.put("110172", "highlight-industrial-heritage-kegums-latvia");
        response.put("111008", "highlight-imogen-holst-united-kingdom");
        response.put("111841", "highlight-george-ii-rakoczi-hungary");
        response.put("112386", "highlight-simon-ruiz-spain");

        return response;
    }

    private Map<String, String> getTopicsMapping() {
        Map<String, String> response = new HashMap<>();

        response.put("agriculture","agriculture");
        response.put("architecture","architecture");
        response.put("royalty","aristocracy");
        response.put("armed.forces","armed-forces");
        response.put("arts","arts");
        response.put("buildings","buildings");
        response.put("catholicism","catholicism");
        response.put("charity","charity");
        response.put("charters","charters");
        response.put("churches","churches");
        response.put("church.records.and.registers","church-records-and-registers");
        response.put("colonialism","colonialism");
        response.put("communism","communism");
        response.put("concentration.camp","concentration-camps");
        response.put("crime","crime");
        response.put("culture","culture");
        response.put("democracy","democracy");
        response.put("economics","economics");
        response.put("education","education");
        response.put("european.union","european-union");
        response.put("french.revolution","french-revolution");
        response.put("german.democratic.republic","gdr-(german-democratic-republic)");
        response.put("germany.sed.fdgb","gdr-parties");
        response.put("genealogy","genealogy");
        response.put("genealogy.archives","genealogy-archives");
        response.put("health","health");
        response.put("heresy","heresy");
        response.put("industrialisation","industry");
        response.put("justice","justice");
        response.put("lifestyle","leisure");
        response.put("maps","maps");
        response.put("medical.sciences","medical-sciences");
        response.put("medieval.period","middle-ages");
        response.put("monasteries","monasteries");
        response.put("municipal.government","municipal-government");
        response.put("music","music");
        response.put("french.napoleon.i","napoleon-i");
        response.put("french.napoleon.iii","napoleon-iii");
        response.put("national.administration","national-government");
        response.put("notaries","notaries");
        response.put("photography","photography");
        response.put("politics","politics");
        response.put("population.censuses","population-censuses");
        response.put("poverty","poverty");
        response.put("protestantism","protestantism");
        response.put("religion","religion");
        response.put("revolutions.of.1848","revolutions-of-1848");
        response.put("schools","schools");
        response.put("science","science");
        response.put("slavery","slavery");
        response.put("social.history","social-history");
        response.put("socialism","socialism");
        response.put("statistics","statistics");
        response.put("taxation","taxation");
        response.put("trade.unions","trade-unions");
        response.put("transport","transport");
        response.put("universities","universities");
        response.put("wars.events","wars");
        response.put("women","women");
        response.put("first.world.war","world-war-i");
        response.put("second.world.war","world-war-ii");

        return response;
    }

    private Map<String, String> getGenericMapping() {
        Map<String, String> response = new HashMap<>();

        response.put("/developments","/tools/for-content-providers");
        response.put("/contact","/contact-us");
        response.put("/eag","/tools/for-content-providers/standards/eag");
        response.put("/help_old","?show=help");
//        response.put("/help/searching","/tools/research-tools");
//        response.put("/help/search-results","/tools/research-tools");
//        response.put("/help/topics","/tools/research-tools");
//        response.put("/help/finding-institutions","/tools/research-tools");
//        response.put("/help/sign-in","/about-us/join-us/?tab=registered-user");
//        response.put("/help/glossary","?show=help");

        response.put("/developments/","/tools/for-content-providers");
        response.put("/contact/","/contact-us");
        response.put("/eag/","/tools/for-content-providers/standards/eag");
        response.put("/help_old/","?show=help");
//        response.put("/help/searching/","/tools/research-tools");
//        response.put("/help/search-results/","/tools/research-tools");
//        response.put("/help/topics/","/tools/research-tools");
//        response.put("/help/finding-institutions/","/tools/research-tools");
//        response.put("/help/sign-in/","/about-us/join-us/?tab=registered-user");
//        response.put("/help/glossary/","?show=help");

        return response;
    }

    private Map<String, String> getApefMapping() {
        Map<String, String> response = new HashMap<>();

        response.put("/index.php","/about-us");
        response.put("/index.php/provide-content","/about-us/join-us/?tab=content-provider");
        response.put("/index.php/country-manager-network","/about-us/who-we-are/?tab=country-manager");
        response.put("/index.php/get-involved","/about-us/join-us/?tab=associate");
        response.put("/index.php/learn-more","/tools/for-content-providers");
        response.put("/index.php/about-us","/about-us/who-we-are");
        response.put("/index.php/news","/about-us/news-events");
        response.put("/index.php/contact","/contact-us");

        return response;
    }

    private Map<String, String> getBlogMapping() {
        Map<String, String> response = new HashMap<>();

        response.put("/about","/about-us");
        response.put("/contact","contact-us");
        response.put("/coming-up","/blog");

        return response;
    }

    private String getEadSearchType(String type){
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

    private Map<String, String> getXsdMapping(){

        Map<String, String> response = new HashMap<>();

        response.put("/Portal/profiles/apeEAD.xsd","/ead/apeEAD.xsd");
        response.put("/Portal/profiles/ead_2002.xsd","/ead/ead_2002.xsd");
        response.put("/Portal/profiles/ead3.xsd","/ead/ead3.xsd");
        response.put("/Portal/profiles/apeEAC-CPF.xsd","/eac-cpf/apeEAC-CPF.xsd");
        response.put("/Portal/profiles/cpf.xsd","/eac-cpf/cpf.xsd");
        response.put("/Portal/profiles/eag_2012.xsd","/eag/eag_2012.xsd");
        response.put("/Portal/profiles/apeMETS.xsd","/mets/apeMETS.xsd");
        response.put("/Portal/profiles/apeMETSxlink.xsd","/mets/apeMETSxlink.xsd");
        response.put("/Portal/profiles/apeMETSRights.xsd","/mets/apeMETSRights.xsd");

        return response;
    }

    private String getEacSearchType(String type){
        return type;
    }
}
