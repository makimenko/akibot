package com.akibot.engine.component;

import com.akibot.engine.message.Message;
import com.akibot.engine.server.Client;

public class DefaultComponent implements Component {
	private Client client;

	@Override
	public Client getClient() {
		return this.client;
	}

	@Override
	public void processMessage(Message message) throws Exception {

	}

	@Override
	public void run() {

	}

	@Override
	public void setClient(Client client) {
		this.client = client;
	}

}
