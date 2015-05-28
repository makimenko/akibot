package com.akibot.jme3.component.visualizer;

import com.akibot.jme3.component.visualizer.utils.AkiNode;

public class NodeRegistrationRequest extends VisualizerRequest {

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
