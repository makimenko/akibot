package akibot.jni;

public class MyTest {
	static {
		System.loadLibrary("akibot-jni-distance-sr05");
	}

	native public static int distanceMicroseconds(int triggerPin, int echoPin, int timeout);
	

}
