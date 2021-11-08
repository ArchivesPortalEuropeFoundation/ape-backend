<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id="yourInstitutionTabContent">
    <table id="yourInstitutionTable_1" class="tablePadding">	
        <tr>
            <td id="yourInstitutionLabel" colspan="4">
                <s:property value="getText('eag2012.tab.yourInstitution')" />
            </td>
        </tr>

        <tr>
            <td>
                <label  for="textYIPersonInstitutionResposibleForTheDescription"><s:property value="getText('eag2012.commons.personResponsible')"/>:</label>
            </td>
            <td colspan="2" class="labelLeft">
                <textarea  id="textYIPersonInstitutionResposibleForTheDescription" onchange="personResponsibleForDescriptionChanged();" >${loader.agent}</textarea>
            </td>
        </tr>

        <tr>
            <td>
                <label for="textYIInstitutionCountryCode"><s:property value="getText('eag2012.commons.countryCode')"/><span class="required">*</span>:</label>
            </td>
            <td>
                <input type="text" id="textYIInstitutionCountryCode" value="${loader.countryCode}" disabled="disabled"/>
            </td>
            <td colspan="2">
            </td>
        </tr>

        <tr>
            <td>
                <label for="textYIIdentifierOfTheInstitution"><s:property value="getText('eag2012.commons.identifierOfTheInstitution')"/><span class="required">*</span>:</label>
            </td>
            <td>
                <textarea id="textYIIdentifierOfTheInstitution" onKeyup="idOfInstitutionChanged('<s:property value="getText('eag2012.yourinstitution.repeatISIL')"/>', '<s:property value="getText('eag2012.errors.errorISIL')"/>');firstIdAndNoISIL();">${loader.otherRepositorId}</textarea>
            </td>
            <td class="labelLeft">
                <label for="textYICodeISL"><s:property value="getText('eag2012.isil.isThisISIL')"/></label>
            </td>
            <td>
                <select id="selectYICodeISIL" onclick="codeISILChanged('<s:property value="getText('eag2012.errors.errorISIL')"/>');">
                    <s:iterator value="yesNoList" var="list">
                        <option value='<s:property value="#list.key"/>' <s:if test='%{newEag && #list.key=="no"}'>selected=selected</s:if><s:if test='%{#list.key == loader.recordIdISIL}'>selected=selected</s:if>><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>

        <s:if test="%{loader.otherRecordId.size() > 0}">
            <s:iterator var="current" value="loader.otherRecordId" status="status">
                <tr>
                    <td>
                        <label for="otherRepositorId_<s:property value="#status.index" />"> <s:property value="getText('eag2012.yourinstitution.futherId')" />:</label>
                    </td>
                    <td>
                        <textarea id="otherRepositorId_<s:property value="#status.index" />" onclick="idOfInstitutionChanged('<s:property value="getText('eag2012.yourinstitution.repeatISIL')"/>', '<s:property value="getText('eag2012.errors.errorISIL')"/>', '<s:property value="#status.index" />');" onkeyup="idOfInstitutionChanged('<s:property value="getText('label.ai.tabs.commons.repeatISIL')"/>', '<s:property value="getText('eag2012.errors.errorISIL')"/>', '<s:property value="#status.index" />');" ><s:property value="#current" /></textarea>
                    </td>
                    <td class="labelLeft">
                        <label for="selectOtherRepositorIdCodeISIL_<s:property value="#status.index" />"> <s:property value="getText('eag2012.isil.isThisISIL')" />:</label>
                    </td>
                    <td>
                        <select id="selectOtherRepositorIdCodeISIL_<s:property value="#status.index" />" onclick="codeISILChanged('<s:property value="getText('eag2012.errors.errorISIL')"/>', '<s:property value="#status.index" />');">
                            <s:iterator value="yesNoList" var="list">
                                <option value='<s:property value="#list.key"/>'
                                        <s:if test='%{#list.key == "no" && #current != loader.recordId}'>selected=selected</s:if>>
                                    <s:property value="#list.value"/>
                                </option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
            </s:iterator>
        </s:if>

        <tr>
            <td colspan="2">
                <input type="button" id="buttonAddFutherIds" value="<s:property value="getText('eag2012.yourinstitution.addOtherIdentifier')" />" onclick="addFurtherIds('<s:property value="getText('eag2012.yourinstitution.futherId')" />', '<s:property value="getText('eag2012.commons.pleaseFillData')" />', '<s:property value="getText('eag2012.isil.isThisISIL')" />', '<s:property value="getText('eag2012.yourinstitution.repeatISIL')" />', '<s:property value="getText('eag2012.errors.errorISIL')" />');" />
            </td>
            <td class="labelLeft">
                <label for="textYIIdUsedInAPE"><s:property value="getText('eag2012.commons.idUsedInApe')"/>:</label>
            </td>
            <td>
                <%-- <input type="hidden" id="selfrecordId" value="${loader.selfRecordId}"/> --%>
                <input type="hidden" id="recordIdHidden" value="${loader.selfRecordId}"/>
                <input type="text" id="textYIIdUsedInAPE" value="<s:if test="%{!newEag}">${loader.recordId}</s:if>" disabled="disabled" />
                </td>
            </tr>
            <tr>
                <td class="orangeLine" colspan="4"></td>
            </tr>
            <tr>
                <td>
                    <label for="textYINameOfTheInstitution"><s:property value="getText('eag2012.commons.nameOfInstitution')"/><span class="required">*</span>:</label>
            </td>
            <td colspan="2">
                <textarea id="textYINameOfTheInstitution">${loader.autform}</textarea>
                <!--  <textarea id="textYINameOfTheInstitution" onchange="nameOfInstitutionChanged('<s:property value="getText('eag2012.commons.errorOnChangeNameOfInstitution')" />', '${loader.initialAutformEscaped}');">${loader.autform}</textarea>-->
            </td>
            <td>
                <label class="language" for="selectYINOTISelectLanguage"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                <select id="selectYINOTISelectLanguage" onchange="nameOfInstitutionLanguageChanged();" >
                    <s:iterator value="languageList" var="language"> 
                        <option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.autformLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
                    </s:iterator>
                </select> 
            </td>
        </tr>
        <tr>
            <td>
                <label for="textYIParallelNameOfTheInstitution"><s:property value="getText('eag2012.commons.alternativeCurrentNameOfInstitution')"/>:</label>
            </td>
            <td colspan="2">
                <textarea id="textYIParallelNameOfTheInstitution" onchange="parallelNameOfInstitutionChanged();">${loader.parform}</textarea>
            </td>
            <td>
                <label class="language" for="selectYIPNOTISelectLanguage"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                <select id="selectYIPNOTISelectLanguage" onchange="parallelNameOfInstitutionLanguageChanged();" >
                    <s:iterator value="languageList" var="language"> 
                        <option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.parformLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <label for="textYISelectParallelNameOfTheInstitution"><s:property value="getText('eag2012.identity.selectType')" />:</label>
            </td>
            <td class="textContact" colspan="2">
                <select id="textYISelectParallelNameOfTheInstitution" size="4" multiple="multiple" class="selectControlTab" onclick="selectTypeOfInstitutionOptionsIntoIdTab()">
                    <s:iterator value="typeOfInstitutionList" var="type"> 
                        <option value="<s:property value="#type.key" />"
                                <s:set var="isSelected" value="false"/>
                                <s:iterator var="current" value="loader.repositoryType" status="status">
                                    <s:if test="#type.key == #current"> selected="selected"</s:if>
                                </s:iterator> ><s:property value="#type.value" /></option>
                    </s:iterator>
                </select>
            </td>
            <td>
            </td>
        </tr>

        <tr>
            <td id="yourInstitutionAddRepositoryLabelTd" colspan="4">
                <s:property value="getText('eag2012.yourinstitution.addRepositoryTabLabel')" />
            </td>
        </tr>
        <tr>
            <td>
                <input type="button" id="buttonAddRepositories" value="<s:property value="getText('eag2012.yourinstitution.addRepositoryTabButton')" />"  onclick="addRepositories('<s:property value="getText('eag2012.yourinstitution.institution')" />', '<s:property value="getText('eag2012.tab.extraRepository')" />', '<s:property value="getText('eag2012.commons.nameOfRepository')" />', '<s:property value="getText('eag2012.commons.roleOfRepository')" />', '<s:property value="getText('eag2012.options.role.headquarters')" />', '<s:property value="getText('eag2012.options.role.branch')" />', '<s:property value="getText('eag2012.options.role.interimArchive')" />', '<s:property value="getText('eag2012.commons.postalAddress')" />', '<s:property value="getText('eag2012.commons.selectLanguage')" />', '<s:property value="getText('eag2012.commons.street')" />', '<s:property value="getText('eag2012.commons.cityTownWithPostalcode')" />', '<s:property value="getText('eag2012.commons.deleteRepository')" />');" />
            </td>
        </tr>
    </table>

    <s:if test="%{loader.yiNumberOfVisitorsAddress.size() > 0}">
        <s:set var="counter" value="0"/>
        <s:iterator var="current" value="loader.yiNumberOfVisitorsAddress" status="status">
            <table id="yiTableVisitorsAddress_<s:property value="%{#status.index + 1}" />" class="tablePadding">
                <tr>
                    <td id="visitorAdressLabel" colspan="4">
                        <s:property value="getText('eag2012.commons.visitorsAddress')" />
                    </td>
                </tr>

                <tr>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="textYIStreet"><s:property value="getText('eag2012.commons.street')"/><span class="required">*</span>:</label>
                        </s:if>
                        <s:else>
                            <label for="textYIStreet"><s:property value="getText('eag2012.commons.street')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2">
                        <textarea id="textYIStreet" onchange="streetOfInstitutionChanged($(this).parent().parent().parent().parent());"><s:property value="loader.yiStreet[#counter]" /></textarea>
                    </td>
                    <td>
                        <label class="language" for="selectYIVASelectLanguage"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        <select id="selectYIVASelectLanguage" onchange="streetOfInstitutionLanguageChanged($(this).parent().parent().parent().parent());" >
                            <s:iterator value="languageList" var="language"> 
                                <option value="<s:property value="#language.key" />"
                                        <s:if test="%{#language.key == loader.yiStreetLang[#counter]}" > selected=selected </s:if>>
                                    <s:property value="#language.value" />
                                </option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="textYICity"><s:property value="getText('eag2012.commons.cityTownWithPostalcode')"/><span class="required">*</span>:</label>
                        </s:if>
                        <s:else>
                            <label for="textYICity"><s:property value="getText('eag2012.commons.cityTownWithPostalcode')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2">
                        <textarea id="textYICity" onchange="cityOfInstitutionChanged($(this).parent().parent().parent().parent());"><s:property value="loader.yiMunicipalityPostalcode[#counter]" /></textarea>
                    </td>
                    <td>
                    </td>
                </tr>

                <tr>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="textYICountry"><s:property value="getText('eag2012.commons.country')"/><span class="required">*</span>:</label>
                        </s:if>
                        <s:else>
                            <label for="textYICountry"><s:property value="getText('eag2012.commons.country')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2">
                        <textarea id="textYICountry" onchange="countryOfInstitutionChanged($(this).parent().parent().parent().parent());"><s:property value="loader.yiCountry[#counter]" /></textarea>
                    </td>
                    <td>
                    </td>
                </tr>

                <tr>
                    <td id="coordinatesLabel" colspan="4">
                        <a href="http://itouchmap.com/latlong.html" target="_blank"><s:property value="getText('eag2012.commons.coordinates')" /></a>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label for="textYILatitude"><s:property value="getText('eag2012.commons.latitude')"/></label>
                    </td>
                    <td>
                        <input type="text" id="textYILatitude" onchange="latitudeOfInstitutionChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiLatitude[#counter]" />" />
                    </td>
                    <td class="labelLeft">
                        <label for="textYILongitude"><s:property value="getText('eag2012.commons.longitude')"/></label>
                    </td>
                    <td>
                        <input type="text" id="textYILongitude" onchange="longitudeOfInstitutionChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiLongitude[#counter]" />" />
                    </td>
                </tr>
            </table>
            <s:set var="counter" value="%{#counter + 1}"/>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="yiTableVisitorsAddress_1" class="tablePadding">
            <tr>
                <td id="visitorAdressLabel" colspan="4">
                    <s:property value="getText('eag2012.commons.visitorsAddress')" />
                </td>
            </tr>

            <tr>
                <td>
                    <label for="textYIStreet"><s:property value="getText('eag2012.commons.street')"/><span class="required">*</span>:</label>
                </td>
                <td colspan="2">
                    <textarea id="textYIStreet" onchange="streetOfInstitutionChanged($(this).parent().parent().parent().parent());"></textarea>	
                </td>
                <td>
                    <label class="language" for="selectYIVASelectLanguage"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                    <select id="selectYIVASelectLanguage" onchange="streetOfInstitutionLanguageChanged($(this).parent().parent().parent().parent());" >
                        <s:iterator value="languageList" var="language"> 
                            <option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>

            <tr>
                <td>
                    <label for="textYICity"><s:property value="getText('eag2012.commons.cityTownWithPostalcode')"/><span class="required">*</span>:</label>
                </td>
                <td colspan="2">
                    <textarea id="textYICity" onchange="cityOfInstitutionChanged($(this).parent().parent().parent().parent());"></textarea>	
                </td>
                <td>
                </td>
            </tr>

            <tr>
                <td>
                    <label for="textYICountry"><s:property value="getText('eag2012.commons.country')"/><span class="required">*</span>:</label>
                </td>
                <td colspan="2">
                    <textarea id="textYICountry" onchange="countryOfInstitutionChanged($(this).parent().parent().parent().parent());">${loader.country}</textarea>
                </td>
                <td>
                </td>
            </tr>

            <tr>
                <td id="coordinatesLabel" colspan="4">
                    <a href="http://itouchmap.com/latlong.html" target="_blank"><s:property value="getText('eag2012.commons.coordinates')" /></a>
                </td>
            </tr>

            <tr>
                <td>
                    <label for="textYILatitude"><s:property value="getText('eag2012.commons.latitude')"/></label>
                </td>
                <td>
                    <input type="text" id="textYILatitude" onchange="latitudeOfInstitutionChanged($(this).parent().parent().parent().parent());" />
                </td>
                <td class="labelLeft">
                    <label for="textYILongitude"><s:property value="getText('eag2012.commons.longitude')"/></label>
                </td>
                <td>
                    <input type="text" id="textYILongitude" onchange="longitudeOfInstitutionChanged($(this).parent().parent().parent().parent());" />
                </td>
            </tr>
        </table>
    </s:else>

    <table id="yiTableButtonAddVisitorsAddress" class="tablePadding">
        <tr>
            <td colspan="2">
                <input type="button" id="buttonAddVisitorsAddressTranslation" value="<s:property value="getText('eag2012.commons.addVisitorTranslationAddress')"/>" onclick="yiAddVisitorsAddressTranslation('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
            </td>
            <td colspan="2">
            </td>
        </tr>
        <tr>
            <td class="orangeLine" colspan="4"></td>
        </tr>
    </table>

    <table id="yiTableOthers" class="tablePadding">
        <tr>
            <td>
                <label for="selectYIContinent" ><s:property value="getText('eag2012.options.continent')"/><span class="required">*</span>:</label>
            </td>
            <td>
                <select id="selectYIContinent" onchange="continentOfInstitutionChanged();" >
                    <s:iterator value="continentOfTheInstitutionList" var="continent"> 
                        <option value="<s:property value="#continent.key" />"<s:if test="%{#continent.key == loader.geogarea}" > selected=selected </s:if>><s:property value="#continent.value" /></option>
                    </s:iterator>
                </select>
            </td>
        </tr>

        <tr>
            <td>
                <label for="textYITelephone" ><s:property value="getText('eag2012.commons.telephone')"/>:</label>
            </td>
            <td>
                <input type="text" id="textYITelephone" onchange="telephoneOfInstitutionChanged($(this));" value="${loader.telephone}" />
            </td>
        </tr>

        <s:if test="%{loader.yiNumberOfEmailAddress.size() > 0}">
            <s:set var="counter" value="0"/>
            <s:iterator var="current" value="loader.yiNumberOfEmailAddress" status="status">
                <s:if test="%{#status.index == 0}">
                    <tr id="trTextYIEmail">
                    </s:if>
                    <s:else>
                    <tr id="trTextYIEmail_<s:property value="%{#status.index + 1}" />">
                    </s:else>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="textYIEmailAddress" ><s:property value="getText('eag2012.commons.email')"/>:</label>
                        </s:if>
                        <s:else>
                            <label for="textYIEmailAddress_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('eag2012.commons.email')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2" class="textContact">
                        <s:if test="%{#status.index == 0}">
                            <textarea id="textYIEmailAddress" onchange="emailOfInstitutionChanged($(this));"><s:property value="loader.yiEmail[#counter]" /></textarea>	
                        </s:if>
                        <s:else>
                            <textarea id="textYIEmailAddress_<s:property value="%{#status.index + 1}" />" onchange="emailOfInstitutionChanged($(this));"><s:property value="loader.yiEmail[#counter]" /></textarea>
                        </s:else>
                    </td>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label class="language" for="selectTextYILangEmail"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:if>
                        <s:else>
                            <label class="language" for="selectTextYILangEmail_<s:property value="%{#status.index + 1}" />"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:else>
                        <s:if test="%{#status.index == 0}">
                            <select id="selectTextYILangEmail" onchange="emailOfInstitutionLangChanged($(this));">
                            </s:if>
                            <s:else>
                                <select id="selectTextYILangEmail_<s:property value="%{#status.index + 1}" />" onchange="emailOfInstitutionLangChanged($(this));">
                                </s:else>
                                <s:iterator value="languageList" var="language"> 
                                    <option value="<s:property value="#language.key" />"
                                            <s:if test="%{#language.key == loader.yiEmailLang[#counter]}" > selected=selected </s:if>>
                                        <s:property value="#language.value" />
                                    </option>
                                </s:iterator>
                            </select>
                    </td>
                </tr>

                <s:if test="%{#status.index == 0}">
                    <tr id="trTextYILangEmail">
                    </s:if>
                    <s:else>
                    <tr id="trTextYILangEmail_<s:property value="%{#status.index + 1}" />">
                    </s:else>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="textYIEmailLinkTitle"><s:property value="getText('eag2012.commons.linkTitle')"/>:</label>
                        </s:if>
                        <s:else>
                            <label for="textYIEmailLinkTitle_<s:property value="%{#status.index + 1}" />"><s:property value="getText('eag2012.commons.linkTitle')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2" class="textContact">
                        <s:if test="%{#status.index == 0}">
                                <!--<input type="text" id="textYIEmailLinkTitle" onchange="emailOfInstitutionLinkChanged($(this));" value="<s:property value="loader.yiEmailTitle[#counter]" />" /> -->
                            <textarea id="textYIEmailLinkTitle" onchange="emailOfInstitutionLinkChanged($(this));"><s:property value="loader.yiEmailTitle[#counter]" /></textarea>

                        </s:if>
                        <s:else>
                            <textarea id="textYIEmailLinkTitle_<s:property value="%{#status.index + 1}" />" onchange="emailOfInstitutionLinkChanged($(this));"><s:property value="loader.yiEmailTitle[#counter]" /></textarea>
                        </s:else>
                    </td>
                    <td>
                    </td>
                </tr>
                <s:set var="counter" value="%{#counter + 1}"/>
            </s:iterator>

            <tr id="trButtonAFEOTInstitution">
                <td colspan="2">
                    <input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("eag2012.commons.addEmail")' />" onclick="addYIFurtherEmailsOfTheInstitution('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
                </td>
                <td colspan="2">
                </td>
            </tr>
            <tr>
                <td class="orangeLine" colspan="4"></td>
            </tr>
        </s:if>
        <s:else>
            <tr id="trTextYIEmail">
                <td>
                    <label for="textYIEmailAddress" ><s:property value="getText('eag2012.commons.email')"/>:</label>
                </td>
                <td colspan="2" class="textContact">
                    <textarea id="textYIEmailAddress" onchange="emailOfInstitutionChanged($(this));"></textarea>
                </td>
                <td>
                    <label class="language" for="selectTextYILangEmail"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                    <select id="selectTextYILangEmail" onchange="emailOfInstitutionLangChanged($(this));">
                        <s:iterator value="languageList" var="language"> 
                            <option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>

            <tr id="trTextYILangEmail">
                <td>
                    <label for="textYIEmailLinkTitle" ><s:property value="getText('eag2012.commons.linkTitle')"/>:</label>
                </td>
                <td colspan="2" class="textContact">
                    <textarea id="textYIEmailLinkTitle" onchange="emailOfInstitutionLinkChanged($(this));"></textarea>
                </td>
                <td></td>
            </tr>

            <tr id="trButtonAFEOTInstitution">
                <td colspan="2">
                    <input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("eag2012.commons.addEmail")' />" onclick="addYIFurtherEmailsOfTheInstitution('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
                </td>
                <td colspan="2">
                </td>
            </tr>
            <tr>
                <td class="orangeLine" colspan="4"></td>
            </tr>
        </s:else>

        <s:if test="%{loader.yiNumberOfWebpageAddress.size() > 0}">
            <s:set var="counter" value="0"/>
            <s:iterator var="current" value="loader.yiNumberOfWebpageAddress" status="status">
                <s:if test="%{#status.index == 0}">
                    <tr id="trButtonYIWebpage">
                    </s:if>
                    <s:else>
                    <tr id="trButtonYIWebpage_<s:property value="%{#status.index + 1}" />">
                    </s:else>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="textYIWebpage" ><s:property value="getText('eag2012.commons.webpage')"/>:</label>
                        </s:if>
                        <s:else>
                            <label for="textYIWebpage_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('eag2012.commons.webpage')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2" class="textContact">
                        <s:if test="%{#status.index == 0}">
                            <textarea id="textYIWebpage" onchange="webOfInstitutionChanged($(this));"><s:property value="loader.yiWebpage[#counter]" /></textarea>
                        </s:if>
                        <s:else>
                            <textarea id="textYIWebpage_<s:property value="%{#status.index + 1}" />" onchange="webOfInstitutionChanged($(this));"><s:property value="loader.yiWebpage[#counter]" /></textarea>
                        </s:else>
                    </td>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label class="language" for="selectTextYILangWebpage"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:if>
                        <s:else>
                            <label class="language" for="selectTextYILangWebpage_<s:property value="%{#status.index + 1}" />"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:else>
                        <s:if test="%{#status.index == 0}">
                            <select id="selectTextYILangWebpage" onchange="webLangOfInstitutionChanged($(this));">
                            </s:if>
                            <s:else>
                                <select id="selectTextYILangWebpage_<s:property value="%{#status.index + 1}" />" onchange="webLangOfInstitutionChanged($(this));">
                                </s:else>
                                <s:iterator value="languageList" var="language"> 
                                    <option value="<s:property value="#language.key" />"
                                            <s:if test="%{#language.key == loader.yiWebpageLang[#counter]}" > selected=selected </s:if>>
                                        <s:property value="#language.value" />
                                    </option>
                                </s:iterator>
                            </select>
                    </td>
                </tr>

                <s:if test="%{#status.index == 0}">
                    <tr id="trButtonYILangWebpage">
                    </s:if>
                    <s:else>
                    <tr id="trButtonYILangWebpage_<s:property value="%{#status.index + 1}" />">
                    </s:else>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="textYIWebpageLinkTitle"><s:property value="getText('eag2012.commons.linkTitle')"/>:</label>
                        </s:if>
                        <s:else>
                            <label for="textYIWebpageLinkTitle_<s:property value="%{#status.index + 1}" />"><s:property value="getText('eag2012.commons.linkTitle')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2" class="textContact">
                        <s:if test="%{#status.index == 0}">
                            <textarea id="textYIWebpageLinkTitle" onchange="webOfInstitutionLinkChanged($(this));"><s:property value="loader.yiWebpageTitle[#counter]" /></textarea>
                        </s:if>
                        <s:else>
                            <textarea id="textYIWebpageLinkTitle_<s:property value="%{#status.index + 1}" />" onchange="webOfInstitutionLinkChanged($(this));"><s:property value="loader.yiWebpageTitle[#counter]" /></textarea>
                        </s:else>
                    </td>
                    <td>
                    </td>
                </tr>
                <s:set var="counter" value="%{#counter + 1}"/>
            </s:iterator>

            <tr id="trButtonAFWOTInstitution">
                <td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
                    <input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("eag2012.commons.addWebpage")' />" onclick="addYIFurtherWebsOfTheInstitution('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
                </td>
            </tr>
            <tr>
                <td class="orangeLine" colspan="4"></td>
            </tr>
        </s:if>
        <s:else>
            <tr id="trButtonYIWebpage">
                <td>
                    <label for="textYIWebpage" ><s:property value="getText('eag2012.commons.webpage')"/>:</label>
                </td>
                <td colspan="2" class="textContact">
                    <textarea id="textYIWebpage" onchange="webOfInstitutionChanged($(this));"></textarea>
                </td>
                <td>
                    <label class="language" for="selectTextYILangWebpage"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                    <select id="selectTextYILangWebpage" onchange="webLangOfInstitutionChanged($(this));">
                        <s:iterator value="languageList" var="language"> 
                            <option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>

            <tr id="trButtonYILangWebpage">
                <td>
                    <label for="textYIWebpageLinkTitle" ><s:property value="getText('eag2012.commons.linkTitle')"/>:</label>
                </td>
                <td colspan="2" class="textContact">
                    <textarea id="textYIWebpageLinkTitle" onchange="webOfInstitutionLinkChanged($(this));"></textarea>
                </td>
            </tr>

            <tr id="trButtonAFWOTInstitution">
                <td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
                    <input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("eag2012.commons.addWebpage")' />" onclick="addYIFurtherWebsOfTheInstitution('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
                </td>
            </tr>
            <tr>
                <td class="orangeLine" colspan="4"></td>
            </tr>
        </s:else>

        <s:if test="%{loader.yiNumberOfOpening.size() > 0}">
            <s:set var="counter" value="0"/>
            <s:iterator var="current" value="loader.yiNumberOfOpening" status="status">
                <s:set var="openingContent" value="loader.yiOpeningContent[#counter]"/>
                <s:set var="openingLang" value="loader.yiOpeningLang[#counter]"/>
                <s:set var="openingHref" value="loader.yiOpeningHref[#counter]"/>
                <s:if test="%{#status.index == 0}">
                    <tr id="trTextYIOpeningTimes">
                    </s:if>
                    <s:else>
                    <tr id="trTextYIOpeningTimes_<s:property value="%{#status.index + 1}" />">
                    </s:else>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="textYIOpeningTimes" ><s:property value="getText('eag2012.commons.openingHours')"/><span class="required">*</span>:</label>
                        </s:if>
                        <s:else>
                            <label for="textYIOpeningTimes_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('eag2012.commons.openingHours')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2" class="textContact">
                        <s:if test="%{#status.index == 0}">
                            <textarea id="textYIOpeningTimes" onchange="openingHoursOfInstitutionChanged($(this));"><s:property value="#openingContent" /></textarea>
                        </s:if>
                        <s:else>
                            <textarea id="textYIOpeningTimes_<s:property value="%{#status.index + 1}" />" onchange="openingHoursOfInstitutionChanged($(this));"><s:property value="#openingContent" /></textarea>
                        </s:else>
                    </td>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label class="language" for="selectTextYIOpeningTimes"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:if>
                        <s:else>
                            <label class="language" for="selectTextYIOpeningTimes_<s:property value="%{#status.index + 1}" />"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:else>
                        <s:if test="%{#status.index == 0}">
                            <select id="selectTextYIOpeningTimes" onchange="duplicateOpeningTimesLanguage($(this));">
                            </s:if>
                            <s:else>
                                <select id="selectTextYIOpeningTimes_<s:property value="%{#status.index + 1}" />" onchange="duplicateOpeningTimesLanguage($(this));">
                                </s:else>
                                <s:iterator value="languageList" var="language"> 
                                    <option value="<s:property value="#language.key" />"
                                            <s:if test="%{#language.key == #openingLang}" > selected=selected </s:if>>
                                        <s:property value="#language.value" />
                                    </option>
                                </s:iterator>
                            </select>
                    </td>
                </tr>
                <s:if test="%{#status.index == 0}">
                    <tr id="trLinkYIOpeningTimes">
                    </s:if>
                    <s:else>
                    <tr id="trLinkYIOpeningTimes_<s:property value="%{#status.index + 1}" />">
                    </s:else>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="linkYIOpeningTimes" ><s:property value="getText('eag2012.commons.openingHours.link')"/>:</label>
                        </s:if>
                        <s:else>
                            <label for="linkYIOpeningTimes_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('eag2012.commons.openingHours.link')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2" class="textContact">
                        <s:if test="%{#status.index == 0}">
                            <textarea id="linkYIOpeningTimes" onchange="openingHoursLinkOfInstitutionChanged($(this));"><s:property value="#openingHref" /></textarea>
                        </s:if>
                        <s:else>
                            <textarea id="linkYIOpeningTimes_<s:property value="%{#status.index + 1}" />" onchange="openingHoursLinkOfInstitutionChanged($(this));"><s:property value="#openingHref" /></textarea>
                        </s:else>
                    </td>
                    <td>

                    </td>
                </tr>
                <s:set var="counter" value="%{#counter + 1}"/>
            </s:iterator>
        </s:if>
        <s:else>
            <tr id="trTextYIOpeningTimes">
                <td>
                    <label for="textYIOpeningTimes" ><s:property value="getText('eag2012.commons.openingHours')"/><span class="required">*</span>:</label>
                </td>
                <td colspan="2" class="textContact">
                    <textarea id="textYIOpeningTimes" onchange="openingHoursOfInstitutionChanged($(this));"></textarea>
                </td>
                <td>
                    <label class="language" for="selectTextYIOpeningTimes"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                    <select id="selectTextYIOpeningTimes" onchange="duplicateOpeningTimesLanguage($(this));" >
                        <s:iterator value="languageList" var="language"> 
                            <option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.openingLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr id="trLinkYIOpeningTimes">
                <td>
                    <label for="linkYIOpeningTimes" ><s:property value="getText('eag2012.commons.openingHours.link')"/>:</label>
                </td>
                <td colspan="2" class="textContact">
                    <textarea id="linkYIOpeningTimes" onchange="openingHoursLinkOfInstitutionChanged($(this));"><s:property value="#openingHref" /></textarea>
                </td>
                <td>

                </td>
            </tr>
        </s:else>

        <tr>
            <td id="tdASAddOpeningTimes" colspan="2">
                <input type="button" id="buttonASAddOpeningTimes"  value="<s:property value='getText("eag2012.commons.addOpeningHours")' />" onclick="yIAddOpeningTimes('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
            </td>
        </tr>
        <tr>
            <td class="orangeLine" colspan="4"></td>
        </tr>
        <s:if test="%{loader.yiRestaccess.size() > 0}">
            <tr>
                <td>
                    <label for="selectAccessibleToThePublic" ><s:property value="getText('eag2012.commons.accessiblePublic')"/><span class="required">*</span>:</label>
                </td>
                <td>
                    <select id="selectAccessibleToThePublic" onchange="accessibleToThePublicChanged();" >
                        <s:iterator value="yesNoList" var="yesno"> 
                            <option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == loader.accessQuestion}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
                        </s:iterator>
                    </select>
                </td>
                <td colspan="2">
                </td>
            </tr>

            <s:set var="counter" value="0"/>
            <s:iterator var="current" value="loader.yiRestaccess" status="status">
                <s:if test="%{#status.index == 0}">
                    <tr id="trYIButtonFutherAccessInformation">
                    </s:if>
                    <s:else>
                    <tr id="trYIButtonFutherAccessInformation_<s:property value="%{#status.index + 1}" />">
                    </s:else>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="futherAccessInformation" ><s:property value="getText('eag2012.accessAndServices.accessRestrictions')"/>:</label>
                        </s:if>
                        <s:else>
                            <label for="futherAccessInformation_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('eag2012.accessAndServices.accessRestrictions')"/>:</label>
                        </s:else>
                    </td>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <textarea id="futherAccessInformation" onchange="futherAccessInformationChanged($(this));"><s:property value="#current" /></textarea>
                        </s:if>
                        <s:else>
                            <textarea id="futherAccessInformation_<s:property value="%{#status.index + 1}" />" onchange="futherAccessInformationChanged($(this));"><s:property value="#current" /></textarea>
                        </s:else>
                    </td>
                    <td class="labelLeft">
                        <s:if test="%{#status.index == 0}">
                            <label for="selectFutherAccessInformation"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:if>
                        <s:else>
                            <label for="selectFutherAccessInformation_<s:property value="%{#status.index + 1}" />"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:else>
                    </td>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <select id="selectFutherAccessInformation" onchange="duplicateAccessInformation($(this));" >
                            </s:if>
                            <s:else>
                                <select id="selectFutherAccessInformation_<s:property value="%{#status.index + 1}" />" onchange="duplicateAccessInformation($(this));" >
                                </s:else>
                                <s:iterator value="languageList" var="language"> 
                                    <option value="<s:property value="#language.key" />"
                                            <s:if test="%{#language.key == loader.yiRestaccessLang[#counter]}" > selected=selected </s:if>>
                                        <s:property value="#language.value" />
                                    </option>
                                </s:iterator>
                            </select>
                    </td>
                </tr>
                <s:set var="counter" value="%{#counter + 1}"/>
            </s:iterator>

            <tr>
                <td colspan="2">
                </td>
                <td colspan="2">
                    <input type="button" id="buttonFutherAccessInformation2" value="<s:property value="getText('eag2012.commons.addFutherAccessInformation')"/>" onclick="yiFutherAccessInformation2('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
                </td>
            </tr>
            <tr>
                <td class="orangeLine" colspan="4"></td>
            </tr>
        </s:if>
        <s:else>
            <tr>
                <td>
                    <label for="selectAccessibleToThePublic" ><s:property value="getText('eag2012.commons.accessiblePublic')"/><span class="required">*</span>:</label>
                </td>
                <td>
                    <select id="selectAccessibleToThePublic" onchange="accessibleToThePublicChanged();" >
                        <s:iterator value="yesNoList" var="yesno"> 
                            <option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == loader.accessQuestion}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
                        </s:iterator>
                    </select>
                </td>
                <td colspan="2" class="buttonAddInformation">
                    <input type="button" id="buttonFutherAccessInformation" value="<s:property value="getText('eag2012.commons.addFutherAccessInformation')"/>" onclick="yiFutherAccessInformation();" />
                </td>
            </tr>
            <tr id="orangeLineFurtherAccessInformation">
                <td class="orangeLine" colspan="4"></td>
            </tr>
            <tr id="trYIButtonFutherAccessInformation" style="display:none;">
                <td>
                    <label for="futherAccessInformation"><s:property value="getText('eag2012.accessAndServices.accessRestrictions')" />:</label>
                </td>
                <td colspan="2">
                    <textarea id="futherAccessInformation" onchange="futherAccessInformationChanged($(this));"></textarea>
                </td>
                <td class="labelLeft">
                    <label class="language" for="selectFutherAccessInformation"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                    <select id="selectFutherAccessInformation" onchange="duplicateAccessInformation($(this));">
                        <s:iterator value="languageList" var="language">
                            <option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>

            <tr>
                <td colspan="2">
                </td>
                <td colspan="2" class="labelLeft">
                    <input type="button" id="buttonFutherAccessInformation2" style="display:none;" value="<s:property value="getText('eag2012.commons.addFutherAccessInformation')"/>" onclick="yiFutherAccessInformation2('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
                </td>
            </tr>
            <tr id="orangeLineFutherAccessInformation2" style="display:none;">
                <td class="orangeLine" colspan="4"></td>
            </tr>
        </s:else>

        <s:if test="%{loader.yiAccessibility.size() > 0}">
            <tr>
                <td>
                    <label for="selectFacilitiesForDisabledPeopleAvailable" ><s:property value="getText('eag2012.commons.disabledAccess')"/><span class="required">*</span>:</label>
                </td>
                <td>
                    <select id="selectFacilitiesForDisabledPeopleAvailable" onchange="facilitiesForDisabledPeopleAvailableChanged();" >
                        <s:iterator value="yesNoList" var="yesno"> 
                            <option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == loader.yiAccessibilityQuestion[0]}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
                        </s:iterator>
                    </select>
                </td>
                <td colspan="2">
                </td>
            </tr>

            <s:set var="counter" value="0"/>
            <s:iterator var="current" value="loader.yiAccessibility" status="status">
                <s:if test="%{#status.index == 0}">
                    <tr id="trButtonAddFutherInformationOnExistingFacilities">
                    </s:if>
                    <s:else>
                    <tr id="trButtonAddFutherInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />">
                    </s:else>
                    <td>
                        <s:if test="%{#status.index == 0}">
                            <label for="futherInformationOnExistingFacilities" ><s:property value="getText('eag2012.commons.disabledAccess.facilities')"/>:</label>
                        </s:if>
                        <s:else>
                            <label for="futherInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('eag2012.commons.disabledAccess.facilities')"/>:</label>
                        </s:else>
                    </td>
                    <td colspan="2">
                        <s:if test="%{#status.index == 0}">
                            <textarea id="futherInformationOnExistingFacilities" onchange="futherInformationOnExistingFacilitiesChanged($(this));"><s:property value="#current" /></textarea>
                        </s:if>
                        <s:else>
                            <textarea id="futherInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />" onchange="futherInformationOnExistingFacilitiesChanged($(this));"><s:property value="#current" /></textarea>
                        </s:else>
                    </td>
                    <td class="labelLeft">
                        <s:if test="%{#status.index == 0}">
                            <label class="language" for="selectFutherAccessInformationOnExistingFacilities"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:if>
                        <s:else>
                            <label class="language" for="selectFutherAccessInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                        </s:else>
                        <s:if test="%{#status.index == 0}">
                            <select id="selectFutherAccessInformationOnExistingFacilities" onchange="duplicateFutherAccessInformationOnExistingFacilitiesLanguage($(this));" >
                            </s:if>
                            <s:else>
                                <select id="selectFutherAccessInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />" onchange="duplicateFutherAccessInformationOnExistingFacilitiesLanguage($(this));">
                                </s:else>
                                <s:iterator value="languageList" var="language"> 
                                    <option value="<s:property value="#language.key" />"
                                            <s:if test="%{#language.key == loader.yiAccessibilityLang[#counter]}" > selected=selected </s:if>>
                                        <s:property value="#language.value" />
                                    </option>
                                </s:iterator>
                            </select>
                    </td>
                </tr>
                <s:set var="counter" value="%{#counter + 1}"/>
            </s:iterator>

            <tr>
                <td colspan="2">
                </td>
                <td colspan="2" class="labelLeft">
                    <input type="button" id="buttonAddFutherInformationOnExistingFacilities2" value="<s:property value="getText('eag2012.yourinstitution.addInfoOnExistingFacilities')"/>" onclick="yiAddFutherInformationOnExistingFacilities2('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
                </td>
            </tr>
        </s:if>
        <s:else>
            <tr>
                <td>
                    <label for="selectFacilitiesForDisabledPeopleAvailable" ><s:property value="getText('eag2012.commons.disabledAccess')"/><span class="required">*</span>:</label>
                </td>
                <td>
                    <select id="selectFacilitiesForDisabledPeopleAvailable" onchange="facilitiesForDisabledPeopleAvailableChanged();" >
                        <s:iterator value="yesNoList" var="yesno"> 
                            <option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == loader.accessibilityQuestion}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
                        </s:iterator>
                    </select>
                </td>
                <td colspan="2" class="buttonAddInformation">
                    <input type="button" id="buttonAddFutherInformationOnExistingFacilities" value="<s:property value="getText('eag2012.yourinstitution.addInfoOnExistingFacilities')"/>" onclick="yiAddFutherInformationOnExistingFacilities();" />
                </td>
            </tr>

            <tr id="trButtonAddFutherInformationOnExistingFacilities" style="display:none;">
                <td>
                    <label for="futherInformationOnExistingFacilities"><s:property value="getText('eag2012.commons.disabledAccess.facilities')" />:</label>
                </td>
                <td colspan="2">
                    <textarea id="futherInformationOnExistingFacilities" onchange="futherInformationOnExistingFacilitiesChanged($(this));"></textarea>		
                </td>
                <td class="labelLeft">
                    <label class="language" for="selectFutherAccessInformationOnExistingFacilities"><s:property value="getText('eag2012.commons.selectLanguage')"/>:</label>
                    <select id="selectFutherAccessInformationOnExistingFacilities" onchange="duplicateFutherAccessInformationOnExistingFacilitiesLanguage($(this));">
                        <s:iterator value="languageList" var="language"> 
                            <option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>

            <tr>
                <td colspan="2"> </td>
                <td colspan="2" class="labelLeft">
                    <input type="button" id="buttonAddFutherInformationOnExistingFacilities2" style="display:none;" value="<s:property value="getText('eag2012.yourinstitution.addInfoOnExistingFacilities')"/>" onclick="yiAddFutherInformationOnExistingFacilities2('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
                </td>
            </tr>
        </s:else>

        <tr id="orangeLineFutherAccessInformation2">
            <td class="orangeLine" colspan="4"></td>
        </tr>
        <tr>
            <td>
                <label>
                    <s:property value="getText('eag2012.commons.shareEagWithWikimedia')"/><span class="required">*</span>:<br/>(<a href="javascript:void(null);" onclick="shareEagWithWikimediaReadMore();"><s:property value="getText('eag2012.commons.shareEagWithWikimedia.readmore')"/></a>)
                </label>
            </td>
            <td colspan="3">
                <input type="radio" ${loader.shareEagWithWikimedia && loader.shareEagWithWikimediaLicence!=null ?"checked":""} name="shareEagWithWikimedia" value="always" onclick="shareEagWithWikimediaCheckedClick()">Yes</input>
                <input style="margin-left: 10px" type="radio" ${!loader.shareEagWithWikimedia && loader.shareEagWithWikimediaLicence!=null?"checked":""} name="shareEagWithWikimedia" value="never" onclick="shareEagWithWikimediaCheckedClick()">No</input>
                <br/><span id="shareEagWithWikimediaError"></span>
<%--                <br/>--%>
                <table id="shareEagWithWikimediaLicenceTable" style="display: ${loader.shareEagWithWikimediaLicence==null ?"none":"block"}">
                    <s:select id="shareEagWithWikimediaLicence" name="shareEagWithWikimediaLicences" list="rightsList" listKey="value" listValue="content" value="loader.shareEagWithWikimediaLicence.getId()"/>
                </table>

                <%--                Text for the pop-up dialog--%>
                <div id="shareEagWithWikimediaDialog" title="<s:property value="getText('eag2012.commons.shareEagWithWikimedia')"/>" style="display: none">
                    <p><s:property value="getText('eag2012.commons.shareEagWithWikimedia.dialog.part1')"/> <a target="_blank" href="https://creativecommons.org/publicdomain/zero/1.0/" style="color: #C65400;">Creative Commons CC0 Public Domain Dedication.</a></p>
                    <br/><p><s:property value="getText('eag2012.commons.shareEagWithWikimedia.dialog.part2')"/></p>
                </div>

                <div id="shareWikimediaHiddenError" style="display: none"><s:property value="getText('eag2012.errors.errorsharewikimedia')"/></div>
            </td>
        </tr>

        <tr>
            <td colspan="3">
            </td>
            <td id="tdButtonsYourInstitutionTab">
                <input type="button" id="buttonYourInstitutionTabNext" value="<s:property value='getText("eag2012.commons.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.errors.fieldRequired')" />', '<s:property value="getText('eag2012.commons.pleaseFillMandatoryFields')" />', '<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>');" />
            </td>
        </tr>
    </table>
</div>
