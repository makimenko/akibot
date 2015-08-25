package com.akibot.tanktrack.component.world.message;

import com.akibot.engine2.message.Response;

public class WorldResponse extends Response {
	private static final long serialVersionUID = 1L;

	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
