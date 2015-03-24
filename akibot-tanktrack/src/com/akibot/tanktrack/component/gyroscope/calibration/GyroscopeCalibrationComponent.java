package com.akibot.tanktrack.component.gyroscope.calibration;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.gyroscope.GyroscopeConfigurationRequest;
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
			GyroscopeCalibrationRequest calibrationRequest = (GyroscopeCalibrationRequest) message;
			GyroscopeCalibrationResponse response = new GyroscopeCalibrationResponse();
			response.copySyncId(message);

			if (calibrationRequest.isResetOffset()) {
				GyroscopeConfigurationRequest gyroscopeConfigurationRequest = new GyroscopeConfigurationRequest();
				gyroscopeConfigurationRequest.setOffsetX(0);
				gyroscopeConfigurationRequest.setOffsetY(0);
				gyroscopeConfigurationRequest.setOffsetZ(0);
				getAkibotClient().getOutgoingMessageManager().broadcastMessage(gyroscopeConfigurationRequest);
			}

			GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();

			long startTime = System.currentTimeMillis();
			resetStats();

			while (System.currentTimeMillis() - startTime < calibrationRequest.getDurationMilliseconds()) {
				GyroscopeResponse gyroscopeResponse = (GyroscopeResponse) getAkibotClient().getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest,
						(int) calibrationRequest.getDurationMilliseconds());
				updateStats(gyroscopeResponse.getX(), gyroscopeResponse.getY(), gyroscopeResponse.getZ());
				Thread.sleep(calibrationRequest.getSleepMilliseconds());
			}

			double offsetX = (maxX + minX) / 2;
			double offsetY = (maxY + minY) / 2;
			double offsetZ = (maxZ + minZ) / 2;
			response.setNewOffsetX(offsetX);
			response.setNewOffsetY(offsetY);
			response.setNewOffsetZ(offsetZ);

			if (calibrationRequest.isUpdateConfiguration()) {
				GyroscopeConfigurationRequest gyroscopeConfigurationRequest = new GyroscopeConfigurationRequest();
				gyroscopeConfigurationRequest.setOffsetX(offsetX);
				gyroscopeConfigurationRequest.setOffsetY(offsetY);
				gyroscopeConfigurationRequest.setOffsetZ(offsetZ);
				getAkibotClient().getOutgoingMessageManager().broadcastMessage(gyroscopeConfigurationRequest);
			}

			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		}
	}

	private void resetStats() {
		minX = 10000;
		minY = 10000;
		minZ = 10000;
		maxX = -10000;
		maxY = -10000;
		maxZ = -10000;
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
