<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div >
    <%-- <span class="texto"><s:property value="getText('menu.functionalities')" /></span> --%> 
    <ul>
        <li><p><br/></p></li>
        <li><br/><span style="font : bold 100% sans-serif;color : #333;text-align : left;"><a href="editWebFormRightsInformation.action"><s:property value="getText('dashboard.menu.manageDataSharing')" /></a></span><p><br/></p></li>
        <li><br/><span style="font : bold 100% sans-serif;color : #333;text-align : left;"><s:property value="getText('dashboard.menu.contentproviderinf')" /></span><p><br/></p></li>
        <li><a href="createeag2012withmenu.action"> - <s:property value="getText('eag.menu.creation')" /></a></li>
        <li><a href="uploadowneag.action"> - <s:property value="getText('dashboard.menu.uploadeag')" /></a></li>
        <!-- <li><br/><a href ="index.action" ><s:property value="getText('dashboard.menu.backtoal')" /></a><p><br/></p></li> -->
    </ul>
</div>