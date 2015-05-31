package com.akibot.jme3.component.visualizer;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.jme3.component.message.NodeRegistrationRequest;
import com.akibot.jme3.component.message.NodeTransformationRequest;
import com.akibot.jme3.component.visualizer.utils.AkiNode;
import com.akibot.jme3.component.visualizer.utils.AkiNodeTransformation;
import com.akibot.jme3.component.visualizer.utils.VisualUtils;
import com.jme3.scene.Node;

public class VisualizerComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(VisualizerComponent.class);

	private VisualizerWindow visualizerWindow;
	private VisualUtils visualUtils;
	private HashMap<String, Node> nodeList = new HashMap<String, Node>();

	public VisualizerComponent(VisualizerWindow visualizerWindow) {
		this.visualizerWindow = visualizerWindow;
		this.visualUtils = new VisualUtils();
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof NodeRegistrationRequest) {
			nodeRegistrarion((NodeRegistrationRequest) message);
		} else if (message instanceof NodeTransformationRequest) {
			NodeTransformationRequest nodeTransformationRequest = (NodeTransformationRequest) message;
			AkiNode akiNode = nodeTransformationRequest.getAkiNode();
			AkiNodeTransformation akiNodeTransformation = akiNode.getTransformation();
			Node findNode = nodeList.get(akiNode.getName());
			visualizerWindow.nodeTransformation(findNode, akiNodeTransformation);
		}
	}

	public void nodeRegistrarion(NodeRegistrationRequest nodeRegistrationRequest) {
		AkiNode akiNode = nodeRegistrationRequest.getAkiNode();
		Node findNode = nodeList.get(akiNode.getName());
		if (findNode == null) {
			Node node = visualUtils.akiNodeToNode(akiNode);
			if (akiNode.getParentNode() == null) {
				visualizerWindow.addNode(visualizerWindow.getBaseNode(), node);
			} else {
				Node findParentNode = nodeList.get(akiNode.getParentNode().getName());
				if (findParentNode != null) {
					visualizerWindow.addNode(findParentNode, node);
				} else {
					log.warn("Node [" + akiNode.getName() + "] can't find parent node [" + akiNode.getParentNode() + "] not found!");
				}
			}
			nodeList.put(akiNode.getName(), node);
			visualizerWindow.nodeTransformation(node, akiNode.getTransformation());
			visualizerWindow.nodeGeometry(node, akiNode);
		}
	}

}
