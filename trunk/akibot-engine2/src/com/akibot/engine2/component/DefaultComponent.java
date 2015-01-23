package com.akibot.engine2.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.server.AkibotNode;

public class DefaultComponent implements Component {
	private static final Logger log = LogManager.getLogger(AkibotNode.class.getName());

	private AkibotNode akibotNode;
	private ClientDescription myClientDescription;
	private List<ClientDescription> clientDescriptionList;
	private String name;

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
		log.trace(name + ": refreshClientDescriptionList");
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
			clientDescriptionList = ClientDescriptionUtils.merge(response.getClientDescriptionList(), clientDescriptionList);
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
			Iterator<ClientDescription> i = clientDescriptionList.iterator();
			while (i.hasNext()) {
				ClientDescription client = (ClientDescription) i.next();
				if (ClientDescriptionUtils.isSystemMessage(message) || ClientDescriptionUtils.isInterestedInMessage(client, message)) {
					akibotNode.send(client, message);
				}
			}
		} else {
			log.trace(name + ": broadcastMessage: Skip broadcasting. No Clients!");
		}
	}

	@Override
	public void setAkibotNode(AkibotNode akibotNode) {
		this.akibotNode = akibotNode;
		this.myClientDescription = new ClientDescription(name, akibotNode.getMyInetSocketAddress());

		if (akibotNode.getParentSocketAddress() != null) {
			ClientDescription parentClientDescription = new ClientDescription(null, akibotNode.getParentSocketAddress());
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

	public AkibotNode getAkibotNode() {
		return akibotNode;
	}

	public List<ClientDescription> getClientDescriptionList() {
		return clientDescriptionList;
	}

	public void setName(String name) {
		this.name = name;
	}

}
