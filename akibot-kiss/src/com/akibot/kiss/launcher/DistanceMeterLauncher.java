package com.akibot.kiss.launcher;

import com.akibot.kiss.component.ComponentController;
import com.akibot.kiss.component.DistanceMeter;

public class DistanceMeterLauncher {
	
	public static void main(String args[]) throws Exception {
		
		DistanceMeter distanceMeter = new DistanceMeter();
		ComponentController distanceMeterController = new ComponentController("localhost", 2002, distanceMeter);
		
		distanceMeterController.run();
		
	}
}
