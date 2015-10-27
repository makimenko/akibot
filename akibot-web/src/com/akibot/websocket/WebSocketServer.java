package com.akibot.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.tanktrack.component.world.message.WorldContentRequest;
import com.akibot.web.listener.AkiBotWebMaster;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {
	static final AkiLogger log = AkiLogger.create(WebSocketServer.class);

	@Inject
	private static WebSocketSessionHandler webSocketSessionHandler = new WebSocketSessionHandler();

	public WebSocketServer() {
		log.debug("constructor");
	}

	@OnOpen
	public void open(Session session) {
		log.debug("open: " + session);
		webSocketSessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		log.debug("close: " + session);
		webSocketSessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		log.debug("onError: " + error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		log.debug("handleMessage: " + message);
		// TODO: JSON to Bean? How?
		// Not working: BeanA bean = (BeanA) JSONObject.toBean( jsonObject, BeanA.class );

		String messageClass = getMessageClass(message);
		if (messageClass.equals("WorldContentRequest")) {
			try {
				WorldContentRequest worldContentRequest = new WorldContentRequest();
				AkiBotWebMaster.getAkibotWebComponent().broadcastMessage(worldContentRequest);
				// asynchronous response
			} catch (FailedToSendMessageException e) {
				log.catching(e);
			}
		}
	}

	private String getMessageClass(String jsonString) {
		JSONObject jsonMessage = new JSONObject(jsonString);
		String messageClass = null;
		try {
			messageClass = (String) jsonMessage.get("messageClass");
		} catch (JSONException e) {
			log.warn("Can't find messageClass attribute");
		}
		return messageClass;
	}

}