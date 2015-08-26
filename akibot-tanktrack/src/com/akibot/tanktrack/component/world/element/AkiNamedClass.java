package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class AkiNamedClass implements Serializable {
	private static final long serialVersionUID = 1L;
	private String className = this.getClass().getSimpleName();

	public String getClassName() {
		return className;
	}

}
