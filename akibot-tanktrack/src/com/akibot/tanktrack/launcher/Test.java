package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.awtcontroller.AwtControllerComponent;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;

public class Test {
	public static void main(String[] args) throws SocketException, UnknownHostException, FailedToSendMessageException, InterruptedException {

		String serverHost = "raspberrypi";
		int serverPort = 2000;

		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		AkibotClient awtController = new AkibotClient("akibot.awtcontroller", new AwtControllerComponent(), serverAddress);
		awtController.start();

		Thread.sleep(100);

		InetSocketAddress address = new InetSocketAddress("raspberrypi", 36143);
		StickMotionRequest request = new StickMotionRequest(DirectionType.STOP);

		awtController.getOutgoingMessageManager().send(address, request);

		Thread.sleep(100);

		Thread.sleep(10000);
		System.exit(0);

	}
}
