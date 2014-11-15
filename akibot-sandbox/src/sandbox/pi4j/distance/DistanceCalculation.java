package sandbox.pi4j.distance;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class DistanceCalculation {
	private GpioPinDigitalOutput triggerPin;
	private GpioPinDigitalInput echoPin;
	private DistanceListener distanceListener;
	private final static float SOUND_SPEED = 340.29f; // speed of sound in m/s

	private class DistanceListener implements GpioPinListenerDigital {
		private long stopTime;

		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			if (event.getState().isHigh()) {
				setStopTime(System.nanoTime());
				synchronized (echoPin) {
					echoPin.notify();
				}
			}
		}

		public long getStopTime() {
			return stopTime;
		}

		public void setStopTime(long stopTime) {
			this.stopTime = stopTime;
		}
	}

	public DistanceCalculation(GpioPinDigitalOutput triggerPin, GpioPinDigitalInput echoPin) {
		this.triggerPin = triggerPin;
		this.echoPin = echoPin;
		this.distanceListener = new DistanceListener();
		echoPin.addListener(distanceListener);
	}

	public double getDistance() {
		triggerPin.high();
		triggerPin.low();
		long startTime = System.nanoTime();
		try {
			synchronized (echoPin) {
				echoPin.wait(100);
			}
		} catch (Exception e) {
			return -1;
		}
		long microSeconds = (long) Math.ceil((distanceListener.getStopTime() - startTime) / 1000.0);

		float cm = microSeconds * SOUND_SPEED / (2 * 10000);

		return microSeconds;
	}

}
