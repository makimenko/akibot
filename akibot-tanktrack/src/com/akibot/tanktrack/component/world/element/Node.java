package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private Node parentNode;
	private List<Node> childs;
	private boolean stickToParent;

	private Geometry geometry;
	private NodeTransformation transformation;

	private boolean castShadow = true;
	private boolean receiveShadow = true;

	public Node(String name) {
		this.name = name;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public List<Node> getChilds() {
		return childs;
	}

	public void setChilds(List<Node> childs) {
		this.childs = childs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public NodeTransformation getTransformation() {
		return transformation;
	}

	public void setTransformation(NodeTransformation transformation) {
		this.transformation = transformation;
	}

	public void attachChild(Node akiNode) {
		if (childs == null) {
			childs = new ArrayList<Node>();
		}
		childs.add(akiNode);
		akiNode.setParentNode(this);

	}

	public boolean isCastShadow() {
		return castShadow;
	}

	public void setCastShadow(boolean castShadow) {
		this.castShadow = castShadow;
	}

	public boolean isReceiveShadow() {
		return receiveShadow;
	}

	public void setReceiveShadow(boolean receiveShadow) {
		this.receiveShadow = receiveShadow;
	}

	public boolean isStickToParent() {
		return stickToParent;
	}

	public void setStickToParent(boolean stickToParent) {
		this.stickToParent = stickToParent;
	}

	public boolean equals(Node node) {
		return (node == this || node.getName().equals(this.name));
	}
}
