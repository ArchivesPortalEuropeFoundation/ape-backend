package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.dpt.utils.ead2edm.DigitalObjectCounter;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import eu.apenet.dpt.utils.ead2edm.XMLUtil;
import eu.apenet.dpt.utils.ead2edm.EdmConfig;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ValidatedState;
import eu.apenet.persistence.vo.Warnings;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class ConvertToEseEdmTask extends AbstractEadTask {

    private static final String FA_XML_TYPE = "fa";

    @Override
    protected String getActionName() {
        return "convert to ESE/EDM";
    }

    public static boolean valid(Ead ead) {
        if (ead instanceof FindingAid) {
            FindingAid findingAid = (FindingAid) ead;
            return ValidatedState.VALIDATED.equals(ead.getValidated())
                    && (EuropeanaState.NOT_CONVERTED.equals(findingAid.getEuropeana()) || EuropeanaState.FATAL_ERROR.equals(findingAid.getEuropeana()));
        }
        return false;
    }

    @Override
    protected void execute(Ead ead, Properties properties) throws Exception {
        if (valid(ead)) {
            FindingAid findingAid = (FindingAid) ead;

            try {
                EadDAO eadDAO = DAOFactory.instance().getEadDAO();
                if (!(findingAid.isPublished() && findingAid.getTotalNumberOfDaos() == 0)) {

                    EseDAO eseDao = DAOFactory.instance().getEseDAO();

                    // OAI identifier for each set will be built according to the syntax
                    // {repocode}:{eadid}, e.g. NL-HaNA:4.VTHR
                    String europeanaSetName = findingAid.getArchivalInstitution().getRepositorycode()
                            + APEnetUtilities.OAIPMH_SET_SEPARATOR + EdmFileUtils.encodeSpecialCharactersForFilename(findingAid.getEadid());

                    EdmConfig edmConfig = new EdmConfig(properties);
                    edmConfig.setEdmIdentifier(europeanaSetName);
                    edmConfig.setRepositoryCode(findingAid.getArchivalInstitution().getRepositorycode());
                    edmConfig.setHost(PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN_MODX));
                    edmConfig.setXmlTypeName(FA_XML_TYPE);
                    edmConfig.setOutputBaseDirectory(EdmFileUtils.getOutputEDMDirPath(APEnetUtilities.getConfig().getRepoDirPath(),
                            findingAid.getArchivalInstitution().getCountry().getIsoname(), findingAid
                            .getArchivalInstitution().getAiId()));

                    File apenetEad = EdmFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(),
                            findingAid.getPathApenetead());
                    File edmOutputDir = EdmFileUtils.getOutputEDMDir(edmConfig.getOutputBaseDirectory(), EdmFileUtils.encodeSpecialCharactersForFilename(findingAid.getEadid()));
                    edmOutputDir.mkdirs();
                    boolean errors = false;
                    StringBuilder warn = new StringBuilder();
                    try {
                        edmConfig.getTransformerXML2XML().transform(apenetEad);
                    } catch (TransformerException e) {
                        errors = true;
                        warn.append("<span class=\"validation-error\">");
                        warn.append("EDM conversion error: ").append(e.getMessage()).append("</span>").append("<br />");
                        if (ead instanceof FindingAid) {
                            ((FindingAid) ead).setEuropeana(EuropeanaState.FATAL_ERROR);
                        }
                    }
                    if (errors) {
                        boolean warningExists = false;
                        Set<Warnings> warningsFromEad = ead.getWarningses();
                        if (!warningsFromEad.isEmpty()) {
                            for (Warnings warning : warningsFromEad) {
                                if (warning.getIswarning()) {
                                    warningExists = true;
                                    warning.setAbstract_(warn.toString());
                                } else {
                                    warningsFromEad.remove(warning);
                                }
                            }
                        }
                        if (!warningExists) {
                            Warnings warnings = new Warnings();
                            warnings.setAbstract_(warn.toString());
                            warnings.setIswarning(true);
                            warnings.setEad(ead);
                            ead.getWarningses().add(warnings);
                        }
                    } else {
                        DigitalObjectCounter digitalObjectCounter = XMLUtil.analyzeEdmXml(edmOutputDir);
                        int numberOfRecords = digitalObjectCounter.getNumberOfProvidedCHO();

                        boolean update = false;
                        if (numberOfRecords > 1) {
                            if (findingAid.getEses().isEmpty()) {
                            } else {
                                update = true;
                            }

                            for (File file : edmOutputDir.listFiles()) {
                                Ese ese = new Ese();
                                ese.setCreationDate(new Date());
                                ese.setModificationDate(ese.getCreationDate());
                                ese.setEset(prepareEdmIdentifier(europeanaSetName));
                                ese.setPath(EdmFileUtils.getRelativeEDMFilePath(findingAid.getArchivalInstitution()
                                        .getCountry().getIsoname(), findingAid.getArchivalInstitution().getAiId(),
                                        edmOutputDir.getName()) + APEnetUtilities.FILESEPARATOR + file.getName());
                                ese.setEseState(DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.NOT_PUBLISHED));
                                ese.setFindingAid(findingAid);
                                ese.setOaiIdentifier(prepareEdmIdentifier(europeanaSetName + APEnetUtilities.OAIPMH_SET_SEPARATOR + URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString())));
                                ese.setNumberOfRecords(1);
                                ese.setNumberOfWebResource(1);
                                ese.setMetadataFormat(MetadataFormat.EDM);
                                if (update) {
                                    eseDao.update(ese);
                                } else {
                                    eseDao.store(ese);
                                }
                            }
                            findingAid.setTotalNumberOfChos(new Long(numberOfRecords));
                            findingAid.setTotalNumberOfWebResourceEdm(new Long(digitalObjectCounter.getNumberOfDigitalObjects()));
                            findingAid.setEuropeana(EuropeanaState.CONVERTED);
                        } else {
                            edmOutputDir.delete();
                            findingAid.setEuropeana(EuropeanaState.NO_EUROPEANA_CANDIDATE);
                            findingAid.setTotalNumberOfChos(0l);
                        }
                    }
                } else {
                    findingAid.setEuropeana(EuropeanaState.NO_EUROPEANA_CANDIDATE);
                }

                eadDAO.store(findingAid);
                logAction(ead);
            } catch (IOException | XMLStreamException | SAXException e) {
                logAction(ead, e);
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            }

        }
    }
    
    private String prepareEdmIdentifier(String input){
        // return input;
        input = input.replace("+", "%20");
        return input.replace("%", "P");
        
    }

}
