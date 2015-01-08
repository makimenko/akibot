package sandbox.pi4j.temperature;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Temperature {

	public class TemperatureListener implements GpioPinListenerDigital {
		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			System.out.println("Event: " + event.getState());
		}
	}

	public void start() {
		GpioController gpio = GpioFactory.getInstance();

		TemperatureListener listener = new TemperatureListener();

		GpioPinDigitalOutput tempPinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22);
		tempPinOut.pulse(1);
		gpio.unprovisionPin(tempPinOut);

		GpioPinDigitalInput tempPinIn = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, PinPullResistance.PULL_DOWN);
		tempPinIn.addListener(listener);

	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Starting...");

		Temperature temerature = new Temperature();
		temerature.start();

		long startTime = System.currentTimeMillis();

		while (System.currentTimeMillis() - startTime < 5000) {
			Thread.sleep(1000);
			System.out.println("waiting...");
		}

		System.out.println("END.");
	}
}
