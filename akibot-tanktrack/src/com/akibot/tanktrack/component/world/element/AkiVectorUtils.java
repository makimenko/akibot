package com.akibot.tanktrack.component.world.element;

public class AkiVectorUtils {

	public static AkiPoint rotate2DVector(AkiPoint vector, AkiAngle angle) {
		double x1 = vector.getX();
		double y1 = vector.getY();
		double angleRadians = angle.getRadians();

		double x2 = (x1 * Math.cos(angleRadians)) - (y1 * Math.sin(angleRadians));
		double y2 = (y1 * Math.cos(angleRadians)) + (x1 * Math.sin(angleRadians));

		return new AkiPoint(x2, y2, 0);
	}

	public static AkiPoint rotateEndOfLine(AkiLine line, AkiAngle angle) {

		AkiPoint vector = rotate2DVector(line.getVector(), angle);
		vector.setX(vector.getX() + line.getFrom().getX());
		vector.setY(vector.getY() + line.getFrom().getY());
		// TODO: Support Z axis

		return vector;
	}

	public static AkiLine rotateLine(AkiLine line, AkiAngle angle) {
		return new AkiLine(line.getFrom(), rotateEndOfLine(line, angle));
	}

	public static float gradToRad(double rad) {
		return (float) (rad * Math.PI / 180);
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

	public static AkiNodeTransformation calculateRelativeTransformation(AkiNode nodeA, AkiNode nodeB)
			throws Exception {

		if (nodeA == null || nodeB == null) {
			throw new Exception();
		}
		AkiNodeTransformation cumulativeTransformation;
		if (nodeB.equals(nodeA)) {
			cumulativeTransformation = new AkiNodeTransformation();
			cumulativeTransformation.resetToDefaults();
		} else {
			cumulativeTransformation = calculateRelativeTransformation(nodeA, nodeB.getParentNode());
			if (nodeB.getTransformation() != null) {
				AkiPoint posVector = nodeB.getTransformation().getPosition();
				if (cumulativeTransformation.getRotation().getZ() != 0) {
					AkiAngle angle = new AkiAngle(cumulativeTransformation.getRotation().getZ());
					posVector = AkiVectorUtils.rotate2DVector(posVector, angle);
				}
				cumulativeTransformation.getPosition().add(posVector);
				cumulativeTransformation.getRotation().add(nodeB.getTransformation().getRotation());
			}
		}
		return cumulativeTransformation;
	}

}
