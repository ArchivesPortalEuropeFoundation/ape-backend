<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="manageQueue">
	<table class="defaultlayout">
		<tr>
			<th><s:text name="admin.queuemanagement.currenttime" /></th>
			<td>${currentTime}</td>
		</tr>
		<tr>
			<th><s:text name="admin.queuemanagement.queue.totalnumber" /></th>
			<td>${numberOfItemsInQueue}</td>
		</tr>
		<tr>
			<th><s:text name="admin.queuemanagement.queue.active" /></th>
			<td class="${queueStatusCss}">${queueStatus}</td>
		</tr>
		<tr>
			<th><s:text name="admin.queuemanagement.queue.processing" /></th>
			<td>${queueProcessing}</td>
		</tr>
		<tr>
			<th><s:text name="admin.queuemanagement.harvesting.dashboard" /></th>
			<td>${dashboardHarvestingStarted}</td>
		</tr>
		<tr>
			<th><s:text name="admin.queuemanagement.harvesting.europeana" /></th>
			<td>${europeanaHarvestingStarted}</td>
		</tr>
		<c:if test="${europeanaHarvestingStarted}">
			<tr>
				<th><s:text name="admin.queuemanagement.harvesting.endtime" /></th>
				<td>${europeanaHarvestingEndTime}</td>
			</tr>
		</c:if>
	</table>
	<s:form action="startStopQueue" method="post">
		<s:actionerror />
		<c:choose>
			<c:when test="${queueActive}">
				<s:submit key="admin.queuemanagement.queue.stop"
					cssClass="mainButton" name="startButton" />
			</c:when>
			<c:when test="${not queueActive and not queueProcessing}">
				<s:submit key="admin.queuemanagement.queue.start"
					cssClass="mainButton" name="startButton" />
			</c:when>
		</c:choose>

	</s:form>
	<s:form action="changeMaintenanceMode" method="post">
		<s:actionerror />
		<c:choose>
			<c:when test="${maintenanceMode}">
				<s:submit value="Stop maintenance mode" />
			</c:when>
			<c:otherwise>
				<s:submit value="Start maintenance mode" />
			</c:otherwise>
		</c:choose>

	</s:form>	
	<s:form action="deleteAllQueueItemsWithErrors" theme="simple" method="post">
		<s:submit value="Delete all errors from the Queue"></s:submit>
	</s:form>
	<s:form action="deleteAllUnusedUploadFiles" theme="simple" method="post">
		<s:submit value="Delete all unused uploads"></s:submit>
	</s:form>
	<s:form action="forceSolrCommit" theme="simple" method="post">
		<s:submit value="Force Solr commit"></s:submit>
	</s:form>
	<c:if test="${not queueActive}">
		<s:form action="republishAllEagFiles" theme="simple" method="post">
			<s:submit value="Republish all EAG files"></s:submit>
		</s:form>	
	</c:if>
	<c:if test="${!empty firstItems}">
		<h2>First items:</h2>
		<table class="defaultlayout">
			<thead>
				<tr>
					<th>ID</th>
					<th>EADID/Filename</th>
					<th><s:text name="admin.queuemanagement.institution.name" /></th>
					<th><s:text name="admin.queuemanagement.queue.action" /></th>
					<th><s:text name="admin.queuemanagement.queue.priority" /></th>
					<th><s:text name="admin.queuemanagement.queue.actions" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${firstItems}">
					<tr>
						<td><c:out value="${item.id}" /></td>
						<td><c:out value="${item.eadidOrFilename}" /></td>
						<td><c:out value="${item.archivalInstitution}" /></td>
						<td><c:out value="${item.action}" /></td>
						<td><c:out value="${item.priority}" /></td>
						<td><c:if test="${not queueActive and not queueProcessing}">
			                        <s:form action="manageQueueItem" theme="simple">
			                        	<input type="hidden" name="queueItemId" value="${item.id}" />
			                        	<select class="selectedAction" name="selectedAction">
												<option value="DISABLE">
													<s:text name="admin.queuemanagement.disable" />
												</option>			                        	                          
		                                		<option value="HIGHEST">
													<s:text name="admin.queuemanagement.highest" />
												</option>
		                                		<option value="LOWEST">
													<s:text name="admin.queuemanagement.lowest" />
												</option>												
			                                	<option value="DELETE">
													<s:text name="admin.queuemanagement.delete" />
												</option>												
					                            </select>
			                            	<input type="submit" value="<s:text name="content.message.go" />" />	
			                            </s:form>						
						</c:if></td>					
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if test="${!empty disabledItems}">
	<h2>Disabled items:</h2>
		<table class="defaultlayout">
			<thead>
				<tr>
					<th>ID</th>
					<th>EADID/Filename</th>
					<th><s:text name="admin.queuemanagement.institution.name" /></th>
					<th><s:text name="admin.queuemanagement.queue.action" /></th>
					<th><s:text name="admin.queuemanagement.queue.priority" /></th>
					<th><s:text name="admin.queuemanagement.queue.actions" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${disabledItems}">
					<tr>
						<td><c:out value="${item.id}" /></td>
						<td><c:out value="${item.eadidOrFilename}" /></td>
						<td><c:out value="${item.archivalInstitution}" /></td>
						<td><c:out value="${item.action}" /></td>
						<td><c:out value="${item.priority}" /></td>
						<td><s:form action="manageQueueItem" theme="simple">
			                        	<input type="hidden" name="queueItemId" value="${item.id}" />
			                        	<select class="selectedAction" name="selectedAction">
												<option value="ENABLE">
													<s:text name="admin.queuemanagement.enable" />
												</option>			                        	                          
		                                		<option value="HIGHEST">
													<s:text name="admin.queuemanagement.highest" />
												</option>
		                                		<option value="LOWEST">
													<s:text name="admin.queuemanagement.lowest" />
												</option>												

			                                	<option value="DELETE">
													<s:text name="admin.queuemanagement.delete" />
												</option>												
					                            </select>
			                            	<input type="submit" value="<s:text name="content.message.go" />" />	
			                            </s:form></td>					
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if test="${!empty itemsWithErrors}">	
		<h2>Items in the queue with errors:</h2>
		<table class="defaultlayout">
			<thead>
				<th>ID</th>
				<th>EADID/Filename</th>
				<th><s:text name="admin.queuemanagement.institution.name" /></th>
				<th><s:text name="admin.queuemanagement.queue.action" /></th>
				<th><s:text name="admin.queuemanagement.queue.priority" /></th>
				<th><s:text name="admin.queuemanagement.queue.actions" /></th>
				<th><s:text name="admin.queuemanagement.queue.errors" /></th>
	
			</thead>
			<tbody>
				<c:forEach var="item" items="${itemsWithErrors}">
					<tr>
						<td><c:out value="${item.id}" /></td>
						<td><c:out value="${item.eadidOrFilename}" /></td>
						<td><c:out value="${item.archivalInstitution}" /></td>
						<td><c:out value="${item.action}" /></td>
						<td><c:out value="${item.priority}" /></td>
						<td><s:form action="manageQueueItem" theme="simple">
			                        	<input type="hidden" name="queueItemId" value="${item.id}" />
			                        	<select class="selectedAction" name="selectedAction">
			                                	<option value="DELETE">
													<s:text name="admin.queuemanagement.delete" />
												</option>			                        	
												<option value="ENABLE">
													<s:text name="admin.queuemanagement.enable" />
												</option>			                        	                          
		                                		<option value="HIGHEST">
													<s:text name="admin.queuemanagement.highest" />
												</option>
		                                		<option value="LOWEST">
													<s:text name="admin.queuemanagement.lowest" />
												</option>																								
					                            </select>
			                            	<input type="submit" value="<s:text name="content.message.go" />" />	
			                            </s:form></td>
						<td><c:out value="${item.errors}" /></td>
	
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</div>
