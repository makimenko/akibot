package com.akibot.engine2.test;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.server.AkibotNode;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;

public class Launcher {

	public static void main(String[] args) throws SocketException, UnknownHostException, FailedToSendMessageException {
		String serverHost = "dm-PC";
		int serverPort = 2001;
		InetSocketAddress inetSocketAddress = new InetSocketAddress(serverHost, serverPort);

		AkibotNode serverNode = new AkibotNode(new DefaultComponent("akibot.server"), serverPort);
		serverNode.start();

		AkibotNode clientNodeA = new AkibotNode(new TestComponent("akibot.clientA"), inetSocketAddress);
		clientNodeA.start();

		AkibotNode clientNodeB = new AkibotNode(new TestComponent("akibot.clientB"), inetSocketAddress);
		clientNodeB.start();

		TestRequest testRequest = new TestRequest();
		testRequest.setX(1);

	}

}
