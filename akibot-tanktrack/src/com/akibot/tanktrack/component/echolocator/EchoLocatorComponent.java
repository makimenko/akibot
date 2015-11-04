package com.akibot.tanktrack.component.echolocator;

import akibot.jni.java.AkibotJniLibrary;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.distance.DistanceDetails;
import com.akibot.tanktrack.component.orientation.RoundRobinUtils;
import com.akibot.tanktrack.component.world.element.Angle;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.VectorUtils;

public class EchoLocatorComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(EchoLocatorComponent.class);
	private AkibotJniLibrary lib;
	private EchoLocatorConfiguration componentConfiguration;
	private double gradTotal = 180;
	private int positionCount = 21;
	private int startPosition = 4;
	private RoundRobinUtils robinUtils = new RoundRobinUtils(360);

	@Override
	public void loadDefaults() {
		addTopic(new EchoLocatorRequest());
	}

	@Override
	public EchoLocatorConfiguration getComponentConfiguration() {
		return componentConfiguration;
	}

	@Override
	public void onGetConfigurationResponse(GetConfigurationResponse getConfigurationResponse) throws FailedToConfigureException {
		getComponentStatus().setReady(false);
		componentConfiguration = (EchoLocatorConfiguration) getConfigurationResponse.getComponentConfiguration();

		try {
			this.lib = instantiate("akibot.jni.java." + componentConfiguration.getAkibotJniLibraryInstance(), AkibotJniLibrary.class);
			log.debug(this.getAkibotClient() + ": onGetConfigurationResponse-getAkibotJniLibraryInstance: "
					+ componentConfiguration.getAkibotJniLibraryInstance());

			this.lib.echoLocatorInitialize(componentConfiguration.getDistanceTriggerPin(), componentConfiguration.getDistanceEchoPin(),
					componentConfiguration.getDistanceTimeout(), componentConfiguration.getSleepBeforeDistance(), componentConfiguration.getServoBasePin(),
					componentConfiguration.getServoHeadPin(), componentConfiguration.getServoLongTime(), componentConfiguration.getServoStepTime(),
					componentConfiguration.getDistanceCount());
		} catch (Exception e) {
			throw new FailedToConfigureException(e);
		}

		getComponentStatus().setReady(true);
	}

	public <T> T instantiate(final String className, final Class<T> type) {
		try {
			return type.cast(Class.forName(className).newInstance());
		} catch (final InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (final ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof EchoLocatorRequest) {
			EchoLocatorResponse response = new EchoLocatorResponse();
			EchoLocatorRequest request = (EchoLocatorRequest) message;

			MultipleDistanceDetails multipleDistanceDetails = getMultipleDistanceDetailsFromEcholocator(request);
			response.setMultipleDistanceDetails(multipleDistanceDetails);

			broadcastResponse(response, request);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private MultipleDistanceDetails getMultipleDistanceDetailsFromEcholocator(EchoLocatorRequest request) {
		float result[] = lib.echoLocatorScanDistance(request.getServoBaseFrom(), request.getServoBaseTo(), request.getServoBaseStep(),
				request.getServoHeadNormal(), request.isTrustToLastPosition());

		MultipleDistanceDetails multipleDistanceDetails = new MultipleDistanceDetails();

		int step;
		int currentPosition = request.getServoBaseFrom();
		if (request.getServoBaseFrom() < request.getServoBaseTo()) {
			step = request.getServoBaseStep();
		} else {
			step = -request.getServoBaseStep();
		}

		int index = 0;
		for (int i = 1; i <= result.length; i++) {
			currentPosition += step;
			DistanceDetails currentDistanceDetails = getDistanceDetails(result[index], currentPosition);
			multipleDistanceDetails.getDistanceDetailsList().add(currentDistanceDetails);
			index++;
		}
		return multipleDistanceDetails;
	}

	private DistanceDetails getDistanceDetails(float distanceMm, int servoBasePosition) {
		DistanceDetails distanceDetails = new DistanceDetails();
		distanceDetails.setDistanceMm(distanceMm);
		distanceDetails.setEndObstacle(distanceMm <= componentConfiguration.getDistanceMaxMm());
		distanceDetails.setErrorAngle(componentConfiguration.getErrorAngle());
		distanceDetails.setNorthAngle(getAngleFromServoBasePosition(servoBasePosition));
		distanceDetails.setPositionOffset(new Point(0, 0, 0));
		return distanceDetails;
	}

	private Angle getAngleFromServoBasePosition(int servoBasePosition) {
		double stepGrad = gradTotal / (positionCount - 1); // 9grad
		double offsetSteps = servoBasePosition - startPosition;
		double resultGrad = offsetSteps * stepGrad;
		resultGrad = robinUtils.add(resultGrad, -90);
		return new Angle(VectorUtils.gradToRad(resultGrad));
	}

}
