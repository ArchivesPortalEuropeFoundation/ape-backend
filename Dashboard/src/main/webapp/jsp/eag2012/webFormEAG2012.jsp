<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eag2012/eag2012.css" type="text/css"/>

<div id="eag2012Div">
	<div class="hidden">
		<div id="processingInfoDiv">
			<table id="processingInfoTable">
				<tr>
					<td>
						<img id="processingInfoImg" src="images/colorbox/loading.gif" />
					</td>
					<td>
						<label class="bold" id="processingInfoLabel" for="processingInfoImg">
							<s:text name="al.message.processing" />
						</label>
					</td>
				</tr>
			</table>
		</div>
	</div>	
	<script type="text/javascript">
		createColorboxForProcessing();
	</script>			
	<div id="validationEAG2012ErrorsDiv">
		<s:actionmessage id="validationEAG2012Errors" />
	</div>
	<form id="webformeag2012" name="webformeag2012" method="POST" action="storeEAG2012.action">
		<input type="hidden" id="saveOrExit" name="saveOrExit" value="save">
		<div id="eag2012Tabs" class="corner-all helper-clearfix">
			<a id="repositories"></a>
			<div id="eag2012tabs_institution" style="float:left;width:100%;">
				<ul id="eag2012tabs_institution_tabs"></ul>
			</div>
			<ul id="eag2012TabsContainer">
				<li id="tab-yourInstitution">
					<a href="#tab-yourInstitution"><s:property value="getText('eag2012.tab.yourInstitution')" /></a> 
				</li>
				<li id="tab-identity">
					<a href="#tab-identity"><s:property value="getText('eag2012.tab.identity')" /></a>
				</li>
				<li id="tab-contact">
					<a href="#tab-contact"><s:property value="getText('eag2012.tab.contact')" /></a>
				</li>
				<li id="tab-accessAndServices">
					<a href="#tab-accessAndServices"><s:property value="getText('eag2012.tab.accessAndServices')" /></a>
				</li>
				<li id="tab-description">
					<a href="#tab-description"><s:property value="getText('eag2012.tab.description')" /></a>
				</li>
				<li id="tab-control">
					<a href="#tab-control"><s:property value="getText('eag2012.tab.control')" /></a>
				</li>
				<li id="tab-relations">
					<a href="#tab-relations"><s:property value="getText('eag2012.tab.relations')" /></a>
				</li>
			</ul>
			<input type="hidden" id="currentTab" value="" />
			<script type="text/javascript">
				$(document).ready(function(){
					hideAndShow("tab-","tab-yourInstitution");
					copyCountryName();
					$("#currentTab").attr("value","tab-yourInstitution");
					$("a[href^='#tab-']").click(function(){
							hideAndShow("tab-",$(this).attr("href").substring(1));
					});
					//clone contact, "access and services" and description
					var numberOfRepos = '<s:property value="loader.numberOfRepositories" />';
					if (numberOfRepos == 0) {
						$("div#divTempContainter").append($("table#contactTable").clone());
					} else {
						$("div#divTempContainter").append($("table#contactTable"));
					}
					$("div#divTempContainter").append($("table#accessAndServicesTable").clone());
					$("div#divTempContainter").append($("table#descriptionTable").clone());
					//update new table names
					if (numberOfRepos == 0) {
						$("div#contactTabContent table#contactTable").attr("id","contactTable_1");
						$("div#contactTabContent table#contactTable_1").show();
						$("div#contactTabContent").show();
						$("div#accessAndServicesTabContent table#accessAndServicesTable").attr("id","accessAndServicesTable_1");
						$("div#accessAndServicesTabContent table#accessAndServicesTable_1").show();
						$("div#accessAndServicesTabContent").show();
						$("div#descriptionTabContent table#descriptionTable").attr("id","descriptionTable_1");
						$("div#descriptionTabContent table#descriptionTable_1").show();
						$("div#descriptionTabContent").show();
					}

					// Load repository tabs if necessary.
					var labelInstitution = '<s:property value="getText('eag2012.yourinstitution.institution')" />';
					var labelRepository = '<s:property value="getText('eag2012.tab.extraRepository')" />';
					loadRepositories(labelInstitution, labelRepository, numberOfRepos);
					
					//postal address - your institution tab and contact tab
					var postalField1 = "<s:property value="loader.streetPostal"/>";
					var postalField2 = "<s:property value="loader.municipalityPostalcodePostal"/>";
					var postalField3 = "<s:property value="loader.streetPostalLang"/>";

					if(postalField1.length>0 || postalField2.length>0 || postalField3.length>0){
						var control=true;
						contactAddPostalAddressIfDifferent('<s:property value="getText('eag2012.commons.postalAddress')" />','<s:property value="getText('eag2012.commons.selectLanguage')"/>','<s:property value="getText('eag2012.commons.street')"/>','<s:property value="getText('eag2012.commons.cityTownWithPostalcode')"/>', '<s:property value="getText('eag2012.commons.pleaseFillDataAddress')" />', control );
						$("#textContactPAStreet").attr("value",postalField1);
						$("#textContactPACity").attr("value",postalField2);
						$("#selectContactLanguagePostalAddress").attr("value",postalField3);
					}
					var furtherAccessInformation = "<s:property value="loader.restaccess"/>";
					var furtherAccessInformationLang = "<s:property value="loader.restaccessLang"/>";
					if(furtherAccessInformation.length>0 || furtherAccessInformationLang.length>0){
						yiFutherAccessInformation();
					}
					var accessibilityInformation = "<s:property value="loader.accessibility"/>";
					var accessibilityInformationLang = "<s:property value="loader.accessibilityLang"/>";
					if(accessibilityInformation.length>0 || accessibilityInformationLang.length>0){
						yiAddFutherInformationOnExistingFacilities();
					}
					// Set editables code ISIL selects.
					var text = '<s:property value="getText('eag2012.errors.errorISIL')" />';
					loadDisableSelectsForFurtheIds(text);
					//last checks for urls
					$("table#yiTableOthers input[id^='textReferencetoyourinstitutionsholdingsguide']").each(function(){
						var message = "<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>";
						checkWebpages($(this),message);
					});
					checkWebpages($("table#yiTableOthers input#textYIWebpage"),"<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>");
					$("table#yiTableOthers input[id^='textYIWebpage_']").each(function(){
						var message = "<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>";
						checkWebpages($(this),message);
					});
					checkWebpages($("table#yiTableOthers input#linkYIOpeningTimes"),"<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>");
					$("table#yiTableOthers input[id^='linkYIOpeningTimes_']").each(function(){
						var message = "<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>";
						checkWebpages($(this),message);
					});
					$("table#resourceRelationTable_1 input[id^='textWebsiteOfResource']").each(function(){
						var message = "<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>";
						checkWebpages($(this),message);
					});
					var  textSpecialCharacters = '<s:property value="getText('error.specialCharacters')" escape="false" />';
			//		$("textarea#textYINameOfTheInstitution").on("input", function() {
			//			checkName(textSpecialCharacters,  $(this));
			//		});
					checkAutforms(textSpecialCharacters);
				//	$("table[id^='identityTableNameOfTheInstitution_']").each(function(){
				//		var id = $(this).attr("id");
				//		$("table#" + id + " textarea#textNameOfTheInstitution").on("input", function(){
				//			checkName(textSpecialCharacters, $(this));
				//		});
				//	});

					deleteColorboxForProcessing();
				});
			</script>
			<div id="container">
			  <div id="tab-yourInstitution">
					<jsp:include page="yourInstitutionTab.jsp" />
			  </div>

			  <div id="tab-identity">
					<jsp:include page="identityTab.jsp" />
			  </div>

			  <div id="tab-contact">
					<jsp:include page="contactTab.jsp" />
			  </div>

			  <div id="tab-accessAndServices">
					<jsp:include page="accessAndServicesTab.jsp" />
			  </div>

			  <div id="tab-description">
					<jsp:include page="descriptionTab.jsp" />
			  </div>

			  <div id="tab-control">
					<jsp:include page="controlTab.jsp" />
			  </div>

			  <div id="tab-relations">
					<jsp:include page="relationsTab.jsp" />
			  </div>
			</div>
		</div>
		<table>
			<tr>
				<td>
		  			<input type="button" id="buttonSaveEAG2012" value="<s:property value='getText("eag2012.commons.save")' />" class="rightButton" 
		  				onclick="clickSaveAction(this.form, '<s:property value="getText('eag2012.errors.fieldRequired')" />', '<s:property value="getText('eag2012.commons.success')" />','<s:property value="getText('eag2012.errors.errorYourInstitution')" />', '<s:property value="getText('eag2012.errors.errorIdentity')" />', '<s:property value="getText('eag2012.errors.errorContact')" />', '<s:property value="getText('eag2012.errors.errorAccessAndServices')" />', '<s:property value="getText('eag2012.errors.errorDesription')" />', '<s:property value="getText('eag2012.errors.errorControl')" />', '<s:property value="getText('eag2012.errors.errorRelations')" />', '<s:property value="getText('eag2012.commons.errorOnChangeNameOfInstitution')" />','<s:property value="getText('eag2012.errors.errorDeleteRepository')"/>', '<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>', '${loader.initialAutformEscaped}', false, '<s:property value="getText('eag2012.errors.errorspecialCharacters')" escape="false" />')"/>
	    			<input type="button" id="buttonExitEAG2012" value="<s:property value='getText("eag2012.commons.exit")' />"
			  			onclick="clickExitAction();" />
	    		</td>
	    	</tr>
	    </table>
	</form>
	<div id="divTempContainter" style="display:none;"></div>
</div>

<div id="dialog-saveOnQuit">
    <div>
        <p id="DlgContent"><s:property value="getText('eaccpf.commons.exitConfirm')" /></p>
        <input id="btnYes" type="button" value="<s:property value="getText('content.message.yes')" />" 
        	onclick="clickSaveAction(this.form, '<s:property value="getText('eag2012.errors.fieldRequired')" />', '<s:property value="getText('eag2012.commons.success')" />','<s:property value="getText('eag2012.errors.errorYourInstitution')" />', '<s:property value="getText('eag2012.errors.errorIdentity')" />', '<s:property value="getText('eag2012.errors.errorContact')" />', '<s:property value="getText('eag2012.errors.errorAccessAndServices')" />', '<s:property value="getText('eag2012.errors.errorDesription')" />', '<s:property value="getText('eag2012.errors.errorControl')" />', '<s:property value="getText('eag2012.errors.errorRelations')" />', '<s:property value="getText('eag2012.commons.errorOnChangeNameOfInstitution')" />','<s:property value="getText('eag2012.errors.errorDeleteRepository')"/>', '<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>', '${loader.initialAutformEscaped}', true, '<s:property value="getText('eag2012.errors.errorspecialCharacters')" escape="false" />');" />
        <input id="btnNo" type="button" value="<s:property value="getText('content.message.no')" />" 
        	onclick="clickExitWithoutSaveAction();" />
    </div>
</div>
