package eu.apenet.dashboard.services.ead.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.services.ead.publish.PublishData;
import eu.apenet.dashboard.services.ead.publish.SolrPublisher;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class DatabaseEadPublisher {
	private static final Logger LOG = Logger.getLogger(DatabaseEadPublisher.class);

	public static long publish(Ead ead) throws Exception {
		EadDatabaseSaver eadDatabaseSaver = new EadDatabaseSaver();
		CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
		EadContent eadContent = ead.getEadContent();
		List<LevelInfo> upperLevels = new ArrayList<LevelInfo>();
		ArchivalInstitution ai = ead.getArchivalInstitution();
		Map<String, Object> fullHierarchy = new HashMap<String, Object>();
		upperLevels.add(new LevelInfo(ead.getId()));
		String initialFilePath = ead.getPathApenetead();
		String eadid = eadContent.getEadid();
		List<ArchivalInstitution> ais = new ArrayList<ArchivalInstitution>();
		while (ai != null) {
			ais.add(ai);
			ai = ai.getParent();
		}
		int depth = 0;
		for (int i = ais.size() - 1; i >= 0; i--) {
			ArchivalInstitution currentAi = ais.get(i);
			String id = SolrValues.AI_PREFIX + currentAi.getAiId();
			String newFacetField = currentAi.getAiname();
			if (currentAi.isGroup()) {
				newFacetField += SolrPublisher.COLON + SolrValues.TYPE_GROUP;
			} else {
				newFacetField += SolrPublisher.COLON + SolrValues.TYPE_LEAF;
			}
			newFacetField += SolrPublisher.COLON + id;
			fullHierarchy.put(SolrFields.AI_DYNAMIC + depth + SolrFields.DYNAMIC_STRING_SUFFIX, newFacetField);
			fullHierarchy.put(SolrFields.AI_DYNAMIC_ID + depth + SolrFields.DYNAMIC_STRING_SUFFIX, id);
			depth++;
		}

		EADCounts eadCounts = new EADCounts();
		SolrPublisher solrPublisher = new SolrPublisher(ead);
		Class<? extends Ead> clazz = XmlType.getEadType(ead).getClazz();
		try {
			PublishData publishData = new PublishData();
			publishData.setXml(eadContent.getXml());
			publishData.setId(ead.getId().longValue());
			publishData.setUpperLevelUnittitles(upperLevels);
			publishData.setFullHierarchy(fullHierarchy);
			publishData.setArchdesc(true);
			eadCounts.addNumberOfDAOs(solrPublisher.parseHeader(eadContent, publishData));
			Set<String> unitids = new HashSet<String>();
			int cOrderId = 0;
			CLevel clevel = clevelDAO.getTopClevelByFileId(ead.getId(), clazz, cOrderId);
			while (clevel != null) {
				eadCounts.addEadCounts(DatabaseCLevelPublisher.publish(clevel,eadContent.getEcId(),ead, solrPublisher, upperLevels, fullHierarchy,unitids, eadDatabaseSaver));
				cOrderId++;
				clevel = clevelDAO.getTopClevelByFileId(ead.getId(), clazz, cOrderId);
			}
			JpaUtil.beginDatabaseTransaction();
			eadDatabaseSaver.updateAll();
			solrPublisher.commitAll(eadCounts);
			JpaUtil.commitDatabaseTransaction();

		} catch (Exception de) {
			if ((initialFilePath != null) && (initialFilePath.contains(APEnetUtilities.FILESEPARATOR))) {
				LOG.error("Unable to publish ead file to solr: " + de.getMessage(), de);
			}
			JpaUtil.rollbackDatabaseTransaction();
			LOG.error(eadid + ": rollback:", de);
			solrPublisher.rollback();
			throw de;
		}
		return solrPublisher.getSolrTime();
	}


}