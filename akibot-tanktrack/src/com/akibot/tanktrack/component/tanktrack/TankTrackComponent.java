package com.akibot.tanktrack.component.tanktrack;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
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

public class TankTrackComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(TankTrackComponent.class);
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
	private long rightDistanceCounterTotal;
	private long leftDistanceCounterTotal;

	private long rightDistanceCounterInteration;
	private long leftDistanceCounterInteration;

	public TankTrackComponent(Pin rightIApin, Pin rightIBpin, Pin rightSpeedPin, Pin leftIApin, Pin leftIBpin, Pin leftSpeedPin) {
		this.rightIApin = rightIApin;
		this.rightIBpin = rightIBpin;
		this.rightSpeedPin = rightSpeedPin;
		this.leftIApin = leftIApin;
		this.leftIBpin = leftIBpin;
		this.leftSpeedPin = leftSpeedPin;
	}

	private void resetDistanceCounters() {
		this.rightDistanceCounterTotal = 0;
		this.leftDistanceCounterTotal = 0;
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

	private void changeDirection(DirectionType direction) {
		switch (direction) {
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
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof StickMotionRequest) {
			onStickMotionRequest((StickMotionRequest) message);
		} else if (message instanceof TimedMotionRequest) {
			onTimedMotionRequest((TimedMotionRequest) message);
		} else if (message instanceof MotionDistanceCounterRequest) {
			onMotionDistanceCounterRequest((MotionDistanceCounterRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onStickMotionRequest(StickMotionRequest stickMotionRequest) throws FailedToSendMessageException {
		log.debug(this.getAkibotClient() + ": " + stickMotionRequest);
		changeDirection(stickMotionRequest.getDirectionType());
		StickMotionResponse response = new StickMotionResponse();
		broadcastResponse(response, stickMotionRequest);
	}

	private void onTimedMotionRequest(TimedMotionRequest timedMotionRequest) throws InterruptedException, FailedToSendMessageException {
		log.debug(this.getAkibotClient() + ": " + timedMotionRequest);

		defaultState();
		long startLeftDistanceCounter = leftDistanceCounterTotal;
		long startRightDistanceCounter = rightDistanceCounterTotal;

		changeDirection(timedMotionRequest.getDirectionType());
		Thread.sleep(timedMotionRequest.getMilliseconds());
		defaultState();

		TimedMotionResponse response = new TimedMotionResponse();
		response.getDistanceCounter().setLeftDistanceCounter(leftDistanceCounterTotal - startLeftDistanceCounter);
		response.getDistanceCounter().setRightDistanceCounter(rightDistanceCounterTotal - startRightDistanceCounter);
		broadcastResponse(response, timedMotionRequest);
	}

	private void onMotionDistanceCounterRequest(MotionDistanceCounterRequest motionDistanceCounterRequest) throws FailedToSendMessageException {
		MotionDistanceCounterResponse response = new MotionDistanceCounterResponse();
		response.getDistanceCounter().setLeftDistanceCounter(leftDistanceCounterTotal);
		response.getDistanceCounter().setRightDistanceCounter(rightDistanceCounterTotal);
		if (motionDistanceCounterRequest.isResetAfterResponse()) {
			resetDistanceCounters();
		}
		broadcastResponse(response, motionDistanceCounterRequest);
	}

	@Override
	public void startComponent() throws FailedToStartException {
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
					rightDistanceCounterTotal++;
				}
			});
			leftSpeedPinInput.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					// System.out.println(" --> LEFT GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
					leftDistanceCounterTotal++;
				}
			});

			resetDistanceCounters();
			defaultState();

			log.debug(this.getAkibotClient() + ": TankTrack GPIOs initialized successfully");
		} catch (Exception e) {
			throw new FailedToStartException(e);
		}
	}

	@Override
	public void loadDefaultTopicList() {
		addTopic(new MotionRequest());
	}
}
