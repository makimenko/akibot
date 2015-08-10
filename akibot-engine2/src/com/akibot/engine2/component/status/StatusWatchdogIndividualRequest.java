package com.akibot.engine2.component.status;

import com.akibot.engine2.message.Request;

public class StatusWatchdogIndividualRequest extends Request {
	private static final long serialVersionUID = 1L;

	private String componentName;

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

}
