/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.exceptions.ViolationException;
import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.response.ArchivalInstitutesResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.InstituteEadResponseSet;
import eu.archivesportaleurope.apeapi.services.AiStatService;
import eu.archivesportaleurope.apeapi.services.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 *
 * @author kaisar
 */
@Component
@Path("/institute")
@Api("/institute")
@Produces({ServerConstants.APE_API_V1})
public class ArchivalInstituteStatResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AiStatService aiStatService;

    @Autowired
    SearchService eadSearch;

    @GET
    @Path("/getInstitutes/{startIndex}/{count}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "return list of Archival institute", response = ArchivalInstitutesResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response getInsByOpenData(@ApiParam(value = "Start Index (Starts form 0)", required = true) @PathParam("startIndex") int startIndex,
            @ApiParam(value = "Count can't be more than 50", required = true) @PathParam("count") int count) {
        if (count < 1 || count > 50) {
            throw new ViolationException("Count can not be less than one and greater than 50", "Count was: " + count);
        }
        try {
            QueryResponse resp = eadSearch.searchInstituteInGroup((startIndex < 0) ? 0 : startIndex, count);
            logger.info("Number fo group command " + resp.getGroupResponse());
            return Response.ok().entity(new ArchivalInstitutesResponse(resp)).build();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }

    @POST
    @Path("/getDocs")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "return list of documents of given type from the given Archival institute", response = EadResponseSet.class)
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getInsDocument(@ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true)
            @Valid InstituteDocRequest request) {
        try {
            QueryResponse queryResponse = eadSearch.searchDocPerInstitute(request);
            InstituteEadResponseSet eadResponseSet = new InstituteEadResponseSet(queryResponse);
            return Response.ok().entity(eadResponseSet).build();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
}