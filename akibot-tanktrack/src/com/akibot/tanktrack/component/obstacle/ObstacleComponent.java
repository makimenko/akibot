package com.akibot.tanktrack.component.obstacle;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;

public class ObstacleComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(ObstacleComponent.class.getName());

	private GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
	private DistanceRequest distanceReqest = new DistanceRequest();
	private GyroscopeResponse gyroscopeResponse;
	private DistanceResponse distanceResponse;
	private boolean hasDistance = false;
	private boolean hasGyroscope = false;
	private ObstacleResponse response;
	private String id;

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof ObstacleRequest) {
			ObstacleRequest request = (ObstacleRequest) message;
			response = new ObstacleResponse();
			response.copySyncId(request);
			id = UUID.randomUUID().toString();

			gyroscopeValueRequest.setTo(request.getGyroscopeName());
			distanceReqest.setTo(request.getDistanceName());
			gyroscopeValueRequest.setSyncId(id);
			distanceReqest.setSyncId(id);

			hasDistance = false;
			hasGyroscope = false;

			getAkibotClient().getOutgoingMessageManager().broadcastMessage(gyroscopeValueRequest);
			getAkibotClient().getOutgoingMessageManager().broadcastMessage(distanceReqest);

		} else if (message instanceof GyroscopeResponse) {
			if (message.getSyncId() != null && message.getSyncId().equalsIgnoreCase(id)) {
				gyroscopeResponse = (GyroscopeResponse) message;
				hasGyroscope = true;
				prepareResponse();
			}
		} else if (message instanceof DistanceResponse) {
			if (message.getSyncId() != null && message.getSyncId().equalsIgnoreCase(id)) {
				distanceResponse = (DistanceResponse) message;
				hasDistance = true;
				prepareResponse();
			}
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

			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);

		}
	}

}
