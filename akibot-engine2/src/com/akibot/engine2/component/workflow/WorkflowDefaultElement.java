package com.akibot.engine2.component.workflow;

import java.io.Serializable;

import com.akibot.engine2.exception.WorkflowException;

public class WorkflowDefaultElement implements WorkflowElement, Serializable {
	private static final long serialVersionUID = 1L;
	private WorkflowElement nextWorkflowElement;
	private String correlationId;

	@Override
	public void setNextWorkflowElement(WorkflowElement nextWorkflowElement) throws WorkflowException {
		this.nextWorkflowElement = nextWorkflowElement;
		if (nextWorkflowElement instanceof WorkflowJoinElement) {
			WorkflowJoinElement join = (WorkflowJoinElement) nextWorkflowElement;
			join.addJoin(nextWorkflowElement);
		}
	}

	@Override
	public WorkflowWait executeElement(WorkflowComponent workflowComponent) throws Exception {
		return null; // By default, nothing to wait
	}

	@Override
	public WorkflowElement getNextWorkflowElement() throws WorkflowException {
		return this.nextWorkflowElement;
	}

	@Override
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	@Override
	public String getCorrelationId() {
		return correlationId;
	}

}
