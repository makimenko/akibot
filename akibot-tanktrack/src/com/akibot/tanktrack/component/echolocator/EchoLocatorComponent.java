package com.akibot.tanktrack.component.echolocator;

import akibot.jni.java.AkibotJniLibrary;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.servo.ServoResponse;

public class EchoLocatorComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(EchoLocatorComponent.class);
	private AkibotJniLibrary lib;
	private EchoLocatorConfiguration echoLocatorConfig;

	public EchoLocatorComponent(EchoLocatorConfiguration echoLocatorConfig) {
		this.echoLocatorConfig = echoLocatorConfig;
	}

	@Override
	public void loadDefaultTopicList() {
		addTopic(new EchoLocatorRequest());
		addTopic(new DistanceResponse());
		addTopic(new ServoResponse());
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof EchoLocatorRequest) {
			EchoLocatorResponse response = new EchoLocatorResponse();
			EchoLocatorRequest request = (EchoLocatorRequest) message;

			float result[] = lib.echoLocator(echoLocatorConfig.getDistanceTriggerPin(), echoLocatorConfig.getDistanceEchoPin(),
					echoLocatorConfig.getDistanceTimeout(), echoLocatorConfig.getSleepBeforeDistance(), echoLocatorConfig.getServoBasePin(),
					echoLocatorConfig.getServoHeadPin(), request.getServoBaseFrom(), request.getServoBaseTo(), request.getServoBaseStep(),
					request.getServoHeadNormal(), echoLocatorConfig.getServoLongTime(), echoLocatorConfig.getServoStepTime(),
					echoLocatorConfig.getDistanceCount(), request.isTrustToLastPosition());

			response.setEchoLocatorResult(result);

			broadcastResponse(response, request);
		} else if (message instanceof DistanceResponse) {
			// Nothing
		} else if (message instanceof ServoResponse) {
			// Nothing
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	@Override
	public void startComponent() throws FailedToStartException {
		try {
			this.lib = new AkibotJniLibrary();
		} catch (Exception e) {
			throw new FailedToStartException(e);
		}
	}

}
