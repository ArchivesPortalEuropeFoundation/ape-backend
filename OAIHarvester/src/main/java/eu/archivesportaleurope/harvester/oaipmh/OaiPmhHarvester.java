package eu.archivesportaleurope.harvester.oaipmh;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterConnectionException;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterInterruptionException;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterParserException;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhParser;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhRecord;
import eu.archivesportaleurope.harvester.oaipmh.parser.record.ResultInfo;
import eu.archivesportaleurope.harvester.util.OaiPmhHttpClient;
import eu.archivesportaleurope.harvester.verb.GetRecordVerb;
import eu.archivesportaleurope.harvester.verb.ListIdentifiersVerb;
import eu.archivesportaleurope.harvester.verb.ListRecordsVerb;

public class OaiPmhHarvester {
	public static final String LOGGER_STRING = "OAI-PMH_PROGRESS";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_STRING);
	private static final int MAX_NUMBER_OF_ERRORS = 15;
	public static void harvestByListRecords(HarvestObject harvestObject, String baseURL, String from, String until, String metadataPrefix, String setSpec,
			OaiPmhParser oaiPmhParser, File errorDir, OaiPmhHttpClient oaiPmhHttpClient) throws Exception {
		try {
			ListRecordsVerb listRecordsVerb = new ListRecordsVerb(oaiPmhHttpClient, baseURL, from, until, setSpec, metadataPrefix, oaiPmhParser,
					errorDir);
			harvestObject.increaseNumberOfRequests();
			ResultInfo resultInfo = listRecordsVerb.harvest(harvestObject);
			boolean hasErrors = false;
			while (resultInfo != null && !harvestObject.isStopHarvesting()) {
				List<String> errors = resultInfo.getErrors();
				if (errors != null && errors.size() > 0) {
					logErrors(harvestObject, resultInfo);
					hasErrors = true;
					break;
				}
				String resumptionToken = resultInfo.getNewResumptionToken();
				if (StringUtils.isBlank(resumptionToken)) {
					resultInfo = null;
				} else {
					harvestObject.increaseNumberOfRequests();
					resultInfo = listRecordsVerb.harvest(harvestObject);
				}
			}
			List<OaiPmhRecord> records = harvestObject.getRecords();
			while (records.size() > 0 ){
				OaiPmhRecord record = records.get(0);
				if (record.isDropped() || record.isDeleted()){
					if (record.isDeleted()){
						harvestObject.addDeletedRecord(record);
					}
					if (record.isDropped()){
						harvestObject.addDroppedRecord(record);
					}
				}
				records.remove(0);
			}
			String logSuffix = "";
			if (harvestObject.isStopHarvesting()){
				logSuffix = "Harvesting is stopped manually.";
			}
			if (hasErrors) {
				harvestObject.setFailed(true);
				LOGGER.error(harvestObject.getNumberOfRecords() + " records harvested, but with errors. " + logSuffix);
			} else {
				LOGGER.info(harvestObject.getNumberOfRecords() + " records harvested successfully with no errors. " + logSuffix);
			}
			if (harvestObject.isStopHarvesting()){
				throw new HarvesterInterruptionException(resultInfo.getRequestUrl(), logSuffix);
			}
		} catch (HarvesterParserException hpe) {
			LOGGER.error("Unable to parse XML response from the OAI-PMH server, look at the XML response file at "
					+ hpe.getNotParsebleResponse().getCanonicalPath()+"\n" + hpe.getMessage());
			throw hpe;
		}
	}
	

	public static void harvestByListIdentifiers(HarvestObject harvestObject, String baseURL, String from, String until, String metadataPrefix, String setSpec,
			OaiPmhParser oaiPmhParser, File errorDir, OaiPmhHttpClient oaiPmhHttpClient) throws Exception {
		try {
			ListIdentifiersVerb listIdentifiersVerb = new ListIdentifiersVerb(oaiPmhHttpClient, baseURL, from, until, setSpec, metadataPrefix, oaiPmhParser,
					errorDir);
			harvestObject.increaseNumberOfRequests();
			ResultInfo resultInfo = listIdentifiersVerb.harvest(harvestObject);
			boolean hasErrors = false;
			while (resultInfo != null && !harvestObject.isStopHarvesting()) {
				List<String> errors = resultInfo.getErrors();
				if (errors != null && errors.size() > 0) {
					logErrors(harvestObject, resultInfo);
					hasErrors = true;
					break;
				}
				String resumptionToken = resultInfo.getNewResumptionToken();
				if (StringUtils.isBlank(resumptionToken)) {
					resultInfo = null;
				} else {
					harvestObject.increaseNumberOfRequests();
					resultInfo = listIdentifiersVerb.harvest(harvestObject);
				}
			}
			String logSuffix = "";
			if (harvestObject.isStopHarvesting()){
				logSuffix = "Harvesting is stopped manually.";
			}
			if (hasErrors) {
				harvestObject.setFailed(true);
				LOGGER.error(harvestObject.getNumberOfRecords() + " identifiers harvested, but with errors. " + logSuffix);
			} else {
				LOGGER.info(harvestObject.getNumberOfRecords() + " identifiers harvested successfully with no errors. " + logSuffix);
			}
			harvestObject.setGetRecordPhase(true);
			GetRecordVerb getRecordVerb = new GetRecordVerb(oaiPmhHttpClient, baseURL, metadataPrefix, oaiPmhParser, errorDir);
			int numberOfErrors = 0;
			List<OaiPmhRecord> records = harvestObject.getRecords();
			while (records.size() > 0 && numberOfErrors <= MAX_NUMBER_OF_ERRORS && !harvestObject.maxNumberOfRecordsExceed()){
				try {
					OaiPmhRecord record = records.get(0);
					if (record.isDropped() || record.isDeleted()){
						harvestObject.increaseNumberOfGetRecords();
						if (record.isDeleted()){
							harvestObject.addDeletedRecord(record);
						}
						if (record.isDropped()){
							harvestObject.addDroppedRecord(record);
						}
					}else {

						ResultInfo getRecordInfo = getRecordVerb.harvest(harvestObject, record.getIdentifier());
						if (getRecordInfo != null){
							harvestObject.increaseNumberOfGetRecords();
							harvestObject.increaseNumberOfRequests();
							harvestObject.setLatestRecordId(record.getIdentifier());
							LOGGER.info("("+harvestObject.getNumberOfRecords()+"," + harvestObject.getNumberOfGetRecords() +"): GR: " + record );
							List<String> errors = getRecordInfo.getErrors();
							if (errors != null && errors.size() > 0) {
								logErrors(harvestObject, getRecordInfo);
								numberOfErrors++;
							}
							if (harvestObject.isStopHarvesting()){
								throw new HarvesterInterruptionException(getRecordInfo.getRequestUrl(), logSuffix);
							}
						}
					}


				}catch (HarvesterParserException hpe){
					harvestObject.addErrors("\nUrl that contains errors: '" + hpe.getRequestUrl() + "'\n", hpe.getNotParsebleResponse().getCanonicalPath()+"\n" + hpe.getMessage());
					numberOfErrors++;
					if (numberOfErrors >= MAX_NUMBER_OF_ERRORS){
						throw hpe;
					}
				}catch (HarvesterConnectionException e) {
					String errors = "Url that have connection problems: '" + e.getRequestUrl() + "'\n\n";
					errors+= e.getMessage() +" (Time out is 60 minutes)";
					LOGGER.error(errors);
					numberOfErrors++;
					if (numberOfErrors >= MAX_NUMBER_OF_ERRORS){
						throw e;
					}
				}
				records.remove(0);

				
			}
			if (harvestObject.isStopHarvesting()){
				throw new HarvesterInterruptionException(resultInfo.getRequestUrl(), logSuffix);
			}
		} catch (HarvesterParserException hpe) {
			LOGGER.error("Unable to parse XML response from the OAI-PMH server, look at the XML response file at "
					+ hpe.getNotParsebleResponse().getCanonicalPath()+"\n" + hpe.getMessage());
			throw hpe;
		}
	}
	private static void logErrors(HarvestObject harvestObject, ResultInfo resultInfo){
		List<String> errors = resultInfo.getErrors();
		if (errors != null && errors.size() > 0) {
		LOGGER.error("Found the following errors:");
		harvestObject.addErrors("\nUrl that contains errors: '" + resultInfo.getRequestUrl() + "'\n");
		int length = errors.size();
		for (int i = 0; i < length; ++i) {
			String item = errors.get(i);
			if (StringUtils.isNotBlank(item)) {
				harvestObject.addErrors(item);
				LOGGER.error(item);
			}

		}
		LOGGER.error("Url that contains errors: '" + resultInfo.getRequestUrl() + "'");	
		}
	}
	
}
