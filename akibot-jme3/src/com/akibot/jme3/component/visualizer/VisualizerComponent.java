package com.akibot.jme3.component.visualizer;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.jme3.component.visualizer.utils.AkiGeometry;
import com.akibot.jme3.component.visualizer.utils.AkiNode;
import com.akibot.jme3.component.visualizer.utils.AkiNodeTransformation;
import com.akibot.jme3.component.visualizer.utils.AkiPoint;
import com.akibot.jme3.component.visualizer.utils.VisualUtils;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

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
			nodeTransformation((NodeTransformationRequest) message);
		}
	}

	public void nodeRegistrarion(NodeRegistrationRequest nodeRegistrationRequest) {
		AkiNode akiNode = nodeRegistrationRequest.getAkiNode();
		Node findNode = nodeList.get(akiNode.getName());
		if (findNode == null) {
			Node node = visualUtils.akiNodeToNode(akiNode);
			if (akiNode.getParentNode() == null) {
				visualizerWindow.getBaseNode().attachChild(node);
			} else {
				Node findParentNode = nodeList.get(akiNode.getParentNode());
				if (findParentNode != null) {
					findParentNode.attachChild(node);
				} else {
					log.warn("Node [" + akiNode.getName() + "] can't find parent node [" + akiNode.getParentNode() + "] not found!");
				}
			}
			nodeList.put(akiNode.getName(), node);
			nodeTransformation(node, akiNode, akiNode.getTransformation());
			nodeGeometry(node, akiNode);
		}
	}

	public void nodeTransformation(NodeTransformationRequest nodeTransformationRequest) {
		AkiNode akiNode = nodeTransformationRequest.getAkiNode();
		AkiNodeTransformation akiNodeTransformation = akiNode.getTransformation();
		Node findNode = nodeList.get(akiNode.getName());
		nodeTransformation(findNode, akiNode, akiNodeTransformation);
	}

	public void nodeTransformation(Node node, AkiNode akiNode, AkiNodeTransformation akiNodeTransformation) {
		if (node != null && akiNodeTransformation != null && akiNodeTransformation.isChanged()) {
			if (akiNodeTransformation.isRotationChanged()) {
				// TODO: validate
				node.rotateUpTo(visualUtils.pointToVector3f(akiNodeTransformation.getRotation()));
			}

			if (akiNodeTransformation.isScaleChanged()) {
				// TODO: validate
				node.setLocalScale(visualUtils.pointToVector3f(akiNodeTransformation.getScale()));
			}

			if (akiNodeTransformation.isTranslationChanged()) {
				// TODO: validate
				node.setLocalTranslation(visualUtils.pointToVector3f(akiNodeTransformation.getTranslation()));
			}
		}
	}

	public void nodeGeometry(Node node, AkiNode akiNode) {
		AkiGeometry akiGeometry = akiNode.getGeometry();

		if (akiGeometry != null) {
			AkiPoint dimension = akiNode.getGeometry().getDimension();
			if (dimension != null) {
				Box box = new Box(dimension.getX(), dimension.getY(), dimension.getZ());
				Geometry geometry = new Geometry(akiNode.getName() + " geometry", box);
				// geometry.setLocalTranslation(new Vector3f(1,-1,1));
				String materialName = (akiNode.getGeometry().getMaterialName() != null ? akiNode.getGeometry().getMaterialName() : "red");

				geometry.setMaterial(visualizerWindow.getMaterialStorage().getMaterial(materialName));
				node.attachChild(geometry);
			}
		}

	}

}
