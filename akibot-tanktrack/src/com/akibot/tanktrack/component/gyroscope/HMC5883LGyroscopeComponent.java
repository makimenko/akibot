package com.akibot.tanktrack.component.gyroscope;

import java.io.IOException;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
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

	private double offsetDegrees;
	private double offsetX;
	private double offsetY;
	private double offsetZ;
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
	private final byte gain_0_88 = 0b000;
	private final byte gain_1_3_default = 0b001;
	private final byte gain_1_9 = 0b010;
	private final byte gain_2_5 = 0b011;
	private final byte gain_4_0 = 0b100;
	private final byte gain_4_7 = 0b101;
	private final byte gain_5_6 = 0b110;
	private final byte gain_8_1 = 0b111;

	// HMC5883L Operating Mode
	private final byte continuous_measurement_mode = 0b00;
	private final byte single_measurement_mode = 0b01;
	private final byte idle_mode = 0b11;

	public HMC5883LGyroscopeComponent(int busNumber, int deviceAddress, double offsetX, double offsetY, double offsetZ, double offsetDegrees)
			throws IOException, InterruptedException {
		log.debug(this.getAkibotClient() + ": Initializing HMC5883LGyroscopeComponent");
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.offsetDegrees = offsetDegrees;

		I2CBus bus = I2CFactory.getInstance(busNumber);
		hmc5883l = bus.getDevice(deviceAddress);

		selfTest();

		hmc5883l.write(configurationRegisterA, cra_8samples_15Hz_normal);
		hmc5883l.write(configurationRegisterB, gain_1_3_default);
		modeSingleMeasurement();

		log.debug(this.getAkibotClient() + ": HMC5883LGyroscopeComponent initialized");
	}

	private void selfTest() throws IOException, InterruptedException {
		log.debug(this.getAkibotClient() + ": HMC5883LGyroscopeComponent senf test is started");

		hmc5883l.write(configurationRegisterA, cra_8samples_15Hz_test_positive);
		hmc5883l.write(configurationRegisterB, gain_0_88);
		modeSingleMeasurement();
		modeSingleMeasurement();

		for (int i = 0; i <= 7; i++) {
			modeSingleMeasurement();
			int x = readShort(dataOutputMSBRegisterX, hmc5883l);
			int y = readShort(dataOutputMSBRegisterY, hmc5883l);
			int z = readShort(dataOutputMSBRegisterZ, hmc5883l);
			// System.out.println("Test: " + x + ", " + y + ", " + z);
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
			GyroscopeConfigurationRequest config = (GyroscopeConfigurationRequest) message;
			offsetX = config.getOffsetX();
			offsetY = config.getOffsetY();
			offsetZ = config.getOffsetZ();
			offsetDegrees = config.getOffsetDegrees();
			log.debug("GyroscopeConfigurationRequest: offsetX=" + offsetX + ", offsetY=" + offsetY + ", offsetZ=" + offsetZ + ", offsetDegrees="
					+ offsetDegrees);

		} else if (message instanceof GyroscopeValueRequest) {
			long startTime = System.currentTimeMillis();
			GyroscopeResponse response = new GyroscopeResponse();
			response.copySyncId(message);

			// GyroscopeValueRequest request = (GyroscopeValueRequest) message;
			modeSingleMeasurement();
			// Reading magnetometer data:
			double x = readShort(dataOutputMSBRegisterX, hmc5883l) - offsetX;
			double y = readShort(dataOutputMSBRegisterY, hmc5883l) - offsetY;
			double z = readShort(dataOutputMSBRegisterZ, hmc5883l) - offsetZ;

			// Calculating North Degrees:
			double bearing = Math.atan2(y, x) * RAD_TO_DEG + 180;
			double northDegreesXY = bearing + offsetDegrees;

			if (northDegreesXY > 360) {
				northDegreesXY = northDegreesXY - 360.0;
			}

			// Preparing response
			response.setX(x);
			response.setY(y);
			response.setZ(z);
			response.setNorthDegrreesXY(northDegreesXY);

			log.trace(this.getAkibotClient() + ": Duration: " + (System.currentTimeMillis() - startTime));
			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		}

	}

	private int readRawShort(int add, I2CDevice i2c) throws IOException {
		int val1 = i2c.read(add);
		int val2 = i2c.read(add + 1);
		return (val1 << 8) + val2;
	}

	private int readShort(int add, I2CDevice i2c) throws IOException {
		int val = readRawShort(add, i2c);
		if (val > Short.MAX_VALUE) {
			return -((65535 - val) + 1);
		} else {
			return val;
		}
	}

}
