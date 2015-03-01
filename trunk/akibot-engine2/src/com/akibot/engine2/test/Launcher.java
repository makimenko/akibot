package com.akibot.engine2.test;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.engine2.test.component.TestResponse;

public class Launcher {

	public static void main(String[] args) throws SocketException, UnknownHostException, FailedToSendMessageException, InterruptedException {

		Launcher launcher = new Launcher();
		launcher.start();
	}

	public void start() throws SocketException, UnknownHostException, FailedToSendMessageException, InterruptedException {
		String serverHost = "localhost";
		int serverPort = 2001;
		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		AkibotClient serverNode = new AkibotClient("akibot.server", new DefaultComponent(), serverPort);
		serverNode.start();

		AkibotClient clientNodeA = new AkibotClient("akibot.clientA", new TestComponent(), serverAddress);
		clientNodeA.getMyClientDescription().getTopicList().add(new TestResponse());
		clientNodeA.start();

		AkibotClient clientNodeB = new AkibotClient("akibot.clientB", new TestComponent(), serverAddress);
		clientNodeB.getMyClientDescription().getTopicList().add(new TestRequest());
		clientNodeB.start();

		Thread.sleep(1000);

		TestRequest testRequest = new TestRequest();
		testRequest.setX(1);
		// clientNodeA.getComponent().broadcastMessage(testRequest);

		TestResponse testResponse = (TestResponse) clientNodeA.getOutgoingMessageManager().sendSyncRequest(testRequest, 1000);
		System.out.println("result = " + testResponse.getResult());

		testRequest.setX(5);
		testResponse = (TestResponse) clientNodeA.getOutgoingMessageManager().sendSyncRequest(testRequest, 1000);
		System.out.println("result = " + testResponse.getResult());

		System.out.println("clients = " + clientNodeB.printClients());
		synchronized (this) {
			this.wait();
		}

	}

}
