package com.akibot.tanktrack.component.world.message;

import com.akibot.tanktrack.component.world.element.AkiNode;

public class NodeTransformationRequest extends WorldRequest {

	private static final long serialVersionUID = 1L;

	private AkiNode akiNode;

	public NodeTransformationRequest(AkiNode akiNode) {
		this.akiNode = akiNode;
	}

	public AkiNode getAkiNode() {
		return akiNode;
	}

	public void setAkiNode(AkiNode akiNode) {
		this.akiNode = akiNode;
	}

}
