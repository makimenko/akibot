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

		AkibotClient serverNode = new AkibotClient(new DefaultComponent("akibot.server"), serverPort);
		serverNode.start();

		AkibotClient clientNodeA = new AkibotClient(new TestComponent("akibot.clientA"), serverAddress);
		clientNodeA.getComponent().getMyClientDescription().getTopicList().add(new TestResponse());
		clientNodeA.start();

		AkibotClient clientNodeB = new AkibotClient(new TestComponent("akibot.clientB"), serverAddress);
		clientNodeB.getComponent().getMyClientDescription().getTopicList().add(new TestRequest());
		clientNodeB.start();

		Thread.sleep(1000);

		TestRequest testRequest = new TestRequest();
		testRequest.setX(1);
		// clientNodeA.getComponent().broadcastMessage(testRequest);

		TestResponse testResponse = (TestResponse) clientNodeA.getComponent().syncRequest(testRequest, 1000);
		System.out.println("result = " + testResponse.getResult());

		testRequest.setX(5);
		testResponse = (TestResponse) clientNodeA.getComponent().syncRequest(testRequest, 1000);
		System.out.println("result = " + testResponse.getResult());

		synchronized (this) {
			this.wait();
		}

	}

}
