package sandbox.motion.simple;

import java.util.Arrays;

public class SimpleMotionPlanning {
	
	public static void main(String[] args ) {

		SimpleMotionPlanning simpleMotionPlanning = new SimpleMotionPlanning();
		simpleMotionPlanning.start();
		
	}
	
	
	public void start() {
		Utils utils = new Utils();
		
		int grid[][] = new int[][]{
				{0, 0, 1, 0, 0, 0},
				{0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0, 1, 0},
				{0, 0, 1, 1, 1, 0},
				{0, 0, 0, 0, 1, 0}		
		};
		System.out.println("GRID:");
		utils.printArray(grid);
		
		int init[] = new int[]{0,0};
		int goal[] = new int[]{grid.length-1, grid[0].length-1};
		
		System.out.println("INIT:");
		utils.printArray(init);
		
		System.out.println("GOAL:");
		utils.printArray(goal);

		
		
		int delta[][] = new int[][] {
				{-1, 0},
				{0, -1},
				{1, 0},
				{0, 1}
		};
		char delta_name[] = new char[] {'^', '<', 'v', '>'};
		
		int cost = 1;
		
		int closed[][] = new int[grid.length][grid[0].length];
		/*for (int c=0; c<=grid.length-1; c++) {
			for (int r=0; r<=grid[0].length-1; r++) {
				closed[c][r] = 1;
			}
		}*/
		closed[init[0]][init[1]] = 1;
		
		System.out.println("CLOSED:");
		utils.printArray(closed);
		
		
		int x=0;
		int y=0;
		int g = 0;

		
		int open[][] = new int[1][3];
		System.out.println("INITIAL OPEN:");
		utils.printArray(open);
		

		
	}
	
}
