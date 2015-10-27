package com.akibot.tanktrack.test;

import java.net.InetSocketAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.component.test.TestComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.echolocator.EchoLocatorRequest;
import com.akibot.tanktrack.component.echolocator.EchoLocatorResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.orientation.OrientationRequest;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.servo.ServoResponse;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisRequest;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisResponse;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.launcher.Constants;

public class DemoTest {

	private static final String VOICE = "voxforge-ru-nsh";
	private static final String SAY1_HELLO = "Привет! Меня зовут АкиБот";
	private static final String SAY2_CAN = "Мне один год, но я уже много что умею";
	private static final String SAY3_MOVE = "У меня есть гусеничные ноги, и я умею ходить вперёд и назад";
	private static final String SAY4_TURN = "Могу поворачивать в разные стороны";
	private static final String SAY5_ECHOLOCATOR = "У меня даже есть ультросонар как у дельфинов. Только у меня их два!";
	private static final String SAY6_WONDER = "Я уверен, вы удивлены, но это еще не все мои способности";
	private static final String SAY7_GYROSCOPE = "Благодаря гироскопу я могу ориентироватся в пространстве";
	private static final String SAY8_WEST = "Запад. В этой стороне нахоидся запад!";
	private static final String SAY9_BYE1 = "Лучшее домашнее животное – это АкиБот.";
	private static final String SAY10_BYE2 = "Я бы спас Вселенную, но уже в пижаме и собрался спать.";

	/*
	 * private static final String VOICE = "cmu-slt-hsmm"; private static final String SAY1_HELLO = "Hey there! My name is AkiBot!"; private static final String
	 * SAY2_CAN = "I'm only 1 year old, nevertheless already can do basic actions."; private static final String SAY3_MOVE =
	 * "I have a tank track which allows me to go forward and backward."; private static final String SAY4_TURN = "Turn to the left and right."; private static
	 * final String SAY5_ECHOLOCATOR = "I also have an ultra sonar same as dolphins have. But I have an extra one on the back side."; private static final
	 * String SAY6_WONDER = "I hope you wondered, but it's only beginning of my assembling and education!"; private static final String SAY7_GYROSCOPE =
	 * "Ah, forgot to mention. I have a compass which help to navigate."; private static final String SAY8_WEST = "West is there!"; private static final String
	 * SAY9_BYE1 = "The best pet is AkiBot!"; private static final String SAY10_BYE2 =
	 * "Hey, human! are your pants reflective aluminum alloy? because i can see myself in them.";
	 */
	private static AkibotClient testClient;
	private final static String dnsHost = Constants.DNS_HOST;
	private final static int dnsPort = Constants.DNS_PORT;
	private final static InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);
	private static final int MAX_WAIT = 20000;

	private static final StickMotionRequest forwardRequest = new StickMotionRequest(DirectionType.FORWARD);
	private static final StickMotionRequest backRequest = new StickMotionRequest(DirectionType.BACKWARD);
	private static final StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);
	private static final StickMotionRequest leftRequest = new StickMotionRequest(DirectionType.LEFT);
	private static final StickMotionRequest rightRequest = new StickMotionRequest(DirectionType.RIGHT);
	private static final GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
	private static final DistanceRequest distanceRequest = new DistanceRequest();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testClient = new AkibotClient("akibot.client", new TestComponent(), dnsAddress);
		testClient.getMyClientDescription().getTopicList().add(new Response());

		testClient.start();
		Thread.sleep(10000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void step1() throws FailedToSendMessageException, InterruptedException {

		// -----------------------------------------------------------------------------------
		say(SAY1_HELLO);
		Thread.sleep(1000);

		// -----------------------------------------------------------------------------------
		say(SAY2_CAN);
		// Thread.sleep(1000);

		callServo("akibot.servo.front");

		// -----------------------------------------------------------------------------------
		sayAsync(SAY3_MOVE);

		broadcast(forwardRequest);
		Thread.sleep(2000);
		broadcast(backRequest);
		Thread.sleep(2000);
		broadcast(stopRequest);

		// -----------------------------------------------------------------------------------
		sayAsync(SAY4_TURN);

		broadcast(leftRequest);
		Thread.sleep(1000);
		broadcast(rightRequest);
		Thread.sleep(2000);
		broadcast(leftRequest);
		Thread.sleep(1000);
		broadcast(stopRequest);
		Thread.sleep(1000);

		// -----------------------------------------------------------------------------------
		sayAsync(SAY5_ECHOLOCATOR);

		callEchoLocator("akibot.echolocator.front");
		callEchoLocator("akibot.echolocator.back");

		// -----------------------------------------------------------------------------------
		say(SAY6_WONDER);
		Thread.sleep(1000);

		// -----------------------------------------------------------------------------------
		say(SAY7_GYROSCOPE);
		Thread.sleep(1000);
		OrientationRequest messageOrientationRequest = new OrientationRequest();
		messageOrientationRequest.setNorthDegreesXY(270);
		messageOrientationRequest.setPrecissionDegrees(1);
		messageOrientationRequest.setTimeoutMillis(10000);
		sendSync(messageOrientationRequest, 13000);

		say(SAY8_WEST);
		broadcast(forwardRequest);
		Thread.sleep(1000);
		broadcast(backRequest);
		Thread.sleep(1000);
		broadcast(stopRequest);

		// -----------------------------------------------------------------------------------
		sayAsync(SAY9_BYE1);
		sayAsync(SAY10_BYE2);
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

		// Step 2:
		echoLocatorRequest.setServoBaseFrom(24);
		echoLocatorRequest.setServoBaseTo(14);
		echoLocatorRequest.setServoBaseStep(1);
		echoLocatorRequest.setServoHeadNormal(14);
		echoLocatorResponse = (EchoLocatorResponse) testClient.getOutgoingMessageManager().sendSyncRequest(echoLocatorRequest, 4000);
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

	private static void broadcast(Message message) throws FailedToSendMessageException {
		testClient.getOutgoingMessageManager().broadcastMessage(message);
	}

	private static void sendSync(Request request, int wait) throws FailedToSendMessageException {
		Response response = testClient.getOutgoingMessageManager().sendSyncRequest(request, wait);
	}

	private static void sayAsync(String text) throws FailedToSendMessageException {
		SpeechSynthesisRequest speechSynthesisRequest = new SpeechSynthesisRequest(text);
		speechSynthesisRequest.setVoice(VOICE);
		testClient.getOutgoingMessageManager().broadcastMessage(speechSynthesisRequest);
	}

	private static void say(String text) throws FailedToSendMessageException {
		SpeechSynthesisRequest speechSynthesisRequest = new SpeechSynthesisRequest(text);
		speechSynthesisRequest.setVoice(VOICE);
		SpeechSynthesisResponse speechSynthesisResponse = (SpeechSynthesisResponse) testClient.getOutgoingMessageManager().sendSyncRequest(
				speechSynthesisRequest, MAX_WAIT);
	}

}
