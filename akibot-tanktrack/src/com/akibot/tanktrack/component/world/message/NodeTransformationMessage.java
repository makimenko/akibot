package com.akibot.tanktrack.component.world.message;

import com.akibot.jme3.component.visualizer.utils.AkiNodeTransformation;

public class NodeTransformationMessage extends WorldContentResponse {
	private static final long serialVersionUID = 1L;
	private String nodeName;
	private AkiNodeTransformation transformation;

	public NodeTransformationMessage() {
		this.setClassName(NodeTransformationMessage.class.getSimpleName());
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public AkiNodeTransformation getTransformation() {
		return transformation;
	}

	public void setTransformation(AkiNodeTransformation transformation) {
		this.transformation = transformation;
	}

}
