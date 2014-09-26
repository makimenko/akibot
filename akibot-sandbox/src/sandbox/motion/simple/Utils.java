package sandbox.motion.simple;

public class Utils {

	public void printArray(int arr[]) {
		for(int c=0;c<=arr.length-1; c++ ) {
			System.out.print(arr[c]+", ");
		}
		System.out.println();
		System.out.println();
	}
	
	public void printArray(int arr[][]) {
		for(int c=0;c<=arr.length-1; c++ ) {
			for(int r=0;r<=arr[0].length-1; r++ ) {
			System.out.print(arr[c][r]+", ");
		}
			System.out.println();
		}
		System.out.println();
			
	}
	
}
