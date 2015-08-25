package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.world.element.AkiNodeTransformation;
import com.akibot.tanktrack.component.world.element.AkiPoint;
import com.akibot.tanktrack.component.world.message.NodeTransformationMessage;

public class AkiBotWebSimulationLauncher {
	static final AkiLogger log = AkiLogger.create(AkiBotWebSimulationLauncher.class);

	public static void main(String[] args) throws Exception {
		String dnsHost = Constants.DNS_WEBTEST_HOST;
		int dnsPort = Constants.DNS_WEBTEST_PORT;

		InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		// Components:
		AkibotClient simulationClient = new AkibotClient("akibot.simulation", new DefaultComponent(), dnsAddress);
		simulationClient.getComponent().getComponentStatus().setReady(true);

		// Start
		simulationClient.start();
		Thread.sleep(500);

		System.out.println("AkiBotWebSimulationLauncher: Started");

		// LOOP forever:

		int minPosX = -100;
		int maxPosX = 100;
		int step = 10;

		int posX = minPosX;
		NodeTransformationMessage nodeTransformationMessage = new NodeTransformationMessage();
		nodeTransformationMessage.setNodeName("robotNode");
		while (true) {
			posX += step;
			if (posX > maxPosX) {
				posX = minPosX;
			}
			AkiNodeTransformation transformation = new AkiNodeTransformation();
			transformation.setPosition(new AkiPoint(posX, 0, 15));
			nodeTransformationMessage.setTransformation(transformation);
			simulationClient.getOutgoingMessageManager().broadcastMessage(nodeTransformationMessage);
			Thread.sleep(1000);
		}
	}

}
