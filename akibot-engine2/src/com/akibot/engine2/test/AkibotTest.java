package com.akibot.engine2.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;

import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.engine2.test.component.TestResponse;

public class AkibotTest {
	private static AkibotClient clientA;
	private static AkibotClient clientB;
	private static AkibotClient server;

	@BeforeClass
	public static void onceExecutedBeforeAll() throws Exception {
		String serverHost = "localhost";
		int serverPort = 2001;
		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		server = new AkibotClient("akibot.server", new DefaultComponent(), serverPort);
		server.start();

		clientA = new AkibotClient("akibot.clientA", new TestComponent(), serverAddress);
		clientA.getMyClientDescription().getTopicList().add(new TestResponse());
		clientA.start();

		clientB = new AkibotClient("akibot.clientB", new TestComponent(), serverAddress);
		clientB.getMyClientDescription().getTopicList().add(new TestRequest());
		clientB.start();

		Thread.sleep(1000);

	}

	@Test
	public void testAsyncRequest() throws FailedToSendMessageException, InterruptedException {
		TestRequest testRequest = new TestRequest();

		testRequest.setX(1);
		clientA.getOutgoingMessageManager().broadcastMessage(testRequest);
		Thread.sleep(100);

		TestComponent testComponentA = (TestComponent) clientA.getComponent();
		assertEquals("Chect response", (Integer) 2, (Integer) testComponentA.getLastTestResponse().getResult());

	}

	@Test
	public void testSyncRequest() throws FailedToSendMessageException {
		TestRequest testRequest = new TestRequest();
		TestResponse testResponse;

		testRequest.setX(1);
		testResponse = (TestResponse) clientA.getOutgoingMessageManager().sendSyncRequest(testRequest, 1000);
		assertEquals("Chect response", (Integer) 2, (Integer) testResponse.getResult());

		//testRequest.setX(-1);
		//testResponse = (TestResponse) clientA.getOutgoingMessageManager().sendSyncRequest(testRequest, 1000);
		//assertEquals("Chect response", (Integer) 0, (Integer) testResponse.getResult());
	}

}
