package com.akibot.engine2.component.configuration;

import java.io.Serializable;

import com.akibot.engine2.message.Request;

public class PutConfigurationRequest extends Request {
	private static final long serialVersionUID = 1L;

	private String name;
	private Serializable value;

	public PutConfigurationRequest() {

	}

	public PutConfigurationRequest(String name, Serializable value) {
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
