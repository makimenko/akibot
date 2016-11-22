package com.akibot.world.dao;

import com.akibot.common.element.Angle;
import com.akibot.common.element.Line2D;
import com.akibot.common.element.Point2D;
import com.akibot.common.utils.CommonUtilsException;

public interface GridGeometryDao {

	void startGridUpdate();

	void endGridUpdate();

	void addPoint2D(final Point2D point2D) throws CommonUtilsException;

	public void reset(int value);

	Line2D createLine2D(final Point2D startPoint2D, final Angle northAngle, final double distanceCm);

	void addLine2D(final Line2D line2D, final boolean endIsObstacle) throws CommonUtilsException;

	int[][] rasterize2D(final Line2D line, final boolean endIsObstacle) throws CommonUtilsException;

	int getAddressX(final Point2D point2D) throws CommonUtilsException;

	int getAddressY(final Point2D point) throws CommonUtilsException;

	void addLine2DWithAngle(final Line2D line2D, final Angle errorAngle, final boolean endIsObstacle)
			throws CommonUtilsException;

}
