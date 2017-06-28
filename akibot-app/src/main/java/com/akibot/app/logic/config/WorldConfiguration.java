package com.akibot.app.logic.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.akibot.app.logic.Constants;
import com.akibot.app.logic.Workflow;
import com.akibot.app.logic.WorkflowImpl;
import com.akibot.app.logic.WorldSynchronizer;
import com.akibot.app.logic.WorldSynchronizerImpl;
import com.akibot.common.element.Vector3D;
import com.akibot.world.dao.WorldContentDao;
import com.akibot.world.dao.WorldContentDaoImpl;
import com.akibot.world.dom.config.GridConfiguration;
import com.akibot.world.dom.geometry.ColladaGeometry;
import com.akibot.world.dom.geometry.Geometry;
import com.akibot.world.dom.geometry.GridGeometry;
import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.node.StandardNode;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class WorldConfiguration {

	@Autowired
	Node worldNode;

	@Autowired
	Node gridNode;

	@Autowired
	Node robotNode;

	@Value("${realtimeSyncDelay}")
	private int realtimeSyncDelay;

	@Value("${gridCellCountX}")
	private int gridCellCountX;

	@Value("${gridCellCountY}")
	private int gridCellCountY;

	@Value("${gridCellSizeMm}")
	private int gridCellSizeMm;

	@Value("${gridMaxObstacleCount}")
	private int gridMaxObstacleCount;

	@Value("${robotModel}")
	private String robotModel;

	// ======================================================

	@Bean
	public Workflow workflow() {
		return new WorkflowImpl();
	}

	@Bean
	public WorldContentDao worldContentDao() {
		return new WorldContentDaoImpl(worldNode);
	}

	@Bean
	public WorldSynchronizer worldSynchronizer() {
		return new WorldSynchronizerImpl(realtimeSyncDelay);
	}

	// ======================================================

	@Bean
	public Node worldNode() {
		return new StandardNode(Constants.NODE_NAME_WORLD);
	}

	@Bean
	public Node gridNode() {
		Node gridNode = new StandardNode(Constants.NODE_NAME_GRID, worldNode);
		return gridNode;
	}

	@Bean
	public Node robotNode() {
		Node robotNode = new StandardNode(Constants.NODE_NAME_ROBOT, gridNode);
		return robotNode;
	}

	@Bean
	public Node gyroscopeNode() {
		Node gyroscopeNode = new StandardNode(Constants.NODE_NAME_GYROSCOPE, robotNode);
		gyroscopeNode.setStickToParent(true);
		return gyroscopeNode;
	}

	@Bean
	public Node distanceCenterNode() {
		Node distanceCenterNode = new StandardNode(Constants.NODE_NAME_DISTANCE_CENTER, robotNode);
		return distanceCenterNode;
	}

	@PostConstruct
	public void applyNodeConfiguration() {
		Vector3D gridOffsetVector = new Vector3D(gridCellCountX * gridCellSizeMm / 2,
				gridCellCountY * gridCellSizeMm / 2, 0);
		GridConfiguration gridConfiguration = new GridConfiguration(gridCellCountX, gridCellCountY, gridCellSizeMm,
				gridMaxObstacleCount, gridOffsetVector);
		Geometry geometry = new GridGeometry(gridConfiguration);
		gridNode.setGeometry(geometry);

		Geometry robotGeometry = new ColladaGeometry(robotModel);
		robotNode.setGeometry(robotGeometry);
	}

}
