package com.akibot.tanktrack.component.gyroscope;

import java.io.IOException;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class HMC5883LGyroscopeComponent extends GyroscopeComponent {
	static final AkiLogger log = AkiLogger.create(HMC5883LGyroscopeComponent.class);

	private I2CDevice hmc5883l;

	private double offsetDegrees;
	private double offsetX;
	private double offsetY;
	private double offsetZ;
	private final double RAD_TO_DEG = 57.295779513082320876798154814105f;


	public HMC5883LGyroscopeComponent(int busNumber, int deviceAddress, double offsetX, double offsetY, double offsetZ, double offsetDegrees) throws IOException {
		log.debug(this.getAkibotClient() + ": Initializing HMC5883LGyroscopeComponent");
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.offsetDegrees = offsetDegrees;

		I2CBus bus = I2CFactory.getInstance(busNumber);
		hmc5883l = bus.getDevice(deviceAddress);

		hmc5883l.write(2, (byte) 0); // enable
		hmc5883l.write("0b01110000".getBytes(), 0, "0b01110000".length());
		hmc5883l.write("0b00100000".getBytes(), 1, "0b01110000".length());
		hmc5883l.write("0b00000000".getBytes(), 2, "0b01110000".length());

		log.debug(this.getAkibotClient() + ": HMC5883LGyroscopeComponent initialized");
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

			// Reading magnetometer data:
			double x = read(3, hmc5883l) - offsetX;
			double y = read(7, hmc5883l) - offsetY;
			double z = read(5, hmc5883l) - offsetZ;

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

	private double read(int add, I2CDevice i2c) throws IOException {
		int val1 = i2c.read(add);
		int val2 = i2c.read(add + 1);
		int val = (val1 << 8) + val2;
		if (val >= 0x8000) {
			return -((65535 - val) + 1);
		} else {
			return val;
		}
	}

}
