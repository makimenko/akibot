package com.akibot.tanktrack.component.world.message;

import com.akibot.tanktrack.component.world.element.Node;

public class WorldContentResponse extends WorldResponse {
	private static final long serialVersionUID = 1L;

	private Node worldNode;

	public WorldContentResponse() {
		this.setClassName(WorldContentResponse.class.getSimpleName());
	}

	public Node getWorldNode() {
		return worldNode;
	}

	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
	}

}
