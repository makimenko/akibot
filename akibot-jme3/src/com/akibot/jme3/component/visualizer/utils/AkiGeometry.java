package com.akibot.jme3.component.visualizer.utils;

import java.io.Serializable;

public class AkiGeometry implements Serializable {

	private static final long serialVersionUID = 1L;

	private AkiPoint dimension;
	private String materialName;

	public AkiPoint getDimension() {
		return dimension;
	}

	public void setDimension(AkiPoint dimension) {
		this.dimension = dimension;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

}
