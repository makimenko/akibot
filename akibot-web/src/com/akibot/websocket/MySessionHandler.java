package com.akibot.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import org.json.JSONObject;

import com.akibot.engine2.logger.AkiLogger;

@ApplicationScoped
public class MySessionHandler {
	static final AkiLogger log = AkiLogger.create(MySessionHandler.class);

	private final HashSet<Session> sessions = new HashSet<Session>();
	private Timer timer;
	private int i = 0;

	public MySessionHandler() {
		log.debug("MySessionHandler constructor");
	}

	public void addSession(Session session) {
		log.debug("MySessionHandler.addSession: " + session);
		sessions.add(session);
		timer = new Timer();
		timer.schedule(new MyTask(), 1 * 1000, 1 * 5000);
	}

	public void removeSession(Session session) {
		log.debug("MySessionHandler.removeSession: " + session);
		sessions.remove(session);
	}

	private void sendToAllConnectedSessions(JSONObject message) {
		log.debug("MySessionHandler.sendToAllConnectedSessions: " + message);
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	private void sendToSession(Session session, JSONObject message) {
		log.trace("MySessionHandler.sendToSession: " + session + ": " + message);
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			sessions.remove(session);
			ex.printStackTrace();
		}
	}

	public class MyTask extends TimerTask {
		@Override
		public void run() {
			log.trace("MyTask.run");
			i++;
			MyBean myBean = new MyBean();
			myBean.setSomeText("HaHaHa: " + i);

			try {
				JSONObject message = new JSONObject(myBean);
				sendToAllConnectedSessions(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}