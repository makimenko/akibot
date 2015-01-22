package com.akibot.engine2.component;

import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.SystemRequest;
import com.akibot.engine2.server.ClientDescription;

public class ClientDescriptionRequest extends SystemRequest {

	private ClientDescription clientDescription;
	
	public ClientDescription getClientDescription() {
		return clientDescription;
	}

	public void setClientDescription(ClientDescription clientDescription) {
		this.clientDescription = clientDescription;
	}

}
