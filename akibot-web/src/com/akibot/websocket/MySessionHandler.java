package com.akibot.websocket;

import java.io.IOException;
import java.util.HashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import org.json.JSONObject;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.web.listener.AkiBotWebMaster;

@ApplicationScoped
public class MySessionHandler {
	static final AkiLogger log = AkiLogger.create(MySessionHandler.class);

	private final HashSet<Session> sessions = new HashSet<Session>();

	public MySessionHandler() {
		log.debug("MySessionHandler constructor");
		AkiBotWebMaster.setMySessionHandler(this);
	}

	public void addSession(Session session) {
		log.debug("MySessionHandler.addSession: " + session);
		sessions.add(session);
	}

	public void removeSession(Session session) {
		log.debug("MySessionHandler.removeSession: " + session);
		sessions.remove(session);
	}

	public void sendToAllConnectedSessions(JSONObject message) {
		log.debug("MySessionHandler.sendToAllConnectedSessions: " + message);
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	public void sendToSession(Session session, JSONObject message) {
		log.trace("MySessionHandler.sendToSession: " + session + ": " + message);
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			sessions.remove(session);
			ex.printStackTrace();
		}
	}

}