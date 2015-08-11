package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultDNSComponent;
import com.akibot.engine2.component.configuration.ConfigurationComponent;
import com.akibot.engine2.component.status.StatusWatchdogComponent;
import com.akibot.engine2.component.test.TestComponent;
import com.akibot.engine2.component.test.TestRequest;
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

		// Components:
		AkibotClient configClient = new AkibotClient("akibot.config", new ConfigurationComponent("."), dnsAddress);
		AkibotClient statusWatchdogClient = new AkibotClient("akibot.status.watchdog", new StatusWatchdogComponent(1 * 1000, 5 * 1000), dnsAddress);
		AkibotClient testClient = new AkibotClient("akibot.test", new TestComponent(), dnsAddress);
		testClient.getComponent().addTopic(new TestRequest());

		// Start all
		configClient.start();
		statusWatchdogClient.start();
		testClient.start();
		Thread.sleep(500);

		System.out.println("AkiBotLauncher: Started");
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
