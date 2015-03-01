package com.akibot.engine2.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;

public class DefaultComponent implements Component {
	private static final Logger log = LogManager.getLogger(DefaultComponent.class.getName());

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
	public void start() {

	}

}
