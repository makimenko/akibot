package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultDNSComponent;
import com.akibot.engine2.component.configuration.ConfigurationComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;

public class AkiBotWebTestLauncher {
	static final AkiLogger log = AkiLogger.create(AkiBotWebTestLauncher.class);

	public static void main(String[] args) throws Exception {
		String dnsHost = Constants.DNS_WEBTEST_HOST;
		int dnsPort = Constants.DNS_WEBTEST_PORT;

		InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		// DNS:
		AkibotClient dns = new AkibotClient("akibot.dns", new DefaultDNSComponent(), dnsPort);
		dns.start();

		// ConfigurationComponent:
		AkibotClient configClient = new AkibotClient("akibot.config", new ConfigurationComponent("."), dnsAddress);

		// Start all
		configClient.start();
		Thread.sleep(1000);

		System.out.println("AkiBotLauncher: Started");
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
