<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>

<c:set var="countryId"><s:property value="countryId"/></c:set>
<div >  
	<h1>Contact forms settings</h1>
	<br/><br/>
	<s:form id="contact" method="POST">
		<div>
		<s:if test="userCM.isContactFormsAsCC()">
			<s:checkbox name="acceptcc" key="label.contactformsascm" checked="checked" />
		</s:if>
		<s:else>
			<s:checkbox name="acceptcc" key="label.contactformsascm" />
		</s:else>
		</div>

		<table style="margin-top: 20px">
			<tr><td>
				<div style="font-style: italic;">
					<p><s:text name="description.contactformsascm" /></p>
				</div>
			</td>
			</tr>
		</table>

		<table style="margin-top: 20px">
			<tr>
				<td><s:submit action="contactFormsSaveSetting" method="changeSetting" key="label.contactformsascm.send" theme="simple"  cssClass="mainButton" /></td>
			</tr>
		</table>
	</s:form>
	<br/>
	<c:if test="${success}">
		<div style="color: green; font-weight: bold"><s:text name="label.contactformsascm.success" /></div>
	</c:if>
</div>

