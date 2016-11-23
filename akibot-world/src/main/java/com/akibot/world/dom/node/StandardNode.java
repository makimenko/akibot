package com.akibot.world.dom.node;

import java.util.ArrayList;
import java.util.List;

import com.akibot.world.dom.geometry.Geometry;
import com.akibot.world.dom.transformation.NodeTransformation3D;

public class StandardNode implements Node {
	private static final long serialVersionUID = 1L;
	private String name;
	private Node parentNode;
	private List<Node> childs;
	private Geometry geometry;
	private NodeTransformation3D transformation;
	private boolean stickToParent;

	public StandardNode(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Node getParentNode() {
		return parentNode;
	}

	@Override
	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	@Override
	public List<Node> getChilds() {
		return childs;
	}

	public void setChilds(List<Node> childs) {
		this.childs = childs;
	}

	@Override
	public Geometry getGeometry() {
		return geometry;
	}

	@Override
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public NodeTransformation3D getTransformation() {
		return transformation;
	}

	@Override
	public void setTransformation(NodeTransformation3D transformation) {
		this.transformation = transformation;
	}

	@Override
	public boolean isStickToParent() {
		return stickToParent;
	}

	@Override
	public void setStickToParent(boolean stickToParent) {
		this.stickToParent = stickToParent;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer(50);
		buf.append("Node(").append(getName()).append(')');
		return buf.toString();
	}

	@Override
	public void attachChild(final Node childNode) {
		if (childs == null) {
			childs = new ArrayList<Node>();
		}
		childs.add(childNode);
		childNode.setParentNode(parentNode);
	}
}
