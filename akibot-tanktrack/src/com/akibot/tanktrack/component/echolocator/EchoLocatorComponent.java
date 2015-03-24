package com.akibot.tanktrack.component.echolocator;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.servo.ServoResponse;

public class EchoLocatorComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(EchoLocatorComponent.class);
	private String distanceName;
	private String servoBaseName;
	private String servoHeadName;

	private DistanceRequest distanceRequest;
	private ServoRequest servoBaseRequest;
	private ServoRequest servoHeadRequest;

	private int syncTimeoutMillisec = 1000;
	private int longServoWaitMicrosec = 500000;
	private int shortServoWaitMicrosec = 50000;

	public EchoLocatorComponent(String distanceName, String servoBaseName, String servoHeadName) {
		this.distanceName = distanceName;
		this.servoBaseName = servoBaseName;
		this.servoHeadName = servoHeadName;
		initializeRequests();
	}

	private void initializeRequests() {
		// Prepare Distance Request:
		distanceRequest = new DistanceRequest();
		distanceRequest.setTo(distanceName);
		// Prepare Servo Motor (base: left-right)
		servoBaseRequest = new ServoRequest();
		servoBaseRequest.setTo(servoBaseName);
		// Prepare Servo Motor (head: up-down)
		servoHeadRequest = new ServoRequest();
		servoHeadRequest.setTo(servoHeadName);
	}

	private void step(int i) throws FailedToSendMessageException {
		servoBaseRequest.setValue(i);
		ServoResponse servoResponse = (ServoResponse) getAkibotClient().getOutgoingMessageManager().sendSyncRequest(servoBaseRequest, syncTimeoutMillisec);
		DistanceResponse distanceResponse = (DistanceResponse) getAkibotClient().getOutgoingMessageManager().sendSyncRequest(distanceRequest,
				syncTimeoutMillisec);
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof EchoLocatorRequest) {
			EchoLocatorResponse response = new EchoLocatorResponse();
			response.copySyncId(message);

			int baseFrom = 4;
			int baseTo = 24;
			int headNormal = 14;

			servoBaseRequest.setMicroseconds(longServoWaitMicrosec);
			servoBaseRequest.setValue(baseFrom);

			servoHeadRequest.setMicroseconds(longServoWaitMicrosec);
			servoHeadRequest.setValue(headNormal);

			getAkibotClient().getOutgoingMessageManager().broadcastMessage(servoBaseRequest);
			getAkibotClient().getOutgoingMessageManager().broadcastMessage(servoHeadRequest);

			Thread.sleep(syncTimeoutMillisec);

			servoBaseRequest.setMicroseconds(shortServoWaitMicrosec);
			for (int i = baseFrom; i <= baseTo; i++) {
				step(i);
			}

			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);

		}
	}

}
