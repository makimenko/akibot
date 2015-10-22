package com.akibot.engine2.component.workflow;

import java.io.Serializable;

import com.akibot.engine2.exception.WorkflowException;

public class WorkflowDefaultElement implements WorkflowElement, Serializable {
	private static final long serialVersionUID = 1L;
	protected WorkflowElement nextWorkflowElement;

	@Override
	public void setNext(WorkflowElement nextWorkflowElement) throws WorkflowException {
		this.nextWorkflowElement = nextWorkflowElement;
		if (nextWorkflowElement instanceof WorkflowJoinElement) {
			((WorkflowJoinElement) nextWorkflowElement).getJoinList().add(this);
		}
	}

	@Override
	public void executeElement() throws Exception {

	}

	@Override
	public void executeNext() throws Exception {
		if (nextWorkflowElement != null) {
			nextWorkflowElement.executeElement();
			if (nextWorkflowElement instanceof WorkflowJoinElement) {
				WorkflowJoinElement workflowJoinElement = (WorkflowJoinElement) nextWorkflowElement;
				if (workflowJoinElement.isReady()) {
					nextWorkflowElement.executeNext();
				}
			} else {
				nextWorkflowElement.executeNext();
			}
		}
	}

}
