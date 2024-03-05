package eu.apenet.dashboard.services.eaccpf;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.analyzers.eaccpf.SocialInfoExtractor;
import eu.apenet.dashboard.services.eaccpf.xml.stream.XmlEacCpfParser;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.ValidatedState;

/**
 *
 * @author papp
 */
public class PublishTask extends AbstractEacCpfTask {

    @Override
    protected void execute(EacCpf eacCpf, Properties properties) throws Exception {
        if (valid(eacCpf)) {
            try {
                long startTime = System.currentTimeMillis();
                long solrTime = XmlEacCpfParser.parseAndPublish(eacCpf);
                logSolrAction(eacCpf, "", solrTime, System.currentTimeMillis() - (startTime + solrTime));

                //Also create its social metadata
                EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
                SocialInfoExtractor socialInfoExtractor = new SocialInfoExtractor();
                String content = socialInfoExtractor.extractStringInfo(eacCpf);
                eacCpf.setMetaContent(content);
                eacCpfDAO.update(eacCpf);

            } catch (Exception e) {
                logAction(eacCpf, e);
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            }
        }
    }

    @Override
    protected String getActionName() {
        return "publish";
    }

    static boolean valid(EacCpf eacCpf) {
        return ValidatedState.VALIDATED.equals(eacCpf.getValidated()) && !eacCpf.isPublished();
    }
}
