package com.akibot.engine2.test.component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.akibot.engine2.message.Message;
import com.akibot.engine2.server.AkibotNode;

public class TestComponent extends AkibotNode {

	public TestComponent(Integer port, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		super(port, parentSocketAddress);
	}

	@Override
	protected void onMessageReceived(Message message) {
		super.onMessageReceived(message);

		if (message instanceof TestRequest) {
			TestRequest request = (TestRequest) message;
			TestResponse response = new TestResponse();
			response.setResult(request.getX() + 1);
			try {
				send(response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
