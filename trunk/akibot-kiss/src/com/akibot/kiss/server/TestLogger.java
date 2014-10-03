package com.akibot.kiss.server;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



public class TestLogger {
	
	static final Logger logger = LogManager.getLogger(TestLogger.class.getName());
	

	public static void main(String[] args) throws IOException, InterruptedException {
		logger.trace("Entering application.");
		logger.info("Entering application.");
		logger.debug("Entering application.");
		
	}
}
