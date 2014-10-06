package com.akibot.kiss.message.status;

import com.akibot.kiss.message.StatusMessage;
import com.akibot.kiss.types.ComponentStatusType;

public class ComponentStatusMessage implements StatusMessage {
	private ComponentStatusType componentStatusType;

	public ComponentStatusType getComponentStatusType() {
		return componentStatusType;
	}

	public void setComponentStatusType(ComponentStatusType componentStatusType) {
		this.componentStatusType = componentStatusType;
	}

		
}
