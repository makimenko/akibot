package sandbox.jni;

import akibot.jni.java.AkibotJniLibrary;

public class TestEchoLocator {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("STARTING....");
		AkibotJniLibrary lib = new AkibotJniLibrary();
		lib.initialize();
		float[] result;
		
		System.out.println("===============");
		result = lib.echoLocator(13, 12, 500000, 50000, 0, 7, 4, 24, 1, 14, 400000, 35000, 1, true);		
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}

		System.out.println("===============");
		result = lib.echoLocator(13, 12, 500000, 50000, 0, 7, 24, 4, 1, 14, 400000, 35000, 1, true);		
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
		
		System.out.println("===============");
		result = lib.echoLocator(13, 12, 500000, 50000, 0, 24, 7, 14, 1, 14, 400000, 35000, 1, true);		
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
		
		System.out.println("END");
	}

}
