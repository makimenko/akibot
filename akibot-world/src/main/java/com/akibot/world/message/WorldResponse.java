package com.akibot.world.message;

import com.akibot.common.message.Response;

public class WorldResponse extends Response implements WorldNamedClass {
	private static final long serialVersionUID = -5380864467861050856L;

	// TODO: Make it more elegant?
	private String className;

	public WorldResponse() {
		this.setClassName(this.getClass().getSimpleName());
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return getClassName();
	}

}
