package com.akibot.engine2.test;

import java.net.SocketException;
import java.net.UnknownHostException;

import com.akibot.engine2.server.AkibotNode;

public class Launcher {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		System.out.print("Strating Server...");
		AkibotNode serverNode = new AkibotNode(2002, null);
		serverNode.start();
		System.out.println("started.");
		
		System.out.print("Starting Client...");
		AkibotNode clientNode = new AkibotNode(null, null);
		clientNode.start();
		System.out.println("started.");
		
		
		
		
	}
}
