package com.akibot.tanktrack.component.obstacle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;

public class ObstacleComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(ObstacleComponent.class.getName());

	GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
	DistanceRequest distanceReqest = new DistanceRequest();

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof ObstacleRequest) {
			ObstacleRequest request = (ObstacleRequest) message;
			gyroscopeValueRequest.setTo(request.getGyroscopeName());
			distanceReqest.setTo(request.getDistanceName());
			ObstacleResponse response = new ObstacleResponse();
			response.copySyncId(message);

			GyroscopeResponse gyroscopeResponse = (GyroscopeResponse) this.getClient().syncRequest(gyroscopeValueRequest, 1000);
			DistanceResponse distanceResponse = (DistanceResponse) this.getClient().syncRequest(distanceReqest, 1000);

			log.debug("Calculation Input: " + gyroscopeResponse.getNorthDegrreesXY() + ", " + distanceResponse.getMm());

			double radians = Math.toRadians(gyroscopeResponse.getNorthDegrreesXY());
			double x = Math.sin(radians) * distanceResponse.getMm();
			double y = Math.cos(radians) * distanceResponse.getMm();
			response.setX(x);
			response.setY(y);

			this.getClient().send(response);
		}
	}

}
