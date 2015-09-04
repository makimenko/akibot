package com.akibot.tanktrack.component.world.element;

public class ArrayUtils {

	public static void printArray(int arr[][]) {
		int numX = arr.length;
		int numY = arr[0].length;

		for (int y = numY - 1; y >= 0; y--) {
			for (int x = 0; x < numX; x++) {
				System.out.print(arr[x][y] + ", ");
			}
			System.out.println();
		}

		System.out.println();
	}

	public static void updateValue(int arr[][], int defaultValue) {
		for (int x = 0; x <= arr.length - 1; x++)
			for (int y = 0; y <= arr[0].length - 1; y++) {
				arr[x][y] = defaultValue;
			}
	}

}
