package com.akibot.tanktrack.component.world.message;

import java.io.Serializable;

import com.akibot.tanktrack.component.world.element.Node;

public class WorldContent implements Serializable {
	private static final long serialVersionUID = 1L;

	private Node worldNode;

	public Node getWorldNode() {
		return worldNode;
	}

	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
	}

}
