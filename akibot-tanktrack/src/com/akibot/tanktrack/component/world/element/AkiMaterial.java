package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class AkiMaterial implements Serializable {
	private static final long serialVersionUID = 1L;

	private int color = 0x0F0F0F;
	private int shading = ThreeConstants.THREE_FlatShading;
	private float opacity = 1;
	private boolean transparent = false;
	private boolean wireframe = false;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getShading() {
		return shading;
	}

	public void setShading(int shading) {
		this.shading = shading;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public boolean isWireframe() {
		return wireframe;
	}

	public void setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
	}

}
