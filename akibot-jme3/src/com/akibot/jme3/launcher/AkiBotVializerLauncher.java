package com.akibot.jme3.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.network.AkibotClient;
import com.akibot.jme3.component.message.VisualizerRequest;
import com.akibot.jme3.component.visualizer.VisualizerComponent;
import com.akibot.jme3.component.visualizer.VisualizerWindow;
import com.akibot.tanktrack.launcher.Constants;
import com.jme3.system.AppSettings;

public class AkiBotVializerLauncher {

	public static void main(String[] args) throws Exception {
		String dnsHost = Constants.DNS_HOST;
		int dnsPort = Constants.DNS_PORT;
		InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		// JME3 Visualizer Window
		VisualizerWindow visualizerWindow = new VisualizerWindow();
		AppSettings set = new AppSettings(true);
		set.setWidth(1600);
		set.setHeight(900);
		visualizerWindow.setSettings(set);
		visualizerWindow.setShowSettings(false);

		// Visualizer Component:
		AkibotClient visualizer = new AkibotClient("akibot.visualizer", new VisualizerComponent(visualizerWindow), dnsAddress);
		visualizer.getMyClientDescription().getTopicList().add(new VisualizerRequest());

		// Start All
		visualizer.start();
		visualizerWindow.start(); // last!

	}

}
