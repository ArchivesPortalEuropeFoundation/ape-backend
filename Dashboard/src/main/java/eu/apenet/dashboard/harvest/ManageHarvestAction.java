package eu.apenet.dashboard.harvest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import eu.apenet.dashboard.listener.Duration;
import eu.apenet.dashboard.listener.HarvesterTask;
import eu.apenet.dashboard.security.SecurityContext;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.listener.HarvesterDaemon;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.archivesportaleurope.harvester.oaipmh.HarvestObject;

/**
 * User: Yoann Moranville
 * Date: 12/11/2013
 *
 * @author Yoann Moranville
 */
public class ManageHarvestAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6086665250239818127L;
	private static final Logger LOGGER = Logger.getLogger(ManageHarvestAction.class);
    private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Integer harvestId;
    private boolean processOnceADay = true;
    private String selectedAction;
    private boolean stopHarvesting = false;
    private List<SelectItem> processOptions = new ArrayList<SelectItem>();
    private List<SelectItem> stopHarvestingProcessingOptions = new ArrayList<SelectItem>();
    public Integer getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(Integer harvestId) {
        this.harvestId = harvestId;
    }
    

    public List<SelectItem> getProcessOptions() {
		return processOptions;
	}

	public void setProcessOptions(List<SelectItem> processOptions) {
		this.processOptions = processOptions;
	}

	@Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("admin.harvestmanagement.title"));
    }

    public String execute() throws Exception {
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        processOnceADay = HarvesterDaemon.isProcessOnceADay();
        getServletRequest().setAttribute("numberOfActiveItems", archivalInstitutionOaiPmhDAO.countEnabledItems());
        getServletRequest().setAttribute("allOaiProfiles", DisplayHarvestProfileItem.getItems(archivalInstitutionOaiPmhDAO.getArchivalInstitutionOaiPmhs(), new Date()));
		getServletRequest().setAttribute("firstItems", DisplayHarvestProfileItem.getItems(archivalInstitutionOaiPmhDAO.getFirstItems(), new Date()));
        getServletRequest().setAttribute("harvestActive", HarvesterDaemon.isActive());
        getServletRequest().setAttribute("harvestProcessing", HarvesterDaemon.isHarvesterProcessing());
        getServletRequest().setAttribute("defaultHarvestingProcessing", APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing());
        getServletRequest().setAttribute("currentTime", DATE_TIME.format(new Date()));
        getServletRequest().setAttribute("dailyHarvesting",processOnceADay );
        processOptions.add(new SelectItem("true", "Look at the queue every day"));
        processOptions.add(new SelectItem("false", "Look at the queue every 10 minutes"));
        stopHarvestingProcessingOptions.add(new SelectItem("false", "Wait till current harvesting process is stopped."));
        stopHarvestingProcessingOptions.add(new SelectItem("true", "Interrupt current harvesting process and stop it."));
        try {
    		HarvestObject harvestObject = HarvesterDaemon.getHarvestObject();
    		if (harvestObject != null){
    			getServletRequest().setAttribute("harvestObject",harvestObject.copy());
    		}
        	
        }catch (Exception e){
        	
        }
        return SUCCESS;
    }

    public String manageHarvestItem(){
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = archivalInstitutionOaiPmhDAO.findById(harvestId.longValue());
		boolean isAdmin = SecurityContext.get().isAdmin();
    	if ("NOW".equals(selectedAction)) {
			archivalInstitutionOaiPmh.setNewHarvesting(new Date());
			archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);

			ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
					.setNameFormat("hervester-thread-%d").build();
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, namedThreadFactory);
			Duration DAILY_HARVESTING_DURATION = new Duration(4, 0, 0);
			HarvesterTask harvesterTask = new HarvesterTask(scheduler, DAILY_HARVESTING_DURATION);
			harvesterTask.setArchivalInstitutionOaiPmh(archivalInstitutionOaiPmh);
			scheduler.schedule(harvesterTask, 0, TimeUnit.SECONDS);
			try {
				Thread.sleep(1000);
				HarvesterDaemon.setHarvesterProcessing(false);
				scheduler.shutdownNow();
				scheduler = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if ("NOW_TODAY".equals(selectedAction)) {
			archivalInstitutionOaiPmh.setNewHarvesting(new Date());
			archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
		}else if ("METHOD".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setHarvestMethodListByIdentifiers(!archivalInstitutionOaiPmh.isHarvestMethodListByIdentifiers());
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("DISABLE".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setEnabled(false);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("ENABLE".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setEnabled(true);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("DELETE".equals(selectedAction) && isAdmin){
			if (archivalInstitutionOaiPmh.getErrorsResponsePath() != null){
				String[] items = DataHarvester.getErrorResponsePaths(archivalInstitutionOaiPmh);
				for (String item: items){
					try {
						ContentUtils.deleteFile(item , false);
					} catch (IOException e) {
						LOGGER.error("Could not delete: " + item + " " + e.getMessage());
					}
				}
			}
    		archivalInstitutionOaiPmhDAO.delete(archivalInstitutionOaiPmh);
    	}else if ("FULL".equals(selectedAction) && isAdmin){
    		archivalInstitutionOaiPmh.setFrom(null);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("DELAY".equals(selectedAction)){
    		Date newHarvestingDate = archivalInstitutionOaiPmh.getNewHarvesting();
    		if (newHarvestingDate == null){
    			newHarvestingDate = new Date();
    		}
    		newHarvestingDate = new Date(newHarvestingDate.getTime() + archivalInstitutionOaiPmh.getIntervalHarvesting());
    		archivalInstitutionOaiPmh.setNewHarvesting(newHarvestingDate);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("DELAY_ONE_DAY".equals(selectedAction)){
    		Date newHarvestingDate = archivalInstitutionOaiPmh.getNewHarvesting();
    		if (newHarvestingDate == null){
    			newHarvestingDate = new Date();
    		}
    		newHarvestingDate = new Date(newHarvestingDate.getTime() + ArchivalInstitutionOaiPmh.ONE_DAY);
    		archivalInstitutionOaiPmh.setNewHarvesting(newHarvestingDate);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("INTERVAL_LOCK".equals(selectedAction) && isAdmin){
    		archivalInstitutionOaiPmh.setLocked(true);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("INTERVAL_UNLOCK".equals(selectedAction) && isAdmin){
    		archivalInstitutionOaiPmh.setLocked(false);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("INTERVAL_DECREASE".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setIntervalHarvesting(decreaseInterval(archivalInstitutionOaiPmh.getIntervalHarvesting()));
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("INTERVAL_INCREASE".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setIntervalHarvesting(increaseInterval(archivalInstitutionOaiPmh.getIntervalHarvesting()));
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}
   	
    	return SUCCESS;
    }

    private static Long increaseInterval(Long value){
    	if (ArchivalInstitutionOaiPmh.INTERVAL_2_WEEKS > value){
    		return ArchivalInstitutionOaiPmh.INTERVAL_2_WEEKS ;
    	}else if (ArchivalInstitutionOaiPmh.INTERVAL_2_WEEKS.equals(value)){
    		return ArchivalInstitutionOaiPmh.INTERVAL_1_MONTH; 
    	}else if (ArchivalInstitutionOaiPmh.INTERVAL_1_MONTH.equals(value)){
    		return ArchivalInstitutionOaiPmh.INTERVAL_3_MONTH; 
    	}else if (ArchivalInstitutionOaiPmh.INTERVAL_3_MONTH.equals(value)){
    		return ArchivalInstitutionOaiPmh.INTERVAL_6_MONTH; 
    	}else {
    		return ArchivalInstitutionOaiPmh.INTERVAL_6_MONTH; 
    	}
    }
    
    private static Long decreaseInterval(Long value){
    	if (ArchivalInstitutionOaiPmh.INTERVAL_6_MONTH.equals(value)){
    		return ArchivalInstitutionOaiPmh.INTERVAL_3_MONTH; 
    	}else if (ArchivalInstitutionOaiPmh.INTERVAL_3_MONTH.equals(value)){
    		return ArchivalInstitutionOaiPmh.INTERVAL_1_MONTH; 
    	}else if (ArchivalInstitutionOaiPmh.INTERVAL_1_MONTH.equals(value)){
    		return ArchivalInstitutionOaiPmh.INTERVAL_2_WEEKS; 
    	}else {
    		return ArchivalInstitutionOaiPmh.INTERVAL_2_WEEKS; 
    	}
    }

    public String startStopHarvester() {
        if(HarvesterDaemon.isActive() || HarvesterDaemon.isHarvesterProcessing()) {
            HarvesterDaemon.stop(stopHarvesting);
        } else {
        	if (APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()){
        		HarvesterDaemon.start(processOnceADay);
            }else {
            	HarvesterDaemon.start(false);
            }
        }
        return SUCCESS;
    }

	public boolean isProcessOnceADay() {
		return processOnceADay;
	}

	public void setProcessOnceADay(boolean processOnceADay) {
		this.processOnceADay = processOnceADay;
	}

	public String getSelectedAction() {
		return selectedAction;
	}

	public void setSelectedAction(String selectedAction) {
		this.selectedAction = selectedAction;
	}

	public boolean isStopHarvesting() {
		return stopHarvesting;
	}

	public void setStopHarvesting(boolean stopHarvesting) {
		this.stopHarvesting = stopHarvesting;
	}

	public List<SelectItem> getStopHarvestingProcessingOptions() {
		return stopHarvestingProcessingOptions;
	}

	public void setStopHarvestingProcessingOptions(List<SelectItem> stopHarvestingProcessingOptions) {
		this.stopHarvestingProcessingOptions = stopHarvestingProcessingOptions;
	}


    
}
