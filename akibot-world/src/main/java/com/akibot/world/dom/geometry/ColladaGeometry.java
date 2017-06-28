package com.akibot.world.dom.geometry;

public class ColladaGeometry extends BaseGeometry {
	private static final long serialVersionUID = 1L;
	private String fileName;

	public ColladaGeometry() {
		super();
	}

	public ColladaGeometry(String fileName) {
		super();
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
