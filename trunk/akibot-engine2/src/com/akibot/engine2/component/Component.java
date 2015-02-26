package com.akibot.engine2.component;

import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;

public interface Component {

	public String getName();

	public void onMessageReceived(Message message) throws Exception;

	public void setAkibotClient(AkibotClient akibotClient);

	public void start();

}
