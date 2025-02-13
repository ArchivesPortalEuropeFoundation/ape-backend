package eu.apenet.dashboard.actions.content;

import com.opensymphony.xwork2.Action;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.xslt.ead.EadXslt;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.listener.QueueDaemon;
import eu.apenet.dashboard.queue.DisplayQueueItem;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.*;

public class ContentManagerStatusAction extends AbstractInstitutionAction {

    public ContentManagerStatusAction() {

    }

    public String execute() throws IOException, SaxonApiException {
        HttpServletResponse response = ServletActionContext.getResponse();

        QueueItemDAO queueDAO = DAOFactory.instance().getQueueItemDAO();
        UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();

        getServletRequest().setAttribute("totalItemsInQueue", queueDAO.countItems());
        getServletRequest().setAttribute("aiItemsInQueue", queueDAO.countItems(getAiId()));
        getServletRequest().setAttribute("positionInQueue", queueDAO.getPositionOfFirstItem(getAiId()));
        getServletRequest().setAttribute("queueActive", QueueDaemon.isActive());
        getServletRequest().setAttribute("errorItems", convert(DAOFactory.instance().getQueueItemDAO().getErrorItemsOfInstitution(getAiId())));

        getServletRequest().setAttribute("aiUpFiles", upFileDAO.countNewUpFiles(getAiId(), FileType.XML));

        response.setHeader("Access-Control-Allow-Origin","*");
        return Action.SUCCESS;
    }

    private List<DisplayQueueItem> convert(List<QueueItem> queueItems) {
        List<DisplayQueueItem> results = new ArrayList<DisplayQueueItem>();
        for (QueueItem queueItem : queueItems) {
            DisplayQueueItem displayItem = new DisplayQueueItem();
            displayItem.setId(queueItem.getId());
            displayItem.setAction(queueItem.getAction().toString());
            displayItem.setPriority(queueItem.getPriority());
            displayItem.setErrors(queueItem.getErrors());
            try {
                if (queueItem.getAbstractContent() != null) {
                    AbstractContent content = queueItem.getAbstractContent();
                    displayItem.setEadidOrFilename(content.getIdentifier());
                    displayItem.setArchivalInstitution(content.getArchivalInstitution().getAiname());
                } else if (queueItem.getUpFile() != null) {
                    UpFile upFile = queueItem.getUpFile();
                    displayItem.setEadidOrFilename(upFile.getPath() + upFile.getFilename());
                    displayItem.setArchivalInstitution(upFile.getArchivalInstitution().getAiname());
                }
                if (QueueAction.USE_PROFILE.equals(queueItem.getAction())) {
                    Properties preferences = EadService.readProperties(queueItem.getPreferences());
                    IngestionprofileDefaultUploadAction ingestionprofileDefaultUploadAction = IngestionprofileDefaultUploadAction
                            .getUploadAction(preferences.getProperty(QueueItem.UPLOAD_ACTION));
                    displayItem.setAction(displayItem.getAction() + " ("
                            + getText(ingestionprofileDefaultUploadAction.getResourceName()) + ")");
                }
            } catch (Exception e) {

            }
            results.add(displayItem);
        }
        return results;
    }
}
