package eu.apenet.dashboard.listener;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import eu.apenet.commons.solr.SolrUtil;
import eu.apenet.commons.utils.APEnetUtilities;

public class SolrMaintenanceTask implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(SolrMaintenanceTask.class);
	private final ScheduledExecutorService scheduler;

	public SolrMaintenanceTask(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void run() {
		LOGGER.info("Maintenance process started");
		try {
			executeMaintenanceTasks();
		} catch (Throwable e) {
			LOGGER.error("Stopping processing for a while: " + APEnetUtilities.generateThrowableLog(e));
		}

		LOGGER.info("Maintenance process stopped");
		if (!scheduler.isShutdown()) {
			int delaySeconds = MaintenanceDaemon.calculateSeconds();
			LOGGER.info("Next maintenance task after " + MaintenanceDaemon.convertNumberToDuration(delaySeconds));
			scheduler.schedule(new SolrMaintenanceTask(scheduler), delaySeconds,
					TimeUnit.SECONDS);
		}
	}

	public void executeMaintenanceTasks() throws Exception {
		APEnetUtilities.getDashboardConfig().setMaintenanceMode(true);
		QueueDaemon.stop();
		SolrUtil.forceSolrCommit();
		SolrUtil.solrOptimize();
		SolrUtil.rebuildAutosuggestion();
		QueueDaemon.start();
		APEnetUtilities.getDashboardConfig().setMaintenanceMode(false);
	}
}
