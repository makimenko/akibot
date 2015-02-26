package com.akibot.engine2.network;

import java.util.UUID;

import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;

public class SynchronizedMessageManager {

	private AkibotClient akibotClient;
	private String syncId;
	private Response syncResponse;

	public SynchronizedMessageManager(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
	}

	public Request enrichRequest(Request originalRequest) throws CloneNotSupportedException {
		Request request = (Request) originalRequest.clone();
		setSyncId(UUID.randomUUID().toString());
		request.setSyncId(getSyncId());
		request.setFrom(akibotClient.getComponent().getName());
		return request;

	}

	public void generateSynId() {

	}

	public String getSyncId() {
		return syncId;
	}

	public Response getSyncResponse() {
		return syncResponse;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	public void setSyncResponse(Response syncResponse) {
		this.syncResponse = syncResponse;
	}

	public void start() {

	}
}
