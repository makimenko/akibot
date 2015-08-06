package com.akibot.web.listener;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedClientConstructorException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.launcher.Constants;

public class AkiBotWebMaster {
	private static AkibotClient akibotWebClient;
	private static boolean initialized;

	static {
		initialized = false;
		String dnsHost = Constants.DNS_WEBTEST_HOST;
		int dnsPort = Constants.DNS_WEBTEST_PORT;

		try {
			InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);
			AkiBotWebMaster.akibotWebClient = new AkibotClient("akibot.web", new DefaultComponent(), dnsAddress);
			AkiBotWebMaster.akibotWebClient.start();

			initialized = true;
			System.out.println("** AkibotWebListener: initialized");
		} catch (FailedClientConstructorException e) {
			System.out.println("** AkibotWebListener: failed");
			e.printStackTrace();
		}
	}

	public static void init() {

	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static AkibotClient getAkibotWebClient() {
		return akibotWebClient;
	}

}
