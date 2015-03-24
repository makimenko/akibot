package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.network.ClientDescription;
import com.akibot.engine2.network.ClientDescriptionUtils;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.engine2.test.component.TestResponse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StressTest {
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
	public void asyncReliability() throws FailedToSendMessageException, InterruptedException {
		TestRequest testRequest = new TestRequest();

		int totalCount = 10;
		((TestComponent) testClient.getComponent()).setArray(new int[totalCount]);
		for (int i = 0; i < totalCount; i++) {
			testRequest.setX(i);
			testClient.getOutgoingMessageManager().broadcastMessage(testRequest);
		}
		Thread.sleep(totalCount * 500);
		int[] array = ((TestComponent) testClient.getComponent()).getArray();
		int ok = 0;

		assertEquals("Array length", totalCount, array.length);

		for (int i = 0; i < array.length; i++) {
			if (array[i] == 1) {
				ok++;
			}
		}
		assertEquals("Receved requests", totalCount, ok);
	}

	@Test
	public void clients() throws Exception {
		int totalClients = 20;

		List clients = new ArrayList<AkibotClient>();

		for (int i = 1; i <= totalClients; i++) {
			AkibotClient client = new AkibotClient("akibot.client." + i, new TestComponent(), serverAddress);
			client.getMyClientDescription().getTopicList().add(new Response());
			clients.add(client);
			client.start();
		}

		Thread.sleep(totalClients * 2000);

		ClientDescriptionUtils utils = new ClientDescriptionUtils();

		Iterator iter = clients.iterator();
		while (iter.hasNext()) {
			AkibotClient client = (AkibotClient) iter.next();
			for (int i = 1; i <= totalClients; i++) {
				String name = "akibot.client." + i;
				if (!name.equals(client.getName())) {
					ClientDescription clientDescription = new ClientDescription(name, null);
					assertEquals(client + " has " + name, true, ClientDescriptionUtils.findByName(client.getClientDescriptionList(), clientDescription) > 0);
				}

			}
		}

	}

	@Test
	public void syncReliability() throws FailedToSendMessageException, InterruptedException {
		TestRequest testRequest = new TestRequest();
		int totalCount = 10;
		for (int i = 0; i < totalCount; i++) {
			TestResponse testResponse = (TestResponse) testClient.getOutgoingMessageManager().sendSyncRequest(testRequest, 2000);
		}
	}

}
