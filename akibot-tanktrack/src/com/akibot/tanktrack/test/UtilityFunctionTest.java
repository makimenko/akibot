package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.akibot.tanktrack.component.distance.DistanceDetails;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.orientation.RoundRobinUtils;
import com.akibot.tanktrack.component.world.element.Angle;
import com.akibot.tanktrack.component.world.element.ArrayUtils;
import com.akibot.tanktrack.component.world.element.GridConfiguration;
import com.akibot.tanktrack.component.world.element.GridGeometry;
import com.akibot.tanktrack.component.world.element.Line;
import com.akibot.tanktrack.component.world.element.Node;
import com.akibot.tanktrack.component.world.element.NodeTransformation;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.VectorUtils;
import com.akibot.tanktrack.component.world.exception.OutsideWorldException;

public class UtilityFunctionTest {

	private RoundRobinUtils robinUtils = new RoundRobinUtils(360);
	private final double ANGLE_PRECISSION = 0.0000000001;

	@Test
	public void roundRobinAdd() {
		assertEquals(0, robinUtils.add(0, 0), 0);
		assertEquals(0, robinUtils.add(0, 360), 0);
		assertEquals(0, robinUtils.add(360, 0), 0);
		assertEquals(0, robinUtils.add(360, 360), 0);
		assertEquals(0, robinUtils.add(0, 720), 0);
		assertEquals(2, robinUtils.add(1, 1), 0);
		assertEquals(271, robinUtils.add(270, 1), 0);
		assertEquals(1, robinUtils.add(180, 181), 0);
		assertEquals(1, robinUtils.add(1, 720), 0);
		assertEquals(358, robinUtils.add(-1, -1), 0);
		assertEquals(10, robinUtils.add(350, 20), 0);
		assertEquals(350, robinUtils.add(10, -20), 0);
		assertEquals(0, robinUtils.add(180.25, 179.75), 0);
		assertEquals(180.75, robinUtils.add(180.5, 0.25), 0);
	}

	@Test
	public void roundRobinRightDistance() {
		assertEquals(270, robinUtils.rightDistance(180, 90), 0);
		assertEquals(1, robinUtils.rightDistance(180, 181), 0);
		assertEquals(359, robinUtils.rightDistance(181, 180), 0);
		assertEquals(0, robinUtils.rightDistance(180, 180), 0);
		assertEquals(61, robinUtils.rightDistance(299, 0), 0);

		assertEquals(359, robinUtils.rightDistance(330, 329), 0);

	}

	@Test
	public void roundRobinLeftDistance() {
		assertEquals(270, robinUtils.leftDistance(180, 270), 0);
		assertEquals(1, robinUtils.leftDistance(180, 179), 0);
		assertEquals(359, robinUtils.leftDistance(179, 180), 0);
		assertEquals(0, robinUtils.leftDistance(180, 180), 0);
		assertEquals(1, robinUtils.leftDistance(330.000000001, 329.000000001), 0);
	}

	@Test
	public void roundRobinModularDistance() {
		assertEquals(10, robinUtils.modularDistance(80, 90), 0);
		assertEquals(10, robinUtils.modularDistance(90, 80), 0);
		assertEquals(10, robinUtils.modularDistance(355, 5), 0);
		assertEquals(10, robinUtils.modularDistance(5, 355), 0);
		assertEquals(0, robinUtils.modularDistance(-5, 355), 0);
		assertEquals(0, robinUtils.modularDistance(1, 1), 0);
	}

	@Test
	public void gridAddPoint() throws OutsideWorldException {
		int maxObstacle = 2;
		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(6, 5, 10, maxObstacle));
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);

		akiGrid.addPoint(new Point(0, 0, 0));
		assertEquals(1, akiGrid.getGrid()[0][0]);

		akiGrid.addPoint(new Point(10, 9, 0));
		assertEquals(1, akiGrid.getGrid()[1][0]);
		akiGrid.addPoint(new Point(11, 9, 0));
		assertEquals(2, akiGrid.getGrid()[1][0]);

		assertEquals(3, akiGrid.getChangeSequence());

		akiGrid.addPoint(new Point(11, 9, 0));
		assertEquals(maxObstacle, akiGrid.getGrid()[1][0]);
		assertEquals(3, akiGrid.getChangeSequence());

	}

	@Test
	public void gridAddLine() throws OutsideWorldException {
		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(6, 5, 10, 2));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[5][4]);
		assertEquals(0, akiGrid.getChangeSequence());

		akiGrid.addLine(new Line(new Point(0, 0, 0), new Point(0, 0, 0)), true);
		assertEquals(1, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getChangeSequence());

		akiGrid.addLine(new Line(new Point(21, 21, 0), new Point(29, 49, 0)), true);

		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][2]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][3]);
		assertEquals(1, akiGrid.getGrid()[2][4]);
		assertEquals(4, akiGrid.getChangeSequence());

		akiGrid.addLine(new Line(new Point(21, 21, 0), new Point(29, 49, 0)), true);

		// System.out.println("After");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][2]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][3]);
		assertEquals(2, akiGrid.getGrid()[2][4]);
		assertEquals(5, akiGrid.getChangeSequence());

		akiGrid.addLine(new Line(new Point(45, 5, 0), new Point(55, 5, 0)), false);

		// System.out.println("After 2");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[4][0]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[5][0]);
		assertEquals(7, akiGrid.getChangeSequence());

		// Make sure that other Cells are not affected
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][1]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][3]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][4]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[3][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[3][3]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[3][4]);

	}

	@Test
	public void gridAddLineNarrowAngle() throws OutsideWorldException {
		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(2, 3, 10, 1));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		Angle angle = new Angle();
		angle.setDegrees(5);
		Line line = new Line(new Point(5, 15, 0), new Point(15, 15, 0));
		akiGrid.addLineWithAngle(line, angle, true);

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);

		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(1, akiGrid.getGrid()[1][1]);

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][0]);
		assertEquals(2, akiGrid.getChangeSequence());
	}

	@Test
	public void gridAddLineWideAngle() throws OutsideWorldException {
		// TODO: Implement

		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(2, 3, 10, 2));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		Angle angle = new Angle();
		angle.setDegrees(45);
		Line line = new Line(new Point(5, 15, 0), new Point(15, 15, 0));
		akiGrid.addLineWithAngle(line, angle, true);
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(1, akiGrid.getGrid()[1][2]);

		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(1, akiGrid.getGrid()[1][1]);

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getGrid()[1][0]);
		assertEquals(4, akiGrid.getChangeSequence());
	}

	@Test
	public void gridAddDistance0Angle() throws OutsideWorldException {
		// TODO: Implement

		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(3, 3, 10, 1));

		Point positionOffset = new Point(15, 15, 0);
		Angle northAngle = new Angle();
		northAngle.setDegrees(-90);

		Angle errorAngle = new Angle();
		errorAngle.setDegrees(1);

		akiGrid.addDistance(new DistanceDetails(positionOffset, northAngle, errorAngle, 10, true));
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][2]);

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[1][1]);
		assertEquals(1, akiGrid.getGrid()[2][1]);

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][0]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][0]);

		assertEquals(2, akiGrid.getChangeSequence());
	}

	@Test
	public void gridAddDistance45Angle() throws OutsideWorldException {
		// TODO: Implement

		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(3, 3, 10, 1));

		Point positionOffset = new Point(15, 15, 0);
		Angle northAngle = new Angle();
		northAngle.setDegrees(180);

		Angle errorAngle = new Angle();
		errorAngle.setDegrees(45);

		akiGrid.addDistance(new DistanceDetails(positionOffset, northAngle, errorAngle, 10, true));
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][2]);

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[1][1]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][1]);

		assertEquals(1, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getGrid()[1][0]);
		assertEquals(1, akiGrid.getGrid()[2][0]);

		assertEquals(4, akiGrid.getChangeSequence());
	}

	@Test
	public void gridRotateVector() {
		float ix0 = 3;
		float iy0 = 2;

		float ix1 = 7;
		float iy1 = 6;

		Line line = new Line();
		line.setFrom(new Point(ix0, iy0, 0));
		line.setTo(new Point(ix1, iy1, 0));

		Point result;

		// Same line
		Angle angle0 = new Angle();
		angle0.setDegrees(0);
		result = VectorUtils.rotateEndOfLine(line, angle0);
		assertEquals(ix1, result.getX(), ANGLE_PRECISSION);
		assertEquals(iy1, result.getY(), ANGLE_PRECISSION);

		// 90 degrees to the left
		Angle angleLeft90 = new Angle();
		angleLeft90.setDegrees(90);
		result = VectorUtils.rotateEndOfLine(line, angleLeft90);
		assertEquals(iy1, result.getY(), ANGLE_PRECISSION);
		assertEquals(true, result.getX() < 0);

		// 90 degrees to the right
		Angle angleRight90 = new Angle();
		angleRight90.setDegrees(-90);
		result = VectorUtils.rotateEndOfLine(line, angleRight90);
		assertEquals(ix1, result.getX(), ANGLE_PRECISSION);
		assertEquals(true, result.getY() < 0);

		// same, 90 degrees to the right (but via negative angle)
		result = VectorUtils.rotateEndOfLine(line, angleLeft90.calculateNegativeAngle());
		assertEquals(ix1, result.getX(), ANGLE_PRECISSION);
		assertEquals(true, result.getY() < 0);

	}

	@Test
	public void gridCalculateLine() {

		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(1, 1, 1, 1));
		double x0 = 1;
		double y0 = 2;
		double distanceCm = 10;
		Point positionOffset = new Point(x0, y0, 0);

		Line result;

		// Same line
		Angle angle0 = new Angle();
		angle0.setDegrees(0);
		result = akiGrid.calculateNorthLine(positionOffset, angle0, distanceCm);
		assertEquals(x0, result.getFrom().getX(), ANGLE_PRECISSION);
		assertEquals(y0, result.getFrom().getY(), ANGLE_PRECISSION);
		assertEquals(x0, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(y0 + distanceCm, result.getTo().getY(), ANGLE_PRECISSION);

		// Opposite direction (180 degrees)
		Angle angle180 = new Angle();
		angle180.setDegrees(180);
		result = akiGrid.calculateNorthLine(positionOffset, angle180, distanceCm);
		assertEquals(x0, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(y0 - distanceCm, result.getTo().getY(), ANGLE_PRECISSION);

		// 90 Degrees
		Angle angle90 = new Angle();
		angle90.setDegrees(90);
		result = akiGrid.calculateNorthLine(positionOffset, angle90, distanceCm);
		assertEquals(x0 - distanceCm, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(y0, result.getTo().getY(), ANGLE_PRECISSION);

	}

	@Test
	public void gridDistanceErrorAngle() throws OutsideWorldException {

		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(5, 3, 10, 1));

		akiGrid.addDistance(new DistanceDetails(new Point(25, 25, 0), new Angle(Math.toRadians(180)), new Angle(Math.toRadians(45)), 25, true));
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[3][2]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[4][2]);

		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[1][1]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][1]);
		assertEquals(GridGeometry.EMPTY_VALUE, akiGrid.getGrid()[3][1]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[4][1]);

		assertEquals(1, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getGrid()[1][0]); // test goal
		assertEquals(1, akiGrid.getGrid()[2][0]);
		assertEquals(1, akiGrid.getGrid()[3][0]); // test goal
		assertEquals(1, akiGrid.getGrid()[4][0]);
	}

	@Test
	public void gridRasterize() throws OutsideWorldException {

		GridGeometry akiGrid = new GridGeometry(new GridConfiguration(4, 4, 1, 1));

		int[][] res = akiGrid.rasterize(new Line(new Point(3, 3, 0), new Point(1, 1, 0)), true);

		ArrayUtils.printArray(res);

		assertEquals(3, res[0][0]);
		assertEquals(3, res[0][1]);

		assertEquals(2, res[1][0]);
		assertEquals(2, res[1][1]);

		assertEquals(1, res[2][0]);
		assertEquals(1, res[2][1]);

	}

	@Test
	public void relativeTransformation() throws Exception {

		Node worldNode = new Node("worldNode");

		Node gridNode = new Node("gridNode");

		worldNode.attachChild(worldNode);

		Node robotNode = new Node("robotNode");
		NodeTransformation robotTransformation = new NodeTransformation();
		robotTransformation.setPosition(new Point(3, 2, 0));
		robotTransformation.setRotation(new Point(0, 0, VectorUtils.gradToRad(45)));
		robotNode.setTransformation(robotTransformation);
		gridNode.attachChild(robotNode);

		Node distanceNode = new Node("distanceNode");
		NodeTransformation distanceTransformation = new NodeTransformation();
		distanceTransformation.setPosition(new Point(0, 2, 0));
		distanceTransformation.setRotation(new Point(0, 0, VectorUtils.gradToRad(5)));
		distanceNode.setTransformation(distanceTransformation);
		robotNode.attachChild(distanceNode);

		NodeTransformation relativeTransformation = VectorUtils.calculateRelativeTransformation(gridNode, distanceNode);

		double COORD_PRECISSION = 0.01;
		double ROTATION_PRECISSION = 0.0000001;

		Point relativePosition = relativeTransformation.getPosition();
		Point relativeRotation = relativeTransformation.getRotation();
		Point relativeScale = relativeTransformation.getScale();

		assertEquals(1.58, relativePosition.getX(), COORD_PRECISSION);
		assertEquals(3.41, relativePosition.getY(), COORD_PRECISSION);
		assertEquals(0, relativePosition.getZ(), 0);

		assertEquals(0, relativeRotation.getX(), 0);
		assertEquals(0, relativeRotation.getY(), 0);
		assertEquals(VectorUtils.gradToRad(50), relativeRotation.getZ(), ROTATION_PRECISSION);

		assertEquals(1, relativeScale.getX(), 0);
		assertEquals(1, relativeScale.getY(), 0);
		assertEquals(1, relativeScale.getZ(), 0);

		// ------- TEST: Grid + Distance
		int maxObstacle = 1;
		GridGeometry gridGeometry = new GridGeometry(new GridConfiguration(10, 10, 1, maxObstacle, new Point(-5, -5, 0)));
		gridNode.setGeometry(gridGeometry);

		gridGeometry.addLine(new Line(new Point(0, 0, 0), new Point(0, 0, 0)), true);

		// TODO: Test Distance!
		DistanceDetails distanceDetails = new DistanceDetails(2, true);

		VectorUtils.updateGridDistance(gridNode, distanceNode, distanceDetails);
		// ArrayUtils.printArray(gridGeometry.getGrid());

		assertEquals(GridGeometry.UNKNOWN_VALUE, gridGeometry.getGrid()[4][9]);
		assertEquals(maxObstacle, gridGeometry.getGrid()[5][9]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, gridGeometry.getGrid()[6][9]);

		assertEquals(GridGeometry.UNKNOWN_VALUE, gridGeometry.getGrid()[5][8]);
		assertEquals(GridGeometry.EMPTY_VALUE, gridGeometry.getGrid()[6][8]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, gridGeometry.getGrid()[7][8]);

		assertEquals(GridGeometry.UNKNOWN_VALUE, gridGeometry.getGrid()[6][7]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, gridGeometry.getGrid()[7][7]);
		assertEquals(GridGeometry.UNKNOWN_VALUE, gridGeometry.getGrid()[8][7]);

	}

	@Test
	public void gridOffset() throws OutsideWorldException {
		GridGeometry gridOffset = new GridGeometry(new GridConfiguration(2, 2, 10, 1, new Point(-10, -10, 0)));
		assertEquals(1, gridOffset.getAddressX(new Point(5, 4, 0)));
		assertEquals(1, gridOffset.getAddressY(new Point(5, 4, 0)));
		assertEquals(0, gridOffset.getAddressX(new Point(-5, -4, 0)));
		assertEquals(0, gridOffset.getAddressY(new Point(-5, -4, 0)));
		gridOffset.addPoint(new Point(5, 5, 0));
		assertEquals(1, gridOffset.getGrid()[1][1]);
	}

	@Test
	public void gridOutsideWorld1() throws OutsideWorldException {
		GridGeometry grid = new GridGeometry(new GridConfiguration(1, 1, 1, 1));
		grid.addPoint(new Point(0, 0, 0));
		assertEquals(1, grid.getGrid()[0][0]);
		try {
			grid.addPoint(new Point(1, 1, 0));
			assertEquals(false, true);
		} catch (OutsideWorldException e) {
		}
	}

	@Test
	public void gridOutsideWorld2() throws OutsideWorldException {
		GridGeometry grid = new GridGeometry(new GridConfiguration(2, 2, 10, 1));
		grid.addPoint(new Point(0, 0, 0));
		grid.addPoint(new Point(19, 19, 0));
		try {
			grid.addPoint(new Point(20, 20, 0));
			assertEquals(false, true);
		} catch (OutsideWorldException e) {
		}
		try {
			grid.addPoint(new Point(-1, -1, 0));
			assertEquals(false, true);
		} catch (OutsideWorldException e) {
		}
	}
	

}
