package com.akibot.engine2.component.configuration;

import com.akibot.engine2.message.Request;

public class PutConfigurationRequest extends Request {
	private static final long serialVersionUID = 1L;

	private String name;
	private ComponentConfiguration componentConfiguration;

	public PutConfigurationRequest() {

	}

	public PutConfigurationRequest(String name, ComponentConfiguration value) {
		this.name = name;
		this.componentConfiguration = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ComponentConfiguration getComponentConfiguration() {
		return componentConfiguration;
	}

	public void setComponentConfiguration(ComponentConfiguration value) {
		this.componentConfiguration = value;
	}

}
