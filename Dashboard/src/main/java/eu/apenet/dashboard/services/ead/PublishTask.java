package eu.apenet.dashboard.services.ead;

import java.util.List;
import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.services.ead.database.DatabaseEadPublisher;
import eu.apenet.dashboard.services.ead.xml.stream.DatabaseXmlEadParser;
import eu.apenet.dashboard.services.ead.xml.stream.XmlEadParser;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.ValidatedState;

public class PublishTask extends AbstractEadTask {
	public static boolean valid(Ead ead) {
		return ValidatedState.VALIDATED.equals(ead.getValidated()) && !ead.isPublished();
	}

	@Override
	public void execute(Ead ead, Properties properties) throws Exception {
		if (valid(ead)) {
			try {
				long startTime = System.currentTimeMillis();
				long solrTime = 0l;
				/*
				 * there is something wrong
				 */
				EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
				List<EadContent> eadContents = eadContentDAO.getEadContentsByFileId(ead.getId(), XmlType.getEadType(ead).getClazz());
				if (eadContents.size() > 1) {
					eadContentDAO.delete(eadContents);
					ead.getEadContents().clear();
				}
				String message = null;
				if (ead.getEadContent() == null) {
					message = "xml";
					solrTime = XmlEadParser.parseEadAndPublish(ead);
				} else {
					message = "database";
					solrTime = DatabaseXmlEadParser.publish(ead);
				}

				logSolrAction(ead, message, solrTime, System.currentTimeMillis() - (startTime + solrTime));
			} catch (Exception e) {
				logAction(ead, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}
		}
	}

	@Override
	protected String getActionName() {
		return "publish";
	}
}
