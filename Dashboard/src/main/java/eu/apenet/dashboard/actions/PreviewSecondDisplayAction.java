package eu.apenet.dashboard.actions;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.services.ead3.Ead3Service;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ead3;

public class PreviewSecondDisplayAction extends AbstractInstitutionAction {

    private static final Logger LOGGER = Logger.getLogger(PreviewSecondDisplayAction.class);
    private String id;
    private String xmlTypeId;
    private String modx;

    public String getId() {
        return id;
    }

    public String getXmlTypeId() {
        return xmlTypeId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setXmlTypeId(String xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    public void setModx(String modx) {
        this.modx = modx;
    }

    public String getModx() {
        return modx;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1953305569537661585L;

    public String execute() throws Exception {
        try {
            XmlType xmlType = XmlType.getType(Integer.parseInt(getXmlTypeId()));
            if (StringUtils.isNotBlank(getId()) && StringUtils.isNumeric(getId())) {
                //String language = getServletRequest().getLocale().getLanguage();
                Object language = null;
                Map<String, Object> session = ActionContext.getContext().getSession();
                if (session.get("WW_TRANS_I18N_LOCALE") == null) {
                    language = session.get("org.apache.tiles.LOCALE");
                } else {
                    language = session.get("WW_TRANS_I18N_LOCALE");
                }
                if (language == null) {
                    language = getServletRequest().getLocale().getLanguage();
                }
                if (xmlType.equals(XmlType.EAC_CPF)) {
                    EacCpfService.createPreviewHTML(xmlType, Integer.parseInt(getId()));
                    EacCpf eacCpf = DAOFactory.instance().getEacCpfDAO()
                            .findById(Integer.parseInt(id), xmlType.getClazz());
                    if (eacCpf != null) {
//                        getServletRequest().setAttribute("identifier", eacCpf.getEncodedIdentifier());
//                        getServletRequest().setAttribute("repoCode",
//                                eacCpf.getArchivalInstitution().getEncodedRepositorycode());
//
//                        String url = "http://" + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN) + "/" + language
//                                + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_EAC_DISPLAY);
//                        getServletRequest().setAttribute("url", url);

                        if ("true".equals(modx)){
                            String url = "https://" + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN_MODX) + "/";
                            url += "advanced-search/search-in-names/results-(names)/?repositoryCode="+eacCpf.getArchivalInstitution().getEncodedRepositorycode()+"&recordId="+eacCpf.getIdentifier();
                            getServletRequest().setAttribute("url", url);
                        }
                        else {
                            String url = "https://" + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN) + "/" + language
                                    + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_EAC_DISPLAY);
                            url += "?p_p_id=eaccpfdisplay_WAR_Portal&p_p_lifecycle=0&p_p_state=pop_up&p_p_mode=view&repositoryCode="+eacCpf.getArchivalInstitution().getEncodedRepositorycode()+"&eaccpfIdentifier="+eacCpf.getEncodedIdentifier()+"&preview=true";
                            getServletRequest().setAttribute("url", url);
                        }

                        return "success-eaccpf";
                    }
                } else if (xmlType.equals(XmlType.EAD_3)) {
                    Ead3Service.createPreviewHTML(xmlType, Integer.parseInt(id));
                    Ead3 ead3 = DAOFactory.instance().getEad3DAO().findById(Integer.parseInt(id));
                    if (ead3 != null && getAiId() != null && getAiId().equals(ead3.getAiId())) {
                        getServletRequest().setAttribute("xmlTypeName", xmlType.getResourceName());
                        getServletRequest().setAttribute("identifier", ead3.getEncodedIdentifier());
                        getServletRequest().setAttribute("repoCode",
                                ead3.getArchivalInstitution().getEncodedRepositorycode());
                        String url = "http://" + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN) + "/" + language
                                + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_EAD_DISPLAY);
                        getServletRequest().setAttribute("url", url);
                        getServletRequest().setAttribute("modx", getModx());
                        return SUCCESS;
                    }
                } else {
                    EadService.createPreviewHTML(xmlType, Integer.parseInt(getId()));
                    Ead ead = DAOFactory.instance().getEadDAO().findById(Integer.parseInt(id), xmlType.getClazz());
                    if (ead != null && getAiId() != null && getAiId().equals(ead.getAiId())) {
//                        getServletRequest().setAttribute("xmlTypeName", xmlType.getResourceName());
//                        getServletRequest().setAttribute("identifier", ead.getEncodedIdentifier());
//                        getServletRequest().setAttribute("repoCode",
//                                ead.getArchivalInstitution().getEncodedRepositorycode());
//                        String url = "http://" + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN) + "/" + language
//                                + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_EAD_DISPLAY);
//                        getServletRequest().setAttribute("url", url);

                        if ("true".equals(modx)){
                            String url = "https://" + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN_MODX) + "/";
                            url += "advanced-search/search-in-archives/results-(archives)/?&repositoryCode="+ead.getArchivalInstitution().getRepositorycode()+"&levelName=archdesc&t="+xmlType.getResourceName()+"&recordId="+ead.getIdentifier();
                            getServletRequest().setAttribute("url", url);
                        }
                        else {
                            String url = "https://" + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN) + "/" + language
                                    + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_EAD_DISPLAY);
                            url += "?p_p_id=eaddisplay_WAR_Portal&p_p_lifecycle=0&p_p_state=pop_up&p_p_mode=view&myaction=displayArchdescAction&xmlTypeName="+xmlType.getResourceName()+"&repoCode="+ead.getArchivalInstitution().getEncodedRepositorycode()+"&eadid="+ead.getEncodedIdentifier()+"&preview=true";
                            getServletRequest().setAttribute("url", url);
                        }

                        return SUCCESS;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(getText("previewseconddisplay.unabletopreview") + " (id,xmlType): (" + getId() + ","
                    + getXmlTypeId() + "): " + e.getMessage(), e);

        }
        addActionError(getText("error.user.second.display.notindexed"));
        return ERROR;

    }

}
