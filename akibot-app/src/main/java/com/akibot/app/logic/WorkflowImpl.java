package com.akibot.app.logic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akibot.common.device.Gyroscope;
import com.akibot.common.element.Vector3D;
import com.akibot.world.dao.WorldContentDao;
import com.akibot.world.dom.config.GridConfiguration;
import com.akibot.world.dom.geometry.ColladaGeometry;
import com.akibot.world.dom.geometry.GridGeometry;
import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.node.StandardNode;
import com.akibot.world.dom.transformation.NodeTransformation3D;

@Component
public class WorkflowImpl implements Workflow {
	private Logger logger = LoggerFactory.getLogger(WorkflowImpl.class);

	private static final int GYRO_REQUEST_INTERVAL_MS = 500;
	private ScheduledExecutorService scheduler;

	@Autowired
	private Gyroscope mainGyroscope;

	@Autowired
	private WorldSynchronizer worldSynchronizer;

	@Autowired
	WorldContentDao worldContentDao;

	public WorkflowImpl() {
		logger.info("Starting Workflow");
	}

	@Override
	public void startSample() {
		logger.info("Starting Simple");
		initWorldContent();
		intiScheduler();
	}

	private void initWorldContent() {
		// TODO: Move to application context

		Node worldNode = new StandardNode(Constants.NODE_NAME_WORLD);

		// ======================== Grid:
		int positionOffset = Constants.GRID_CELL_COUNT * Constants.GRID_CELL_SIZE / 2;
		GridConfiguration gridConfiguration = new GridConfiguration(Constants.GRID_CELL_COUNT,
				Constants.GRID_CELL_COUNT, Constants.GRID_CELL_SIZE, Constants.GRID_MAX_OBSTACLE_COUNT,
				new Vector3D(-positionOffset, -positionOffset, 0));
		GridGeometry gridGeometry = new GridGeometry(gridConfiguration);
		Node gridNode = new Node(Constants.NODE_NAME_GRID);
		gridNode.setGeometry(gridGeometry);
		worldContentDao.attachChild(worldNode, gridNode);

		// ======================== Robot:
		ColladaGeometry robotGeometry = new ColladaGeometry();
		robotGeometry.setFileName("model/AkiBot.dae");
		Node robotNode = new Node(Constants.NODE_NAME_ROBOT);
		robotNode.setGeometry(robotGeometry);
		worldContentDao.attachChild(gridNode, robotNode);

		// ======================== Gyroscope:
		Node gyroscopeNode = new Node(Constants.NODE_NAME_GYROSCOPE);
		NodeTransformation3D gyroTransform = new NodeTransformation3D();
		gyroTransform.setRotation(new Vector3D(0, 0, Math.toRadians(45)));
		gyroscopeNode.setStickToParent(true);
		worldContentDao.attachChild(robotNode, gyroscopeNode);

		// ======================== Distance:
		Node distanceCenterNode = new Node(Constants.NODE_NAME_DISTANCE_CENTER);
		worldContentDao.attachChild(robotNode, distanceCenterNode);

		worldContentDao.setWorldNode(worldNode);
	}

	private void intiScheduler() {
		logger.debug("Initialize Scheduler");
		this.scheduler = Executors.newScheduledThreadPool(1);

		final Runnable gyroRequestTask = new Runnable() {
			@Override
			public void run() {
				try {
					logger.trace("gyroRequestTask");
					String nodeName = "gyroscope";
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
