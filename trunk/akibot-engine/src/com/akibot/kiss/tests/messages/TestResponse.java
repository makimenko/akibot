package com.akibot.kiss.tests.messages;

import com.akibot.kiss.message.Response;

public class TestResponse extends Response {
	private static final long serialVersionUID = 1L;
	private Integer result;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;

	}
}