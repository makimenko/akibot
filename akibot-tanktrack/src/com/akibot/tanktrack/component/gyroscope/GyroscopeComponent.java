package com.akibot.tanktrack.component.gyroscope;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class GyroscopeComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(GyroscopeComponent.class);

	@Override
	public void onMessageReceived(Message message) throws Exception {
		super.onMessageReceived(message);
	}
}
