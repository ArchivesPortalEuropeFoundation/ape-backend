/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

/**
 *
 * @author kaisar
 */
import eu.apenet.commons.solr.Ead3SolrFields;
import eu.archivesportaleurope.apeapi.utils.CommonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.common.SolrDocument;

@XmlRootElement
@ApiModel
public class EadDocResponse {

    @ApiModelProperty(required = true, value = "Internal APE identifier of the result")
    private final String id;
    @ApiModelProperty(value = "Title of the finding aid. ")
    private final String findingAidTitle;
    @ApiModelProperty(value = "Number of search hits on the current document.")
    private final long numberOfResults;

    @ApiModelProperty(value = "Name of the repository holding the fonds")
    private String repository;

    @ApiModelProperty(value = "Name of the country where the repository is. In English. ")
    private String country;

    @ApiModelProperty(value = "Language of the description of the result.")
    private String language;

    @ApiModelProperty(value = "Code of the repository holding the fonds. Preferably, but not necessarily <a target='_blank' href='https://en.wikipedia.org/wiki/International_Standard_Identifier_for_Libraries_and_Related_Organizations'>ISIL</a>")
    private String repositoryCode;

    @ApiModelProperty(value = "Fonds unit ID")
    private String findingAidNo;

    @ApiModelProperty(value = "Unit date")
    private String unitDate;

    @ApiModelProperty(value = "Abstract of the content / Scope Content")
    private String scopeContent;

    @ApiModelProperty(value = "Number of DAO")
    private int numberOfDigitalObjects = 0;

    @ApiModelProperty(value = "Number of DAO below")
    private int numberOfDigitalObjectsInDescendents = 0;

    @ApiModelProperty(value = "Number of Descendents")
    private int numberOfDescendents = 0;

    public EadDocResponse(Group group) {
        //ex: Inventaris van het archief van de Nederlandse Ambassade in Nepal, 1965-1974:G:F124
        String temp = group.getGroupValue();
        int lastColonIndex = temp.lastIndexOf(":");
        this.id = temp.substring(lastColonIndex + 1);
        temp = temp.substring(0, lastColonIndex);
        lastColonIndex = temp.lastIndexOf(":");
        this.findingAidTitle = temp.substring(0, lastColonIndex);
        this.numberOfResults = group.getResult().getNumFound();
        //get the default document
        SolrDocument solrDocument = group.getResult().get(0);

        this.language = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.LANGUAGE));
        this.country = CommonUtils.splitByColon(CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.COUNTRY)), 0);
        this.repository = CommonUtils.splitByColon(CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.AI)), 0);
        this.repositoryCode = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.REPOSITORY_CODE));
        this.findingAidNo = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.RECORD_ID));
        this.unitDate = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.ALTERNATE_UNIT_DATE));
        this.scopeContent = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.SCOPE_CONTENT));

        Object dao = solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_DAO);
        if (dao != null) {
            this.numberOfDigitalObjects = Integer.parseInt(CommonUtils.objectToString(dao));
        }

        Object daoBelow = solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_DAO_BELOW);
        if (daoBelow != null) {
            this.numberOfDigitalObjectsInDescendents = Integer.parseInt(CommonUtils.objectToString(daoBelow));
        }

        Object des = solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_DESCENDENTS);
        if (des != null) {
            this.numberOfDescendents = Integer.parseInt(CommonUtils.objectToString(des));
        }
    }

    public String getId() {
        return id;
    }

    public String getFindingAidTitle() {
        return findingAidTitle;
    }

    public long getNumberOfResults() {
        return numberOfResults;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getFindingAidNo() {
        return findingAidNo;
    }

    public void setFindingAidNo(String findingAidNo) {
        this.findingAidNo = findingAidNo;
    }

    public String getUnitDate() {
        return unitDate;
    }

    public void setUnitDate(String unitDate) {
        this.unitDate = unitDate;
    }

    public String getScopeContent() {
        return scopeContent;
    }

    public void setScopeContent(String scopeContent) {
        this.scopeContent = scopeContent;
    }

    public int getNumberOfDigitalObjects() {
        return numberOfDigitalObjects;
    }

    public void setNumberOfDigitalObjects(int numberOfDigitalObjects) {
        this.numberOfDigitalObjects = numberOfDigitalObjects;
    }

    public int getNumberOfDigitalObjectsInDescendents() {
        return numberOfDigitalObjectsInDescendents;
    }

    public void setNumberOfDigitalObjectsInDescendents(int numberOfDigitalObjectsInDescendents) {
        this.numberOfDigitalObjectsInDescendents = numberOfDigitalObjectsInDescendents;
    }

    public int getNumberOfDescendents() {
        return numberOfDescendents;
    }

    public void setNumberOfDescendents(int numberOfDescendents) {
        this.numberOfDescendents = numberOfDescendents;
    }
}
