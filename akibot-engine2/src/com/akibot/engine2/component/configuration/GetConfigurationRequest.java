package com.akibot.engine2.component.configuration;

import com.akibot.engine2.message.Request;

public class GetConfigurationRequest extends Request {
	private static final long serialVersionUID = 1L;

	private String name;

	public GetConfigurationRequest() {
	}

	public GetConfigurationRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
