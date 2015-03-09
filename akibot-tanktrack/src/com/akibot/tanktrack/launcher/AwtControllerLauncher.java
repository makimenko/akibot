package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.awtcontroller.AwtControllerComponent;

public class AwtControllerLauncher {
	static final Logger log = LogManager.getLogger(AwtControllerLauncher.class.getName());

	public static void main(String[] args) throws Exception {

		String serverHost = "raspberrypi";
		int serverPort = 2000;

		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		AkibotClient awtController = new AkibotClient("akibot.awtcontroller", new AwtControllerComponent(), serverAddress);
		awtController.getMyClientDescription().getTopicList().add(new Response());

		/*
		 * AkibotClient orientation = new AkibotClient("akibot.orientation", new
		 * OrientationComponent("akibot.tanktrack", "akibot.gyroscope"),
		 * serverAddress);
		 * orientation.getMyClientDescription().getTopicList().add(new
		 * OrientationRequest());
		 * orientation.getMyClientDescription().getTopicList().add(new
		 * Response());
		 * 
		 * AkibotClient gyroscopeCalibration = new
		 * AkibotClient("akibot.gyroscope.calibration", new
		 * GyroscopeCalibrationComponent(), serverAddress);
		 * gyroscopeCalibration.getMyClientDescription().getTopicList().add(new
		 * GyroscopeCalibrationRequest());
		 * gyroscopeCalibration.getMyClientDescription().getTopicList().add(new
		 * GyroscopeResponse());
		 */
		awtController.start();
		// orientation.start();
		// gyroscopeCalibration.start();

		// LOOP forever:

		while (true) {
			Thread.sleep(10000);
		}

	}

}
