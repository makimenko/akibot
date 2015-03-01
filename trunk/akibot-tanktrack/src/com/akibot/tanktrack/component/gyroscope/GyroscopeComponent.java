package com.akibot.tanktrack.component.gyroscope;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.message.Message;

public class GyroscopeComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(GyroscopeComponent.class.getName());

	@Override
	public void onMessageReceived(Message message) throws Exception {
		super.onMessageReceived(message);
	}
}
