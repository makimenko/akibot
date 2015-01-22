package com.akibot.engine2.component;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.server.AkibotNode;

public class DefaultComponent implements Component {
	private AkibotNode akibotNode;

	@Override
	public void start() {

	}

	@Override
	public void onMessageReceived(Message message) {

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
