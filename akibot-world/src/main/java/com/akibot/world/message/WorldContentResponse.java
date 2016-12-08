package com.akibot.world.message;

import com.akibot.world.dom.node.Node;

public class WorldContentResponse extends WorldResponse {
	private static final long serialVersionUID = 1915551651191389749L;

	private Node worldNode;

	public Node getWorldNode() {
		return worldNode;
	}

	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
	}


}
