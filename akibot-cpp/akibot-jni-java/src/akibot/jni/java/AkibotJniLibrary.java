package akibot.jni.java;

public class AkibotJniLibrary {

    public native void initialize();

    public native int getDistance(int triggerPin, int echoPin, int timeoutMicroseconds);

    public native void servo(int servoPin, int initialValue, int pwmRange, int divisor, int value, int microseconds);
    
    public native int pulseIn(int pin, int level, int timeoutMicroseconds);
    
    public native void echoLocatorInitialize(int distanceTriggerPin, int distanceEchoPin, int distanceTimeout, int sleepBeforeDistance, int servoBasePin, int servoHeadPin, int servoLongTime, int servoStepTime, int distanceCount);
    
    public native float[] echoLocatorScanDistance(int servoBaseFrom, int servoBaseTo, int servoBaseStep, int servoHeadNormal, boolean trustToLastPosition);

 }
