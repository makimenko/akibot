package com.akibot.world.message;

public class WorldCommandRequest extends WorldRequest {
	private static final long serialVersionUID = -932073097693007852L;
	private long timeout;

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
