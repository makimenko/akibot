package sandbox.pi4j.distance;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class DistanceCalculation {
	private GpioPinDigitalOutput triggerPin;
	private GpioPinDigitalInput echoPin;
	private DistanceListener distanceListener;
	private final static float SOUND_SPEED = 340.29f; // speed of sound in m/s

	private class DistanceListener implements GpioPinListenerDigital {
		private long startTime;
		private long stopTime;

		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			if (event.getState().isHigh()) {
				startTime = System.nanoTime();
			} else if (event.getState().isLow()) {
				stopTime = System.nanoTime();
				synchronized (echoPin) {
					echoPin.notify();
				}
			}
		}

		public long getDiff() throws Exception {
			if (stopTime > startTime) {
				return stopTime - startTime;
			} else {
				throw new Exception("Timeout!");
			}
		}
	}

	public DistanceCalculation(GpioPinDigitalOutput triggerPin, GpioPinDigitalInput echoPin) {
		this.triggerPin = triggerPin;
		this.echoPin = echoPin;
		this.distanceListener = new DistanceListener();
		echoPin.addListener(distanceListener);
	}

	public double getDistance() {

		triggerPin.pulse(1);

		try {
			synchronized (echoPin) {
				echoPin.wait(70);
			}

			long microSeconds = (long) Math.ceil((distanceListener.getDiff()) / 1000.0);
			float cm = microSeconds * SOUND_SPEED / (2 * 10000);
			return cm;
		} catch (Exception e) {
			return -1;
		}

	}

}
