import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.jme3.math.Vector3f;

public class ArrayFileParser {

	public List<Vector3f> loadPlotList(String filename) throws Exception {
		ArrayList<Vector3f> array = new ArrayList<Vector3f>();

		File inFile = new File(filename);
		Scanner in = new Scanner(inFile);

		while (in.hasNextLine()) {
			String line = in.nextLine();
			System.out.println("LINE: " + line);
			String[] currentLine = line.trim().split("\\s+");
			Vector3f plot = new Vector3f(Float.parseFloat(currentLine[0]), Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
			array.add(plot);
		}
		return array;
	}

	public static void main(String[] args) throws Exception {
		ArrayFileParser arrayFileParser = new ArrayFileParser();
		List<Vector3f> plotList = arrayFileParser.loadPlotList("calibration.txt");
		System.out.println("SIZE: " + plotList.size());
	}
}
