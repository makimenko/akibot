package com.akibot.jme3.component.visualizer;

import com.akibot.engine2.message.Request;
import com.akibot.jme3.component.visualizer.utils.AkiPoint;

public class VisualizerRequest extends Request {
	private static final long serialVersionUID = 1L;

	private AkiPoint akiPoint;

	public AkiPoint getAkiPoint() {
		return akiPoint;
	}

	public void setAkiPoint(AkiPoint akiPoint) {
		this.akiPoint = akiPoint;
	}

}
