package com.akibot.engine2.component;

import java.io.Serializable;

public class ComponentStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean ready;

	public ComponentStatus() {
		ready = false;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

}
