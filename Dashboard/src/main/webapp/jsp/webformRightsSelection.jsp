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
        <s:text name="dashboard.managedatasharing.introduction.newinstitute"/>
    </s:if>
    <s:text name="dashboard.managedatasharing.introduction"/>
    <br><br>
    <s:form method="POST" theme="simple" action="saveRightsDeclaration" >
        <table class="rightsinformation-tbl" style="width:70%">
            <colgroup>
                <col style="width:33%">
                <col style="width:67%">
            </colgroup>
            <tbody>
                <tr>
                    <td><s:label for="rights" key="label.rightsinfo.defaultstatement"/></td>
                    <td>
                        <s:select id="rights" name="rights" list="rightsList" listKey="value" listValue="content" onchange="updateRightsText()"/>
                        <div id="entitlementRights" style="margin-top: 20px; display: <s:if test="entitlementRights!=null">block</s:if><s:else>none</s:else>">
                            <div style="margin-bottom: 7px;">
                                <s:label for="entitlementRights" key="label.rightsinfo.entitlementstatement"/>
                            </div>
                            <div>
                                <s:select id="entitlementRights" name="entitlementRights" list="entitlementRightsList" listKey="value" listValue="content"/>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><s:label key="label.rightsinfo.description"/></td>
                    <td><s:textarea id="description" name="description" value="%{description}"/></td>
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