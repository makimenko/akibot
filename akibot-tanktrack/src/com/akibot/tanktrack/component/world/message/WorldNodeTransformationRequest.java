package com.akibot.tanktrack.component.world.message;

import com.akibot.tanktrack.component.world.element.AkiNodeTransformation;

public class WorldNodeTransformationRequest extends WorldUpdateRequest {
	private static final long serialVersionUID = 1L;

	private String nodeName;
	private AkiNodeTransformation transformation;

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
