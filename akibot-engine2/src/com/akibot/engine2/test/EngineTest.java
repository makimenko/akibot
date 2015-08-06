package com.akibot.engine2.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.DefaultDNSComponent;
import com.akibot.engine2.component.configuration.ConfigurationComponent;
import com.akibot.engine2.component.configuration.GetConfigurationRequest;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.component.configuration.PutConfigurationRequest;
import com.akibot.engine2.component.configuration.PutConfigurationResponse;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.NooneInterestedException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.network.ClientDescription;
import com.akibot.engine2.network.ClientDescriptionUtils;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestComponentWithConfig;
import com.akibot.engine2.test.component.TestConfiguration;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.engine2.test.component.TestResponse;
import com.akibot.engine2.test.component.TestResponse2;
import com.akibot.engine2.test.component.TestSleepRequest;

public class EngineTest {
	private static AkibotClient clientA;
	private static AkibotClient clientB;
	private static AkibotClient dns;
	private static InetSocketAddress dnsAddress;

	@BeforeClass
	public static void onceExecutedBeforeAll() throws Exception {
		String dnsHost = "localhost";
		int dnsPort = 2001;
		dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		dns = new AkibotClient("akibot.dns", new DefaultComponent(), dnsPort);
		dns.start();

		clientA = new AkibotClient("akibot.clientA", new TestComponent(), dnsAddress);
		clientA.getMyClientDescription().getTopicList().add(new TestResponse());
		clientA.getMyClientDescription().getTopicList().add(new TestResponse2());
		clientA.getMyClientDescription().getTopicList().add(new GetConfigurationResponse());
		clientA.getMyClientDescription().getTopicList().add(new PutConfigurationResponse());
		clientA.start();

		clientB = new AkibotClient("akibot.clientB", new TestComponent(), dnsAddress);
		clientB.getMyClientDescription().getTopicList().add(new TestRequest());
		clientB.getMyClientDescription().getTopicList().add(new TestSleepRequest());
		clientB.start();

		Thread.sleep(100);

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
		ClientDescription me = new ClientDescription("one", null, new InetSocketAddress("localhost", 1000));
		ClientDescription client2a = new ClientDescription("two", null, new InetSocketAddress("localhost", 1001));
		ClientDescription client2b = new ClientDescription("two", null, new InetSocketAddress("localhost", 1002));
		ClientDescription client2c = new ClientDescription("two_new", null, new InetSocketAddress("localhost", 1002));

		List<ClientDescription> clientDescriptionList = new ArrayList<ClientDescription>();
		clientDescriptionList.add(me);

		assertEquals("Client 1 added", (Integer) 1, (Integer) clientDescriptionList.size());

		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(clientA, client2a, clientDescriptionList);
		assertEquals("Client 2a added", (Integer) 2, (Integer) clientDescriptionList.size());

		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(clientA, client2b, clientDescriptionList);
		assertEquals("Client 2b replaced 2a", (Integer) 2, (Integer) clientDescriptionList.size());

		assertEquals("Client 2a not exists", false, ClientDescriptionUtils.findByAddress(clientDescriptionList, client2a) > 0);
		assertEquals("Client 2b exists", true, ClientDescriptionUtils.findByAddress(clientDescriptionList, client2b) > 0);

		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(clientA, client2c, clientDescriptionList);
		assertEquals("Client 2c rename", (Integer) 2, (Integer) clientDescriptionList.size());
		assertEquals("Client 2c rename", false, ClientDescriptionUtils.findByName(clientDescriptionList, client2a) > 0);
		assertEquals("Client 2c rename", false, ClientDescriptionUtils.findByName(clientDescriptionList, client2b) > 0);
		assertEquals("Client 2c rename", true, ClientDescriptionUtils.findByName(clientDescriptionList, client2c) > 0);
	}

	@Test
	public void testClientListMerge() {
		ClientDescription me = new ClientDescription("one", null, new InetSocketAddress("localhost", 1000));
		ClientDescription client2a = new ClientDescription("two", null, new InetSocketAddress("localhost", 1001));
		ClientDescription client2b = new ClientDescription("two", null, new InetSocketAddress("localhost", 1002));
		ClientDescription client2c = new ClientDescription("two_new", null, new InetSocketAddress("localhost", 1002));

		List<ClientDescription> clientDescriptionList = new ArrayList<ClientDescription>();
		clientDescriptionList.add(me);
		clientDescriptionList.add(client2a);

		List<ClientDescription> newList1 = new ArrayList<ClientDescription>();
		newList1.add(client2a);
		clientDescriptionList = ClientDescriptionUtils.mergeList(clientA, newList1, clientDescriptionList);
		assertEquals("Client 2a received (no changes)", (Integer) 2, (Integer) clientDescriptionList.size());

		List<ClientDescription> newList2 = new ArrayList<ClientDescription>();
		newList2.add(client2b);
		clientDescriptionList = ClientDescriptionUtils.mergeList(clientA, newList2, clientDescriptionList);
		assertEquals("Client 2b received", (Integer) 2, (Integer) clientDescriptionList.size());
		assertEquals("Client 2b received", false, ClientDescriptionUtils.findByAddress(clientDescriptionList, client2a) > 0);
		assertEquals("Client 2b received", true, ClientDescriptionUtils.findByAddress(clientDescriptionList, client2b) > 0);

		List<ClientDescription> newList3 = new ArrayList<ClientDescription>();
		newList3.add(client2c);
		clientDescriptionList = ClientDescriptionUtils.mergeList(clientA, newList3, clientDescriptionList);
		assertEquals("Client 2c received", (Integer) 2, (Integer) clientDescriptionList.size());
		assertEquals("Client 2c received", false, ClientDescriptionUtils.findByName(clientDescriptionList, client2a) > 0);
		assertEquals("Client 2c received", false, ClientDescriptionUtils.findByName(clientDescriptionList, client2b) > 0);
		assertEquals("Client 2c received", true, ClientDescriptionUtils.findByName(clientDescriptionList, client2c) > 0);
	}

	@Test
	public void testConcurrentSyncMessage() throws FailedToSendMessageException {
		TestSleepRequest testSleepRequest1 = new TestSleepRequest(200);
		TestSleepRequest testSleepRequest2 = new TestSleepRequest(500);
		TestResponse testResponse1 = new TestResponse();
		TestResponse testResponse2 = new TestResponse();
		try {
			testResponse1 = (TestResponse) clientA.getOutgoingMessageManager().sendSyncRequest(testSleepRequest1, 50);
		} catch (FailedToSendMessageException e) {

		}
		try {
			testResponse2 = (TestResponse) clientA.getOutgoingMessageManager().sendSyncRequest(testSleepRequest2, 1000);
		} catch (FailedToSendMessageException e) {

		}
		assertEquals("Request 2", 500, testResponse2.getResult());
	}

	@Test
	public void testConcurrentSyncMessage2() throws FailedToSendMessageException {
		TestSleepRequest testSleepRequest1 = new TestSleepRequest(200);
		TestSleepRequest testSleepRequest2 = new TestSleepRequest(501);
		TestResponse testResponse1 = new TestResponse();
		TestResponse2 testResponse2 = new TestResponse2();
		try {
			testResponse1 = (TestResponse) clientA.getOutgoingMessageManager().sendSyncRequest(testSleepRequest1, 50);
		} catch (FailedToSendMessageException e) {

		}
		try {
			testResponse2 = (TestResponse2) clientA.getOutgoingMessageManager().sendSyncRequest(testSleepRequest2, 1000);
		} catch (FailedToSendMessageException e) {

		}
		assertEquals("Request 2", 501, testResponse2.getResult());
	}

	@Test
	public void testNullMessage() throws FailedToSendMessageException {
		try {
			TestResponse testResponse = (TestResponse) clientA.getOutgoingMessageManager().sendSyncRequest(null, 100);
		} catch (FailedToSendMessageException e) {
		}
		try {
			clientA.getOutgoingMessageManager().broadcastMessage(null);
		} catch (FailedToSendMessageException e) {
		}
	}

	@Test
	public void testQuickSendOnStartup() throws Exception {
		AkibotClient clientC = new AkibotClient("akibot.clientC", new TestComponent(), dnsAddress);
		clientC.getMyClientDescription().getTopicList().add(new TestResponse());
		clientC.start();

		TestRequest request = new TestRequest();
		request.setX(1);

		TestResponse response = new TestResponse();
		try {
			response = (TestResponse) clientC.getOutgoingMessageManager().sendSyncRequest(request, 100);
			assertEquals("check (if possible)", 2, response.getResult());
		} catch (NooneInterestedException e) {
		}

		Thread.sleep(100);
		request.setX(2);
		response = (TestResponse) clientC.getOutgoingMessageManager().sendSyncRequest(request, 100);

		assertEquals("check", 3, response.getResult());
	}

	@Test
	public void testConfiguration() throws Exception {
		AkibotClient configClient = new AkibotClient("akibot.config", new ConfigurationComponent("./config"), dnsAddress);
		configClient.start();
		Thread.sleep(500);

		PutConfigurationRequest putConfigurationRequest = new PutConfigurationRequest();
		String name = "test1";
		putConfigurationRequest.setName(name);
		TestConfiguration testConfiguration = new TestConfiguration();
		testConfiguration.setX(777);
		putConfigurationRequest.setComponentConfiguration(testConfiguration);

		PutConfigurationResponse putConfigurationResponse = (PutConfigurationResponse) clientA.getOutgoingMessageManager().sendSyncRequest(
				putConfigurationRequest, 2000);

		GetConfigurationRequest getConfigurationRequest = new GetConfigurationRequest(name);
		GetConfigurationResponse getConfigurationResponse = (GetConfigurationResponse) clientA.getOutgoingMessageManager().sendSyncRequest(
				getConfigurationRequest, 2000);

		TestConfiguration resultTestConfiguration = (TestConfiguration) getConfigurationResponse.getComponentConfiguration();

		assertEquals("Compare properties", 777, resultTestConfiguration.getX());

	}

	@Test
	public void testConfigurationFileName() throws Exception {
		AkibotClient configClient = new AkibotClient("akibot.config", new ConfigurationComponent("./config"), dnsAddress);
		configClient.start();
		Thread.sleep(500);

		PutConfigurationRequest putConfigurationRequest = new PutConfigurationRequest();
		String name = "test2&78234**_21/|:\\../../";

		TestConfiguration testConfiguration = new TestConfiguration();
		testConfiguration.setX(999);
		putConfigurationRequest.setName(name);
		putConfigurationRequest.setComponentConfiguration(testConfiguration);

		PutConfigurationResponse putConfigurationResponse = (PutConfigurationResponse) clientA.getOutgoingMessageManager().sendSyncRequest(
				putConfigurationRequest, 2000);

		GetConfigurationRequest getConfigurationRequest = new GetConfigurationRequest(name);
		GetConfigurationResponse getConfigurationResponse = (GetConfigurationResponse) clientA.getOutgoingMessageManager().sendSyncRequest(
				getConfigurationRequest, 2000);

		TestConfiguration r = (TestConfiguration) getConfigurationResponse.getComponentConfiguration();

		assertEquals("Compare properties", 999, r.getX());

	}

	@Test
	public void testComponentWithConfiguration() throws Exception {
		String clientName = "akibot.clientD";
		AkibotClient clientD = new AkibotClient(clientName, new TestComponentWithConfig(), dnsAddress);
		clientD.getMyClientDescription().getTopicList().add(new TestRequest());
		clientD.start();
		Thread.sleep(100);

		TestRequest testRequest = new TestRequest();
		testRequest.setTo(clientName);
		testRequest.setX(1);

		boolean failed;

		failed = false;
		try {
			TestResponse testResponse = (TestResponse) clientA.getOutgoingMessageManager().sendSyncRequest(testRequest, 1000);
		} catch (FailedToSendMessageException e) {
			failed = true;
		}
		assertEquals("Expecting to fail (before configuration)", true, failed);

		TestConfiguration testConfiguration = new TestConfiguration();
		GetConfigurationResponse getConfigurationResponse = new GetConfigurationResponse();
		getConfigurationResponse.setComponentConfiguration(testConfiguration);
		getConfigurationResponse.setTo(clientName);
		clientA.getOutgoingMessageManager().broadcastMessage(getConfigurationResponse);
		Thread.sleep(100);

		failed = false;
		try {
			TestResponse testResponse = (TestResponse) clientA.getOutgoingMessageManager().sendSyncRequest(testRequest, 1000);
			assertEquals("Check response", 2, testResponse.getResult());
		} catch (FailedToSendMessageException e) {
			failed = true;
		}
		assertEquals("Not expecting to fail (after configuration)", false, failed);

	}

	@Test
	public void testParentClientDescription() throws Exception {
		System.out.println("=============================");
		int tempDnsPort = 2050;
		InetSocketAddress tempDnsAddress = new InetSocketAddress("192.168.0.106", tempDnsPort);

		String tmpDnsName = "akibot.tmp.dns";
		AkibotClient tmpDnsClient = new AkibotClient(tmpDnsName, new DefaultDNSComponent(), tempDnsPort);
		tmpDnsClient.start();

		AkibotClient tmpClient1 = new AkibotClient("akibot.tmp.client1", new DefaultComponent(), tempDnsAddress);
		tmpClient1.start();

		Thread.sleep(100);

		assertEquals("Check count", 1, tmpClient1.getClientDescriptionList().size());
		ClientDescription clientDescription = tmpClient1.getClientDescriptionList().get(0);
		System.out.println("** tmpDnsClient = " + tmpDnsClient.getMyClientDescription());
		System.out.println("** " + tmpClient1.getClientDescriptionList());
		System.out.println("** result = " + clientDescription);

		assertEquals("Check name", tmpDnsName, clientDescription.getName());
		assertEquals("Check class name", "com.akibot.engine2.component.DefaultDNSComponent", clientDescription.getComponentClassName());

	}

	@Test
	public void testClientDescriptionUtils() {
		ClientDescription dns_before = new ClientDescription(null, null, new InetSocketAddress("localhost", 1000));
		ClientDescription dns = new ClientDescription("dns", "Component1", new InetSocketAddress("localhost", 1000));

		List<ClientDescription> clientDescriptionList = new ArrayList<ClientDescription>();
		clientDescriptionList.add(dns_before);

		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(clientA, dns, clientDescriptionList);

		assertEquals("Client count", (Integer) 1, (Integer) clientDescriptionList.size());

		ClientDescription result = clientDescriptionList.get(0);

		assertEquals("Client name", "dns", result.getName());
		assertEquals("Client class name", "Component1", result.getComponentClassName());
	}

}
