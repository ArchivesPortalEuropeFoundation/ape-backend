package eu.apenet.redirects.util;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

public class RedirectService {

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

    public String handleRequest(HttpServletRequest httpServletRequest){

        String scheme = httpServletRequest.getScheme();
        String serverName = httpServletRequest.getServerName();
        int serverPort = httpServletRequest.getServerPort();

        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            baseUrl.append(":").append(serverPort);
        }

        return handleQueryString(baseUrl.toString(), httpServletRequest.getPathInfo(), httpServletRequest.getQueryString());
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
//            return handleQueryString(baseUrl.toString(), url.getPath().substring(0), url.getQuery());
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    private String handleQueryString(String baseUrl, String path, String queryString){

        if (matchesPattern("\\/directory\\/-\\/dir\\/ai\\/code\\/.+", path)){
            String aiRepositoryCode = null;
            if (matchesPattern("\\/directory\\/-\\/dir\\/ai\\/code\\/.+\\/search\\/.+", path)) {
                String temp = path.replace("/directory/-/dir/ai/code/","");
                aiRepositoryCode = temp.substring(0, temp.indexOf("/"));
            }
            else {
                aiRepositoryCode = path.substring(path.lastIndexOf("/") + 1, path.length());
            }
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);

            String newUrl = "/advanced-search/search-in-institutions/results-(institutions)/?repositoryCode=";
            return "directory url: " + baseUrl+newUrl+aiRepositoryCode;
        }
        else if (path.equals("/directory")){
            String newUrl = "/find-an-institution";
            return "directory url: " + baseUrl+newUrl;
        }
        return path+" -- "+queryString;
    }

    private boolean matchesPattern(String pattern, String text){
        return text.matches(pattern);
    }
}
