package sandbox.pi4j.magnetometer;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class MagnetometerOffsetCalculation {

	static double minX, maxX, minY, maxY, minZ, maxZ;
	static double offsetX, offsetY, offsetZ;
	static double totalX, totalY, totalZ;
	static double totalOffsetX, totalOffsetY, totalOffsetZ;
	static int samples = 0;

	public static void main(String[] args) throws IOException, InterruptedException {
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		I2CDevice hmc5883l = bus.getDevice(0x1e);

		hmc5883l.write(2, (byte) 0); // enable

		hmc5883l.write("0b01110000".getBytes(), 0, "0b01110000".length());
		hmc5883l.write("0b00100000".getBytes(), 1, "0b01110000".length());
		hmc5883l.write("0b00000000".getBytes(), 2, "0b01110000".length());

		// 561.000, -161.000, 24.000 / 182.450

		minX = 10000;
		minY = 10000;
		minZ = 10000;
		maxX = -10000;
		maxY = -10000;
		maxZ = -10000;

		long now = System.currentTimeMillis();
		while (System.currentTimeMillis() - now < 12000) {
			double x = read(3, hmc5883l);
			double y = read(7, hmc5883l);
			double z = read(5, hmc5883l);
			updateStats(x, y, z);

			// System.out.format("%10.3f, %10.3f, %10.3f / %10.3f %n", x, y, z);
			System.out.println(x + " " + y + " " + z);
			Thread.sleep(250);
		}

		offsetX = (maxX + minX) / 2;
		offsetY = (maxY + minY) / 2;
		offsetZ = (maxZ + minZ) / 2;

		totalOffsetX = totalX / samples;
		totalOffsetY = totalY / samples;
		totalOffsetZ = totalZ / samples;

		System.out.format("STATS            : %10.3f, %10.3f, %10.3f / %10.3f, %10.3f, %10.3f %n", minX, minY, minZ, maxX, maxY, maxZ);
		System.out.format("OFFSET (min/max) : %10.3f, %10.3f, %10.3f %n", offsetX, offsetY, offsetZ);
		System.out.format("OFFSET (total)   : %10.3f, %10.3f, %10.3f %n", totalOffsetX, totalOffsetY, totalOffsetZ);

	}

	private static void updateStats(double x, double y, double z) {
		totalX += x;
		totalY += y;
		totalZ += z;
		samples++;

		if (x < minX) {
			minX = x;
		}
		if (y < minY) {
			minY = y;
		}
		if (z < minZ) {
			minZ = z;
		}
		if (x > maxX) {
			maxX = x;
		}
		if (y > maxY) {
			maxY = y;
		}
		if (z > maxZ) {
			maxZ = z;
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
