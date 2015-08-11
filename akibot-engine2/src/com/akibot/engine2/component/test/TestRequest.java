package com.akibot.engine2.component.test;

import com.akibot.engine2.message.Request;

public class TestRequest extends Request {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Override
	public String toString() {
		return super.toString() + " (from=" + getFrom() + ", to=" + getTo() + ", syncId=" + getSyncId() + "): x=" + getX();
	}
}
