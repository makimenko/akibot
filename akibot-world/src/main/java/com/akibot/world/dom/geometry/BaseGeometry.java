package com.akibot.world.dom.geometry;

public class BaseGeometry implements Geometry {
	private static final long serialVersionUID = 1L;

	@Override
	public String getClassName() {
		return this.getClass().getSimpleName();
	}

}
