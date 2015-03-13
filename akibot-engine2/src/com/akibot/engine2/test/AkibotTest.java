package com.akibot.engine2.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.network.ClientDescription;
import com.akibot.engine2.network.ClientDescriptionUtils;
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

		// testRequest.setX(-1);
		// testResponse = (TestResponse)
		// clientA.getOutgoingMessageManager().sendSyncRequest(testRequest,
		// 1000);
		// assertEquals("Chect response", (Integer) 0, (Integer)
		// testResponse.getResult());
	}

	@Test
	public void testClientSimpleMerge() {
		ClientDescription me = new ClientDescription("one", new InetSocketAddress("localhost", 1000));
		ClientDescription client2a = new ClientDescription("two", new InetSocketAddress("localhost", 1001));
		ClientDescription client2b = new ClientDescription("two", new InetSocketAddress("localhost", 1002));
		ClientDescription client2c = new ClientDescription("two_new", new InetSocketAddress("localhost", 1002));

		List<ClientDescription> clientDescriptionList = new ArrayList<ClientDescription>();
		clientDescriptionList.add(me);

		assertEquals("Client 1 added", (Integer) 1, (Integer) clientDescriptionList.size());

		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(me, client2a, clientDescriptionList);
		assertEquals("Client 2a added", (Integer) 2, (Integer) clientDescriptionList.size());

		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(me, client2b, clientDescriptionList);
		assertEquals("Client 2b replaced 2a", (Integer) 2, (Integer) clientDescriptionList.size());

		assertEquals("Client 2a not exists", false, ClientDescriptionUtils.findByAddress(clientDescriptionList, client2a) > 0);
		assertEquals("Client 2b exists", true, ClientDescriptionUtils.findByAddress(clientDescriptionList, client2b) > 0);

		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(me, client2c, clientDescriptionList);
		assertEquals("Client 2c rename", (Integer) 2, (Integer) clientDescriptionList.size());
		assertEquals("Client 2c rename", false, ClientDescriptionUtils.findByName(clientDescriptionList, client2a) > 0);
		assertEquals("Client 2c rename", false, ClientDescriptionUtils.findByName(clientDescriptionList, client2b) > 0);
		assertEquals("Client 2c rename", true, ClientDescriptionUtils.findByName(clientDescriptionList, client2c) > 0);
	}

	@Test
	public void testClientListMerge() {
		ClientDescription me = new ClientDescription("one", new InetSocketAddress("localhost", 1000));
		ClientDescription client2a = new ClientDescription("two", new InetSocketAddress("localhost", 1001));
		ClientDescription client2b = new ClientDescription("two", new InetSocketAddress("localhost", 1002));
		ClientDescription client2c = new ClientDescription("two_new", new InetSocketAddress("localhost", 1002));

		List<ClientDescription> clientDescriptionList = new ArrayList<ClientDescription>();
		clientDescriptionList.add(me);
		clientDescriptionList.add(client2a);

		List<ClientDescription> newList1 = new ArrayList<ClientDescription>();
		newList1.add(client2a);
		clientDescriptionList = ClientDescriptionUtils.mergeList(me, newList1, clientDescriptionList);
		assertEquals("Client 2a received (no changes)", (Integer) 2, (Integer) clientDescriptionList.size());

		List<ClientDescription> newList2 = new ArrayList<ClientDescription>();
		newList2.add(client2b);
		clientDescriptionList = ClientDescriptionUtils.mergeList(me, newList2, clientDescriptionList);
		assertEquals("Client 2b received", (Integer) 2, (Integer) clientDescriptionList.size());
		assertEquals("Client 2b received", false, ClientDescriptionUtils.findByAddress(clientDescriptionList, client2a) > 0);
		assertEquals("Client 2b received", true, ClientDescriptionUtils.findByAddress(clientDescriptionList, client2b) > 0);

		List<ClientDescription> newList3 = new ArrayList<ClientDescription>();
		newList3.add(client2c);
		clientDescriptionList = ClientDescriptionUtils.mergeList(me, newList3, clientDescriptionList);
		assertEquals("Client 2c received", (Integer) 2, (Integer) clientDescriptionList.size());
		assertEquals("Client 2c received", false, ClientDescriptionUtils.findByName(clientDescriptionList, client2a) > 0);
		assertEquals("Client 2c received", false, ClientDescriptionUtils.findByName(clientDescriptionList, client2b) > 0);
		assertEquals("Client 2c received", true, ClientDescriptionUtils.findByName(clientDescriptionList, client2c) > 0);

	}

}
