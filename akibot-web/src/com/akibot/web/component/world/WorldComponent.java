package com.akibot.web.component.world;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.web.component.world.element.AkiNode;
import com.akibot.web.component.world.message.NodeRegistrationRequest;
import com.akibot.web.component.world.message.WorldRequest;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);
	private AkiNode baseNode = new AkiNode("baseNode");
	private HashMap<String, AkiNode> nodeList = new HashMap<String, AkiNode>();

	public WorldComponent() {

	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof WorldRequest) {

		}

	}

	private void nodeRegistration(NodeRegistrationRequest nodeRegistrationRequest) {
		AkiNode akiNode = nodeRegistrationRequest.getAkiNode();
		AkiNode findNode = nodeList.get(akiNode.getName());

		if (findNode == null) { // If not exists
			if (akiNode.getParentNode() == null) {
				baseNode.attachChild(akiNode);
			} else {
				AkiNode findParentNode = nodeList.get(akiNode.getParentNode().getName());
				if (findParentNode != null) {
					findParentNode.attachChild(akiNode);
				} else {
					log.warn("Node [" + akiNode.getName() + "] can't find parent node [" + akiNode.getParentNode() + "] not found!");
				}
			}
			nodeList.put(akiNode.getName(), akiNode);
			// nodeTransformation(node, akiNode.getTransformation());
			// nodeGeometry(node, akiNode);
		}
	}

}
