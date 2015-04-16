package com.akibot.jme3.component.visualizer.utils;

import com.jme3.math.Vector3f;

public class VisualUtils {

	public Vector3f pointToVector3f(AkiPoint akiPoint) {
		return new Vector3f(akiPoint.getX(), akiPoint.getY(), akiPoint.getZ());
	}
}
