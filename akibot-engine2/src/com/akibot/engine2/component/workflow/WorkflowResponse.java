package com.akibot.engine2.component.workflow;

import java.util.HashMap;

import com.akibot.engine2.message.Response;

public class WorkflowResponse extends Response {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Response> responseList = new HashMap<String, Response>();

	public HashMap<String, Response> getResponseList() {
		return responseList;
	}

}
