package com.akibot.kiss.component;

import com.akibot.kiss.message.Request;
import com.akibot.kiss.server.Client;

public class DefaultComponent implements Component {
	private Client client;

	@Override
	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public Client getClient() {
		return this.client;
	}

	@Override
	public void executeRequest(Request request) throws Exception {

	}

	@Override
	public void start() {

	}

}
