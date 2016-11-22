package com.akibot.world.dom.geometry;

public class BaseNodeGeometry implements NodeGeometry {
	private static final long serialVersionUID = 1L;
	private String className = this.getClass().getSimpleName();

	@Override
	public String getClassName() {
		return className;
	}

	public void setClassName(final String className) {
		this.className = className;
	}

}
