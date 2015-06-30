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
import com.akibot.tanktrack.component.audio.AudioRequest;
import com.akibot.tanktrack.component.audio.AudioResponse;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.echolocator.EchoLocatorRequest;
import com.akibot.tanktrack.component.echolocator.EchoLocatorResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.orientation.OrientationRequest;
import com.akibot.tanktrack.component.orientation.OrientationResponse;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.servo.ServoResponse;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisRequest;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisResponse;
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
		assertEquals("Check value " + result, true, result > 100 && result < 7000);
	}

	@Test
	public void testServo() throws FailedToSendMessageException, InterruptedException {
		callServo("akibot.servo.front");
		callServo("akibot.servo.back");
	}

	private void callServo(String to) throws FailedToSendMessageException {
		ServoResponse servoResponse;

		ServoRequest servoFrontBaseRequest = new ServoRequest();
		servoFrontBaseRequest.setMicroseconds(500000);
		servoFrontBaseRequest.setTo(to + ".base");

		ServoRequest servoFrontHeadRequest = new ServoRequest();
		servoFrontHeadRequest.setMicroseconds(500000);
		servoFrontHeadRequest.setTo(to + ".head");

		servoFrontBaseRequest.setValue(4);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoFrontBaseRequest, 1000);

		servoFrontBaseRequest.setValue(14);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoFrontBaseRequest, 1000);

		servoFrontBaseRequest.setValue(24);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoFrontBaseRequest, 1000);

		servoFrontBaseRequest.setValue(14);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoFrontBaseRequest, 1000);

		servoFrontHeadRequest.setValue(24);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoFrontHeadRequest, 1000);

		servoFrontHeadRequest.setValue(14);
		servoResponse = (ServoResponse) testClient.getOutgoingMessageManager().sendSyncRequest(servoFrontHeadRequest, 1000);
	}

	@Test
	public void testEchoLocator() throws FailedToSendMessageException, InterruptedException {
		callEchoLocator("akibot.echolocator.front");
		callEchoLocator("akibot.echolocator.back");
	}

	private void callEchoLocator(String to) throws FailedToSendMessageException {
		EchoLocatorRequest echoLocatorRequest = new EchoLocatorRequest();
		echoLocatorRequest.setTo(to);
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
		Thread.sleep(1500);
		testClient.getOutgoingMessageManager().broadcastMessage(backRequest);
		Thread.sleep(1500);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		Thread.sleep(1500);

	}

	@Test
	public void testSpeech() throws FailedToSendMessageException, InterruptedException {
		SpeechSynthesisRequest speechSynthesisRequest = new SpeechSynthesisRequest();
		SpeechSynthesisResponse speechSynthesisResponse;
		speechSynthesisRequest.setVoice("voxforge-ru-nsh");

		long startTime = System.currentTimeMillis();
		speechSynthesisRequest.setSpeechText("Привет! Меня зовут АкиБот. Давай дружить?");
		speechSynthesisResponse = (SpeechSynthesisResponse) testClient.getOutgoingMessageManager().sendSyncRequest(speechSynthesisRequest, 10000);
		long duration = System.currentTimeMillis() - startTime;
		assertEquals("Duration of speech", true, duration > 3800);

	}

	@Test
	public void testAudio() throws FailedToSendMessageException, InterruptedException {
		AudioRequest audioRequest = new AudioRequest();
		AudioResponse audioResponse;

		long startTime = System.currentTimeMillis();
		audioRequest.setAudioUrl("file:///usr/share/scratch/Media/Sounds/Effects/Bubbles.wav");
		audioResponse = (AudioResponse) testClient.getOutgoingMessageManager().sendSyncRequest(audioRequest, 10000);
		long duration = System.currentTimeMillis() - startTime;
		assertEquals("Duration of audio", true, duration > 4000);

	}

	@Test
	public void testGyroscopeAround() throws FailedToSendMessageException, InterruptedException {
		StickMotionRequest leftRequest = new StickMotionRequest(DirectionType.LEFT);
		StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);
		GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();

		testClient.getOutgoingMessageManager().broadcastMessage(leftRequest);

		int sample = 0;
		double minValue = 0;
		double maxValue = 0;

		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime <= 15000) {
			GyroscopeResponse gyroscopeValueResponse = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 1000);
			double value = gyroscopeValueResponse.getNorthDegrreesXY();
			sample++;
			if (sample == 1) {
				minValue = value;
				maxValue = value;
			}
			if (value < minValue) {
				minValue = value;
			}
			if (value > maxValue) {
				maxValue = value;
			}
		}

		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		System.out.println("RESULT: Compass range [" + minValue + ", max=" + maxValue + "]");
		assertEquals("MIN compass value must be close to 0 (" + minValue + ")", true, minValue < 20);
		assertEquals("MAX compass value must be close to 360 (" + maxValue + ")", true, maxValue > 350);

	}

	@Test
	public void testOrientation() throws FailedToSendMessageException, InterruptedException {
		OrientationRequest orientationRequest = new OrientationRequest();
		orientationRequest.setNorthDegrreesXY(90);
		orientationRequest.setPrecissionDegrees(1);
		orientationRequest.setTimeoutMillis(10000);
		OrientationResponse orientationResponse = (OrientationResponse) testClient.getOutgoingMessageManager().sendSyncRequest(orientationRequest, 13000);
		System.out.println(orientationResponse);
		assertEquals("Orientation status", true, orientationResponse.isSuccess());
		assertEquals("Orientation value", true, orientationResponse.getNorthDegrreesXY() >= 80 && orientationResponse.getNorthDegrreesXY() <= 100);

	}

}
