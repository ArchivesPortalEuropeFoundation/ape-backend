/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public abstract class AbstractTypeActions extends AbstractInstitutionAction {

    protected static final String CONVERT_VALIDATE_PUBLISH = "convert_validate_publish";
    protected static final String CONVERT_VALIDATE = "convert_validate";
    protected static final String DELETE = "delete";
    protected static final String UNPUBLISH = "unpublish";
    protected static final String PUBLISH = "publish";
    protected static final String VALIDATE = "validate";
    protected static final String CONVERT = "convert";
    protected static final String DELETE_ESE_EDM = "deleteEseEdm";
    protected static final String DELETE_FROM_QUEUE = "deleteFromQueue";
    protected static final String DELETE_FROM_EUROPEANA = "deleteFromEuropeana";
    protected static final String DELIVER_TO_EUROPEANA = "deliverToEuropeana";
    protected static final String VALIDATE_PUBLISH = "validate_publish";
    protected static final String REINDEX_SOCIAL = "reindexsocial";

    /**
     *
     */
    protected static final long serialVersionUID = 8481634493528974541L;
    protected Logger logger = Logger.getLogger(getClass());
    protected Integer xmlTypeId;
    protected String action;

    public Integer getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(Integer xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    protected abstract Properties getConversionParameters();

    protected XmlType getXmlType() {
        return XmlType.getType(xmlTypeId);
    }

    public abstract String deleteEseEdm();

    public abstract String deleteFromEuropeana();

    public abstract String deliverToEuropeana();

    public abstract String deleteFromQueue();

}
