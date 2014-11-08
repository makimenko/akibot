package sandbox.pi4j.magnetometer;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Magnetometer {

	public static void main(String[] args) throws IOException, InterruptedException {
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		I2CDevice hmc5883l = bus.getDevice(0x1e);

		hmc5883l.write("0b01110000".getBytes(), 0, "0b01110000".length());
		hmc5883l.write("0b00100000".getBytes(), 1, "0b01110000".length());
		hmc5883l.write("0b00000000".getBytes(), 2, "0b01110000".length());

		double scale = 1.3;

		long now = System.currentTimeMillis();
		while (System.currentTimeMillis() - now < 60000) {
			double x_out = read(3, hmc5883l) * scale;
			double y_out = read(7, hmc5883l) * scale;
			double z_out = read(5, hmc5883l) * scale;
			double bearing;
			bearing = Math.atan2(y_out, x_out);
			bearing *= 57.2957795;
			bearing += 180.0;

			// bearing += 180.0;
			// if (bearing>360) {
			// bearing = bearing - 360.0;
			// }

			System.out.format("%10.3f, %10.3f, %10.3f / %10.3f %n", x_out, y_out, z_out, bearing);
			Thread.sleep(200);
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
