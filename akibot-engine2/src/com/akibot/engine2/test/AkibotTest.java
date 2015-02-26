package com.akibot.engine2.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.engine2.test.component.TestResponse;

public class AkibotTest {
	AkibotClient serverNode;
	AkibotClient clientNodeA;
	AkibotClient clientNodeB;

	@Before
	public void setUp() throws SocketException, UnknownHostException, InterruptedException {

		String serverHost = "localhost";
		int serverPort = 2001;
		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		serverNode = new AkibotClient(new DefaultComponent("akibot.server"), serverPort);
		serverNode.start();

		clientNodeA = new AkibotClient(new TestComponent("akibot.clientA"), serverAddress);
		clientNodeA.getComponent().getMyClientDescription().getTopicList().add(new TestResponse());
		clientNodeA.start();

		clientNodeB = new AkibotClient(new TestComponent("akibot.clientB"), serverAddress);
		clientNodeB.getComponent().getMyClientDescription().getTopicList().add(new TestRequest());
		clientNodeB.start();

		// Thread.sleep(1000);

	}

	@After
	public void tearDown() {

	}

	@Test
	public void test() throws FailedToSendMessageException {
		TestRequest testRequest = new TestRequest();
		TestResponse testResponse;

		testRequest.setX(1);
		testResponse = (TestResponse) clientNodeA.getComponent().syncRequest(testRequest, 1000);
		assertEquals("Chect response", (Integer) 2, (Integer) testResponse.getResult());

		testRequest.setX(-1);
		testResponse = (TestResponse) clientNodeA.getComponent().syncRequest(testRequest, 1000);
		assertEquals("Chect response", (Integer) 0, (Integer) testResponse.getResult());
	}

}
