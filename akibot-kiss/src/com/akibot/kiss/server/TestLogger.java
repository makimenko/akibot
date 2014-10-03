package com.akibot.kiss.server;

import java.io.IOException;
import java.util.logging.Logger;

public class TestLogger {
	static Logger log = Logger.getLogger(TestLogger.class.getName());

	public static void main(String[] args) throws IOException, InterruptedException {
		log.info("Hello!");
	}
}
