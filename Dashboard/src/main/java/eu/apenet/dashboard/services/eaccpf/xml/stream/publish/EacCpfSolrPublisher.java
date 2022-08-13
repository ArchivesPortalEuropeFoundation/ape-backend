package eu.apenet.dashboard.services.eaccpf.xml.stream.publish;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedHashSet;
import java.util.Set;

import eu.apenet.persistence.vo.RightsInformation;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.NodeList;

import eu.apenet.commons.solr.AbstractSolrServerHolder;
import eu.apenet.commons.solr.EacCpfSolrServerHolder;
import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;

public class EacCpfSolrPublisher extends AbstractSolrPublisher {

    private String recordId;

    public void publishEacCpf(EacCpf eacCpf, EacCpfPublishData eacCpfPublishData) throws MalformedURLException, SolrServerException, IOException {
        recordId = eacCpf.getIdentifier();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(Ead3SolrFields.ID, eacCpf.getId());
        add(doc, SolrFields.EAC_CPF_RECORD_ID, eacCpf.getIdentifier());
        addLowerCase(doc, SolrFields.EAC_CPF_FACET_ENTITY_TYPE, eacCpfPublishData.getEntityType());
        doc.addField(SolrFields.EAC_CPF_ENTITY_ID, eacCpfPublishData.getEntityIds());
        doc.addField(SolrFields.EAC_CPF_NAMES, eacCpfPublishData.getNames());
        doc.addField(SolrFields.EAC_CPF_PLACES, eacCpfPublishData.getPlaces());
        doc.addField(SolrFields.EAC_CPF_OCCUPATION, eacCpfPublishData.getOccupations());
        doc.addField(SolrFields.EAC_CPF_FACET_FUNCTION, eacCpfPublishData.getFunctions());
        doc.addField(SolrFields.EAC_CPF_FACET_MANDATE, eacCpfPublishData.getMandates());
        add(doc, SolrFields.EAC_CPF_DESCRIPTION, eacCpfPublishData.getDescription());
        add(doc, Ead3SolrFields.START_DATE, eacCpfPublishData.getFromDate());
        add(doc, Ead3SolrFields.END_DATE, eacCpfPublishData.getToDate());
        add(doc, SolrFields.EAC_CPF_DATE_DESCRIPTION, eacCpfPublishData.getDateDescription());
        add(doc, Ead3SolrFields.DATE_TYPE, eacCpfPublishData.getDateType());
        ArchivalInstitution archivalInstitution = eacCpf.getArchivalInstitution();
        add(doc, Ead3SolrFields.COUNTRY, archivalInstitution.getCountry().getEncodedCname() + COLON + SolrValues.TYPE_GROUP + COLON + archivalInstitution.getCountry().getId());
        doc.addField(Ead3SolrFields.COUNTRY_ID, archivalInstitution.getCountry().getId());
        add(doc, Ead3SolrFields.LANGUAGE, eacCpfPublishData.getLanguage());

        add(doc, Ead3SolrFields.AI, archivalInstitution.getAiname() + COLON + archivalInstitution.getAiId());
        doc.addField(Ead3SolrFields.AI_ID, archivalInstitution.getAiId());
        add(doc, Ead3SolrFields.OTHER, eacCpfPublishData.getOther());
        doc.addField(Ead3SolrFields.REPOSITORY_CODE, archivalInstitution.getRepositorycode());
        doc.addField(SolrFields.EAC_CPF_NUMBER_OF_MATERIAL_RELATIONS, eacCpfPublishData.getNumberOfArchivalMaterialRelations());
        doc.addField(SolrFields.EAC_CPF_NUMBER_OF_NAME_RELATIONS, eacCpfPublishData.getNumberOfNameRelations());
        doc.addField(SolrFields.EAC_CPF_NUMBER_OF_INSTITUTIONS_RELATIONS, eacCpfPublishData.getNumberOfInstitutionsRelations());

        if (eacCpf.getRightsInformation() != null) {
            doc.addField(SolrFields.EAG_LICENCE_NAME, eacCpf.getRightsInformation().getRightsName());
            doc.addField(SolrFields.EAG_LICENCE_ABBREVIATION, eacCpf.getRightsInformation().getAbbreviation());
            doc.addField(SolrFields.EAG_LICENCE_SHAREABLE, getLicenceShareableType(eacCpf.getRightsInformation()));
        }

        doc.addField(Ead3SolrFields.OPEN_DATA, archivalInstitution.isOpenDataEnabled());
        addSolrDocument(doc);
    }

    @Override
    protected String getKey() {
        return recordId;
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
        return EacCpfSolrServerHolder.getInstance();
    }

    public long unpublish(EacCpf eacCpf) throws SolrServerException, IOException {
        return getSolrServerHolder().deleteByQuery("(" + Ead3SolrFields.AI_ID + ":" + eacCpf.getAiId() + " AND " + Ead3SolrFields.ID + ":\"" + eacCpf.getId() + "\")");
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
            case "CC BY-ND":
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
}
