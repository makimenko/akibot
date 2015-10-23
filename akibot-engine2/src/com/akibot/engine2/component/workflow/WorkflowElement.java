package com.akibot.engine2.component.workflow;

import com.akibot.engine2.exception.WorkflowException;

public interface WorkflowElement {
	public void setNextWorkflowElement(WorkflowElement nextWorkflowElement) throws WorkflowException;

	public WorkflowWait executeElement(WorkflowComponent workflowComponent) throws Exception;

	public WorkflowElement getNextWorkflowElement();

}
