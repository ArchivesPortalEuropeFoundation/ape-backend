<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:if test="searchableItems">
    <div style="text-align:left; font-size:12pt;">
        <p><span style="font-weight:bold; background-color:red; color:white;">ATTENTION:</span> The change that you are about to make can only be done while no content is published. Please change to the Content manager and unpublish all your data first. You will then be able to make the change to your licence statement and will have to publish your data again, once the change has been applied.</p><br/>
    </div>
</s:if>
<s:hidden id="currentRightsSelection" name="currentRightsSelection"/>
<s:hidden id="ccOrPdmLicence" name="ccOrPdmLicence"/>
<div>
    <s:if test="newInstitution">
    <p>This is your first time to enter the Dashboard. It is mandatory to confirm the licence under which you intend to publish your institution's metadata on Archives Portal Europe.</p><br/>
    <p>If you intend to apply the <a href="http://creativecommons.org/licenses/by-sa/4.0/" target="_blank">Creative Commons Attribution, ShareAlike licence</a> according to the Archives Portal Europe's Metadata Exchange Agreement, please leave the selection below as is and confirm by clicking the "Save" button. Alternatively, you are free to choose any of the other licences available via the drop-down menu.</p><br/>
    </s:if>
    <p>The licence you choose here will be applied to the description of your institution and to all descriptions of archival material as well as all descriptions of its context (e.g. records creators) by default. If required and suitable, you can apply differing licences on the level of each finding aid or other file when using a profile for your upload preferences. Such profiles can be created via your account once these first steps have been finalised.</p><br/>
    <p>Please note that, if you choose to make your metadata available under the terms of one of the <a href="https://creativecommons.org/licenses/" target="_blank">Creative Commons licences</a>, this choice cannot be revoked. I.e. once published under a specific licence, you should only change this in case you decide to waive all your rights and apply the <a href="https://creativecommons.org/publicdomain/zero/1.0/" target="_blank">Creative Commons CC0 Public Domain Dedication</a> or in case your metadata becomes part of the public domain and you would want to apply the <a href="https://creativecommons.org/publicdomain/mark/1.0/" target="_blank">Public Domain Mark</a>.</p><br/>
    <p>If no alternative licence is chosen here, the <a href="http://creativecommons.org/licenses/by-sa/4.0/" target="_blank">Creative Commons Attribution, ShareAlike licence</a> will be applied according to the Archives Portal Europe's Metadata Exchange Agreement.</p>
    <br><br>
    <s:form method="POST" theme="simple" action="saveRightsDeclaration" >
        <table style="width:600px">
            <colgroup>
                <col style="width:33%">
                <col style="width:67%">
            </colgroup>
            <tbody>
                <tr>
                    <td><s:label for="rights" key="label.rightsinfo.defaultstatement"/></td>
                    <td><s:select id="rights" name="rights" list="rightsList" listKey="value" listValue="content" onchange="updateRightsText()"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><s:label key="label.rightsinfo.description"/></td>
                    <td><s:label id="description" name="description" key="%{description}"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><s:label key="label.rightsinfo.rightsholder"/></td>
                    <td><s:textfield name="rightsHolder" value="%{rightsHolder}"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>&nbsp;</td>
                </tr>
            </tbody>
        </table>
        <br/>
        <table id="saveButtonPanel">
            <tr style="text-align:right;">
                <td>
                    <s:if test="!searchableItems"><s:submit id="submitRightsDeclaration" key="label.rightsinfo.save"/></s:if>
                    <s:submit id="cancelRightsDeclaration" action="cancelRightsDeclaration" key="label.cancel" />
                </td>
            </tr>
        </table>
    </s:form>
</div>