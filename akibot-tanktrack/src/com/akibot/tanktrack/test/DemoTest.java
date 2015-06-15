package com.akibot.tanktrack.test;

import java.net.InetSocketAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;

public class DemoTest {
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

}
