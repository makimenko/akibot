package com.akibot.engine2.network;

import com.akibot.engine2.message.SystemRequest;

public class ClientDescriptionRequest extends SystemRequest {

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
