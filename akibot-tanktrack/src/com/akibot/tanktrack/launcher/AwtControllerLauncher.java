package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.awtcontroller.AwtControllerComponent;
import com.akibot.tanktrack.component.gyroscope.calibration.GyroscopeCalibrationComponent;

public class AwtControllerLauncher {
	static final AkiLogger log = AkiLogger.create(AwtControllerLauncher.class);

	public static void main(String[] args) throws Exception {
		// AAA
		String serverHost = Constants.SERVER_HOST;
		int serverPort = Constants.SERVER_PORT;
		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		AkibotClient awtController = new AkibotClient("akibot.awtcontroller", new AwtControllerComponent(), serverAddress);
		AkibotClient gyroscopeCalibration = new AkibotClient("akibot.gyroscope.calibration", new GyroscopeCalibrationComponent(), serverAddress);

		awtController.start();
		gyroscopeCalibration.start();

		System.out.println("AwtControllerLauncher: Started");
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
