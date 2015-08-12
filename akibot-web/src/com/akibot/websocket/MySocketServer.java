package com.akibot.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/actions")
public class MySocketServer {

	@Inject
	private MySessionHandler sessionHandler = new MySessionHandler();

	@OnOpen
	public void open(Session session) {
		System.out.println("MySocketServer.open");
		sessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		System.out.println("MySocketServer.close");
		sessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		System.out.println("MySocketServer.onError");
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		System.out.println("MySocketServer.onMessage");

		// JSONObject jsonObject = JSONObject.fromObject(message);

		// BeanA bean = (BeanA) JSONObject.toBean( jsonObject, BeanA.class );
		// sessionHandler.addDevice(device);

	}
}