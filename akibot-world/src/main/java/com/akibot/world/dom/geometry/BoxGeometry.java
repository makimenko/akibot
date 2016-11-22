package com.akibot.world.dom.geometry;

import com.akibot.common.element.Vector3D;
import com.akibot.world.dom.config.Material;

public class BoxGeometry extends BaseGeometry {
	private static final long serialVersionUID = 1L;
	private Vector3D dimension;
	private Material material;

	public Vector3D getDimension() {
		return dimension;
	}

	public void setDimension(final Vector3D dimension) {
		this.dimension = dimension;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(final Material material) {
		this.material = material;
	}

}
