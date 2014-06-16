<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="controlTabContent">
    <table id="controlTable" class="tablePadding">
        <tr>
            <td><label for="apeId"><s:property value="getText('eaccpf.control.idintheapeeaccpf')" /></label></td>
            <s:if test="%{loader.recordId == ''}">
                <td><s:property value="getText('eaccpf.control.valuewillbegenerated')" /></td>
            </s:if>
            <s:else>
                <td><input type="text" id="apeId" name="apeId" readonly="true" value="${loader.recordId}"/></td>
            </s:else>
        </tr>
        <tr>
            <td><label for="responsiblePerson"><s:property value="getText('eaccpf.control.personinstitutionresponsiblefordescription')" /></label></td>
            <td><input type="text" id="responsiblePerson" name="responsiblePerson" readonly="true" value="${loader.controlResponsiblePerson}" /></td>
        </tr>
        <tr>
            <td><label for="responsibleInstitution"><b><s:property value="getText('eaccpf.control.identifierofinstitutionresponsible')" />*</b></label></td>
            <td><input type="text" id="responsibleInstitution" name="responsibleInstitution" readonly="true" value="${loader.agencyCode}" /></td>
        </tr>
    </table>
    <s:if test="%{loader.otherRecordIds.size() > 0}">
        <s:iterator var="current" value="loader.otherRecordIds" status="status">
            <table id="localId_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <td><label for="textLocalId_<s:property value="#status.index + 1" />"><s:property value="getText('eaccpf.control.otherrecordidentifier')" /></label></td>
                    <td><input type="text" id="textLocalId_<s:property value="#status.index + 1" />" name="textLocalId_<s:property value="#status.index + 1" />" value="<s:property value="#current" />"/></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="localId_1" class="tablePadding">
            <tr>
                <td><label for="textLocalId_1"><s:property value="getText('eaccpf.control.otherRecordIdentifier')" /></label></td>
                <td><input type="text" id="textLocalId_1" name="textLocalId_1" /></td>
            </tr>
        </table>
    </s:else>
    <table id="addLocalIdButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.control.addlocalidentifier')" />" id="addLocalIdButton" onClick="addLocalId('<s:property value="getText('eaccpf.control.error.emptyfields')" />');" /></td>
        </tr>
    </table>
    <table id="usedLanguagesAndScripts" class="tablePadding">
        <tr>
            <th colspan="4"><s:property value="getText('eaccpf.control.usedlanguagesandscriptsfordescription')" /></th>
        </tr>
        <tr>
            <td><b><s:property value="getText('eaccpf.commons.select.language')" />*</b></td>
            <td>
                <select id="controlLanguage" name="controlLanguage" >
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key == loader.controlLanguageCode}'>selected="selected"</s:if><s:elseif test='%{#language.key == defaultLanguage}'>selected="selected"</s:elseif>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
            <td><b><s:property value="getText('eaccpf.control.selectascript')" />*</b></td>
            <td>
                <select id="controlScript" name="controlScript" >
                    <s:iterator value="scriptList" var="script">
                        <option value='<s:property value="#script.key"/>' <s:if test='%{#script.key == loader.controlScriptCode}'>selected="selected"</s:if><s:elseif test='%{#script.key == defaultScript}'>selected="selected"</s:elseif>><s:property value="#script.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
    </table>
</div>