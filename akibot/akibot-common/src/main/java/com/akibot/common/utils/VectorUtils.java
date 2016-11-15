package com.akibot.common.utils;

import com.akibot.common.element.Angle;
import com.akibot.common.element.Line2D;
import com.akibot.common.element.Point2D;
import com.akibot.common.element.Vector2D;
import com.akibot.common.element.Vector3D;

/**
 * Trigonometry functions for Vector, Angle, Point and Line manipulations.
 * 
 * @author Mihail
 *
 */
public class VectorUtils {
	private Angle offsetNorthAngle = new Angle(Math.toRadians(-90));

	/**
	 * Rotate {@link Vector2D} by {@link Angle}
	 * 
	 * @param vector2D
	 *            Vector to be rotated
	 * @param angle
	 *            Angle value for rotation
	 * @return Result value of rotated {@link Vector2D}
	 */
	public Vector2D rotate2DVector(final Vector2D vector2D, final Angle angle) {
		if (angle == null) {
			return vector2D;
		} else {
			final double x1 = vector2D.getX();
			final double y1 = vector2D.getY();
			final double angleRadians = angle.getRadians();

			final double x2 = x1 * Math.cos(angleRadians) - y1 * Math.sin(angleRadians);
			final double y2 = y1 * Math.cos(angleRadians) + x1 * Math.sin(angleRadians);

			return new Vector2D(x2, y2);
		}
	}

	/**
	 * Rotates end of Line {@link Line2D#to} by {@link Angle} value.
	 * 
	 * @param line2D
	 *            Line to be rotated
	 * @param angle
	 *            Value of Rotation Angle
	 * @return Result of rotation {@link Point2D} (end of line).
	 */
	public Point2D rotateEndOfLine2D(final Line2D line2D, final Angle angle) {
		final Vector2D vector = rotate2DVector(line2D.getVector(), angle);

		final Point2D resultPoint2D = new Point2D();
		resultPoint2D.setX(vector.getX() + line2D.getFrom().getX());
		resultPoint2D.setY(vector.getY() + line2D.getFrom().getY());

		return resultPoint2D;
	}

	/**
	 * Rotates end of Line {@link Line2D#to} by {@link Angle} value. The
	 * beginning of Line {@link Line2D#from} stays unchanged
	 * 
	 * @param line2D
	 *            Line to be rotated
	 * @param angle
	 *            Value of Rotation Angle
	 * @return Rotation result as {@link Line2D}
	 */
	public Line2D rotateLine2D(final Line2D line2D, final Angle angle) {
		return new Line2D(line2D.getFrom(), rotateEndOfLine2D(line2D, angle));
	}

	/**
	 * 
	 * @param vector3d
	 *            output from gyroscope
	 * @return
	 */
	public Angle getNorthAngle(Vector3D vector3d) {
		double radians = Math.atan2(vector3d.getY(), vector3d.getX());
		Angle angle = new Angle(radians);
		angle.add(offsetNorthAngle);
		return angle;
	}

}
