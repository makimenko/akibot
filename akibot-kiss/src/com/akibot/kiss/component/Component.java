package com.akibot.kiss.component;

import com.akibot.kiss.message.Request;
import com.akibot.kiss.server.Client;

public interface Component {

	public void executeRequest(Client client, Request request) throws Exception;
	
}
