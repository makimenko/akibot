package com.akibot.engine2.test;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.akibot.engine2.component.ClientDescription;
import com.akibot.engine2.component.ClientDescriptionResponse;
import com.akibot.engine2.component.ClientDescriptionUtils;
import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.server.AkibotNode;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;

public class Launcher {

	public static void main(String[] args) throws SocketException, UnknownHostException, FailedToSendMessageException {
		String serverHost = "localhost";
		int serverPort = 2001;
		InetSocketAddress inetSocketAddress = new InetSocketAddress(serverHost, serverPort);

		System.out.print("Strating Server...");
		AkibotNode serverNode = new AkibotNode(new DefaultComponent("akibot.server"), serverPort);
		serverNode.start();
		System.out.println("started.");

		System.out.print("Starting Client A...");
		AkibotNode clientNodeA = new AkibotNode(new TestComponent("akibot.clientA"), inetSocketAddress);
		clientNodeA.start();
		System.out.println("started.");

		System.out.print("Starting Client B...");
		AkibotNode clientNodeB = new AkibotNode(new TestComponent("akibot.clientB"), inetSocketAddress);
		clientNodeB.start();
		System.out.println("started.");

		TestRequest testRequest = new TestRequest();
		testRequest.setX(1);

		//clientNodeA.(testRequest);

	}

	public void testMerge() {
		ClientDescriptionResponse a = new ClientDescriptionResponse();
		List aList = new ArrayList<ClientDescription>();
		aList.add(new ClientDescription("aList1", new InetSocketAddress(10)));
		aList.add(new ClientDescription("aList2", new InetSocketAddress(11)));
		a.setClientDescriptionList(aList);

		ClientDescriptionResponse b = new ClientDescriptionResponse();
		List bList = new ArrayList<ClientDescription>();
		bList.add(new ClientDescription("bList1", new InetSocketAddress(12)));
		bList.add(new ClientDescription("bList1", new InetSocketAddress(13)));
		b.setClientDescriptionList(bList);

		ClientDescriptionUtils utils = new ClientDescriptionUtils();

		aList = utils.merge(aList, bList);

		System.out.println("============a:");
		System.out.println(a);
		System.out.println("============b:");
		System.out.println(b);

	}
}
