<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags" %>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags" %>

<div id="queueStatusDiv" class="queueStatusDiv">
    <c:if test="${aiItemsInQueue > 0}">
        <s:text name="content.message.queue.size.youritems"/> <span class="aiItemsInQueue">${aiItemsInQueue}</span>,
    </c:if>
    <s:text name="content.message.queue.size.allitems"/> <span class="totalItemsInQueue">${totalItemsInQueue}</span>
    <c:if test="${!empty positionInQueue}">,
        <s:text name="content.message.queue.size.itemsbeforeyou"/> <span class="positionInQueue">${positionInQueue}</span>
    </c:if>

    <div class="filesDiv">
        <c:if test="${aiUpFiles > 0}">
            You recently uploaded <span class="aiUpFiles">${aiUpFiles}</span> files! Check their status <a href="checkfilesuploaded.action">here</a>.
        </c:if>
        </div>
    <div class="refreshDiv">
        The queue status refreshes automatically... next refresh in <span id="timerCount"></span> seconds. Manual <a
            id="manualRefresh" href="javascript:void(null);">refresh</a>!
    </div>

    <br/>

    <c:if test="${not empty errorItems}">There are problems in the queue with your files, click <span class="link"
                                                                                                      id="seeErrors">here to see</span>.
        <div class="hidden" id="errorItems">
            <c:forEach var="errorItem" items="${errorItems}">
                ${errorItem.eadidOrFilename} - ${errorItem.action}: ${errorItem.errors}<br/>
            </c:forEach>
        </div>
    </c:if>
</div>