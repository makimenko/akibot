package com.akibot.world.dao;

import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.transformation.NodeTransformation3D;

public interface WorldContentDao {

	void setWorldNode(Node worldNode);
	
	Node findNode(String name);

	Node findMasterNode(Node node);

	void applyTransformation(Node node, NodeTransformation3D transformation);

	void attachChild(final Node parentNode, final Node childNode);
	
}
