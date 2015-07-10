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
	private int triggerPin;
	private int echoPin;
	private int timeoutMicroseconds;

	public DistanceMeterComponent(int triggerPin, int echoPin, int timeoutMicroseconds) {

		this.triggerPin = triggerPin;
		this.echoPin = echoPin;
		this.timeoutMicroseconds = timeoutMicroseconds;
	}

	public DistanceMeterComponent() throws Exception {
		throw new Exception("Unimplemented constructor!");
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
		response.setMm(lib.getDistance(triggerPin, echoPin, timeoutMicroseconds));
		log.trace(this.getAkibotClient() + ": Duration: " + (System.currentTimeMillis() - startTime));
		broadcastResponse(response, distanceRequest);
	}

	@Override
	public void start() throws FailedToStartException {
		try {
			this.lib = new AkibotJniLibrary();
			this.lib.initialize();
		} catch (Exception e) {
			throw new FailedToStartException(e);
		}
	}

}
