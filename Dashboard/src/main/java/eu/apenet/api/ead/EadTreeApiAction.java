package eu.apenet.api.ead;

import com.opensymphony.xwork2.Action;
import eu.apenet.api.ead.tree.EadTreeJSONWriter;
import eu.apenet.api.ead.tree.EadTreeParams;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EadContent;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EadTreeApiAction {

    private static final int ZERO = 0;

    private Long parentId;
    private String ecId;
    //private Long faId;
    private String clevelId;
    private String more;
    private Integer orderId = ZERO;
    private Integer max;
    private String xmlTypeName;

    private String tree;

    public EadTreeApiAction() {

    }

    public String execute() {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();

        EadContent eadContent = null;
        if (ecId != null) {
            EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
            if (ecId.startsWith("F")) {
                eadContent = eadContentDAO.getEadContentByFindingAidId(Integer.parseInt(ecId.substring(1)));
            } else if (ecId.startsWith("S")) {
                eadContent = eadContentDAO.getEadContentBySourceGuideId(Integer.parseInt(ecId.substring(1)));
            }
            if (ecId.startsWith("H")) {
                eadContent = eadContentDAO.getEadContentByHoldingsGuideId(Integer.parseInt(ecId.substring(1)));
            }
        }

        StrutsResourceBundleSource strutsResourceBundleSource = new StrutsResourceBundleSource();
        EadTreeParams eadTreeParams = new EadTreeParams();
        if (eadContent!=null) {
            eadTreeParams.setEcId(eadContent.getEcId());
        }
        eadTreeParams.setParentId(getParentId());
        eadTreeParams.setSolrId(getClevelId());
        eadTreeParams.setOrderId(getOrderId());
        eadTreeParams.setMax(getMax());
        eadTreeParams.setMore(getMore());
        eadTreeParams.setXmlTypeName(getXmlTypeName());

        EadTreeJSONWriter eadTreeJSONWriter = new EadTreeJSONWriter();
        eadTreeJSONWriter.setMessageSource(strutsResourceBundleSource);
        eadTreeJSONWriter.writeJSON(eadTreeParams, request, response);

        response.setHeader("Access-Control-Allow-Origin","*");
        return Action.SUCCESS;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public void setXmlTypeName(String xmlTypeName) {
        this.xmlTypeName = xmlTypeName;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getMore() {
        return more;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getMax() {
        return max;
    }

    public String getXmlTypeName() {
        return xmlTypeName;
    }

    public String getTree() {
        return tree;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId;
    }

    public void setClevelId(String clevelId) {
        this.clevelId = clevelId;
    }

    public String getEcId() {
        return ecId;
    }

    public String getClevelId() {
        return clevelId;
    }
}
