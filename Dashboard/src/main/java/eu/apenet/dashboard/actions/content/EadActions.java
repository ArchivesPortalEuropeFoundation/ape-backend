package eu.apenet.dashboard.actions.content;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;

public class EadActions extends AbstractEadActions {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    /**
     *
     */
    private static final long serialVersionUID = 2671921974304007944L;

    public String validateEad() {
        try {
            EadService.validate(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String convertEad(Properties properties) {
        try {
            EadService.convert(getXmlType(), id, properties);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String publishEad() {
        try {
            EadService.publish(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String unpublishEad() {
        try {
            EadService.unpublish(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String deleteEad() {
        try {
            EadService.delete(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String convertValidatePublishEad(Properties properties) {
        try {
            EadService.convertValidatePublish(getXmlType(), id, properties);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }

    }

    @Override
    public String deleteEseEdm() {
        try {
            EadService.deleteEseEdm(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteFromEuropeana() {
        try {
            EadService.deleteFromEuropeana(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deliverToEuropeana() {
        try {
            EadService.deliverToEuropeana(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteFromQueue() {
        try {
            EadService.deleteFromQueue(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String download() {
        try {
            File file = EadService.download(getId(), getXmlType());
            ContentUtils.downloadXml(this.getServletRequest(), getServletResponse(), file);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public String changeToDynamic() {
        try {
            EadService.makeDynamic(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String changeToStatic() {
        try {
            EadService.makeStatic(getXmlType(), id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String downloadHgSgStatistics() throws IOException, Exception {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, getXmlType().getClazz());
        SecurityContext.get().checkAuthorized(ead);
        CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
        HgSgFaRelationDAO hgSgFaRelationDAO = DAOFactory.instance().getHgSgFaRelationDAO();
        String name = APEnetUtilities.convertToFilename(ead.getEadid()) + "-statistics" + SIMPLE_DATE_FORMAT.format(new Date()) + ".ods";
        OutputStream outputStream = ContentUtils.getOutputStreamToDownload(this.getServletRequest(),
                getServletResponse(), name, "application/vnd.oasis.opendocument.spreadsheet");
        HgSgStatisticsDocumentWriter documentWriter = new HgSgStatisticsDocumentWriter();
        List<CLevel> cLevels = clevelDAO.getNotLinkedCLevels(id, getXmlType().getClazz());
        for (CLevel cLevel : cLevels) {
            HgSgStatistics hgSgStatistics = getCLevelStatistics(cLevel);
            documentWriter.addStatistics(hgSgStatistics);
        }
        List<HgSgFaRelation> hgSgFaRelations = hgSgFaRelationDAO.getHgSgFaRelations(id, getXmlType().getClazz(), false);
        for (HgSgFaRelation hgSgFaRelation : hgSgFaRelations) {
            CLevel cLevel = hgSgFaRelation.getHgSgClevel();
            FindingAid findingAid = hgSgFaRelation.getFindingAid();
            HgSgStatistics hgSgStatistics = getSgFaRelationsStatistics(cLevel, findingAid);
            documentWriter.addStatistics(hgSgStatistics);
        }
        documentWriter.save(outputStream);
        return null;
    }

    private HgSgStatistics getCLevelStatistics(CLevel cLevel) {
        HgSgStatistics hgSgStatistics = new HgSgStatistics(cLevel.getUnitid());
        hgSgStatistics.unittitle = cLevel.getUnittitle();
        hgSgStatistics.hrefEadid = cLevel.getHrefEadid();
        hgSgStatistics.linked = false;
        return hgSgStatistics;
    }

    private HgSgStatistics getSgFaRelationsStatistics(CLevel cLevel, FindingAid findingAid) {
        HgSgStatistics hgSgStatistics = new HgSgStatistics(cLevel.getUnitid());
        hgSgStatistics.unittitle = cLevel.getUnittitle();
        hgSgStatistics.hrefEadid = cLevel.getHrefEadid();
        hgSgStatistics.linked = true;
        hgSgStatistics.faPublished = findingAid.isPublished();
        hgSgStatistics.faTitle = findingAid.getTitle().replace('"', '\'');
        return hgSgStatistics;
    }

    private static class HgSgStatistics {

        private String unitid;
        private String unittitle;
        private String hrefEadid;
        private boolean linked;
        private boolean faPublished;
        private String faTitle;

        public HgSgStatistics(String unitid) {
            this.unitid = unitid;
        }
    }

    private class HgSgStatisticsDocumentWriter extends DocumentWriter {

        int rowNumber = 0;

        public HgSgStatisticsDocumentWriter() throws Exception {
            int colNumber = 0;
            fillHeaderCell(colNumber, rowNumber, "HG/SG unitid");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "HG/SG unittitle");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "HG/SG href eadid");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "Linked");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "FA published");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "FA title");
            colNumber++;
            rowNumber++;
        }

        public void addStatistics(HgSgStatistics hgSgStatistics) {
            int colNumber = 0;
            fillCell(colNumber, rowNumber, hgSgStatistics.unitid);
            colNumber++;
            fillCell(colNumber, rowNumber, hgSgStatistics.unittitle);
            colNumber++;
            fillCell(colNumber, rowNumber, hgSgStatistics.hrefEadid);
            colNumber++;
            if (hgSgStatistics.linked) {
                fillCell(colNumber, rowNumber, 1);
                colNumber++;
                if (hgSgStatistics.faPublished) {
                    fillCell(colNumber, rowNumber, 1);
                } else {
                    fillCell(colNumber, rowNumber, 0);
                }
                colNumber++;
                fillCell(colNumber, rowNumber, hgSgStatistics.faTitle);
            } else {
                fillCell(colNumber, rowNumber, 0);
            }
            colNumber++;
            rowNumber++;
        }
    }

    private class DocumentWriter {

        SpreadsheetDocument document;
        Table table;

        public DocumentWriter() throws Exception {
            document = SpreadsheetDocument.newSpreadsheetDocument();
            table = document.getSheetByIndex(0);
        }

        public void save(OutputStream outputStream) throws Exception {
            document.save(outputStream);
        }

        protected void fillHeaderCell(int col, int row, String stringValue) {
            table.getColumnByIndex(col).setUseOptimalWidth(true);
            Cell cell = table.getCellByPosition(col, row);
            Font font = cell.getFont();
            font.setFontStyle(StyleTypeDefinitions.FontStyle.BOLD);
            font.setSize(10);
            cell.setFont(font);
            cell.setDisplayText(stringValue);
        }

        protected void fillCell(int col, int row, String stringValue) {
            Cell cell = table.getCellByPosition(col, row);
            cell.setDisplayText(stringValue);
        }

        protected void fillCell(int col, int row, int stringValue) {
            Cell cell = table.getCellByPosition(col, row);
            cell.setDisplayText(stringValue + "");
        }

        protected void fillCell(int col, int row, long stringValue) {
            Cell cell = table.getCellByPosition(col, row);
            cell.setDisplayText(stringValue + "");
        }
    }
}
