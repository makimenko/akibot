package com.akibot.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.akibot.engine2.logger.AkiLogger;

@ApplicationScoped
@ServerEndpoint("/actions")
public class MySocketServer {
	static final AkiLogger log = AkiLogger.create(MySocketServer.class);

	public MySocketServer() {
		log.debug("MySocketServer constructor");
	}
	
	@Inject
	private MySessionHandler sessionHandler;

	@OnOpen
	public void open(Session session) {
		log.debug("MySocketServer.open: " + session);
		sessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		log.debug("MySocketServer.close: " + session);
		sessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		log.debug("MySocketServer.onError: " + error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		log.debug("MySocketServer.onMessage: " + message);

		// JSONObject jsonObject = JSONObject.fromObject(message);

		// BeanA bean = (BeanA) JSONObject.toBean( jsonObject, BeanA.class );
		// sessionHandler.addDevice(device);

	}
	
	
	
}