package com.akibot.kiss.message.response;

import com.akibot.kiss.message.Response;

public class AuthorizationResponse implements Response {
	private String name;
	private String componentClassName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComponentClassName() {
		return componentClassName;
	}

	public void setComponentClassName(String componentClassName) {
		this.componentClassName = componentClassName;
	}

}
