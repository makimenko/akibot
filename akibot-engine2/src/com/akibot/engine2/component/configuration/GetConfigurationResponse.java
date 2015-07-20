package com.akibot.engine2.component.configuration;

import java.io.Serializable;

import com.akibot.engine2.message.SystemResponse;

public class GetConfigurationResponse extends SystemResponse {
	private static final long serialVersionUID = 1L;

	private String name;
	private ComponentConfiguration componentConfiguration;

	public GetConfigurationResponse() {

	}

	public GetConfigurationResponse(String name, ComponentConfiguration value) {
		this.name = name;
		this.componentConfiguration = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Serializable getComponentConfiguration() {
		return componentConfiguration;
	}

	public void setComponentConfiguration(ComponentConfiguration value) {
		this.componentConfiguration = value;
	}

}
