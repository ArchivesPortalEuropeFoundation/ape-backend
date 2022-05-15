package eu.apenet.api.eac;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.eac.EacXslt;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
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

public class EacApiAction {
    private String html = "";

    private static final List<SolrField> DEFAULT_HIGHLIGHT_FIELDS = SolrField.getDefaults();
    public static final String EACCPFDETAILS_XSLT = "eaccpfdetails";
    public static final String EACCPFDETAILS_PREVIEW_XSLT = "eaccpfdetailspreview";

    private String eaccpfId;
    private String aiId;
    private String aiName;
    private String aiRepositoryCode;
    private Boolean preview = false;
    private String element;
    private String term;
    private String translationLanguage = "default";
    private String langNavigator = "en";
    private String aiCodeUrl = "http://localhost:8080/web/guest/directory/-/dir/ai/code";
    private String eacUrlBase = "http://localhost:8080/web/guest/eac-display/-/eac/pl";
    private String eadUrl = "http://localhost:8080/web/guest/ead-display/-/ead/pl";
    private String secondDisplayUrl = null;

    private final static Map<String, String> xsltUrls = new HashMap<String,String>();
    static {
        xsltUrls.put(EACCPFDETAILS_XSLT, "xsl/eaccpf/eaccpfdetails.xsl");
        xsltUrls.put(EACCPFDETAILS_PREVIEW_XSLT,"xsl/eaccpf/eaccpfdetails-preview.xsl");
    }

    public EacApiAction() {

    }

    public String execute() throws IOException, SaxonApiException {
        HttpServletResponse response = ServletActionContext.getResponse();

        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
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

        EacCpf eaccpf = eacCpfDAO.getEacCpfByIdentifier(aiRepositoryCode, eaccpfId,!preview);
        ArchivalInstitution archivalInstitution = eaccpf.getArchivalInstitution();

        File file= new File(APEnetUtilities.getApePortalAndDashboardConfig().getRepoDirPath() + eaccpf.getPath());
        if (file.exists()){
            FileReader eacFile = new FileReader(file);
            Source xmlSource = new StreamSource(new StringReader(this.readFile(eacFile)));
            List<SolrField> highlightFields = SolrField.getSolrFieldsByIdString(element);
            if (highlightFields.size() == 0) {
                highlightFields = DEFAULT_HIGHLIGHT_FIELDS;
            }

            Integer aiIdInt = archivalInstitution.getAiId();
//            if (StringUtils.isNotBlank(archivalInstitution.getAiId())) {
//                aiIdInt = Integer.parseInt(aiId);
//            }
            String xslLocation = xsltUrls.get(preview?EACCPFDETAILS_PREVIEW_XSLT:EACCPFDETAILS_XSLT);
            if (xslLocation == null){
//                LOG.warn("EAC-CPF xsl type does not exist: " + getType());
            }else {
                StringWriter stringWriter = new StringWriter();

                EacXslt.convertEacToHtml(xslLocation, stringWriter, xmlSource, term,
                        highlightFields, new StrutsResourceBundleSource(), secondDisplayUrl, aiIdInt, getPreview(),
                        /*APEnetUtilities.getApePortalConfig().getSolrStopwordsUrl()*/null, this.getTranslationLanguage(), this.getAiCodeUrl(),
                        this.getEacUrlBase(), this.getEadUrl(), this.getLangNavigator());
                this.html = stringWriter.toString();
            }
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

    public void setEaccpfId(String eaccpfId) {
        this.eaccpfId = eaccpfId;
    }

    public void setAiName(String aiName) {
        this.aiName = aiName;
    }

    public void setPreview(Boolean preview) {
        this.preview = preview;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setTranslationLanguage(String translationLanguage) {
        this.translationLanguage = translationLanguage;
    }

    public void setLangNavigator(String langNavigator) {
        this.langNavigator = langNavigator;
    }

    public void setAiCodeUrl(String aiCodeUrl) {
        this.aiCodeUrl = aiCodeUrl;
    }

    public void setEacUrlBase(String eacUrlBase) {
        this.eacUrlBase = eacUrlBase;
    }

    public void setEadUrl(String eadUrl) {
        this.eadUrl = eadUrl;
    }

    public String getEaccpfId() {
        return eaccpfId;
    }

    public String getAiName() {
        return aiName;
    }

    public Boolean getPreview() {
        return preview;
    }

    public String getElement() {
        return element;
    }

    public String getTerm() {
        return term;
    }

    public String getTranslationLanguage() {

        if (translationLanguage.equals("default") || translationLanguage.equals("showAll")){
            return translationLanguage;
        }
        Map<String, String> langMap = APEnetUtilities.getIso2ToIso3LanguageCodesMap();

        String langTransIso3 = "eng";
        if (langMap.get(translationLanguage)!= null && !langMap.get(translationLanguage).isEmpty()){
            langTransIso3 = langMap.get(translationLanguage);
        }
        return langTransIso3;
    }

    public String getLangNavigator() {
        return langNavigator;
    }

    public String getAiCodeUrl() {
        return aiCodeUrl;
    }

    public String getEacUrlBase() {
        return eacUrlBase;
    }

    public String getEadUrl() {
        return eadUrl;
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
}
