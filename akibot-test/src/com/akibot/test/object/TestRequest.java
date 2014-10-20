package com.akibot.test.object;

import com.akibot.engine.message.Request;

public class TestRequest extends Request {
	private static final long serialVersionUID = 1L;
	private Integer x;

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}
}
