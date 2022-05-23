package eu.apenet.api.ead.tree;

public class EadTreeParams {

    private static final int ZERO = 0;

    private Long parentId;
    private Long ecId;
    private String solrId;
    private String more;
    private Integer orderId = ZERO;
    private Integer max;
    private String xmlTypeName;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getEcId() {
        return ecId;
    }

    public void setEcId(Long ecId) {
        this.ecId = ecId;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getSolrId() {
        return solrId;
    }

    public void setSolrId(String solrId) {
        this.solrId = solrId;
    }

    public String getXmlTypeName() {
        return xmlTypeName;
    }

    public void setXmlTypeName(String xmlTypeName) {
        this.xmlTypeName = xmlTypeName;
    }

}
