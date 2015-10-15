package com.akibot.tanktrack.component.world.message;

import com.akibot.tanktrack.component.world.element.NodeTransformation;

public class WorldNodeTransformationRequest extends WorldUpdateRequest {
	private static final long serialVersionUID = 1L;

	private String nodeName;
	private NodeTransformation transformation;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public NodeTransformation getTransformation() {
		return transformation;
	}

	public void setTransformation(NodeTransformation transformation) {
		this.transformation = transformation;
	}

}
