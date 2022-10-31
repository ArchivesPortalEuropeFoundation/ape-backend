package eu.apenet.redirects.util;

public class Redirection {

    public static final String REDIRECTION_TYPE_PORTAL = "REDIRECTION_TYPE_PORTAL";
    public static final String REDIRECTION_TYPE_BLOG = "REDIRECTION_TYPE_BLOG";
    public static final String REDIRECTION_TYPE_APEF = "REDIRECTION_TYPE_APEF";
    public static final String REDIRECTION_TYPE_WIKI = "REDIRECTION_TYPE_WIKI";
    public static final String REDIRECTION_TYPE_XSD = "REDIRECTION_TYPE_XSD";
    public static final String REDIRECTION_TYPE_DPT = "REDIRECTION_TYPE_DPT";

    private String type;
    private String newUrl;
    private String path;
    private String queryString;
    private boolean handled = true;
    private boolean idNotFound = false;
    private String referer;
    private String locale;
    private boolean passThrough = false;

    public void setType(String type) {
        this.type = type;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getType() {
        return type;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setIdNotFound(boolean idNotFound) {
        this.idNotFound = idNotFound;
    }

    public boolean isIdNotFound() {
        return idNotFound;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getReferer() {
        return referer;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }

    public void setPassThrough(boolean passThrough) {
        this.passThrough = passThrough;
    }

    public boolean isPassThrough() {
        return passThrough;
    }
}
