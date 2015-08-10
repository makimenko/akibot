package com.akibot.engine2.component;

import com.akibot.engine2.component.configuration.ComponentConfiguration;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;

public interface Component {

	public void onMessageReceived(Message message) throws Exception;

	public void setAkibotClient(AkibotClient akibotClient);

	public void startComponent() throws FailedToStartException;

	/**
	 * Define Message types in which Component is interested (update AkibotClient topic list)
	 */
	public void loadDefaults();

	public void onGetConfigurationResponse(GetConfigurationResponse getConfigurationResponse) throws FailedToConfigureException;

	public ComponentStatus getComponentStatus();

	public AkibotClient getAkibotClient();

	public ComponentConfiguration getComponentConfiguration();

	public void addTopic(Message message);

	public Response sendSyncRequest(Request request, int timeout) throws FailedToSendMessageException;

	public void broadcastResponse(Response response, Request request) throws FailedToSendMessageException;

}
