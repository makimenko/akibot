package com.akibot.engine2.test;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
		String serverHost = "dm-PC";
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
		System.out.println("======================");

		TestRequest testRequest = new TestRequest();
		testRequest.setX(1);
		clientNodeA.getComponent().broadcastMessage(testRequest);
		System.out.println("size="+clientNodeA.getComponent().getMyClientDescription().getTopicList().size());

		synchronized (this) {
			this.wait();
		}

	}

}
