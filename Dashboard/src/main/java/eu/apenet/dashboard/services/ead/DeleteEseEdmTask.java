package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;

public class DeleteEseEdmTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "delete ESE/EDM";
	}

	public static boolean valid(Ead ead) {
		if (ead instanceof FindingAid) {
			FindingAid findingAid = (FindingAid) ead;
			return EuropeanaState.CONVERTED.equals(findingAid.getEuropeana());
		}
		return false;
	}

	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		if (valid(ead)) {

			FindingAid findingAid = (FindingAid) ead;

			try {

				// Remove all ese files related to the finding aid from ese
				// table and
				// physically from the repository
				List<Ese> eseList = new ArrayList<Ese>();
				EseDAO eseDao = DAOFactory.instance().getEseDAO();
				eseList = eseDao.getEses(ead.getId(), findingAid.getAiId());

				for (int i = 0; i < eseList.size(); i++) {
					Ese ese = eseList.get(i);

					ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPath(), false);
					if (ese.getPathHtml() != null) {
						FileUtils.deleteDirectory(new File(APEnetUtilities.getConfig().getRepoDirPath()
								+ ese.getPathHtml() + "dir"));
						ContentUtils
								.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPathHtml(), false);
					}
					if (ese.getEseState().getState().equals(EseState.NOT_PUBLISHED)) {
						eseDao.delete(ese);
					} else {
						ese.setPath(null);
						eseDao.update(ese);
					}

				}
				findingAid.setEuropeana(EuropeanaState.NOT_CONVERTED);
				eadDAO.store(findingAid);
				logAction(ead, true);
			} catch (Exception e) {
				logAction(ead, false);
				throw new APEnetException("Could not delete ese/edm the file with ID: " + ead.getId(), e);
			}
		}

	}

}
