package com.akibot.engine2.component.workflow;

import java.io.Serializable;

public class WorkflowDefinition implements Serializable {
	private static final long serialVersionUID = 1L;
	private WorkflowElement startWorkflowElement;

	public void setStart(WorkflowElement startWorkflowElement) {
		this.startWorkflowElement = startWorkflowElement;
	}

	public void executeWorkflow() throws Exception {
		startWorkflowElement.executeElement();
		startWorkflowElement.executeNext();
	}

}
