package com.akibot.engine2.component;

import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;

public class DefaultComponent implements Component {
	private static final AkiLogger log = AkiLogger.create(DefaultComponent.class);

	private AkibotClient akibotClient;

	public AkibotClient getAkibotClient() {
		return akibotClient;
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {

	}

	@Override
	public void setAkibotClient(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
	}

	@Override
	public void startComponent() throws FailedToStartException {

	}

	@Override
	public void loadConfiguration() throws FailedToConfigureException {

	}

	public void broadcastMessage(Message message) throws FailedToSendMessageException {
		getAkibotClient().getOutgoingMessageManager().broadcastMessage(message);
	}

	public void broadcastResponse(Response response, Request request) throws FailedToSendMessageException {
		response.copySyncId(request);
		broadcastMessage(response);
	}

	public Response sendSyncRequest(Request request, int timeout) throws FailedToSendMessageException {
		return getAkibotClient().getOutgoingMessageManager().sendSyncRequest(request, timeout);
	}

	public void addTopic(Message message) {
		getAkibotClient().getMyClientDescription().getTopicList().add(message);
	}

	@Override
	public void loadDefaultTopicList() {

	}

}
