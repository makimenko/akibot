package com.akibot.engine2.component.workflow;

import java.io.Serializable;

public class WorkflowDefinition implements Serializable {
	private static final long serialVersionUID = 1L;
	private WorkflowElement startWorkflowElement;
	private long timeoutMilliseconds;

	public WorkflowDefinition(long timeoutMilliseconds) {
		this.timeoutMilliseconds = timeoutMilliseconds;
	}

	public WorkflowElement getStartWorkflowElement() {
		return startWorkflowElement;
	}

	public void setStartWorkflowElement(WorkflowElement startWorkflowElement) {
		this.startWorkflowElement = startWorkflowElement;
	}

	public long getTimeoutMilliseconds() {
		return timeoutMilliseconds;
	}

	public void setTimeoutMilliseconds(long timeoutMilliseconds) {
		this.timeoutMilliseconds = timeoutMilliseconds;
	}

}
