#include <jni.h>
#include <stdio.h>
#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <sys/time.h>  
#include <wiringPi.h>  
#include <iostream>
#include <string>
#include "AkibotJniLibrary.h"
#include "DistanceMeter.h"
#include "AkibotUtils.h"


DistanceMeter distanceMeter;
bool wiringPiInitialized = false;

class Exception {
public:
    Exception(const std::string& msg) : msg_(msg) {
        fprintf(stderr, "ERROR: %s\n", msg.c_str());
    }
    ~Exception() {
    }
    std::string getMessage() const {
        return (msg_);
    }
private:
    std::string msg_;
};

void checkWiringPiInitialized() throw() {
    if (!wiringPiInitialized) {
        throw (Exception("Wiring Pi is not initialized!\n"));
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
        throw (Exception("Can't initialize wiringPi: %s\n"));
    } else {
        fprintf(stdout, "Wiring Pi successfully initialized.\n");
        wiringPiInitialized = true;
    }
}
