package com.akibot.tanktrack.component.world.element;

public class AkiBoxGeometry extends AkiNamedClass implements AkiGeometry {
	private static final long serialVersionUID = 1L;
	private AkiPoint dimension;
	private AkiMaterial material;

	public AkiPoint getDimension() {
		return dimension;
	}

	public void setDimension(AkiPoint dimension) {
		this.dimension = dimension;
	}

	public AkiMaterial getMaterial() {
		return material;
	}

	public void setMaterial(AkiMaterial material) {
		this.material = material;
	}

}
