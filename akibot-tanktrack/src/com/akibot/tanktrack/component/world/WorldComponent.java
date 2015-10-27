package com.akibot.tanktrack.component.world;

import java.io.Serializable;
import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.configuration.ComponentConfiguration;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.distance.DistanceDetails;
import com.akibot.tanktrack.component.world.element.Angle;
import com.akibot.tanktrack.component.world.element.Node;
import com.akibot.tanktrack.component.world.element.NodeTransformation;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.VectorUtils;
import com.akibot.tanktrack.component.world.message.WorldConfiguration;
import com.akibot.tanktrack.component.world.message.WorldContent;
import com.akibot.tanktrack.component.world.message.WorldContentRequest;
import com.akibot.tanktrack.component.world.message.WorldContentResponse;
import com.akibot.tanktrack.component.world.message.WorldDistanceUpdateRequest;
import com.akibot.tanktrack.component.world.message.WorldNodeTransformationRequest;
import com.akibot.tanktrack.component.world.message.WorldRequest;
import com.akibot.tanktrack.component.world.message.WorldUpdateRequest;
import com.akibot.tanktrack.launcher.Constants;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);
	private Node worldNode;
	private HashMap<String, Node> nodeList = new HashMap<String, Node>();
	private WorldConfiguration componentConfiguration;

	public WorldComponent() {

	}

	@Override
	public ComponentConfiguration getComponentConfiguration() {
		return this.componentConfiguration;
	}

	@Override
	public void onGetConfigurationResponse(GetConfigurationResponse getConfigurationResponse) throws FailedToConfigureException {
		Serializable responseValue = getConfigurationResponse.getComponentConfiguration();
		if (responseValue instanceof WorldConfiguration) {
			setComponentConfiguration((WorldConfiguration) responseValue);
		} else {
			throw new FailedToConfigureException(responseValue.toString());
		}
	}

	public void setComponentConfiguration(WorldConfiguration componentConfiguration) throws FailedToConfigureException {
		this.componentConfiguration = componentConfiguration;
		initWorld();
	}

	private void initWorld() {
		worldNode = componentConfiguration.getWorldContent().getWorldNode();
		indexAll(worldNode);

		getComponentStatus().setReady(true);
		// TODO: Remove simulation:
		simulateMessages();
	}

	private void simulateMessages() {

		// Update Gyroscope Rotation:
		WorldNodeTransformationRequest gyroUpdateRequest = new WorldNodeTransformationRequest();
		gyroUpdateRequest.setNodeName(Constants.COMPONENT_NAME_AKIBOT_GYROSCOPE);
		NodeTransformation gyroTransformation = new NodeTransformation();
		gyroTransformation.setRotation(new Point(0, 0, VectorUtils.gradToRad(45)));
		gyroUpdateRequest.setTransformation(gyroTransformation);
		onWorldUpdateRequest(gyroUpdateRequest);

		// Update Robot Position:
		WorldNodeTransformationRequest robotMoveRequest = new WorldNodeTransformationRequest();
		robotMoveRequest.setNodeName(Constants.NODE_NAME_ROBOT);
		NodeTransformation robotMoveTransformation = new NodeTransformation();
		robotMoveTransformation.setPosition(new Point(10, 0, 1.5f));
		robotMoveRequest.setTransformation(robotMoveTransformation);
		onWorldUpdateRequest(robotMoveRequest);

		// Update Distance:
		WorldDistanceUpdateRequest worldDistanceUpdateRequest = new WorldDistanceUpdateRequest();
		worldDistanceUpdateRequest.setGridNodeName(Constants.NODE_NAME_GRID);
		DistanceDetails distance;

		// front:
		worldDistanceUpdateRequest.setDistanceNodeName(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT);
		distance = new DistanceDetails(new Point(0, 0, 0), new Angle(0), Constants.DISTANCE_ERRROR_ANGLE, 1000, true);
		worldDistanceUpdateRequest.setDistanceDetails(distance);
		onWorldUpdateRequest(worldDistanceUpdateRequest);

		// back:
		worldDistanceUpdateRequest.setDistanceNodeName(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK);
		distance = new DistanceDetails(new Point(0, 0, 0), new Angle(0), Constants.DISTANCE_ERRROR_ANGLE, 500, true);
		worldDistanceUpdateRequest.setDistanceDetails(distance);
		onWorldUpdateRequest(worldDistanceUpdateRequest);
	}

	public void index(Node node) {
		nodeList.put(node.getName(), node);
	}

	public void indexAll(Node node) {
		index(node);
		if (node.getChilds() != null) {
			for (Node i : node.getChilds()) {
				indexAll(i);
			}
		}
	}

	public Node findNode(String name) {
		return nodeList.get(name);
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof WorldContentRequest) {
			onWorldContentRequest((WorldContentRequest) message);
		} else if (message instanceof WorldUpdateRequest) {
			onWorldUpdateRequest((WorldUpdateRequest) message);
		}
	}

	private void onWorldContentRequest(WorldContentRequest worldContentRequest) throws FailedToSendMessageException {
		WorldContentResponse worldContentResponse = new WorldContentResponse();
		WorldContent worldContent = new WorldContent();
		worldContent.setWorldNode(worldNode);
		worldContentResponse.setWorldContent(worldContent);
		this.broadcastResponse(worldContentResponse, worldContentRequest);
	}

	private void onWorldUpdateRequest(WorldUpdateRequest worldUpdateRequest) {
		if (worldUpdateRequest instanceof WorldNodeTransformationRequest) {
			onWorldNodeTransformationRequest((WorldNodeTransformationRequest) worldUpdateRequest);
		} else if (worldUpdateRequest instanceof WorldDistanceUpdateRequest) {
			onWorldDistanceUpdateRequest((WorldDistanceUpdateRequest) worldUpdateRequest);
		}
	}

	private void onWorldNodeTransformationRequest(WorldNodeTransformationRequest worldNodeTransformationRequest) {
		Node node = nodeList.get(worldNodeTransformationRequest.getNodeName());
		NodeTransformation transformation = worldNodeTransformationRequest.getTransformation();
		Node masterNode = findMasterNode(node);
		applyTransformation(masterNode, transformation);
	}

	private Node findMasterNode(Node node) {
		Node parentNode = node.findParentNode();
		if (node.isStickToParent() == false || parentNode == null) {
			return node;
		} else {
			return findMasterNode(parentNode);
		}
	}

	private void applyTransformation(Node node, NodeTransformation transformation) {
		if (node.getTransformation() == null) {
			NodeTransformation defaultTransformation = new NodeTransformation();
			defaultTransformation.resetToDefaults();
			node.setTransformation(defaultTransformation);
		}
		if (transformation.getPosition() != null) {
			node.getTransformation().setPosition(transformation.getPosition());
		}
		if (transformation.getRotation() != null) {
			node.getTransformation().setRotation(transformation.getRotation());
		}
		if (transformation.getScale() != null) {
			node.getTransformation().setScale(transformation.getScale());
		}
	}

	private void onWorldDistanceUpdateRequest(WorldDistanceUpdateRequest worldDistanceUpdateRequest) {
		try {
			Node distanceNode = findNode(worldDistanceUpdateRequest.getDistanceNodeName());
			Node gridNode = findNode(worldDistanceUpdateRequest.getGridNodeName());
			DistanceDetails distanceDetails = worldDistanceUpdateRequest.getDistanceDetails();
			VectorUtils.updateGridDistance(gridNode, distanceNode, distanceDetails);
		} catch (Exception e) {
			log.catching(this.getAkibotClient(), e);
		}
	}

	@Override
	public void loadDefaults() {
		addTopic(new WorldRequest());
	}

	public Node getWorldNode() {
		return worldNode;
	}

}
