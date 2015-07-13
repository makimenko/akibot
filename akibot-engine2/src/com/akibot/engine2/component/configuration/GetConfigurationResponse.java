package com.akibot.engine2.component.configuration;

import java.io.Serializable;

import com.akibot.engine2.message.Response;

public class GetConfigurationResponse extends Response {
	private static final long serialVersionUID = 1L;

	private String name;
	private Serializable value;

	public GetConfigurationResponse() {

	}

	public GetConfigurationResponse(String name, Serializable value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Serializable getValue() {
		return value;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}

}
