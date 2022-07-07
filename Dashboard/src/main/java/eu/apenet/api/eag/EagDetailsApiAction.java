package eu.apenet.api.eag;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.solr.AbstractSearcher;
import eu.apenet.commons.solr.SolrQueryParameters;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.ClasspathURIResolver;
import eu.apenet.commons.xslt.EagXslt;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EagDetailsApiAction {

    private String aiId;
    private String aiName;
    private String aiRepositoryCode;

    private List<EacCpf> eacCpfList;
    private Long eacCpfTotalCount;
    private List<EagDetailFa> fa = new ArrayList<>();
    private List<EagDetailHg> hg = new ArrayList<>();
    private List<EagDetailSg> sg = new ArrayList<>();
    private List<EagDetailEac> ec = new ArrayList<>();
    private Long eadTotalCount;

    private String max = "100";
    private String page;
    private String type;

    private String q;
    private String qdb;

    public EagDetailsApiAction() {

    }

    public String execute() {
        HttpServletResponse response = ServletActionContext.getResponse();

        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();

        ArchivalInstitution archivalInstitution;
        if (aiId != null) {
            archivalInstitution = archivalInstitutionDAO.findById(Integer.parseInt(aiId));
            aiName = archivalInstitution.getAiname();
            aiRepositoryCode = archivalInstitution.getRepositorycode();
        }
        else if (aiName != null) {
            archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByAiName(aiName);
            aiRepositoryCode = archivalInstitution.getRepositorycode();
            aiId = ""+archivalInstitution.getAiId();
        }
        else {
            archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(aiRepositoryCode);
            aiId = ""+archivalInstitution.getAiId();
            aiName = archivalInstitution.getAiname();
        }

        boolean isEad = false;
        boolean isEacCpf = false;
        if (EacCpf.class.equals(XmlType.getTypeByResourceName(type).getClazz())) {
            isEacCpf = true;
        }
        else {
            isEad = true;
        }

        if (q == null || q.trim().length()==0) {
            ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
            eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
            eadSearchOptions.setContentClass(XmlType.getTypeByResourceName(type).getClazz());
            eadSearchOptions.setPublished(true);
            eadSearchOptions.setPageSize(Integer.parseInt(max));
            eadSearchOptions.setOrderByField("title");
            eadSearchOptions.setPageNumber(Integer.parseInt(page));

            if (qdb != null && qdb.trim().length()>0) {
                eadSearchOptions.setSearchTerms(qdb);
                eadSearchOptions.setSearchTermsField("title");
            }

            if (EacCpf.class.equals(eadSearchOptions.getContentClass())) {
                List<EacCpf> eacCpfList = eacCpfDAO.getEacCpfs(eadSearchOptions);
                eacCpfTotalCount = eacCpfDAO.countEacCpfs(eadSearchOptions);
                for (EacCpf eacCpf : eacCpfList) {
                    EagDetailEac eagDetailEac = new EagDetailEac(eacCpf);
                    ec.add(eagDetailEac);
                }
            } else {
                eadTotalCount = eadDAO.countEads(eadSearchOptions);
                List<Ead> eadList = eadDAO.getEads(eadSearchOptions);
                for (Ead ead : eadList) {
                    if (FindingAid.class.equals(eadSearchOptions.getContentClass())) {
                        EagDetailFa eagDetailFa = new EagDetailFa((FindingAid) ead);
                        fa.add(eagDetailFa);
                    } else if (SourceGuide.class.equals(eadSearchOptions.getContentClass())) {
                        EagDetailSg eagDetailSg = new EagDetailSg((SourceGuide) ead);
                        sg.add(eagDetailSg);
                    } else if (HoldingsGuide.class.equals(eadSearchOptions.getContentClass())) {
                        EagDetailHg eagDetailHg = new EagDetailHg((HoldingsGuide) ead);
                        hg.add(eagDetailHg);
                    }
                }
            }
        }
        else {
            SolrQueryParameters solrQueryParameters = new SolrQueryParameters();
            solrQueryParameters.setTerm(q);
            solrQueryParameters.setMatchAllWords(true);
            solrQueryParameters.setTimeAllowed(true);

            if (isEad) {
                List<String> andParams = new ArrayList<>();
                andParams.add("archdesc");
                solrQueryParameters.getAndParameters().put("levelName", andParams);
            }

            List<String> orParams = new ArrayList<>();
            orParams.add(aiRepositoryCode);
            solrQueryParameters.getOrParameters().put("repositoryCode",orParams);

//            SolrQuery solrQuery = new SolrQuery();
//            solrQuery.setQuery(escapeSolrCharacters(q));
//            solrQuery.addFilterQuery("levelName:archdesc");
//            solrQuery.addFilterQuery("repositoryCode:aiRepositoryCode");

            AbstractSearcher abstractSearcher = new AbstractSearcher() {
                @Override
                protected String getCore() {
                    if (EacCpf.class.equals(XmlType.getTypeByResourceName(type).getClazz())) {
                        return "eac-cpfs";
                    }
                    else {
                        return "ead3s";
                    }
                }

                protected String getSolrSearchUrl() {
                    return APEnetUtilities.getDashboardConfig().getBaseSolrIndexUrl() + "/" + this.getCore();
                }
            };
            try {
                QueryResponse queryResponse = abstractSearcher.performNewSearchForListView(solrQueryParameters,(Integer.parseInt(page)-1) * Integer.parseInt(max), Integer.parseInt(max), null);
                SolrDocumentList solrDocumentList = queryResponse.getResults();
                Iterator<SolrDocument> iterator = solrDocumentList.iterator();
                while (iterator.hasNext()){
                    SolrDocument solrDocument = iterator.next();

                    if (isEacCpf){
                        EagDetailEac eagDetailEac = new EagDetailEac();
                        eagDetailEac.setTitle(solrDocument.getFirstValue("names").toString());
                        eagDetailEac.setId(solrDocument.getFirstValue("id").toString());
                        ec.add(eagDetailEac);
                        eacCpfTotalCount = solrDocumentList.getNumFound();
                    }
                    else {
                        eadTotalCount = solrDocumentList.getNumFound();
                        if (FindingAid.class.equals(XmlType.getTypeByResourceName(type).getClazz())) {
                            EagDetailFa eagDetailFa = new EagDetailFa();
                            eagDetailFa.setTitle(solrDocument.getFirstValue("unitTitle").toString());
                            eagDetailFa.setEadid(solrDocument.getFirstValue("recordId").toString());
                            fa.add(eagDetailFa);
                        } else if (SourceGuide.class.equals(XmlType.getTypeByResourceName(type).getClazz())) {
                            EagDetailSg eagDetailSg = new EagDetailSg();
                            eagDetailSg.setTitle(solrDocument.getFirstValue("unitTitle").toString());
                            eagDetailSg.setEadid(solrDocument.getFirstValue("recordId").toString());
                            sg.add(eagDetailSg);
                        } else if (HoldingsGuide.class.equals(XmlType.getTypeByResourceName(type).getClazz())) {
                            EagDetailHg eagDetailHg = new EagDetailHg();
                            eagDetailHg.setTitle(solrDocument.getFirstValue("unitTitle").toString());
                            eagDetailHg.setEadid(solrDocument.getFirstValue("recordId").toString());
                            hg.add(eagDetailHg);
                        }
                    }
                }
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        response.setHeader("Access-Control-Allow-Origin","*");
        return Action.SUCCESS;
    }

    public static String escapeSolrCharacters(String term) {
        if (StringUtils.isNotBlank(term)) {
            term = term.replaceAll(" - ", " \"-\" ");
            term = term.replaceAll(" \\+ ", " \"+\" ");
        }

        return term;
    }

    public void setAiId(String aiId) {
        this.aiId = aiId;
    }

    public String getAiId() {
        return aiId;
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

    public void setEacCpfList(List<EacCpf> eacCpfList) {
        this.eacCpfList = eacCpfList;
    }

    public void setEacCpfTotalCount(Long eacCpfTotalCount) {
        this.eacCpfTotalCount = eacCpfTotalCount;
    }

    public void setEadTotalCount(Long eadTotalCount) {
        this.eadTotalCount = eadTotalCount;
    }

    public List<EacCpf> getEacCpfList() {
        return eacCpfList;
    }

    public Long getEacCpfTotalCount() {
        return eacCpfTotalCount;
    }

    public Long getEadTotalCount() {
        return eadTotalCount;
    }

    public void setFa(List<EagDetailFa> fa) {
        this.fa = fa;
    }

    public List<EagDetailFa> getFa() {
        return fa;
    }

    public void setHg(List<EagDetailHg> hg) {
        this.hg = hg;
    }

    public void setSg(List<EagDetailSg> sg) {
        this.sg = sg;
    }

    public void setEc(List<EagDetailEac> ec) {
        this.ec = ec;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EagDetailHg> getHg() {
        return hg;
    }

    public List<EagDetailSg> getSg() {
        return sg;
    }

    public List<EagDetailEac> getEc() {
        return ec;
    }

    public String getMax() {
        return max;
    }

    public String getPage() {
        return page;
    }

    public String getType() {
        return type;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getQ() {
        return q;
    }

    public void setQdb(String qdb) {
        this.qdb = qdb;
    }

    public String getQdb() {
        return qdb;
    }
}
