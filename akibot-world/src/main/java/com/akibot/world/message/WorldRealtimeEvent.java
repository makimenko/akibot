package com.akibot.world.message;

import com.akibot.common.message.RealtimeEvent;

public class WorldRealtimeEvent extends RealtimeEvent implements WorldNamedClass {
	private static final long serialVersionUID = -7751564908905778289L;
	private String className;

	public WorldRealtimeEvent() {
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
