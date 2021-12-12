package eu.apenet.dashboard.services.eag.xml.stream.publish;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import eu.apenet.persistence.vo.RightsInformation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.NodeList;

import eu.apenet.commons.solr.AbstractSolrServerHolder;
import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.EagSolrServerHolder;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class EagSolrPublisher extends AbstractSolrPublisher {

    private static final Logger LOGGER = Logger.getLogger(EagSolrPublisher.class);
    public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");

    private String recordId;

    public void publish(ArchivalInstitution archivalInstitution, EagPublishData publishData) throws MalformedURLException, SolrServerException, IOException {
        recordId = archivalInstitution.getAiId() + "";
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(Ead3SolrFields.ID, archivalInstitution.getAiId());
        doc.addField(Ead3SolrFields.REPOSITORY_CODE, archivalInstitution.getRepositorycode());
        doc.addField(SolrFields.EAG_NAME, archivalInstitution.getAiname());
        doc.addField(SolrFields.EAG_OTHER_NAMES, publishData.getOtherNames());
        doc.addField(SolrFields.EAG_ADDRESS, publishData.getAddress());
        doc.addField(SolrFields.EAG_REPOSITORIES, publishData.getRepositories());
        doc.addField(SolrFields.EAG_REPOSITORY_TYPE, convertRepositoryTypes(publishData.getRepositoryTypes()));
        add(doc, SolrFields.EAG_DESCRIPTION, publishData.getDescription());
        add(doc, SolrFields.EAG_OTHER, publishData.getOther());
        doc.addField(SolrFields.EAG_PLACES, publishData.getPlaces());
        doc.addField(SolrFields.EAG_AI_GROUPS, publishData.getAiGroups());
        doc.addField(SolrFields.EAG_AI_GROUPS_FACET, publishData.getAiGroupFacets());
        doc.addField(SolrFields.EAG_AI_GROUP_ID, publishData.getAiGroupIds());
        doc.addField(SolrFields.EAG_COUNTRIES, publishData.getCountries());
        add(doc, Ead3SolrFields.COUNTRY, archivalInstitution.getCountry().getEncodedCname() + COLON + SolrValues.TYPE_GROUP + COLON + archivalInstitution.getCountry().getId());
        doc.addField(Ead3SolrFields.COUNTRY_ID, archivalInstitution.getCountry().getId());

        doc.addField(SolrFields.EAG_LICENCE_NAME, archivalInstitution.getShareWithWikimedia().getRightsName());
        doc.addField(SolrFields.EAG_LICENCE_ABBREVIATION, archivalInstitution.getShareWithWikimedia().getAbbreviation());
        doc.addField(SolrFields.EAG_LICENCE_SHAREABLE, getLicenceShareableType(archivalInstitution.getShareWithWikimedia()));

        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        doc.addField(Ead3SolrFields.OPEN_DATA, archivalInstitutionDao.findById(archivalInstitution.getAiId()).isOpenDataEnabled());

        addSolrDocument(doc);
    }

    public void deleteEverything() throws SolrServerException {
        rollbackSolrDocuments("*:*");
    }

    @Override
    protected String getKey() {
        return recordId;
    }

    private static String getLicenceShareableType(RightsInformation rightsInformation){
        String result = "";

        switch (rightsInformation.getAbbreviation()){
            case "CC BY":
            case "CC BY-SA":
            case "CC0":
            case "PDM":
                result = "yes";
                break;
            case "CC BY-NC":
            case "CC BY-NC-ND":
            case "CC BY-NC-SA":
            case "NoC-NC":
            case "NoC-OKLR":
            case "InC-EDU":
                result = "yesWithConditions";
                break;
            case "InC":
            case "InC-EU-OW":
            case "CNE":
            default:
                result = "maybeSeekPermission";
                break;
        }

        return result;
    }

    private static Set<String> convertRepositoryTypes(Set<String> repositoryTypes) {
        Set<String> result = new HashSet<String>();
        for (String repositoryType : repositoryTypes) {
            if ("Business archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_BUSINESS_ARCHIVES);
            } else if ("Church and religious archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_CHURCH_ARCHIVES);
            } else if ("County/local authority archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_COUNTY_ARCHIVES);
            } else if ("Specialised non-governmental archives and archives of other cultural (heritage) institutions".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_CULTURAL_ARCHIVES);
            } else if ("Media archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_MEDIA_ARCHIVES);
            } else if ("Municipal archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_MUNICIPAL_ARCHIVES);
            } else if ("National archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_NATIONAL_ARCHIVES);
            } else if ("Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_POLITICAL_ARCHIVES);
            } else if ("Private persons and family archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_PRIVATE_ARCHIVES);
            } else if ("Regional archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_REGIONAL_ARCHIVES);
            } else if ("Specialised government archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_SPECIALISED_ARCHIVES);
            } else if ("University and research archives".equalsIgnoreCase(repositoryType)) {
                result.add(SolrValues.EAG_REPOSITORY_TYPE_UNIVERSITY_ARCHIVES);
            }

        }
        return result;
    }

    protected static Set<String> getTextsWithoutMultiplity(NodeList nodeList, boolean strip) {
        Set<String> results = new LinkedHashSet<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            String text = removeUnusedCharacters(nodeList.item(i).getTextContent());
            if (StringUtils.isNotBlank(text)) {
                if (strip) {
                    int index = text.indexOf('(');
                    if (index > 0) {
                        text = text.substring(0, index).trim();
                    }
                }
                results.add(text);
            }
        }
        return results;
    }

    @Override
    protected AbstractSolrServerHolder getSolrServerHolder() {
        return EagSolrServerHolder.getInstance();
    }

    public long unpublish(ArchivalInstitution archivalInstitution) throws SolrServerException, IOException {
        return getSolrServerHolder().deleteByQuery("(" + Ead3SolrFields.ID + ":" + archivalInstitution.getAiId() + ")");
    }
}
