package com.akibot.tanktrack.world;

import java.util.ArrayList;

public class Node {
	private Vector3d position;
	private Vector3d scale;
	private Vector3d rotation;
	private String name;

	private Node parent;
	private ArrayList<Node> childs;

	public Vector3d getPosition() {
		return position;
	}

	public void setPosition(Vector3d position) {
		this.position = position;
	}

	public Vector3d getScale() {
		return scale;
	}

	public void setScale(Vector3d scale) {
		this.scale = scale;
	}

	public Vector3d getRotation() {
		return rotation;
	}

	public void setRotation(Vector3d rotation) {
		this.rotation = rotation;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public ArrayList<Node> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<Node> childs) {
		this.childs = childs;
	}

	public Node(String name) {
		setName(name);
		position = new Vector3d();
		scale = new Vector3d();
		rotation = new Vector3d();
		childs = new ArrayList<Node>();
	}

	public void attachChild(Node node) {
		childs.add(node);
		node.setParent(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
