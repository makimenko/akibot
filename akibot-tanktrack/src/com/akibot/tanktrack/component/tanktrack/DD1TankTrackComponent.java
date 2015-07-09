package com.akibot.tanktrack.component.tanktrack;

import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class DD1TankTrackComponent extends TankTrackComponent {
	static final AkiLogger log = AkiLogger.create(DD1TankTrackComponent.class);
	private GpioController gpio;
	// Pins
	private Pin rightIApin;
	private Pin rightIBpin;
	private Pin leftIApin;
	private Pin leftIBpin;
	private Pin leftSpeedPin;
	private Pin rightSpeedPin;
	// GPIO Input and Output
	private GpioPinDigitalOutput leftBackwardPinOutput;
	private GpioPinDigitalOutput leftForwardPinOutput;
	private GpioPinDigitalOutput rightBackwardPinOutput;
	private GpioPinDigitalOutput rightForwardPinOutput;
	private GpioPinDigitalInput leftSpeedPinInput;
	private GpioPinDigitalInput rightSpeedPinInput;
	// Speed and Distance counters
	private long rightDistanceCounter;
	private long leftDistanceCounter;

	public DD1TankTrackComponent(Pin rightIApin, Pin rightIBpin, Pin rightSpeedPin, Pin leftIApin, Pin leftIBpin, Pin leftSpeedPin) {
		this.rightIApin = rightIApin;
		this.rightIBpin = rightIBpin;
		this.rightSpeedPin = rightSpeedPin;
		this.leftIApin = leftIApin;
		this.leftIBpin = leftIBpin;
		this.leftSpeedPin = leftSpeedPin;
	}

	private void resetDistanceCounters() {
		this.rightDistanceCounter = 0;
		this.leftDistanceCounter = 0;
	}

	private void defaultState() {
		if (leftForwardPinOutput.isHigh())
			leftForwardPinOutput.low();

		if (rightForwardPinOutput.isHigh())
			rightForwardPinOutput.low();

		if (leftBackwardPinOutput.isHigh())
			leftBackwardPinOutput.low();

		if (rightBackwardPinOutput.isHigh())
			rightBackwardPinOutput.low();
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof StickMotionRequest) {
			StickMotionRequest request = (StickMotionRequest) message;
			log.debug(this.getAkibotClient() + ": " + request);
			switch (request.getDirectionType()) {
			case FORWARD:
				defaultState();
				rightForwardPinOutput.high();
				leftForwardPinOutput.high();
				break;
			case BACKWARD:
				defaultState();
				rightBackwardPinOutput.high();
				leftBackwardPinOutput.high();
				break;
			case LEFT:
				defaultState();
				rightForwardPinOutput.high();
				leftBackwardPinOutput.high();
				break;
			case RIGHT:
				defaultState();
				leftForwardPinOutput.high();
				rightBackwardPinOutput.high();
				break;
			default:
				defaultState();
				break;
			}
			StickMotionResponse response = new StickMotionResponse();
			response.copySyncId(message);
			this.getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);

		} else if (message instanceof TimedMotionRequest) {
			TimedMotionRequest timedMotionRequest = (TimedMotionRequest) message;
			log.debug(this.getAkibotClient() + ": " + timedMotionRequest);

			defaultState();
			long startLeftDistanceCounter = leftDistanceCounter;
			long startRightDistanceCounter = rightDistanceCounter;

			switch (timedMotionRequest.getDirectionType()) {
			case FORWARD:
				rightForwardPinOutput.high();
				leftForwardPinOutput.high();
				break;
			case BACKWARD:
				rightBackwardPinOutput.high();
				leftBackwardPinOutput.high();
				break;
			case LEFT:
				rightForwardPinOutput.high();
				leftBackwardPinOutput.high();
				break;
			case RIGHT:
				leftForwardPinOutput.high();
				rightBackwardPinOutput.high();
				break;
			}
			Thread.sleep(timedMotionRequest.getMilliseconds());
			defaultState();

			TimedMotionResponse response = new TimedMotionResponse();
			response.copySyncId(message);
			response.getDistanceCounter().setLeftDistanceCounter(leftDistanceCounter - startLeftDistanceCounter);
			response.getDistanceCounter().setRightDistanceCounter(rightDistanceCounter - startRightDistanceCounter);
			this.getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);

		} else if (message instanceof MotionDistanceCounterRequest) {
			MotionDistanceCounterRequest request = (MotionDistanceCounterRequest) message;
			MotionDistanceCounterResponse response = new MotionDistanceCounterResponse();
			response.copySyncId(message);
			response.getDistanceCounter().setLeftDistanceCounter(leftDistanceCounter);
			response.getDistanceCounter().setRightDistanceCounter(rightDistanceCounter);
			if (request.isResetAfterResponse()) {
				resetDistanceCounters();
			}
			this.getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		}
	}

	@Override
	public void start() throws FailedToStartException {
		try {
			log.debug(this.getAkibotClient() + ": Initializing Tanktrack GPIOs");
			gpio = GpioFactory.getInstance();

			rightBackwardPinOutput = gpio.provisionDigitalOutputPin(rightIApin, "rightIA", PinState.LOW);
			rightForwardPinOutput = gpio.provisionDigitalOutputPin(rightIBpin, "rightIB", PinState.LOW);
			rightSpeedPinInput = gpio.provisionDigitalInputPin(rightSpeedPin, PinPullResistance.PULL_DOWN);

			leftBackwardPinOutput = gpio.provisionDigitalOutputPin(leftIApin, "leftIA", PinState.LOW);
			leftForwardPinOutput = gpio.provisionDigitalOutputPin(leftIBpin, "leftIB", PinState.LOW);
			leftSpeedPinInput = gpio.provisionDigitalInputPin(leftSpeedPin, PinPullResistance.PULL_DOWN); // TODO: Not working!!!

			rightSpeedPinInput.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					// System.out.println(" --> RIGHT GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
					rightDistanceCounter++;
				}
			});
			leftSpeedPinInput.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					// System.out.println(" --> LEFT GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
					leftDistanceCounter++;
				}
			});

			resetDistanceCounters();
			defaultState();

			log.debug(this.getAkibotClient() + ": TankTrack GPIOs initialized successfully");
		} catch (Exception e) {
			throw new FailedToStartException(e);
		}
	}

}
