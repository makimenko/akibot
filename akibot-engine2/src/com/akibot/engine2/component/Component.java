package com.akibot.engine2.component;

import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;

public interface Component {

	public void onMessageReceived(Message message) throws Exception;

	public void setAkibotClient(AkibotClient akibotClient);

	public void start() throws FailedToStartException;

	public void loadConfiguration() throws FailedToConfigureException;

}
