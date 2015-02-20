package com.akibot.engine2.test;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import com.akibot.engine2.component.ClientDescription;
import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.server.AkibotNode;
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
		InetSocketAddress inetSocketAddress = new InetSocketAddress(serverHost, serverPort);

		AkibotNode serverNode = new AkibotNode(new DefaultComponent("akibot.server"), serverPort);
		serverNode.start();

		AkibotNode clientNodeA = new AkibotNode(new TestComponent("akibot.clientA"), inetSocketAddress);
		clientNodeA.getComponent().getMyClientDescription().getTopicList().add(new TestResponse());
		clientNodeA.start();

		AkibotNode clientNodeB = new AkibotNode(new TestComponent("akibot.clientB"), inetSocketAddress);
		clientNodeB.getComponent().getMyClientDescription().getTopicList().add(new TestRequest());
		clientNodeB.start();

		Thread.sleep(1000);

		TestRequest testRequest = new TestRequest();
		testRequest.setX(1);
		// clientNodeA.getComponent().broadcastMessage(testRequest);

		TestResponse testResponse = (TestResponse) clientNodeA.getComponent().syncRequest(testRequest, 1000);

		System.out.println("result = " + testResponse.getResult());

		synchronized (this) {
			this.wait();
		}

	}

}
