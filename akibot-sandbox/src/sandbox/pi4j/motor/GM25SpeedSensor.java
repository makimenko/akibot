package sandbox.pi4j.motor;

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GM25SpeedSensor {

	private Pin inS1 = RaspiPin.GPIO_29;
	private Pin inS2 = RaspiPin.GPIO_28;
	private long loopMs = 10000;
	private long s1Count = 0;
	private long s2Count = 0;

	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println("STARTING...");
		GM25SpeedSensor sensor = new GM25SpeedSensor();
		sensor.test();
		System.out.println("END.");
	}

	private void test() throws InterruptedException {
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalInput digitalInS1 = gpio.provisionDigitalInputPin(inS1);
		final GpioPinDigitalInput digitalInS2 = gpio.provisionDigitalInputPin(inS2);

		GpioPinListenerDigital listener1 = new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s1Count++;
			}
		};

		GpioPinListenerDigital listener2 = new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s2Count++;
			}
		};

		digitalInS1.addListener(listener1);
		digitalInS2.addListener(listener2);

		System.out.println("Listening");
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime <= loopMs) {
			Thread.sleep(500);
		}

		System.out.println("  s1Count=" + s1Count);
		System.out.println("  s2Count=" + s2Count);
	}

}
