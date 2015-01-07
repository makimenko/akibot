package akibot.jni.java;

public class AkibotJniLibrary {

    public native int getDistance(int triggerPin, int echoPin, int timeout);

    public native int getDistance(int pin, int level);

}
