package com.akibot.engine2.component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.network.ClientDescription;
import com.akibot.engine2.network.ClientDescriptionRequest;
import com.akibot.engine2.network.ClientDescriptionResponse;
import com.akibot.engine2.network.ClientDescriptionUtils;

public class DefaultComponent implements Component {
	private static final Logger log = LogManager.getLogger(DefaultComponent.class.getName());

	private AkibotClient akibotClient;
	private ClientDescription myClientDescription;
	private List<ClientDescription> clientDescriptionList;
	private String name;

	private String syncId;
	private Response syncResponse;

	public String getSyncId() {
		return syncId;
	}

	public void setSyncResponse(Response syncResponse) {
		this.syncResponse = syncResponse;
	}

	public Response getSyncResponse() {
		return syncResponse;
	}

	public DefaultComponent(String name) {
		this.name = name;
		clientDescriptionList = new ArrayList<ClientDescription>();
		log.debug(name + ": component initialized.");
	}

	@Override
	public void start() {
		refreshClientDescriptionList();
	}

	public void refreshClientDescriptionList() {
		log.trace(name + ": refreshClientDescriptionList: " + myClientDescription);
		try {
			ClientDescriptionRequest clientDescriptionRequest = new ClientDescriptionRequest();
			clientDescriptionRequest.setClientDescription(myClientDescription);
			broadcastMessage(clientDescriptionRequest);
		} catch (FailedToSendMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSystemMessageReceived(Message message) throws Exception {
		log.trace(name + ": onSystemMessageReceived (from=" + message.getFrom() + "): " + message);
		if (message instanceof ClientDescriptionRequest) {
			ClientDescriptionRequest request = (ClientDescriptionRequest) message;
			clientDescriptionList = ClientDescriptionUtils.merge(request.getClientDescription(), clientDescriptionList);
			ClientDescriptionResponse response = new ClientDescriptionResponse();
			response.setClientDescriptionList(clientDescriptionList);
			broadcastMessage(response);
		} else if (message instanceof ClientDescriptionResponse) {
			ClientDescriptionResponse response = (ClientDescriptionResponse) message;
			clientDescriptionList = ClientDescriptionUtils.merge(myClientDescription, response.getClientDescriptionList(), clientDescriptionList);
			// broadcastMessage(response);
		}
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {

	}

	@Override
	public void broadcastMessage(Message message) throws FailedToSendMessageException {
		if (clientDescriptionList != null && clientDescriptionList.size() > 0) {
			log.trace(name + ": broadcastMessage: " + message);
			for (ClientDescription client : clientDescriptionList) {
				if (ClientDescriptionUtils.isSystemMessage(message) || ClientDescriptionUtils.isInterestedInMessage(client, message)) {
					akibotClient.send(client, message);
				}
			}
		} else {
			log.trace(name + ": broadcastMessage: Skip broadcasting. No Clients!");
		}
	}

	public Response syncRequest(Request request, int timeout) throws FailedToSendMessageException {
		Request newRequest;
		try {
			newRequest = (Request) request.clone();

			syncResponse = null;

			syncId = UUID.randomUUID().toString();
			newRequest.setFrom(this.getName());
			newRequest.setSyncId(syncId);

			log.trace("Sync messasge sent: " + newRequest + " (syncId=" + newRequest.getSyncId() + ")");
			broadcastMessage(newRequest);

			synchronized (this.syncId) {
				this.syncId.wait(timeout);
			}

			if (syncResponse == null) {
				throw new Exception("Timeout occured while waiting sync response");
			} else {
				log.trace("Sync messasge received: " + syncResponse.getSyncId() + ": " + syncResponse);
			}
			return syncResponse;

		} catch (Exception e) {
			log.catching(e);
			throw new FailedToSendMessageException();
		}
	}

	@Override
	public void setAkibotNode(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
		this.myClientDescription = new ClientDescription(name, akibotClient.getMyInetSocketAddress());

		if (akibotClient.getParentSocketAddress() != null) {
			ClientDescription parentClientDescription = new ClientDescription(null, akibotClient.getParentSocketAddress());
			clientDescriptionList.add(parentClientDescription);
		}

		log.debug(this + ": " + myClientDescription);
	}

	@Override
	public void sendMessage(ClientDescription clientDescription, Message message) throws FailedToSendMessageException {
		sendMessage(clientDescription, message);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public ClientDescription getMyClientDescription() {
		return myClientDescription;
	}

	public void setMyClientDescription(ClientDescription myClientDescription) {
		this.myClientDescription = myClientDescription;
	}

	public AkibotClient getAkibotNode() {
		return akibotClient;
	}

	public List<ClientDescription> getClientDescriptionList() {
		return clientDescriptionList;
	}

	public void setName(String name) {
		this.name = name;
	}

}
