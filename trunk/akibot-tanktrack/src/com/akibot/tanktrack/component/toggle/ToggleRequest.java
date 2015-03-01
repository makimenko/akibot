package com.akibot.tanktrack.component.toggle;

import com.akibot.engine2.message.Request;

public class ToggleRequest extends Request {
	private static final long serialVersionUID = 1L;
	private ToggleType type;

	public ToggleType getType() {
		return type;
	}

	public void setType(ToggleType type) {
		this.type = type;
	}

}
