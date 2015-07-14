package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultServerComponent;
import com.akibot.engine2.component.configuration.ConfigurationComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.tanktrack.component.audio.AudioComponent;
import com.akibot.tanktrack.component.distance.DistanceMeterComponent;
import com.akibot.tanktrack.component.echolocator.EchoLocatorComponent;
import com.akibot.tanktrack.component.echolocator.EchoLocatorConfig;
import com.akibot.tanktrack.component.gyroscope.HMC5883LGyroscopeComponent;
import com.akibot.tanktrack.component.orientation.OrientationComponent;
import com.akibot.tanktrack.component.servo.ServoComponent;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisComponent;
import com.akibot.tanktrack.component.tanktrack.DD1TankTrackComponent;

public class AkiBotLauncher {
	static final AkiLogger log = AkiLogger.create(AkiBotLauncher.class);

	public static void main(String[] args) throws Exception {
		String serverHost = Constants.SERVER_HOST;
		int serverPort = Constants.SERVER_PORT;

		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		// Server:
		AkibotClient server = new AkibotClient("akibot.server", new DefaultServerComponent(), serverPort);
		server.start();

		// ConfigurationComponent:
		AkibotClient configClient = new AkibotClient("akibot.config", new ConfigurationComponent("."), serverAddress);

		// TankTrack:
		AkibotClient tankTrack = new AkibotClient("akibot.tanktrack", new DD1TankTrackComponent(Constants.TANK_TRACK_RIGHT_IA, Constants.TANK_TRACK_RIGHT_IB,
				Constants.TANK_TRACK_RIGHT_SPEED, Constants.TANK_TRACK_LEFT_IA, Constants.TANK_TRACK_LEFT_IB, Constants.TANK_TRACK_LEFT_SPEED), serverAddress);

		// Gyroscope:
		AkibotClient gyroscope = new AkibotClient("akibot.gyroscope", new HMC5883LGyroscopeComponent(Constants.GYROSCOPE_BUS_NUMBER,
				Constants.GYROSCOPE_DEVICE_ADDRESS), serverAddress);

		// SpeechSynthesis:
		AkibotClient speechSynthesisClient = new AkibotClient("akibot.speech", new SpeechSynthesisComponent(Constants.SPEECH_HOST, Constants.SPEECH_PORT,
				Constants.SPEECH_VOICE), serverAddress);

		// Distance Meter
		AkibotClient distance = new AkibotClient("akibot.front.distance", new DistanceMeterComponent(Constants.FRONT_DISTANCE_TRIGGER_PIN,
				Constants.FRONT_DISTANCE_ECHO_PIN, Constants.FRONT_DISTANCE_TIMEOUT), serverAddress);

		// Servo motors
		AkibotClient servoFrontBase = new AkibotClient("akibot.servo.front.base", new ServoComponent(Constants.FRONT_SERVO_BASE_PIN, 0, 200, 200),
				serverAddress);

		AkibotClient servoFrontHead = new AkibotClient("akibot.servo.front.head", new ServoComponent(Constants.FRONT_SERVO_HEAD_PIN, 0, 200, 200),
				serverAddress);

		AkibotClient servoBackBase = new AkibotClient("akibot.servo.back.base", new ServoComponent(Constants.BACK_SERVO_BASE_PIN, 0, 200, 200), serverAddress);

		AkibotClient servoBackHead = new AkibotClient("akibot.servo.back.head", new ServoComponent(Constants.BACK_SERVO_HEAD_PIN, 0, 200, 200), serverAddress);

		AkibotClient testComponent = new AkibotClient("akibot.test", new TestComponent(), serverAddress);
		testComponent.getMyClientDescription().getTopicList().add(new TestRequest());

		AkibotClient audioComponent = new AkibotClient("akibot.audio", new AudioComponent(), serverAddress);

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

		AkibotClient orientation = new AkibotClient("akibot.orientation", new OrientationComponent("akibot.tanktrack", "akibot.gyroscope"), serverAddress);

		// Start all
		configClient.start();
		Thread.sleep(1000);

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
