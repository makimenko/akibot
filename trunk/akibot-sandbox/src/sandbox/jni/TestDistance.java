package sandbox.jni;

import akibot.jni.java.AkibotJniLibrary;

public class TestDistance {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("STARTING....");
		AkibotJniLibrary lib = new AkibotJniLibrary();
		lib.initialize();
		for (int i = 0; i <= 100; i++) {
			int cm = lib.getDistance(25, 27, 50000);
			System.out.println("Distance is: " + cm + " cm");
			Thread.sleep(100);
		}
		System.out.println("END");
	}

}
