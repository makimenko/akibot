package com.akibot.kiss.component.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.akibot.kiss.server.Server;

public class TestBasic {

	@Test
	public void test() throws IOException {
		try {
			int port = 2001;
			Server server = new Server(port);
			server.start();
			
			
		} catch (Exception e) {
			fail(e.toString());
		}

	}

}
