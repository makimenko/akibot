package sandbox.pi4j.obstacle.ir;

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/*
 *  Four-Way Infrared Tracing 4 Channel Tracking Sensor Module Transmission Line Modules Obstacle Avoidance for Arduino
 *  NOTE: working ok with 5V! (level-converter 5v-3v needed)°
 *  NOTE: Output is voltage 5V-0V (distance)
 *  Max distance:
 *  -- Hand 2cm (6.5-15 max 2 sensors, marked as "OK")
 *  -- White/Green 4.5 cm
 *  
 */

public class Obstacle4ChannelsIR {

	private Pin in1 = RaspiPin.GPIO_01;
	private Pin in2 = RaspiPin.GPIO_02;
	private Pin in3 = RaspiPin.GPIO_03;
	private Pin in4 = RaspiPin.GPIO_04;

	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println("STARTING...");
		Obstacle4ChannelsIR obstacle = new Obstacle4ChannelsIR();
		obstacle.test();
		System.out.println("END.");
	}

	private void test() throws InterruptedException {
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalInput digitalIn1 = gpio.provisionDigitalInputPin(in1);
		// final GpioPinDigitalInput digitalIn2 = gpio.provisionDigitalInputPin(in2);
		// final GpioPinDigitalInput digitalIn3 = gpio.provisionDigitalInputPin(in3, PinPullResistance.PULL_DOWN);
		// final GpioPinDigitalInput digitalIn4 = gpio.provisionDigitalInputPin(in4, PinPullResistance.PULL_DOWN);

		GpioPinListenerDigital listener = new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				System.out.println(event.getPin() + " = " + event.getState());
			}
		};

		digitalIn1.addListener(listener);
		// digitalIn2.addListener(listener);
		// digitalIn3.addListener(listener);
		// digitalIn4.addListener(listener);

		System.out.println("Listening");
		for (;;) {
			Thread.sleep(500);
		}

	}

}
