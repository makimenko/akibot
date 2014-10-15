package com.akibot.kiss.component;

import com.akibot.kiss.message.Request;
import com.akibot.kiss.server.Client;

public interface Component {

	public void setClient(Client client);

	public Client getClient();

	public void executeRequest(Request request) throws Exception;

	public void start();
	
}
