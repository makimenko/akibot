package com.akibot.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LoggerTest {

	private static final Logger logger = LogManager.getLogger(LoggerTest.class);

	@Test
	public void testLogMessage() {
		logger.info("Hello");

	}
}
