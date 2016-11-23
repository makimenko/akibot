package com.akibot.app.logic;

public class Constants {

	public static final String NODE_NAME_WORLD = "world";
	public static final String NODE_NAME_GRID = "grid";
	public static final String NODE_NAME_ROBOT = "robot";
	public static final String NODE_NAME_GYROSCOPE = "gyroscope";
	public static final String NODE_NAME_ORIENTATION = "orientation";
	public static final String NODE_NAME_DISTANCE_CENTER = "distance.center";

	public static final int GRID_CELL_COUNT = 100;
	public static final int GRID_CELL_SIZE = 100;

	public static final long REALTIME_POOLING_TIME = 5000;
	public static final long REALTIME_NOTIFICATION_DELAY = 30000;

	public static final double GYROSCOPE_TOLERANCE = 0.5;
	public static final double ECHOLOCATOR_TOLERANCE = 50;
	public static final int GRID_MAX_OBSTACLE_COUNT = 10;
	public static final double ANGLE_TOLERANCE = Math.toRadians(5);
	public static final long DEFAULT_COMMAND_TIMEOUT = 5000;

}
