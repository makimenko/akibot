package com.akibot.tanktrack.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.message.Response;
import com.akibot.engine.server.Client;
import com.akibot.engine.server.ClientDescription;
import com.akibot.tanktrack.component.awtcontroller.AwtControllerComponent;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.calibration.GyroscopeCalibrationComponent;
import com.akibot.tanktrack.component.gyroscope.calibration.GyroscopeCalibrationRequest;
import com.akibot.tanktrack.component.obstacle.ObstacleComponent;
import com.akibot.tanktrack.component.obstacle.ObstacleRequest;
import com.akibot.tanktrack.component.orientation.OrientationComponent;
import com.akibot.tanktrack.component.orientation.OrientationRequest;

public class AwtControllerLauncher {
	static final Logger log = LogManager.getLogger(AwtControllerLauncher.class.getName());

	public static void main(String[] args) throws Exception {

		String host = "raspberrypi";
		int port = 2000;

		AwtControllerComponent awtControllerComponent = new AwtControllerComponent();
		ClientDescription awtControllerDescription = new ClientDescription("akibot.awtcontroller");
		awtControllerDescription.getTopicList().add(new Response());
		Client awtControllerClient = new Client(host, port, awtControllerComponent, awtControllerDescription);

		OrientationComponent orientationComponent = new OrientationComponent("akibot.tanktrack", "akibot.gyroscope");
		ClientDescription orientationDescription = new ClientDescription("akibot.orientation");
		orientationDescription.getTopicList().add(new OrientationRequest());
		orientationDescription.getTopicList().add(new Response());
		Client orientationClient = new Client(host, port, orientationComponent, orientationDescription);

		GyroscopeCalibrationComponent gyroscopeCalibrationComponent = new GyroscopeCalibrationComponent();
		ClientDescription gyroscopeCalibrationDescription = new ClientDescription("akibot.gyroscope.calibration");
		gyroscopeCalibrationDescription.getTopicList().add(new GyroscopeCalibrationRequest());
		gyroscopeCalibrationDescription.getTopicList().add(new GyroscopeResponse());
		Client gyroscopeCalibrationClient = new Client(host, port, gyroscopeCalibrationComponent, gyroscopeCalibrationDescription);

		ObstacleComponent obstacleComponent = new ObstacleComponent();
		ClientDescription obstacleDescription = new ClientDescription("akibot.obstacle");
		obstacleDescription.getTopicList().add(new ObstacleRequest());
		obstacleDescription.getTopicList().add(new GyroscopeResponse());
		obstacleDescription.getTopicList().add(new DistanceResponse());
		Client obstacleClient = new Client(host, port, obstacleComponent, obstacleDescription);

		awtControllerClient.start();
		orientationClient.start();
		gyroscopeCalibrationClient.start();
		obstacleClient.start();

		// LOOP forever:

		while (true) {
			Thread.sleep(10000);
		}

	}

}
