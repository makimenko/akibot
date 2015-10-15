package com.akibot.tanktrack.component.world.element;

public class ColladaGeometry extends NamedClass implements Geometry {
	private static final long serialVersionUID = 1L;

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
