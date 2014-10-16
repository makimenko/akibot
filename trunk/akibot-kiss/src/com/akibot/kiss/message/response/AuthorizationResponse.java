package com.akibot.kiss.message.response;

import com.akibot.kiss.message.Response;
import com.akibot.kiss.server.ClientDescription;

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
