package com.akibot.engine2.message;

public class Response extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void copySyncId(Request request) {
		if (request != null && request.getSyncId() != null) {
			this.setSyncId(request.getSyncId());
		}
	}

}
