package com.akibot.jme3.test;

import java.net.InetSocketAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.jme3.component.visualizer.VisualizerRequest;
import com.akibot.jme3.component.visualizer.utils.AkiPoint;

public class VisualizerTest {

	private static AkibotClient testClient;
	private final static String serverHost = "raspberrypi";
	private final static int serverPort = 2000;
	private final static InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testClient = new AkibotClient("akibot.client", new TestComponent(), serverAddress);
		testClient.getMyClientDescription().getTopicList().add(new Response());

		testClient.start();
		Thread.sleep(5000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testMovement() throws FailedToSendMessageException, InterruptedException {
		VisualizerRequest request = new VisualizerRequest();

		request.setAkiPoint(new AkiPoint(10, 20, 30));
		testClient.getOutgoingMessageManager().broadcastMessage(request);
		Thread.sleep(1000);

		request.setAkiPoint(new AkiPoint(100, 200, 300));
		testClient.getOutgoingMessageManager().broadcastMessage(request);
		Thread.sleep(1000);

		request.setAkiPoint(new AkiPoint(1000, 2000, 3000));
		testClient.getOutgoingMessageManager().broadcastMessage(request);
		Thread.sleep(1000);

	}

}
