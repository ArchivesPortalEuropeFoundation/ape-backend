<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div >
    <ul>
        <li><p><br/></p></li>
        <li><br/><span style="font : bold 100% sans-serif;color : #333;text-align : left;"><a href="editWebFormRightsInformation.action"><s:property value="getText('dashboard.menu.manageDataSharing')" /></a></span><p><br/></p></li>
        <li><br/><span style="font : bold 100% sans-serif;color : #333;text-align : left;"><s:property value="getText('dashboard.menu.contentproviderinf')" /></span><p><br/></p></li>
        <li><a href="createeag2012withmenu.action"><s:property value="getText('dashboard.menu.editeag')" /></a></li>
        <li><a href="uploadowneagwithmenu.action"><s:property value="getText('dashboard.menu.uploadeag')" /></a></li>
        <li><a href="downloadeag.action?ai_id=<s:property value="%{ai_id}"/>" target="_blank"><s:property value="getText('dashboard.menu.downloadeag')" /></a></li>
        <li><a href ="enableOpenData.action"><s:text name="dashboard.menu.topic.enableapi" /></a></li>
        <li><a href="changeainame.action"><s:property value="getText('dashboard.menu.changeainame')" /></a></li>
        <li><a href="displaySetFeedbackEmailaddress.action"><s:property value="getText('dashboard.menu.ai.setfeedbackemail')" /></a></li>
        <li><br/><span style="font : bold 100% sans-serif;color : #333;text-align : left;"><s:property value="getText('dashboard.menu.contentinf')" /></span><p><br/></p></li>
        <li><a href ="ingestionprofiles.action"><s:property value="getText('dashboard.menu.editIngestionprofiles')" /></a></li>
        <li><a href ="automaticharvestingcreation.action"><s:property value="getText('dashboard.menu.automaticharvestingcreation')" /></a></li>
        <li><a href ="upload.action"><s:property value="getText('dashboard.menu.uploadcontent')" /></a></li>
        <li><a href ="indexEacCpf.action"><s:property value="getText('dashboard.menu.createNewEacCpf')" /></a></li>
        <li><a href ="checkfilesuploaded.action"><s:property value="getText('dashboard.menu.contentmanager')" /></a></li>
        <li><a href ="manageTopicMappings.action"><s:text name="dashboard.menu.topic.mappings" /></a></li>
    </ul>
</div>
