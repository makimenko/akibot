package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.DefaultDNSComponent;
import com.akibot.engine2.component.configuration.ComponentConfiguration;
import com.akibot.engine2.component.configuration.ConfigurationComponent;
import com.akibot.engine2.component.configuration.GetConfigurationRequest;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.component.configuration.PutConfigurationRequest;
import com.akibot.engine2.component.configuration.PutConfigurationResponse;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.distance.DistanceMeterConfiguration;
import com.akibot.tanktrack.component.echolocator.EchoLocatorConfiguration;
import com.akibot.tanktrack.component.gyroscope.GyroscopeConfiguration;
import com.akibot.tanktrack.component.gyroscope.GyroscopeOffsetConfiguration;
import com.akibot.tanktrack.component.orientation.OrientationConfiguration;
import com.akibot.tanktrack.component.servo.ServoConfiguration;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisConfiguration;
import com.akibot.tanktrack.component.tanktrack.TankTrackConfiguration;
import com.akibot.tanktrack.component.world.element.ColladaGeometry;
import com.akibot.tanktrack.component.world.element.GridConfiguration;
import com.akibot.tanktrack.component.world.element.GridGeometry;
import com.akibot.tanktrack.component.world.element.Node;
import com.akibot.tanktrack.component.world.element.NodeTransformation;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.VectorUtils;
import com.akibot.tanktrack.component.world.message.WorldConfiguration;
import com.akibot.tanktrack.component.world.message.WorldContent;

public class LoadConfiguration {
	static final AkiLogger log = AkiLogger.create(LoadConfiguration.class);
	private AkibotClient client;
	private int TIMEOUT = 1000;

	public static void main(String[] args) throws Exception {
		LoadConfiguration loadConfiguration = new LoadConfiguration("localhost", 7122);
		System.out.println("Upload default configuration:");
		loadConfiguration.saveAkibotGyroscope();
		loadConfiguration.saveAkibotFrontDistance();
		loadConfiguration.saveAkibotFrontEcholocator();
		loadConfiguration.saveAkibotBackEcholocator();
		loadConfiguration.saveAkibotSpeech();
		loadConfiguration.saveAkibotOrientation();
		loadConfiguration.saveAkibotServoFrontBase();
		loadConfiguration.saveAkibotServoFrontHead();
		loadConfiguration.saveAkibotServoBackBase();
		loadConfiguration.saveAkibotServoBackHead();
		loadConfiguration.saveAkibotTankTrack();
		loadConfiguration.saveAkibotWorld();
		System.out.println("Done");
	}

	public LoadConfiguration(String dnsHost, int dnsPort) throws Exception {
		System.out.println("*** " + this.getClass().getName());
		InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		AkibotClient dns = new AkibotClient("LoadConfiguration.dns", new DefaultDNSComponent(), dnsPort);

		AkibotClient configClient = new AkibotClient("LoadConfiguration.config", new ConfigurationComponent("."), dnsAddress);
		configClient.getMyClientDescription().getTopicList().add(new GetConfigurationRequest());
		configClient.getMyClientDescription().getTopicList().add(new PutConfigurationRequest());

		client = new AkibotClient("LoadConfiguration.client", new DefaultComponent(), dnsAddress);
		client.getMyClientDescription().getTopicList().add(new GetConfigurationResponse());
		client.getMyClientDescription().getTopicList().add(new PutConfigurationResponse());

		dns.start();
		configClient.start();
		client.start();

		Thread.sleep(100);
	}

	private void save(String name, ComponentConfiguration componentConfiguration) throws FailedToSendMessageException {
		System.out.println("  Save: " + name);
		PutConfigurationRequest putConfigurationRequest = new PutConfigurationRequest();
		putConfigurationRequest.setName(name);
		putConfigurationRequest.setComponentConfiguration(componentConfiguration);
		client.getOutgoingMessageManager().sendSyncRequest(putConfigurationRequest, TIMEOUT);
	}

	private void saveAkibotGyroscope() throws FailedToSendMessageException {
		GyroscopeOffsetConfiguration gyroscopeOffsetConfiguration = new GyroscopeOffsetConfiguration();
		// {"data":"{\"from\":\"akibot.gyroscope.calibration\",\"to\":\"akibot.web\",\"newOffsetX\":389,\"newOffsetY\":-173,\"newOffsetZ\":-1117.5}"}
		gyroscopeOffsetConfiguration.setOffsetX(389);
		gyroscopeOffsetConfiguration.setOffsetY(-173);
		gyroscopeOffsetConfiguration.setOffsetZ(-1117.5);

		GyroscopeConfiguration gyroscopeConfiguration = new GyroscopeConfiguration();
		gyroscopeConfiguration.setGyroscopeOffsetConfiguration(gyroscopeOffsetConfiguration);
		gyroscopeConfiguration.setBusNumber(Constants.GYROSCOPE_BUS_NUMBER);
		gyroscopeConfiguration.setDeviceAddress(Constants.GYROSCOPE_DEVICE_ADDRESS);

		save("akibot.gyroscope", gyroscopeConfiguration);
	}

	private void saveAkibotFrontDistance() throws FailedToSendMessageException {
		DistanceMeterConfiguration distanceMeterConfiguration = new DistanceMeterConfiguration();
		distanceMeterConfiguration.setTriggerPin(Constants.FRONT_DISTANCE_TRIGGER_PIN);
		distanceMeterConfiguration.setEchoPin(Constants.FRONT_DISTANCE_ECHO_PIN);
		distanceMeterConfiguration.setTimeoutMicroseconds(Constants.FRONT_DISTANCE_TIMEOUT);
		save("akibot.front.distance", distanceMeterConfiguration);
	}

	private void saveAkibotFrontEcholocator() throws FailedToSendMessageException {
		// EchoLocatorFront:
		EchoLocatorConfiguration echoLocatorFrontConfig = new EchoLocatorConfiguration();
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
		save("akibot.echolocator.front", echoLocatorFrontConfig);
	}

	private void saveAkibotBackEcholocator() throws FailedToSendMessageException {
		// EchoLocatorFront:
		EchoLocatorConfiguration echoLocatorBackConfig = new EchoLocatorConfiguration();
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
		save("akibot.echolocator.back", echoLocatorBackConfig);
	}

	private void saveAkibotSpeech() throws FailedToSendMessageException {
		SpeechSynthesisConfiguration speechSynthesisConfiguration = new SpeechSynthesisConfiguration();
		speechSynthesisConfiguration.setMaryttsHost(Constants.SPEECH_HOST);
		speechSynthesisConfiguration.setMaryttsPort(Constants.SPEECH_PORT);
		speechSynthesisConfiguration.setMarytssDefaultVoice(Constants.SPEECH_VOICE);

		save("akibot.speech", speechSynthesisConfiguration);
	}

	private void saveAkibotOrientation() throws FailedToSendMessageException {
		OrientationConfiguration orientationConfiguration = new OrientationConfiguration();
		orientationConfiguration.setTankTrackName("akibot.tanktrack");
		orientationConfiguration.setGyroscopeName("akibot.gyroscope");
		orientationConfiguration.setSyncRequestTimeout(2000);
		orientationConfiguration.setEasyDegrees(45);
		orientationConfiguration.setEasyMillis(100);
		orientationConfiguration.setStepMillis(100);
		save("akibot.orientation", orientationConfiguration);
	}

	private void saveAkibotServoFrontBase() throws FailedToSendMessageException {
		ServoConfiguration servoConfiguration = new ServoConfiguration();
		servoConfiguration.setServoPin(Constants.FRONT_SERVO_BASE_PIN);
		servoConfiguration.setInitialValue(0);
		servoConfiguration.setPwmRange(200);
		servoConfiguration.setDivisor(200);
		save("akibot.servo.front.base", servoConfiguration);
	}

	private void saveAkibotServoFrontHead() throws FailedToSendMessageException {
		ServoConfiguration servoConfiguration = new ServoConfiguration();
		servoConfiguration.setServoPin(Constants.FRONT_SERVO_HEAD_PIN);
		servoConfiguration.setInitialValue(0);
		servoConfiguration.setPwmRange(200);
		servoConfiguration.setDivisor(200);
		save("akibot.servo.front.head", servoConfiguration);
	}

	private void saveAkibotServoBackBase() throws FailedToSendMessageException {
		ServoConfiguration servoConfiguration = new ServoConfiguration();
		servoConfiguration.setServoPin(Constants.BACK_SERVO_BASE_PIN);
		servoConfiguration.setInitialValue(0);
		servoConfiguration.setPwmRange(200);
		servoConfiguration.setDivisor(200);
		save("akibot.servo.back.base", servoConfiguration);
	}

	private void saveAkibotServoBackHead() throws FailedToSendMessageException {
		ServoConfiguration servoConfiguration = new ServoConfiguration();
		servoConfiguration.setServoPin(Constants.BACK_SERVO_HEAD_PIN);
		servoConfiguration.setInitialValue(0);
		servoConfiguration.setPwmRange(200);
		servoConfiguration.setDivisor(200);
		save("akibot.servo.back.head", servoConfiguration);
	}

	private void saveAkibotTankTrack() throws FailedToSendMessageException {
		TankTrackConfiguration tankTrackConfiguration = new TankTrackConfiguration();

		tankTrackConfiguration.setRightIApin(Constants.TANK_TRACK_RIGHT_IA);
		tankTrackConfiguration.setRightIBpin(Constants.TANK_TRACK_RIGHT_IB);
		tankTrackConfiguration.setRightSpeedPin(Constants.TANK_TRACK_RIGHT_SPEED);

		tankTrackConfiguration.setLeftIApin(Constants.TANK_TRACK_LEFT_IA);
		tankTrackConfiguration.setLeftIBpin(Constants.TANK_TRACK_LEFT_IB);
		tankTrackConfiguration.setLeftSpeedPin(Constants.TANK_TRACK_LEFT_SPEED);

		save("akibot.tanktrack", tankTrackConfiguration);
	}

	private void saveAkibotWorld() throws FailedToSendMessageException {
		String name = "akibot.world";

		Node worldNode = new Node(name);

		// ======================== Grid Node:
		int cellCount = 50;
		int cellSizeCm = 10;
		int positionOffset = cellCount * cellSizeCm / 2;
		GridConfiguration gridConfiguration = new GridConfiguration(cellCount, cellCount, cellSizeCm, 2, new Point(-positionOffset, -positionOffset, 0));
		GridGeometry gridGeometry = new GridGeometry(gridConfiguration);
		Node gridNode = new Node(Constants.NODE_NAME_GRID);
		gridNode.setGeometry(gridGeometry);
		worldNode.attachChild(gridNode);

		// ======================== Robot Node:
		ColladaGeometry robotGeometry = new ColladaGeometry();
		robotGeometry.setFileName("../js/loader/AkiBot.dae");
		Node robotNode = new Node(Constants.NODE_NAME_ROBOT);
		robotNode.setGeometry(robotGeometry);

		gridNode.attachChild(robotNode);

		// ======================== Gyroscope
		Node gyroscopeNode = new Node(Constants.COMPONENT_NAME_AKIBOT_GYROSCOPE);
		gyroscopeNode.setStickToParent(true);
		robotNode.attachChild(gyroscopeNode);

		// ======================== frontDistanceNode
		Node frontDistanceNode = new Node(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT);
		NodeTransformation frontDistanceTransformation = new NodeTransformation();
		frontDistanceTransformation.setPosition(new Point(0, 8f, 5f));
		frontDistanceNode.setTransformation(frontDistanceTransformation);
		frontDistanceNode.setStickToParent(true);
		robotNode.attachChild(frontDistanceNode);

		// ======================== backDistanceNode
		Node backDistanceNode = new Node(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK);
		NodeTransformation backDistanceTransformation = new NodeTransformation();
		backDistanceTransformation.setPosition(new Point(0, -8f, 5f));
		backDistanceTransformation.setRotation(new Point(0, 0, VectorUtils.gradToRad(180)));
		backDistanceNode.setTransformation(backDistanceTransformation);
		backDistanceNode.setStickToParent(true);
		robotNode.attachChild(backDistanceNode);

		WorldConfiguration worldConfiguration = new WorldConfiguration();
		WorldContent worldContent = new WorldContent();
		worldContent.setWorldNode(worldNode);
		worldConfiguration.setWorldContent(worldContent);
		save(name, worldConfiguration);
	}
}
