package com.akibot.world.message;

import com.akibot.world.dom.transformation.NodeTransformation3D;

public class WorldNodeTransformationEvent extends WorldRealtimeEvent {
	private static final long serialVersionUID = 3152756094201080086L;

	private String nodeName;
	private NodeTransformation3D transformation;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public NodeTransformation3D getTransformation() {
		return transformation;
	}

	public void setTransformation(NodeTransformation3D transformation) {
		this.transformation = transformation;
	}

}
