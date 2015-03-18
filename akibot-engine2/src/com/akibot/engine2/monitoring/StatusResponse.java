package com.akibot.engine2.monitoring;

import java.util.List;

import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.ClientDescription;

public class StatusResponse extends Response {

	private static final long serialVersionUID = 1L;

	private long startupTime;
	private long currentTime;

	private ClientDescription myClientDescription;
	private List<ClientDescription> clientDescriptionList;

	public long getStartupTime() {
		return startupTime;
	}

	public void setStartupTime(long startupTime) {
		this.startupTime = startupTime;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public ClientDescription getMyClientDescription() {
		return myClientDescription;
	}

	public void setMyClientDescription(ClientDescription myClientDescription) {
		this.myClientDescription = myClientDescription;
	}

	public List<ClientDescription> getClientDescriptionList() {
		return clientDescriptionList;
	}

	public void setClientDescriptionList(List<ClientDescription> clientDescriptionList) {
		this.clientDescriptionList = clientDescriptionList;
	}

}
