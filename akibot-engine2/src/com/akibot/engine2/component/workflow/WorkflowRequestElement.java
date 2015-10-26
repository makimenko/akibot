package com.akibot.engine2.component.workflow;

import com.akibot.engine2.exception.WorkflowException;
import com.akibot.engine2.message.Request;

public class WorkflowRequestElement extends WorkflowDefaultElement {
	private static final long serialVersionUID = 1L;
	private Request request;

	public WorkflowRequestElement(String correlationId, Request request) throws WorkflowException {
		super();
		setCorrelationId(correlationId);
		if (request == null || request.getTo() == null) {
			throw new WorkflowException("Request is incomplete!");
		}
		this.request = request;
	}

	@Override
	public WorkflowWait executeElement(WorkflowComponent workflowComponent) throws Exception {
		WorkflowWaitResponse workflowWaitResponse = new WorkflowWaitResponse(getRequest());
		getRequest().setSyncId(getCorrelationId());
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
