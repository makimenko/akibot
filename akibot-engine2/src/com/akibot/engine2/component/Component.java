package com.akibot.engine2.component;

import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;

public interface Component {

	public void onMessageReceived(Message message) throws Exception;

	public void setAkibotClient(AkibotClient akibotClient);

	public void startComponent() throws FailedToStartException;

	/**
	 * Define Message types in which Component is interested (update AkibotClient topic list)
	 */
	public void loadDefaultTopicList();

	public void loadConfiguration() throws FailedToConfigureException;

}
