package com.akibot.world.dom.node;

import java.util.List;

import com.akibot.world.dom.geometry.NodeGeometry;
import com.akibot.world.dom.transformation.NodeTransformation3D;

public class StandardNode implements Node {
	private static final long serialVersionUID = 1L;
	private String name;
	private Node parentNode;
	private List<Node> childs;
	private NodeGeometry geometry;
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

	public NodeGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(NodeGeometry geometry) {
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

	public void setStickToParent(boolean stickToParent) {
		this.stickToParent = stickToParent;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer(50);
		buf.append("Node(").append(getName()).append(')');
		return buf.toString();
	}
}
