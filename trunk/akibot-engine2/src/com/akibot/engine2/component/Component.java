package com.akibot.engine2.component;

import com.akibot.engine2.message.Message;

public interface Component {

	public void start();

	public void onMessageReceived(Message message);

}
