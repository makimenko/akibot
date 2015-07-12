package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultServerComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.tanktrack.component.audio.AudioComponent;
import com.akibot.tanktrack.component.audio.AudioRequest;
import com.akibot.tanktrack.component.distance.DistanceMeterComponent;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.echolocator.EchoLocatorComponent;
import com.akibot.tanktrack.component.echolocator.EchoLocatorConfig;
import com.akibot.tanktrack.component.echolocator.EchoLocatorRequest;
import com.akibot.tanktrack.component.gyroscope.GyroscopeRequest;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.HMC5883LGyroscopeComponent;
import com.akibot.tanktrack.component.orientation.OrientationComponent;
import com.akibot.tanktrack.component.orientation.OrientationRequest;
import com.akibot.tanktrack.component.servo.ServoComponent;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.servo.ServoResponse;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisComponent;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisRequest;
import com.akibot.tanktrack.component.tanktrack.DD1TankTrackComponent;
import com.akibot.tanktrack.component.tanktrack.MotionRequest;
import com.akibot.tanktrack.component.tanktrack.MotionResponse;

public class AkiBotLauncher {
	static final AkiLogger log = AkiLogger.create(AkiBotLauncher.class);

	public static void main(String[] args) throws Exception {
		String serverHost = "raspberrypi";
		int serverPort = 2000;

		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		// Server:
		AkibotClient server = new AkibotClient("akibot.server", new DefaultServerComponent(), serverPort);
		server.start();

		// TankTrack:
		AkibotClient tankTrack = new AkibotClient("akibot.tanktrack", new DD1TankTrackComponent(Constants.TANK_TRACK_RIGHT_IA, Constants.TANK_TRACK_RIGHT_IB,
				Constants.TANK_TRACK_RIGHT_SPEED, Constants.TANK_TRACK_LEFT_IA, Constants.TANK_TRACK_LEFT_IB, Constants.TANK_TRACK_LEFT_SPEED), serverAddress);
		tankTrack.getMyClientDescription().getTopicList().add(new MotionRequest());

		// Gyroscope:
		AkibotClient gyroscope = new AkibotClient("akibot.gyroscope", new HMC5883LGyroscopeComponent(Constants.GYROSCOPE_BUS_NUMBER,
				Constants.GYROSCOPE_DEVICE_ADDRESS, Constants.GYROSCOPE_OFFSET_X, Constants.GYROSCOPE_OFFSET_Y, Constants.GYROSCOPE_OFFSET_Z,
				Constants.GYROSCOPE_OFFSET_DEGREES), serverAddress);
		gyroscope.getMyClientDescription().getTopicList().add(new GyroscopeRequest());

		// SpeechSynthesis:
		AkibotClient speechSynthesisClient = new AkibotClient("akibot.speech", new SpeechSynthesisComponent(Constants.SPEECH_HOST, Constants.SPEECH_PORT,
				Constants.SPEECH_VOICE), serverAddress);
		speechSynthesisClient.getMyClientDescription().getTopicList().add(new SpeechSynthesisRequest());

		// Distance Meter
		AkibotClient distance = new AkibotClient("akibot.front.distance", new DistanceMeterComponent(Constants.FRONT_DISTANCE_TRIGGER_PIN,
				Constants.FRONT_DISTANCE_ECHO_PIN, Constants.FRONT_DISTANCE_TIMEOUT), serverAddress);
		distance.getMyClientDescription().getTopicList().add(new DistanceRequest());

		// Servo motors
		AkibotClient servoFrontBase = new AkibotClient("akibot.servo.front.base", new ServoComponent(Constants.FRONT_SERVO_BASE_PIN, 0, 200, 200),
				serverAddress);
		servoFrontBase.getMyClientDescription().getTopicList().add(new ServoRequest());

		AkibotClient servoFrontHead = new AkibotClient("akibot.servo.front.head", new ServoComponent(Constants.FRONT_SERVO_HEAD_PIN, 0, 200, 200),
				serverAddress);
		servoFrontHead.getMyClientDescription().getTopicList().add(new ServoRequest());

		AkibotClient servoBackBase = new AkibotClient("akibot.servo.back.base", new ServoComponent(Constants.BACK_SERVO_BASE_PIN, 0, 200, 200), serverAddress);
		servoBackBase.getMyClientDescription().getTopicList().add(new ServoRequest());

		AkibotClient servoBackHead = new AkibotClient("akibot.servo.back.head", new ServoComponent(Constants.BACK_SERVO_HEAD_PIN, 0, 200, 200), serverAddress);
		servoBackHead.getMyClientDescription().getTopicList().add(new ServoRequest());

		AkibotClient testComponent = new AkibotClient("akibot.test", new TestComponent(), serverAddress);
		testComponent.getMyClientDescription().getTopicList().add(new TestRequest());

		AkibotClient audioComponent = new AkibotClient("akibot.audio", new AudioComponent(), serverAddress);
		audioComponent.getMyClientDescription().getTopicList().add(new AudioRequest());

		// EchoLocatorFront:
		EchoLocatorConfig echoLocatorFrontConfig = new EchoLocatorConfig();
		echoLocatorFrontConfig.setDistanceTriggerPin(Constants.ECHOLOCATOR_FRONT_DISTANCE_TRIGGER_PIN);
		echoLocatorFrontConfig.setDistanceEchoPin(Constants.ECHOLOCATOR_FRONT_DISTANCE_ECHO_PIN);
		echoLocatorFrontConfig.setDistanceTimeout(Constants.ECHOLOCATOR_FRONT_DISTANCE_TIMEOUT);
		echoLocatorFrontConfig.setSleepBeforeDistance(Constants.ECHOLOCATOR_FRONT_SLEEP_BEFORE_DISNTANCE);
		echoLocatorFrontConfig.setServoBasePin(Constants.ECHOLOCATOR_FRONT_SERVO_BASE_PIN);
		echoLocatorFrontConfig.setServoHeadPin(Constants.ECHOLOCATOR_FRONT_SERVO_HEAD_PIN);
		// TODO: Put into request servoLongTime?:
		echoLocatorFrontConfig.setServoLongTime(Constants.ECHOLOCATOR_FRONT_SERVO_LONG_TIME);
		echoLocatorFrontConfig.setServoStepTime(Constants.ECHOLOCATOR_FRONT_SERVO_STEP_TIME);
		echoLocatorFrontConfig.setDistanceCount(Constants.ECHOLOCATOR_FRONT_DISTANCE_COUNT);

		AkibotClient echoLocatorFront = new AkibotClient("akibot.echolocator.front", new EchoLocatorComponent(echoLocatorFrontConfig), serverAddress);
		echoLocatorFront.getMyClientDescription().getTopicList().add(new EchoLocatorRequest());
		echoLocatorFront.getMyClientDescription().getTopicList().add(new DistanceResponse());
		echoLocatorFront.getMyClientDescription().getTopicList().add(new ServoResponse());

		// EchoLocatorFront:
		EchoLocatorConfig echoLocatorBackConfig = new EchoLocatorConfig();
		echoLocatorBackConfig.setDistanceTriggerPin(Constants.ECHOLOCATOR_BACK_DISTANCE_TRIGGER_PIN);
		echoLocatorBackConfig.setDistanceEchoPin(Constants.ECHOLOCATOR_BACK_DISTANCE_ECHO_PIN);
		echoLocatorBackConfig.setDistanceTimeout(Constants.ECHOLOCATOR_BACK_DISTANCE_TIMEOUT);
		echoLocatorBackConfig.setSleepBeforeDistance(Constants.ECHOLOCATOR_BACK_SLEEP_BEFORE_DISNTANCE);
		echoLocatorBackConfig.setServoBasePin(Constants.ECHOLOCATOR_BACK_SERVO_BASE_PIN);
		echoLocatorBackConfig.setServoHeadPin(Constants.ECHOLOCATOR_BACK_SERVO_HEAD_PIN);
		// TODO: Put into request servoLongTime?:
		echoLocatorBackConfig.setServoLongTime(Constants.ECHOLOCATOR_BACK_SERVO_LONG_TIME);
		echoLocatorBackConfig.setServoStepTime(Constants.ECHOLOCATOR_BACK_SERVO_STEP_TIME);
		echoLocatorBackConfig.setDistanceCount(Constants.ECHOLOCATOR_BACK_DISTANCE_COUNT);

		AkibotClient echoLocatorBack = new AkibotClient("akibot.echolocator.back", new EchoLocatorComponent(echoLocatorBackConfig), serverAddress);
		echoLocatorBack.getMyClientDescription().getTopicList().add(new EchoLocatorRequest());
		echoLocatorBack.getMyClientDescription().getTopicList().add(new DistanceResponse());
		echoLocatorBack.getMyClientDescription().getTopicList().add(new ServoResponse());

		AkibotClient orientation = new AkibotClient("akibot.orientation", new OrientationComponent("akibot.tanktrack", "akibot.gyroscope"), serverAddress);
		orientation.getMyClientDescription().getTopicList().add(new OrientationRequest());
		orientation.getMyClientDescription().getTopicList().add(new MotionResponse());
		orientation.getMyClientDescription().getTopicList().add(new GyroscopeResponse());

		// Start all
		tankTrack.start();
		gyroscope.start();
		speechSynthesisClient.start();
		distance.start();
		// obstacle.start();
		servoFrontBase.start();
		servoFrontHead.start();
		servoBackHead.start();
		servoBackBase.start();
		testComponent.start();
		echoLocatorFront.start();
		echoLocatorBack.start();
		audioComponent.start();
		orientation.start();

		System.out.println("AkiBotLauncher: Started");
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
