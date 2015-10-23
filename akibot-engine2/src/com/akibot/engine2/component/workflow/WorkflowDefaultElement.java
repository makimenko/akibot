package com.akibot.engine2.component.workflow;

import java.io.Serializable;

import com.akibot.engine2.exception.WorkflowException;

public class WorkflowDefaultElement implements WorkflowElement, Serializable {
	private static final long serialVersionUID = 1L;
	private WorkflowElement nextWorkflowElement;

	@Override
	public void setNextWorkflowElement(WorkflowElement nextWorkflowElement) throws WorkflowException {
		this.nextWorkflowElement = nextWorkflowElement;

	}

	@Override
	public WorkflowWait executeElement(WorkflowComponent workflowComponent) throws Exception {
		return null; // By default, nothing to wait
	}

	@Override
	public WorkflowElement getNextWorkflowElement() {
		return this.nextWorkflowElement;
	}

}
