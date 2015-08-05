package com.akibot.web.listener;

import java.net.InetSocketAddress;

import javax.servlet.ServletContextEvent;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedClientConstructorException;
import com.akibot.engine2.network.AkibotClient;

public class AkibotWebListener implements javax.servlet.ServletContextListener {
	private AkibotClient webComponent;
	private boolean initialized;

	public AkibotClient getWebComponent() {
		return webComponent;
	}

	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("** AkibotWebListener: contextDestroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("** AkibotWebListener: initializing...");
		init();
	}

	private void init() {
		initialized = false;
		String dnsHost = "localhost"; // Constants.DNS_HOST;
		int dnsPort = 2010; // Constants.DNS_PORT;

		try {
			InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);
			this.webComponent = new AkibotClient("akibot.web", new DefaultComponent(), dnsAddress);
			this.webComponent.start();
			initialized = true;
			System.out.println("** AkibotWebListener: initialized");
		} catch (FailedClientConstructorException e) {
			System.out.println("** AkibotWebListener: failed");
			e.printStackTrace();
		}

	}

}