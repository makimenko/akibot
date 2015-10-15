package com.akibot.tanktrack.component.world;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.world.element.Angle;
import com.akibot.tanktrack.component.world.element.ColladaGeometry;
import com.akibot.tanktrack.component.world.element.GridConfiguration;
import com.akibot.tanktrack.component.world.element.GridGeometry;
import com.akibot.tanktrack.component.world.element.Node;
import com.akibot.tanktrack.component.world.element.NodeTransformation;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.DistanceDetails;
import com.akibot.tanktrack.component.world.exception.OutsideWorldException;
import com.akibot.tanktrack.component.world.message.WorldContentRequest;
import com.akibot.tanktrack.component.world.message.WorldContentResponse;
import com.akibot.tanktrack.component.world.message.WorldDistanceUpdateRequest;
import com.akibot.tanktrack.component.world.message.WorldNodeTransformationRequest;
import com.akibot.tanktrack.component.world.message.WorldUpdateRequest;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);
	private Node worldNode;
	private HashMap<String, Node> nodeList = new HashMap<String, Node>();

	String robotNodeName = "robotNode";

	public WorldComponent() {
		initWorld();
	}

	private void initWorld() {
		// ======================== World Node:
		worldNode = new Node("worldNode");
		// AkiBoxGeometry worldGeometry = new AkiBoxGeometry();
		// worldGeometry.setDimension(new AkiPoint(500, 500, 2));

		// AkiMaterial worldMaterial = new AkiMaterial();
		// worldMaterial.setColor(0x00ffff);
		// worldMaterial.setOpacity(0.5f);
		// worldMaterial.setTransparent(true);
		// worldGeometry.setMaterial(worldMaterial);
		// worldNode.setGeometry(worldGeometry);

		index(worldNode);

		// ======================== Grid Node:
		int cellCount = 50;
		int cellSizeCm = 10;
		int positionOffset = cellCount * cellSizeCm / 2;
		GridConfiguration gridConfiguration = new GridConfiguration(cellCount, cellCount, cellSizeCm, 2, new Point(-positionOffset, -positionOffset, 0));
		GridGeometry gridGeometry = new GridGeometry(gridConfiguration);
		Node gridNode = new Node("gridNode");
		gridNode.setGeometry(gridGeometry);

		Angle errorAngle = new Angle();
		errorAngle.setDegrees(15);

		try {
			gridGeometry.addDistance(new DistanceDetails(new Point(positionOffset, positionOffset, 0), new Angle(0), errorAngle, 100, true));
			gridGeometry.addDistance(new DistanceDetails(new Point(positionOffset, positionOffset, 0), new Angle(Math.toRadians(30)), errorAngle, 100, true));
			gridGeometry.addDistance(new DistanceDetails(new Point(positionOffset, positionOffset, 0), new Angle(Math.toRadians(180)), errorAngle, 200, true));
		} catch (OutsideWorldException e) {
			log.catching(this.getAkibotClient(), e);
		}

		worldNode.attachChild(gridNode);

		index(gridNode);

		// ======================== Robot Node:
		ColladaGeometry robotGeometry = new ColladaGeometry();
		robotGeometry.setFileName("../js/loader/AkiBot.dae");
		Node robotNode = new Node(robotNodeName);
		robotNode.setGeometry(robotGeometry);

		NodeTransformation robotTransformation = new NodeTransformation();
		robotTransformation.setPosition(new Point(0, 0, 1.5f));
		robotNode.setTransformation(robotTransformation);

		gridNode.attachChild(robotNode);
		index(robotNode);

		// ======================== Gyroscope
		Node gyroscopeNode = new Node("gyroscopeNode");
		gyroscopeNode.setStickToParent(true);
		index(gyroscopeNode);

		// ======================== Distance
		Node frontDistanceNode = new Node("frontDistanceNode");

		NodeTransformation frontDistanceTransformation = new NodeTransformation();
		frontDistanceTransformation.setPosition(new Point(0, -8f, 5f));
		frontDistanceNode.setTransformation(frontDistanceTransformation);
		frontDistanceNode.setStickToParent(true);
		index(frontDistanceNode);

	}

	public void index(Node node) {
		nodeList.put(node.getName(), node);
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
		Node parentNode = node.getParentNode();
		if (node.isStickToParent() == false || parentNode == null) {
			return node;
		} else {
			return findMasterNode(parentNode);
		}

	}

	private void applyTransformation(Node node, NodeTransformation transformation) {
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
		// TODO: Relative distance: Grid->Distance(front/back)
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
