package com.akibot.tanktrack.component.orientation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;

public class OrientationComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(OrientationComponent.class.getName());
	private String tankTrackName;
	private String gyroscopeName;

	public OrientationComponent(String tankTrackName, String gyroscopeName) {
		this.tankTrackName = tankTrackName;
		this.gyroscopeName = gyroscopeName;
	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof OrientationRequest) {
			OrientationRequest orientationRequest = (OrientationRequest) message;
			if (orientationRequest.getNorthDegrreesXY() >= 0 && orientationRequest.getNorthDegrreesXY() <= 360
					&& orientationRequest.getPrecissionDegrees() > 0 && orientationRequest.getPrecissionDegrees() < 360
					&& orientationRequest.getTimeoutMillis() > 0 && orientationRequest.getTimeoutMillis() <= 60000) {

				log.debug("OrientationRequest: " + orientationRequest);
				long startTimeMills = System.currentTimeMillis();
				int syncRequestTimeout = 2000; // 2 second timeout
				long stepMillis = 100; // TODO: Configuratble
				long totalDegrees = 360;

				GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
				gyroscopeValueRequest.setTo(gyroscopeName);
				StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);
				StickMotionRequest leftRequest = new StickMotionRequest(DirectionType.LEFT);
				StickMotionRequest rightRequest = new StickMotionRequest(DirectionType.RIGHT);
				stopRequest.setTo(tankTrackName);
				leftRequest.setTo(tankTrackName);
				rightRequest.setTo(tankTrackName);

				GyroscopeResponse gyroscopeResponse = new GyroscopeResponse();
				int lastDirection = 0;

				while (System.currentTimeMillis() - startTimeMills < orientationRequest.getTimeoutMillis()) {
					gyroscopeResponse = (GyroscopeResponse) getClient().syncRequest(gyroscopeValueRequest, syncRequestTimeout);

					if (isExpected(orientationRequest, gyroscopeResponse)) {
						log.debug("Orientation Succeeded");
						getClient().send(stopRequest);
						OrientationResponse successOrientationResponse = new OrientationResponse();
						successOrientationResponse.setSuccess(true);
						successOrientationResponse.setNorthDegrreesXY(gyroscopeResponse.getNorthDegrreesXY());
						getClient().send(successOrientationResponse);
						return;
					} else {
						double aXY = gyroscopeResponse.getNorthDegrreesXY();
						double eXY = orientationRequest.getNorthDegrreesXY();

						double positive = (aXY < eXY ? eXY - aXY : eXY - aXY + totalDegrees);
						double negative = (aXY < eXY ? eXY - aXY - totalDegrees : eXY - aXY);

						if (positive <= Math.abs(negative) && lastDirection != -1) {
							lastDirection = -1;
							getClient().send(leftRequest);
						} else if (positive > Math.abs(negative) && lastDirection != +1) {
							lastDirection = +1;
							getClient().send(rightRequest);
						}

						Thread.sleep(stepMillis);
					}
				}
				log.debug("Orientation Failed");
				getClient().send(stopRequest);
				OrientationResponse failedOrientationResponse = new OrientationResponse();
				failedOrientationResponse.setSuccess(false);
				failedOrientationResponse.setNorthDegrreesXY(gyroscopeResponse.getNorthDegrreesXY());
				getClient().send(failedOrientationResponse);

			} else {
				log.error("Invalid Orientation Request");
			}

		}
	}

	public boolean isExpected(OrientationRequest orientationRequest, GyroscopeResponse gyroscopeResponse) {
		double aXY = gyroscopeResponse.getNorthDegrreesXY();
		double eXY = orientationRequest.getNorthDegrreesXY();
		double ePrecission = orientationRequest.getPrecissionDegrees();

		double minXY = eXY - ePrecission;
		double maxXY = eXY + ePrecission;

		return (aXY >= minXY && aXY <= maxXY); // TODO: implement round-robin

	}

}
