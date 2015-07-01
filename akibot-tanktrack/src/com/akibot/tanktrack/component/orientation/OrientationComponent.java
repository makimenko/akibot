package com.akibot.tanktrack.component.orientation;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;

public class OrientationComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(AkibotClient.class);
	private String gyroscopeName;
	private String tankTrackName;
	private GyroscopeValueRequest gyroscopeValueRequest;
	private int syncRequestTimeout;
	private long stepMillis;
	private long totalDegrees;

	public OrientationComponent(String tankTrackName, String gyroscopeName) {
		this.tankTrackName = tankTrackName;
		this.gyroscopeName = gyroscopeName;
		this.gyroscopeValueRequest = new GyroscopeValueRequest();
		this.gyroscopeValueRequest.setTo(this.gyroscopeName);
		this.syncRequestTimeout = 2000; // 2 second timeout
		this.stepMillis = 100; // TODO: Configuratble
		this.totalDegrees = 360;
	}

	public boolean isExpected(OrientationRequest orientationRequest, GyroscopeResponse gyroscopeResponse) {
		double aXY = gyroscopeResponse.getNorthDegrreesXY();
		double eXY = orientationRequest.getNorthDegrreesXY();
		double ePrecission = orientationRequest.getPrecissionDegrees();
		double minXY = eXY - ePrecission;
		double maxXY = eXY + ePrecission;
		return (aXY >= minXY && aXY <= maxXY); // TODO: implement round-robin (0 grad)
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof OrientationRequest) {
			OrientationRequest orientationRequest = (OrientationRequest) message;
			if (orientationRequest.getNorthDegrreesXY() >= 0 && orientationRequest.getNorthDegrreesXY() <= 360 && orientationRequest.getPrecissionDegrees() > 0
					&& orientationRequest.getPrecissionDegrees() < 360 && orientationRequest.getTimeoutMillis() > 0
					&& orientationRequest.getTimeoutMillis() <= 60000) {

				log.debug(this.getAkibotClient() + ": OrientationRequest: " + orientationRequest);
				long startTimeMills = System.currentTimeMillis();

				StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);
				StickMotionRequest leftRequest = new StickMotionRequest(DirectionType.LEFT);
				StickMotionRequest rightRequest = new StickMotionRequest(DirectionType.RIGHT);
				stopRequest.setTo(tankTrackName);
				leftRequest.setTo(tankTrackName);
				rightRequest.setTo(tankTrackName);

				GyroscopeResponse gyroscopeResponse = new GyroscopeResponse();
				int lastDirection = 0;

				OrientationResponse orientationResponse = new OrientationResponse();
				orientationRequest.copySyncId(message);
				boolean success = false;

				while (System.currentTimeMillis() - startTimeMills < orientationRequest.getTimeoutMillis()) {
					gyroscopeResponse = getGyroscopeResponse();

					if (isExpected(orientationRequest, gyroscopeResponse)) {
						log.debug(this.getAkibotClient() + ": Orientation Succeeded");

						getAkibotClient().getOutgoingMessageManager().sendSyncRequest(stopRequest, syncRequestTimeout);
						Thread.sleep(stepMillis);
						gyroscopeResponse = getGyroscopeResponse();
						// success = true;
						break;
					} else {
						double aXY = gyroscopeResponse.getNorthDegrreesXY();
						double eXY = orientationRequest.getNorthDegrreesXY();

						double positive = (aXY < eXY ? eXY - aXY : eXY - aXY + totalDegrees);
						double negative = (aXY < eXY ? eXY - aXY - totalDegrees : eXY - aXY);

						if (positive <= Math.abs(negative) && lastDirection != -1) {
							lastDirection = -1;
							getAkibotClient().getOutgoingMessageManager().broadcastMessage(leftRequest);
						} else if (positive > Math.abs(negative) && lastDirection != +1) {
							lastDirection = +1;
							getAkibotClient().getOutgoingMessageManager().broadcastMessage(rightRequest);
						}

						Thread.sleep(stepMillis);
					}
				}
				log.debug(this.getAkibotClient() + ": Orientation Failed");
				getAkibotClient().getOutgoingMessageManager().sendSyncRequest(stopRequest, syncRequestTimeout);
				Thread.sleep(stepMillis);
				gyroscopeResponse = getGyroscopeResponse();

				orientationResponse.setSuccess(isExpected(orientationRequest, gyroscopeResponse));
				orientationResponse.setNorthDegrreesXY(gyroscopeResponse.getNorthDegrreesXY());
				orientationResponse.copySyncId(message);
				getAkibotClient().getOutgoingMessageManager().broadcastMessage(orientationResponse);

			} else {
				log.error(this.getAkibotClient() + ": Invalid Orientation Request");
			}

		}
	}

	private GyroscopeResponse getGyroscopeResponse() throws FailedToSendMessageException {
		return (GyroscopeResponse) getAkibotClient().getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, syncRequestTimeout);
	}

}
