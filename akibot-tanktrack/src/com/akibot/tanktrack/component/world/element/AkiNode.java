package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AkiNode implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private AkiNode parentNode;
	private List<AkiNode> childs;
	private boolean stickToParent;

	private AkiGeometry geometry;
	private AkiNodeTransformation transformation;

	private boolean castShadow = true;
	private boolean receiveShadow = true;

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

	public void attachChild(AkiNode akiNode) {
		if (childs == null) {
			childs = new ArrayList<AkiNode>();
		}
		childs.add(akiNode);

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

	
}
