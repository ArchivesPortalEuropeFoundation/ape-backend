package eu.apenet.dashboard.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TextProviderHelper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

import eu.apenet.dashboard.exception.NotAuthorizedException;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.UserRole;

public final class SecurityContext implements HttpSessionBindingListener, Serializable {

    private static final Logger LOGGER = Logger.getLogger(SecurityContext.class);
    protected static final String VAR_SECURITY_CONTEXT = "var-security-context";
    protected static final String VAR_SECURITY_CONTEXT_WEBDAV = "var-security-context-webdav";
    private String emailAddress;
    private String name;
    private Integer partnerId;
    private Integer countryId = null;
    private Integer refresh_interval = 20; //20ms
    private String countryName = "UNKNOWN";
    private String countryIsoname;
    private List<Integer> aiIds = new ArrayList<Integer>();
    private String role;
    private SecurityContext parent;
    private SelectedArchivalInstitution selectedInstitution;
    private String sessionId;
    private boolean webdav;
    
    protected SecurityContext(User partner, SecurityContext parent) {
        this(partner, false);
        this.parent = parent;
    }
    
    protected SecurityContext(User partner, boolean webdav) {
        this.webdav = webdav;
        role = partner.getUserRole().getRole();
        if (!isAdminOrCoordinator()) {
            countryId = partner.getCountryId();
            countryName = partner.getCountry().getCname();
            countryIsoname = partner.getCountry().getIsoname();
            for (ArchivalInstitution ai : partner.getArchivalInstitutions()) {
                aiIds.add(ai.getAiId());
            }
            aiIds = Collections.unmodifiableList(aiIds);
        }
        partnerId = partner.getId();
        emailAddress = partner.getEmailAddress();
        name = partner.getFirstName() + " " + partner.getLastName();
        // used in globalRefresh_interval to store refresh interval in the session
        refresh_interval = this.getRefresh_interval();
    }
    
    public Integer getPartnerId() {
        return partnerId;
    }
    
    public Integer getCountryId() {
        return countryId;
    }
    
    public List<Integer> getAiIds() {
        return aiIds;
    }
    
    public boolean isWebdav() {
        return webdav;
    }
    
    public String getRole() {
        return role;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getParentName() {
        if (parent != null) {
            return parent.getName();
        }
        return null;
    }

    protected SecurityContext getParent() {
        return parent;
    }

    public boolean isChild() {
        return parent != null;
    }
    
    public boolean isCountryManager() {
        return UserRole.ROLE_COUNTRY_MANAGER.equals(role);
    }
    
    public boolean isInstitutionManager() {
        return UserRole.ROLE_INSTITUTION_MANAGER.equals(role);
    }
    
    public boolean isCountryManagerCoordinator() {
        return UserRole.ROLE_COUNTRY_MANAGER_COORDINATOR.equals(role);
    }
    
    public boolean isAdmin() {
        return UserRole.ROLE_ADMIN.equals(role);
    }
    
    public boolean isAdminOrCoordinator() {
        return UserRole.ROLE_COUNTRY_MANAGER_COORDINATOR.equals(role) || UserRole.ROLE_ADMIN.equals(role);
    }
    
    @Override
    public boolean equals(Object obj) {
        SecurityContext otherSecurityContext = (SecurityContext) obj;
        return this.getPartnerId().equals(otherSecurityContext.getPartnerId());
    }
    
    public static SecurityContext get() {
        HttpSession newSession = ServletActionContext.getRequest().getSession();
        return get(newSession);
    }

    private static SecurityContext get(HttpSession session) {
        return ((SecurityContext) session.getAttribute(VAR_SECURITY_CONTEXT));
    }

    public static SecurityContext getWebDavContext(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return ((SecurityContext) session.getAttribute(VAR_SECURITY_CONTEXT_WEBDAV));
    }

    protected void logout(boolean logoutToParent) {
        HttpServletRequest request = ServletActionContext.getRequest();
        if (logoutToParent) {
            parent.login();
        } else {
            request.getSession().removeAttribute(VAR_SECURITY_CONTEXT);
            request.getSession().invalidate();
        }
    }
    
    protected void login() {
        HttpServletRequest request = ServletActionContext.getRequest();
        login(request);
    }

    protected void login(HttpServletRequest request) {
        request.getSession().invalidate();
        HttpSession newSession = request.getSession(true);
        if (webdav) {
            newSession.setAttribute(VAR_SECURITY_CONTEXT_WEBDAV, this);
        } else {
            newSession.setAttribute(VAR_SECURITY_CONTEXT, this);
        }
        sessionId = newSession.getId();
    }
    
    @Override
    public String toString() {
        String result = this.getEmailAddress();
        if (parent != null) {
            result += " (" + parent.toString() + ") ";
        }
        if (!isAdminOrCoordinator()) {
            result += " - " + countryIsoname + " ";
            if (selectedInstitution != null) {
                result += selectedInstitution.getId();
            }
            result += " - ";
        }
        return result;
    }
    
    public String getCountryName() {
        return countryName;
    }
    
    public String getCountryIsoname() {
        return countryIsoname;
    }
    
    public String getLocalizedCountryName() {
        ValueStack valueStack = ActionContext.getContext().getValueStack();
        String countryKey = "country." + countryName.toLowerCase().replace(" ", "_");
        return TextProviderHelper.getText(countryKey, countryName, valueStack);
    }
    
    protected void changeArchivalInstitution(ArchivalInstitution archivalInstitution) {
        boolean authorized = false;
        if (isCountryManager()) {
            authorized = archivalInstitution.getPartnerId() == null && countryId.equals(archivalInstitution.getCountryId());
        } else {
            authorized = aiIds.contains(archivalInstitution.getAiId());
        }
        if (authorized) {
            selectedInstitution = new SelectedArchivalInstitution(archivalInstitution.getAiId(),
                    archivalInstitution.getAiname());
        }
    }
    
    public void checkAuthorized(ArchivalInstitution archivalInstitution) {
        checkAuthorized(archivalInstitution.getAiId());
    }
    
    public void checkAuthorized(Ead ead) {
        checkAuthorized(ead.getAiId());
    }
    
    public void checkAuthorized(EacCpf eacCpf) {
        checkAuthorized(eacCpf.getAiId());
    }
    
    public void checkAuthorized(Ead3 ead3) {
        checkAuthorized(ead3.getAiId());
    }

    public void checkAuthorizedToManageQueue() {
        if (!isAdmin()) {
            throw new NotAuthorizedException("To index from queue you need to be admin");
        }
    }

    public void checkAuthorized(Integer id) {
        if (selectedInstitution == null || !selectedInstitution.getId().equals(id)) {
            throw new NotAuthorizedException("This institution is not selected");
        }
    }
    
    public SelectedArchivalInstitution getSelectedInstitution() {
        return selectedInstitution;
    }
    
    public static class SelectedArchivalInstitution implements Serializable {

        private Integer id;
        private String name;
        
        protected SelectedArchivalInstitution(Integer aiId, String name) {
            this.id = aiId;
            this.name = name;
        }
        
        public Integer getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
    }
    
    protected String getSessionId() {
        return sessionId;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        if (event.getValue() instanceof SecurityContext) {
            if (!webdav) {
                SecurityContextContainer.putSecurityContext((SecurityContext) event.getValue());
            }
        }
    }
    
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        if (!webdav) {
            SecurityContextContainer.deleteSecurityContext(this);
        }
    }
    
    public Integer getRefresh_interval() {
        return refresh_interval;
    }
    
    public void setRefresh_interval(Integer refresh_interval) {
        this.refresh_interval = refresh_interval;
    }
}
