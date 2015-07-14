package com.akibot.tanktrack.component.gyroscope;

import java.io.IOException;

import com.akibot.engine2.component.configuration.GetConfigurationRequest;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.orientation.RoundRobinUtils;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * http://www51.honeywell.com/aero/common/documents/myaerospacecatalog-documents /Defense_Brochures-documents/HMC5883L_3-Axis_Digital_Compass_IC.pdf
 * 
 * @author dm
 *
 */
public class HMC5883LGyroscopeComponent extends GyroscopeComponent {
	static final AkiLogger log = AkiLogger.create(HMC5883LGyroscopeComponent.class);

	private I2CDevice hmc5883l;

	private int busNumber;
	private int deviceAddress;
	private GyroscopeConfiguration gyroscopeConfiguration;
	private final double RAD_TO_DEG = 57.295779513082320876798154814105f;

	// HMC5883L Register Addresses:
	private final int configurationRegisterA = 0;
	private final int configurationRegisterB = 1;
	private final int modeRegister = 2;
	private final int dataOutputMSBRegisterX = 3;
	private final int dataOutputMSBRegisterZ = 5;
	private final int dataOutputMSBRegisterY = 7;

	// HMC5883L: Configuration Register A:
	private final byte cra_8samples_15Hz_normal = 0b01110000; // Set to 8 samples @ 15Hz
	private final byte cra_8samples_15Hz_test_positive = 0b01110001; // Set to 8 samples @ 15Hz, Positive bias configuration
	private final byte cra_8samples_15Hz_test_negative = 0b01110010; // Set to 8 samples @ 15Hz, Positive bias configuration

	// HMC5883L Configuration Register B: Gain Configuration Bits
	private final byte gain_0_88 = (byte) 0b00000000;
	private final byte gain_1_3_default = (byte) 0b00100000;
	private final byte gain_1_9 = (byte) 0b01000000;
	private final byte gain_2_5 = (byte) 0b01100000;
	private final byte gain_4_0 = (byte) 0b10000000;
	private final byte gain_4_7 = (byte) 0b10100000;
	private final byte gain_5_6 = (byte) 0b11000000;
	private final byte gain_8_1 = (byte) 0b11100000;

	// HMC5883L Operating Mode
	private final byte continuous_measurement_mode = 0b00;
	private final byte single_measurement_mode = 0b01;
	private final byte idle_mode = 0b11;
	private RoundRobinUtils roundRobinUtils;

	public HMC5883LGyroscopeComponent(int busNumber, int deviceAddress) throws IOException, InterruptedException {
		this.busNumber = busNumber;
		this.deviceAddress = deviceAddress;
		this.roundRobinUtils = new RoundRobinUtils(360);
	}

	private void selfTest(byte gain, byte cra) throws IOException, InterruptedException {
		log.debug(this.getAkibotClient() + ": HMC5883LGyroscopeComponent senf test is started");

		hmc5883l.write(configurationRegisterA, cra);
		hmc5883l.write(configurationRegisterB, gain);
		modeContinuousMeasurement();
		Thread.sleep(10);

		System.out.println("-");
		for (int i = 0; i <= 7; i++) {
			int x = readShort(dataOutputMSBRegisterX, hmc5883l);
			int y = readShort(dataOutputMSBRegisterY, hmc5883l);
			int z = readShort(dataOutputMSBRegisterZ, hmc5883l);
			System.out.println("Test: " + x + ", " + y + ", " + z);
			Thread.sleep(80);
		}
		// TODO: Still unclear how to validate output!
		hmc5883l.write(configurationRegisterA, cra_8samples_15Hz_normal); // Exit self test mode
	}

	public void modeIdle() throws IOException, InterruptedException {
		hmc5883l.write(modeRegister, idle_mode);
		Thread.sleep(7);
	}

	public void modeSingleMeasurement() throws IOException, InterruptedException {
		hmc5883l.write(modeRegister, single_measurement_mode);
		Thread.sleep(7);
	}

	public void modeContinuousMeasurement() throws IOException, InterruptedException {
		hmc5883l.write(modeRegister, continuous_measurement_mode);
		Thread.sleep(7);
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof GyroscopeConfigurationRequest) {
			onGyroscopeConfigurationRequest((GyroscopeConfigurationRequest) message);
		} else if (message instanceof GyroscopeValueRequest) {
			onGyroscopeValueRequest((GyroscopeValueRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onGyroscopeConfigurationRequest(GyroscopeConfigurationRequest gyroscopeConfigurationRequest) {
		gyroscopeConfiguration = gyroscopeConfigurationRequest.getGyroscopeConfiguration();
		log.debug("GyroscopeConfigurationRequest: " + gyroscopeConfiguration);
	}

	private void onGyroscopeValueRequest(GyroscopeValueRequest gyroscopeValueRequest) throws IOException, FailedToSendMessageException {
		long startTime = System.currentTimeMillis();
		GyroscopeResponse response = new GyroscopeResponse();

		// Reading magnetometer data:
		double x = readShort(dataOutputMSBRegisterX, hmc5883l);
		double y = readShort(dataOutputMSBRegisterY, hmc5883l);
		double z = readShort(dataOutputMSBRegisterZ, hmc5883l);

		if (!gyroscopeValueRequest.isIgnoreOffset()) {
			x -= gyroscopeConfiguration.getOffsetX();
			y -= gyroscopeConfiguration.getOffsetY();
			z -= gyroscopeConfiguration.getOffsetZ();
		}

		// Calculating North Degrees:
		double bearing = (Math.atan2(y, x) * RAD_TO_DEG) + 180;
		double northDegreesXY = roundRobinUtils.add(bearing, gyroscopeConfiguration.getOffsetDegrees());

		// Preparing response
		response.setX(x);
		response.setY(y);
		response.setZ(z);
		response.setNorthDegrreesXY(northDegreesXY);

		log.trace(this.getAkibotClient() + ": Duration: " + (System.currentTimeMillis() - startTime));
		broadcastResponse(response, gyroscopeValueRequest);
	}

	private int readRawShort(int address, I2CDevice i2c) throws IOException {
		int val1 = i2c.read(address);
		int val2 = i2c.read(address + 1);
		return (val1 << 8) + val2;
	}

	private int readShort(int address, I2CDevice i2c) throws IOException {
		int val = readRawShort(address, i2c);
		if (val > Short.MAX_VALUE) {
			return -(((2 * Short.MAX_VALUE) + 1 - val) + 1);
		} else {
			return val;
		}
	}

	@Override
	public void loadConfiguration() throws FailedToConfigureException {
		try {
			Thread.sleep(500);
			GetConfigurationRequest getConfigurationRequest = new GetConfigurationRequest();
			getConfigurationRequest.setName(getAkibotClient().getName() + "-gyroscopeConfiguration");
			GetConfigurationResponse getConfigurationResponse = (GetConfigurationResponse) sendSyncRequest(getConfigurationRequest, 2000);
			this.gyroscopeConfiguration = (GyroscopeConfiguration) getConfigurationResponse.getValue();
		} catch (Exception e) {
			throw new FailedToConfigureException(e);
		}
	}

	@Override
	public void start() throws FailedToStartException {
		try {
			log.debug(this.getAkibotClient() + ": Initializing HMC5883LGyroscopeComponent");

			I2CBus bus = I2CFactory.getInstance(busNumber);
			hmc5883l = bus.getDevice(deviceAddress);

			modeIdle();
			// selfTest(gain_0_88, cra_8samples_15Hz_normal);
			// selfTest(gain_8_1, cra_8samples_15Hz_normal);

			hmc5883l.write(configurationRegisterA, cra_8samples_15Hz_normal);
			hmc5883l.write(configurationRegisterB, gain_1_3_default);
			modeContinuousMeasurement();

			log.debug(this.getAkibotClient() + ": HMC5883LGyroscopeComponent initialized");
		} catch (Exception e) {
			throw new FailedToStartException(e);
		}
	}

	@Override
	public void loadDefaultTopicList() {
		addTopic(new GyroscopeRequest());
		addTopic(new GetConfigurationResponse());
	}
}
