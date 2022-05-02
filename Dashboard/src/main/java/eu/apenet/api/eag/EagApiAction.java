package eu.apenet.api.eag;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.ClasspathURIResolver;
import eu.apenet.commons.xslt.EagXslt;
import eu.apenet.commons.xslt.extensions.ResourcebundleExtension;
import eu.apenet.commons.xslt.extensions.RetrieveRelatedAIIdExtension;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import net.sf.saxon.s9api.*;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

public class EagApiAction {
    private String html = "";
//    private String json = "";

    private String aiId;
    private String aiName;
    private String aiRepositoryCode;
    private Boolean preview = false;

    private Boolean hasEacCpfs;
    private Boolean hasFindingAids;
    private Boolean hasSourceGuides;
    private Boolean hasHoldingGuides;

    public EagApiAction() {

    }

    public String execute() {

        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();

        ArchivalInstitution archivalInstitution;
        if (aiId != null) {
            archivalInstitution = archivalInstitutionDAO.findById(Integer.parseInt(aiId));
        }
        else if (aiName != null) {
            archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByAiName(aiName);
        }
        else {
            archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);
        }
        String eagPath = APEnetUtilities.getDashboardConfig().getRepoDirPath() + archivalInstitution.getEagPath();

        StringWriter stringWriter = new StringWriter();
        try {
            EagXslt.displayAiDetails(preview, stringWriter, new File(eagPath), new StrutsResourceBundleSource(), null, null);
        } catch (SaxonApiException e) {
            e.printStackTrace();
        }

        this.html = stringWriter.toString();

//        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><temp>"+stringWriter.toString()+"</temp>";
//
//        Source xmlSource = new StreamSource(new ByteArrayInputStream(input.getBytes()));
//        String xslt = "xsl/xml-to-json.xsl";
//        try {
//            XsltExecutable executable = getXsltExecutable(xslt);
//            XsltTransformer transformer = executable.load();
//            transformer.setSource(xmlSource);
//            Serializer serializer = new Serializer();
//            StringWriter jsonStringWriter = new StringWriter();
//            serializer.setOutputWriter(jsonStringWriter);
//            transformer.setDestination(serializer);
//            transformer.transform();
//            String response = jsonStringWriter.toString();
//            this.json = response;
//        } catch (SaxonApiException e) {
//            e.printStackTrace();
//        }

        ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
        eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
        eadSearchOptions.setPublished(true);
        eadSearchOptions.setContentClass(HoldingsGuide.class);
        this.hasHoldingGuides = eadDAO.existEads(eadSearchOptions);
        eadSearchOptions.setContentClass(FindingAid.class);
        this.hasFindingAids = eadDAO.existEads(eadSearchOptions);
        eadSearchOptions.setContentClass(SourceGuide.class);
        this.hasSourceGuides = eadDAO.existEads(eadSearchOptions);
        eadSearchOptions.setContentClass(EacCpf.class);
        this.hasEacCpfs = eacCpfDAO.existEacCpfs(eadSearchOptions);

        return Action.SUCCESS;
    }

    private static XsltExecutable getXsltExecutable(String xslUrl) throws SaxonApiException {
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Source xsltSource = new StreamSource(classLoader.getResourceAsStream(xslUrl));
        Processor processor = new Processor(false);
        XsltCompiler compiler = processor.newXsltCompiler();
        compiler.setURIResolver(new ClasspathURIResolver(xslUrl));

        return compiler.compile(xsltSource);
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

    public String getAiId() {
        return aiId;
    }

    public Boolean getPreview() {
        return preview;
    }

    public void setPreview(Boolean preview) {
        this.preview = preview;
    }

//    public void setJson(String json) {
//        this.json = json;
//    }
//
//    public String getJson() {
//        return json;
//    }


    public void setHasEacCpfs(Boolean hasEacCpfs) {
        this.hasEacCpfs = hasEacCpfs;
    }

    public void setHasFindingAids(Boolean hasFindingAids) {
        this.hasFindingAids = hasFindingAids;
    }

    public void setHasSourceGuides(Boolean hasSourceGuides) {
        this.hasSourceGuides = hasSourceGuides;
    }

    public void setHasHoldingGuides(Boolean hasHoldingGuides) {
        this.hasHoldingGuides = hasHoldingGuides;
    }

    public Boolean getHasEacCpfs() {
        return hasEacCpfs;
    }

    public Boolean getHasFindingAids() {
        return hasFindingAids;
    }

    public Boolean getHasSourceGuides() {
        return hasSourceGuides;
    }

    public Boolean getHasHoldingGuides() {
        return hasHoldingGuides;
    }

    public void setAiName(String aiName) {
        this.aiName = aiName;
    }

    public String getAiName() {
        return aiName;
    }

    public void setAiRepositoryCode(String aiRepositoryCode) {
        this.aiRepositoryCode = aiRepositoryCode;
    }

    public String getAiRepositoryCode() {
        return aiRepositoryCode;
    }
}
