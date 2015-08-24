package com.akibot.websocket;

import java.io.IOException;
import java.util.HashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import org.json.JSONObject;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.web.listener.AkiBotWebMaster;

@ApplicationScoped
public class WebSocketSessionHandler {
	static final AkiLogger log = AkiLogger.create(WebSocketSessionHandler.class);

	private final HashSet<Session> sessions = new HashSet<Session>();

	public WebSocketSessionHandler() {
		log.debug("constructor");
		AkiBotWebMaster.setWebSocketSessionHandler(this);
	}

	public void addSession(Session session) {
		log.debug("addSession: " + session);
		sessions.add(session);
	}

	public void removeSession(Session session) {
		log.debug("removeSession: " + session);
		sessions.remove(session);
	}

	public void sendToAllWebSocketSessions(JSONObject message) {
		log.debug("sendToAllConnectedSessions: " + message);
		for (Session session : sessions) {
			sendToWebSocketSession(session, message);
		}
	}

	public void sendToWebSocketSession(Session session, JSONObject message) {
		log.trace("sendToSession: " + session + ": " + message);
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			sessions.remove(session);
			ex.printStackTrace();
		}
	}

}