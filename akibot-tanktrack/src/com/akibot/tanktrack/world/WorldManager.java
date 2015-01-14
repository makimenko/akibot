package com.akibot.tanktrack.world;

public class WorldManager {

	public static void main(String[] args) {

		WorldNode worldNode = new WorldNode("World");

		Node locationNode = new Node("Riga");
		worldNode.attachChild(locationNode);

		Node tankNode = new Node("TankTrack");
		locationNode.attachChild(tankNode);

	}
}
