package com.akibot.engine2.component.workflow;

import com.akibot.engine2.exception.WorkflowException;
import com.akibot.engine2.message.Request;

public class WorkflowSyncRequestElement extends WorkflowDefaultElement {
	private static final long serialVersionUID = 1L;
	private Request request;

	public WorkflowSyncRequestElement(Request request) throws WorkflowException {
		if (request == null || request.getTo() == null) {
			throw new WorkflowException("Request is incomplete!");
		}
		this.request = request;
	}

	@Override
	public WorkflowWait executeElement(WorkflowComponent workflowComponent) throws Exception {
		WorkflowWaitResponse workflowWaitResponse = new WorkflowWaitResponse(getRequest());
		workflowComponent.broadcastMessage(getRequest());
		return workflowWaitResponse;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

}
