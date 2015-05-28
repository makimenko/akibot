package com.akibot.jme3.component.visualizer.utils;

import java.io.Serializable;
import java.util.List;

public class AkiNode implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private AkiNode parentNode;
	private List<AkiNode> childs;

	private AkiGeometry geometry;
	private AkiNodeTransformation transformation;

	public AkiNode(String name) {
		this.name = name;
	}

	public AkiNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(AkiNode parentNode) {
		this.parentNode = parentNode;
	}

	public List<AkiNode> getChilds() {
		return childs;
	}

	public void setChilds(List<AkiNode> childs) {
		this.childs = childs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AkiGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(AkiGeometry geometry) {
		this.geometry = geometry;
	}

	public AkiNodeTransformation getTransformation() {
		return transformation;
	}

	public void setTransformation(AkiNodeTransformation transformation) {
		this.transformation = transformation;
	}

}
