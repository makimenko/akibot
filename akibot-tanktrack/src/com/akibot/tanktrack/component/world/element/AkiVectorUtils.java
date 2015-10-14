package com.akibot.tanktrack.component.world.element;

public class AkiVectorUtils {

	public static AkiPoint rotateVector(AkiLine line, AkiAngle angle) {

		double ix0 = line.getFrom().getX();
		double iy0 = line.getFrom().getY();

		double ix1 = line.getTo().getX();
		double iy1 = line.getTo().getY();

		double angleRadians = angle.getRadians();

		double x1 = ix1 - ix0;
		double y1 = iy1 - iy0;

		double x2 = (x1 * Math.cos(angleRadians)) - (y1 * Math.sin(angleRadians)) + ix0;
		double y2 = (y1 * Math.cos(angleRadians)) + (x1 * Math.sin(angleRadians)) + iy0;

		return new AkiPoint(x2, y2, 0); // TODO: Make double everywhere
	}

	public static AkiLine rotateLine(AkiLine line, AkiAngle angle) {
		return new AkiLine(line.getFrom(), rotateVector(line, angle));
	}

	public static float gradToRad(double rad) {
		return (float) (-rad * Math.PI / 180);
	}

	// public static void updateGridDistance(AkiNode gridNode, AkiNode distanceNode, DistanceDetails distanceDetails) {
	// AkiPoint totalPosition = new AkiPoint(0, 0, 0); // TODO: Total Position
	// AkiAngle totalAngle = new AkiAngle(); // TODO: Total Angle
	//
	// DistanceDetails totalDistanceDetail = new DistanceDetails();
	// totalDistanceDetail.setDistanceCm(distanceDetails.getDistanceCm());
	// totalDistanceDetail.setEndObstacle(distanceDetails.isEndObstacle());
	// totalDistanceDetail.setErrorAngle(distanceDetails.getErrorAngle());
	// totalDistanceDetail.setNorthAngle(totalAngle);
	// totalDistanceDetail.setPositionOffset(totalPosition);
	//
	// AkiGridGeometry gridGeometry = (AkiGridGeometry) gridNode.getGeometry();
	// gridGeometry.addDistance(totalDistanceDetail);
	//
	// }

	public static AkiNodeTransformation calculateRelativeTransformation(AkiNode nodeA, AkiNode nodeB) {
		return new AkiNodeTransformation(); // TODO: implement calculateRelativeTransformation
	}

}
