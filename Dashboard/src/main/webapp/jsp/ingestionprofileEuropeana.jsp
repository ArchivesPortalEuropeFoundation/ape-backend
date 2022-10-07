<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ingestionprofile.css" type="text/css"/>

<script type='text/javascript'>
    $(function() {
        $('#licensecreativecommons').click(function() {
            $('#hiddenCreativeCommonsLicense').show();
            $('#hiddenEuropeanaLicense').hide();
        });

        $('#licensecc0').click(function() {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').hide();
        });

        $('#licensecpdm').click(function() {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').hide();
        });

        $('#licenseeuropeana').click(function() {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').show();
        });

    });

    function changeInheritFileParentCheckState() {
        if ($("#inheritFileParentCheck").attr('checked')) {
            enableInheritFileParentCheckState();
        } else {
            disableInheritFileParentCheckState();
        }
    }

    function enableInheritFileParentCheckState() {
        $("input[id^='inheritFileParent']").each(function() {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritFileParentCheckState() {
        $("input[id^='inheritFileParent']").each(function() {
            if ($(this).attr("id") != "inheritFileParentCheck") {
                $(this).attr('disabled', 'disabled');
                if ($(this).val() == "false") {
                    $(this).attr('checked', 'checked');
                } else {
                    $(this).removeAttr('checked');
                }
            }
        });
    }

    function changeInheritOriginationCheckState() {
        if ($("#inheritOriginationCheck").attr('checked')) {
            enableInheritOriginationCheckState();
        } else {
            disableInheritOriginationCheckState();
        }
    }

    function enableInheritOriginationCheckState() {
        $("input[name^='inheritOrigination']").each(function() {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritOriginationCheckState() {
        $("input[name^='inheritOrigination']").each(function() {
            if ($(this).attr("name") != "inheritOriginationCheck") {
                $(this).attr('disabled', 'disabled');
                if ($(this).val() == "false") {
                    $(this).attr('checked', 'checked');
                } else {
                    $(this).removeAttr('checked');
                }
            }
        });
    }

    function enableInheritUnittitleCheckState() {
        $("input[name^='inheritUnittitle']").each(function() {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritUnittitleCheckState() {
        $("input[name^='inheritUnittitle']").each(function() {
            if ($(this).attr("name") != "inheritUnittitleCheck") {
                $(this).attr('disabled', 'disabled');
                if ($(this).val() == "no") {
                    $(this).attr('checked', 'checked');
                } else {
                    $(this).removeAttr('checked');
                }
            }
        });
    }
    
    function changeLanguageOfDescriptionState() {
        if ($("#languageDescriptionSameAsMaterialCheck").attr('checked')) {
            disableLanguageOfDescription();
        } else {
            enableLanguageOfDescription();
        }
    }

    function enableLanguageOfDescription() {
        $("tr#trLanguageOfTheDescription").show();
    }

    function disableLanguageOfDescription() {
        $("tr#trLanguageOfTheDescription td.tdVertical select[name^='languageSelectionDescription']").val("");
        $("tr#trLanguageOfTheDescription td.tdVertical input[name^='languageDescriptionCheck']").removeAttr('checked');
        $("tr#trLanguageOfTheDescription td.tdVertical input[name^='__checkbox_languageDescriptionCheck']").removeAttr('checked');
        $("tr#trLanguageOfTheDescription").hide();
    }
</script>

<div id="headerContainer">
</div>

<div id="europeanaTabContent">
    <table id="conversionToEDMForm">
        <tr>
            <td colspan="2" class="titleLabel">
                <s:label key="ead2ese.label.general.settings" />
            </td>
        </tr>
        <tr>
            <td class="inputLabel">
                <s:label key="ead2ese.label.specify.idsource"  for="sourceOfIdentifiers" />:
            </td>
            <td>
                <s:iterator value="sourceOfIdentifiersSet">
                    <s:radio name="sourceOfIdentifiers" id="sourceOfIdentifiers" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
        <tr>
            <td class="inputLabel">
                <s:label key="ead2ese.label.specify.sourceOfFondsTitle"  for="sourceOfFondsTitle" />:
            </td>
            <td>
                <s:iterator value="sourceOfFondsTitleSet">
                    <s:radio name="sourceOfFondsTitle" id="sourceOfFondsTitle" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
        <tr>
            <td class="inputLabel">
                <s:label key="ead2ese.label.inherit.unittitle" for="inheritUnittitle" />:
            </td>
            <td>
                <s:iterator value="yesNoSet">
                    <s:radio name="inheritUnittitle" id="inheritUnittitle" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="titleLabel">
                <s:label key="ead2ese.label.specific.settings" />
            </td>
        </tr>
        <tr id="trDataProvider">
            <td class="inputLabel"><s:label key="ead2ese.label.dataprovider" for="textDataProvider" />*:</td>
            <td>
                <s:textfield id="textDataProvider" name="textDataProvider" maxLength="100"/>
                <s:checkbox name="dataProviderCheck" id="dataProviderCheck" />
                <s:label key="ead2ese.content.dataprovider.file" for="dataProviderCheck"/>
            </td>
        </tr>
        <tr id="trSelectType">
            <td class="inputLabel"><s:label key="ead2ese.label.type" for="edmDaoType" />*:</td>
            <td><s:select id="edmDaoType" name="europeanaDaoType" list="typeSet" listKey="value" listValue="content" required="true" />
                <s:checkbox name="europeanaDaoTypeCheck" id="daoTypeCheck" />
                <s:label key="ead2ese.label.type.file" for="europeanaDaoType"/>
            </td>
        </tr>
        <tr id="trLanguageOfTheMaterial">
            <td class="inputLabel">
                <s:label key="ead2ese.label.language.material" for="languageSelectionMaterial" /><span class="required">*</span>:
            </td>
            <td class="tdVertical">
                <s:select name="languageSelectionMaterial" id="languageSelectionMaterial" listKey="value" listValue="content" list="languages" required="true"
                          multiple="true" size="4"></s:select>

                <s:checkbox name="languageMaterialCheck" id="languageMaterialCheck" />
                <s:label key="ead2ese.label.language.file" for="languageMaterialCheck"/>
                <s:fielderror fieldName="languageSelectionMaterial"/>
                <s:fielderror fieldName="languageMaterialCheck"/>
            </td>
        </tr>

        <tr>
            <td class="inputLabel">
                <s:label key="ead2ese.label.language.description" for="languageSelectionDescription" /><span class="required">*</span>:
            </td>
            <td>
                <s:checkbox name="languageDescriptionSameAsMaterialCheck" id="languageDescriptionSameAsMaterialCheck" onchange="changeLanguageOfDescriptionState();" />
                <s:label key="ead2ese.label.language.descriptionSameAsMaterial" for="languageDescriptionSameAsMaterialCheck"/>
                <s:fielderror fieldName="languageDescriptionSameAsMaterialCheck"/>
            </td>
        </tr>
        <s:if test="languageDescriptionSameAsMaterialCheck=='true'">
            <c:set var="languageDescriptionInvisible" value="style=\"display: none;\""></c:set>
        </s:if>
        <tr id="trLanguageOfTheDescription" ${languageDescriptionInvisible}>
            <td></td>
            <td class="tdVertical">
                <s:select name="languageSelectionDescription" id="languageSelectionDescription" listKey="value" listValue="content" list="languages" required="true"
                          multiple="false" size="4" ></s:select>
                <s:checkbox name="languageDescriptionCheck" id="languageDescriptionCheck" />
                <s:label key="ead2ese.label.language.file" for="languageDescriptionCheck"/>
                <s:fielderror fieldName="languageSelectionDescription"/>
                <s:fielderror fieldName="languageDescriptionCheck"/>
            </td>
        </tr>

        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.license" for="license" /><span class="required">*</span>:</td>
            <td>
                <s:if test="licenseCheck==true">
                    <s:checkbox name="licenseCheck" id="licenseCheck" value="true"></s:checkbox>
                </s:if>
                <s:else>
                    <s:checkbox name="licenseCheck" id="licenseCheck" value="false"></s:checkbox>
                </s:else>
                <s:label key="ead2ese.label.license.file" for="licenseCheck"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <s:iterator value="licenseSet">
                    <s:radio name="license" id="license" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
                (<s:label key="ead2ese.content.license.moreinfo"/> <s:a target="_blank" href="https://pro.europeana.eu/page/available-rights-statements" ><s:property value="getText('ead2ese.content.license.link')" /></s:a>)
                </td>
            </tr>
        <s:if test="license=='cc0' || license=='cpdm' || license=='europeana'">
            <c:set var="creativeCommonsInvisible" value="style=\"display: none;\""></c:set>
        </s:if>
        <s:if test="license=='creativecommons' || license=='cc0' || license=='cpdm'">
            <c:set var="europeanaInvisible" value="style=\"display: none;\""></c:set>
        </s:if>
        <tr id="hiddenEuropeanaLicense"  ${europeanaInvisible}>
            <td class="inputLabel"><s:label key="ead2ese.label.license.europeana" for="europeanaLicense" />*:</td>
            <td><s:select id="europeanaLicense" name="europeanaLicense" list="europeanaLicenseSet" listKey="value" listValue="content"/><s:fielderror fieldName="europeanaLicense"/>
            </td>
        </tr>
        <tr id="hiddenCreativeCommonsLicense" ${creativeCommonsInvisible}>
            <td></td>
            <td>
                <div id="cc_js_widget_container">
                    <script type="text/javascript" src="https://api.creativecommons.org/jswidget/tags/0.97/complete.js?locale=en_US&amp;want_a_license=definitely"></script>
                </div>
            </td>
        </tr>
        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.license.additional" for="licenseAdditionalInformation" />:</td>
            <td><s:textarea id="licenseAdditionalInformation" name="licenseAdditionalInformation" />
            </td>
        </tr>
    </table>
</div>
