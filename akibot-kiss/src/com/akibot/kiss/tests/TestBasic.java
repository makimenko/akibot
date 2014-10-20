package com.akibot.kiss.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.akibot.kiss.server.Client;
import com.akibot.kiss.server.ClientDescription;
import com.akibot.kiss.server.Server;
import com.akibot.kiss.tests.messages.TestComponent;
import com.akibot.kiss.tests.messages.TestRequest;
import com.akibot.kiss.tests.messages.TestResponse;

public class TestBasic {

	@Test
	public void test() throws IOException {
		try {
			String host = "localhost";
			int port = 2001;
			Server server = new Server(port);
			server.start();

			TestComponent componentA = new TestComponent();
			ClientDescription componentADescription = new ClientDescription("test.a");
			componentADescription.getTopicList().add(new TestRequest());
			Client componentAClient = new Client(host, port, componentA, componentADescription);
			componentAClient.start();

			TestComponent componentB = new TestComponent();
			ClientDescription componentBDescription = new ClientDescription("test.b");
			componentBDescription.getTopicList().add(new TestResponse());
			Client componentBClient = new Client(host, port, componentB, componentBDescription);
			componentBClient.start();

			TestRequest request = new TestRequest();
			TestResponse response;

			request.setX(9);
			response = (TestResponse) componentBClient.syncRequest(request, 1000);
			assertEquals("Check reseponse value", (Integer) 10, (Integer) response.getResult());

			request.setX(4);
			response = (TestResponse) componentBClient.syncRequest(request, 1000);
			assertEquals("Check reseponse value", (Integer) 5, (Integer) response.getResult());

		} catch (Exception e) {
			fail(e.toString());
		}

	}

}
