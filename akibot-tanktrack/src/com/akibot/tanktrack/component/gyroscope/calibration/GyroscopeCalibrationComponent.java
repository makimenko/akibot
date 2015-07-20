package com.akibot.tanktrack.component.gyroscope.calibration;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.gyroscope.GyroscopeOffsetConfiguration;
import com.akibot.tanktrack.component.gyroscope.GyroscopeOffsetConfigurationRequest;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;

public class GyroscopeCalibrationComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(GyroscopeCalibrationComponent.class);

	double minX;
	double minY;
	double minZ;
	double maxX;
	double maxY;
	double maxZ;

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof GyroscopeCalibrationRequest) {
			onGyroscopeCalibrationRequest((GyroscopeCalibrationRequest) message);
		} else if (message instanceof GyroscopeResponse) {
			// Nothing
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	@Override
	public void loadDefaults() {
		addTopic(new GyroscopeCalibrationRequest());
		addTopic(new GyroscopeResponse());
		getComponentStatus().setReady(true);
	}

	private void onGyroscopeCalibrationRequest(GyroscopeCalibrationRequest gyroscopeCalibrationRequest) throws FailedToSendMessageException,
			InterruptedException {
		GyroscopeCalibrationResponse response = new GyroscopeCalibrationResponse();

		GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
		gyroscopeValueRequest.setIgnoreOffset(true); // real values needed

		long startTime = System.currentTimeMillis();
		resetStats();

		while (System.currentTimeMillis() - startTime < gyroscopeCalibrationRequest.getDurationMilliseconds()) {
			GyroscopeResponse gyroscopeResponse = (GyroscopeResponse) sendSyncRequest(gyroscopeValueRequest,
					(int) gyroscopeCalibrationRequest.getDurationMilliseconds());
			updateStats(gyroscopeResponse.getX(), gyroscopeResponse.getY(), gyroscopeResponse.getZ());
			Thread.sleep(gyroscopeCalibrationRequest.getSleepMilliseconds());
		}

		double offsetX = (maxX + minX) / 2;
		double offsetY = (maxY + minY) / 2;
		double offsetZ = (maxZ + minZ) / 2;
		response.setNewOffsetX(offsetX);
		response.setNewOffsetY(offsetY);
		response.setNewOffsetZ(offsetZ);

		if (gyroscopeCalibrationRequest.isUpdateConfiguration()) {
			GyroscopeOffsetConfigurationRequest gyroscopeOffsetConfigurationRequest = new GyroscopeOffsetConfigurationRequest();
			GyroscopeOffsetConfiguration gyroscopeOffsetConfiguration = new GyroscopeOffsetConfiguration();
			gyroscopeOffsetConfiguration.setOffsetX(offsetX);
			gyroscopeOffsetConfiguration.setOffsetY(offsetY);
			gyroscopeOffsetConfiguration.setOffsetZ(offsetZ);
			gyroscopeOffsetConfigurationRequest.setGyroscopeOffsetConfiguration(gyroscopeOffsetConfiguration);
			broadcastMessage(gyroscopeOffsetConfigurationRequest);
		}
		broadcastResponse(response, gyroscopeCalibrationRequest);
	}

	private void resetStats() {
		minX = 100000;
		minY = 100000;
		minZ = 100000;
		maxX = -100000;
		maxY = -100000;
		maxZ = -100000;
	}

	private void updateStats(double x, double y, double z) {
		if (x < minX) {
			minX = x;
		}
		if (y < minY) {
			minY = y;
		}
		if (z < minZ) {
			minZ = z;
		}
		if (x > maxX) {
			maxX = x;
		}
		if (y > maxY) {
			maxY = y;
		}
		if (z > maxZ) {
			maxZ = z;
		}
	}

}
