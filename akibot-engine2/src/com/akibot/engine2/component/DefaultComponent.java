package com.akibot.engine2.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.Launcher;

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
