package sandbox.pi4j.magnetometer;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Magnetometer {

	public static void main(String[] args) throws IOException, InterruptedException {
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		I2CDevice hmc5883l = bus.getDevice(0x1e);

		hmc5883l.write(2, (byte) 0); // enable

		hmc5883l.write("0b01110000".getBytes(), 0, "0b01110000".length());
		hmc5883l.write("0b00100000".getBytes(), 1, "0b01110000".length());
		hmc5883l.write("0b00000000".getBytes(), 2, "0b01110000".length());

		double offsetX = 337;
		double offsetY = -106;
		double offsetZ = 486;

		while (true) {
			double x = read(3, hmc5883l) - offsetX;
			double y = read(7, hmc5883l) - offsetY;
			double z = read(5, hmc5883l) - offsetZ;

			double RAD_TO_DEG = 57.295779513082320876798154814105f;
			Double bearing = Math.atan2(y, x) * RAD_TO_DEG + 180;
			// yz zy xy yx
			System.out.format("%10.3f, %10.3f, %10.3f / %10.3f %n", x, y, z, bearing);
			Thread.sleep(250);
		}

	}

	private static double read(int add, I2CDevice i2c) throws IOException {
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
