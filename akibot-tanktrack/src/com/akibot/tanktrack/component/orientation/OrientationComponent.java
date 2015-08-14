package com.akibot.tanktrack.component.orientation;

import java.io.Serializable;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.MotionResponse;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TimedMotionRequest;

public class OrientationComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(OrientationComponent.class);

	private OrientationConfiguration orientationConfiguration;
	private RoundRobinUtils robinUtils = new RoundRobinUtils(360);
	private GyroscopeValueRequest gyroscopeValueRequest;
	private StickMotionRequest stopRequest;
	private StickMotionRequest leftRequest;
	private StickMotionRequest rightRequest;
	private TimedMotionRequest easyLeftRequest;
	private TimedMotionRequest easyRightRequest;

	@Override
	public void onGetConfigurationResponse(GetConfigurationResponse getConfigurationResponse) throws FailedToConfigureException {
		Serializable responseValue = getConfigurationResponse.getComponentConfiguration();
		if (responseValue instanceof OrientationConfiguration) {
			setOrientationConfiguration((OrientationConfiguration) responseValue);
		} else {
			throw new FailedToConfigureException(responseValue.toString());
		}
	}

	public void setOrientationConfiguration(OrientationConfiguration orientationConfiguration) {
		this.orientationConfiguration = orientationConfiguration;
		init();
	}

	public void init() {
		log.debug(this.getAkibotClient() + ": Initializing orientation");
		getComponentStatus().setReady(false);
		this.gyroscopeValueRequest = new GyroscopeValueRequest();
		this.gyroscopeValueRequest.setTo(orientationConfiguration.getGyroscopeName());

		stopRequest = new StickMotionRequest(DirectionType.STOP);
		leftRequest = new StickMotionRequest(DirectionType.LEFT);
		rightRequest = new StickMotionRequest(DirectionType.RIGHT);
		stopRequest.setTo(orientationConfiguration.getTankTrackName());
		leftRequest.setTo(orientationConfiguration.getTankTrackName());
		rightRequest.setTo(orientationConfiguration.getTankTrackName());

		easyLeftRequest = new TimedMotionRequest(DirectionType.LEFT);
		easyRightRequest = new TimedMotionRequest(DirectionType.RIGHT);
		easyLeftRequest.setMilliseconds(orientationConfiguration.getEasyMillis());
		easyRightRequest.setMilliseconds(orientationConfiguration.getEasyMillis());
		easyLeftRequest.setTo(orientationConfiguration.getTankTrackName());
		easyRightRequest.setTo(orientationConfiguration.getTankTrackName());
		getComponentStatus().setReady(true);
	}

	@Override
	public void loadDefaults() {
		addTopic(new OrientationRequest());
		addTopic(new MotionResponse());
		addTopic(new GyroscopeResponse());
	}

	public boolean isExpected(OrientationRequest orientationRequest, GyroscopeResponse gyroscopeResponse) {
		double aXY = gyroscopeResponse.getNorthDegreesXY();
		double eXY = orientationRequest.getNorthDegreesXY();
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

		if (orientationRequest.getNorthDegreesXY() >= 0 && orientationRequest.getNorthDegreesXY() <= 360 && orientationRequest.getPrecissionDegrees() > 0
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
					double aXY = gyroscopeResponse.getNorthDegreesXY();
					double eXY = orientationRequest.getNorthDegreesXY();
					double rightDistance = robinUtils.rightDistance(aXY, eXY);
					double leftDistance = robinUtils.leftDistance(aXY, eXY);

					easyMove = easyMove || Math.min(rightDistance, leftDistance) < orientationConfiguration.getEasyDegrees();

					if (leftDistance <= rightDistance && easyMove) {
						lastDirection = 0;
						// System.out.println("LEFT (easy)");
						sendSyncRequest(easyLeftRequest, orientationConfiguration.getSyncRequestTimeout());
					} else if (rightDistance < leftDistance && easyMove) {
						lastDirection = 0;
						// System.out.println("RIGHT (easy)");
						sendSyncRequest(easyRightRequest, orientationConfiguration.getSyncRequestTimeout());
					} else if (leftDistance <= rightDistance && lastDirection != -1) {
						lastDirection = -1;
						// System.out.println("LEFT");
						sendSyncRequest(leftRequest, orientationConfiguration.getSyncRequestTimeout());
					} else if (rightDistance < leftDistance && lastDirection != +1) {
						lastDirection = +1;
						// System.out.println("RIGHT");
						sendSyncRequest(rightRequest, orientationConfiguration.getSyncRequestTimeout());
					}
					Thread.sleep(orientationConfiguration.getStepMillis());
				}
			}
			if (!easyMove) {
				sendSyncRequest(stopRequest, orientationConfiguration.getSyncRequestTimeout());
				Thread.sleep(orientationConfiguration.getStepMillis());
			}
			gyroscopeResponse = getGyroscopeResponse();
			boolean expected = isExpected(orientationRequest, gyroscopeResponse);
			orientationResponse.setSuccess(expected);
			log.debug(this.getAkibotClient() + ": Orientation: " + expected);

			orientationResponse.setNorthDegreesXY(gyroscopeResponse.getNorthDegreesXY());
			broadcastResponse(orientationResponse, orientationRequest);

		} else {
			throw new InvalidOrientationRequestException(orientationRequest.toString());
		}
	}

	private GyroscopeResponse getGyroscopeResponse() throws FailedToSendMessageException {
		return (GyroscopeResponse) sendSyncRequest(gyroscopeValueRequest, orientationConfiguration.getSyncRequestTimeout());
	}

	@Override
	public OrientationConfiguration getComponentConfiguration() {
		return this.orientationConfiguration;
	}
}
