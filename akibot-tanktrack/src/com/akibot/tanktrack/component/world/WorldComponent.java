package com.akibot.tanktrack.component.world;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.world.element.AkiAngle;
import com.akibot.tanktrack.component.world.element.AkiColladaGeometry;
import com.akibot.tanktrack.component.world.element.AkiGridConfiguration;
import com.akibot.tanktrack.component.world.element.AkiGridGeometry;
import com.akibot.tanktrack.component.world.element.AkiNode;
import com.akibot.tanktrack.component.world.element.AkiNodeTransformation;
import com.akibot.tanktrack.component.world.element.AkiPoint;
import com.akibot.tanktrack.component.world.element.DistanceDetails;
import com.akibot.tanktrack.component.world.message.WorldContentRequest;
import com.akibot.tanktrack.component.world.message.WorldContentResponse;
import com.akibot.tanktrack.component.world.message.WorldDistanceUpdateRequest;
import com.akibot.tanktrack.component.world.message.WorldNodeTransformationRequest;
import com.akibot.tanktrack.component.world.message.WorldUpdateRequest;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);
	private AkiNode worldNode;
	private HashMap<String, AkiNode> nodeList = new HashMap<String, AkiNode>();

	String robotNodeName = "robotNode";

	public WorldComponent() {
		initWorld();
	}

	private void initWorld() {
		// ======================== World Node:
		worldNode = new AkiNode("worldNode");
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
		AkiGridConfiguration gridConfiguration = new AkiGridConfiguration(cellCount, cellCount, cellSizeCm, 2);
		AkiGridGeometry gridGeometry = new AkiGridGeometry(gridConfiguration);
		AkiNode gridNode = new AkiNode("gridNode");
		gridNode.setGeometry(gridGeometry);

		AkiNodeTransformation gridTransformation = new AkiNodeTransformation();
		gridTransformation.setPosition(new AkiPoint(-positionOffset, -positionOffset, 1));
		gridNode.setTransformation(gridTransformation);

		AkiAngle errorAngle = new AkiAngle();
		errorAngle.setDegrees(15);

		gridGeometry.addDistance(new DistanceDetails(new AkiPoint(positionOffset, positionOffset, 0), new AkiAngle(0), errorAngle, 100, true));
		gridGeometry.addDistance(new DistanceDetails(new AkiPoint(positionOffset, positionOffset, 0), new AkiAngle(Math.toRadians(30)), errorAngle, 100, true));
		gridGeometry
				.addDistance(new DistanceDetails(new AkiPoint(positionOffset, positionOffset, 0), new AkiAngle(Math.toRadians(180)), errorAngle, 200, true));

		worldNode.attachChild(gridNode);

		index(gridNode);

		// ======================== Robot Node:
		AkiColladaGeometry robotGeometry = new AkiColladaGeometry();
		robotGeometry.setFileName("../js/loader/AkiBot.dae");
		AkiNode robotNode = new AkiNode(robotNodeName);
		robotNode.setGeometry(robotGeometry);

		AkiNodeTransformation robotTransformation = new AkiNodeTransformation();
		robotTransformation.setPosition(new AkiPoint(0, 0, 1.5f));
		robotNode.setTransformation(robotTransformation);

		gridNode.attachChild(robotNode);
		index(robotNode);

		// ======================== Gyroscope
		AkiNode gyroscopeNode = new AkiNode("gyroscopeNode");
		gyroscopeNode.setStickToParent(true);
		index(gyroscopeNode);

		// ======================== Distance
		AkiNode frontDistanceNode = new AkiNode("frontDistanceNode");

		AkiNodeTransformation frontDistanceTransformation = new AkiNodeTransformation();
		frontDistanceTransformation.setPosition(new AkiPoint(0, -8f, 5f));
		frontDistanceNode.setTransformation(frontDistanceTransformation);
		frontDistanceNode.setStickToParent(true);
		index(frontDistanceNode);

	}

	public void index(AkiNode node) {
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
		AkiNode node = nodeList.get(worldNodeTransformationRequest.getNodeName());
		AkiNodeTransformation transformation = worldNodeTransformationRequest.getTransformation();
		AkiNode masterNode = findMasterNode(node);
		applyTransformation(masterNode, transformation);
	}

	private AkiNode findMasterNode(AkiNode node) {
		AkiNode parentNode = node.getParentNode();
		if (node.isStickToParent() == false || parentNode == null) {
			return node;
		} else {
			return findMasterNode(parentNode);
		}

	}

	private void applyTransformation(AkiNode node, AkiNodeTransformation transformation) {
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

	public AkiNode getWorldNode() {
		return worldNode;
	}

}
