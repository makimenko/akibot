package com.akibot.app.logic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akibot.common.device.Gyroscope;
import com.akibot.common.element.Vector3D;
import com.akibot.world.dao.WorldContentDao;
import com.akibot.world.dom.node.Node;

@Component
public class WorkflowImpl implements Workflow {
	private Logger logger = LoggerFactory.getLogger(WorkflowImpl.class);

	private static final int GYRO_REQUEST_INTERVAL_MS = 5000;
	private ScheduledExecutorService scheduler;

	@Autowired
	private Gyroscope mainGyroscope;

	@Autowired
	private WorldSynchronizer worldSynchronizer;

	@Autowired
	WorldContentDao worldContentDao;

	@Autowired
	Node worldNode;

	public WorkflowImpl() {
		logger.info("Starting Workflow");
	}

	@PostConstruct
	public void init() {
		startSample();
	}

	@Override
	public void startSample() {
		logger.info("Starting Simple");
		intiScheduler();
	}

	private void intiScheduler() {
		logger.debug("Initialize Scheduler");
		this.scheduler = Executors.newScheduledThreadPool(1);

		final Runnable gyroRequestTask = new Runnable() {
			@Override
			public void run() {
				try {
					logger.trace("gyroRequestTask");
					String nodeName = "gyroscopeNode";
					Vector3D gyroscopeValue = mainGyroscope.getGyroscopeValue();
					worldSynchronizer.syncGyroscope(nodeName, gyroscopeValue);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					scheduler.shutdown();
				}
			}
		};

		scheduler.scheduleAtFixedRate(gyroRequestTask, 0, GYRO_REQUEST_INTERVAL_MS, TimeUnit.MILLISECONDS);
	}

}
