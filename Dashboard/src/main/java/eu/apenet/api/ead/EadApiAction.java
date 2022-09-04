package eu.apenet.api.ead;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.eac.EacXslt;
import eu.apenet.commons.xslt.ead.EadXslt;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EadApiAction {
    private String html = "";
    private String childHtml = "";

    private static final List<SolrField> DEFAULT_HIGHLIGHT_FIELDS = SolrField.getDefaults();
    public static final String CDETAILS_CHILD_XSLT = "cdetails-child";
    public static final String FRONTPAGE_XSLT = "frontpage";
    public static final String CDETAILS_XSLT = "cdetails";


    private String element;
    private String term;
    private String type;
    private String xmlType;
    private String preview = "false";
    private String dashboardPreview = "false";
    private String eacUrlBase = "/advanced-search/search-in-names/results-(names)";
    private String secondDisplayUrl = null;

    private String aiId;
    private String aiName;
    private String aiRepositoryCode;

    String eadid;
    String clevelid;
    String clevelunitid;
    String ecId;

    private String max = "10";
    private String page;
    private Long totalNumberOfChildren;

    private final static Map<String, String> xsltUrls = new HashMap<String,String>();
    static {
        xsltUrls.put(CDETAILS_XSLT, "xsl/ead/cdetails.xsl");
        xsltUrls.put(CDETAILS_CHILD_XSLT, "xsl/ead/cdetails-child.xsl");
        xsltUrls.put(FRONTPAGE_XSLT, "xsl/ead/frontpage.xsl");
    }

    public EadApiAction() {

    }

    public String execute() throws IOException, SaxonApiException {
        HttpServletResponse response = ServletActionContext.getResponse();

        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        if (aiId != null){
            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.findById(Integer.parseInt(aiId));
            aiRepositoryCode = archivalInstitution.getRepositorycode();
            aiName = archivalInstitution.getAiname();
        }
        else if (aiName != null){
            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByAiName(aiName);
            aiRepositoryCode = archivalInstitution.getRepositorycode();
            aiId = ""+archivalInstitution.getAiId();
        }
        else {
            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);
            aiId = ""+archivalInstitution.getAiId();
            aiName = archivalInstitution.getAiname();
        }

        XmlType xmlType = XmlType.getTypeByResourceName(getXmlType());

        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();

        String xml = null;
        String xmlchildren = null;

        if (clevelunitid != null){
            List<CLevel> cLevels = cLevelDAO.getCLevel(aiRepositoryCode, xmlType.getEadClazz(), eadid, clevelunitid);
            if (cLevels != null && cLevels.size()>0) {
                clevelid = ""+cLevels.get(0).getId();
            }
        }

        if (clevelid != null){
            CLevel cLevel = cLevelDAO.getCLevel(aiRepositoryCode, xmlType.getEadClazz(), eadid, Long.parseLong(clevelid));

            if (clevelunitid == null){
                clevelunitid = cLevel.getUnitid();
            }

            xml = cLevel.getXml();

            Integer pageNumberInt = 1;
            if (page != null) {
                pageNumberInt = Integer.parseInt(page);
            }
            int orderId = (pageNumberInt - 1) * Integer.parseInt(max);
            List<CLevel> children = cLevelDAO.findChildCLevels(cLevel.getId(), orderId, Integer.parseInt(max));
            totalNumberOfChildren = cLevelDAO.countChildCLevels(cLevel.getId());
            if (totalNumberOfChildren>0) {
                StringBuilder builder = new StringBuilder();
                builder.append("<c xmlns=\"urn:isbn:1-931666-22-9\">");
                for (CLevel child : children) {
                    builder.append(child.getXml());
                }
                builder.append("</c>");
                xmlchildren = builder.toString();
            }
        }
        else {
            Ead ead = eadDAO.getEadByEadid(xmlType.getEadClazz(), Integer.parseInt(aiId), eadid);
            ecId = ead.getId()+"";
            clevelid = ead.getId()+"";
            if (xmlType.equals(XmlType.EAD_FA)){
                clevelid = "F"+clevelid;
            }
            else if (xmlType.equals(XmlType.EAD_HG)){
                clevelid = "H"+clevelid;
            }
            else if (xmlType.equals(XmlType.EAD_SG)){
                clevelid = "S"+clevelid;
            }

            EadContent eadContent = ead.getEadContent();
            xml = eadContent.getXml();
        }

        Source xmlSource = new StreamSource(new StringReader(xml));
        Source xmlSourceChildren = xmlchildren!=null ? new StreamSource(new StringReader(xmlchildren)) : null;
        List<SolrField> highlightFields = SolrField.getSolrFieldsByIdString(element);
        if (highlightFields.size() == 0) {
            highlightFields = DEFAULT_HIGHLIGHT_FIELDS;
        }
        try {
            Integer aiIdInt = null;
            if (StringUtils.isNotBlank(aiId)) {
                aiIdInt = Integer.parseInt(aiId);
            }
            String xslLocation = xsltUrls.get(getType());
            if (xslLocation == null) {
//                LOG.warn("EAD xsl type does not exist: " + getType());
            } else {
                String typeOfDisplay = "normal";
                if ("true".equalsIgnoreCase(getPreview())) {
                    typeOfDisplay = "preview";
                } else if (CDETAILS_CHILD_XSLT.equalsIgnoreCase(getType())) {
                    typeOfDisplay = "child";
                }

                StringWriter stringWriter = new StringWriter();
                EadXslt.convertEadToHtml(xslLocation, stringWriter, xmlSource, term,
                        highlightFields, new StrutsResourceBundleSource(), secondDisplayUrl, aiIdInt,
                        "true".equalsIgnoreCase(getDashboardPreview()), /*APEnetUtilities.getApePortalConfig().getSolrStopwordsUrl()*/null,
                        typeOfDisplay, xmlType, this.getEacUrlBase());
                this.html = stringWriter.toString();

                if (xmlSourceChildren != null){
                    stringWriter = new StringWriter();
                    EadXslt.convertEadToHtml(xsltUrls.get(CDETAILS_CHILD_XSLT), stringWriter, xmlSourceChildren, term,
                            highlightFields, new StrutsResourceBundleSource(), secondDisplayUrl, aiIdInt,
                            "true".equalsIgnoreCase(getDashboardPreview()), /*APEnetUtilities.getApePortalConfig().getSolrStopwordsUrl()*/null,
                            typeOfDisplay, xmlType, this.getEacUrlBase());
                    this.childHtml = stringWriter.toString();
                }
            }
        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
        }

        response.setHeader("Access-Control-Allow-Origin","*");
        return Action.SUCCESS;
    }

    private String readFile(FileReader file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(file);
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(lineSeparator);
        }

        return stringBuilder.toString();
    }

    public void setAiName(String aiName) {
        this.aiName = aiName;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPreview() {
        return preview;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAiName() {
        return aiName;
    }


    public String getElement() {
        return element;
    }

    public String getTerm() {
        return term;
    }

    public String getEacUrlBase() {
        return "http://"+ PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN_MODX) + eacUrlBase;
    }

    public void setSecondDisplayUrl(String secondDisplayUrl) {
        this.secondDisplayUrl = secondDisplayUrl;
    }

    public String getSecondDisplayUrl() {
        return secondDisplayUrl;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setAiId(String aiId) {
        this.aiId = aiId;
    }

    public void setAiRepositoryCode(String aiRepositoryCode) {
        this.aiRepositoryCode = aiRepositoryCode;
    }

    public String getAiId() {
        return aiId;
    }

    public String getAiRepositoryCode() {
        return aiRepositoryCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDashboardPreview(String dashboardPreview) {
        this.dashboardPreview = dashboardPreview;
    }

    public String getDashboardPreview() {
        return dashboardPreview;
    }

    public void setXmlType(String xmlType) {
        this.xmlType = xmlType;
    }

    public String getXmlType() {
        return xmlType;
    }

    public void setEacUrlBase(String eacUrlBase) {
        this.eacUrlBase = eacUrlBase;
    }

    public void setEadid(String eadid) {
        this.eadid = eadid;
    }

    public String getEadid() {
        return eadid;
    }

    public void setClevelid(String clevelid) {
        this.clevelid = clevelid;
    }

    public String getClevelid() {
        return clevelid;
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

    public void setChildHtml(String childHtml) {
        this.childHtml = childHtml;
    }

    public String getChildHtml() {
        return childHtml;
    }

    public void setTotalNumberOfChildren(Long totalNumberOfChildren) {
        this.totalNumberOfChildren = totalNumberOfChildren;
    }

    public Long getTotalNumberOfChildren() {
        return totalNumberOfChildren;
    }

    public void setClevelunitid(String clevelunitid) {
        this.clevelunitid = clevelunitid;
    }

    public String getClevelunitid() {
        return clevelunitid;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId;
    }

    public String getEcId() {
        return ecId;
    }
}
