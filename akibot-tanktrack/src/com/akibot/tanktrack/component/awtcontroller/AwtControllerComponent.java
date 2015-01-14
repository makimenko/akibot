package com.akibot.tanktrack.component.awtcontroller;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.calibration.GyroscopeCalibrationResponse;

public class AwtControllerComponent extends DefaultComponent {
	AwtControllerAppl appl;

	public AwtControllerComponent() {

	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof DistanceResponse) {
			DistanceResponse distanceResponse = (DistanceResponse) message;
			appl.getTextArea().append(distanceResponse.getFrom() + ": " + distanceResponse + "\n");
		} else if (message instanceof GyroscopeResponse) {
			GyroscopeResponse response = (GyroscopeResponse) message;
			appl.getTextArea().append(response.getFrom() + ": " + response + "\n");
		} else if (message instanceof GyroscopeCalibrationResponse) {
			GyroscopeCalibrationResponse response = (GyroscopeCalibrationResponse) message;
			appl.getTextArea().append(response.getFrom() + ": " + response + "\n");
		}
		super.processMessage(message);
	}

	@Override
	public void run() {
		this.appl = new AwtControllerAppl(this.getClient());
		appl.start();
	}

}
