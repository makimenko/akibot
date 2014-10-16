package com.akibot.kiss.message.response;

import com.akibot.kiss.message.Response;
import com.akibot.kiss.types.ComponentStatusType;

public class ComponentStatusResponse extends Response {
	private ComponentStatusType componentStatusType;

	public ComponentStatusType getComponentStatusType() {
		return componentStatusType;
	}

	public void setComponentStatusType(ComponentStatusType componentStatusType) {
		this.componentStatusType = componentStatusType;
	}

}
