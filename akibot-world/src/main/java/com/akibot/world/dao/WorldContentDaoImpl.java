package com.akibot.world.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.transformation.NodeTransformation3D;

public class WorldContentDaoImpl implements WorldContentDao {
	private Node worldNode;
	private Map<String, Node> nodeList;

	public WorldContentDaoImpl() {
		this.nodeList = new HashMap<String, Node>();
	}

	// TODO: is it needed?
	private void index(Node node) {
		nodeList.put(node.getName(), node);
	}

	// TODO: is it needed?
	private void indexAllChilds(Node node) {
		index(node);
		if (node.getChilds() != null) {
			for (Node i : node.getChilds()) {
				indexAllChilds(i);
			}
		}
	}

	@Override
	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
		indexAllChilds(worldNode);
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


	public void attachChild(final Node parentNode, final Node childNode) {
		List<Node> childs = parentNode.getChilds();
		if (childs == null) {
			childs = new ArrayList<Node>();
		}
		childs.add(childNode);
		childNode.setParentNode(parentNode);
	}
	
}
