package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;

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
import com.akibot.tanktrack.component.orientation.RoundRobinUtils;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.servo.ServoResponse;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisRequest;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisResponse;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.MotionDistanceCounterRequest;
import com.akibot.tanktrack.component.tanktrack.MotionDistanceCounterResponse;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TimedMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TimedMotionResponse;
import com.akibot.tanktrack.launcher.Constants;

public class TankTrackTest {
	private static AkibotClient testClient;
	private final static String dnsHost = Constants.DNS_HOST;
	private final static int dnsPort = Constants.DNS_PORT;
	private final static InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);
	private RoundRobinUtils roundRobinUtils = new RoundRobinUtils(360);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testClient = new AkibotClient("akibot.client", new TestComponent(), dnsAddress);
		testClient.getMyClientDescription().getTopicList().add(new Response());
		testClient.start();
		Thread.sleep(5000);
	}

	@Test
	public void testStickMovement() throws FailedToSendMessageException, InterruptedException {
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
		Thread.sleep(2000);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		GyroscopeResponse middlePosition = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 2000);
		double diffAfterMoveToTheRight = roundRobinUtils.rightDistance(startPosition.getNorthDegrreesXY(), middlePosition.getNorthDegrreesXY());
		assertEquals("Check value diffAfterMoveToTheRight " + diffAfterMoveToTheRight, true, diffAfterMoveToTheRight > 2);

		testClient.getOutgoingMessageManager().broadcastMessage(leftRequest);
		Thread.sleep(2000);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		GyroscopeResponse endPosition = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 2000);
		double diffAfterMoveToTheLeft = roundRobinUtils.leftDistance(middlePosition.getNorthDegrreesXY(), endPosition.getNorthDegrreesXY());
		assertEquals("Check value diffAfterMoveToTheLeft " + diffAfterMoveToTheLeft, true, diffAfterMoveToTheLeft > 2);

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
		long startTime = System.currentTimeMillis();
		audioRequest.setAudioUrl("file:///usr/share/scratch/Media/Sounds/Effects/Bubbles.wav");
		AudioResponse audioResponse = (AudioResponse) testClient.getOutgoingMessageManager().sendSyncRequest(audioRequest, 10000);
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
		double expectedMin = 20;
		double expectedMax = 350;
		boolean between = false;
		double previousValue = -1;
		double maxDiff = 0;

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
			if (!between && roundRobinUtils.modularDistance(value, 180) < 20) {
				between = true;
			}
			if (minValue < expectedMin && maxValue > expectedMax && between) {
				break;
			}
			if (previousValue != -1) {
				double diff = roundRobinUtils.modularDistance(value, previousValue);
				// System.out.println("value=" + value + ", diff=" + diff);
				if (diff > maxDiff) {
					maxDiff = diff;
				}
			}
			previousValue = value;
			Thread.sleep(10);
		}
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		System.out.println("RESULT: Compass range [" + minValue + ", max=" + maxValue + "], maxDiffs=" + maxDiff);
		assertEquals("MIN compass value must be close to 0 (" + minValue + ")", true, minValue < expectedMin);
		assertEquals("MAX compass value must be close to 360 (" + maxValue + ")", true, maxValue > expectedMax);
		assertEquals("BETWEEN compass", true, between);
		assertEquals("Step difference low (" + maxDiff + ")", true, maxDiff > 5);
		assertEquals("Step difference high (" + maxDiff + ")", true, maxDiff < 50);
	}

	@Test
	public void testOrientation() throws FailedToSendMessageException, InterruptedException {
		OrientationRequest orientationRequest = new OrientationRequest();
		orientationRequest.setNorthDegrreesXY(90);
		orientationRequest.setPrecissionDegrees(5);
		orientationRequest.setTimeoutMillis(10000);
		OrientationResponse orientationResponse = (OrientationResponse) testClient.getOutgoingMessageManager().sendSyncRequest(orientationRequest, 13000);
		System.out.println(orientationResponse);
		assertEquals("Orientation status", true, orientationResponse.isSuccess());

		GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
		GyroscopeResponse gyroscopeValueResponse = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 1000);
		assertEquals("Orientation value", true, roundRobinUtils.modularDistance(gyroscopeValueResponse.getNorthDegrreesXY(), 90) < 20);
	}

	@Test
	public void testMovementDistanceCounter() throws FailedToSendMessageException, InterruptedException {
		MotionDistanceCounterRequest motionDistanceCounterRequest = new MotionDistanceCounterRequest();
		StickMotionRequest forwardRequest = new StickMotionRequest(DirectionType.FORWARD);
		StickMotionRequest backRequest = new StickMotionRequest(DirectionType.BACKWARD);
		StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);

		MotionDistanceCounterResponse motionDistanceCounterResponseBefore = (MotionDistanceCounterResponse) testClient.getOutgoingMessageManager()
				.sendSyncRequest(motionDistanceCounterRequest, 500);

		testClient.getOutgoingMessageManager().broadcastMessage(forwardRequest);
		Thread.sleep(500);
		testClient.getOutgoingMessageManager().broadcastMessage(backRequest);
		Thread.sleep(500);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		Thread.sleep(100);
		MotionDistanceCounterResponse motionDistanceCounterResponseAfter = (MotionDistanceCounterResponse) testClient.getOutgoingMessageManager()
				.sendSyncRequest(motionDistanceCounterRequest, 500);

		System.out.println("motionDistanceCounterResponseBefore=" + motionDistanceCounterResponseBefore.getDistanceCounter().getRightDistanceCounter());
		System.out.println("motionDistanceCounterResponseAfter=" + motionDistanceCounterResponseAfter.getDistanceCounter().getRightDistanceCounter());
		assertEquals("Compare Right Distance Counter", true, motionDistanceCounterResponseAfter.getDistanceCounter().getRightDistanceCounter()
				- motionDistanceCounterResponseBefore.getDistanceCounter().getRightDistanceCounter() > 500);

		// TODO: Skip due to sensor issue: assertEquals("Compare Left Distance Counter", true,
		// motionDistanceCounterResponseAfter.getDistanceCounter().getLeftDistanceCounter() -
		// motionDistanceCounterResponseBefore.getDistanceCounter().getLeftDistanceCounter() > 100);

	}

	@Test
	public void testTimedMovement() throws FailedToSendMessageException, InterruptedException {
		TimedMotionRequest timedMotionRequest = new TimedMotionRequest();
		timedMotionRequest.setDirectionType(DirectionType.FORWARD);
		timedMotionRequest.setMilliseconds(500);
		TimedMotionResponse timedMotionResponse;

		timedMotionResponse = (TimedMotionResponse) testClient.getOutgoingMessageManager().sendSyncRequest(timedMotionRequest, 1000);
		assertEquals("Check distance 1", true, timedMotionResponse.getDistanceCounter().getRightDistanceCounter() > 100);

		timedMotionRequest.setDirectionType(DirectionType.BACKWARD);
		timedMotionResponse = (TimedMotionResponse) testClient.getOutgoingMessageManager().sendSyncRequest(timedMotionRequest, 1000);
		assertEquals("Check distance 2", true, timedMotionResponse.getDistanceCounter().getRightDistanceCounter() > 100);
	}
}
