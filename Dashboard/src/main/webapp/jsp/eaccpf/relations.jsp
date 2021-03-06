<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="relationsTabContent">
    <h2 class="tablePadding"><s:property value="getText('eaccpf.relations.cpf.section')" /></h2>
    <s:if test="%{loader.cpfRelations.size() > 0}">
        <s:iterator var="current" value="loader.cpfRelations" status="status">
            <table id="cpfRelationsTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.cpf.relation')" /></th>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.cpf.relation.name')" />:</label></td>
                    <td><input type="text" id="textCpfRelationName" name="textCpfRelationName_<s:property value="#status.index + 1" />" value="<s:property value="#current.relationName" />" /></td>
                    <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
                    <td>
                        <select id="cpfRelationLanguage" name="cpfRelationLanguage_<s:property value="#status.index + 1" />" >
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key==#current.language}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.cpf.relation.identifier')" />:</label></td>
                    <td><input type="text" id="textCpfRelationId" name="textCpfRelationId_<s:property value="#status.index + 1" />" value="<s:property value="#current.id" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
                    <td><input type="text" id="textCpfRelationLink" name="textCpfRelationLink_<s:property value="#status.index + 1" />" value="<s:property value="#current.link" />" /></td>
                    <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />:*</b></label></td>
                    <td><select id="cpfRelationType" name="cpfRelationType_<s:property value="#status.index + 1" />">
                            <s:iterator value="cpfRelationTypeList" var="relationType">
                                <option value='<s:property value="#relationType.key"/>' <s:if test='%{#relationType.key==#current.relationType}'>selected="selected"</s:if>><s:property value="#relationType.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="4"><s:property value="getText('eaccpf.relations.cpf.relation.organisation')" /></td>
                </tr>
                <s:if test="%{#current.agencyNames.size() > 0}">
                    <s:iterator var="currentName" value="#current.agencyNames" status="status2">
                        <s:iterator var="currentId" value="#current.agencyCodes" status="status3">
                            <s:if test="%{#status2.index == #status3.index}">
                                <tr id="trCpfRelationRespOrg_<s:property value="#status2.index + 1" />">
                                    <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                                    <td><input type="text" id="textCpfRelRespOrgPerson" name="cpfRelationsTable_<s:property value="#status.index + 1" />_textCpfRelRespOrgPerson_<s:property value="#status2.index + 1" />" value="<s:property value="#currentName" />" /></td>
                                    <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                                    <td><input type="text" id="textCpfRelRespOrgId" name="cpfRelationsTable_<s:property value="#status.index + 1" />_textCpfRelRespOrgId_<s:property value="#status3.index + 1" />"  value="<s:property value="#currentId" />"/></td>
                                </tr>
                            </s:if>
                        </s:iterator>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr id="trCpfRelationRespOrg_1">
                        <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                        <td><input type="text" id="textCpfRelRespOrgPerson" name="cpfRelationsTable_1_textCpfRelRespOrgPerson_1" /></td>
                        <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                        <td><input type="text" id="textCpfRelRespOrgId" name="cpfRelationsTable_1_textCpfRelRespOrgId_1" /></td>
                    </tr>
                </s:else>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
                    <td colspan="3"><textarea id="textareaCpfRelationDescription" name="textareaCpfRelationDescription_<s:property value="#status.index + 1" />" style="height: 16px; width: 200px;"><s:property value="#current.description" /></textarea></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="cpfRelationsTable_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.cpf.relation')" /></th>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.cpf.relation.name')" />:</label></td>
                <td><input type="text" id="textCpfRelationName" name="textCpfRelationName_1" /></td>
                <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
                <td>
                    <select id="cpfRelationLanguage" name="cpfRelationLanguage_1" onchange="">
                        <s:iterator value="languages" var="language">
                            <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.cpf.relation.identifier')" />:</label></td>
                <td><input type="text" id="textCpfRelationId" name="textCpfRelationId_1" /></td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
                <td><input type="text" id="textCpfRelationLink" name="textCpfRelationLink_1" /></td>
                <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />*</b>:</label></td>
                <td><select id="cpfRelationType" name="cpfRelationType_1" onchange="">
                        <s:iterator value="cpfRelationTypeList" var="relationType">
                            <option value='<s:property value="#relationType.key"/>'><s:property value="#relationType.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="4"><s:property value="getText('eaccpf.relations.cpf.relation.organisation')" /></td>
            </tr>
            <tr id="trCpfRelationRespOrg_1">
                <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                <td><input type="text" id="textCpfRelRespOrgPerson" name="cpfRelationsTable_1_textCpfRelRespOrgPerson_1" /></td>
                <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                <td><input type="text" id="textCpfRelRespOrgId" name="cpfRelationsTable_1_textCpfRelRespOrgId_1" /></td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
                <td colspan="3"><textarea id="textareaCpfRelationDescription" name="textareaCpfRelationDescription_1" style="height: 16px; width: 200px;"></textarea></td>
            </tr>
        </table>
    </s:else>
    <table class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.relations.add.further.cpf')" />" id="addCpfRelationButton" onClick="addCpfRelation('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.relations.error.popup')" />');" /></td>
        </tr>
    </table>
    <h2 class="tablePadding"><s:property value="getText('eaccpf.relations.resources.section')" /></h2>
    <s:if test="%{loader.resRelations.size() > 0}">
        <s:iterator var="current" value="loader.resRelations" status="status">
            <table id="resRelationsTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.resources.relation')" /></th>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.resource.relation.name')" />:</label></td>
                    <td><input type="text" id="textResRelationName" name="textResRelationName_<s:property value="#status.index + 1" />" value="<s:property value="#current.relationName" />" /></td>
                    <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
                    <td>
                        <select id="resRelationLanguage" name="resRelationLanguage_<s:property value="#status.index + 1" />">
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key==#current.language}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.resource.relation.identifier')" />:</label></td>
                    <td><input type="text" id="textResRelationId" name="textResRelationId_<s:property value="#status.index + 1" />" value="<s:property value="#current.id" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
                    <td><input type="text" id="textResRelationLink" name="textResRelationLink_<s:property value="#status.index + 1" />" value="<s:property value="#current.link" />"  /></td>
                    <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />:*</b></label></td>
                    <td><select id="resRelationType" name="resRelationType_<s:property value="#status.index + 1" />">
                            <s:iterator value="resRelationTypeList" var="relationType">
                                <option value='<s:property value="#relationType.key"/>' <s:if test='%{#relationType.key==#current.relationType}'>selected="selected"</s:if>><s:property value="#relationType.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="4"><s:property value="getText('eaccpf.relations.resource.relation.organisation')" /></td>
                </tr>
                <s:if test="%{#current.agencyNames.size() > 0}">
                    <s:iterator var="currentName" value="#current.agencyNames" status="status2">
                        <s:iterator var="currentId" value="#current.agencyCodes" status="status3">
                            <s:if test="%{#status2.index == #status3.index}">
                                <tr id="trResRelationRespOrg_<s:property value="#status2.index + 1" />">
                                    <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                                    <td><input type="text" id="textResRelRespOrgPerson" name="resRelationsTable_<s:property value="#status.index + 1" />_textResRelRespOrgPerson_<s:property value="#status2.index + 1" />" value="<s:property value="#currentName" />" /></td>
                                    <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                                    <td><input type="text" id="textResRelRespOrgId" name="resRelationsTable_<s:property value="#status.index + 1" />_textResRelRespOrgId_<s:property value="#status3.index + 1" />"  value="<s:property value="#currentId" />"/></td>
                                </tr>
                            </s:if>
                        </s:iterator>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr id="trResRelationRespOrg_1">
                        <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                        <td><input type="text" id="textResRelRespOrgPerson" name="resRelationsTable_1_textResRelRespOrgPerson_1" /></td>
                        <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                        <td><input type="text" id="textResRelRespOrgId" name="resRelationsTable_1_textResRelRespOrgId_1" /></td>
                    </tr>
                </s:else>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
                    <td colspan="3"><textarea id="textareaResRelationDescription" name="textareaResRelationDescription_<s:property value="#status.index + 1" />" style="height: 16px; width: 200px;"><s:property value="#current.description" /></textarea></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="resRelationsTable_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.resources.relation')" /></th>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.resource.relation.name')" />:</label></td>
                <td><input type="text" id="textResRelationName" name="textResRelationName_1" /></td>
                <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
                <td>
                    <select id="resRelationLanguage" name="resRelationLanguage_1" onchange="">
                        <s:iterator value="languages" var="language">
                            <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.resource.relation.identifier')" />:</label></td>
                <td><input type="text" id="textResRelationId" name="textResRelationId_1" /></td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
                <td><input type="text" id="textResRelationLink" name="textResRelationLink_1" /></td>
                <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />*</b>:</label></td>
                <td><select id="resRelationType" name="resRelationType_1" onchange="">
                        <s:iterator value="resRelationTypeList" var="relationType">
                            <option value='<s:property value="#relationType.key"/>'><s:property value="#relationType.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="4"><s:property value="getText('eaccpf.relations.resource.relation.organisation')" /></td>
            </tr>
            <tr id="trResRelationRespOrg_1">
                <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                <td><input type="text" id="textResRelRespOrgPerson" name="resRelationsTable_1_textResRelRespOrgPerson_1" /></td>
                <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                <td><input type="text" id="textResRelRespOrgId" name="resRelationsTable_1_textResRelRespOrgId_1" /></td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
                <td colspan="3"><textarea id="textareaResRelationDescription" name="textareaResRelationDescription_1" style="height: 16px; width: 200px;"></textarea></td>
            </tr>
        </table>
    </s:else>
    <table class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.relations.add.further.resource')" />" id="addResRelationButton" onClick="addResRelation('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.relations.error.popup')" />');" /></td>
        </tr>
    </table>
    <h2 class="tablePadding"><s:property value="getText('eaccpf.relations.functions.section')" /></h2>
    <s:if test="%{loader.fncRelations.size() > 0}">
        <s:iterator var="current" value="loader.fncRelations" status="status">
            <table id="fncRelationsTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.functions.relation')" /></th>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.function.relation.name')" />:</label></td>
                    <td><input type="text" id="textFncRelationName" name="textFncRelationName_<s:property value="#status.index + 1" />" value="<s:property value="#current.relationName" />" /></td>
                    <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
                    <td>
                        <select id="fncRelationLanguage" name="fncRelationLanguage_<s:property value="#status.index + 1" />">
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key==#current.language}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.function.relation.identifier')" />:</label></td>
                    <td><input type="text" id="textFncRelationId" name="textFncRelationId_<s:property value="#status.index + 1" />" value="<s:property value="#current.id" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
                    <td><input type="text" id="textFncRelationLink" name="textFncRelationLink_<s:property value="#status.index + 1" />" value="<s:property value="#current.link" />"  /></td>
                    <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />:*</b></label></td>
                    <td><select id="fncRelationType" name="fncRelationType_<s:property value="#status.index + 1" />">
                            <s:iterator value="fncRelationTypeList" var="relationType">
                                <option value='<s:property value="#relationType.key"/>' <s:if test='%{#relationType.key==#current.relationType}'>selected="selected"</s:if>><s:property value="#relationType.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="4"><s:property value="getText('eaccpf.relations.function.relation.organisation')" /></td>
                </tr>
                <s:if test="%{#current.agencyNames.size() > 0}">
                    <s:iterator var="currentName" value="#current.agencyNames" status="status2">
                        <s:iterator var="currentId" value="#current.agencyCodes" status="status3">
                            <s:if test="%{#status2.index == #status3.index}">
                                <tr id="trFncRelationRespOrg_<s:property value="#status2.index + 1" />">
                                    <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                                    <td><input type="text" id="textFncRelRespOrgPerson" name="fncRelationsTable_<s:property value="#status.index + 1" />_textFncRelRespOrgPerson_<s:property value="#status2.index + 1" />" value="<s:property value="#currentName" />" /></td>
                                    <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                                    <td><input type="text" id="textFncRelRespOrgId" name="fncRelationsTable_<s:property value="#status.index + 1" />_textFncRelRespOrgId_<s:property value="#status3.index + 1" />"  value="<s:property value="#currentId" />"/></td>
                                </tr>
                            </s:if>
                        </s:iterator>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr id="trFncRelationRespOrg_1">
                        <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                        <td><input type="text" id="textFncRelRespOrgPerson" name="fncRelationsTable_1_textFncRelRespOrgPerson_1" /></td>
                        <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                        <td><input type="text" id="textFncRelRespOrgId" name="fncRelationsTable_1_textFncRelRespOrgId_1" /></td>
                    </tr>
                </s:else>
                <tr>
                    <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
                    <td colspan="3"><textarea id="textareaFncRelationDescription" name="textareaFncRelationDescription_<s:property value="#status.index + 1" />" style="height: 16px; width: 200px;"><s:property value="#current.description" /></textarea></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="fncRelationsTable_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.functions.relation')" /></th>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.function.relation.name')" />:</label></td>
                <td><input type="text" id="textFncRelationName" name="textFncRelationName_1" /></td>
                <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
                <td>
                    <select id="fncRelationLanguage" name="fncRelationLanguage_1" onchange="">
                        <s:iterator value="languages" var="language">
                            <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.function.relation.identifier')" />:</label></td>
                <td><input type="text" id="textFncRelationId" name="textFncRelationId_1" /></td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
                <td><input type="text" id="textFncRelationLink" name="textFncRelationLink_1" /></td>
                <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />*</b>:</label></td>
                <td><select id="fncRelationType" name="fncRelationType_1" onchange="">
                        <s:iterator value="fncRelationTypeList" var="relationType">
                            <option value='<s:property value="#relationType.key"/>'><s:property value="#relationType.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="4"><s:property value="getText('eaccpf.relations.function.relation.organisation')" /></td>
            </tr>
            <tr id="trFncRelationRespOrg_1">
                <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
                <td><input type="text" id="textFncRelRespOrgPerson" name="fncRelationsTable_1_textFncRelRespOrgPerson_1" /></td>
                <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
                <td><input type="text" id="textFncRelRespOrgId" name="fncRelationsTable_1_textFncRelRespOrgId_1" /></td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
                <td colspan="3"><textarea id="textareaFncRelationDescription" name="textareaFncRelationDescription_1" style="height: 16px; width: 200px;"></textarea></td>
            </tr>
        </table>
    </s:else>
    <table class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.relations.add.further.function')" />" id="addFncRelationButton" onClick="addFncRelation('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.relations.error.popup')" />');" /></td>
        </tr>
    </table>
</div>
