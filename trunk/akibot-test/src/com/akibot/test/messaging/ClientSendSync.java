package com.akibot.test.messaging;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.akibot.engine.component.Component;
import com.akibot.engine.message.Message;
import com.akibot.engine.message.Request;
import com.akibot.engine.message.Response;
import com.akibot.engine.server.Client;
import com.akibot.engine.server.ClientDescription;
import com.akibot.test.object.TestComponent;
import com.akibot.test.object.TestRequest;
import com.akibot.test.object.TestResponse;

public class ClientSendSync {

	public static void main(String args[]) {
		int port = 2002;
		String host = "raspberrypi";
		try {
			System.out.println("CLIENT: Start");

			Component componentA = new TestComponent();
			ClientDescription componentADescription = new ClientDescription("test.a");
			componentADescription.getTopicList().add(new TestRequest());
			Client componentAClient = new Client(host, port, componentA, componentADescription);
			componentAClient.start();

			Component componentB = new TestComponent();
			ClientDescription componentBDescription = new ClientDescription("test.b");
			componentBDescription.getTopicList().add(new TestResponse());
			Client componentBClient = new Client(host, port, componentB, componentBDescription);
			componentBClient.start();
			
			long timeout = 2000;
			long startTime = System.currentTimeMillis();
			int count = 0;
			while (System.currentTimeMillis() - startTime < timeout) {
				count++;
				TestRequest request = new TestRequest();
				request.setX(1);
				Response response = (Response) componentBClient.syncRequest(request, 1000);

			}
			long duration = System.currentTimeMillis() - startTime;
			
			System.out.println("Performance Stats: count=" + count + ", duration=" + duration + ", avg=" + (duration / count));
			// Performance Stats: count=23, duration=10411, avg=452

		} catch (Exception e) {
			System.out.println("CLIENT: " + e);
		}
		System.out.println("CLIENT: End");

	}
}
