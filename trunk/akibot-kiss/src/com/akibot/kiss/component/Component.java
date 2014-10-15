package com.akibot.kiss.component;

import com.akibot.kiss.message.Message;
import com.akibot.kiss.server.Client;

public interface Component {

	public void setClient(Client client);

	public Client getClient();

	public void processMessage(Message message) throws Exception;

	public void start();

}
