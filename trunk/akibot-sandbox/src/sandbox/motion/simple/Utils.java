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
		//System.out.println("rows="+arr.length+"; cols="+arr[0].length);
		for(int r=0;r<=arr.length-1; r++ ) {
			for(int c=0;c<=arr[0].length-1; c++ ) {
			System.out.print(arr[r][c]+", ");
		}
			System.out.println();
		}
		System.out.println();
	}
	
}
