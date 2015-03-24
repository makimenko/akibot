package com.akibot.tanktrack.component.echolocator;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class EchoLocatorComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(EchoLocatorComponent.class);
	private String distanceName;
	private String servoBaseName;

	public EchoLocatorComponent(String distanceName, String servoBaseName) {
		this.distanceName = distanceName;
		this.servoBaseName = servoBaseName;
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof EchoLocatorRequest) {

		}
	}

}
