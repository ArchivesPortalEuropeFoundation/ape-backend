package eu.apenet.dashboard.services.eag.xml.stream.publish;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.NodeList;

import eu.apenet.commons.solr.AbstractSolrServerHolder;
import eu.apenet.commons.solr.EagSolrServerHolder;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class EagSolrPublisher  extends AbstractSolrPublisher{


	private static final Logger LOGGER = Logger.getLogger(EagSolrPublisher.class);
	public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");


	private String recordId;


	public void publish(ArchivalInstitution archivalInstitution, EagPublishData publishData) throws MalformedURLException, SolrServerException, IOException {
		recordId = archivalInstitution.getAiId() +"";
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField(SolrFields.ID, archivalInstitution.getAiId());
		doc.addField(SolrFields.REPOSITORY_CODE, archivalInstitution.getRepositorycode());
		doc.addField(SolrFields.EAG_NAME, archivalInstitution.getAiname());		
		doc.addField(SolrFields.EAG_OTHER_NAMES, publishData.getOtherNames());		
		doc.addField(SolrFields.EAG_PLACES, publishData.getPlaces());		
		doc.addField(SolrFields.EAG_REPOSITORIES, publishData.getRepositories());	
		doc.addField(SolrFields.EAG_REPOSITORY_TYPE, publishData.getRepositoryTypes());
		add(doc, SolrFields.EAG_DESCRIPTION, publishData.getDescription());
		add(doc, SolrFields.EAG_OTHER, publishData.getOther());	
		add(doc, SolrFields.EAG_LANGUAGE, publishData.getLanguage());	
		
		doc.addField(SolrFields.EAG_AI_GROUPS, publishData.getAiGroups());
		doc.addField(SolrFields.EAG_AI_GROUPS_FACET, publishData.getAiGroupFacets());
		doc.addField(SolrFields.EAG_AI_GROUP_ID, publishData.getAiGroupIds());
		doc.addField(SolrFields.EAG_COUNTRIES, publishData.getCountries());
		add(doc, SolrFields.COUNTRY, archivalInstitution.getCountry().getEncodedCname() + COLON + SolrValues.TYPE_GROUP + COLON + archivalInstitution.getCountry().getId());
		doc.addField(SolrFields.COUNTRY_ID, archivalInstitution.getCountry().getId());

		addSolrDocument(doc);
	}
	public void deleteEverything() throws SolrServerException{
		rollbackSolrDocuments("*:*");
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
				if (strip){
					int index = text.indexOf('(');
					if (index > 0){
						text  = text.substring(0, index).trim();
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
		return getSolrServerHolder().deleteByQuery("(" + SolrFields.ID + ":" + archivalInstitution.getAiId()+ "\")");
	}
}
