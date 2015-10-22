package com.akibot.engine2.component.workflow;

import com.akibot.engine2.exception.WorkflowException;

public interface WorkflowElement {
	public void setNext(WorkflowElement nextWorkflowElement) throws WorkflowException;

	public void executeElement() throws Exception;

	public void executeNext() throws Exception;

}
