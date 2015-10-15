package com.akibot.tanktrack.component.world;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.world.element.Angle;
import com.akibot.tanktrack.component.world.element.ColladaGeometry;
import com.akibot.tanktrack.component.world.element.DistanceDetails;
import com.akibot.tanktrack.component.world.element.GridConfiguration;
import com.akibot.tanktrack.component.world.element.GridGeometry;
import com.akibot.tanktrack.component.world.element.Node;
import com.akibot.tanktrack.component.world.element.NodeTransformation;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.VectorUtils;
import com.akibot.tanktrack.component.world.message.WorldContentRequest;
import com.akibot.tanktrack.component.world.message.WorldContentResponse;
import com.akibot.tanktrack.component.world.message.WorldDistanceUpdateRequest;
import com.akibot.tanktrack.component.world.message.WorldNodeTransformationRequest;
import com.akibot.tanktrack.component.world.message.WorldUpdateRequest;
import com.akibot.tanktrack.launcher.Constants;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);
	private Node worldNode;
	private HashMap<String, Node> nodeList = new HashMap<String, Node>();

	public WorldComponent() {
		initWorld();
	}

	private void initWorld() {
		// ======================== World Node:
		worldNode = new Node("worldNode");
		index(worldNode);

		// ======================== Grid Node:
		int cellCount = 50;
		int cellSizeCm = 10;
		int positionOffset = cellCount * cellSizeCm / 2;
		GridConfiguration gridConfiguration = new GridConfiguration(cellCount, cellCount, cellSizeCm, 2, new Point(-positionOffset, -positionOffset, 0));
		GridGeometry gridGeometry = new GridGeometry(gridConfiguration);
		Node gridNode = new Node(Constants.NODE_NAME_GRID);
		gridNode.setGeometry(gridGeometry);
		worldNode.attachChild(gridNode);
		index(gridNode);

		// ======================== Robot Node:
		ColladaGeometry robotGeometry = new ColladaGeometry();
		robotGeometry.setFileName("../js/loader/AkiBot.dae");
		Node robotNode = new Node(Constants.NODE_NAME_ROBOT);
		robotNode.setGeometry(robotGeometry);

		// NodeTransformation robotTransformation = new NodeTransformation();
		// robotTransformation.setPosition(new Point(10, 0, 1.5f));
		// robotTransformation.setRotation(new Point(0, 0, VectorUtils.gradToRad(45)));
		// robotNode.setTransformation(robotTransformation);

		gridNode.attachChild(robotNode);
		index(robotNode);

		// ======================== Gyroscope
		Node gyroscopeNode = new Node(Constants.COMPONENT_NAME_AKIBOT_GYROSCOPE);
		gyroscopeNode.setStickToParent(true);
		robotNode.attachChild(gyroscopeNode);
		index(gyroscopeNode);

		// ======================== frontDistanceNode
		Node frontDistanceNode = new Node(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT);
		NodeTransformation frontDistanceTransformation = new NodeTransformation();
		frontDistanceTransformation.setPosition(new Point(0, 8f, 5f));
		frontDistanceNode.setTransformation(frontDistanceTransformation);
		frontDistanceNode.setStickToParent(true);
		robotNode.attachChild(frontDistanceNode);
		index(frontDistanceNode);

		// ======================== backDistanceNode
		Node backDistanceNode = new Node(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK);
		NodeTransformation backDistanceTransformation = new NodeTransformation();
		backDistanceTransformation.setPosition(new Point(0, -8f, 5f));
		backDistanceTransformation.setRotation(new Point(0, 0, VectorUtils.gradToRad(180)));
		backDistanceNode.setTransformation(backDistanceTransformation);
		backDistanceNode.setStickToParent(true);
		robotNode.attachChild(backDistanceNode);
		index(backDistanceNode);

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
		distance = new DistanceDetails(new Point(0, 0, 0), new Angle(0), Constants.DISTANCE_ERRROR_ANGLE, 100, true);
		worldDistanceUpdateRequest.setDistanceDetails(distance);
		onWorldUpdateRequest(worldDistanceUpdateRequest);

		// back:
		worldDistanceUpdateRequest.setDistanceNodeName(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK);
		distance = new DistanceDetails(new Point(0, 0, 0), new Angle(0), Constants.DISTANCE_ERRROR_ANGLE, 50, true);
		worldDistanceUpdateRequest.setDistanceDetails(distance);
		onWorldUpdateRequest(worldDistanceUpdateRequest);
	}

	public void index(Node node) {
		nodeList.put(node.getName(), node);
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
		worldContentResponse.setWorldNode(worldNode);
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

	//
	// private void onGyroscopeResponse(GyroscopeResponse gyroscopeResponse) throws FailedToSendMessageException {
	// AkiNode robotNode = nodeList.get(robotNodeName);
	//
	// AkiPoint rotation = new AkiPoint(0f, 0f, gradToRad(gyroscopeResponse.getNorthDegreesXY()));
	// robotNode.getTransformation().setRotation(rotation);
	//
	// AkiNodeTransformation transformation = new AkiNodeTransformation();
	// ___DEL___NodeTransformationMessage nodeTransformationMessage = new ___DEL___NodeTransformationMessage();
	// nodeTransformationMessage.setNodeName(robotNodeName);
	// nodeTransformationMessage.setTransformation(transformation);
	// transformation.setRotation(rotation);
	//
	// this.broadcastMessage(nodeTransformationMessage);
	// }
	//
	//

	@Override
	public void loadDefaults() {
		addTopic(new WorldContentRequest());
		addTopic(new GyroscopeResponse());
		getComponentStatus().setReady(true);
	}

	public Node getWorldNode() {
		return worldNode;
	}

}
