package com.akibot.tanktrack.component.echolocator;

import akibot.jni.java.AkibotJniLibrary;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class EchoLocatorComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(EchoLocatorComponent.class);
	private AkibotJniLibrary lib;
	private EchoLocatorConfig echoLocatorConfig;

	public EchoLocatorComponent(EchoLocatorConfig echoLocatorConfig) {
		this.echoLocatorConfig = echoLocatorConfig;
		this.lib = new AkibotJniLibrary();
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof EchoLocatorRequest) {
			EchoLocatorResponse response = new EchoLocatorResponse();
			response.copySyncId(message);
			EchoLocatorRequest request = (EchoLocatorRequest) message;

			float result[] = lib.echoLocator(echoLocatorConfig.getDistanceTriggerPin(), echoLocatorConfig.getDistanceEchoPin(),
					echoLocatorConfig.getDistanceTimeout(), echoLocatorConfig.getSleepBeforeDistance(), echoLocatorConfig.getServoBasePin(),
					echoLocatorConfig.getServoHeadPin(), request.getServoBaseFrom(), request.getServoBaseTo(), request.getServoBaseStep(),
					request.getServoHeadNormal(), echoLocatorConfig.getServoLongTime(), echoLocatorConfig.getServoStepTime(),
					echoLocatorConfig.getDistanceCount(), request.isTrustToLastPosition());

			response.setEchoLocatorResult(result);

			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		}
	}
}
