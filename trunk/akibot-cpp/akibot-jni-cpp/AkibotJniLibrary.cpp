#include "Akibot.h"

DistanceMeterSRF05 distanceMeter;
bool wiringPiInitialized = false;



void checkWiringPiInitialized() {
    if (!wiringPiInitialized) {
        throw (AkibotException("Wiring Pi is not initialized!\n"));
    }
}

JNIEXPORT jint JNICALL Java_akibot_jni_java_AkibotJniLibrary_getDistance
(JNIEnv *env, jobject obj, jint triggerPin, jint echoPin, jint timeout) {
    checkWiringPiInitialized();
    if (!distanceMeter.isInitializedFor(triggerPin, echoPin)) {
        distanceMeter.initialize(triggerPin, echoPin);
    }

    return distanceMeter.getDistance();
}

JNIEXPORT jint JNICALL Java_akibot_jni_java_AkibotJniLibrary_pulseIn
(JNIEnv *env, jobject obj, jint pin, jint level) {
    checkWiringPiInitialized();
    return AkibotUtils::pulseIn(pin, level);
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
