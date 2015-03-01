package com.akibot.engine2.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.engine2.test.component.TestResponse;

public class AkibotTest {
	private static AkibotClient clientNodeA;
	private static AkibotClient clientNodeB;
	private static AkibotClient serverNode;

	@BeforeClass
	public static void onceExecutedBeforeAll() throws SocketException, UnknownHostException, InterruptedException {
		String serverHost = "localhost";
		int serverPort = 2001;
		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		serverNode = new AkibotClient("akibot.server", new DefaultComponent(), serverPort);
		serverNode.start();

		clientNodeA = new AkibotClient("akibot.clientA", new TestComponent(), serverAddress);
		clientNodeA.getMyClientDescription().getTopicList().add(new TestResponse());
		clientNodeA.start();

		clientNodeB = new AkibotClient("akibot.clientB", new TestComponent(), serverAddress);
		clientNodeB.getMyClientDescription().getTopicList().add(new TestRequest());
		clientNodeB.start();

		// Thread.sleep(1000);

	}

	@Test
	public void testAsyncRequest() throws FailedToSendMessageException, InterruptedException {
		TestRequest testRequest = new TestRequest();

		testRequest.setX(1);
		clientNodeA.getOutgoingMessageManager().broadcastMessage(testRequest);
		Thread.sleep(100);

		TestComponent testComponentA = (TestComponent) clientNodeA.getComponent();
		assertEquals("Chect response", (Integer) 2, (Integer) testComponentA.getLastTestResponse().getResult());

	}

	@Test
	public void testSyncRequest() throws FailedToSendMessageException {
		TestRequest testRequest = new TestRequest();
		TestResponse testResponse;

		testRequest.setX(1);
		testResponse = (TestResponse) clientNodeA.getOutgoingMessageManager().sendSyncRequest(testRequest, 1000);
		assertEquals("Chect response", (Integer) 2, (Integer) testResponse.getResult());

		testRequest.setX(-1);
		testResponse = (TestResponse) clientNodeA.getOutgoingMessageManager().sendSyncRequest(testRequest, 1000);
		assertEquals("Chect response", (Integer) 0, (Integer) testResponse.getResult());
	}

}
