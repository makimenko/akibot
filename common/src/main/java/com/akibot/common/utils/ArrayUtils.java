package com.akibot.common.utils;

import java.io.PrintStream;

/**
 * Array Utilities
 * 
 * @author Mihail
 *
 */
public class ArrayUtils {

	/**
	 * Prints array into {@link PrintStream}
	 * 
	 * @param printStream
	 *            {@link PrintStream} for printing the array
	 * @param arr
	 *            2-dimensional array to be printed
	 */
	public void printArray(final PrintStream printStream, final int[][] arr) {
		for (int y = arr[0].length - 1; y >= 0; y--) {
			for (int x = 0; x < arr.length; x++) {
				printStream.print(arr[x][y]);
				printStream.print(", ");
			}
			printStream.println();
		}
		printStream.println();
	}

	/**
	 * Update all cells of array and set to default value
	 * 
	 * @param arr
	 *            2-dimensional array
	 * @param defaultValue
	 *            value to be set in each array element
	 */
	public void updateValue(int arr[][], final int defaultValue) {
		for (int x = 0; x <= arr.length - 1; x++) {
			for (int y = 0; y <= arr[0].length - 1; y++) {
				arr[x][y] = defaultValue;
			}
		}
	}

	public long countIf(int arr[][], final int value) {
		long count = 0;
		for (int x = 0; x <= arr.length - 1; x++) {
			for (int y = 0; y <= arr[0].length - 1; y++) {
				if (arr[x][y] == value) {
					count++;
				}
			}
		}
		return count;
	}

}
