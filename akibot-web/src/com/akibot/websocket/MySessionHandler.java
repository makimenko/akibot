package com.akibot.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import org.json.JSONObject;

@ApplicationScoped
public class MySessionHandler {
	private int deviceId = 0;
	private final HashSet<Session> sessions = new HashSet<Session>();
	private final Set devices = new HashSet<>();
	private Timer timer;

	public void addSession(Session session) {
		System.out.println("MySessionHandler.addSession");
		sessions.add(session);
		/*
		 * for (Device device : devices) { JsonObject addMessage = createAddMessage(device); sendToSession(session, addMessage); }
		 */

		timer = new Timer();
		timer.schedule(new MyTask(), 1 * 1000, 1 * 5000);

	}

	public void removeSession(Session session) {
		System.out.println("MySessionHandler.removeSession");
		sessions.remove(session);
	}

	private void sendToAllConnectedSessions(JSONObject message) {
		System.out.println("MySessionHandler.sendToAllConnectedSessions");
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	private void sendToSession(Session session, JSONObject message) {
		System.out.println("MySessionHandler.sendToSession");
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			sessions.remove(session);
			ex.printStackTrace();
		}
	}

	public class MyTask extends TimerTask {
		public MyTask() {
		}

		public void run() {
			System.out.println("MyTask.run");

			MyBean myBean = new MyBean();
			myBean.setSomeText("HaHaHa");

			try {
				JSONObject message = new JSONObject(myBean);

				sendToAllConnectedSessions(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}