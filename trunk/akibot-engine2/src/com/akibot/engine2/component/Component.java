package com.akibot.engine2.component;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.server.AkibotNode;

public interface Component {

	public void start();

	public void onMessageReceived(Message message);

	public void sendMessage(Message message) throws FailedToSendMessageException;

	public void setAkibotNode(AkibotNode akibotNode);

}
