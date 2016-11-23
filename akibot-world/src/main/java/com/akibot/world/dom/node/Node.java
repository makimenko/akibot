package com.akibot.world.dom.node;

import java.util.List;

import com.akibot.world.dom.WorldElement;
import com.akibot.world.dom.geometry.Geometry;
import com.akibot.world.dom.transformation.NodeTransformation3D;

public interface Node extends WorldElement {

	String getName();

	Node getParentNode();

	void setParentNode(Node parentNode);

	public void setTransformation(NodeTransformation3D transformation);

	NodeTransformation3D getTransformation();

	List<Node> getChilds();

	public boolean isStickToParent();

	void setStickToParent(boolean stickToParent);

	Geometry getGeometry();

	void setGeometry(Geometry geometry);

}
