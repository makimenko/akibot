package com.akibot.kiss.server;

public class ClientDescription {
	private String name;
	private String componentClassName;
	private long connectTime;

	public ClientDescription(String name, String componentClassName) {
		this.name = name;
		this.componentClassName = componentClassName;
		this.connectTime = System.currentTimeMillis();
	}

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

	public long getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(long connectTime) {
		this.connectTime = connectTime;
	}
}
