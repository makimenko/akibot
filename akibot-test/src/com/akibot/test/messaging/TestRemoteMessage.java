package com.akibot.test.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.akibot.engine.exception.FailedToSendMessageException;
import com.akibot.engine.server.Client;
import com.akibot.engine.server.ClientDescription;
import com.akibot.engine.server.Server;
import com.akibot.test.object.TestComponent;
import com.akibot.test.object.TestRequest;
import com.akibot.test.object.TestResponse;

public class TestRemoteMessage {
	TestComponent componentA;
	Client componentAClient;
	TestComponent componentB;

	Client componentBClient;
	private String host = "raspberrypi";
	private int port = 2002;

	@Before
	public void setUp() {
		try {

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
		componentAClient.stop();
		componentBClient.stop();
	}

	@Test
	public void testAsyncMessage() throws InterruptedException, FailedToSendMessageException {
		TestRequest request = new TestRequest();
		TestResponse response;

		request.setX(7);
		componentBClient.send(request);
		Thread.sleep(1000); // wait for some time
		response = componentA.getLastResponse();
		assertEquals("Check async reseponse value 1", (Integer) 8, response.getResult());

		request.setX(4);
		componentBClient.send(request);
		Thread.sleep(1000); // wait for some time
		response = componentA.getLastResponse();
		assertEquals("Check async response value 2", (Integer) 5, (Integer) response.getResult());

	}

	@Test
	public void testSyncMessage() throws InterruptedException, FailedToSendMessageException {
		TestRequest request = new TestRequest();
		TestResponse response;

		request.setX(9);
		response = (TestResponse) componentBClient.syncRequest(request, 500);
		assertEquals("Check sync response value 1", (Integer) 10, response.getResult());

		request.setX(-1);
		response = (TestResponse) componentBClient.syncRequest(request, 500);
		assertEquals("Check sync response value 2", (Integer) 0, response.getResult());
	}

	@Test
	public void testPerformanceSyncMessage() throws InterruptedException, FailedToSendMessageException, Exception {
		TestRequest request = new TestRequest();
		TestResponse response;
		long startTime = System.currentTimeMillis();
		for (int i = 0; i <= 1000; i++) {
			long interim = System.currentTimeMillis();
			request.setX(i);
			response = (TestResponse) componentBClient.syncRequest(request, 1000);
			if (response.getResult() != i + 1) {
				throw new Exception("Invalid result");
			}
			System.out.println("Interim msec: "+(System.currentTimeMillis()-interim));
		}
		assertEquals("Chech performance (1k per second)", true, (System.currentTimeMillis() - startTime < 1000));
	}

}
