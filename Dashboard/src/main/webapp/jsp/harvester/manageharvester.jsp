<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<dashboard:securityContext var="securityContext" />
	<script type="text/javascript">
	
	$(function() {
		$("table").tablesorter({
			debug: false,
			theme : 'tablesorter' 
		});
	});
	</script>
<div id="manageHarvester">
    <table class="defaultlayout">
        <tr>
            <th><s:text name="admin.harvestermanagement.currenttime" /></th>
            <td>${currentTime}</td>
        </tr>
        <tr>
            <th><s:text name="admin.harvestermanagement.harvester.totalactiveitem" /></th>
            <td>${numberOfActiveItems}</td>
        </tr>
        <tr>
            <th><s:text name="admin.harvestermanagement.harvester.active" /></th>
            <td>${harvestActive}</td>
        </tr>
        <tr>
            <th><s:text name="admin.harvestermanagement.harvester.processing" /></th>
            <td>${harvestProcessing}</td>
        </tr>
        <tr>
            <th><s:text name="admin.harvestermanagement.harvester.daily" /></th>
            <td>${dailyHarvesting}</td>
        </tr>
        <tr>
            <th>Default Harvesting processing</th>
            <td>${defaultHarvestingProcessing}</td>
        </tr>
        
    </table>
	<c:if test="${securityContext.admin}">
		<s:form action="startStopHarvester" method="post" theme="simple">
			<s:actionerror />
			<c:choose>
				<c:when test="${harvestActive or harvestProcessing}">
					<s:select  name="stopHarvesting" list="stopHarvestingProcessingOptions" listKey="value" listValue="content"></s:select>
					<s:submit key="admin.harvestermanagement.harvester.stop" cssClass="mainButton" name="startButton" />
				</c:when>
				<c:otherwise>
					<c:if test="${defaultHarvestingProcessing}">
					<s:select  name="processOnceADay" list="processOptions" listKey="value" listValue="content"></s:select>
					</c:if>
					<s:submit key="admin.harvestermanagement.harvester.start" cssClass="mainButton" name="startButton" />
				</c:otherwise>
			</c:choose>
		</s:form>
		<br/>
	</c:if>
    <c:if test="${!empty harvestObject}">
    <h2>Harvesting process status</h2>
    <table class="defaultlayout">
        <tr>
            <th>ID:</th>
            <td>${harvestObject.id}</td>
        </tr>
        <tr>
            <th>Latest record ID:</th>
            <td>${harvestObject.latestRecordId}</td>
        </tr>
        <tr>
            <th>Timestamp:</th>
            <td>${harvestObject.latestChangeDate}</td>
        </tr>
        <tr>
            <th>Record processes</th>
            <td><c:if test="${harvestObject.getRecordPhase}">${harvestObject.numberOfGetRecords} / </c:if>${harvestObject.numberOfRecords}</td>
        </tr>
        <tr>
            <th>Requests processed</th>
            <td>${harvestObject.numberOfRequests}</td>
        </tr>
        
    </table>   
    <br/> 
    </c:if>
	<h2>First items</h2>
                <table class="defaultlayout fullWidth">
			        <thead>
			            <tr>
			            	<th>ID</th>
			             	<th>Country</th>
			             	<th>Archival Institution</th>
			                <th>URL</th>
			                <th>Set</th>
			                <th>Metadata</th>
			                <th>Last date</th>
			                <th>Next date</th>
			                <th>OAI-from</th>
			                <th>Interval</th>
			                <th>Status</th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${firstItems}">

			            <tr class="${item.globalCss}">
			            	<td><c:out value="${item.id}" /></td>
			            	<td><c:out value="${item.country}" /></td>
			            	<td class="textLimitContainer"><span class="textLimit" title="<c:out value="${item.ainame}" />"><c:out value="${item.ainame}" /></span></td>
			                <td class="textLimitContainer"><span class="textLimit" title="<c:out value="${item.url}" />"><c:out value="${item.url}" /></span></td>
			                <td><c:out value="${item.set}" /></td>
			                <td><c:out value="${item.metadataPrefix}" /></td>
			                <td><c:out value="${item.lastHarvesting}" /></td>
			                <td><c:out value="${item.newHarvesting}" /></td>
			                <td><c:out value="${item.from}" /></td>
			                <td><c:out value="${item.intervalHarvesting}" /></td>
			                <td class="${item.errorCss} textLimitContainer"><c:if test="${!empty item.harvestingStatus}"><apenet:resource>${item.harvestingStatus}</apenet:resource></c:if></td>

			            </tr>
			        </c:forEach>
			        </tbody>
			    </table>
		<br/>
    	<h2>All saved harvest processes:</h2>
                <table class="defaultlayout fullWidth tablesorter">
			        <thead>
			            <tr>
			            	<th>ID</th>
			             	<th>Country</th>
			             	<th>Archival Institution</th>
			                <th>URL</th>
			                <th>Set</th>
			                <th>Metadata</th>
			                <th>Last date</th>
			                <th>Next date</th>
			                <th>OAI-from</th>
			                <th>OAI-method</th>
			                <th>Interval</th>
			                <th>Status</th>
			                <th>Actions</th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${allOaiProfiles}">

			            <tr class="${item.globalCss}">
			            	<td><c:out value="${item.id}" /></td>
			            	<td><c:out value="${item.country}" /></td>
			            	<td class="textLimitContainer"><span class="textLimit" title="<c:out value="${item.ainame}" />"><c:out value="${item.ainame}" /></span></td>
			                <td class="textLimitContainer"><span class="textLimit" title="<c:out value="${item.url}" />"><c:out value="${item.url}" /></span></td>
			                <td><c:out value="${item.set}" /></td>
			                <td><c:out value="${item.metadataPrefix}" /></td>
			                <td><c:out value="${item.lastHarvesting}" /></td>
			                <td><c:out value="${item.newHarvesting}" /></td>
			                <td><c:out value="${item.from}" /></td>
			                <td>
			                	<c:choose >
			                		<c:when test="${item.harvestMethodListByIdentifiers}">ListIdentifers</c:when>
			                		<c:otherwise>ListRecords</c:otherwise>
			                	</c:choose>
			                </td>
			                <td><c:out value="${item.intervalHarvesting}" /></td>
			                <td  class="${item.errorCss} textLimitContainer">
			                	<c:if test="${!empty item.harvestingStatus}">
			                		<c:choose>
			                				<c:when test="${empty item.harvestingDetails}"><apenet:resource>${item.harvestingStatus}</apenet:resource></c:when>
			                				<c:otherwise>
						                		<a href="downloadHarvesterErrorsText.action?harvestId=${item.id}"><apenet:resource>${item.harvestingStatus}</apenet:resource></a>
						                		<c:if test="${!empty item.errorResponsePath}">
						                			<c:forTokens items="${item.errorResponsePath}" delims="|" varStatus="varStatus">
						                				<br/><a href="downloadHarvesterErrorsXml.action?harvestId=${item.id}&index=${varStatus.index}">OAI-PMH Response ${varStatus.index}</a>
						                			</c:forTokens>
						                		</c:if>
											</c:otherwise>	
			                		</c:choose>

			                	</c:if>
			                	
			                </td>
			                <td class="actions">
			                    <c:choose>
			                        <c:when test="${harvestProcessing && !empty harvestObject && harvestObject.id == item.id}">
			                            <s:text name="admin.harvestermanagement.harvester.processing.noactions" />
			                        </c:when>
			                        <c:otherwise>
			 
			                        <s:form action="manageHarvestItem" theme="simple">
			                        	<input type="hidden" name="harvestId" value="${item.id}" />
			                        	<select class="selectedAction" name="selectedAction">
			                            <c:choose>
			                                <c:when test="${item.enabled}">
			                                	<option value="NOW">
													<s:text name="admin.harvestermanagement.harvester.startnow" />
												</option>
												<option value="NOW_TODAY">
													<s:text name="admin.harvestermanagement.harvester.starttoday" />
												</option>
			                                	<option value="DISABLE">
													<s:text name="admin.harvestermanagement.harvester.idle" />
												</option>
			                                </c:when>
			                                <c:otherwise>
			                                	<option value="ENABLE">
													<s:text name="admin.harvestermanagement.harvester.activate" />
												</option>
												<c:if test="${securityContext.admin}">
													<option value="DELETE">
														<s:text name="admin.harvestermanagement.harvester.delete" />
													</option>
												</c:if>
			                                </c:otherwise>
			                            </c:choose>
												<option value="INTERVAL_DECREASE">
													Decrease interval
												</option>
												<option value="INTERVAL_INCREASE">
													Increase interval
												</option>
											<c:if test="${securityContext.admin}">
			                            <c:choose>
			                                <c:when test="${item.locked}">
												<option value="INTERVAL_UNLOCK">
													Unlock interval
												</option>	
			                                </c:when>
			                                <c:otherwise>
												<option value="INTERVAL_LOCK">
													Lock interval
												</option>
			                                </c:otherwise>
			                            </c:choose>
											</c:if>
												<option value="DELAY">
													Delay with interval
												</option>
												<option value="DELAY_ONE_DAY">
													Delay with one day
												</option>
												<c:if test="${securityContext.admin}">
													<option value="FULL">
														Harvest everything
													</option>
												</c:if>
			                            		<option value="METHOD">
													Change harvester method
												</option>												
			                            </select>
			                            	<input type="submit" value="<s:text name="content.message.go" />" />	
			                            </s:form>
			                        </c:otherwise>
			                    </c:choose>
			                </td>
			            </tr>
			        </c:forEach>
			        </tbody>
			    </table>
</div>
