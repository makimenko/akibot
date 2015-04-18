package com.akibot.jme3.component.visualizer.utils;

import java.util.List;

public class AkiNode {
	private AkiNode parentNode;
	private List<AkiNode> childs;
	private String name;
	private AkiPoint translation;
	private AkiPoint scale;
	private AkiPoint rotation;

	public AkiNode(String name) {
		this.name = name;
	}

}
