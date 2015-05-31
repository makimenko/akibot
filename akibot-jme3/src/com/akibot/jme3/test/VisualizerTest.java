package com.akibot.jme3.test;

import java.net.InetSocketAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.jme3.component.message.NodeRegistrationRequest;
import com.akibot.jme3.component.visualizer.utils.AkiGeometry;
import com.akibot.jme3.component.visualizer.utils.AkiNode;
import com.akibot.jme3.component.visualizer.utils.AkiNodeTransformation;
import com.akibot.jme3.component.visualizer.utils.AkiPoint;

public class VisualizerTest {

	private static AkibotClient testClient;
	private final static String serverHost = "raspberrypi";
	private final static int serverPort = 2000;
	private final static InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testClient = new AkibotClient("akibot.client", new TestComponent(), serverAddress);
		testClient.getMyClientDescription().getTopicList().add(new Response());

		testClient.start();
		Thread.sleep(5000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() throws FailedToSendMessageException, InterruptedException {

		AkiNode homeNode = new AkiNode("home");
		AkiGeometry homeGeometry = new AkiGeometry();
		homeGeometry.setDimension(new AkiPoint(5, 5, 1));
		homeGeometry.setMaterialName("ground");
		homeNode.setGeometry(homeGeometry);
		testClient.getOutgoingMessageManager().broadcastMessage(new NodeRegistrationRequest(homeNode));

		AkiNode robotNode = new AkiNode("robot");
		AkiGeometry robotGeometry = new AkiGeometry();
		robotGeometry.setDimension(new AkiPoint(2, 1, 1));
		robotGeometry.setMaterialName("object");
		robotNode.setGeometry(robotGeometry);

		AkiNodeTransformation robotAkiNodeTransformation = new AkiNodeTransformation();
		// robotAkiNodeTransformation.setTranslation(new AkiPoint(10, 0, +10));
		// robotAkiNodeTransformation.setRotation(new AkiPoint(1, 1, 1));
		robotNode.setTransformation(robotAkiNodeTransformation);

		// robotNode.setParentNode(homeNode);s

		testClient.getOutgoingMessageManager().broadcastMessage(new NodeRegistrationRequest(robotNode));

	}

}
