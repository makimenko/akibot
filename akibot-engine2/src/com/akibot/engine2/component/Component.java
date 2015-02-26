package com.akibot.engine2.component;

import java.util.List;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.network.ClientDescription;

public interface Component {

	public void start();

	public void onSystemMessageReceived(Message message) throws Exception;

	public void onMessageReceived(Message message) throws Exception;

	public void sendMessage(ClientDescription clientDescription, Message message) throws FailedToSendMessageException;

	public void setAkibotNode(AkibotClient akibotClient);

	public String getName();

	// ---------------- Not component:

	public Response syncRequest(Request request, int timeout) throws FailedToSendMessageException;

	public void broadcastMessage(Message message) throws FailedToSendMessageException;

	public ClientDescription getMyClientDescription();

	public List<ClientDescription> getClientDescriptionList();

	public String getSyncId();

	public void setSyncResponse(Response syncResponse);

	public Response getSyncResponse();

}
