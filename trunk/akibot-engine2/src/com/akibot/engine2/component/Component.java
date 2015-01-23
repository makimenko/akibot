package com.akibot.engine2.component;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.server.AkibotNode;

public interface Component {

	public void start();

	public void onSystemMessageReceived(Message message) throws Exception;

	public void onMessageReceived(Message message) throws Exception;

	public void sendMessage(ClientDescription clientDescription, Message message) throws FailedToSendMessageException;

	public void broadcastMessage(Message message) throws FailedToSendMessageException;

	public void setAkibotNode(AkibotNode akibotNode);

	public String getName();

}
