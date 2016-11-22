package com.akibot.world.dao;

import java.util.HashMap;
import java.util.Map;

import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.transformation.NodeTransformation3D;

public class WorldContentManagerDaoImpl implements WorldContentDao {
	private Node worldNode;

	private Map<String, Node> nodeList;

	public WorldContentManagerDaoImpl() {
		this.nodeList = new HashMap<String, Node>();
	}

	public Node getWorldNode() {
		return worldNode;
	}

	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
	}

	public void index(Node node) {
		nodeList.put(node.getName(), node);
	}

	public void indexAllChilds(Node node) {
		index(node);
		if (node.getChilds() != null) {
			for (Node i : node.getChilds()) {
				indexAllChilds(i);
			}
		}
	}

	public void indexRecalculation() {
		indexAllChilds(getWorldNode());
	}

	@Override
	public Node findNode(String name) {
		return nodeList.get(name);
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

}
