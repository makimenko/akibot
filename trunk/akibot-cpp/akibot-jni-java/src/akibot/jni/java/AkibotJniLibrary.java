package akibot.jni.java;

public class AkibotJniLibrary {

    public native void initialize();

    public native int getDistance(int triggerPin, int echoPin, int timeout);

    public native int pulseIn(int pin, int level);

}
