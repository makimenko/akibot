package com.akibot.tanktrack.component.world;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.world.element.AkiGeometry;
import com.akibot.tanktrack.component.world.element.AkiNode;
import com.akibot.tanktrack.component.world.element.AkiPoint;
import com.akibot.tanktrack.component.world.message.WorldContentRequest;
import com.akibot.tanktrack.component.world.message.WorldContentResponse;
import com.akibot.tanktrack.component.world.message.WorldRequest;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);
	private AkiNode worldNode;
	private HashMap<String, AkiNode> nodeList = new HashMap<String, AkiNode>();

	public WorldComponent() {

		worldNode = new AkiNode("worldNode");
		AkiGeometry baseNodeGeometry = new AkiGeometry();
		baseNodeGeometry.setDimension(new AkiPoint(500, 500, 10));
		worldNode.setGeometry(baseNodeGeometry);
		monitorNode(worldNode);

		AkiGeometry robotNodeGeometry = new AkiGeometry();
		robotNodeGeometry.setDimension(new AkiPoint(50, 50, 50));
		AkiNode robotNode = new AkiNode("robotNode");
		robotNode.setGeometry(robotNodeGeometry);
		worldNode.attachChild(robotNode);
		monitorNode(robotNode);

	}

	public void monitorNode(AkiNode node) {
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
