package com.akibot.engine.message.response;

import com.akibot.engine.message.Response;
import com.akibot.engine.server.ClientDescription;

public class AuthorizationResponse extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientDescription clientDescription;

	public ClientDescription getClientDescription() {
		return clientDescription;
	}

	public void setClientDescription(ClientDescription clientDescription) {
		this.clientDescription = clientDescription;
	}

}
