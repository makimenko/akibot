package com.akibot.engine2.test.component;

import com.akibot.engine2.message.Response;

public class TestResponse2 extends Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int result;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return super.toString() + " (from=" + getFrom() + ", to=" + getTo() + ", syncId=" + getSyncId() + "): " + result;
	}
}
