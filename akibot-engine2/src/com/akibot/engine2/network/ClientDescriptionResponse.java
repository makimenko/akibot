package com.akibot.engine2.network;

import java.util.List;

import com.akibot.engine2.message.SystemResponse;

public class ClientDescriptionResponse extends SystemResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ClientDescription> clientDescriptionList;
	private ClientDescription senderClientDescription;

	public List<ClientDescription> getClientDescriptionList() {
		return clientDescriptionList;
	}

	public void setClientDescriptionList(List<ClientDescription> clientDescriptionList) {
		this.clientDescriptionList = clientDescriptionList;
	}

	@Override
	public String toString() {
		return "ClientDescriptionResponse [" + (clientDescriptionList == null ? 0 : clientDescriptionList.size()) + "]";

	}

	public ClientDescription getSenderClientDescription() {
		return senderClientDescription;
	}

	public void setSenderClientDescription(ClientDescription senderClientDescription) {
		this.senderClientDescription = senderClientDescription;
	}

}
