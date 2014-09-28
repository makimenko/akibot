package sandbox.motion.simple;

import java.util.Arrays;

public class Utils {

	public void printArray(int arr[]) {
		for (int c = 0; c <= arr.length - 1; c++) {
			System.out.print(arr[c] + ", ");
		}
		System.out.println();
		System.out.println();
	}

	public void printArray(int arr[][]) {
		// System.out.println("rows="+arr.length+"; cols="+arr[0].length);
		for (int r = 0; r <= arr.length - 1; r++) {
			for (int c = 0; c <= arr[0].length - 1; c++) {
				System.out.print(arr[r][c] + ", ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void updateValue(int arr[][], int defaultValue) {
		for (int r = 0; r <= arr.length - 1; r++)
			for (int c = 0; c <= arr[0].length - 1; c++) {
				arr[r][c] = defaultValue;
			}
	}

	public <T> T[] append(T[] arr, T element) {
		final int N = arr.length;
		arr = Arrays.copyOf(arr, N + 1);
		arr[N] = element;
		return arr;
	}

	public <T> T[] deleteLast(T[] arr) {
		final int N = arr.length;
		arr = Arrays.copyOf(arr, N - 1);
		return arr;
	}

}
