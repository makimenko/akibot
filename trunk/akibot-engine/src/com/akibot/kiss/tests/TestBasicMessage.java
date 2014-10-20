package com.akibot.kiss.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.akibot.kiss.server.Client;
import com.akibot.kiss.server.ClientDescription;
import com.akibot.kiss.server.Server;
import com.akibot.kiss.tests.messages.TestComponent;
import com.akibot.kiss.tests.messages.TestRequest;
import com.akibot.kiss.tests.messages.TestResponse;

public class TestBasicMessage {
	TestComponent componentA;
	Client componentAClient;
	TestComponent componentB;

	Client componentBClient;
	private String host = "localhost";
	private int port = 2001;
	private Server server;

	@Before
	public void setUp() {
		try {
			server = new Server(port);
			server.start();

			componentA = new TestComponent();
			ClientDescription componentADescription = new ClientDescription("test.a");
			componentADescription.getTopicList().add(new TestRequest());
			componentAClient = new Client(host, port, componentA, componentADescription);
			componentAClient.start();

			componentB = new TestComponent();
			ClientDescription componentBDescription = new ClientDescription("test.b");
			componentBDescription.getTopicList().add(new TestResponse());
			componentBClient = new Client(host, port, componentB, componentBDescription);
			componentBClient.start();

		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@After
	public void tearDown() {
		server.stop();
	}

	@Test
	public void testAsyncMessage() throws IOException, InterruptedException {
		TestRequest request = new TestRequest();
		TestResponse response;

		request.setX(7);
		componentBClient.send(request);
		Thread.sleep(1000); // wait for some time
		response = componentA.getLastResponse();
		assertEquals("Check async reseponse value", (Integer) 8, response.getResult());

		// request.setX(4);
		// response = (TestResponse) componentBClient.syncRequest(request,
		// 1000);
		// assertEquals("Check reseponse value", (Integer) 5, (Integer)
		// response.getResult());

	}

	@Test
	public void testSyncMessage() throws IOException, InterruptedException, CloneNotSupportedException {
		TestRequest request = new TestRequest();
		TestResponse response;

		request.setX(9);
		response = (TestResponse) componentBClient.syncRequest(request, 1000);
		assertEquals("Check reseponse value", (Integer) 10, response.getResult());

		request.setX(4);
		response = (TestResponse) componentBClient.syncRequest(request, 1000);
		assertEquals("Check reseponse value", (Integer) 5, response.getResult());
	}

}
