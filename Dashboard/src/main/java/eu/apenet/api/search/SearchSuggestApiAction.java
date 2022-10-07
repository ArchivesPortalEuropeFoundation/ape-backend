package eu.apenet.api.search;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.solr.*;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.eac.EacXslt;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;

public class SearchSuggestApiAction {

    protected static final String END_ARRAY = "]\n";
    protected static final String START_ARRAY = "[\n";
    protected static final String END_ITEM = "}";
    protected static final String START_ITEM = "{";
    protected static final String COMMA = ",";

    protected static final String UTF8 = "UTF-8";

    public static final String EAD = "ead";
    public static final String EAC_CPF = "eaccpf";
    public static final String EAG = "eag";

    private String sourceType;
    private String term;
    private String request_locale="en";

    private final Logger log = Logger.getLogger(getClass());

    public SearchSuggestApiAction() {

    }

    public String execute() throws IOException, SaxonApiException {
        HttpServletResponse response = ServletActionContext.getResponse();

        writeJSON(getSourceType(), getTerm(), response);

        return Action.NONE;
    }

    public void writeJSON(String sourceType, String term, HttpServletResponse response) {

        try {
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale(getRequest_locale()));
            StringBuilder builder = new StringBuilder();
            builder.append(START_ARRAY);
            if (StringUtils.isNotBlank(getTerm())) {
                List<AutocompletionResult> results = new ArrayList<AutocompletionResult>();
                AbstractSolrServerHolder abstractSearcher = null;
                if (EAD.equals(getSourceType())) {
                    abstractSearcher = getEadSearcher();
                    add(results,abstractSearcher, getTerm(), null);
                    write(builder, results, false, null,numberFormat);
                } else if (EAC_CPF.equals(getSourceType())) {
                    abstractSearcher = getEacCpfSearcher();
                    add(results,abstractSearcher, getTerm(), null);
                    write(builder, results, false, null,numberFormat);
                }  else if (EAG.equals(getSourceType())) {
                    abstractSearcher = getEagSearcher();
                    add(results,abstractSearcher, getTerm(), null);
                    write(builder, results, false, null,numberFormat);
                }
                else {
                    add(results, getEadSearcher(), getTerm(), "archives");
                    add(results, getEacCpfSearcher(), getTerm(), "names");
                    add(results, getEagSearcher(), getTerm(), "institutions");
                    write(builder, results, true, new StrutsResourceBundleSource(), numberFormat);
                }
            }
            builder.append(END_ARRAY);
            writeToResponseAndClose(builder, response);

        } catch (Exception e) {

        }

    }

    private AbstractSolrServerHolder getEadSearcher(){
        return EadSolrServerHolder.getInstance();
    }

    private AbstractSolrServerHolder getEagSearcher(){
        return EagSolrServerHolder.getInstance();
    }

    private AbstractSolrServerHolder getEacCpfSearcher(){
        return EacCpfSolrServerHolder.getInstance();
    }

    private void writeToResponseAndClose(StringBuilder stringBuilder, HttpServletResponse resourceResponse)
            throws UnsupportedEncodingException, IOException {
        resourceResponse.setCharacterEncoding(UTF8);
        resourceResponse.setContentType("application/json");
        resourceResponse.setHeader("Access-Control-Allow-Origin","*");

        Writer writer =  new OutputStreamWriter(resourceResponse.getOutputStream(), UTF8);
//        Writer writer = getResponseWriter(resourceResponse);
        writer.write(stringBuilder.toString());
        writer.flush();
        writer.close();
    }

    private static void write(StringBuilder builder, List<AutocompletionResult> results, boolean advanced, StrutsResourceBundleSource source, NumberFormat numberFormat) {
        boolean isAdded = false;
        if (advanced) {
            Collections.sort(results);
            for (int i = 0; i < 10 && i < results.size(); i++) {
                AutocompletionResult result = results.get(i);
                if (isAdded) {
                    builder.append(COMMA);
                } else {
                    isAdded = true;
                }
                builder.append(START_ITEM);
                builder.append("\"value\":");
                builder.append("\"" + result.getTerm() + "\"");
                builder.append(COMMA);
                builder.append("\"label\":");
                builder.append("\"" + result.getTerm() + " (" + numberFormat.format(result.getFrequency()) + " " + source.getString("search.autocompletion.type." + result.getType()) + ")\"");
                builder.append(END_ITEM);
            }
        } else {
            for (AutocompletionResult result : results) {
                if (isAdded) {
                    builder.append(COMMA);
                } else {
                    isAdded = true;
                }
                builder.append("{\"");
                builder.append(result.getTerm());
                builder.append("\":");
                builder.append(result.getFrequency());
                builder.append("}");
            }
        }
    }

    private static void add(List<AutocompletionResult> results, AbstractSolrServerHolder abstractSearcher,
                            String term, String sourceType) throws SolrServerException, IOException {
        TermsResponse termsResponse = abstractSearcher.getTerms(term.trim());
        for (Map.Entry<String, List<TermsResponse.Term>> entry : termsResponse.getTermMap().entrySet()) {
            for (TermsResponse.Term termItem : entry.getValue()) {
                results.add(new AutocompletionResult(termItem, sourceType));
            }
        }
    }

    private static class AutocompletionResult implements Comparable<AutocompletionResult> {
        String type;
        String term;
        long frequency;

        protected AutocompletionResult(TermsResponse.Term term, String type) {
            this.type = type;
            this.term = term.getTerm();
            this.frequency = term.getFrequency();
        }

        public String getType() {
            return type;
        }

        public String getTerm() {
            return term;
        }

        public long getFrequency() {
            return frequency;
        }

        @Override
        public int compareTo(AutocompletionResult o) {
            if (frequency > o.getFrequency()) {
                return -1;
            } else if (frequency < o.getFrequency()) {
                return 1;
            }
            return 0;
        }

    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getTerm() {
        return term;
    }

    public void setRequest_locale(String request_locale) {
        this.request_locale = request_locale;
    }

    public String getRequest_locale() {
        return request_locale;
    }
}
