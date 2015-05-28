package com.akibot.jme3.component.visualizer.utils;

import java.io.Serializable;

public class AkiNodeTransformation implements Serializable {

	private static final long serialVersionUID = 1L;

	private AkiPoint translation;
	private AkiPoint scale;
	private AkiPoint rotation;

	private boolean translationChanged = false;
	private boolean scaleChanged = false;
	private boolean rotationChanged = false;

	public AkiPoint getTranslation() {
		return translation;
	}

	public void setTranslation(AkiPoint translation) {
		this.translation = translation;
		translationChanged = true;
	}

	public AkiPoint getScale() {
		return scale;
	}

	public void setScale(AkiPoint scale) {
		this.scale = scale;
		scaleChanged = true;
	}

	public AkiPoint getRotation() {
		return rotation;
	}

	public void setRotation(AkiPoint rotation) {
		this.rotation = rotation;
		rotationChanged = true;
	}

	public boolean isTranslationChanged() {
		return translationChanged;
	}

	public boolean isScaleChanged() {
		return scaleChanged;
	}

	public boolean isRotationChanged() {
		return rotationChanged;
	}

	public boolean isChanged() {
		return rotationChanged && scaleChanged && translationChanged;
	}

}
