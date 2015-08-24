package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.awtcontroller.AwtControllerComponent;

public class AwtControllerLauncher {
	static final AkiLogger log = AkiLogger.create(AwtControllerLauncher.class);

	public static void main(String[] args) throws Exception {
		// AAA
		String dnsHost = Constants.DNS_HOST;
		int dnsPort = Constants.DNS_PORT;
		InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		AkibotClient awtController = new AkibotClient("akibot.awtcontroller", new AwtControllerComponent(), dnsAddress);

		awtController.start();

		System.out.println("AwtControllerLauncher: Started");
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
