package com.akibot.websocket;

import java.io.Serializable;

public class MyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String someText;

	public String getSomeText() {
		return someText;
	}

	public void setSomeText(String someText) {
		this.someText = someText;
	}

}
