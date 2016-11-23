package com.akibot.world.dao;

import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.transformation.NodeTransformation3D;

public interface WorldContentDao {

	Node findNode(String name);

	Node findMasterNode(Node node);

	void applyTransformation(Node node, NodeTransformation3D transformation);

}
