package com.akibot.web.bean;

import java.io.Serializable;

public class SimplifiedClientDescription implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String address;
	private String componentClassName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getComponentClassName() {
		return componentClassName;
	}

	public void setComponentClassName(String componentClassName) {
		this.componentClassName = componentClassName;
	}

}
