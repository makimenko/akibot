package com.akibot.engine2.component.test;

import com.akibot.engine2.message.Request;

public class TestSleepRequest extends Request {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long sleepMills;

	public TestSleepRequest() {

	}

	public TestSleepRequest(long sleepMills) {
		this.sleepMills = sleepMills;
	}

	public long getSleepMills() {
		return sleepMills;
	}

	public void setSleepMills(long sleepMills) {
		this.sleepMills = sleepMills;
	}

	@Override
	public String toString() {
		return super.toString() + " (from=" + getFrom() + ", to=" + getTo() + ", syncId=" + getSyncId() + "): " + sleepMills;
	}
}
