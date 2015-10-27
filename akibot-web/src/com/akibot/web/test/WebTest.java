package com.akibot.web.test;

import org.json.JSONObject;
import org.junit.Test;

import com.akibot.tanktrack.component.distance.DistanceDetails;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.world.element.Angle;
import com.akibot.tanktrack.component.world.element.Point;

public class WebTest {

	@Test
	public void jsonTest() {
		DistanceResponse distanceResponse = new DistanceResponse();
		DistanceDetails distanceDetails = new DistanceDetails();
		distanceDetails.setDistanceMm(100);
		distanceDetails.setEndObstacle(true);
		distanceDetails.setErrorAngle(new Angle(0));
		distanceDetails.setNorthAngle(new Angle(0));
		distanceDetails.setPositionOffset(new Point(0, 0, 0));
		distanceResponse.setDistanceDetails(distanceDetails);

		JSONObject objectMessage = new JSONObject(distanceResponse);

	}

}
