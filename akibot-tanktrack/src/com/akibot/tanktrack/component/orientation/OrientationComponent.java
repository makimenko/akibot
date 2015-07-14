package com.akibot.tanktrack.component.orientation;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.MotionResponse;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TimedMotionRequest;

public class OrientationComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(AkibotClient.class);
	private String gyroscopeName;
	private String tankTrackName;
	private GyroscopeValueRequest gyroscopeValueRequest;
	private int syncRequestTimeout;
	private long stepMillis;
	private long easyMillis;
	private long easyDegrees;
	private RoundRobinUtils robinUtils;

	private StickMotionRequest stopRequest;
	private StickMotionRequest leftRequest;
	private StickMotionRequest rightRequest;
	private TimedMotionRequest easyLeftRequest;
	private TimedMotionRequest easyRightRequest;

	public OrientationComponent(String tankTrackName, String gyroscopeName) {
		this.tankTrackName = tankTrackName;
		this.gyroscopeName = gyroscopeName;
		this.gyroscopeValueRequest = new GyroscopeValueRequest();
		this.gyroscopeValueRequest.setTo(this.gyroscopeName);
		this.syncRequestTimeout = 2000; // 2 second timeout
		this.stepMillis = 100; // TODO: Configuratble
		this.easyDegrees = 45;
		this.easyMillis = 100; // TODO: Configuratble
		this.robinUtils = new RoundRobinUtils(360);

		stopRequest = new StickMotionRequest(DirectionType.STOP);
		leftRequest = new StickMotionRequest(DirectionType.LEFT);
		rightRequest = new StickMotionRequest(DirectionType.RIGHT);
		stopRequest.setTo(tankTrackName);
		leftRequest.setTo(tankTrackName);
		rightRequest.setTo(tankTrackName);

		easyLeftRequest = new TimedMotionRequest(DirectionType.LEFT);
		easyRightRequest = new TimedMotionRequest(DirectionType.RIGHT);
		easyLeftRequest.setMilliseconds(easyMillis);
		easyRightRequest.setMilliseconds(easyMillis);
		easyLeftRequest.setTo(tankTrackName);
		easyRightRequest.setTo(tankTrackName);

	}

	@Override
	public void loadDefaultTopicList() {
		addTopic(new OrientationRequest());
		addTopic(new MotionResponse());
		addTopic(new GyroscopeResponse());

	}

	public boolean isExpected(OrientationRequest orientationRequest, GyroscopeResponse gyroscopeResponse) {
		double aXY = gyroscopeResponse.getNorthDegrreesXY();
		double eXY = orientationRequest.getNorthDegrreesXY();
		double ePrecission = orientationRequest.getPrecissionDegrees();
		return (robinUtils.leftDistance(aXY, eXY) <= ePrecission || robinUtils.rightDistance(aXY, eXY) <= ePrecission);
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof OrientationRequest) {
			onOrientationRequest((OrientationRequest) message);
		} else if (message instanceof MotionResponse) {
			// Nothing
		} else if (message instanceof GyroscopeResponse) {
			// Nothing
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onOrientationRequest(OrientationRequest orientationRequest) throws FailedToSendMessageException, InterruptedException,
			InvalidOrientationRequestException {

		if (orientationRequest.getNorthDegrreesXY() >= 0 && orientationRequest.getNorthDegrreesXY() <= 360 && orientationRequest.getPrecissionDegrees() > 0
				&& orientationRequest.getPrecissionDegrees() < 360 && orientationRequest.getTimeoutMillis() > 0
				&& orientationRequest.getTimeoutMillis() <= 60000) {

			log.debug(this.getAkibotClient() + ": OrientationRequest: " + orientationRequest);
			long startTimeMills = System.currentTimeMillis();

			GyroscopeResponse gyroscopeResponse = new GyroscopeResponse();
			int lastDirection = 0;
			boolean easyMove = false;

			OrientationResponse orientationResponse = new OrientationResponse();

			while (System.currentTimeMillis() - startTimeMills < orientationRequest.getTimeoutMillis()) {
				gyroscopeResponse = getGyroscopeResponse();

				if (isExpected(orientationRequest, gyroscopeResponse)) {
					break;
				} else {
					double aXY = gyroscopeResponse.getNorthDegrreesXY();
					double eXY = orientationRequest.getNorthDegrreesXY();
					double rightDistance = robinUtils.rightDistance(aXY, eXY);
					double leftDistance = robinUtils.leftDistance(aXY, eXY);

					easyMove = easyMove || Math.min(rightDistance, leftDistance) < easyDegrees;

					if (leftDistance <= rightDistance && easyMove) {
						lastDirection = 0;
						// System.out.println("LEFT (easy)");
						sendSyncRequest(easyLeftRequest, syncRequestTimeout);
					} else if (rightDistance < leftDistance && easyMove) {
						lastDirection = 0;
						// System.out.println("RIGHT (easy)");
						sendSyncRequest(easyRightRequest, syncRequestTimeout);
					} else if (leftDistance <= rightDistance && lastDirection != -1) {
						lastDirection = -1;
						// System.out.println("LEFT");
						sendSyncRequest(leftRequest, syncRequestTimeout);
					} else if (rightDistance < leftDistance && lastDirection != +1) {
						lastDirection = +1;
						// System.out.println("RIGHT");
						sendSyncRequest(rightRequest, syncRequestTimeout);
					}
					Thread.sleep(stepMillis);
				}
			}
			if (!easyMove) {
				sendSyncRequest(stopRequest, syncRequestTimeout);
				Thread.sleep(stepMillis);
			}
			gyroscopeResponse = getGyroscopeResponse();
			boolean expected = isExpected(orientationRequest, gyroscopeResponse);
			orientationResponse.setSuccess(expected);
			log.debug(this.getAkibotClient() + ": Orientation: " + expected);

			orientationResponse.setNorthDegrreesXY(gyroscopeResponse.getNorthDegrreesXY());
			broadcastResponse(orientationResponse, orientationRequest);

		} else {
			throw new InvalidOrientationRequestException(orientationRequest.toString());
		}
	}

	private GyroscopeResponse getGyroscopeResponse() throws FailedToSendMessageException {
		return (GyroscopeResponse) sendSyncRequest(gyroscopeValueRequest, syncRequestTimeout);
	}

}
