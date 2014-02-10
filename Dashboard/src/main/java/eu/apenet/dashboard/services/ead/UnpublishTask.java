package eu.apenet.dashboard.services.ead;

import java.io.IOException;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.solr.UpdateSolrServerHolder;
import eu.apenet.commons.solr.eads.EadSolrFields;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class UnpublishTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "unpublish";
	}
	public static boolean valid(Ead ead){
		return ead.isPublished();
	}
	@Override
	public void execute(Ead ead, Properties properties) throws Exception {
		if (valid(ead)) {
			try {
				long startTime = System.currentTimeMillis();
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				XmlType xmlType = XmlType.getEadType(ead);
				logger.debug("Removing the EAD (" + xmlType.getName() + ") with eadid '" + ead.getEadid()
						+ "' from the index");
				long solrTime = deleteFromSolr(ead.getEadid(), ead.getAiId());
				logger.debug("Changing EAD (" + xmlType.getName() + ") state of the EAD with eadid " + ead.getEadid());
				JpaUtil.beginDatabaseTransaction();
				ContentUtils.changeSearchable(ead, false);
				ead.setTotalNumberOfUnits(0l);
				ead.setTotalNumberOfUnitsWithDao(0l);
				eadDAO.insertSimple(ead);
				JpaUtil.commitDatabaseTransaction();
				logSolrAction(ead, "", solrTime, System.currentTimeMillis()-(startTime+solrTime));
			} catch (Exception e) {
				logAction(ead, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}
		}
	}

	private static long deleteFromSolr(String eadid, int aiId) throws SolrServerException, IOException {
		UpdateSolrServerHolder server = UpdateSolrServerHolder.getInstance();
		return server.deleteByQuery("(" + EadSolrFields.AI_ID + ":" + aiId + " AND " + EadSolrFields.EADID + ":\"" + eadid + "\")");
	}
}
