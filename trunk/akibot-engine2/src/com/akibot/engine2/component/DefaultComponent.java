package com.akibot.engine2.component;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
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
	public void start() {

	}

}
