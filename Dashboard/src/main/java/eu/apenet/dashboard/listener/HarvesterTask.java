package eu.apenet.dashboard.listener;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.harvest.DataHarvester;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Yoann Moranville Date: 23/10/2013
 * 
 * @author Yoann Moranville
 */
public class HarvesterTask implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(HarvesterTask.class);
	private Duration duration;
	private Duration delay;
	private static final long INTERVAL = 40000;
	private final ScheduledExecutorService scheduler;

	private ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh;

	public HarvesterTask(ScheduledExecutorService scheduler, Duration maxDuration, Duration delay) {
		this.duration = maxDuration;
		this.scheduler = scheduler;
		this.delay = delay;
		JpaUtil.init();
	}

	public HarvesterTask(ScheduledExecutorService scheduler, Duration maxDuration) {
		this.duration = maxDuration;
		this.scheduler = scheduler;
		JpaUtil.init();
	}
	@Override
	public void run() {
		if (delay == null){
			LOGGER.info("Harvester process started");			
		}else {
			LOGGER.debug("Harvester process started");
		}
		long endTime = System.currentTimeMillis() + duration.getMilliseconds();
		boolean stopped = false;
		while (!stopped && !scheduler.isShutdown() && System.currentTimeMillis() < endTime) {
			try {
				HarvesterDaemon.setHarvesterProcessing(true);
				processHarvester(endTime);
			} catch (Throwable e) {
				LOGGER.error("Stopping processing for a while: " + APEnetUtilities.generateThrowableLog(e));
				try {
					JpaUtil.rollbackDatabaseTransaction();
				} catch (Exception de) {
					LOGGER.error(de.getMessage());
				}
				stopped = true;
			}
			if (!stopped && !APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing() && (System.currentTimeMillis() + INTERVAL) < endTime) {
				cleanUp();
				try {
					Thread.sleep(INTERVAL);
				} catch (InterruptedException e) {
				}
			} else {
				cleanUp();
				stopped = true;
			}
		}
		if (delay == null){
			LOGGER.info("Harvester process stopped");			
		}else {
			LOGGER.debug("Harvester process stopped");
		}
		if (delay != null && !scheduler.isShutdown()) {
			scheduler.schedule(new HarvesterTask(scheduler, duration, delay), delay.getMilliseconds(),
					TimeUnit.MILLISECONDS);
		}
	}

	private static void cleanUp() {
		try {
			HarvesterDaemon.setHarvesterProcessing(false);
			JpaUtil.closeDatabaseSession();
		} catch (Exception de) {
			LOGGER.error(de.getMessage());
		}
	}

	public boolean processHarvester(long endTime) throws Exception {
		ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance()
				.getArchivalInstitutionOaiPmhDAO();
		List<ArchivalInstitutionOaiPmh> archivalInstitutionOaiPmhList = archivalInstitutionOaiPmhDAO.getReadyItems();

		if (getArchivalInstitutionOaiPmh() != null){
			archivalInstitutionOaiPmhList.clear();
			archivalInstitutionOaiPmhList.add(archivalInstitutionOaiPmhDAO.findById(getArchivalInstitutionOaiPmh().getId()));
		}
		// Check for each if the interval is right and which one should be done
		// right now
		for (ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh : archivalInstitutionOaiPmhList) {
			if (scheduler.isShutdown() || System.currentTimeMillis() > endTime) {
				break;
			}
			boolean continueTask = false;
			if (archivalInstitutionOaiPmh.isHarvestOnlyWeekend()) {
				continueTask = false;
				Calendar calendar = Calendar.getInstance();
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
					continueTask = true;
				}
			}else {
				continueTask = true;
			}
			if (continueTask) {
				new DataHarvester(archivalInstitutionOaiPmh.getId()).run();
			}
			
		}

		return false;
	}

	public void setArchivalInstitutionOaiPmh(ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh) {
		this.archivalInstitutionOaiPmh = archivalInstitutionOaiPmh;
	}

	public ArchivalInstitutionOaiPmh getArchivalInstitutionOaiPmh() {
		return archivalInstitutionOaiPmh;
	}
}
