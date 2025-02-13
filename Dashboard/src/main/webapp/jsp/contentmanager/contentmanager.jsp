<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
    $(document).ready(function () {
        initContentManager(${xmlTypeId});
        hideOrShowSelectAllFAsWindow();
    });
</script>
<div id="contentmanager">
    <div id="relatedLinks">
        <a href="upload.action"><s:text name="dashboard.menu.uploadcontent" /></a>
    </div>
    <div id="listFiles">
        <div id="listFilesActions">
            <span id="clearAll" class="linkList leftSide">[<s:text name="content.message.clearall" />]
            </span> <span id="selectAllFiles" class="linkList leftSide">[<s:text name="content.message.selectall" />]
            </span>
        </div>
        <div id="sizeFiles"></div>
    </div>
    <div class="searchOptionsContent">
        <s:form id="newSearchForm" theme="simple" action="updateContentmanager">
            <s:hidden name="refreshFromSession" value="false" />
            <s:hidden name="orderByField" />
            <s:hidden name="orderByAscending" />
            <table>
                <tr>
                    <th class="contentThType" rowspan="2"><span><s:text name="content.message.type" />:</span></th>
                    <td class="nextContentThType" rowspan="4">
                        <s:iterator value="typeList">
                            <input id="newSearchForm_xmlTypeId<s:property value="key" />" class="typeRadio" type="radio" value="<s:property value="key"/>" name="xmlTypeId">
                            <label for="newSearchForm_xmlTypeId<s:property value="key" />"><s:property value="value"/></label>
                            <br>
                        </s:iterator>

                    </td>
                    <th><s:text name="content.message.converted" />:</th>
                    <td><s:checkboxlist list="convertedStatusList" name="convertedStatus" /></td>
                    <th><s:text name="content.message.linkedshort" />:</th>
                    <td><s:checkboxlist list="linkedStatusList" name="linkedStatus" /></td>


                </tr>
                <tr>
                    <th><s:text name="content.message.validated" />:</th>
                    <td><s:checkboxlist list="validatedStatusList" name="validatedStatus" /></td>
                    <th><s:text name="content.message.queue" />:</th>
                    <td><s:checkboxlist list="queuingStatusList" name="queuingStatus" /></td>
                </tr>
                <tr>
                    <th></th>
                    <th><s:text name="content.message.published" />:</th>
                    <td><s:checkboxlist list="publishedStatusList" name="publishedStatus" /></td>
                    <c:choose>
                        <c:when test="${xmlTypeId == '2'}">
                            <th class="findingAidOptions"></th>
                            <td class="findingAidOptions"></td>
                        </c:when>
                        <c:when test="${xmlTypeId == '4'}">
                            <th class="findingAidOptions"></th>
                            <td class="findingAidOptions"></td>
                        </c:when>
                        <c:otherwise>
                            <th class="findingAidOptions"><s:text name="content.message.europeana" />:</th>
                            <td class="findingAidOptions"><s:checkboxlist list="europeanaStatusList" name="europeanaStatus" /></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th></th>
                    <td colspan="5"><s:textfield name="searchTerms" />
                        <s:select cssClass="small left"	list="searchTermsFieldList" name="searchTermsField" />
                        <input type="submit" id="searchButton" value="<s:text name="content.message.search" />" class="mainButton"/>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
    <c:if test="${harvestingStarted or !queueActive}">
        <div id="queueStopped">
            <c:choose>
                <c:when test="${harvestingStarted}"><s:text name="content.message.harvesting" /></c:when>
                <c:when test="${!queueActive}"><s:text name="content.message.queue.stopped.maintenance" /></c:when>
            </c:choose>
        </div>
    </c:if>
    <div id="eads">
        <div id="ead-results-container">
            <c:choose>
                <c:when test="${xmlTypeId == '2'}">
                    <jsp:include page="contentmanager-eaccpf-results.jsp" />
                </c:when>
                <%--HIDE EAD3
                <c:when test="${xmlTypeId == '4'}">
                    <jsp:include page="contentmanager-ead3-results.jsp" />
                </c:when>--%>
                <c:otherwise>
                    <jsp:include page="contentmanager-results.jsp" />
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        var period = 10;
        manualCallAjax();
        var myTimeout = setInterval(ajaxTimer, 1000);
        var timerCount = period;
        function ajaxTimer() {
            if (timerCount == 0){
                timerCount = period;
                callAjax();
            }
            else {
                timerCount--;
            }

            $("#timerCount").html(timerCount);
        }

        function manualCallAjax() {
            timerCount = period;
            $("#timerCount").html(timerCount);
            callAjax();
        }

        function callAjax() {
            $("#queueSize").load("contentmanagerstatus.action #queueStatusDiv", function() {
                $("#timerCount").html(timerCount);

                $("#manualRefresh").click(function () {
                    manualCallAjax();
                });
            });
        }


    });
</script>

<c:if test="${errorLinkHgSg}">
    <jsp:include page="linktohgsgerror.jsp" />
</c:if>