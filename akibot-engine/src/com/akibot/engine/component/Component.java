package com.akibot.engine.component;

import com.akibot.engine.message.Message;
import com.akibot.engine.server.Client;

public interface Component extends Runnable {

	public Client getClient();

	public void processMessage(Message message) throws Exception;

	public void setClient(Client client);

	public void run();

	public void stop();

}
