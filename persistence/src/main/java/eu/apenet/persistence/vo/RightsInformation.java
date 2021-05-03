/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apef
 */
@Entity
@Table(name = "rights_information", catalog = "ape", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RightsInformation.findAll", query = "SELECT r FROM RightsInformation r")
    , @NamedQuery(name = "RightsInformation.findById", query = "SELECT r FROM RightsInformation r WHERE r.id = :id")
    , @NamedQuery(name = "RightsInformation.findByAbbreviation", query = "SELECT r FROM RightsInformation r WHERE r.abbreviation = :abbreviation")
    , @NamedQuery(name = "RightsInformation.findByLink", query = "SELECT r FROM RightsInformation r WHERE r.link = :link")
    , @NamedQuery(name = "RightsInformation.findByDescription", query = "SELECT r FROM RightsInformation r WHERE r.description = :description")})
public class RightsInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "abbreviation", nullable = false, length = 20)
    private String abbreviation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "link", nullable = false, length = 255)
    private String link;
    @Size(max = 2147483647)
    @Column(name = "description", length = 2147483647)
    private String description;
    
    @OneToMany(mappedBy = "rightsInformation")
    private Set<ArchivalInstitution> archivalInstitutions = new HashSet<>(0);

    @OneToMany(mappedBy = "rightsInformation")
    private Set<EacCpf> eacCpfs = new HashSet<>(0);

    @OneToMany(mappedBy = "rightsInformation")
    private Set<FindingAid> findingAids = new HashSet<>(0);

    @OneToMany(mappedBy = "rightsInformation")
    private Set<HoldingsGuide> holdingsGuides = new HashSet<>(0);

    @OneToMany(mappedBy = "rightsInformation")
    private Set<SourceGuide> sourceGuides = new HashSet<>(0);
    
    public RightsInformation() {
    }

    public RightsInformation(Integer id) {
        this.id = id;
    }

    public RightsInformation(Integer id, String abbreviation, String link) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.link = link;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ArchivalInstitution> getArchivalInstitutions() {
        return archivalInstitutions;
    }

    public void setArchivalInstitutions(Set<ArchivalInstitution> archivalInstitutions) {
        this.archivalInstitutions = archivalInstitutions;
    }

    public Set<EacCpf> getEacCpfs() {
        return eacCpfs;
    }

    public void setEacCpfs(Set<EacCpf> eacCpfs) {
        this.eacCpfs = eacCpfs;
    }

    public Set<FindingAid> getFindingAids() {
        return findingAids;
    }

    public void setFindingAids(Set<FindingAid> findingAids) {
        this.findingAids = findingAids;
    }

    public Set<HoldingsGuide> getHoldingsGuides() {
        return holdingsGuides;
    }

    public void setHoldingsGuides(Set<HoldingsGuide> holdingsGuides) {
        this.holdingsGuides = holdingsGuides;
    }

    public Set<SourceGuide> getSourceGuides() {
        return sourceGuides;
    }

    public void setSourceGuides(Set<SourceGuide> sourceGuides) {
        this.sourceGuides = sourceGuides;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RightsInformation)) {
            return false;
        }
        RightsInformation other = (RightsInformation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.apenet.persistence.dao.RightsInformation[ id=" + id + " ]";
    }
    
}
