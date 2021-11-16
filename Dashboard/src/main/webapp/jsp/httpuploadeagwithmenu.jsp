<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div >
	<s:actionmessage/>
	<s:form cssStyle="margin-top: 10px" id="uploadFormWithWikimedia" method="post" enctype= "multipart/form-data">
		<s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
		<s:hidden name="action:eaghttpuploadwithmenu" value="Upload" />
		<div id="shareEagWithWikimediaLicenceHiddenDiv"></div>
		<s:file id="httpFile" name="httpFile" label="Select file to upload"/>
		<table id="shareEagWithWikimediaError_tbl">
			<tr>
				<td>
					<p id="shareEagWithWikimediaError_required" style="display: none" class="fieldRequired">
						<s:property value="getText('label.uploadfile')"/>
					</p>
				</td>
			</tr>
		</table>


		<div style="text-align: center">
			<s:if test="%{shouldShowLicencePopup}">
				<s:submit theme="simple" id="uploadBtn2" data-popup="true" key="label.upload"/>
			</s:if>
			<s:else>
				<s:submit theme="simple" id="uploadBtn2" data-popup="false" key="label.upload"/>
			</s:else>
			<s:submit theme="simple" key="label.cancel" action="dashboardHome"/>
		</div>

		<table id="shareEagWithWikimediaDialog2" style="display: none; text-align: center">
			<tr>
				<td style="text-align: left">
					<p><s:property value="getText('eag2012.commons.shareEagWithWikimedia.dialog.part1')"/> <a target="_blank" href="https://creativecommons.org/publicdomain/zero/1.0/" style="color: #C65400;">Creative Commons CC0 Public Domain Dedication.</a></p>
				</td>
			</tr>
			<tr>
				<td style="text-align: left; padding-top: 10px">
					<s:property value="getText('eag2012.commons.shareEagWithWikimedia.dialog.part2b1')"/>
					${shareEagWithWikimediaRightsInformation.getRightsName()}.
					<s:property value="getText('eag2012.commons.shareEagWithWikimedia.dialog.part2b2')"/>.
				</td>
			</tr>
			<tr>
				<td style="padding-top: 10px">
					<table id="shareEagWithWikimediaLicenceTable">
						<s:checkbox label="Creative Commons CC0 Public Domain Dedication" id="shouldShareEagWithCC0" name="shouldShareEagWithCC0" value="aBoolean" fieldValue="true"/>
					</table>
				</td>
			</tr>
		</table>
	</s:form>
</div>
