package com.akibot.world.dao;

import java.util.HashMap;
import java.util.Map;

import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.transformation.NodeTransformation3D;

public class WorldContentDaoImpl implements WorldContentDao {
	private Node worldNode;
	// TODO: is it needed?
	private final Map<String, Node> nodeList;

	public WorldContentDaoImpl(Node worldNode) {
		this.nodeList = new HashMap<String, Node>();
		this.worldNode = worldNode;
		indexAllChilds(worldNode);
	}

	private void index(Node node) {
		nodeList.put(node.getName(), node);
	}

	private void indexAllChilds(Node node) {
		index(node);
		if (node.getChilds() != null) {
			for (Node i : node.getChilds()) {
				indexAllChilds(i);
			}
		}
	}

	@Override
	public Node findNode(String name) {
		Node result = nodeList.get(name);
		if (result == null) {
			// Try to re-index in case not found
			indexAllChilds(this.worldNode);
			result = nodeList.get(name);
		}
		return result;
	}

	@Override
	public Node findMasterNode(Node node) {
		if (node == null) {
			return null;
		} else {
			Node parentNode = node.getParentNode();
			if (node.isStickToParent() == false || parentNode == null) {
				return node;
			} else {
				return findMasterNode(parentNode);
			}
		}
	}

	@Override
	public void applyTransformation(Node node, NodeTransformation3D transformation) {
		if (node.getTransformation() == null) {
			NodeTransformation3D defaultTransformation = new NodeTransformation3D();
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

	@Override
	public Node getWorldNode() {
		return worldNode;
	}
}
