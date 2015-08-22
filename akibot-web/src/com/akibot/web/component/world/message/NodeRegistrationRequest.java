package com.akibot.web.component.world.message;

import com.akibot.web.component.world.element.AkiNode;

public class NodeRegistrationRequest extends WorldRequest {

	private static final long serialVersionUID = 1L;

	private AkiNode akiNode;

	public NodeRegistrationRequest() {

	}

	public NodeRegistrationRequest(AkiNode akiNode) {
		this.akiNode = akiNode;
	}

	public AkiNode getAkiNode() {
		return akiNode;
	}

	public void setAkiNode(AkiNode akiNode) {
		this.akiNode = akiNode;
	}

}
