package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
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

public class DemoTest {
	private static AkibotClient testClient;
	private final static String serverHost = "raspberrypi";
	private final static int serverPort = 2000;
	private final static InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);
	private static final int MAX_WAIT = 20000;
	private static final String VOICE = "voxforge-ru-nsh";

	private static final StickMotionRequest forwardRequest = new StickMotionRequest(DirectionType.FORWARD);
	private static final StickMotionRequest backRequest = new StickMotionRequest(DirectionType.BACKWARD);
	private static final StickMotionRequest stopRequest = new StickMotionRequest(DirectionType.STOP);
	private static final StickMotionRequest leftRequest = new StickMotionRequest(DirectionType.LEFT);
	private static final StickMotionRequest rightRequest = new StickMotionRequest(DirectionType.RIGHT);
	private static final GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
	private static final DistanceRequest distanceRequest = new DistanceRequest();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testClient = new AkibotClient("akibot.client", new TestComponent(), serverAddress);
		testClient.getMyClientDescription().getTopicList().add(new Response());

		testClient.start();
		Thread.sleep(6000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void step1() throws FailedToSendMessageException, InterruptedException {

		// -----------------------------------------------------------------------------------
		say("Привет! Меня зовут АкиБот");
		Thread.sleep(1000);

		// -----------------------------------------------------------------------------------
		say("Мне один год, но я уже много что умею");
		Thread.sleep(1000);

		callServo("akibot.servo.front");

		// -----------------------------------------------------------------------------------
		sayAsync("У меня есть гусеничные ноги, и я умею ходить вперёд и назад");

		broadcast(forwardRequest);
		Thread.sleep(2000);
		broadcast(backRequest);
		Thread.sleep(2000);
		broadcast(stopRequest);

		// -----------------------------------------------------------------------------------
		sayAsync("Могу поворачивать в разные стороны");

		broadcast(leftRequest);
		Thread.sleep(1000);
		broadcast(rightRequest);
		Thread.sleep(2000);
		broadcast(leftRequest);
		Thread.sleep(1000);
		broadcast(stopRequest);
		Thread.sleep(1000);

		// -----------------------------------------------------------------------------------
		sayAsync("У меня даже есть ультросонар как у дельфинов. Только у меня их два!");

		callEchoLocator("akibot.echolocator.front");
		callEchoLocator("akibot.echolocator.back");

		// -----------------------------------------------------------------------------------
		say("Я уверен, вы удивлены, но это еще не все мои способности");
		Thread.sleep(1000);

		// -----------------------------------------------------------------------------------
		say("Благодаря гироскопу я могу ориентироватся в пространстве");
		Thread.sleep(1000);
		OrientationRequest messageOrientationRequest = new OrientationRequest();
		messageOrientationRequest.setNorthDegrreesXY(270);
		messageOrientationRequest.setPrecissionDegrees(1);
		messageOrientationRequest.setTimeoutMillis(10000);
		sendSync(messageOrientationRequest, 13000);

		say("Запад. В этой стороне нахоидся запад!");
		broadcast(forwardRequest);
		Thread.sleep(1000);
		broadcast(backRequest);
		Thread.sleep(1000);
		broadcast(stopRequest);

		// -----------------------------------------------------------------------------------
		sayAsync("Лучшее домашнее животное – это АкиБот.");
		sayAsync("Я бы спас Вселенную, но уже в пижаме и собрался спать.");
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
