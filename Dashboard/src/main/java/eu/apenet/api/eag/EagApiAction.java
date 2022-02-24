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
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
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
    private String json = "";

    private String aiId;
    private Boolean preview = false;

    public EagApiAction() {

    }

    public String execute() {

        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDAO.findById(Integer.parseInt(aiId));
        String eagPath = APEnetUtilities.getDashboardConfig().getRepoDirPath() + archivalInstitution.getEagPath();

        StringWriter stringWriter = new StringWriter();
        try {
            EagXslt.displayAiDetails(preview, stringWriter, new File(eagPath), new StrutsResourceBundleSource(), null, null);
        } catch (SaxonApiException e) {
            e.printStackTrace();
        }

        this.html = stringWriter.toString();

        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><temp>"+stringWriter.toString()+"</temp>";

        Source xmlSource = new StreamSource(new ByteArrayInputStream(input.getBytes()));
        String xslt = "xsl/xml-to-json.xsl";
        try {
            XsltExecutable executable = getXsltExecutable(xslt);
            XsltTransformer transformer = executable.load();
            transformer.setSource(xmlSource);
            Serializer serializer = new Serializer();
            StringWriter jsonStringWriter = new StringWriter();
            serializer.setOutputWriter(jsonStringWriter);
            transformer.setDestination(serializer);
            transformer.transform();
            String response = jsonStringWriter.toString();
            this.json = response;
        } catch (SaxonApiException e) {
            e.printStackTrace();
        }

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

    public void setJson(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }
}
