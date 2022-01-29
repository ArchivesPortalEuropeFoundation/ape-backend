<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="headerContainer">
</div>

<div id="basicTabContent">
    <table>
        <tr>
            <td><s:label key="ingestionprofiles.defaultActionUpload" /></td>
            <td><s:select id="uploadedFileAction" name="uploadedFileAction" list="uploadedFileActions" listKey="value" listValue="content" /></td>
        </tr>
        <tr>
            <td><s:label key="ingestionprofiles.defaultActionExisting" /></td>
            <td><s:select id="existingFileAction" name="existingFileAction" list="existingFileActions" listKey="value" listValue="content" /></td>
        </tr>
        <tr>
            <td><s:label key="ingestionprofiles.defaultActionNoEadid" /></td>
            <td><s:select id="noEadidAction" name="noEadidAction" list="noEadidActions" listKey="value" listValue="content" /></td>
        </tr>
        <tr id="defaultDaoType">
            <td><s:label key="ingestionprofiles.defaultDao" /></td>
            <td>
                <s:select id="daoType" name="daoType" list="daoTypes" listKey="value" listValue="content" />
                <s:checkbox id="daoTypeCheck" name="daoTypeCheck" />
                <s:label key="ead2ese.label.type.file" for="daoType"/>
            </td>
        </tr>
        <%--<c:if test="${associatedFiletype == 4}">--%>

        <tr id="extractEacObject">
            <td><s:label for="extractEac" key="content.message.ead3.eac.extract"/></td>
            <td><s:checkbox id="extractEac" name="extractEacFromEad3"/></td>
        </tr>
        <%--</c:if>--%>
        
            <c:if test="${!empty xslFiles}">
                <tr id="xslConversion">
                    <td><s:label key="ingestionprofiles.defaultXslFile" /></td>
                    <td><s:select id="defaultXslFile" name="defaultXslFile" list="xslFiles" listKey="value" listValue="content" /></td>
                </tr>
            </c:if>

        <tr id="trRightForDigitalObject">
            <td id="tdLabelRightForDigitalObject">
                <s:label key="content.message.default.rights.digital.objects" for="rightDigitalObjects"/>
            </td>
            <td id="tdSelectRightForDigitalObject">
                <s:select id="rightDigitalObjects" name="rightDigitalObjects" list="rightsDigitalObjects" listKey="value" listValue="content" onchange="deleteMessage($(this));" />
            </td>
        </tr>
        <tr id="trDescriptionRightForDigitalObject">
            <td id="tdLabelDescriptionRightForDigitalObject" class="optionsSubLabels">
                <s:label key="dashboard.hgcreation.label.description" for="descriptionRightForDigitalObject" />
            </td>
            <td id="tdTextDescriptionRightForDigitalObject">
                <s:textarea id="rightDigitalDescription" name="rightDigitalDescription" />
            </td>
        </tr>
        <tr id="trHolderRightForDigitalObject">
            <td id="tdLabelHolderRightForDigitalObject" class="optionsSubLabels">
                <s:label key="content.message.rights.holder" for="textHolderRightForDigitalObject" />
            </td>
            <td id="tdTextHolderRightForDigitalObject">
                <s:textfield id="rightDigitalHolder" name="rightDigitalHolder" cssClass="longInputText" />
            </td>
        </tr>
        <tr id="trRightForEADData">
            <td id="tdLabelRightForEADData">
                <s:label key="content.message.default.rights.ead.data" for="rightEadData"/>
            </td>
            <td id="tdSelectRightForEADData">
                <s:select id="rightEadData" name="rightEadData" list="rightsEadData" listKey="value" listValue="content" onchange="deleteMessage($(this));" />
            </td>
        </tr>
        <tr id="trDescriptionRightForEADData">
            <td id="tdLabelDescriptionRightForEADData" class="optionsSubLabels">
                <s:label key="dashboard.hgcreation.label.description" for="descriptionRightForEADData" />
            </td>
            <td id="tdTextDescriptionRightForEADData">
                <s:textarea id="rightEadDescription" name="rightEadDescription" />
            </td>
        </tr>
        <tr id="trHolderRightForEADData">
            <td id="tdLabelHolderRightForEADData" class="optionsSubLabels">
                <s:label key="content.message.rights.holder" for="textHolderRightForEADData" />
            </td>
            <td id="tdTextHolderRightForEADData">
                <s:textfield id="rightEadHolder" name="rightEadHolder" cssClass="longInputText" />
            </td>
        </tr>
    </table>
</div>
