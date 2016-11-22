package com.akibot.world.dom.config;

public class Material implements Configuration {
	private static final long serialVersionUID = 1L;
	private int color;
	private int shading;
	private float opacity;
	private boolean transparent;
	private boolean wireframe;

	public Material() {
		this.transparent = false;
		this.wireframe = false;
		this.shading = ThreeMaterialConstants.THREE_FLAT_SHADING;
		this.color = 0x0F0F0F;
		this.opacity = 1;
	}

	public int getColor() {
		return color;
	}

	public void setColor(final int color) {
		this.color = color;
	}

	public int getShading() {
		return shading;
	}

	public void setShading(final int shading) {
		this.shading = shading;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(final float opacity) {
		this.opacity = opacity;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(final boolean transparent) {
		this.transparent = transparent;
	}

	public boolean isWireframe() {
		return wireframe;
	}

	public void setWireframe(final boolean wireframe) {
		this.wireframe = wireframe;
	}

}
