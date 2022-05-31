package eu.archivesportaleurope.harvester.oaipmh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhRecord;

public class HarvestObject {
	private Long id;
    private DateHarvestModel newestFileHarvested;
    private DateHarvestModel oldestFileHarvested;
    private String harvestingDetails;
    private boolean failed;
    private boolean error;
    private boolean warning;
    private int numberOfRecords = 0;
    private int numberOfGetRecords = 0;
    private int numberOfRequests = 0;
    private boolean stopHarvesting = false;
    private String latestRecordId;
    private Date latestChangeDate;
    private boolean getRecordPhase = false;
    private String notParsableResponses;
    private Integer maxNumberOfRecords = null;
    private List<OaiPmhRecord> records = new ArrayList<OaiPmhRecord>();
    private List<OaiPmhRecord> deletedRecords = new ArrayList<OaiPmhRecord>();
	private List<OaiPmhRecord> droppeddRecords = new ArrayList<OaiPmhRecord>();
    
    public HarvestObject(){
    }
    
    
    public HarvestObject(Long id, Integer maxNumberOfRecords){
    	this.id = id;
    	this.maxNumberOfRecords = maxNumberOfRecords;
    }
    
    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public boolean maxNumberOfRecordsExceed(){
		if (maxNumberOfRecords == null){
			return false;
		}else {
			if (getRecordPhase){
				return numberOfGetRecords >= maxNumberOfRecords;
			}else {
				return numberOfRecords >= maxNumberOfRecords;
			}
		}
	}
	
	public String getHarvestingDetails() {
		return harvestingDetails;
	}
	public boolean isFailed() {
		return failed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	
	public boolean isWarning() {
		return warning;
	}


	public void setWarning(boolean warning) {
		this.warning = warning;
	}


	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public Date getLatestChangeDate() {
		return latestChangeDate;
	}


	public void setLatestChangeDate(Date latestChangeDate) {
		this.latestChangeDate = latestChangeDate;
	}



	public List<OaiPmhRecord> getRecords() {
		return records;
	}
	public void setRecords(List<OaiPmhRecord> records) {
		this.records = records;
	}
	public int getNumberOfRecords() {
		return numberOfRecords;
	}


	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}


	public int getNumberOfGetRecords() {
		return numberOfGetRecords;
	}
	public void setNumberOfGetRecords(int numberOfGetRecords) {
		this.numberOfGetRecords = numberOfGetRecords;
	}
	public int getNumberOfRequests() {
		return numberOfRequests;
	}
	public void increaseNumberOfRequests(){
		numberOfRequests++;
	}
	public void increaseNumberOfGetRecords(){
		numberOfGetRecords++;
	}
	public void increaseNumberOfRecords(){
		numberOfRecords++;
	}

	public void setNumberOfRequests(int numberOfRequests) {
		this.numberOfRequests = numberOfRequests;
	}
	


	public String getLatestRecordId() {
		return latestRecordId;
	}


	public boolean isGetRecordPhase() {
		return getRecordPhase;
	}
	public void setGetRecordPhase(boolean getRecordPhase) {
		this.getRecordPhase = getRecordPhase;
	}
	public void setLatestRecordId(String latestRecordId) {
		this.latestRecordId = latestRecordId;
		this.latestChangeDate = new Date();
	}


	public boolean isStopHarvesting() {
		return stopHarvesting;
	}


	public void setStopHarvesting(boolean stopHarvesting) {
		this.stopHarvesting = stopHarvesting;
	}

	public void addErrors(String errors, String notParsableResponse) {
		error = true;
		if (this.harvestingDetails == null){
			this.harvestingDetails = errors + "\n";
		}else {
			this.harvestingDetails += errors + "\n";
		}
		if (this.notParsableResponses == null){
			this.notParsableResponses = notParsableResponse;
		}else {
			this.notParsableResponses +="|" + notParsableResponse ;
		}
	}
	
	public String getNotParsableResponses() {
		return notParsableResponses;
	}
	public void addErrors(String errors) {
		error = true;
		if (this.harvestingDetails == null){
			this.harvestingDetails = errors + "\n";
		}else {
			this.harvestingDetails += errors + "\n";
		}
	}
	public void addDeletedRecord(OaiPmhRecord record){
		if (record != null){
			this.deletedRecords.add(record);
		}
	}
	
	public List<OaiPmhRecord> getDeletedRecords() {
		return deletedRecords;
	}

	public void addDroppedRecord(OaiPmhRecord record){
		if (record != null){
			this.droppeddRecords.add(record);
		}
	}

	public List<OaiPmhRecord> getDroppedRecords() {
		return droppeddRecords;
	}

	public void addInfo(String info) {
		if (this.harvestingDetails == null){
			this.harvestingDetails = info + "\n";
		}else {
			this.harvestingDetails += info + "\n";
		}
	}

	public void addWarnings(String errors) {
		this.warning= true;
		if (this.harvestingDetails == null){
			this.harvestingDetails = errors + "\n";
		}else {
			this.harvestingDetails += errors + "\n";
		}
	}
	public DateHarvestModel getNewestFileHarvested() {
		return newestFileHarvested;
	}


	public void setNewestFileHarvested(DateHarvestModel newestFileHarvested) {
		this.newestFileHarvested = newestFileHarvested;
	}


	public DateHarvestModel getOldestFileHarvested() {
		return oldestFileHarvested;
	}


	public void setOldestFileHarvested(DateHarvestModel oldestFileHarvested) {
		this.oldestFileHarvested = oldestFileHarvested;
	}
	public void add(OaiPmhRecord oaiPmhRecord){
		records.add(oaiPmhRecord);
        if(oldestFileHarvested == null && newestFileHarvested == null) {
            oldestFileHarvested = new DateHarvestModel(oaiPmhRecord.getTimestamp(), oaiPmhRecord.getIdentifier());
            newestFileHarvested = new DateHarvestModel(oaiPmhRecord.getTimestamp(), oaiPmhRecord.getIdentifier());
        } else {
            if(!newestFileHarvested.isCurrentOlderThanNew(oaiPmhRecord.getTimestamp())) {
                newestFileHarvested = new DateHarvestModel(oaiPmhRecord.getTimestamp(), oaiPmhRecord.getIdentifier());
            } else if(oldestFileHarvested.isCurrentOlderThanNew(oaiPmhRecord.getTimestamp())) {
                oldestFileHarvested = new DateHarvestModel(oaiPmhRecord.getTimestamp(), oaiPmhRecord.getIdentifier());
            }
        }
	}

	public static class DateHarvestModel {
        private Date datestamp;
        private String identifier;

        public DateHarvestModel(Date datestamp, String identifier) {
            this.datestamp = datestamp;
            this.identifier = identifier;
        }

        @Override
        public String toString() {
            return "datestamp: '" + datestamp + "' - identifier: '" + identifier + "'";
        }

        public boolean isCurrentOlderThanNew(Date newDate) {
            return datestamp.after(newDate);
        }
    }

	public HarvestObject copy(){
		HarvestObject object = new HarvestObject(id, maxNumberOfRecords);
		object.setLatestRecordId(latestRecordId);
		object.setNumberOfRecords(numberOfRecords);
		object.setNumberOfRequests(numberOfRequests);
		object.setLatestChangeDate(latestChangeDate);
		object.setGetRecordPhase(getRecordPhase);
		object.setNumberOfGetRecords(numberOfGetRecords);
		return object;
	}
}
