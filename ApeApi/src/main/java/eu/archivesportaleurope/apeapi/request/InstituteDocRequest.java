/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mahbub
 */
@XmlRootElement
@ApiModel
public class InstituteDocRequest extends PageRequest {

    @ApiModelProperty(value = "Institute's ID", example = " ")
    @NotNull(message = "Institute's ID can not be null")
    String instituteId;

    @ApiModelProperty(value = "Document type", example = "fa")
    @NotNull(message = "Document type can not be null")
    String docType;

    @ApiModelProperty(value = "Sorting result with fields and sorting type")
    SortRequest sortRequest;

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public SortRequest getSortRequest() {
        return sortRequest;
    }

    public void setSortRequest(SortRequest sortRequest) {
        this.sortRequest = sortRequest;
    }

}
