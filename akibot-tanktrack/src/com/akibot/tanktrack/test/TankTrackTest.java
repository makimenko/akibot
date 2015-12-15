package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;

import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.component.test.TestComponent;
import com.akibot.engine2.component.workflow.WorkflowDefinition;
import com.akibot.engine2.component.workflow.WorkflowElement;
import com.akibot.engine2.component.workflow.WorkflowForkElement;
import com.akibot.engine2.component.workflow.WorkflowJoinElement;
import com.akibot.engine2.component.workflow.WorkflowRequest;
import com.akibot.engine2.component.workflow.WorkflowRequestElement;
import com.akibot.engine2.component.workflow.WorkflowResponse;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.WorkflowException;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.audio.AudioRequest;
import com.akibot.tanktrack.component.audio.AudioResponse;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.echolocator.EchoLocatorRequest;
import com.akibot.tanktrack.component.echolocator.EchoLocatorResponse;
import com.akibot.tanktrack.component.echolocator.MultipleDistanceDetails;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.orientation.OrientationRequest;
import com.akibot.tanktrack.component.orientation.OrientationResponse;
import com.akibot.tanktrack.component.orientation.RoundRobinUtils;
import com.akibot.tanktrack.component.scout.ScoutDistanceAroundResponse;
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
import com.akibot.tanktrack.component.world.element.NodeTransformation;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.VectorUtils;
import com.akibot.tanktrack.component.world.message.WorldMultipleDistanceUpdateRequest;
import com.akibot.tanktrack.component.world.message.WorldNodeTransformationRequest;
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
		assertEquals("Check value", true, gyroscopeResponse.getNorthDegreesXY() > 0 && gyroscopeResponse.getNorthDegreesXY() < 360);
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
		double diffAfterMoveToTheRight = roundRobinUtils.rightDistance(startPosition.getNorthDegreesXY(), middlePosition.getNorthDegreesXY());
		assertEquals("Check value diffAfterMoveToTheRight " + diffAfterMoveToTheRight, true, diffAfterMoveToTheRight > 2);

		testClient.getOutgoingMessageManager().broadcastMessage(leftRequest);
		Thread.sleep(2000);
		testClient.getOutgoingMessageManager().broadcastMessage(stopRequest);
		GyroscopeResponse endPosition = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 2000);
		double diffAfterMoveToTheLeft = roundRobinUtils.leftDistance(middlePosition.getNorthDegreesXY(), endPosition.getNorthDegreesXY());
		assertEquals("Check value diffAfterMoveToTheLeft " + diffAfterMoveToTheLeft, true, diffAfterMoveToTheLeft > 2);

	}

	@Test
	public void testDistance() throws FailedToSendMessageException, InterruptedException {
		DistanceRequest distanceRequest = new DistanceRequest();
		DistanceResponse distanceResponse = (DistanceResponse) testClient.getOutgoingMessageManager().sendSyncRequest(distanceRequest, 2000);
		
		double result = distanceResponse.getDistanceDetails().getDistanceMm();
		System.out.println("result="+result);
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
		callEchoLocator(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT);
		callEchoLocator(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK);
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

		assertEquals("Validate 1 Echo Locator Request" + echoLocatorResponse.getMultipleDistanceDetails().getDistanceDetailsList().size(), 21,
				echoLocatorResponse.getMultipleDistanceDetails().getDistanceDetailsList().size());

		// Step 2:
		echoLocatorRequest.setServoBaseFrom(24);
		echoLocatorRequest.setServoBaseTo(14);
		echoLocatorRequest.setServoBaseStep(1);
		echoLocatorRequest.setServoHeadNormal(14);
		echoLocatorResponse = (EchoLocatorResponse) testClient.getOutgoingMessageManager().sendSyncRequest(echoLocatorRequest, 4000);
		assertEquals("Validate 2 Echo Locator Request" + echoLocatorResponse.getMultipleDistanceDetails().getDistanceDetailsList().size(), 11,
				echoLocatorResponse.getMultipleDistanceDetails().getDistanceDetailsList().size());
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
			double value = gyroscopeValueResponse.getNorthDegreesXY();

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
		orientationRequest.setNorthDegreesXY(90);
		orientationRequest.setPrecissionDegrees(5);
		orientationRequest.setTimeoutMillis(10000);
		OrientationResponse orientationResponse = (OrientationResponse) testClient.getOutgoingMessageManager().sendSyncRequest(orientationRequest, 13000);
		System.out.println(orientationResponse);
		assertEquals("Orientation status", true, orientationResponse.isSuccess());

		GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
		GyroscopeResponse gyroscopeValueResponse = (GyroscopeResponse) testClient.getOutgoingMessageManager().sendSyncRequest(gyroscopeValueRequest, 1000);
		assertEquals("Orientation value", true, roundRobinUtils.modularDistance(gyroscopeValueResponse.getNorthDegreesXY(), 90) < 20);
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

	// EXCLUDED:
	public void SKIP_testParallelEchoLocator() throws FailedToSendMessageException, InterruptedException, WorkflowException, CloneNotSupportedException {
		int timeout = 10000;
		String correlationFront = "A";
		String correlationBack = "B";
		WorkflowRequest worflowRequest = new WorkflowRequest();
		WorkflowDefinition workflowDefinition = new WorkflowDefinition(timeout);

		EchoLocatorRequest frontEchoLocatorRequest = new EchoLocatorRequest();
		frontEchoLocatorRequest.setTo(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT);
		frontEchoLocatorRequest.setServoBaseFrom(4);
		frontEchoLocatorRequest.setServoBaseTo(24);
		frontEchoLocatorRequest.setServoBaseStep(1);
		frontEchoLocatorRequest.setServoHeadNormal(14);

		EchoLocatorRequest backEchoLocatorRequest = (EchoLocatorRequest) frontEchoLocatorRequest.clone();
		backEchoLocatorRequest.setTo(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK);

		WorkflowElement fork = new WorkflowForkElement();
		WorkflowElement request1 = new WorkflowRequestElement(correlationFront, frontEchoLocatorRequest);
		WorkflowElement request2 = new WorkflowRequestElement(correlationBack, backEchoLocatorRequest);

		WorkflowElement join = new WorkflowJoinElement();

		fork.setNextWorkflowElement(request1);
		// fork.setNextWorkflowElement(request2);

		request1.setNextWorkflowElement(join);
		// request2.setNextWorkflowElement(join);

		workflowDefinition.setStartWorkflowElement(fork);
		worflowRequest.setWorflowDefinition(workflowDefinition);

		WorkflowResponse workflowResponse = (WorkflowResponse) testClient.getOutgoingMessageManager().sendSyncRequest(worflowRequest, timeout);

		assertEquals("Number of Responses", 2, workflowResponse.getResponseList().size());

		// UPDATE WORLD:
		EchoLocatorResponse frontEchoLocatorResponse = (EchoLocatorResponse) workflowResponse.getResponseList().get(correlationFront);
		EchoLocatorResponse backEchoLocatorResponse = (EchoLocatorResponse) workflowResponse.getResponseList().get(correlationBack);

		assertEquals("Validate Front", 21, frontEchoLocatorResponse.getMultipleDistanceDetails().getDistanceDetailsList().size());
		assertEquals("Validate Back", 21, backEchoLocatorResponse.getMultipleDistanceDetails().getDistanceDetailsList().size());

	}

}
