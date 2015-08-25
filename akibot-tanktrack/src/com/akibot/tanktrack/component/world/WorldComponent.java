package com.akibot.tanktrack.component.world;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.world.element.AkiGeometry;
import com.akibot.tanktrack.component.world.element.AkiMaterial;
import com.akibot.tanktrack.component.world.element.AkiNode;
import com.akibot.tanktrack.component.world.element.AkiNodeTransformation;
import com.akibot.tanktrack.component.world.element.AkiPoint;
import com.akibot.tanktrack.component.world.message.WorldContentRequest;
import com.akibot.tanktrack.component.world.message.WorldContentResponse;
import com.akibot.tanktrack.component.world.message.WorldRequest;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);
	private AkiNode worldNode;
	private HashMap<String, AkiNode> nodeList = new HashMap<String, AkiNode>();

	public WorldComponent() {

		// ======================== World Node:
		worldNode = new AkiNode("worldNode");
		AkiGeometry worldGeometry = new AkiGeometry();
		worldGeometry.setDimension(new AkiPoint(500, 500, 5));
		worldNode.setGeometry(worldGeometry);

		AkiMaterial worldMaterial = new AkiMaterial();
		worldMaterial.setColor(0x00ffff);
		worldMaterial.setOpacity(0.5f);
		worldMaterial.setTransparent(true);
		worldNode.setMaterial(worldMaterial);

		index(worldNode);

		// ======================== Robot Node:
		AkiGeometry robotGeometry = new AkiGeometry();
		robotGeometry.setDimension(new AkiPoint(10, 10, 10));
		AkiNode robotNode = new AkiNode("robotNode");
		robotNode.setGeometry(robotGeometry);

		AkiNodeTransformation robotTransformation = new AkiNodeTransformation();
		robotTransformation.setPosition(new AkiPoint(0, 0, 15));
		// robotTransformation.setRotation(new AkiPoint(1, 1, 1));
		robotTransformation.setScale(new AkiPoint(1, 1, 1));
		robotNode.setTransformation(robotTransformation);

		AkiMaterial robotMaterial = new AkiMaterial();
		robotMaterial.setColor(0xff0000);
		robotMaterial.setOpacity(0.8f);
		robotMaterial.setTransparent(true);		
		robotNode.setMaterial(robotMaterial);

		worldNode.attachChild(robotNode);

		index(robotNode);

	}

	public void index(AkiNode node) {
		nodeList.put(node.getName(), node);
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof WorldRequest) {
			if (message instanceof WorldContentRequest) {
				WorldContentRequest worldContentRequest = (WorldContentRequest) message;
				WorldContentResponse worldContentResponse = new WorldContentResponse();
				worldContentResponse.setWorldNode(worldNode);
				this.broadcastResponse(worldContentResponse, worldContentRequest);
			}
		}
	}

	@Override
	public void loadDefaults() {
		addTopic(new WorldContentRequest());
		getComponentStatus().setReady(true);
	}

}
