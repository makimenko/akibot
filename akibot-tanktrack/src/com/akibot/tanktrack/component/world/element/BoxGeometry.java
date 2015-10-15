package com.akibot.tanktrack.component.world.element;

public class BoxGeometry extends NamedClass implements Geometry {
	private static final long serialVersionUID = 1L;
	private Point dimension;
	private Material material;

	public Point getDimension() {
		return dimension;
	}

	public void setDimension(Point dimension) {
		this.dimension = dimension;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

}
