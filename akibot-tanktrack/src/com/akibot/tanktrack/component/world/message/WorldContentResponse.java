package com.akibot.tanktrack.component.world.message;

import com.akibot.tanktrack.component.world.element.AkiNode;

public class WorldContentResponse extends WorldResponse {
	private static final long serialVersionUID = 1L;

	private AkiNode worldNode;

	public WorldContentResponse() {
		this.setClassName(WorldContentResponse.class.getSimpleName());
	}

	public AkiNode getWorldNode() {
		return worldNode;
	}

	public void setWorldNode(AkiNode worldNode) {
		this.worldNode = worldNode;
	}

}
