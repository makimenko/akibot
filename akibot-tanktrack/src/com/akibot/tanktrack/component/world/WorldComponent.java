package com.akibot.tanktrack.component.world;

import java.util.HashMap;
import java.util.Timer;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.world.element.AkiBoxGeometry;
import com.akibot.tanktrack.component.world.element.AkiColladaGeometry;
import com.akibot.tanktrack.component.world.element.AkiMaterial;
import com.akibot.tanktrack.component.world.element.AkiNode;
import com.akibot.tanktrack.component.world.element.AkiNodeTransformation;
import com.akibot.tanktrack.component.world.element.AkiPoint;
import com.akibot.tanktrack.component.world.message.NodeTransformationMessage;
import com.akibot.tanktrack.component.world.message.WorldContentRequest;
import com.akibot.tanktrack.component.world.message.WorldContentResponse;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);
	private AkiNode worldNode;
	private HashMap<String, AkiNode> nodeList = new HashMap<String, AkiNode>();
	private Timer timer;
	String robotNodeName = "robotNode";

	public WorldComponent() {
		initWorld();
		scheduleWorldPoolingTask(1 * 1000, 1 * 1000);
	}

	private void scheduleWorldPoolingTask(int delay, int period) {
		timer = new Timer();
		timer.schedule(new WorldPoolingTask(this), delay, period);
	}

	private void initWorld() {
		// ======================== World Node:
		worldNode = new AkiNode("worldNode");
		AkiBoxGeometry worldGeometry = new AkiBoxGeometry();
		worldGeometry.setDimension(new AkiPoint(500, 500, 2));

		AkiMaterial worldMaterial = new AkiMaterial();
		worldMaterial.setColor(0x00ffff);
		worldMaterial.setOpacity(0.5f);
		worldMaterial.setTransparent(true);
		worldGeometry.setMaterial(worldMaterial);
		worldNode.setGeometry(worldGeometry);

		index(worldNode);

		// ======================== Robot Node:
		AkiColladaGeometry robotGeometry = new AkiColladaGeometry();
		robotGeometry.setFileName("../js/loader/AkiBot.dae");
		AkiNode robotNode = new AkiNode(robotNodeName);
		robotNode.setGeometry(robotGeometry);

		AkiNodeTransformation robotTransformation = new AkiNodeTransformation();
		robotTransformation.setPosition(new AkiPoint(0, 0, 1));
		// robotTransformation.setRotation(new AkiPoint(1, 1, 1));
		// robotTransformation.setScale(new AkiPoint(100, 100, 100));
		robotNode.setTransformation(robotTransformation);

		worldNode.attachChild(robotNode);

		index(robotNode);
	}

	public void index(AkiNode node) {
		nodeList.put(node.getName(), node);
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof WorldContentRequest) {
			onWorldContentRequest((WorldContentRequest) message);
		} else if (message instanceof GyroscopeResponse) {
			onGyroscopeResponse((GyroscopeResponse) message);
		}
	}

	private void onWorldContentRequest(WorldContentRequest worldContentRequest) throws FailedToSendMessageException {
		WorldContentResponse worldContentResponse = new WorldContentResponse();
		worldContentResponse.setWorldNode(worldNode);
		this.broadcastResponse(worldContentResponse, worldContentRequest);
	}

	private void onGyroscopeResponse(GyroscopeResponse gyroscopeResponse) throws FailedToSendMessageException {
		AkiNode robotNode = nodeList.get(robotNodeName);

		AkiPoint rotation = new AkiPoint(0f, 0f, gradToRad(gyroscopeResponse.getNorthDegreesXY()));
		robotNode.getTransformation().setRotation(rotation);

		AkiNodeTransformation transformation = new AkiNodeTransformation();
		NodeTransformationMessage nodeTransformationMessage = new NodeTransformationMessage();
		nodeTransformationMessage.setNodeName(robotNodeName);
		nodeTransformationMessage.setTransformation(transformation);
		transformation.setRotation(rotation);

		this.broadcastMessage(nodeTransformationMessage);
	}

	private float gradToRad(double rad) {
		return (float) (-rad * Math.PI / 180);
	}

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
