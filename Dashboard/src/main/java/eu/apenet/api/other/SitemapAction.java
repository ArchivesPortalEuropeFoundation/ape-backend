package eu.apenet.api.other;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.solr.HighlightType;
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.DisplayUtils;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import eu.archivesportaleurope.util.ApeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitemapAction {

    InputStream xmlInputStream;
    String aicode;
    String eadid;
    String type;
    String page;

    private static final String HTTP_NOT_FOUND = "404";
    private static final String PRIORITY = "priority";
    private static final String URL = "url";
    private static final String LASTMOD = "lastmod";
    private static final String LOC = "loc";
    private static final String SITEMAP = "sitemap";
    private static final double PAGESIZE = 10000;
    private static final double PAGESIZE_EAD = 10000;
    private static final double PAGESIZE_EAC = 45000;
    private static final double PAGESIZE_FA = 45000;
    private static final double PAGESIZE_HG = 45000;
    private static final double PAGESIZE_SG = 45000;
    private static final double PAGESIZE_C = 45000;
    private static final String APPLICATION_XML = "application/xml";
    private static final String UTF8 = "UTF-8";
    private static final String SITEMAP_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9";
    private static final QName SITEMAP_INDEX_ELEMENT = new QName(SITEMAP_NAMESPACE, "sitemapindex");
    private static final QName URLSET_ELEMENT = new QName(SITEMAP_NAMESPACE, "urlset");
    private static SimpleDateFormat W3C_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");

    private final Logger log = Logger.getLogger(getClass());

    public SitemapAction() {

    }

    public String execute() throws NoSuchAlgorithmException, IOException {

        if ("eag".equals(this.type)){
            generateSitemapForEAGs();
        }
        else if ("eac".equals(this.type)){
            if (page != null) {
                    generateSitemapForEACs();
                } else {
                    generateSitemapIndexForEACs();
                }
        }
        else if ("fa".equals(this.type)){
            if (page != null) {
                generateSitemapForFAs();
            } else {
                generateSitemapIndexForFAs();
            }
        }
        else if ("sg".equals(this.type)){
            if (page != null) {
                generateSitemapForSGs();
            } else {
                generateSitemapIndexForSGs();
            }
        }
        else if ("hg".equals(this.type)){
            if (page != null) {
                generateSitemapForHGs();
            } else {
                generateSitemapIndexForHGs();
            }
        }
        else if ("c".equals(this.type)){
            if (page != null) {
                generateSitemapForCs();
            } else {
                generateSitemapIndexForCs();
            }
        }
        else {
            this.type = "eag";
            generateSitemapForEAGs();
        }

//        if (this.aicode != null){
//            if ("eag".equals(this.type)){
//                generateAiContentEag();
//            }
//            else if ("ead".equals(this.type)){
//                if (page != null) {
//                    generateAiEADSitemap();
//                } else {
//                    generateAiEADSitemapIndex();
//                }
//            }
//            else if ("eac".equals(this.type)){
//                if (page != null) {
//                    generateAiEACSitemap();
//                } else {
//                    generateAiEACSitemapIndex();
//                }
//            }
//            else {
//                if (this.eadid != null) {
//                    if (page != null) {
//                        generateEadSitemap();
//                    } else {
//                        generateEadSitemapIndex();
//                    }
//                }
//                else {
//                    generateAiSitemapIndex();
//                }
//            }
//        }
//        else {
//            generateGlobalSitemapIndex();
//        }


        return "xml";
    }

    private void generateSitemapForEAGs(){
        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeSitemapStartElement(xmlWriter);
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsWithoutGroups();
            for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
                SitemapUrl siteMapUrl = new SitemapUrl(archivalInstitution.getRepositorycode());
                writeSitemapElement(xmlWriter, FriendlyUrlUtil.getSitemapEAGActualUrl(siteMapUrl), null, "0.5");
            }
            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapIndexForEACs(){
        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeIndexStartElement(xmlWriter);

            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            long numberOfEacCpfItems = eacCpfDAO.countEacCpfs(contentSearchOptions);

            int numberOfPages = 1;
            if (numberOfEacCpfItems > PAGESIZE_EAC) {
                numberOfPages = (int) Math.ceil((double) numberOfEacCpfItems / PAGESIZE_EAC);
            }

            for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
                SitemapUrl siteMapUrl = new SitemapUrl();
                siteMapUrl.setPageNumberAsInt(pageNumber);
                String url = FriendlyUrlUtil.getSitemapEACUrl(siteMapUrl);
                writeIndexElement(xmlWriter, url, null);
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapForEACs(){
        Integer pageNumber = Integer.parseInt(this.page);

        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeSitemapStartElement(xmlWriter);

            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setPageNumber(pageNumber);
            contentSearchOptions.setPageSize((int) PAGESIZE_EAC);
            List<EacCpf> eacCpfs = eacCpfDAO.getEacCpfs(contentSearchOptions);

            for (EacCpf eacCpf : eacCpfs) {
                EacCpfPersistentUrl eacCpfPersistentUrl = new EacCpfPersistentUrl(eacCpf.getArchivalInstitution().getRepositorycode(), eacCpf.getIdentifier());
                writeSitemapElement(xmlWriter, FriendlyUrlUtil.getEacCpfPersistentUrlForSitemap(eacCpfPersistentUrl), null, "0.5");
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapIndexForFAs(){
        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeIndexStartElement(xmlWriter);

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setContentClass(FindingAid.class);
            long numberOfItems = eadDAO.countEads(contentSearchOptions);

            int numberOfPages = 1;
            if (numberOfItems > PAGESIZE_FA) {
                numberOfPages = (int) Math.ceil((double) numberOfItems / PAGESIZE_FA);
            }

            for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
                SitemapUrl siteMapUrl = new SitemapUrl();
                siteMapUrl.setPageNumberAsInt(pageNumber);
                String url = FriendlyUrlUtil.getSitemapFAUrl(siteMapUrl);
                writeIndexElement(xmlWriter, url, null);
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapForFAs(){
        Integer pageNumber = Integer.parseInt(this.page);

        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeSitemapStartElement(xmlWriter);

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setContentClass(FindingAid.class);
            contentSearchOptions.setPageNumber(pageNumber);
            contentSearchOptions.setPageSize((int) PAGESIZE_FA);
            List<Ead> eads = eadDAO.getEads(contentSearchOptions);

            for (Ead ead : eads) {
                EadPersistentUrl eadPersistentUrl = new EadPersistentUrl(ead.getArchivalInstitution().getRepositorycode(), XmlType.getContentType(ead).getResourceName(), ead.getIdentifier());
                String url = FriendlyUrlUtil.getSitemapUrl(eadPersistentUrl);
                writeSitemapElement(xmlWriter, FriendlyUrlUtil.getEadPersistentUrlForSitemap(eadPersistentUrl), null, "0.5");
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapIndexForHGs(){
        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeIndexStartElement(xmlWriter);

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setContentClass(HoldingsGuide.class);
            long numberOfItems = eadDAO.countEads(contentSearchOptions);

            int numberOfPages = 1;
            if (numberOfItems > PAGESIZE_HG) {
                numberOfPages = (int) Math.ceil((double) numberOfItems / PAGESIZE_HG);
            }

            for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
                SitemapUrl siteMapUrl = new SitemapUrl();
                siteMapUrl.setPageNumberAsInt(pageNumber);
                String url = FriendlyUrlUtil.getSitemapHGUrl(siteMapUrl);
                writeIndexElement(xmlWriter, url, null);
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapForHGs(){
        Integer pageNumber = Integer.parseInt(this.page);

        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeSitemapStartElement(xmlWriter);

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setContentClass(HoldingsGuide.class);
            contentSearchOptions.setPageNumber(pageNumber);
            contentSearchOptions.setPageSize((int) PAGESIZE_HG);
            List<Ead> eads = eadDAO.getEads(contentSearchOptions);

            for (Ead ead : eads) {
                EadPersistentUrl eadPersistentUrl = new EadPersistentUrl(ead.getArchivalInstitution().getRepositorycode(), XmlType.getContentType(ead).getResourceName(), ead.getIdentifier());
                String url = FriendlyUrlUtil.getSitemapUrl(eadPersistentUrl);
                writeSitemapElement(xmlWriter, FriendlyUrlUtil.getEadPersistentUrlForSitemap(eadPersistentUrl), null, "0.5");
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapIndexForSGs(){
        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeIndexStartElement(xmlWriter);

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setContentClass(SourceGuide.class);
            long numberOfItems = eadDAO.countEads(contentSearchOptions);

            int numberOfPages = 1;
            if (numberOfItems > PAGESIZE_SG) {
                numberOfPages = (int) Math.ceil((double) numberOfItems / PAGESIZE_SG);
            }

            for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
                SitemapUrl siteMapUrl = new SitemapUrl();
                siteMapUrl.setPageNumberAsInt(pageNumber);
                String url = FriendlyUrlUtil.getSitemapSGUrl(siteMapUrl);
                writeIndexElement(xmlWriter, url, null);
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapForSGs(){
        Integer pageNumber = Integer.parseInt(this.page);

        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeSitemapStartElement(xmlWriter);

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setContentClass(SourceGuide.class);
            contentSearchOptions.setPageNumber(pageNumber);
            contentSearchOptions.setPageSize((int) PAGESIZE_SG);
            List<Ead> eads = eadDAO.getEads(contentSearchOptions);

            for (Ead ead : eads) {
                EadPersistentUrl eadPersistentUrl = new EadPersistentUrl(ead.getArchivalInstitution().getRepositorycode(), XmlType.getContentType(ead).getResourceName(), ead.getIdentifier());
                writeSitemapElement(xmlWriter, FriendlyUrlUtil.getEadPersistentUrlForSitemap(eadPersistentUrl), null, "0.5");
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapIndexForCs(){
        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeIndexStartElement(xmlWriter);

            CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
            long numberOfItems = cLevelDAO.countAllCLevels();

            int numberOfPages = 1;
            if (numberOfItems > PAGESIZE_C) {
                numberOfPages = (int) Math.ceil((double) numberOfItems / PAGESIZE_C);
            }

            for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
                SitemapUrl siteMapUrl = new SitemapUrl();
                siteMapUrl.setPageNumberAsInt(pageNumber);
                String url = FriendlyUrlUtil.getSitemapCUrl(siteMapUrl);
                writeIndexElement(xmlWriter, url, null);
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateSitemapForCs(){
        Integer pageNumber = Integer.parseInt(this.page);

        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeSitemapStartElement(xmlWriter);

            CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
            List<CLevel> cLevels = cLevelDAO.getCLevels(pageNumber, (int)PAGESIZE_C);

            for (CLevel cLevel : cLevels) {
                EadContent eadContent = cLevel.getEadContent();
                Ead ead = null;
                if (eadContent.getFindingAid() != null) {
                    ead = eadContent.getFindingAid();
                }
                else if (eadContent.getSourceGuide() != null) {
                    ead = eadContent.getSourceGuide();
                }
                else if (eadContent.getHoldingsGuide() != null) {
                    ead = eadContent.getHoldingsGuide();
                }
                EadPersistentUrl eadPersistentUrl = new EadPersistentUrl(ead.getArchivalInstitution().getRepositorycode(), XmlType.getContentType(ead).getResourceName(), ead.getIdentifier());
                eadPersistentUrl.setClevel(cLevel);
                writeSitemapElement(xmlWriter, FriendlyUrlUtil.getEadPersistentUrlForSitemap(eadPersistentUrl), null, "0.5");
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateGlobalSitemapIndex(){
        XMLStreamWriter xmlWriter = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(
                    baos, UTF8);
            writeIndexStartElement(xmlWriter);
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsWithoutGroups();
            for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
                xmlWriter.writeStartElement(SITEMAP);
                xmlWriter.writeStartElement(LOC);
                SitemapUrl siteMapUrl = new SitemapUrl(archivalInstitution.getRepositorycode());
                xmlWriter.writeCharacters(FriendlyUrlUtil.getSitemapUrl(siteMapUrl));
                xmlWriter.writeEndElement();
                if (archivalInstitution.getContentLastModifiedDate() != null) {
                    xmlWriter.writeStartElement(LASTMOD);
                    xmlWriter.writeCharacters(W3C_DATETIME_FORMAT.format(archivalInstitution
                            .getContentLastModifiedDate()));
                    xmlWriter.writeEndElement();
                }
                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void generateAiSitemapIndex(){

        try {
            String repoCode = this.aicode;

            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();

            XMLStreamWriter xmlWriter = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(baos, UTF8);

            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(repoCode);
            ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
            eadSearchOptions.setPublished(true);
            eadSearchOptions.setContentClass(FindingAid.class);
            eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());

            writeIndexStartElement(xmlWriter);
            SitemapUrl siteMapUrl = new SitemapUrl(archivalInstitution.getRepositorycode());
            String url = FriendlyUrlUtil.getSitemapEAGUrl(siteMapUrl);
            writeIndexElement(xmlWriter, url, null);

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            long numberOfItems = eadDAO.countEads(eadSearchOptions);
            eadSearchOptions.setContentClass(HoldingsGuide.class);
            long numberOfItemsHG = eadDAO.countEads(eadSearchOptions);
            eadSearchOptions.setContentClass(SourceGuide.class);
            long numberOfItemsSG = eadDAO.countEads(eadSearchOptions);
            if (numberOfItems+numberOfItemsHG+numberOfItemsSG > 0) {
                url = FriendlyUrlUtil.getSitemapEADUrl(siteMapUrl);
                writeIndexElement(xmlWriter, url, archivalInstitution.getContentLastModifiedDate());
            }

            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
            long numberOfEacCpfItems = eacCpfDAO.countEacCpfs(eadSearchOptions);
            if (numberOfEacCpfItems>0) {
                url = FriendlyUrlUtil.getSitemapEACUrl(siteMapUrl);
                writeIndexElement(xmlWriter, url, archivalInstitution.getContentLastModifiedDate());
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());

        } catch (Exception e) {
//            LOGGER.error("Unable to generate ai index: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }
    }

    private void generateAiEACSitemapIndex() {
        try {
            String repoCode = this.aicode;
            long numberOfEacCpfItems = 0;

            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
            EadDAO eadDAO = DAOFactory.instance().getEadDAO();

            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(repoCode);
            ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
            eadSearchOptions.setPublished(true);
            eadSearchOptions.setContentClass(FindingAid.class);
            eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
            numberOfEacCpfItems = eacCpfDAO.countEacCpfs(eadSearchOptions);
            if (numberOfEacCpfItems > PAGESIZE) {
                int numberOfEacCpfPages = (int) Math.ceil((double) numberOfEacCpfItems / PAGESIZE);
                int numberOfTotalPages = numberOfEacCpfPages;
//                LOGGER.debug(getUserAgent(resourceRequest) + ": AI-index:" + archivalInstitution.getRepositorycode() + " #p:" + numberOfTotalPages);

                XMLStreamWriter xmlWriter = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(baos, UTF8);

                writeIndexStartElement(xmlWriter);
                for (int pageNumber = 1; pageNumber <= numberOfTotalPages; pageNumber++) {
                    SitemapUrl siteMapUrl = new SitemapUrl(archivalInstitution.getRepositorycode());
                    siteMapUrl.setPageNumberAsInt(pageNumber);
                    String url = FriendlyUrlUtil.getSitemapEACUrl(siteMapUrl);
                    writeIndexElement(xmlWriter, url, archivalInstitution.getContentLastModifiedDate());
                }
                xmlWriter.writeEndElement();
                xmlWriter.writeEndDocument();
                xmlWriter.flush();
                xmlWriter.close();

                this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());

            } else {
                generateAiContentEac(archivalInstitution, 1);
            }
        } catch (Exception e) {
//            LOGGER.error("Unable to generate ai index: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }
    }

    private void generateAiEADSitemapIndex(){
        try {
            String repoCode = this.aicode;
            long numberOfItems = 0;
            long numberOfEacCpfItems = 0;

            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            EadDAO eadDAO = DAOFactory.instance().getEadDAO();

            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(repoCode);
            ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
            eadSearchOptions.setPublished(true);
            eadSearchOptions.setContentClass(FindingAid.class);
            eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
            numberOfItems = eadDAO.countEads(eadSearchOptions);
            if (numberOfItems > PAGESIZE) {
                int numberOfPages = (int) Math.ceil((double) numberOfItems / PAGESIZE);
                int numberOfTotalPages = numberOfPages;
//                LOGGER.debug(getUserAgent(resourceRequest) + ": AI-index:" + archivalInstitution.getRepositorycode() + " #p:" + numberOfTotalPages);

                XMLStreamWriter xmlWriter = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(baos, UTF8);

                writeIndexStartElement(xmlWriter);
                for (int pageNumber = 1; pageNumber <= numberOfTotalPages; pageNumber++) {
                    SitemapUrl siteMapUrl = new SitemapUrl(archivalInstitution.getRepositorycode());
                    siteMapUrl.setPageNumberAsInt(pageNumber);
                    String url = FriendlyUrlUtil.getSitemapEADUrl(siteMapUrl);
                    writeIndexElement(xmlWriter, url, archivalInstitution.getContentLastModifiedDate());
                }
                xmlWriter.writeEndElement();
                xmlWriter.writeEndDocument();
                xmlWriter.flush();
                xmlWriter.close();

                this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());

            } else {
                generateAiContentEad(archivalInstitution, 1);
            }
        } catch (Exception e) {
//            LOGGER.error("Unable to generate ai index: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }
    }

    public void generateAiEADSitemap() {
        try {
            String repoCode = this.aicode;
            Integer pageNumber = Integer.parseInt(this.page);
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(repoCode);
            generateAiContentEad(archivalInstitution, pageNumber);
        } catch (Exception e) {
//            LOGGER.error("Unable to generate ai sitemap: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }

    }

    public void generateAiEACSitemap() {
        try {
            String repoCode = this.aicode;
            Integer pageNumber = Integer.parseInt(this.page);
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(repoCode);
            generateAiContentEac(archivalInstitution, pageNumber);
        } catch (Exception e) {
//            LOGGER.error("Unable to generate ai sitemap: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }

    }

    public void generateAiContentEag() {
        try {
            String repoCode = this.aicode;
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(repoCode);

            XMLStreamWriter xmlWriter = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(baos, UTF8);

            writeSitemapStartElement(xmlWriter);

            SitemapUrl siteMapUrl = new SitemapUrl(archivalInstitution.getRepositorycode());
            String url = FriendlyUrlUtil.getSitemapEAGActualUrl(siteMapUrl);
            writeSitemapElement(xmlWriter, url, null, "0.8");

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());

        } catch (Exception e) {
//            LOGGER.error("Unable to generate ai index: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }
    }

    public void generateAiContentEad(ArchivalInstitution archivalInstitution, int pageNumber) {
        try {
//            LOGGER.debug(getUserAgent(resourceRequest) + ": AI-content:" + archivalInstitution.getRepositorycode() + " pn:" + pageNumber);

            EadDAO eadDAO = DAOFactory.instance().getEadDAO();

            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setContentClass(FindingAid.class);
            contentSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
            contentSearchOptions.setPageNumber(pageNumber);
            contentSearchOptions.setPageSize((int) PAGESIZE);
            List<Ead> eads = eadDAO.getEads(contentSearchOptions);

            XMLStreamWriter xmlWriter = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(baos, UTF8);

            writeIndexStartElement(xmlWriter);
            if (pageNumber == 1) {
                contentSearchOptions.setContentClass(HoldingsGuide.class);
                contentSearchOptions.setPageSize(0);
                eads.addAll(eadDAO.getEads(contentSearchOptions));
                contentSearchOptions.setContentClass(SourceGuide.class);
                eads.addAll(eadDAO.getEads(contentSearchOptions));
            }
            if (eads.size() > 0) {

                for (Ead ead : eads) {
                    EadPersistentUrl eadPersistentUrl = new EadPersistentUrl(archivalInstitution.getRepositorycode(), XmlType.getContentType(ead).getResourceName(), ead.getIdentifier());
                    String url = FriendlyUrlUtil.getSitemapUrl(eadPersistentUrl);
                    writeIndexElement(xmlWriter, url, ead.getPublishDate());
                }
            }
            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());

        } catch (Exception e) {
//            LOGGER.error("Unable to generate ai content: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }
    }

    public void generateAiContentEac(ArchivalInstitution archivalInstitution, int pageNumber) {
        try {
//            LOGGER.debug(getUserAgent(resourceRequest) + ": AI-content:" + archivalInstitution.getRepositorycode() + " pn:" + pageNumber);

            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();

            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(true);
            contentSearchOptions.setContentClass(FindingAid.class);
            contentSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
            contentSearchOptions.setPageNumber(pageNumber);
            contentSearchOptions.setPageSize((int) PAGESIZE);

            XMLStreamWriter xmlWriter = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(baos, UTF8);

            writeSitemapStartElement(xmlWriter);

            List<EacCpf> eacCpfs = eacCpfDAO.getEacCpfs(contentSearchOptions);
            if (eacCpfs.size() > 0) {
                for (EacCpf eacCpf : eacCpfs) {
                    EacCpfPersistentUrl eacCpfPersistentUrl = new EacCpfPersistentUrl(archivalInstitution.getRepositorycode(), eacCpf.getIdentifier());
                    String url = FriendlyUrlUtil.getEacCpfPersistentUrlForSitemap(eacCpfPersistentUrl);
                    writeSitemapElement(xmlWriter, url, eacCpf.getPublishDate(), "0.5");
                }
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();

            this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());

        } catch (Exception e) {
//            LOGGER.error("Unable to generate ai content: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }
    }

    public void generateEadSitemapIndex() {
        try {
            String repoCode = this.aicode;

            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();

            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(repoCode);
            XmlType xmlType = XmlType.getTypeByResourceName(this.type);
            long numberOfItems = 0;
            ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
            eadSearchOptions.setPublished(true);
            eadSearchOptions.setContentClass(xmlType.getClazz());
            eadSearchOptions.setEadid(eadid);
            eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
            Ead ead = eadDAO.getEads(eadSearchOptions).get(0);
            numberOfItems = cLevelDAO.countCLevels(xmlType.getEadClazz(), ead.getId());
            if (numberOfItems > PAGESIZE_EAD) {
                int numberOfPages = (int) Math.ceil((double) numberOfItems / PAGESIZE_EAD);

                XMLStreamWriter xmlWriter = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(baos, UTF8);

                writeIndexStartElement(xmlWriter);
                for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
                    EadPersistentUrl eadPersistentUrl = new EadPersistentUrl(archivalInstitution.getRepositorycode(), XmlType.getContentType(ead).getResourceName(), ead.getIdentifier());
                    eadPersistentUrl.setPageNumberAsInt(pageNumber);
                    String url = FriendlyUrlUtil.getSitemapUrl(eadPersistentUrl);
                    writeIndexElement(xmlWriter, url, ead.getPublishDate());
                }
                xmlWriter.writeEndElement();
                xmlWriter.writeEndDocument();
                xmlWriter.flush();
                xmlWriter.close();

                this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
            } else {
                generateEadContent(xmlType, archivalInstitution, eadid, 1);
            }
        } catch (Exception e) {
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }
    }

    public void generateEadSitemap() {
        try {
            String eadid = this.eadid;
            String repoCode = this.aicode;
            Integer pageNumber = Integer.parseInt(this.page);
            XmlType xmlType = XmlType.getTypeByResourceName(this.type);
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(repoCode);
            generateEadContent(xmlType, archivalInstitution, eadid, pageNumber);
        } catch (Exception e) {
//            LOGGER.error("Unable to generate ead sitemap: " + e.getMessage());
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }

    }

    public void generateEadContent(XmlType xmlType, ArchivalInstitution archivalInstitution, String eadid, int pageNumber) {
        try {
            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();

            ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
            eadSearchOptions.setPublished(true);
            eadSearchOptions.setContentClass(xmlType.getClazz());
            eadSearchOptions.setEadid(eadid);
            eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
            Ead ead = eadDAO.getEads(eadSearchOptions).get(0);
            List<CLevel> clevels = cLevelDAO.getCLevels(xmlType.getEadClazz(), ead.getId(), pageNumber, (int) PAGESIZE_EAD);
            EadPersistentUrl eadPersistentUrl = new EadPersistentUrl(archivalInstitution.getRepositorycode(), XmlType.getContentType(ead).getResourceName(), ead.getIdentifier());
            if (clevels.size() >= 0) {
                XMLStreamWriter xmlWriter = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                xmlWriter = (XMLOutputFactory.newInstance()).createXMLStreamWriter(baos, UTF8);

                writeSitemapStartElement(xmlWriter);
                if (pageNumber == 1) {
                    String url = FriendlyUrlUtil.getEadPersistentUrlForSitemap(eadPersistentUrl);
                    writeSitemapElement(xmlWriter, url, ead.getPublishDate(), "0.8");
                }
                for (CLevel cLevel : clevels) {
                    eadPersistentUrl.setClevel(cLevel);
                    String url = FriendlyUrlUtil.getEadPersistentUrlForSitemap(eadPersistentUrl);
                    if (eadPersistentUrl.isPersistent()){
                        writeSitemapElement(xmlWriter, url, ead.getPublishDate(), null);
                    }else {
                        writeSitemapElement(xmlWriter, url, ead.getPublishDate(), "0.3");
                    }
                }
                xmlWriter.writeEndElement();
                xmlWriter.writeEndDocument();
                xmlWriter.flush();
                xmlWriter.close();

                this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
            } else {
//                resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, HTTP_NOT_FOUND);
        }
    }

    private static void writeIndexStartElement(XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null) {
            xmlWriter.writeStartElement(SITEMAP_INDEX_ELEMENT.getPrefix(), SITEMAP_INDEX_ELEMENT.getLocalPart(),
                    SITEMAP_INDEX_ELEMENT.getNamespaceURI());
            xmlWriter.writeDefaultNamespace(SITEMAP_NAMESPACE);
        }
    }

    private static void writeSitemapStartElement(XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null) {
            xmlWriter.writeStartElement(URLSET_ELEMENT.getPrefix(), URLSET_ELEMENT.getLocalPart(),
                    URLSET_ELEMENT.getNamespaceURI());
            xmlWriter.writeDefaultNamespace(SITEMAP_NAMESPACE);
        }
    }

    private static void writeSitemapElement(XMLStreamWriter xmlWriter, String url, Date lastModDate, String priority)
            throws XMLStreamException {
        if (xmlWriter != null) {
            xmlWriter.writeStartElement(URL);
            xmlWriter.writeStartElement(LOC);
            xmlWriter.writeCharacters(url);
            xmlWriter.writeEndElement();
            if (lastModDate != null) {
                xmlWriter.writeStartElement(LASTMOD);
                xmlWriter.writeCharacters(W3C_DATETIME_FORMAT.format(lastModDate));
                xmlWriter.writeEndElement();
            }
            if (priority != null) {
                xmlWriter.writeStartElement(PRIORITY);
                xmlWriter.writeCharacters(priority);
                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();
        }
    }

    private static void writeIndexElement(XMLStreamWriter xmlWriter, String url, Date lastModDate)
            throws XMLStreamException {
        if (xmlWriter != null) {
            xmlWriter.writeStartElement(SITEMAP);
            xmlWriter.writeStartElement(LOC);
            xmlWriter.writeCharacters(url);
            xmlWriter.writeEndElement();
            if (lastModDate != null) {
                xmlWriter.writeStartElement(LASTMOD);
                xmlWriter.writeCharacters(W3C_DATETIME_FORMAT.format(lastModDate));
                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();
        }
    }

    public InputStream getXmlInputStream() {
        return this.xmlInputStream;
    }

    public void setAicode(String aicode) {
        this.aicode = aicode;
    }

    public String getAicode() {
        return aicode;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public void setEadid(String eadid) {
        this.eadid = eadid;
    }

    public String getEadid() {
        return eadid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public class SitemapUrl extends AbstractUrl{

        public SitemapUrl() {
            super(null);
        }

        public SitemapUrl(String repoCode) {
            super(repoCode);
        }

        @Override
        public String toString() {
//            return encodeUrl(super.toString() +  getPageNumberSuffix());
            return super.toString() +  getPageNumberSuffix();
        }

    }

    public class EadPersistentUrl extends AbstractContentUrl{


        public static final String PARAMETER_UNITID = "unitid";
        public static final String PARAMETER_NPID = "dbid";

        private String unitid;
        private String searchId;



        public EadPersistentUrl(String repoCode, String xmlTypeName, String identifier){
            super(repoCode, xmlTypeName, identifier);
        }
        public void setClevel(CLevel cLevel) {
            if (cLevel != null){
                setSearchIdAsLong(cLevel.getId());
                if (cLevel.isDuplicateUnitid()){
                    setUnitid(null);
                }else {
                    setUnitid(cLevel.getUnitid());
                }
            }
        }

        public void setUnitid(String unitid) {
            this.unitid = unitid;
        }

        public void setSearchId(String searchId) {
            this.searchId = searchId;
        }
        public void setSearchIdAsLong(Long searchId) {
            if (searchId != null){
                this.searchId = SolrValues.C_LEVEL_PREFIX + searchId;
            }
        }

        public String getUnitid() {
            return unitid;
        }

        public String getSearchId() {
            return searchId;
        }

        @Override
        public String toString(){
            String url= super.toString();
            if (StringUtils.isBlank(unitid)) {
                if (StringUtils.isNotBlank(searchId)) {
                    String solrPrefix = searchId.substring(0, 1);
                    if (SolrValues.C_LEVEL_PREFIX.equals(solrPrefix)) {
                        url+= "&"+ PARAMETER_NPID + "=" + searchId;
                    }
                }
            }else {
                url+= "&"+ PARAMETER_UNITID + "=" + encodeUrl(unitid);
            }

            url += this.getPageNumberSuffix();
            url += this.getSearchSuffix();
            return url;//encodeUrl(url);
        }

        public boolean isPersistent(){
            return !(StringUtils.isBlank(unitid) && StringUtils.isNotBlank(searchId));
        }
    }

    public class EacCpfPersistentUrl extends AbstractContentUrl {


        public static final String PARAMETER_RELATION = "relation";

        private String relation;


        public EacCpfPersistentUrl(String repoCode, String identifier) {
            super(repoCode, XmlType.EAC_CPF.getResourceName(), identifier);
        }


        public void setRelation(String relation) {
            this.relation = relation;
        }


        @Override
        public String toString() {
            String url = super.toString();
            if (StringUtils.isNotBlank(relation)) {
                url += "&" + PARAMETER_RELATION + "=" + encodeUrl(relation);
            }
            url += this.getSearchSuffix();

            return url;
        }

    }

    public class AbstractContentUrl extends AbstractUrl {

        public static final String PARAMETER_TYPE = "type";
        public static final String PARAMETER_ID = "eadid";

        public static final String PARAMETER_SEARCH = "search";

        private String searchTerms;
        private String searchFieldsSelectionId;

        private String xmlTypeName;
        private String identifier;

        public void setSearchTerms(String searchTerms) {
            this.searchTerms = searchTerms;
        }

        public void setSearchFieldsSelectionId(String searchFieldsSelectionId) {
            this.searchFieldsSelectionId = searchFieldsSelectionId;
        }

        public AbstractContentUrl(String repoCode, String xmlTypeName, String identifier) {
            super(repoCode);
            this.xmlTypeName = xmlTypeName;
            this.identifier = identifier;
        }

        public String getXmlTypeName() {
            return xmlTypeName;
        }

        @Override
        public String toString() {
            return super.toString() + "&" + PARAMETER_TYPE + "=" + xmlTypeName + "&" + PARAMETER_ID + "=" + encodeUrl(identifier);
        }

        public String getSearchSuffix() {
            if (StringUtils.isNotBlank(searchTerms) && StringUtils.isNotBlank(searchFieldsSelectionId)) {
                List<SolrField> solrFields = SolrField.getSolrFieldsByIdString(searchFieldsSelectionId);
                HighlightType highlightType = HighlightType.DEFAULT;
                if (solrFields.size() > 0) {
                    highlightType = solrFields.get(0).getType();
                }
                String newSearchWords = encodeUrl(searchTerms);
                if (StringUtils.isNotBlank(newSearchWords)) {
                    return "&" + PARAMETER_SEARCH + "=" + searchFieldsSelectionId + FriendlyUrlUtil.SEPARATOR + newSearchWords;
                }

            }
            return "";
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    public static class AbstractUrl {
        private static final Logger LOGGER = Logger.getLogger(AbstractUrl.class);
        public static final String PARAMETER_AICODE = "aicode";
        public static final String PARAMETER_PAGE = "page";
        private String repoCode;
        private String pageNumber;
        public AbstractUrl(String repoCode) {
            this.repoCode = repoCode;
        }

        protected String getRepoCode() {
            return ApeUtil.encodeRepositoryCode(repoCode);
        }


        public void setPageNumber(String pageNumber) {
            this.pageNumber = pageNumber;
        }
        public void setPageNumberAsInt(Integer pageNumber) {
            if (pageNumber != null){
                this.pageNumber = pageNumber.toString();
            }
        }


        @Override
        public String toString() {
            if (repoCode!=null) {
                return "?" + PARAMETER_AICODE + "=" + getRepoCode();
            }
            return "";
        }

        public String getPageNumberSuffix(){
            if (StringUtils.isNotBlank(pageNumber)){
                return (repoCode!=null?"&":"?")+PARAMETER_PAGE + "="+pageNumber;
            }else {
                return "";
            }
        }

        public static String encodeUrl(String url){
            if (StringUtils.isNotBlank(url)){
                try {
                    return URLEncoder.encode(url, "UTF-8").replaceAll("%2F", "/").replaceAll("%3A", ":");
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(ApeUtil.generateThrowableLog(e));
                }
            }
            return null;
        }
    }

    public static final class FriendlyUrlUtil {

        public static final String EAG_DISPLAY_PERSISTENT = "eagdisplay-persistent-archdesc";
//        public static final String EAD_DISPLAY_PERSISTENT = "eaddisplay-persistent-archdesc";
        public static final String EAD_DISPLAY_PERSISTENT_TOP_LEVEL = "eaddisplay-persistent-archdesc";
        public static final String EAD_DISPLAY_PERSISTENT_C_LEVEL_UNITID = "eaddisplay-persistent-clevel-unitid";
        public static final String EAD_DISPLAY_PERSISTENT_C_LEVEL_DBID = "eaddisplay-persistent-clevel-dbid";
        public static final String EAD_DISPLAY_PERSISTENT_PAGING = "eaddisplay-persistent-paging";
        public static final String FEATURED_EXHIBITION = "featured-exhibition-details";
        public static final String FEATURED_EXHIBITION_ARTICLE = "featured-exhibition-details-article";
        public static final String DIRECTORY_COUNTRY = "directory-country";
        public static final String DIRECTORY_INSTITUTION_ID = "directory-institution-id";
        public static final String DIRECTORY_INSTITUTION_CODE = "directory-institution-code";
        public static final String DIRECTORY_CONTENT = "directory-content";
        public static final String DIRECTORY_SITEMAP = "directory-sitemap";
        public static final String SEARCH = "ead-search";
        public static final String WIDGET_EAD_SEARCH = "widget-ead-search";
        public static final String WIDGET_SAVED_SEARCH = "widget-saved-search";
        public static final String TOPIC_SEARCH = "topic-search";
        public static final String TOPICS_PAGE = "topic-overview";
        public static final String SAVED_SEARCH = "saved-search";
        public static final String HELP_PAGES = "help-pages";
        public static final String SAVED_SEARCH_OVERVIEW = "saved-search-overview";
        public static final String SAVED_BOOKMARKS = "saved-bookmarks";
        public static final String SAVED_BOOKMARKS_OVERVIEW = "saved-bookmarks-overview";
        public static final String SAVED_COLLECTION_OVERVIEW = "saved-collection-overview";
        public static final String SEPARATOR = "/";
        public static final String EAC_CPF_DISPLAY = "eac-display";
        public static final String API_KEY = "get-api-key";

        private final static Map<String, String> urls = new HashMap<String, String>();
        static {
            urls.put(EAG_DISPLAY_PERSISTENT, "/institution/aicode/");
            urls.put(EAD_DISPLAY_PERSISTENT_TOP_LEVEL, "/archive/aicode/{repo_code}/type/{ead_type}/id/{record_id}");
            urls.put(EAD_DISPLAY_PERSISTENT_C_LEVEL_UNITID, "/archive/aicode/{repo_code}/type/{ead_type}/id/{record_id}/unitid/{unitid}");
            urls.put(EAD_DISPLAY_PERSISTENT_C_LEVEL_DBID, "/archive/aicode/{repo_code}/type/{ead_type}/id/{record_id}/dbid/{dbid}");
            urls.put(FEATURED_EXHIBITION, "/featured-document/-/fed/pk");
            urls.put(FEATURED_EXHIBITION_ARTICLE, "/featured-document/-/fed/a");
            urls.put(DIRECTORY_COUNTRY, "/directory/-/dir/co");
            urls.put(DIRECTORY_INSTITUTION_ID, "/directory/-/dir/ai/id");
            urls.put(DIRECTORY_INSTITUTION_CODE, "/directory/-/dir/ai/code");
            urls.put(DIRECTORY_CONTENT, "/directory/-/dir/content");
            urls.put(DIRECTORY_SITEMAP, "/sitemap.action");
            urls.put(SEARCH, "/search");
            urls.put(WIDGET_EAD_SEARCH, "/search/-/s/n");
            urls.put(WIDGET_SAVED_SEARCH, "/search/-/s/d");
            urls.put(SAVED_SEARCH, "/search/-/s/d");
            urls.put(TOPIC_SEARCH, "/search/-/s/n/topic");
            urls.put(HELP_PAGES, "/help");
            urls.put(TOPICS_PAGE, "/topics");
            urls.put(SAVED_SEARCH_OVERVIEW, "/saved-searches/-/sv");
            urls.put(SAVED_BOOKMARKS, "/bookmarks/-/s/d");
            urls.put(SAVED_BOOKMARKS_OVERVIEW, "/saved-bookmarks/-/sb");
            urls.put(EAC_CPF_DISPLAY, "/name/aicode/{repo_code}/id/{record_id}");
            urls.put(SAVED_COLLECTION_OVERVIEW, "/saved-collections/-/cs");
            urls.put(API_KEY,"/get-api-key");
        }

        public static String encodeUrl(String url){
            if (StringUtils.isNotBlank(url)){
                try {
                    return URLEncoder.encode(url, "UTF-8").replaceAll("%2F", "/").replaceAll("%3A", ":");
                } catch (UnsupportedEncodingException e) {
//                    LOGGER.error(ApeUtil.generateThrowableLog(e));
                }
            }
            return null;
        }

        public static String getSitemapUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + sitemapUrl.toString() ;
        }

        public static String getSitemapEAGUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + sitemapUrl.toString() + "&type=eag";
        }

        public static String getSitemapEAGActualUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.EAG_DISPLAY_PERSISTENT, false) ;
            return baseUrl + sitemapUrl.getRepoCode();
        }

        public static String getSitemapEADUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + sitemapUrl.toString() + "&type=ead";
        }
        public static String getSitemapFAUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + sitemapUrl.toString() + "&type=fa";
        }
        public static String getSitemapSGUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + sitemapUrl.toString() + "&type=sg";
        }
        public static String getSitemapHGUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + sitemapUrl.toString() + "&type=hg";
        }
        public static String getSitemapCUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + sitemapUrl.toString() + "&type=c";
        }

        public static String getSitemapEACUrl(SitemapUrl sitemapUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + sitemapUrl.toString() + "&type=eac";
        }

        public static String getSitemapUrl(EadPersistentUrl eadPerstistentUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.DIRECTORY_SITEMAP, false) ;
            return baseUrl + eadPerstistentUrl.toString() ;
        }

        public static String getEacCpfPersistentUrlForSitemap(EacCpfPersistentUrl eacCpfPerstistentUrl){
            String baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.EAC_CPF_DISPLAY, false) ;
            baseUrl = baseUrl.replace("{repo_code}", eacCpfPerstistentUrl.getRepoCode());
            baseUrl = baseUrl.replace("{record_id}", encodeUrl(eacCpfPerstistentUrl.getIdentifier()));
            return baseUrl ;// + eacCpfPerstistentUrl.toString() ;
        }

        public static String getEadPersistentUrlForSitemap(EadPersistentUrl eadPerstistentUrl){
            String baseUrl = null;
            if (eadPerstistentUrl.getUnitid() == null && eadPerstistentUrl.getSearchId() == null) {
                baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.EAD_DISPLAY_PERSISTENT_TOP_LEVEL, false);
            }
            else {
                if (eadPerstistentUrl.getUnitid() != null){
                    baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.EAD_DISPLAY_PERSISTENT_C_LEVEL_UNITID, false);
                    baseUrl = baseUrl.replace("{unitid}", encodeUrl(eadPerstistentUrl.getUnitid()));
                }
                else {
                    baseUrl = FriendlyUrlUtil.getUrlWithoutLocalization(FriendlyUrlUtil.EAD_DISPLAY_PERSISTENT_C_LEVEL_DBID, false);
                    baseUrl = baseUrl.replace("{dbid}", eadPerstistentUrl.getSearchId());
                }
            }

            baseUrl = baseUrl.replace("{repo_code}", eadPerstistentUrl.getRepoCode());
            baseUrl = baseUrl.replace("{record_id}", encodeUrl(eadPerstistentUrl.getIdentifier()));
            baseUrl = baseUrl.replace("{ead_type}", eadPerstistentUrl.getXmlTypeName());

            return baseUrl;
        }

        public static String getUrlWithoutLocalization(String type, boolean noHttps) {
            try {
                String dashboardDomain = PropertiesUtil.get(PropertiesKeys.APE_DASHBOARD_DOMAIN);
                String urlHome = "https://"+dashboardDomain+"/Dashboard";
                if (!type.equals(FriendlyUrlUtil.DIRECTORY_SITEMAP)){
                    dashboardDomain = PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN_MODX);
                    urlHome = "https://"+dashboardDomain;
                }
                if (noHttps){
                    urlHome = urlHome.replaceFirst("https://", "http://");
                }
                return urlHome + urls.get(type);
            } catch (Exception e) {
//                LOGGER.error("Unable to generate url: " + e.getMessage());
            }
            return null;
        }
    }
}
