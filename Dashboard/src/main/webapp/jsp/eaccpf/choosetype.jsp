<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eaccpf/eaccpf.css" type="text/css"/>


<div id="eacCpfDiv">
    <h1><s:property value="getText('eaccpf.start.title')" /></h1>
    <s:form id="webformChooseMode" name="webformChooseMode" method="POST" enctype="multipart/form-data" action="editEacCpf.action" theme="simple">
        <div id="container" class="container">
            <div id="headerContainer">
            </div>

            <table id="choosetypeTable" class="tablePadding">
                <tr id="newType">
                    <td><b><s:property value="getText('eaccpf.start.createInstance')" />*</b></td>
                    <td><s:radio id="new" name="cpfType" list="cpfTypeList" listKey="key" listValue="value" value="defaultCpfType"/></td>
                </tr>
                <tr>
                    <td colspan="2"><s:property value="getText('eaccpf.start.text')" /></td>
                </tr>
                <tr id="newLanguage">
                    <td><s:property value="getText('eaccpf.start.language')" />&nbsp;&nbsp;&nbsp;<s:select id="new" name="defaultLanguage" list="languages" listKey="key" listValue="value" /></td>
                    <td><s:property value="getText('eaccpf.start.script')" />&nbsp;&nbsp;&nbsp;<s:select id="new" name="defaultScript" list="scriptList" listKey="key" listValue="value" /></td>
                </tr>
            </table>
        </div>
        <table id="goButtonPanel">
            <tr>
                <td></td>
                <td></td>
                <td><input type="button" class="rightButton" id="buttonGo" value="<s:property value="getText('eaccpf.start.go')" />" onclick='clickGoAction()' /></td>
                <td><input type="button" id="buttonExit" value="<s:property value="getText('eaccpf.commons.exit')" />" onclick='clickExitActionStartPage();' /></td>
            </tr>
        </table>
        <input type="hidden" id="currentTr" value="" />
    </s:form>
</div>