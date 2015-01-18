package com.akibot.tanktrack.component.distance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akibot.jni.java.AkibotJniLibrary;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;

public class DistanceMeterComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(DistanceMeterComponent.class.getName());
	private AkibotJniLibrary lib;
	private int triggerPin;
	private int echoPin;
	private int timeoutMicroseconds;

	public DistanceMeterComponent(int triggerPin, int echoPin, int timeoutMicroseconds) {
		this.lib = new AkibotJniLibrary();
		this.lib.initialize();
		this.triggerPin = triggerPin;
		this.echoPin = echoPin;
		this.timeoutMicroseconds = timeoutMicroseconds;
	}

	public DistanceMeterComponent() throws Exception {
		throw new Exception("Unimplemented constructor!");
	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof DistanceRequest) {
			long startTime = System.currentTimeMillis();
			DistanceRequest request = (DistanceRequest) message;
			DistanceResponse response = new DistanceResponse();
			response.setMm(lib.getDistance(triggerPin, echoPin, timeoutMicroseconds));
			response.copySyncId(message);
			log.trace("Duration: " + (System.currentTimeMillis() - startTime));
			this.getClient().send(response);
		}
	}

	@Override
	public void run() {

	}

}
