package com.akibot.tanktrack.component.obstacle;

import java.util.UUID;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;

public class ObstacleComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(ObstacleComponent.class);

	private GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
	private DistanceRequest distanceReqest = new DistanceRequest();
	private GyroscopeResponse gyroscopeResponse;
	private DistanceResponse distanceResponse;
	private boolean hasDistance = false;
	private boolean hasGyroscope = false;
	private ObstacleResponse response;
	private String id;
	private ObstacleRequest obstacleRequest;

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof ObstacleRequest) {
			onObstacleRequest((ObstacleRequest) message);
		} else if (message instanceof GyroscopeResponse) {
			onGyroscopeResponse((GyroscopeResponse) message);
		} else if (message instanceof DistanceResponse) {
			onDistanceResponse((DistanceResponse) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	@Override
	public void loadDefaultTopicList() {
		addTopic(new ObstacleRequest());
		addTopic(new GyroscopeResponse());
		addTopic(new DistanceResponse());
	}

	private void onObstacleRequest(ObstacleRequest obstacleRequest) throws FailedToSendMessageException {
		this.obstacleRequest = obstacleRequest;
		response = new ObstacleResponse();

		id = UUID.randomUUID().toString();

		gyroscopeValueRequest.setTo(obstacleRequest.getGyroscopeName());
		distanceReqest.setTo(obstacleRequest.getDistanceName());
		gyroscopeValueRequest.setSyncId(id);
		distanceReqest.setSyncId(id);

		hasDistance = false;
		hasGyroscope = false;

		broadcastMessage(gyroscopeValueRequest);
		broadcastMessage(distanceReqest);
	}

	private void onGyroscopeResponse(GyroscopeResponse gyroscopeResponse) throws FailedToSendMessageException {
		if (gyroscopeResponse.getSyncId() != null && gyroscopeResponse.getSyncId().equalsIgnoreCase(id)) {
			hasGyroscope = true;
			prepareResponse();
		}
	}

	private void onDistanceResponse(DistanceResponse distanceResponse) throws FailedToSendMessageException {
		if (distanceResponse.getSyncId() != null && distanceResponse.getSyncId().equalsIgnoreCase(id)) {
			hasDistance = true;
			prepareResponse();
		}
	}

	private void prepareResponse() throws FailedToSendMessageException {
		if (hasDistance && hasGyroscope) {
			log.debug(this.getAkibotClient() + ": Calculation Input: " + gyroscopeResponse.getNorthDegrreesXY() + ", " + distanceResponse.getMm());

			double radians = Math.toRadians(gyroscopeResponse.getNorthDegrreesXY());
			double x = Math.sin(radians) * distanceResponse.getMm();
			double y = Math.cos(radians) * distanceResponse.getMm();
			response.setX(x);
			response.setY(y);

			broadcastResponse(response, this.obstacleRequest);
			this.obstacleRequest = null;
		}
	}

}
