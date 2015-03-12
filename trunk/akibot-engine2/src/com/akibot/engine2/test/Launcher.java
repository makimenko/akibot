package com.akibot.engine2.test;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.ClientDescriptionRequest;

public class Launcher {
	private static final AkiLogger log = AkiLogger.create(Launcher.class.getName());

	public static void main(String[] args) throws Exception {

		Launcher launcher = new Launcher();
		launcher.start();
	}

	public void start() throws Exception {

		ClientDescriptionRequest request = new ClientDescriptionRequest();
		request.setFrom("akibot.awtcontroller");
		request.setTo("akibot.distance");
		log.msg("akibot.awtcontroller", "IN", request);

	}

}
