package com.akibot.kiss.component;

import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.server.Client;

public interface Component {

	public void executeCommand(Client client, CommandMessage command) throws Exception;
	
}
