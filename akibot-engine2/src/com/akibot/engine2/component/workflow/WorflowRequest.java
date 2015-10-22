package com.akibot.engine2.component.workflow;

import com.akibot.engine2.message.Request;

public class WorflowRequest extends Request {
	private static final long serialVersionUID = 1L;
	private WorkflowDefinition worflowDefinition;

	public WorkflowDefinition getWorflowDefinition() {
		return worflowDefinition;
	}

	public void setWorflowDefinition(WorkflowDefinition worflowDefinition) {
		this.worflowDefinition = worflowDefinition;
	}

}
