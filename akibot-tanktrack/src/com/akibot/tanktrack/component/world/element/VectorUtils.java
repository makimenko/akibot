package com.akibot.tanktrack.component.world.element;

public class VectorUtils {

	public static Point rotate2DVector(Point vector, Angle angle) {
		double x1 = vector.getX();
		double y1 = vector.getY();
		double angleRadians = angle.getRadians();

		double x2 = (x1 * Math.cos(angleRadians)) - (y1 * Math.sin(angleRadians));
		double y2 = (y1 * Math.cos(angleRadians)) + (x1 * Math.sin(angleRadians));

		return new Point(x2, y2, 0);
	}

	public static Point rotateEndOfLine(Line line, Angle angle) {

		Point vector = rotate2DVector(line.getVector(), angle);
		vector.setX(vector.getX() + line.getFrom().getX());
		vector.setY(vector.getY() + line.getFrom().getY());
		// TODO: Support Z axis

		return vector;
	}

	public static Line rotateLine(Line line, Angle angle) {
		return new Line(line.getFrom(), rotateEndOfLine(line, angle));
	}

	public static float gradToRad(double rad) {
		return (float) (rad * Math.PI / 180);
	}

	public static void updateGridDistance(Node gridNode, Node distanceNode, DistanceDetails distanceDetails) throws Exception {
		NodeTransformation relativeTransformation = VectorUtils.calculateRelativeTransformation(gridNode, distanceNode);

		GridGeometry gridGeometry = (GridGeometry) gridNode.getGeometry();

		double angle = relativeTransformation.getRotation().getZ();
		angle += distanceDetails.getNorthAngle().getRadians();
		Angle relativeAngle = new Angle(angle);

		DistanceDetails relativeDistanceDetail = new DistanceDetails();
		relativeDistanceDetail.setDistanceCm(distanceDetails.getDistanceCm());
		relativeDistanceDetail.setEndObstacle(distanceDetails.isEndObstacle());
		relativeDistanceDetail.setErrorAngle(distanceDetails.getErrorAngle());
		relativeDistanceDetail.setNorthAngle(relativeAngle);
		relativeDistanceDetail.setPositionOffset(relativeTransformation.getPosition());

		gridGeometry.addDistance(relativeDistanceDetail);
	}

	public static NodeTransformation calculateRelativeTransformation(Node nodeA, Node nodeB) throws Exception {

		if (nodeA == null || nodeB == null) {
			throw new Exception();
		}
		NodeTransformation cumulativeTransformation;
		if (nodeB.equals(nodeA)) {
			cumulativeTransformation = new NodeTransformation();
			cumulativeTransformation.resetToDefaults();
		} else {
			cumulativeTransformation = calculateRelativeTransformation(nodeA, nodeB.findParentNode());
			if (nodeB.getTransformation() != null) {
				Point posVector = nodeB.getTransformation().getPosition();
				if (cumulativeTransformation.getRotation().getZ() != 0) {
					Angle angle = new Angle(cumulativeTransformation.getRotation().getZ());
					posVector = VectorUtils.rotate2DVector(posVector, angle);
				}
				cumulativeTransformation.getPosition().add(posVector);
				cumulativeTransformation.getRotation().add(nodeB.getTransformation().getRotation());
			}
		}
		return cumulativeTransformation;
	}

}
