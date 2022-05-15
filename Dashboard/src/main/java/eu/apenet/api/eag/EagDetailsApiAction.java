package eu.apenet.api.eag;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.StrutsResourceBundleSource;
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

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
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

    public EagDetailsApiAction() {

    }

    public String execute() {

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

        ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
        eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
        eadSearchOptions.setContentClass(XmlType.getTypeByResourceName(type).getClazz());
        eadSearchOptions.setPublished(true);
        eadSearchOptions.setPageSize(Integer.parseInt(max));
        eadSearchOptions.setOrderByField("title");
        eadSearchOptions.setPageNumber(Integer.parseInt(page));

        if (EacCpf.class.equals(eadSearchOptions.getContentClass())){
            List<EacCpf> eacCpfList = eacCpfDAO.getEacCpfs(eadSearchOptions);
            eacCpfTotalCount = eacCpfDAO.countEacCpfs(eadSearchOptions);
            for (EacCpf eacCpf : eacCpfList){
                EagDetailEac eagDetailEac = new EagDetailEac(eacCpf);
                ec.add(eagDetailEac);
            }
        }else {
            eadTotalCount = eadDAO.countEads(eadSearchOptions);
            List<Ead> eadList = eadDAO.getEads(eadSearchOptions);
            for (Ead ead : eadList){
                if (FindingAid.class.equals(eadSearchOptions.getContentClass())){
                    EagDetailFa eagDetailFa = new EagDetailFa((FindingAid)ead);
                    fa.add(eagDetailFa);
                }
                else if (SourceGuide.class.equals(eadSearchOptions.getContentClass())){
                    EagDetailSg eagDetailSg = new EagDetailSg((SourceGuide)ead);
                    sg.add(eagDetailSg);
                }
                else if (HoldingsGuide.class.equals(eadSearchOptions.getContentClass())){
                    EagDetailHg eagDetailHg = new EagDetailHg((HoldingsGuide)ead);
                    hg.add(eagDetailHg);
                }
            }
        }
        return Action.SUCCESS;
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
}
