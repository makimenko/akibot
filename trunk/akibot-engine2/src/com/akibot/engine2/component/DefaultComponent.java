package com.akibot.engine2.component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.server.AkibotNode;

public class DefaultComponent implements Component {
	private AkibotNode akibotNode;
	private ClientDescription myClientDescription;
	private List<ClientDescription> clientDescriptionList;
	private String name;

	public DefaultComponent(String name) {
		this.name = name;
	}

	@Override
	public void start() {

	}

	public void refreshClientDescriptionList() {
		try {
			ClientDescriptionRequest clientDescriptionrequest = new ClientDescriptionRequest();
			clientDescriptionrequest.setClientDescription(myClientDescription);
			broadcastMessage(clientDescriptionrequest);
		} catch (FailedToSendMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSystemMessageReceived(Message message) throws Exception {
		if (message instanceof ClientDescriptionRequest) {
			ClientDescriptionRequest request = (ClientDescriptionRequest) message;
			clientDescriptionList = ClientDescriptionUtils.merge(request.getClientDescription(), clientDescriptionList);
			ClientDescriptionResponse response = new ClientDescriptionResponse();
			response.setClientDescriptionList(clientDescriptionList);
			broadcastMessage(response);
		} else if (message instanceof ClientDescriptionResponse) {
			ClientDescriptionResponse response = (ClientDescriptionResponse) message;
			clientDescriptionList = ClientDescriptionUtils.merge(response.getClientDescriptionList(), clientDescriptionList);
		}
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {

	}

	@Override
	public void broadcastMessage(Message message) throws FailedToSendMessageException {
		Iterator i = clientDescriptionList.iterator();
		while (i.hasNext()) {
			ClientDescription client = (ClientDescription) i.next();
			if (ClientDescriptionUtils.isSystemMessage(message) || ClientDescriptionUtils.isInterestedInMessage(client, message)) {
				akibotNode.send(client, message);
			}
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
	}

	@Override
	public void sendMessage(ClientDescription clientDescription, Message message) throws FailedToSendMessageException {
		sendMessage(clientDescription, message);
	}

}
