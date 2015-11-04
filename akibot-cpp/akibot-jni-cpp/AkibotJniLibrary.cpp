#include "Akibot.h"

DistanceMeterSRF05 distanceMeter;
Servo servo;
EchoLocator echoLocator;
bool wiringPiInitialized = false;

void checkWiringPiInitialized() {
    if (!wiringPiInitialized) {
        throw (AkibotException("Wiring Pi is not initialized!\n"));
    }
}

JNIEXPORT jint JNICALL Java_akibot_jni_java_AkibotJniLibrary_getDistance
(JNIEnv *env, jobject obj, jint triggerPin, jint echoPin, jint timeoutMicroseconds) {
    checkWiringPiInitialized();
    if (!distanceMeter.isInitializedFor(triggerPin, echoPin, timeoutMicroseconds)) {
        distanceMeter.initialize(triggerPin, echoPin, timeoutMicroseconds);
    }

    return distanceMeter.getDistance();
}

JNIEXPORT jint JNICALL Java_akibot_jni_java_AkibotJniLibrary_pulseIn
(JNIEnv *env, jobject obj, jint pin, jint level, jint timeoutMicroseconds) {
    checkWiringPiInitialized();
    return AkibotUtils::pulseIn(pin, level, timeoutMicroseconds);
}

JNIEXPORT void JNICALL Java_akibot_jni_java_AkibotJniLibrary_initialize
(JNIEnv *env, jobject obj) {
    wiringPiInitialized = false;
    fprintf(stdout, "Initializing Wiring Pi...\n");

    if (wiringPiSetup() == -1) {
        throw (AkibotException("Can't initialize wiringPi: %s\n"));
    } else {
        fprintf(stdout, "Wiring Pi successfully initialized.\n");
        wiringPiInitialized = true;
    }
}

JNIEXPORT void JNICALL Java_akibot_jni_java_AkibotJniLibrary_servo
(JNIEnv *env, jobject obj, jint servoPin, jint initialValue, jint pwmRange, jint divisor, jint value, jint microseconds) {
    checkWiringPiInitialized();
    if (!servo.isInitializedFor(servoPin, initialValue, pwmRange, divisor)) {
        servo.initialize(servoPin, initialValue, pwmRange, divisor);
    }
    servo.softPwmWriteAndWait(value, microseconds);
}

JNIEXPORT void JNICALL Java_akibot_jni_java_AkibotJniLibrary_echoLocatorInitialize
(JNIEnv *env, jobject obj, jint distanceTriggerPin, jint distanceEchoPin, jint distanceTimeout, jint sleepBeforeDistance, jint servoBasePin, jint servoHeadPin, jint servoLongTime, jint servoStepTime, jint distanceCount) {
        echoLocator.initialize(distanceTriggerPin, distanceEchoPin, distanceTimeout, sleepBeforeDistance, servoBasePin, servoHeadPin, servoLongTime, servoStepTime, distanceCount);
}
  

JNIEXPORT jfloatArray JNICALL Java_akibot_jni_java_AkibotJniLibrary_echoLocatorScanDistance
(JNIEnv *env, jobject obj, jint servoBaseFrom, jint servoBaseTo, jint servoBaseStep, jint servoHeadNormal, jboolean trustToLastPosition) {

    float* scanResult = echoLocator.scanDistance(servoBaseFrom, servoBaseTo, servoBaseStep, servoHeadNormal, trustToLastPosition);
    int size = echoLocator.size;
    jfloatArray result;
    result = env->NewFloatArray(size);
    if (result == NULL) {
        return NULL; /* out of memory error thrown */
    }
    env->SetFloatArrayRegion(result, 0, size, scanResult);
    return result;
}
