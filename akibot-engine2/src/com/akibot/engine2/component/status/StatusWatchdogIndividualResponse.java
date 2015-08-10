package com.akibot.engine2.component.status;

import com.akibot.engine2.component.ComponentStatus;
import com.akibot.engine2.message.Response;

public class StatusWatchdogIndividualResponse extends Response {
	private static final long serialVersionUID = 1L;

	private ComponentStatus componentStatus;

	public ComponentStatus getComponentStatus() {
		return componentStatus;
	}

	public void setComponentStatus(ComponentStatus componentStatus) {
		this.componentStatus = componentStatus;
	}

}
