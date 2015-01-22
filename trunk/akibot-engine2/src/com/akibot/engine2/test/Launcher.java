package com.akibot.engine2.test;

import java.net.SocketException;
import java.net.UnknownHostException;

import com.akibot.engine2.server.AkibotComponent;
import com.akibot.engine2.test.component.TestComponent;

public class Launcher {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		System.out.print("Strating Server...");
		AkibotComponent serverNode = new AkibotComponent(2002, null);
		serverNode.start();
		System.out.println("started.");

		System.out.print("Starting Client A...");
		AkibotComponent clientNodeA = new TestComponent(null, null);
		clientNodeA.start();
		System.out.println("started.");

		System.out.print("Starting Client B...");
		AkibotComponent clientNodeB = new TestComponent(null, null);
		clientNodeB.start();
		System.out.println("started.");

	}
}
