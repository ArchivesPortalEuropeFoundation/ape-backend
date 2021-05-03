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
import javax.persistence.Basic;
import javax.persistence.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "c_level")
public class CLevel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2806155070360797061L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "order_id")
    private Integer orderId;
    private boolean leaf;
    @Column(name = "ec_id")
    private Long ecId;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ec_id", foreignKey = @ForeignKey(name = "c_level_ec_id_fkey"), insertable = false, updatable = false)
    private EadContent eadContent;

    @Column(columnDefinition = "TEXT")
    private String unittitle;

    @Column(columnDefinition = "TEXT")
    private String unitid;

    @Column(name = "duplicate_unitid")
    private boolean duplicateUnitid;

    @Column(name = "cid", columnDefinition = "TEXT")
    private String cid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_cl_id", foreignKey = @ForeignKey(name = "c_level_parent_id_fkey"), insertable = false, updatable = false)
    private CLevel parent;

    @Column(name = "parent_cl_id")
    private Long parentId;
    @Column(name = "href_eadid")
    private String hrefEadid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ead3_id", foreignKey = @ForeignKey(name = "ead3_id_fkey"))
    private Ead3 ead3;

    @OneToMany(mappedBy = "hgSgClevel")
    private Set<HgSgFaRelation> hgSgFaRelations = new HashSet<HgSgFaRelation>();

    @Column(columnDefinition = "TEXT")
    private String xml;

    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] cBinary;

    @Column(name = "rights_information")
    private Integer rightsInformationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rights_information", foreignKey = @ForeignKey(name = "archival_institution_rinf_id_fkey"), insertable = false, updatable = false)
    private RightsInformation rightsInformation;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getcBinary() {
        return cBinary;
    }

    public void setcBinary(byte[] cBinary) {
        this.cBinary = cBinary;
    }

    public HgSgFaRelation getHgSgFaRelation() {
        Set<HgSgFaRelation> set = getHgSgFaRelations();
        if (set == null || set.isEmpty()) {
            return null;
        }
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

    public Ead3 getEad3() {
        return ead3;
    }

    public void setEad3(Ead3 ead3) {
        this.ead3 = ead3;
    }

    public Integer getRightsInformationId() {
        return rightsInformationId;
    }

    public void setRightsInformationId(Integer rightsInformationId) {
        this.rightsInformationId = rightsInformationId;
    }

    public RightsInformation getRightsInformation() {
        return rightsInformation;
    }

    public void setRightsInformation(RightsInformation rightsInformation) {
        this.rightsInformation = rightsInformation;
    }
}
