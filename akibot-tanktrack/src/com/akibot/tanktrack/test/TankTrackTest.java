package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.echolocator.EchoLocatorRequest;
import com.akibot.tanktrack.component.echolocator.EchoLocatorResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.servo.ServoResponse;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;

public class TankTrackTest {
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
	public void testMovement() throws FailedToSendMessageException, InterruptedException {
		StickMotionRequest forwardRequest = new StickMotionRequest(DirectionType.FORWARD);
		StickMotionRequest backRequest = new StickMotionRequest(DirectionType.BACKWARD);
		StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);

		testClient.getOutgoingMessageManager().broadcastMessage(forwardRequest);
		Thread.sleep(500);
		testClient.getOutgoingMessageManager().broadcastMessage(backRequest);
		Thread.sleep(500);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
	}

	@Test
	public void testGyroscope() throws FailedToSendMessageException, InterruptedException {
		GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
		GyroscopeResponse gyroscopeResponse = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 1000);
		assertEquals("Check value", true, gyroscopeResponse.getNorthDegrreesXY() > 0 && gyroscopeResponse.getNorthDegrreesXY() < 360);
	}

	@Test
	public void testMovementAndGyroscope() throws FailedToSendMessageException, InterruptedException {
		StickMotionRequest leftRequest = new StickMotionRequest(DirectionType.LEFT);
		StickMotionRequest rightRequest = new StickMotionRequest(DirectionType.RIGHT);
		StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);
		GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();

		GyroscopeResponse startPosition = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 2000);

		testClient.getOutgoingMessageManager().broadcastMessage(rightRequest);
		Thread.sleep(1000);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		GyroscopeResponse middlePosition = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 2000);
		double diff = startPosition.getNorthDegrreesXY() - middlePosition.getNorthDegrreesXY();
		assertEquals("Check value " + diff, true, diff > 2);

		testClient.getOutgoingMessageManager().broadcastMessage(leftRequest);
		Thread.sleep(1000);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		GyroscopeResponse endPosition = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 2000);
		double diff2 = middlePosition.getNorthDegrreesXY() - endPosition.getNorthDegrreesXY();
		assertEquals("Check value " + diff2, true, diff2 < 2);

	}

	@Test
	public void testDistance() throws FailedToSendMessageException, InterruptedException {
		DistanceRequest distanceRequest = new DistanceRequest();
		DistanceResponse distanceResponse = (DistanceResponse) testClient.getOutgoingMessageManager().sendSyncRequest(distanceRequest, 2000);

		double result = distanceResponse.getMm();
		assertEquals("Check value " + result, true, result > 100);
	}

	@Test
	public void testServo() throws FailedToSendMessageException, InterruptedException {
		ServoResponse servoResponse;

		ServoRequest servoBaseRequest = new ServoRequest();
		servoBaseRequest.setMicroseconds(500000);
		servoBaseRequest.setTo("akibot.servo.base");

		ServoRequest servoHeadRequest = new ServoRequest();
		servoHeadRequest.setMicroseconds(500000);
		servoHeadRequest.setTo("akibot.servo.head");

		servoBaseRequest.setValue(4);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoBaseRequest, 1000);

		servoBaseRequest.setValue(14);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoBaseRequest, 1000);

		servoBaseRequest.setValue(24);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoBaseRequest, 1000);

		servoBaseRequest.setValue(14);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoBaseRequest, 1000);

		servoHeadRequest.setValue(24);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoHeadRequest, 1000);

		servoHeadRequest.setValue(14);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoHeadRequest, 1000);
	}

	@Test
	public void testEchoLocator() throws FailedToSendMessageException, InterruptedException {
		EchoLocatorRequest echoLocatorRequest = new EchoLocatorRequest();
		EchoLocatorResponse echoLocatorResponse;

		// Step 1:
		echoLocatorRequest.setServoBaseFrom(4);
		echoLocatorRequest.setServoBaseTo(24);
		echoLocatorRequest.setServoBaseStep(1);
		echoLocatorRequest.setServoHeadNormal(14);
		echoLocatorResponse = (EchoLocatorResponse) testClient.getOutgoingMessageManager().sendSyncRequest(echoLocatorRequest, 4000);
		assertEquals("Validate 1 Echo Locator Request" + echoLocatorResponse.getEchoLocatorResult().length, 21,
				echoLocatorResponse.getEchoLocatorResult().length);

		// Step 2:
		echoLocatorRequest.setServoBaseFrom(24);
		echoLocatorRequest.setServoBaseTo(14);
		echoLocatorRequest.setServoBaseStep(1);
		echoLocatorRequest.setServoHeadNormal(14);
		echoLocatorResponse = (EchoLocatorResponse) testClient.getOutgoingMessageManager().sendSyncRequest(echoLocatorRequest, 4000);
		assertEquals("Validate 2 Echo Locator Request" + echoLocatorResponse.getEchoLocatorResult().length, 11,
				echoLocatorResponse.getEchoLocatorResult().length);

	}

	@Test
	public void testMultipleActions() throws FailedToSendMessageException, InterruptedException {
		EchoLocatorRequest echoLocatorRequest = new EchoLocatorRequest();
		EchoLocatorResponse echoLocatorResponse;

		StickMotionRequest forwardRequest = new StickMotionRequest(DirectionType.FORWARD);
		StickMotionRequest backRequest = new StickMotionRequest(DirectionType.BACKWARD);
		StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);

		// Step 2:
		echoLocatorRequest.setServoBaseFrom(24);
		echoLocatorRequest.setServoBaseTo(14);
		echoLocatorRequest.setServoBaseStep(1);
		echoLocatorRequest.setServoHeadNormal(14);
		testClient.getOutgoingMessageManager().broadcastMessage(forwardRequest);
		testClient.getOutgoingMessageManager().broadcastMessage(echoLocatorRequest);
		Thread.sleep(1000);
		testClient.getOutgoingMessageManager().broadcastMessage(backRequest);
		Thread.sleep(1000);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		Thread.sleep(1000);

	}

}
