package com.akibot.engine2.component;

import java.util.List;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.server.AkibotNode;
import com.akibot.engine2.server.ClientDescription;

public class DefaultComponent implements Component {
	private AkibotNode akibotNode;
	private List<ClientDescription> clientDescriptionList;

	@Override
	public void start() {
		
	}

	@Override
	public void onSystemMessageReceived(Message message) throws Exception {
		if (message instanceof ClientDescriptionRequest) {
			ClientDescriptionResponse response = new ClientDescriptionResponse();
			response.setClientDescriptionList(clientDescriptionList);
			sendMessage(response);
		} else if (message instanceof ClientDescriptionResponse) {
			ClientDescriptionResponse response = (ClientDescriptionResponse) message;
			clientDescriptionList = response.mergeNewInto(clientDescriptionList);
		}

	}

	@Override
	public void onMessageReceived(Message message) throws Exception {

	}

	@Override
	public void sendMessage(Message message) throws FailedToSendMessageException {
		akibotNode.send(message);
	}

	@Override
	public void setAkibotNode(AkibotNode akibotNode) {
		this.akibotNode = akibotNode;
	}

}
