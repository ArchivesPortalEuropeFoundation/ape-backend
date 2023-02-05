<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<dashboard:securityContext var="securityContext" />
         <div >
			<c:if test="${securityContext.countryManager}">
				  <span class="subTitle"><s:property value="getText('al.menu.header')"/> <c:out value="${securityContext.localizedCountryName }"></c:out> </span>
			</c:if>
                  <ul>
				<c:if test="${securityContext.countryManager}">
				     <li><p><br/></p></li>
                     <li><br/><span style="font : bold 100% sans-serif;color : #333;text-align : left"><s:property value="getText('al.menu.manageal')" /> </span><p><br/></p></li>                     
                     <li><s:a action="ALEditor"> -  <s:property value="getText('al.menu.edital')" /></s:a></li>
                     <%--<li><s:a action="editArchivalLandscape"> -  <s:property value="getText('al.menu.edital')" /> (beta)</s:a></li>--%>
                     <li><s:a action="ALUploadForm"> -   <s:property value="getText('al.menu.uploadal')" /></s:a></li>
                     <li><a title="<s:property value="getText('al.menu.viewal')"/>" href="viewAL.action"> -  <s:text name="al.menu.viewal" /></a>
	    			 </li>
                     <li><s:a action="ALDownload" target="_blank"> -   <s:property value="getText('al.menu.downloadal')" /></s:a></li>    
                              
                     <li><s:a action="changeAlIdentifiers"> -   <s:property value="getText('al.menu.changeAlIdentifiers')" /></s:a></li>
                    <li><a href ="manageTopicMappingsCountryManager.action"><s:text name="dashboard.menu.topic.mappings" /></a></li>
				</c:if>
                     <li><br/><span style="font : bold 100% sans-serif;color : #333;text-align : left;"><s:a action="GoDashboard"><s:property value="getText('al.menu.managecontent')"/></s:a></span><br/></li>
                     
                <c:if test="${securityContext.countryManager}">   
                     <li><s:a action="institutionManagerManagement"> - <s:text name="usermanagement.institution.title"/> </s:a></li>
                     <li><s:a action="contactFormsCCManagement"> - <s:text name="al.menu.contactfomrs.cc"/> </s:a></li>
                     <li><s:a action="downloadInstitutionsStatistics"> -   <s:text name="al.menu.statistics.institutions" /></s:a></li>      
                    </c:if>

                 </ul>
                 <div style="width:100%; clear:both;padding:10px;"> </div>
             </div>


