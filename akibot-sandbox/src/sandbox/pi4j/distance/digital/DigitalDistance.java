package sandbox.pi4j.distance.digital;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;

// DataSheet: http://www.datasheetspdf.com/PDF/DYP-ME007TX/907788/1

public class DigitalDistance {

	private int index = 0;
	private byte valueHData;
	private byte valueLData;
	private byte valueSum;
	private int count = 0;
	private long runningMillis = 50000;
	private final int BAUD_RATE = 9600;
	// /CH340G => dev/ttyUSB0
	private String device = "/dev/ttyUSB0"; // Serial.DEFAULT_COM_PORT;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("STARTING...");
		DigitalDistance distanceMeter = new DigitalDistance();
		distanceMeter.serialReadTest();
		// distanceMeter.serialListenerTest();

	}

	public void serialReadTest() throws InterruptedException {
		final Serial serial = SerialFactory.createInstance();
		System.out.println("Opening serial...");
		serial.open(device, BAUD_RATE);
		serial.flush();

		System.out.println("Reading...");
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime <= runningMillis) {
			byte data = (byte) serial.read();
			processByte(data);
		}

		System.out.println("EXIT (Processed count = " + count + ")");
		System.exit(0);
	}

	public void serialListenerTest() throws InterruptedException {
		final Serial serial = SerialFactory.createInstance();
		serial.addListener(new SerialDataListener() {
			@Override
			public void dataReceived(SerialDataEvent event) {
				processStringData(event.getData());
			}
		});

		System.out.println("Opening serial...");
		serial.open(Serial.DEFAULT_COM_PORT, BAUD_RATE);
		serial.flush();

		System.out.println("Waiting...");
		Thread.sleep(runningMillis);

		System.out.println("EXIT (Processed count = " + count + ")");
		System.exit(0);
	}

	private void processStringData(String stringData) {
		byte[] dataArray = stringData.getBytes();
		for (int i = 0; i < dataArray.length; i++) {
			byte data = dataArray[i];
			processByte(data);
		}
	}

	private void processByte(byte data) {

		// System.out.printf("0x%02X ", data);

		if (data == (byte) 0xFF) {
			// 1. 0XFF: for a frame to start data for judgment.
			index = 1;
			valueHData = 0;
			valueLData = 0;
			valueSum = 0;
		} else if (index == 1) {
			// 2. H_DATA: distance data of high eight.
			valueHData = data;
			index++;
		} else if (index == 2) {
			// 3. L_DATA: distance data of low 8 bits.
			valueLData = data;
			index++;
		} else if (index == 3) {
			// 4. SUM: data and, used to come to pass. Its 0XFF H_DATA + + L_DATA = SUM (only low 8)
			valueSum = data; // TODO: Calculate checksums
			index++;
			int mili = ((valueHData & 0xff) << 8) | (valueLData & 0xff);
			System.out.println("Distance = " + mili);
			count++;
		} else if (index == 4) {
			System.out.println(" Incorrect ");
		}
	}

}
