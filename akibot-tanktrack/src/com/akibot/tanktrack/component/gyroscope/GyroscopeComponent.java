package com.akibot.tanktrack.component.gyroscope;

import java.io.IOException;
import java.io.Serializable;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class GyroscopeComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(GyroscopeComponent.class);

	private I2CDevice i2cDevice;
	private GyroscopeConfiguration componentConfiguration;
	private final double RAD_TO_DEG = 57.295779513082320876798154814105f;

	private void selfTest(byte gain, byte cra) throws IOException, InterruptedException {
		log.debug(this.getAkibotClient() + ": HMC5883LGyroscopeComponent senf test is started");

		i2cDevice.write(GyroscopeConstantsHMC5883L.CONFIGURATION_REGISTER_A, cra);
		i2cDevice.write(GyroscopeConstantsHMC5883L.CONFIGURATION_REGISTER_B, gain);
		modeContinuousMeasurement();
		Thread.sleep(GyroscopeConstantsHMC5883L.SLEEP_MINI);

		System.out.println("-");
		for (int i = 0; i <= 7; i++) {
			int x = readShort(GyroscopeConstantsHMC5883L.DATA_OUTPUT_MSB_REGISTER_X, i2cDevice);
			int y = readShort(GyroscopeConstantsHMC5883L.DATA_OUTPUT_MSB_REGISTER_Y, i2cDevice);
			int z = readShort(GyroscopeConstantsHMC5883L.DATA_OUTPUT_MSB_REGISTER_Z, i2cDevice);
			System.out.println("Test: " + x + ", " + y + ", " + z);
			Thread.sleep(GyroscopeConstantsHMC5883L.SLEEP_MAXI);
		}
		// TODO: Still unclear how to validate output!
		i2cDevice.write(GyroscopeConstantsHMC5883L.CONFIGURATION_REGISTER_A, GyroscopeConstantsHMC5883L.CRA_8SAMPLE_15MHZ_NORMAL); // Exit self test mode
	}

	public void modeIdle() throws IOException, InterruptedException {
		i2cDevice.write(GyroscopeConstantsHMC5883L.MODE_REGISTER, GyroscopeConstantsHMC5883L.IDLE_MODE);
		Thread.sleep(GyroscopeConstantsHMC5883L.SLEEP_MINI);
	}

	public void modeSingleMeasurement() throws IOException, InterruptedException {
		i2cDevice.write(GyroscopeConstantsHMC5883L.MODE_REGISTER, GyroscopeConstantsHMC5883L.SINGLE_MEASUREMENT_MODE);
		Thread.sleep(GyroscopeConstantsHMC5883L.SLEEP_MINI);
	}

	public void modeContinuousMeasurement() throws IOException, InterruptedException {
		i2cDevice.write(GyroscopeConstantsHMC5883L.MODE_REGISTER, GyroscopeConstantsHMC5883L.CONTINUOUS_MEASUREMENT_MODE);
		Thread.sleep(GyroscopeConstantsHMC5883L.SLEEP_MINI);
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof GyroscopeValueRequest) {
			onGyroscopeValueRequest((GyroscopeValueRequest) message);
		}
		// else if (message instanceof GyroscopeConfigurationRequest) {
		// onGyroscopeConfigurationRequest((GyroscopeConfigurationRequest) message);
		// } else if (message instanceof GyroscopeOffsetConfigurationRequest) {
		// onGyroscopeOffsetConfigurationRequest((GyroscopeOffsetConfigurationRequest) message);
		// }
		else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	@Override
	public void onGetConfigurationResponse(GetConfigurationResponse getConfigurationResponse) throws FailedToConfigureException {
		Serializable responseValue = getConfigurationResponse.getComponentConfiguration();
		if (responseValue instanceof GyroscopeConfiguration) {
			setGyroscopeConfiguration((GyroscopeConfiguration) responseValue);
		} else {
			throw new FailedToConfigureException(responseValue.toString());
		}
	}

	public GyroscopeConfiguration getGyroscopeConfiguration() {
		return componentConfiguration;
	}

	public void setGyroscopeConfiguration(GyroscopeConfiguration gyroscopeConfiguration) throws FailedToConfigureException {
		this.componentConfiguration = gyroscopeConfiguration;
		init();
	}

	private void onGyroscopeOffsetConfigurationRequest(GyroscopeOffsetConfigurationRequest gyroscopeOffsetConfigurationRequest) {
		log.debug("onGyroscopeOffsetConfigurationRequest: " + gyroscopeOffsetConfigurationRequest);
		getGyroscopeConfiguration().setGyroscopeOffsetConfiguration(gyroscopeOffsetConfigurationRequest.getGyroscopeOffsetConfiguration());
	}

	private void onGyroscopeConfigurationRequest(GyroscopeConfigurationRequest gyroscopeConfigurationRequest) throws FailedToConfigureException {
		log.debug("onGyroscopeConfigurationRequest: " + gyroscopeConfigurationRequest);
		setGyroscopeConfiguration(gyroscopeConfigurationRequest.getGyroscopeConfiguration());
	}

	private void init() throws FailedToConfigureException {
		try {
			getComponentStatus().setReady(false);
			log.debug(this.getAkibotClient() + ": Initializing HMC5883LGyroscopeComponent");

			I2CBus bus = I2CFactory.getInstance(componentConfiguration.getBusNumber());
			i2cDevice = bus.getDevice(componentConfiguration.getDeviceAddress());


			modeIdle();
			//selfTest(GyroscopeConstantsHMC5883L.GAIN_8_1, GyroscopeConstantsHMC5883L.CRA_8SAMPLE_15MHZ_NORMAL);
			// selfTest(gain_8_1, cra_8samples_15Hz_normal);

			i2cDevice.write(GyroscopeConstantsHMC5883L.CONFIGURATION_REGISTER_A, GyroscopeConstantsHMC5883L.CRA_8SAMPLE_15MHZ_NORMAL);
			i2cDevice.write(GyroscopeConstantsHMC5883L.CONFIGURATION_REGISTER_B, GyroscopeConstantsHMC5883L.GAIN_1_3_DEFAULT);
			modeContinuousMeasurement();

			getComponentStatus().setReady(true);
			log.debug(this.getAkibotClient() + ": HMC5883LGyroscopeComponent initialized");
		} catch (Exception e) {
			log.catching(e);
			// throw new FailedToStartException(e);
		}
	}

	private void onGyroscopeValueRequest(GyroscopeValueRequest gyroscopeValueRequest) throws IOException, FailedToSendMessageException {
		long startTime = System.currentTimeMillis();
		GyroscopeResponse response = new GyroscopeResponse();

		// Reading magnetometer data:
		double x = readShort(GyroscopeConstantsHMC5883L.DATA_OUTPUT_MSB_REGISTER_X, i2cDevice);
		double y = readShort(GyroscopeConstantsHMC5883L.DATA_OUTPUT_MSB_REGISTER_Y, i2cDevice);
		double z = readShort(GyroscopeConstantsHMC5883L.DATA_OUTPUT_MSB_REGISTER_Z, i2cDevice);

		if (!gyroscopeValueRequest.isIgnoreOffset()) {
			x -= componentConfiguration.getGyroscopeOffsetConfiguration().getOffsetX();
			y -= componentConfiguration.getGyroscopeOffsetConfiguration().getOffsetY();
			z -= componentConfiguration.getGyroscopeOffsetConfiguration().getOffsetZ();
		}

		// Calculating North Degrees:
		double northDegreesXY = (Math.atan2(y, x) * RAD_TO_DEG) + 180;
		// roundRobinUtils.add(bearing, gyroscopeConfiguration.getOffsetDegrees());

		// Preparing response
		response.setX(x);
		response.setY(y);
		response.setZ(z);
		response.setNorthDegreesXY(northDegreesXY);

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
	public GyroscopeConfiguration getComponentConfiguration() {
		return componentConfiguration;
	}

	@Override
	public void loadDefaults() {
		addTopic(new GyroscopeRequest());
	}
}
