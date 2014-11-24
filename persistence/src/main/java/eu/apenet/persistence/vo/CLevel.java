package eu.apenet.persistence.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import eu.archivesportaleurope.util.ApeUtil;

@Entity
@Table(name = "c_level")
public class CLevel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2806155070360797061L;
	

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long clId;
	@Column(name = "order_id")
	private Integer orderId;
	private boolean leaf;
	@Column(name = "ec_id")
	private Long ecId;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ec_id", insertable=false, updatable=false)
	private EadContent eadContent;
	private String unittitle;
	private String unitid;
	@Column(name = "duplicate_unitid")	
	private boolean duplicateUnitid;
	@Column(name = "cid")
	private String cid;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_cl_id", insertable=false, updatable=false)
	private CLevel parent;
	
	@Column(name = "parent_cl_id")
	private Long parentClId;
	@Column(name = "href_eadid")
	private String hrefEadid;

	@OneToMany(mappedBy="hgSgClevel")
	private Set<HgSgFaRelation> hgSgFaRelations = new HashSet<HgSgFaRelation>();
	
	private String xml;

	public String getUnittitle() {
		return unittitle;
	}
	public void setUnittitle(String unittitle) {
		this.unittitle = unittitle;
	}
    public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public CLevel getParent() {
		return parent;
	}
	public void setParent(CLevel parent) {
		this.parent = parent;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public Long getParentClId() {
		return parentClId;
	}
	public void setParentClId(Long parentClId) {
		this.parentClId = parentClId;
	}
	public Long getClId() {
		return clId;
	}
	public void setClId(Long clId) {
		this.clId = clId;
	}

	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public Long getEcId() {
		return ecId;
	}
	public void setEcId(Long ecId) {
		this.ecId = ecId;
	}
	public EadContent getEadContent() {
		return eadContent;
	}
	public void setEadContent(EadContent eadContent) {
		this.eadContent = eadContent;
	}
	public void setHrefEadid(String hrefEadid) {
		this.hrefEadid = hrefEadid;
	}
	public String getHrefEadid() {
		return hrefEadid;
	}
	public Set<HgSgFaRelation> getHgSgFaRelations() {
		return hgSgFaRelations;
	}
	public void setHgSgFaRelations(Set<HgSgFaRelation> hgSgFaRelations) {
		this.hgSgFaRelations = hgSgFaRelations;
	}
    public HgSgFaRelation getHgSgFaRelation() {
        Set<HgSgFaRelation> set = getHgSgFaRelations();
        if(set == null || set.isEmpty())
            return null;
        return set.iterator().next();
    }
	public boolean isDuplicateUnitid() {
		return duplicateUnitid;
	}
	public void setDuplicateUnitid(boolean duplicateUnitid) {
		this.duplicateUnitid = duplicateUnitid;
	}
	
	public String getEncodedCid() {
		return ApeUtil.encodeSpecialCharacters(cid);
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}


    
}
