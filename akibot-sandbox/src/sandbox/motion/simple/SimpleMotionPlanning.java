package sandbox.motion.simple;

import java.util.Arrays;

public class SimpleMotionPlanning {

	public static void main(String[] args) {
		SimpleMotionPlanning simpleMotionPlanning = new SimpleMotionPlanning();
		simpleMotionPlanning.start();
	}

		
	public void start() {
		Utils utils = new Utils();

		int grid[][] = new int[][] { 
				{ 0, 0, 1, 0, 0, 0 }, 
				{ 0, 0, 1, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 1, 0 }, 
				{ 0, 0, 1, 1, 1, 0 }, 
				{ 0, 0, 0, 0, 1, 0 } 
		};
		
	
		System.out.println("GRID:");
		utils.printArray(grid);

		int init[] = new int[] { 0, 0 };
		int goal[] = new int[] { grid.length - 1, grid[0].length - 1 };

		System.out.println("INIT:");
		utils.printArray(init);

		System.out.println("GOAL:");
		utils.printArray(goal);

		int delta[][] = new int[][] { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
		

		int cost = 1;

		int closed[][] = new int[grid.length][grid[0].length];
		closed[init[0]][init[1]] = 1;

		int expand[][] = new int[grid.length][grid[0].length];
		utils.updateValue(expand, -1);

		int action[][] = new int[grid.length][grid[0].length];
		utils.updateValue(action, -1);

		//System.out.println("CLOSED:");
		//utils.printArray(closed);

		int x = 0;
		int y = 0;
		int g = 0;

		int open[][] = new int[][] { { g, x, y } };
		expand[x][y] = 0;
		int count = 0;

		//System.out.println("INITIAL OPEN:");
		//utils.printArray(open);

		boolean found = false;
		boolean resign = false;

		while (!found && !resign) {

			if (open.length == 0) {
				resign = true;
				break;
			} else {
				Arrays.sort(open, new AscendingComparator());
				int[] next = open[open.length - 1];
				open = Arrays.copyOf(open, open.length - 1);

				//System.out.println("take list item");
				//utils.printArray(next);
				x = next[1];
				y = next[2];
				g = next[0];
				expand[x][y] = count;
				count++;

				if (x == goal[0] && y == goal[1]) {
					found = true;
				} else {

					for (int i = 0; i < delta.length; i++) {
						int x2 = x + delta[i][0];
						int y2 = y + delta[i][1];

						if (x2 >= 0 && x2 < grid.length && y2 >= 0 && y2 < grid[0].length) {
							if (closed[x2][y2] == 0 && grid[x2][y2] == 0) {
								int g2 = g + cost;
								open = utils.append(open, new int[] { g2, x2, y2 });
								closed[x2][y2] = 1;
								action[x2][y2] = i;
							}
						}
					}

					//System.out.println("new open list:");
					//utils.printArray(open);
				}
			}
		}

		System.out.println("Found=" + found);

		if (found) {
			System.out.println("EXPAND:");
			utils.printArray(expand);

			int policy[][] = new int[grid.length][grid[0].length];
			utils.updateValue(policy, -1);
			x = goal[0];
			y = goal[1];
			policy[x][y] = 9;
			while (x != init[0] || y != init[1]) {
				int x2 = x - delta[action[x][y]][0];
				int y2 = y - delta[action[x][y]][1];
				policy[x2][y2] = action[x][y];
				x = x2;
				y = y2;
			}

			System.out.println("POLICY:");
			utils.printArray(policy);

		}

	}

}
