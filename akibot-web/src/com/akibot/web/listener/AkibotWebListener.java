package com.akibot.web.listener;

import javax.servlet.ServletContextEvent;

public class AkibotWebListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("** AkibotWebListener: contextDestroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("** AkibotWebListener: initializing...");
		AkiBotWebMaster.init();
	}

}