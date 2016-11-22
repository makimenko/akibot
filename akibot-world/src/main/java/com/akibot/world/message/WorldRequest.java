package com.akibot.world.message;

import com.akibot.common.message.Request;

public class WorldRequest extends Request implements WorldNamedClass {
	private static final long serialVersionUID = -2350734972537159293L;
	private String className;

	public WorldRequest() {
		this.setClassName(this.getClass().getSimpleName());
	}

	@Override
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String getClassName() {
		return this.className;
	}

}
