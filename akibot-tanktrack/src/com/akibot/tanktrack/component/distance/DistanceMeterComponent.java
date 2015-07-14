package com.akibot.tanktrack.component.distance;

import akibot.jni.java.AkibotJniLibrary;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class DistanceMeterComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(DistanceMeterComponent.class);
	private AkibotJniLibrary lib;
	private DistanceMeterConfiguration distanceMeterConfiguration;

	public DistanceMeterComponent(int triggerPin, int echoPin, int timeoutMicroseconds) {
		this.distanceMeterConfiguration = new DistanceMeterConfiguration();
		distanceMeterConfiguration.setTriggerPin(triggerPin);
		distanceMeterConfiguration.setEchoPin(echoPin);
		distanceMeterConfiguration.setTimeoutMicroseconds(timeoutMicroseconds);
	}

	@Override
	public void loadDefaultTopicList() {
		addTopic(new DistanceRequest());
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof DistanceRequest) {
			onDistanceRequest((DistanceRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onDistanceRequest(DistanceRequest distanceRequest) throws FailedToSendMessageException {
		long startTime = System.currentTimeMillis();
		DistanceResponse response = new DistanceResponse();
		response.setMm(lib.getDistance(distanceMeterConfiguration.getTriggerPin(), distanceMeterConfiguration.getEchoPin(),
				distanceMeterConfiguration.getTimeoutMicroseconds()));
		log.trace(this.getAkibotClient() + ": Duration: " + (System.currentTimeMillis() - startTime));
		broadcastResponse(response, distanceRequest);
	}

	@Override
	public void startComponent() throws FailedToStartException {
		try {
			this.lib = new AkibotJniLibrary();
			this.lib.initialize();
		} catch (Exception e) {
			throw new FailedToStartException(e);
		}
	}

}
