package com.akibot.engine2.component.workflow;

import java.io.Serializable;

import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;

public class WorkflowWaitResponse implements WorkflowWait, Serializable {
	private static final long serialVersionUID = 1L;
	private Request request;

	public WorkflowWaitResponse(Request request) {
		this.request = request;
	}

	@Override
	public String getCorrelationId() {
		return requestToCorrelationId(this.request);
	}

	public boolean equalsCorrelationId(Response response) {
		return getCorrelationId().equals(responseToCorrelationId(response));
	}

	public static String requestToCorrelationId(Request request) {
		return request.getTo(); // TODO: Improve uniqueness
	}

	public static String responseToCorrelationId(Response response) {
		return response.getFrom(); // TODO: Improve uniqueness
	}
}
